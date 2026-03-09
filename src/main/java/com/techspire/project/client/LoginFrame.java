package com.techspire.project.client;

import com.techspire.project.client.view.LoginPanel;
import com.techspire.project.remote.Jdbc;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    public LoginFrame(Jdbc jdbc) {
        setTitle("Employee Leave Management System - Login");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel(new GridLayout(1, 2));
        root.add(new LoginPanel(jdbc));
        root.add(createRightVisualPanel());

        setContentPane(root);
        setVisible(true);
    }

    private JPanel createRightVisualPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(83, 66, 244));
        panel.setLayout(new GridBagLayout());

        JPanel innerCard = new JPanel();
        innerCard.setPreferredSize(new Dimension(360, 430));
        innerCard.setBackground(new Color(120, 106, 234));
        innerCard.setBorder(BorderFactory.createLineBorder(new Color(170, 160, 255), 1));
        innerCard.setLayout(new BorderLayout());

        JLabel title = new JLabel("Welcome Back", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JLabel subtitle = new JLabel("HR & Employee Leave Management", SwingConstants.CENTER);
        subtitle.setForeground(new Color(216, 216, 236));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        textPanel.setOpaque(false);
        textPanel.add(title);
        textPanel.add(subtitle);

        innerCard.add(textPanel, BorderLayout.CENTER);
        panel.add(innerCard);

        return panel;
    }
}