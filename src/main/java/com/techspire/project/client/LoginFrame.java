package com.techspire.project.client;

import com.techspire.project.client.view.LoginPanel;
import com.techspire.project.client.view.ui.DarkTheme;
import com.techspire.project.remote.Jdbc;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

    public LoginFrame(Jdbc jdbc) {
        setTitle("Employee Leave Management System - Login");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel(new GridLayout(1, 2));
        root.setBackground(DarkTheme.BG);

        root.add(new LoginPanel(jdbc));
        root.add(createRightVisualPanel());

        setContentPane(root);
    }

    private JPanel createRightVisualPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(DarkTheme.BG);

        JPanel innerCard = new JPanel(new BorderLayout(0, 20));
        innerCard.setPreferredSize(new Dimension(400, 470));
        innerCard.setBackground(DarkTheme.PANEL);
        innerCard.setBorder(new CompoundBorder(
                new LineBorder(DarkTheme.BORDER, 1, true),
                new EmptyBorder(40, 30, 40, 30)
        ));

        JLabel title = new JLabel("Welcome Back", SwingConstants.CENTER);
        title.setForeground(DarkTheme.TEXT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));

        JLabel subtitle = new JLabel("HR & Employee Leave Management", SwingConstants.CENTER);
        subtitle.setForeground(new Color(170, 170, 180));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JTextArea description = new JTextArea(
                "Manage employee profiles, leave applications, approvals, and yearly reports in one streamlined dashboard."
        );
        description.setWrapStyleWord(true);
        description.setLineWrap(true);
        description.setEditable(false);
        description.setOpaque(false);
        description.setFocusable(false);
        description.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        description.setForeground(new Color(185, 185, 195));
        description.setBorder(null);

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        description.setAlignmentX(Component.CENTER_ALIGNMENT);

        textPanel.add(Box.createVerticalGlue());
        textPanel.add(title);
        textPanel.add(Box.createVerticalStrut(12));
        textPanel.add(subtitle);
        textPanel.add(Box.createVerticalStrut(24));
        textPanel.add(description);
        textPanel.add(Box.createVerticalGlue());

        innerCard.add(textPanel, BorderLayout.CENTER);
        panel.add(innerCard);

        return panel;
    }
}