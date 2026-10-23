package login;

import common.FileUtil;
import counter.CounterFrame;
import techniciancustomer.TechnicianFrame;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("APU Automotive Service Centre Login");
        setSize(500, 360);
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

        // username row
        JPanel userRow = new JPanel(new BorderLayout(15, 0));
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(labelFont);
        userLabel.setPreferredSize(new Dimension(95, 38));

        usernameField = new JTextField();
        usernameField.setFont(inputFont);
        usernameField.setPreferredSize(new Dimension(280, 38));

        userRow.add(userLabel, BorderLayout.WEST);
        userRow.add(usernameField, BorderLayout.CENTER);

        // password row
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

        // buttons
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

        // when press button or hit Enter inside passfield, trigger checkLogin
        loginButton.addActionListener(e -> checkLogin());
        exitButton.addActionListener(e -> System.exit(0));
        passwordField.addActionListener(e -> checkLogin());
    }

    private void checkLogin() {
        // grab whatever user typed into the text boxes
        String enteredUsername = usernameField.getText().trim();
        String enteredPassword = new String(passwordField.getPassword()).trim();

        // if empty field, yell at user
        if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ask LoginManager to check txt file for us
        String matchedRole = LoginManager.authenticate(enteredUsername, enteredPassword);

        // if matchedRole is not null, means login win
        if (matchedRole != null) {
            JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + enteredUsername);
            openRoleDashboard(matchedRole, enteredUsername);
            this.dispose(); // close login window
        } else {
            // means wrong username or pass
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    // open corresponding dashboard window based on role
    private void openRoleDashboard(String role, String username) {
        String r = role.trim().toUpperCase();

        if (r.equals("MANAGER")) {
            JOptionPane.showMessageDialog(null, "Opening Manager Dashboard...");
            FileUtil.loadAllUsers();
            FileUtil.currentUser = FileUtil.findUserByUsername(username);
            new manager.ManagerFrame().setVisible(true);

        } else if (r.equals("COUNTER") || r.equals("STAFF")) {
            JOptionPane.showMessageDialog(null, "Opening Counter Dashboard...");
            FileUtil.loadAllUsers();
            FileUtil.currentUser = FileUtil.findUserByUsername(username);
            CounterFrame a = new CounterFrame();
            a.setLocationRelativeTo(this);
            a.setVisible(true);
            

        } else if (r.equals("TECHNICIAN")) {
            try {
                // launch your technician window and pass username into it
                TechnicianFrame frame = new TechnicianFrame(username);
                frame.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error opening Technician dashboard: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (r.equals("CUSTOMER")) {
            FileUtil.loadAllUsers();
            FileUtil.currentUser = FileUtil.findUserByUsername(username);
            customer.CustomerFrame frame = new customer.CustomerFrame(FileUtil.currentUser);
            frame.setLocationRelativeTo(this);
            frame.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(null, "Unrecognized role: " + role, "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
