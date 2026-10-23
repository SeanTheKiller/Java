/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package common;

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
    private JTextField ageField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField addressField;
    private User user;
    // true = dialog creates a new customer instead of editing an existing user
    private boolean createMode;

    // edit an existing user
    public EditProfileDialog(Frame owner, User user) {
        this(owner, user, false);
    }

    // create a new customer: fields start blank
    public EditProfileDialog(Frame owner) {
        this(owner, null, true);
    }

    private EditProfileDialog(Frame owner, User user, boolean createMode) {
        super(owner, createMode ? "Create Customer" : "Edit Profile", true);
        this.createMode = createMode;
        this.user = user;
        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));

        if (createMode) {
            nameField = new JTextField(20);
            phoneField = new JTextField(20);
            passField = new JPasswordField(20);
            ageField = new JTextField(20);
            fullNameField = new JTextField(20);
            emailField = new JTextField(20);
            addressField = new JTextField(20);
        } else {
            nameField = new JTextField(user.getUsername(), 20);
            phoneField = new JTextField(user.getPhoneNumber(), 20);
            passField = new JPasswordField(user.getPassword(), 20);
            ageField = new JTextField(String.valueOf(user.getAge()), 20);
            fullNameField = new JTextField(user.getName(), 20);
            emailField = new JTextField(user.getEmail(), 20);
            addressField = new JTextField(user.getHomeAddress(), 20);
        }

        form.add(new JLabel("Username:"));
        form.add(nameField);
        form.add(new JLabel("Password:"));
        form.add(passField);
        form.add(new JLabel("Phone:"));
        form.add(phoneField);
        form.add(new JLabel("Age:"));
        form.add(ageField);
        form.add(new JLabel("Name:"));
        form.add(fullNameField);
        form.add(new JLabel("Email:"));
        form.add(emailField);
        form.add(new JLabel("Home Address:"));
        form.add(addressField);

        JButton ok = new JButton(createMode ? "Create" : "Save");
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
            String ageText = ageField.getText().trim();
            String name = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            String address = addressField.getText().trim();
            String password = new String(passField.getPassword());

            // check phone nomber
            if (phoneNo.length() != 10 || !phoneNo.matches("\\d+")){
                throw new IllegalArgumentException("provide phone number in the format of '0123016789'");
            }
            // check age
            int age;
            try {
                age = Integer.parseInt(ageText);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Age must be a number");
            }

            if (createMode) {
                FileUtil.loadAllUsers();
                // the username must not exist at all in users.txt
                if (FileUtil.findUserByUsername(username) != null) {
                    throw new IllegalArgumentException("username already taken, try another one");
                }
                // Customer constructor validates all fields and adds itself to allUsers
                new customer.Customer(username, password, phoneNo, age, name, email, address);
                FileUtil.saveAllUsers();
                JOptionPane.showMessageDialog(this, "Customer created successfully.");
                dispose();
                return;
            }

            // resync first: every FileUtil.loadAllUsers() rebuilds fresh User objects,
            // so our reference may be a stale copy no longer inside allUsers.
            // Find ourselves by the ORIGINAL username to get the live object.
            User liveUser = FileUtil.findUserByUsername(user.getUsername());
            if (liveUser == null) {
                throw new IllegalArgumentException("your user record no longer exists in users.txt");
            }
            if (liveUser != user) {
                user = liveUser;   // hold the fresh object that is actually in allUsers
            }

            // check username
            User foundUser = FileUtil.findUserByUsername(username);
            if (foundUser != null && foundUser != user){
                throw new IllegalArgumentException("username already taken, try another one");
            }

            user.setUsername(username);
            user.setPassword(password);
            user.setPhoneNumber(phoneNo);
            user.setAge(age);
            user.setName(name);
            user.setEmail(email);
            user.setHomeAddress(address);

            // save user
//            FileUtil.printAllUsers();
            FileUtil.saveAllUsers();
            // if edited user is logged-in user, currentUser points to same object, auto synced
            dispose();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid input ", JOptionPane.ERROR_MESSAGE);
        }
    }

}
