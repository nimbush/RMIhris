package com.techspire.project.client.view.ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class DarkThemeUtil {

    private DarkThemeUtil() {
    }

    public static void applyGlobalDarkTheme() {
        Color background = new Color(18, 18, 18);
        Color panelBackground = new Color(28, 28, 28);
        Color fieldBackground = new Color(34, 34, 34);
        Color borderColor = new Color(70, 70, 70);
        Color foreground = Color.WHITE;
        Color accent = new Color(74, 134, 232);
        Color danger = new Color(230, 57, 70);

        Font defaultFont = new Font("SansSerif", Font.PLAIN, 14);
        Font boldFont = new Font("SansSerif", Font.BOLD, 14);

        Border buttonBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)
        );

        Border textBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        );

        UIManager.put("control", panelBackground);
        UIManager.put("info", panelBackground);
        UIManager.put("nimbusBase", background);
        UIManager.put("nimbusAlertYellow", new Color(255, 189, 46));
        UIManager.put("nimbusDisabledText", new Color(140, 140, 140));
        UIManager.put("nimbusFocus", accent);
        UIManager.put("nimbusGreen", new Color(46, 204, 113));
        UIManager.put("nimbusInfoBlue", accent);
        UIManager.put("nimbusLightBackground", fieldBackground);
        UIManager.put("nimbusOrange", new Color(255, 140, 0));
        UIManager.put("nimbusRed", danger);
        UIManager.put("nimbusSelectedText", foreground);
        UIManager.put("nimbusSelectionBackground", accent);
        UIManager.put("text", foreground);

        UIManager.put("Panel.background", panelBackground);
        UIManager.put("Panel.foreground", foreground);

        UIManager.put("OptionPane.background", panelBackground);
        UIManager.put("OptionPane.foreground", foreground);
        UIManager.put("OptionPane.messageForeground", foreground);
        UIManager.put("OptionPane.border", BorderFactory.createLineBorder(borderColor));

        UIManager.put("Label.background", panelBackground);
        UIManager.put("Label.foreground", foreground);
        UIManager.put("Label.font", defaultFont);

        UIManager.put("Button.background", new Color(90, 170, 230));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.select", new Color(70, 140, 210));
        UIManager.put("Button.focus", accent);
        UIManager.put("Button.font", boldFont);
        UIManager.put("Button.border", buttonBorder);
        UIManager.put("Button.margin", new Insets(8, 18, 8, 18));

        UIManager.put("TextField.background", fieldBackground);
        UIManager.put("TextField.foreground", foreground);
        UIManager.put("TextField.caretForeground", foreground);
        UIManager.put("TextField.border", textBorder);
        UIManager.put("TextField.font", defaultFont);

        UIManager.put("PasswordField.background", fieldBackground);
        UIManager.put("PasswordField.foreground", foreground);
        UIManager.put("PasswordField.caretForeground", foreground);
        UIManager.put("PasswordField.border", textBorder);
        UIManager.put("PasswordField.font", defaultFont);

        UIManager.put("TextArea.background", fieldBackground);
        UIManager.put("TextArea.foreground", foreground);
        UIManager.put("TextArea.caretForeground", foreground);
        UIManager.put("TextArea.border", textBorder);
        UIManager.put("TextArea.font", defaultFont);

        UIManager.put("ComboBox.background", fieldBackground);
        UIManager.put("ComboBox.foreground", foreground);
        UIManager.put("ComboBox.selectionBackground", accent);
        UIManager.put("ComboBox.selectionForeground", foreground);
        UIManager.put("ComboBox.font", defaultFont);

        UIManager.put("Table.background", panelBackground);
        UIManager.put("Table.foreground", foreground);
        UIManager.put("Table.selectionBackground", accent);
        UIManager.put("Table.selectionForeground", foreground);
        UIManager.put("Table.gridColor", borderColor);
        UIManager.put("Table.font", defaultFont);

        UIManager.put("TableHeader.background", new Color(36, 36, 36));
        UIManager.put("TableHeader.foreground", foreground);
        UIManager.put("TableHeader.font", boldFont);

        UIManager.put("ScrollPane.background", panelBackground);
        UIManager.put("ScrollPane.foreground", foreground);

        UIManager.put("Viewport.background", panelBackground);

        UIManager.put("Dialog.background", panelBackground);
        UIManager.put("RootPane.background", panelBackground);
    }

    public static void styleOptionPaneButtons() {
        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        defaults.put("Button.background", new Color(90, 170, 230));
        defaults.put("Button.foreground", Color.WHITE);
        defaults.put("Button.select", new Color(70, 140, 210));
    }
}