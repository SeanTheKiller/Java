/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package counter;

/**
 *
 * @author johnc
 */
import common.FileUtil;
import common.User;
import javax.swing.*;
import java.awt.*;

public class EditProfileDialog extends JDialog {
    private JTextField nameField;
    private JTextField phoneField;
    private JPasswordField passField;
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
            String phoneNo = phoneField.getText().trim();
            String username = nameField.getText().trim();

            // check phone nomber
            if (phoneNo.length() != 10 || !phoneNo.matches("\\d+")){
                throw new IllegalArgumentException("provide phone number in the format of '0123016789'");
            }
            // check username
            User foundUser = FileUtil.findUserByUsername(username);
            if (foundUser != null && foundUser != user){
                throw new IllegalArgumentException("username already taken, try another one");
            }
            
            user.setUsername(username);
            user.setPassword(new String(passField.getPassword()));
            user.setPhoneNumber(phoneNo);
            
            // save user
            FileUtil.printAllUsers();
            FileUtil.saveAllUsers();
            // if edited user is logged-in user, currentUser points to same object, auto synced
            dispose();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid input ", JOptionPane.ERROR_MESSAGE);
        }
    }

}
