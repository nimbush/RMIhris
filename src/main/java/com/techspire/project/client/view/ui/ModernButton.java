package com.techspire.project.client.view.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ModernButton extends JButton {

    public ModernButton(String text) {
        super(text);
        init();
    }

    private void init() {
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(true);

        setBackground(DarkTheme.ACCENT);
        setForeground(Color.WHITE);

        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        setBorder(new EmptyBorder(10, 16, 10, 16));

        // hover effect
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(DarkTheme.ACCENT.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(DarkTheme.ACCENT);
            }
        });
    }

    public void setDangerStyle() {
        setBackground(new Color(220, 53, 69));
    }

    public void setSuccessStyle() {
        setBackground(new Color(25, 135, 84));
    }

    public void setSecondaryStyle() {
        setBackground(new Color(70, 70, 70));
    }
}