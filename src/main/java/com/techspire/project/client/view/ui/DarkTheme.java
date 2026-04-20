package com.techspire.project.client.view.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class DarkTheme {

    public static final Color BG = new Color(18, 18, 18);
    public static final Color PANEL = new Color(30, 30, 30);
    public static final Color TEXT = new Color(230, 230, 230);
    public static final Color BORDER = new Color(60, 60, 60);

    public static final Color ACCENT = new Color(66, 133, 244);
    public static final Color SUCCESS = new Color(52, 168, 83);
    public static final Color ERROR = new Color(234, 67, 53);
    public static final Color WARNING = new Color(251, 188, 5);

    public static void apply() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        UIManager.put("Panel.background", BG);
        UIManager.put("OptionPane.background", BG);
        UIManager.put("OptionPane.messageForeground", TEXT);

        UIManager.put("Label.foreground", TEXT);

        UIManager.put("Button.background", ACCENT);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.focus", ACCENT);
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 13));

        UIManager.put("TextField.background", PANEL);
        UIManager.put("TextField.foreground", TEXT);
        UIManager.put("TextField.caretForeground", TEXT);
        UIManager.put("TextField.border", new LineBorder(BORDER, 1));

        UIManager.put("PasswordField.background", PANEL);
        UIManager.put("PasswordField.foreground", TEXT);
        UIManager.put("PasswordField.caretForeground", TEXT);
        UIManager.put("PasswordField.border", new LineBorder(BORDER, 1));

        UIManager.put("TextArea.background", PANEL);
        UIManager.put("TextArea.foreground", TEXT);
        UIManager.put("TextArea.caretForeground", TEXT);
        UIManager.put("TextArea.border", new LineBorder(BORDER, 1));

        UIManager.put("ComboBox.background", PANEL);
        UIManager.put("ComboBox.foreground", TEXT);

        UIManager.put("Table.background", PANEL);
        UIManager.put("Table.foreground", TEXT);
        UIManager.put("Table.selectionBackground", ACCENT);
        UIManager.put("Table.selectionForeground", Color.WHITE);
        UIManager.put("Table.gridColor", BORDER);

        UIManager.put("TableHeader.background", new Color(32, 33, 36));
        UIManager.put("TableHeader.foreground", new Color(232, 234, 237));

        UIManager.put("ScrollPane.background", BG);
        UIManager.put("Viewport.background", BG);
    }
}