    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package manager;

import common.FileUtil;
import common.User;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author jerem
 */
public class CRUDFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CRUDFrame.class.getName());

    /**
     * Creates new form CRUDFrame
     */
    public CRUDFrame() {
        initComponents();
        buildUserList();
    }

    private void buildUserList() {
        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout(5, 5));

        JPanel usersPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(usersPanel);
        add(scrollPane, BorderLayout.CENTER);

        JButton createButton = new JButton("Create");
        createButton.addActionListener(e -> {
            new CreateFrame().setVisible(true);
            dispose();
        });
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(createButton);
        add(bottomPanel, BorderLayout.SOUTH);

        FileUtil.loadAllUsers();
        usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.Y_AXIS));

        JPanel header = new JPanel(new GridLayout(1, 5));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        header.setBorder(new EmptyBorder(5, 5, 5, 5));
        header.add(new JLabel("Username"));
        header.add(new JLabel("Role"));
        header.add(new JLabel("Phone"));
        header.add(new JLabel("Edit"));
        header.add(new JLabel("Delete"));
        usersPanel.add(header);

        for (User user : FileUtil.allUsers) {
            JPanel row = new JPanel(new GridLayout(1, 5));
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            row.setBorder(new EmptyBorder(2, 5, 2, 5));
            row.add(new JLabel(user.getUsername()));
            row.add(new JLabel(user.getRole()));
            row.add(new JLabel(user.getPhoneNumber()));

            JButton editButton = new JButton("Edit");
            editButton.addActionListener(e -> editUser(user));
            row.add(editButton);

            JButton deleteButton = new JButton("Delete");
            deleteButton.addActionListener(e -> deleteUser(user));
            row.add(deleteButton);
            usersPanel.add(row);
        }

        setTitle("Manage Users");
        setSize(650, 400);
        setLocationRelativeTo(null);
    }

    private void editUser(User user) {
        JTextField usernameField = new JTextField(user.getUsername());
        JTextField passwordField = new JTextField(user.getPassword());
        JTextField roleField = new JTextField(user.getRole());
        JTextField phoneField = new JTextField(user.getPhoneNumber());
        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.add(new JLabel("Username"));
        form.add(usernameField);
        form.add(new JLabel("Password"));
        form.add(passwordField);
        form.add(new JLabel("Role"));
        form.add(roleField);
        form.add(new JLabel("Phone Number"));
        form.add(phoneField);

        int result = JOptionPane.showConfirmDialog(
                this, form, "Edit User", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            ManagerFunctions.updateUser(
                    user.getUsername(),
                    usernameField.getText(),
                    passwordField.getText(),
                    roleField.getText(),
                    phoneField.getText());
            buildUserList();
            revalidate();
            repaint();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid input",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUser(User user) {
        int result = JOptionPane.showConfirmDialog(
                this, "Delete user '" + user.getUsername() + "'?",
                "Confirm delete", JOptionPane.YES_NO_OPTION);
        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            ManagerFunctions.deleteUser(user.getUsername());
            buildUserList();
            revalidate();
            repaint();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Delete failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new CRUDFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
