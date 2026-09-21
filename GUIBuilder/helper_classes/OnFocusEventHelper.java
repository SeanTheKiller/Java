package helper_classes;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.text.JTextComponent;

public class OnFocusEventHelper {

    public static void setOnFocusText(JTextComponent textComponent, String placeholderText, Color focusGainedColor, Color focusLostColor) {
        textComponent.setText(placeholderText);
        textComponent.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textComponent.getText().equals(placeholderText)) {
                    textComponent.setText("");
                    textComponent.setForeground(focusGainedColor);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textComponent.getText().isEmpty()) {
                    textComponent.setText(placeholderText);
                    textComponent.setForeground(focusLostColor);
                }
            }
        });
    }
}