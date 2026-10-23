package manager;

import common.FileUtil;
import common.ServicePrice;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SetPricesFrame extends javax.swing.JFrame {

    private final JPanel pricesPanel = new JPanel(new GridLayout(0, 3, 5, 5));

    public SetPricesFrame() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Set Prices");
        add(pricesPanel, BorderLayout.CENTER);
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> {
            new ManagerFrame().setVisible(true);
            dispose();
        });
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
        setSize(400, 250);
        setLocationRelativeTo(null);
        loadPriceRows();
    }

    private void loadPriceRows() {
        FileUtil.loadPrices();
        pricesPanel.removeAll();
        pricesPanel.add(new JLabel("Service"));
        pricesPanel.add(new JLabel("Price"));
        pricesPanel.add(new JLabel(""));

        for (ServicePrice servicePrice : FileUtil.allPrices) {
            JTextField priceField = new JTextField(String.valueOf(servicePrice.getPrice()));
            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(e -> savePrice(servicePrice, priceField));
            pricesPanel.add(new JLabel(servicePrice.getServiceType()));
            pricesPanel.add(priceField);
            pricesPanel.add(saveButton);
        }

        pricesPanel.revalidate();
        pricesPanel.repaint();
    }

    private void savePrice(ServicePrice servicePrice, JTextField priceField) {
        try {
            double price = Double.parseDouble(priceField.getText().trim());
            ManagerFunctions.updateServicePrice(servicePrice.getServiceType(), price);
            JOptionPane.showMessageDialog(this, "Price updated successfully.");
            loadPriceRows();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid price.",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
        }
    }
}
