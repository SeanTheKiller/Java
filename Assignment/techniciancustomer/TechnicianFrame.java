package techniciancustomer;

import login.LoginFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TechnicianFrame extends JFrame {

    private String technicianName;
    private JTable jobTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> statusDropdown;
    private JTextArea notesArea;
    private JLabel selectedJobLabel;

    public TechnicianFrame(String technicianName) {
        this.technicianName = technicianName;

        setTitle("Technician Dashboard - " + technicianName);
        setSize(850, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        JLabel title = new JLabel("Assigned Work Orders (" + technicianName + ")");
        title.setFont(new Font("Arial", Font.BOLD, 18));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });

        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(logoutBtn, BorderLayout.EAST);

        // Table
        String[] columns = {"Job ID", "Customer", "Car Plate", "Service", "Technician", "Status", "Notes"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        jobTable = new JTable(tableModel);
        jobTable.setRowHeight(25);
        jobTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane tableScroll = new JScrollPane(jobTable);
        tableScroll.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

        // Form Controls
        JPanel controlPanel = new JPanel(new BorderLayout(10, 10));
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 20, 15, 20),
                BorderFactory.createTitledBorder("Update Selected Job")
        ));

        JPanel row1 = new JPanel(new GridLayout(1, 4, 10, 10));
        selectedJobLabel = new JLabel("Selected Job: None");
        selectedJobLabel.setFont(new Font("Arial", Font.BOLD, 13));

        statusDropdown = new JComboBox<>(new String[]{"Pending", "In Progress", "Completed"});

        row1.add(selectedJobLabel);
        row1.add(new JLabel("Set Status:", SwingConstants.RIGHT));
        row1.add(statusDropdown);

        JPanel notesPanel = new JPanel(new BorderLayout(5, 5));
        notesPanel.add(new JLabel("Technician Remarks:"), BorderLayout.NORTH);
        notesArea = new JTextArea(3, 20);
        notesArea.setLineWrap(true);
        notesPanel.add(new JScrollPane(notesArea), BorderLayout.CENTER);

        JButton saveBtn = new JButton("Save & Update Job");
        saveBtn.setFont(new Font("Arial", Font.BOLD, 13));
        saveBtn.setPreferredSize(new Dimension(150, 36));
        saveBtn.addActionListener(e -> saveJob());

        controlPanel.add(row1, BorderLayout.NORTH);
        controlPanel.add(notesPanel, BorderLayout.CENTER);
        controlPanel.add(saveBtn, BorderLayout.SOUTH);

        // Layout
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // Selection Listener with safe guard
        jobTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = jobTable.getSelectedRow();
                if (row != -1 && row < tableModel.getRowCount()) {
                    selectedJobLabel.setText("Selected Job: " + tableModel.getValueAt(row, 0));
                    statusDropdown.setSelectedItem(tableModel.getValueAt(row, 5));
                    Object noteVal = tableModel.getValueAt(row, 6);
                    notesArea.setText(noteVal != null ? noteVal.toString() : "");
                }
            }
        });

        // Initial Load
        loadInitialData();
    }

    private void loadInitialData() {
        tableModel.setRowCount(0);
        List<String[]> jobs = TechnicianFunctions.getJobsForTechnician(technicianName);
        for (String[] row : jobs) {
            tableModel.addRow(row);
        }
    }

    private void saveJob() {
        int row = jobTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a job from the table first.");
            return;
        }

        String jobId = tableModel.getValueAt(row, 0).toString();
        String status = (String) statusDropdown.getSelectedItem();
        String notes = notesArea.getText().trim();

        // 1. Update text file via TechnicianFunctions
        boolean ok = TechnicianFunctions.updateJob(jobId, status, notes);

        if (ok) {
            // 2. Direct in-memory table update (avoids clearing the whole table!)
            tableModel.setValueAt(status, row, 5);
            tableModel.setValueAt(notes, row, 6);

            JOptionPane.showMessageDialog(this, "Job " + jobId + " updated successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update job in file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}