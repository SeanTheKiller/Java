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

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Create");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2.setText("Update");

        jButton3.setText("Read");

        jButton4.setText("Delete");

        jLabel1.setText("CRUD");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(234, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addComponent(jButton3)
                .addGap(18, 18, 18)
                .addComponent(jButton4)
                .addContainerGap(65, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

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
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
