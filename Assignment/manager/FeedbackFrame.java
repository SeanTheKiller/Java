package manager;

import common.FileUtil;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class FeedbackFrame extends JFrame {

    public FeedbackFrame() {
        setTitle("View Feedback");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));

        DefaultTableModel feedbackModel = new DefaultTableModel(
                new Object[]{"Appointment", "Customer", "Source", "Feedback / Comment"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable feedbackTable = new JTable(feedbackModel);
        feedbackTable.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(feedbackTable), BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadFeedback(feedbackModel));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            new ManagerFrame().setVisible(true);
            dispose();
        });
        javax.swing.JPanel buttons = new javax.swing.JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(refreshButton);
        buttons.add(backButton);
        add(buttons, BorderLayout.SOUTH);

        setSize(750, 400);
        setLocationRelativeTo(null);
        loadFeedback(feedbackModel);
    }

    private void loadFeedback(DefaultTableModel feedbackModel) {
        feedbackModel.setRowCount(0);
        List<ReportFunctions.FeedbackRow> rows
                = ReportFunctions.loadFeedbackRows(FileUtil.loadAllAppointments());
        for (ReportFunctions.FeedbackRow row : rows) {
            feedbackModel.addRow(new Object[]{row.getAppointmentId(), row.getCustomer(),
                row.getSource(), row.getText()});
        }
    }
}
