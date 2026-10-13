package login;

import techniciancustomer.TechnicianFrame;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("APU Automotive Service Centre Login");
        setSize(550, 360);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        Font titleFont = new Font("Arial", Font.BOLD, 18);
        Font labelFont = new Font("Arial", Font.PLAIN, 15);
        Font inputFont = new Font("Arial", Font.PLAIN, 15);
        Font buttonFont = new Font("Arial", Font.BOLD, 14);

        JLabel headerLabel = new JLabel("Service Centre Portal", SwingConstants.CENTER);
        headerLabel.setFont(titleFont);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 15, 10));

        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setBorder(BorderFactory.createEmptyBorder(10, 45, 15, 45));

        // Username
        JPanel userRow = new JPanel(new BorderLayout(15, 0));
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(labelFont);
        userLabel.setPreferredSize(new Dimension(95, 38));

        usernameField = new JTextField();
        usernameField.setFont(inputFont);
        usernameField.setPreferredSize(new Dimension(280, 38));

        userRow.add(userLabel, BorderLayout.WEST);
        userRow.add(usernameField, BorderLayout.CENTER);

        // Password
        JPanel passRow = new JPanel(new BorderLayout(15, 0));
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(labelFont);
        passLabel.setPreferredSize(new Dimension(95, 38));

        passwordField = new JPasswordField();
        passwordField.setFont(inputFont);
        passwordField.setPreferredSize(new Dimension(280, 38));

        passRow.add(passLabel, BorderLayout.WEST);
        passRow.add(passwordField, BorderLayout.CENTER);

        formContainer.add(userRow);
        formContainer.add(Box.createVerticalStrut(18));
        formContainer.add(passRow);

        // Buttons
        JButton loginButton = new JButton("Login");
        JButton exitButton = new JButton("Exit");

        loginButton.setFont(buttonFont);
        loginButton.setPreferredSize(new Dimension(115, 38));

        exitButton.setFont(buttonFont);
        exitButton.setPreferredSize(new Dimension(115, 38));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);

        setLayout(new BorderLayout());
        add(headerLabel, BorderLayout.NORTH);
        add(formContainer, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> checkLogin());
        exitButton.addActionListener(e -> System.exit(0));
        passwordField.addActionListener(e -> checkLogin());
    }

    private void checkLogin() {
        String enteredUsername = usernameField.getText().trim();
        String enteredPassword = new String(passwordField.getPassword()).trim();

        if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Locate users.txt whether running from /Java or /Java/Assignment
        File file = new File("Assignment/data/users.txt");
        if (!file.exists()) {
            file = new File("data/users.txt");
        }

        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "Cannot find users.txt! Place it in data/ folder.", "File Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean found = false;
        String userRole = "";

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // Skip header row
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] parts = line.split("\\|");
                if (parts.length >= 3) {
                    String fileUser = parts[0].trim();
                    String filePass = parts[1].trim();
                    String fileRole = parts[2].trim();

                    if (fileUser.equals(enteredUsername) && filePass.equals(enteredPassword)) {
                        found = true;
                        userRole = fileRole;
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (found) {
            JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + enteredUsername);
            openRoleDashboard(userRole, enteredUsername);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openRoleDashboard(String role, String username) {
        String enteredUser = usernameField.getText().trim();
        String r = role.trim().toUpperCase();

        if (r.equals("MANAGER")) {
            JOptionPane.showMessageDialog(null, "Opening Manager Dashboard...");
            // new manager.ManagerFrame().setVisible(true);

        } else if (r.equals("COUNTER") || r.equals("STAFF")) {
            JOptionPane.showMessageDialog(null, "Opening Counter Dashboard...");
            // new counter.CounterFrame().setVisible(true);

        } else if (r.equals("TECHNICIAN")) {
            try {
                techniciancustomer.TechnicianFrame frame = new techniciancustomer.TechnicianFrame(enteredUser);
                frame.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error opening Technician dashboard: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (r.equals("CUSTOMER")) {
            JOptionPane.showMessageDialog(null, "Opening Customer Dashboard...");
            // new techniciancustomer.CustomerFrame().setVisible(true);

        } else {
            JOptionPane.showMessageDialog(null, "Unrecognized role: " + role, "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}