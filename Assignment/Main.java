import javax.swing.SwingUtilities;
import login.LoginFrame;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();

            loginFrame.setLocationRelativeTo(null);
            loginFrame.setVisible(true);
        });
    }
}
