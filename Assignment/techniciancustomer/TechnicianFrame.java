package techniciancustomer;

import login.LoginFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TechnicianFrame extends JFrame {

    private String technicianName;
    private JTable jobTable;
    private DefaultTableModel tableModel;

    // Detail Panel Components
    private JLabel selectedJobLabel;
    private JLabel customerLabel;
    private JLabel carPlateLabel;
    private JLabel serviceTypeLabel;
    private JComboBox<String> statusDropdown;

    // Separate Comment and Feedback text areas
    private JTextArea customerCommentArea;
    private JTextArea technicianFeedbackArea;

    public TechnicianFrame(String technicianName) {
        this.technicianName = technicianName;

        setTitle("APU Automotive Service Centre - Technician Portal");
        setSize(1040, 700);
        setMinimumSize(new Dimension(920, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        loadAppointments();
    }

    private void initUI() {
        JPanel rootPanel = new JPanel(new BorderLayout(15, 15));
        rootPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        rootPanel.setBackground(new Color(245, 247, 250));

        // 1. TOP HEADER PANEL
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JPanel titleSubPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        titleSubPanel.setOpaque(false);

        JLabel welcomeLabel = new JLabel("Welcome back, Technician " + technicianName);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 19));
        welcomeLabel.setForeground(new Color(33, 37, 41));

        JLabel subtitleLabel = new JLabel("Review customer symptoms, inspect vehicles, and record technical feedback.");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(108, 117, 125));

        titleSubPanel.add(welcomeLabel);
        titleSubPanel.add(subtitleLabel);

        JPanel headerButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        headerButtons.setOpaque(false);

        JButton profileBtn = new JButton("Edit Profile");
        profileBtn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        profileBtn.setFocusPainted(false);
        profileBtn.addActionListener(e -> openEditProfileDialog());

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        logoutBtn.setFocusPainted(false);
        logoutBtn.addActionListener(e -> logout());

        headerButtons.add(profileBtn);
        headerButtons.add(logoutBtn);

        headerPanel.add(titleSubPanel, BorderLayout.WEST);
        headerPanel.add(headerButtons, BorderLayout.EAST);

        // 2. CENTER TABLE PANEL (Shows 8 columns)
        String[] columns = {"Job ID", "Customer", "Car Plate", "Service Type", "Technician", "Status", "Customer Comment", "Technician Feedback"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        jobTable = new JTable(tableModel);
        jobTable.setRowHeight(28);
        jobTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        jobTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        jobTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jobTable.setShowGrid(true);
        jobTable.setGridColor(new Color(225, 230, 235));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        jobTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        jobTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        jobTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

        JScrollPane tableScroll = new JScrollPane(jobTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 205, 210)),
                "Assigned Appointments",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 13)
        ));
        tableScroll.getViewport().setBackground(Color.WHITE);

        // 3. BOTTOM WORK ORDER DETAILS PANEL
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 205, 210)),
                "Service Order Details & Status Update",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 13)
        ));

        // Read-only order meta info (Left)
        JPanel infoGrid = new JPanel(new GridLayout(4, 2, 8, 6));
        infoGrid.setOpaque(false);
        infoGrid.setBorder(new EmptyBorder(5, 10, 5, 15));

        selectedJobLabel = new JLabel("-");
        customerLabel = new JLabel("-");
        carPlateLabel = new JLabel("-");
        serviceTypeLabel = new JLabel("-");

        infoGrid.add(createBoldLabel("Selected Job ID:"));
        infoGrid.add(selectedJobLabel);
        infoGrid.add(createBoldLabel("Customer Name:"));
        infoGrid.add(customerLabel);
        infoGrid.add(createBoldLabel("Vehicle Plate:"));
        infoGrid.add(carPlateLabel);
        infoGrid.add(createBoldLabel("Service Package:"));
        infoGrid.add(serviceTypeLabel);

        // Center: Customer Comment (Read-Only) vs. Technician Feedback (Editable)
        JPanel textAreasGrid = new JPanel(new GridLayout(1, 2, 12, 0));
        textAreasGrid.setOpaque(false);

        // Customer Comment Box (Read-Only)
        JPanel commentBoxPanel = new JPanel(new BorderLayout(5, 5));
        commentBoxPanel.setOpaque(false);
        commentBoxPanel.add(createBoldLabel("Customer Comment / Reported Issue:"), BorderLayout.NORTH);

        customerCommentArea = new JTextArea(4, 20);
        customerCommentArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        customerCommentArea.setLineWrap(true);
        customerCommentArea.setWrapStyleWord(true);
        customerCommentArea.setEditable(false);
        customerCommentArea.setBackground(new Color(240, 242, 245));
        JScrollPane commentScroll = new JScrollPane(customerCommentArea);
        commentBoxPanel.add(commentScroll, BorderLayout.CENTER);

        // Technician Feedback Box (Editable)
        JPanel feedbackBoxPanel = new JPanel(new BorderLayout(5, 5));
        feedbackBoxPanel.setOpaque(false);
        feedbackBoxPanel.add(createBoldLabel("Technician Feedback / Service Log:"), BorderLayout.NORTH);

        technicianFeedbackArea = new JTextArea(4, 20);
        technicianFeedbackArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        technicianFeedbackArea.setLineWrap(true);
        technicianFeedbackArea.setWrapStyleWord(true);
        JScrollPane feedbackScroll = new JScrollPane(technicianFeedbackArea);
        feedbackBoxPanel.add(feedbackScroll, BorderLayout.CENTER);

        textAreasGrid.add(commentBoxPanel);
        textAreasGrid.add(feedbackBoxPanel);

        // Bottom status dropdown and Save button
        JPanel actionControls = new JPanel(new BorderLayout(10, 0));
        actionControls.setOpaque(false);
        actionControls.setBorder(new EmptyBorder(8, 0, 0, 0));

        JPanel statusRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        statusRow.setOpaque(false);
        statusRow.add(createBoldLabel("Work Order Status:"));

        statusDropdown = new JComboBox<>(new String[]{"Pending", "In Progress", "Completed"});
        statusDropdown.setFont(new Font("SansSerif", Font.PLAIN, 13));
        statusDropdown.setPreferredSize(new Dimension(140, 28));
        statusRow.add(statusDropdown);

        JButton saveButton = new JButton("Save & Update Appointment");
        saveButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        saveButton.setPreferredSize(new Dimension(230, 34));
        saveButton.setBackground(new Color(40, 116, 240));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(e -> saveJobUpdates());

        actionControls.add(statusRow, BorderLayout.WEST);
        actionControls.add(saveButton, BorderLayout.EAST);

        // Assemble bottom container
        JPanel centerBottom = new JPanel(new BorderLayout(10, 10));
        centerBottom.setOpaque(false);
        centerBottom.add(textAreasGrid, BorderLayout.CENTER);
        centerBottom.add(actionControls, BorderLayout.SOUTH);

        bottomPanel.add(infoGrid, BorderLayout.WEST);
        bottomPanel.add(centerBottom, BorderLayout.CENTER);

        // Add everything to frame
        rootPanel.add(headerPanel, BorderLayout.NORTH);
        rootPanel.add(tableScroll, BorderLayout.CENTER);
        rootPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(rootPanel);

        // Selection Listener
        jobTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = jobTable.getSelectedRow();
                if (row != -1 && row < tableModel.getRowCount()) {
                    selectedJobLabel.setText(tableModel.getValueAt(row, 0).toString());
                    customerLabel.setText(tableModel.getValueAt(row, 1).toString());
                    carPlateLabel.setText(tableModel.getValueAt(row, 2).toString());
                    serviceTypeLabel.setText(tableModel.getValueAt(row, 3).toString());
                    statusDropdown.setSelectedItem(tableModel.getValueAt(row, 5).toString());

                    // Column 6 = Customer Comment (Read-only)
                    Object commentObj = tableModel.getValueAt(row, 6);
                    customerCommentArea.setText(commentObj != null ? commentObj.toString() : "");

                    // Column 7 = Technician Feedback (Editable)
                    Object feedbackObj = tableModel.getValueAt(row, 7);
                    technicianFeedbackArea.setText(feedbackObj != null ? feedbackObj.toString() : "");
                }
            }
        });
    }

    private JLabel createBoldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(new Color(50, 55, 60));
        return label;
    }

    private void loadAppointments() {
        tableModel.setRowCount(0);
        List<String[]> jobs = TechnicianFunctions.getJobsForTechnician(technicianName);

        for (String[] row : jobs) {
            tableModel.addRow(row);
        }

        if (tableModel.getRowCount() > 0) {
            jobTable.setRowSelectionInterval(0, 0);
        }
    }

    private void saveJobUpdates() {
        int selectedRow = jobTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment from the table first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String jobId = tableModel.getValueAt(selectedRow, 0).toString();
        String newStatus = (String) statusDropdown.getSelectedItem();
        String newFeedback = technicianFeedbackArea.getText().trim();

        boolean success = TechnicianFunctions.updateJob(jobId, newStatus, newFeedback);

        if (success) {
            // Update table model in place
            tableModel.setValueAt(newStatus, selectedRow, 5);
            tableModel.setValueAt(newFeedback, selectedRow, 7);

            JOptionPane.showMessageDialog(this, "Work Order " + jobId + " updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update record in appointments.txt", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openEditProfileDialog() {
        String[] profile = TechnicianFunctions.getUserProfile(technicianName);

        if (profile == null) {
            JOptionPane.showMessageDialog(this, "Unable to load user profile details.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(this, "Edit Technician Profile", true);
        dialog.setSize(380, 290);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 12));
        formPanel.setBorder(new EmptyBorder(20, 25, 10, 25));

        JTextField usernameDisplay = new JTextField(profile[0]);
        usernameDisplay.setEditable(false);

        JTextField roleDisplay = new JTextField(profile[2]);
        roleDisplay.setEditable(false);

        JPasswordField passwordInput = new JPasswordField(profile[1]);
        JTextField phoneInput = new JTextField(profile[3]);

        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameDisplay);
        formPanel.add(new JLabel("Role:"));
        formPanel.add(roleDisplay);
        formPanel.add(new JLabel("New Password:"));
        formPanel.add(passwordInput);
        formPanel.add(new JLabel("Phone Number:"));
        formPanel.add(phoneInput);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        JButton saveBtn = new JButton("Save Changes");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> {
            String newPass = new String(passwordInput.getPassword()).trim();
            String newPhone = phoneInput.getText().trim();

            if (newPass.isEmpty() || newPhone.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Fields cannot be blank.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!newPhone.matches("\\d{10,11}")) {
                JOptionPane.showMessageDialog(dialog, "Phone number must be 10 or 11 digits (e.g., 0123456789).", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean updated = TechnicianFunctions.updateProfile(technicianName, newPass, newPhone);
            if (updated) {
                JOptionPane.showMessageDialog(dialog, "Profile updated successfully!");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to write updates to users.txt", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void logout() {
        this.dispose();
        new LoginFrame().setVisible(true);
    }
}