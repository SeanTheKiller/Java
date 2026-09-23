package login;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {

        setTitle("APU Automotive Service Centre Login");
        setSize(400, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 10, 20)
        );

        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        JButton exitButton = new JButton("Exit");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(event -> checkLogin());
        exitButton.addActionListener(event -> System.exit(0));
    }

    private void checkLogin() {

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.equals("manager") && password.equals("1234")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Login successful"
            );
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid username or password"
            );
        }
    }
}
