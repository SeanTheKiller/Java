package customer;

import common.Appointment;
import common.Comment;
import common.EditProfileDialog;
import common.FileUtil;
import common.Payment;
import common.User;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Main window for a logged-in customer. One tab per requirement:
 *   My Profile | Service History | Payments | Feedback | Comments
 */
public class CustomerFrame extends JFrame {

    private User user;                       // the logged-in customer
    private CustomerFunctions functions;     // does all the file work

    // Profile tab labels
    private JLabel usernameLabel = new JLabel();
    private JLabel phoneLabel = new JLabel();
    private JLabel ageLabel = new JLabel();
    private JLabel fullNameLabel = new JLabel();
    private JLabel emailLabel = new JLabel();
    private JLabel addressLabel = new JLabel();

    // Tables (each needs a "model" that holds its rows)
    private DefaultTableModel historyModel = makeModel("Appointment ID", "Car Plate", "Service", "Date & Time", "Technician", "Status");
    private DefaultTableModel paymentModel = makeModel("Appointment ID", "Amount (RM)", "Date", "Method", "Collected By");
    private DefaultTableModel feedbackModel = makeModel("Appointment ID", "Service", "Technician", "Status", "Your Issue", "Technician Feedback");
    private DefaultTableModel commentModel = makeModel("Comment ID", "Appointment ID", "About", "Staff", "Comment", "Date");
    private JLabel totalPaidLabel = new JLabel();

    // Comments tab inputs
    private JComboBox<String> appointmentBox = new JComboBox<>();
    private JComboBox<String> staffBox = new JComboBox<>();
    private JTextArea commentArea = new JTextArea(4, 40);

