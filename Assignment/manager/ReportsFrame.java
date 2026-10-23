package manager;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ReportsFrame extends JFrame {

    private final JPanel overviewPanel = new JPanel(new GridLayout(0, 2, 8, 8));
    private final DefaultTableModel appointmentModel = model("Category", "Value");
    private final DefaultTableModel staffModel = model(
            "Technician", "Assigned", "Completed", "In Progress", "Pending", "Completion Rate");
    private final DefaultTableModel paymentModel = model("Payment Summary", "Value");

    public ReportsFrame() {
        setTitle("Reports");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Overview", overviewPanel);
        tabs.addTab("Appointments", new JScrollPane(new JTable(appointmentModel)));
        tabs.addTab("Technicians", new JScrollPane(new JTable(staffModel)));
        tabs.addTab("Payments", new JScrollPane(new JTable(paymentModel)));
        add(tabs, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadReport());
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            new ManagerFrame().setVisible(true);
            dispose();
        });
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(refreshButton);
        buttons.add(backButton);
        add(buttons, BorderLayout.SOUTH);

        setSize(750, 450);
        setLocationRelativeTo(null);
        loadReport();
    }

    private void loadReport() {
        ReportFunctions.ReportData report = ReportFunctions.loadReport();
        loadOverview(report);
        loadAppointments(report);
        loadStaff(report);
        loadPayments(report);
    }

    private void loadOverview(ReportFunctions.ReportData report) {
        overviewPanel.removeAll();
        addSummary("Total appointments", report.getTotalAppointments());
        addSummary("Completed", count(report.getAppointmentsByStatus(), "Completed"));
        addSummary("In progress", count(report.getAppointmentsByStatus(), "In Progress"));
        addSummary("Pending", count(report.getAppointmentsByStatus(), "Pending"));
        addSummary("Total billed", money(report.getTotalBilled()));
        addSummary("Total paid", money(report.getTotalPaid()));
        addSummary("Outstanding", money(report.getOutstanding()));
        overviewPanel.revalidate();
        overviewPanel.repaint();
    }

    private void loadAppointments(ReportFunctions.ReportData report) {
        appointmentModel.setRowCount(0);
        for (Map.Entry<String, Integer> entry : report.getAppointmentsByStatus().entrySet()) {
            appointmentModel.addRow(new Object[]{"Status: " + entry.getKey(), entry.getValue()});
        }
        for (Map.Entry<String, Integer> entry : report.getAppointmentsByService().entrySet()) {
            appointmentModel.addRow(new Object[]{"Service: " + entry.getKey(), entry.getValue()});
        }
    }

    private void loadStaff(ReportFunctions.ReportData report) {
        staffModel.setRowCount(0);
        for (ReportFunctions.StaffReport staff : report.getStaffReports().values()) {
            staffModel.addRow(new Object[]{staff.getTechnician(), staff.getAssigned(),
                staff.getCompleted(), staff.getInProgress(), staff.getPending(),
                String.format("%.2f%%", staff.getCompletionRate())});
        }
    }

    private void loadPayments(ReportFunctions.ReportData report) {
        paymentModel.setRowCount(0);
        paymentModel.addRow(new Object[]{"Total billed", money(report.getTotalBilled())});
        paymentModel.addRow(new Object[]{"Total paid", money(report.getTotalPaid())});
        paymentModel.addRow(new Object[]{"Outstanding", money(report.getOutstanding())});
        paymentModel.addRow(new Object[]{"Fully paid", report.getFullyPaid()});
        paymentModel.addRow(new Object[]{"Partially paid", report.getPartiallyPaid()});
        paymentModel.addRow(new Object[]{"Unpaid", report.getUnpaid()});
    }

    private void addSummary(String label, Object value) {
        overviewPanel.add(new JLabel(label));
        overviewPanel.add(new JLabel(String.valueOf(value)));
    }

    private static int count(Map<String, Integer> values, String key) {
        Integer count = values.get(key);
        return count == null ? 0 : count;
    }

    private static String money(double amount) {
        return String.format("RM %.2f", amount);
    }

    private static DefaultTableModel model(String... columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }
}
