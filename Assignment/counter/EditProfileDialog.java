/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package counter;

/**
 *
 * @author johnc
 */
import common.User;
import javax.swing.*;
import java.awt.*;

public class EditProfileDialog extends JDialog {
    private JTextField nameField;
    private JTextField phoneField;
    private JPasswordField passField;
    private boolean saved = false;
    private User user;

    public EditProfileDialog(Frame owner, User user) {
        super(owner, "Edit Profile", true);
        this.user = user;
        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        
        nameField = new JTextField(user.getUsername(), 20);
        phoneField = new JTextField(user.getPhoneNumber(), 20);
        passField = new JPasswordField(user.getPassword(), 20);

        form.add(new JLabel("Username:"));
        form.add(nameField);
        form.add(new JLabel("Password:"));
        form.add(passField);
        form.add(new JLabel("Phone:"));
        form.add(phoneField);
        
        JButton ok = new JButton("Save");
        JButton cancel = new JButton("Cancel");
        ok.addActionListener(e -> onSave());
        cancel.addActionListener(e -> dispose());

        JPanel buttons = new JPanel();
        buttons.add(ok);
        buttons.add(cancel);

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }

    private void onSave() {
        try {
            user.setUsername(nameField.getText().trim());
            user.setPassword(new String(passField.getPassword()));
            user.setPhoneNumber(phoneField.getText().trim());
            saved = true;
            dispose();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid input", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() { return saved; }
}