    public CustomerFrame(User user) {
        this.user = user;
        this.functions = new CustomerFunctions(user.getUsername());

        setTitle("APU-ASC | Customer: " + user.getUsername());
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("My Profile", buildProfileTab());
        tabs.addTab("Service History", new JScrollPane(new JTable(historyModel)));
        tabs.addTab("Payments", buildPaymentsTab());
        tabs.addTab("Feedback", new JScrollPane(new JTable(feedbackModel)));
        tabs.addTab("Comments", buildCommentsTab());

        // bottom bar: Refresh + Logout
        JButton refreshBtn = new JButton("Refresh");
        JButton logoutBtn = new JButton("Logout");
        refreshBtn.addActionListener(e -> refreshAll());
        logoutBtn.addActionListener(e -> logout());
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(refreshBtn);
        bottom.add(logoutBtn);

        add(tabs, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        refreshAll();
    }

    // ------------------------------------------------------------------
    // Building each tab
    // ------------------------------------------------------------------

    private JPanel buildProfileTab() {
        JPanel info = new JPanel(new GridLayout(0, 2, 10, 10));
        info.add(new JLabel("Username:"));
        info.add(usernameLabel);
        info.add(new JLabel("Phone Number:"));
        info.add(phoneLabel);
        info.add(new JLabel("Age:"));
        info.add(ageLabel);
        info.add(new JLabel("Name:"));
        info.add(fullNameLabel);
        info.add(new JLabel("Email:"));
        info.add(emailLabel);
        info.add(new JLabel("Home Address:"));
        info.add(addressLabel);
        info.add(new JLabel("Role:"));
        info.add(new JLabel("Customer"));

        JButton editBtn = new JButton("Edit Profile");
        editBtn.addActionListener(e -> editProfile());

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(info, BorderLayout.NORTH);
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnRow.add(editBtn);
        panel.add(btnRow, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildPaymentsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(new JTable(paymentModel)), BorderLayout.CENTER);
        panel.add(totalPaidLabel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildCommentsTab() {
        // when the chosen appointment changes, refill the staff list
        appointmentBox.addActionListener(e -> fillStaffBox());

        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);

        JButton submitBtn = new JButton("Submit Comment");
        submitBtn.addActionListener(e -> submitComment());

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.add(new JLabel("Completed appointment:"));
        form.add(appointmentBox);
        form.add(new JLabel("Staff member:"));
        form.add(staffBox);

        JPanel top = new JPanel(new BorderLayout(5, 5));
        top.setBorder(BorderFactory.createTitledBorder("Leave a comment"));
        top.add(form, BorderLayout.NORTH);
        top.add(new JScrollPane(commentArea), BorderLayout.CENTER);
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRow.add(submitBtn);
        top.add(btnRow, BorderLayout.SOUTH);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBorder(BorderFactory.createTitledBorder("My previous comments"));
        bottom.add(new JScrollPane(new JTable(commentModel)), BorderLayout.CENTER);

        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.add(top);
        panel.add(bottom);
        return panel;
    }

    // ------------------------------------------------------------------
    // Loading data into the screen
    // ------------------------------------------------------------------

    private void refreshAll() {
        usernameLabel.setText(user.getUsername());
        phoneLabel.setText(user.getPhoneNumber());
        ageLabel.setText(String.valueOf(user.getAge()));
        fullNameLabel.setText(user.getName());
        emailLabel.setText(user.getEmail());
        addressLabel.setText(user.getHomeAddress());

        // Service history
        historyModel.setRowCount(0);   // clear old rows
        for (Appointment a : functions.getServiceHistory()) {
            historyModel.addRow(new Object[]{a.getAppointmentId(), a.getCarPlate(), a.getServiceType(),
                    a.getDatetime().replace("T", " "), a.getTechnician(), a.getStatus()});
        }

        // Payments
        paymentModel.setRowCount(0);
        for (Payment p : functions.getPaymentHistory()) {
            paymentModel.addRow(new Object[]{p.getAppointmentId(),
                    String.format("%.2f", p.getPaidAmount()), p.getPaymentDate(), p.getMethod(), p.getCounterStaff()});
        }
        totalPaidLabel.setText(String.format("  Total paid: RM %.2f", functions.getTotalPaid()));

        // Feedback
        feedbackModel.setRowCount(0);
        for (Appointment a : functions.getAppointmentsWithFeedback()) {
            feedbackModel.addRow(new Object[]{a.getAppointmentId(), a.getServiceType(), a.getTechnician(),
                    a.getStatus(), a.getCustomerComment(), a.getTechnicianFeedback()});
        }

        // Comments: my previous comments
        commentModel.setRowCount(0);
        for (Comment c : functions.getMyComments()) {
            commentModel.addRow(new Object[]{c.getCommentId(), c.getAppointmentId(), c.getTargetRole(),
                    c.getTargetUsername(), c.getText(), c.getDate()});
        }

        // Comments: list of completed appointments to choose from
        appointmentBox.removeAllItems();
        for (Appointment a : functions.getCompletedAppointments()) {
            appointmentBox.addItem(a.getAppointmentId());
        }
        fillStaffBox();
    }

    // Fill the staff dropdown with the technician + counter staff of the chosen appointment
    private void fillStaffBox() {
        staffBox.removeAllItems();
        String apptId = (String) appointmentBox.getSelectedItem();
        if (apptId == null) return;

        for (Appointment a : functions.getCompletedAppointments()) {
            if (a.getAppointmentId().equals(apptId)) {
                staffBox.addItem("Technician - " + a.getTechnician());
            }
        }
        String counterStaff = functions.getCounterStaffFor(apptId);
        if (counterStaff != null) {
            staffBox.addItem("Counter - " + counterStaff);
        }
    }

    // ------------------------------------------------------------------
    // Button actions (event handling)
    // ------------------------------------------------------------------

    private void submitComment() {
        String apptId = (String) appointmentBox.getSelectedItem();
        String staff = (String) staffBox.getSelectedItem();
        if (apptId == null || staff == null) {
            JOptionPane.showMessageDialog(this, "You have no completed appointments to comment on.",
                    "Nothing to comment on", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String[] parts = staff.split(" - ");   // "Technician - sean" -> ["Technician", "sean"]
        try {
            functions.addComment(apptId, parts[0], parts[1], commentArea.getText());
            JOptionPane.showMessageDialog(this, "Comment submitted. Thank you!");
            commentArea.setText("");
            refreshAll();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid comment", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editProfile() {
        // reuse the shared dialog; it is "modal", so the code below waits until it closes
        new EditProfileDialog(this, user).setVisible(true);
        functions = new CustomerFunctions(user.getUsername());  // username may have changed
        setTitle("APU-ASC | Customer: " + user.getUsername());
        refreshAll();
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this, "Log out?", "Logout", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            FileUtil.currentUser = null;
            dispose();
            new login.LoginFrame().setVisible(true);
        }
    }

    // Helper: a table model whose cells cannot be edited by typing
    private static DefaultTableModel makeModel(String... columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
    }

    // Run this file directly to test the customer screen without logging in
    public static void main(String[] args) {
        FileUtil.loadAllUsers();
        User u = FileUtil.findUserByUsername("mike");
        if (u == null) {
            System.out.println("Test user 'mike' not found in users.txt");
            return;
        }
        SwingUtilities.invokeLater(() -> new CustomerFrame(u).setVisible(true));
    }
}
