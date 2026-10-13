package techniciancustomer;

import login.LoginFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TechnicianFrame extends JFrame {

    private String currentTechnician;
    private JTable jobTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> statusDropdown;
    private JTextArea notesArea;
    private JLabel selectedJobLabel;

    public TechnicianFrame(String technicianUsername) {
        this.currentTechnician = technicianUsername;

        setTitle("APU Automotive - Technician Dashboard (" + currentTechnician + ")");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        JLabel titleLabel = new JLabel("Assigned Work Orders");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 13));
        logoutButton.addActionListener(e -> logout());

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        // Table Setup
        String[] columnNames = {"Job ID", "Customer", "Car Plate", "Service", "Technician", "Status", "Notes"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            // Prevent direct cell editing so user uses controls below
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        jobTable = new JTable(tableModel);
        jobTable.setRowHeight(26);
        jobTable.setFont(new Font("Arial", Font.PLAIN, 13));
        jobTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(jobTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

        // Bottom Edit Panel
        JPanel editPanel = new JPanel(new BorderLayout(10, 10));
        editPanel.setBorder(BorderFactory.createTitledBorder("Update Selected Work Order"));

        JPanel inputFieldsPanel = new JPanel(new GridLayout(2, 2, 10, 8));

        selectedJobLabel = new JLabel("Selected Job: None");
        selectedJobLabel.setFont(new Font("Arial", Font.BOLD, 13));

        statusDropdown = new JComboBox<>(new String[]{"Pending", "In Progress", "Completed"});

        inputFieldsPanel.add(selectedJobLabel);
        inputFieldsPanel.add(new JLabel("Update Status:"));
        inputFieldsPanel.add(new JLabel()); // Spacer
        inputFieldsPanel.add(statusDropdown);

        JPanel notesPanel = new JPanel(new BorderLayout(5, 5));
        notesPanel.add(new JLabel("Technician Remarks / Notes:"), BorderLayout.NORTH);
        notesArea = new JTextArea(3, 30);
        notesArea.setLineWrap(true);
        notesPanel.add(new JScrollPane(notesArea), BorderLayout.CENTER);

        JButton updateButton = new JButton("Save & Update Job");
        updateButton.setFont(new Font("Arial", Font.BOLD, 13));
        updateButton.setPreferredSize(new Dimension(160, 38));
        updateButton.addActionListener(e -> updateJobStatus());

        JPanel bottomContainer = new JPanel(new BorderLayout(10, 10));
        bottomContainer.setBorder(BorderFactory.createEmptyBorder(0, 20, 15, 20));
        bottomContainer.add(inputFieldsPanel, BorderLayout.NORTH);
        bottomContainer.add(notesPanel, BorderLayout.CENTER);
        bottomContainer.add(updateButton, BorderLayout.SOUTH);

        // Add to Frame
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomContainer, BorderLayout.SOUTH);

        // Selection Listener for Table
        jobTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = jobTable.getSelectedRow();
            if (selectedRow != -1) {
                String jobId = tableModel.getValueAt(selectedRow, 0).toString();
                String status = tableModel.getValueAt(selectedRow, 5).toString();
                String notes = tableModel.getValueAt(selectedRow, 6).toString();

                selectedJobLabel.setText("Selected Job: " + jobId);
                statusDropdown.setSelectedItem(status);
                notesArea.setText(notes);
            }
        });

        // Load Jobs
        loadAssignedJobs();
    }

    private File getAppointmentsFile() {
        File file = new File("Assignment/data/appointments.txt");
        if (!file.exists()) {
            file = new File("data/appointments.txt");
        }
        return file;
    }

    private void loadAssignedJobs() {
        tableModel.setRowCount(0); // Clear existing rows
        File file = getAppointmentsFile();

        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "appointments.txt not found! Create one in data/ folder.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }

                String[] parts = line.split("\\|");
                if (parts.length >= 7) {
                    String techName = parts[4].trim();

                    // Only display tasks assigned to this technician
                    if (techName.equalsIgnoreCase(currentTechnician)) {
                        tableModel.addRow(new Object[]{
                                parts[0].trim(), // ID
                                parts[1].trim(), // Customer
                                parts[2].trim(), // Car Plate
                                parts[3].trim(), // Service
                                parts[4].trim(), // Technician
                                parts[5].trim(), // Status
                                parts[6].trim()  // Notes
                        });
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading jobs: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateJobStatus() {
        int selectedRow = jobTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a job from the table first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String targetJobId = tableModel.getValueAt(selectedRow, 0).toString();
        String newStatus = (String) statusDropdown.getSelectedItem();
        String newNotes = notesArea.getText().trim();

        File file = getAppointmentsFile();
        List<String> fileLines = new ArrayList<>();
        boolean foundAndUpdated = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");
                if (parts.length >= 7 && parts[0].trim().equals(targetJobId)) {
                    // Update this specific job
                    String updatedLine = parts[0].trim() + "|" +
                            parts[1].trim() + "|" +
                            parts[2].trim() + "|" +
                            parts[3].trim() + "|" +
                            parts[4].trim() + "|" +
                            newStatus + "|" +
                            newNotes;
                    fileLines.add(updatedLine);
                    foundAndUpdated = true;
                } else {
                    fileLines.add(line);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error reading file for update: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (foundAndUpdated) {
            // Write updated lines back to text file
            try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                for (String l : fileLines) {
                    pw.println(l);
                }
                JOptionPane.showMessageDialog(this, "Job " + targetJobId + " updated successfully!");
                loadAssignedJobs(); // Refresh table view
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error writing back to file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void logout() {
        this.dispose();
        new LoginFrame().setVisible(true);
    }
}