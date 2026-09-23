import javax.swing.*;
import java.awt.Color;
import helper_classes.*;

public class WindowBuilder {
  public static void main(String[] args) {

     JFrame frame = new JFrame("My Awesome Window");
     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     frame.setSize(742, 400);
     JPanel panel = new JPanel();
     panel.setLayout(null);
     panel.setBackground(Color.decode("#eeeeee"));

     JButton element1 = new JButton("Click Me");
     element1.setBounds(203, 195, 139, 54);
     element1.setBackground(Color.decode("#ffffff"));
     element1.setForeground(Color.decode("#1b1b1b"));
     element1.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
     element1.setBorder(new RoundedBorder(4, Color.decode("#626262"), 1));
     element1.setFocusPainted(false);
     OnClickEventHelper.setOnClickColor(element1, Color.decode("#c2c2c2"), Color.decode("#ffffff"));
     panel.add(element1);

     JTextField element2 = new JTextField("");
     element2.setBounds(276, 143, 193, 23);
     element2.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
     element2.setBackground(Color.decode("#ffffff"));
     element2.setForeground(Color.decode("#737674"));
     element2.setBorder(new RoundedBorder(2, Color.decode("#626262"), 1));
     OnFocusEventHelper.setOnFocusText(element2, "Your Input!", Color.decode("#1b1b1b"),   Color.decode("#737674"));
     panel.add(element2);

     JButton element5 = new JButton("Click Me");
     element5.setBounds(398, 194, 137, 55);
     element5.setBackground(Color.decode("#ffffff"));
     element5.setForeground(Color.decode("#1b1b1b"));
     element5.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
     element5.setBorder(new RoundedBorder(4, Color.decode("#626262"), 1));
     element5.setFocusPainted(false);
     OnClickEventHelper.setOnClickColor(element5, Color.decode("#c2c2c2"), Color.decode("#ffffff"));
     panel.add(element5);

     JLabel element6 = new JLabel("LOGIN PASSWORD");
     element6.setBounds(307, 109, 173, 16);
     element6.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
     element6.setForeground(Color.decode("#1b1b1b"));
     panel.add(element6);

     frame.add(panel);
     frame.setVisible(true);

  }
}