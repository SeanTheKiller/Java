package login;

import counter.CounterFrame;
import manager.ManagerFrame;
import techniciancustomer.TechnicianFrame;
import common.FileUtil;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("APU Automotive Service Centre Login");
        setSize(550, 360);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        Font titleFont = new Font("SansSerif", Font.BOLD, 18);
        Font labelFont = new Font("SansSerif", Font.PLAIN, 15);
        Font inputFont = new Font("SansSerif", Font.PLAIN, 15);
        Font buttonFont = new Font("SansSerif", Font.BOLD, 14);

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

        String matchedRole = LoginManager.authenticate(enteredUsername, enteredPassword);

        if (matchedRole != null) {
            JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + enteredUsername);
            openRoleDashboard(matchedRole, enteredUsername);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openRoleDashboard(String role, String username) {
        String r = role.trim().toUpperCase();

        try {
            if (r.equals("MANAGER")) {
                new ManagerFrame().setVisible(true);
                this.dispose();

            } else if (r.equals("COUNTER") || r.equals("STAFF")) {
                //Dont fucking touch my fleutil load user again
                FileUtil.loadAllUsers();
                FileUtil.currentUser = FileUtil.findUserByUsername(username);
                CounterFrame a = new CounterFrame();
                a.setLocationRelativeTo(this);
                a.setVisible(true);
                this.dispose();

            } else if (r.equals("TECHNICIAN")) {
                new TechnicianFrame(username).setVisible(true);
                this.dispose();

            } else if (r.equals("CUSTOMER")) {
//                boolean opened = tryOpenCustomerFrame(username);
//                if (opened) {
//                    this.dispose();
//                } else {
//                    JOptionPane.showMessageDialog(this, 
//                        "Customer dashboard constructor mismatch.\nPlease check CustomerFrame.java constructor signature.", 
//                        "Customer Module Notice", 
//                        JOptionPane.INFORMATION_MESSAGE);
//                }
                FileUtil.loadAllUsers();
                FileUtil.currentUser = FileUtil.findUserByUsername(username);
                customer.CustomerFrame frame = new customer.CustomerFrame(FileUtil.currentUser);
                frame.setLocationRelativeTo(this);
                frame.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(this, "Unrecognized role: " + role, "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error opening dashboard: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Safely attempts to find and launch whatever constructor CustomerFrame has
    private boolean tryOpenCustomerFrame(String username) {
        try {
            Class<?> clazz = Class.forName("customer.CustomerFrame");
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();

            for (Constructor<?> c : constructors) {
                c.setAccessible(true);
                Class<?>[] params = c.getParameterTypes();

                // If constructor takes (String)
                if (params.length == 1 && params[0] == String.class) {
                    JFrame frame = (JFrame) c.newInstance(username);
                    frame.setVisible(true);
                    return true;
                }

                // If constructor takes no arguments ()
                if (params.length == 0) {
                    JFrame frame = (JFrame) c.newInstance();
                    frame.setVisible(true);
                    return true;
                }
            }
            
            // Print available constructor signatures to console for easy debugging
            System.out.println("Available CustomerFrame constructors:");
            for (Constructor<?> c : constructors) {
                System.out.println(" -> " + c.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}