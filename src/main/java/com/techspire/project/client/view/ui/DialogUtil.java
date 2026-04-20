package com.techspire.project.client.view.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DialogUtil {

    private DialogUtil() {
    }

    public static void styleMessageDialog() {
        UIManager.put("OptionPane.background", new Color(24, 24, 24));
        UIManager.put("Panel.background", new Color(24, 24, 24));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);

        UIManager.put("Button.background", new Color(76, 132, 255));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.select", new Color(76, 132, 255));

        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 14));
    }

    public static boolean showConfirmDialog(Component parent, String title, String message) {
        Window owner = parent == null ? null : SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = new JDialog(owner, title, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(430, 190);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parent);

        JPanel root = new JPanel(new BorderLayout(15, 15));
        root.setBackground(new Color(24, 24, 24));
        root.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel iconLabel = new JLabel(UIManager.getIcon("OptionPane.questionIcon"));
        JPanel iconPanel = new JPanel(new BorderLayout());
        iconPanel.setBackground(new Color(24, 24, 24));
        iconPanel.add(iconLabel, BorderLayout.NORTH);

        JLabel messageLabel = new JLabel(message);
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        JPanel centerPanel = new JPanel(new BorderLayout(12, 0));
        centerPanel.setBackground(new Color(24, 24, 24));
        centerPanel.add(iconPanel, BorderLayout.WEST);
        centerPanel.add(messageLabel, BorderLayout.CENTER);

        JButton okButton = createButton("OK", new Color(76, 132, 255));
        JButton cancelButton = createButton("Cancel", new Color(220, 53, 69));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttonPanel.setBackground(new Color(24, 24, 24));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        final boolean[] result = {false};

        okButton.addActionListener(e -> {
            result[0] = true;
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> {
            result[0] = false;
            dialog.dispose();
        });

        root.add(centerPanel, BorderLayout.CENTER);
        root.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.getRootPane().setDefaultButton(okButton);
        dialog.setVisible(true);

        return result[0];
    }

    public static boolean showFormDialog(Component parent, String title, JPanel formPanel, String okText) {
        Window owner = parent == null ? null : SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = new JDialog(owner, title, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parent);

        JButton okButton = createButton(okText, new Color(76, 132, 255));
        JButton cancelButton = createButton("Cancel", new Color(220, 53, 69));

        JPanel root = new JPanel(new BorderLayout(15, 15));
        root.setBackground(new Color(24, 24, 24));
        root.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(24, 24, 24));
        wrapper.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttonPanel.setBackground(new Color(24, 24, 24));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        final boolean[] result = {false};

        okButton.addActionListener(e -> {
            result[0] = true;
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> {
            result[0] = false;
            dialog.dispose();
        });

        root.add(wrapper, BorderLayout.CENTER);
        root.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.pack();
        dialog.getRootPane().setDefaultButton(okButton);
        dialog.setVisible(true);

        return result[0];
    }

    private static JButton createButton(String text, Color background) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 38));
        button.setBackground(background);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return button;
    }
}