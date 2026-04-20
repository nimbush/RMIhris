package com.techspire.project.client.view.ui;


import javax.swing.*;
import java.awt.*;

public abstract class BasePanel extends JPanel {

    public BasePanel() {
        setBackground(DarkTheme.BG);
        setForeground(DarkTheme.TEXT);
        setLayout(new BorderLayout());
    }

    protected JPanel createCard() {
        JPanel panel = new JPanel();
        panel.setBackground(DarkTheme.PANEL);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }
}