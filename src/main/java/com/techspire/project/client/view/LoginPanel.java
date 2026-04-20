package com.techspire.project.client.view;

import com.techspire.project.client.EmployeeDashboardFrame;
import com.techspire.project.client.HrDashboardFrame;
import com.techspire.project.client.LoginFrame;
import com.techspire.project.client.view.ui.BasePanel;
import com.techspire.project.client.view.ui.DarkTheme;
import com.techspire.project.client.view.ui.DialogUtil;
import com.techspire.project.client.view.ui.ModernButton;
import com.techspire.project.dto.LoginResponseDto;
import com.techspire.project.model.enums.UserType;
import com.techspire.project.remote.Jdbc;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class LoginPanel extends BasePanel {

    private final Jdbc jdbc;
    private final JTextField icNumberField;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final ModernButton loginButton;

    public LoginPanel(Jdbc jdbc) {
        this.jdbc = jdbc;
        this.icNumberField = new JTextField(22);
        this.usernameField = new JTextField(22);
        this.passwordField = new JPasswordField(22);
        this.loginButton = new ModernButton("Login Now");

        initUi();
        registerEvents();
    }

    private void initUi() {
        setBackground(DarkTheme.BG);
        setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setBackground(DarkTheme.PANEL);
        card.setPreferredSize(new Dimension(430, 500));
        card.setLayout(new GridBagLayout());
        card.setBorder(new CompoundBorder(
                new LineBorder(DarkTheme.BORDER, 1, true),
                new EmptyBorder(35, 35, 35, 35)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        JLabel titleLabel = new JLabel("LOGIN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 34));
        titleLabel.setForeground(DarkTheme.TEXT);

        JLabel subtitleLabel = new JLabel("Sign in to continue", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(160, 160, 170));

        styleField(icNumberField);
        styleField(usernameField);
        styleField(passwordField);
        loginButton.setPreferredSize(new Dimension(160, 46));

        gbc.gridy = 0;
        card.add(titleLabel, gbc);

        gbc.gridy = 1;
        card.add(subtitleLabel, gbc);

        gbc.gridy = 2;
        card.add(createLabeledField("IC Number", icNumberField), gbc);

        gbc.gridy = 3;
        card.add(createLabeledField("Username", usernameField), gbc);

        gbc.gridy = 4;
        card.add(createLabeledField("Password", passwordField), gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(22, 10, 10, 10);
        card.add(loginButton, gbc);

        add(card);
    }

    private JPanel createLabeledField(String label, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);

        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        jLabel.setForeground(new Color(190, 190, 200));

        panel.add(jLabel, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private void styleField(JComponent field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        field.setPreferredSize(new Dimension(320, 42));
        field.setBorder(new CompoundBorder(
                new LineBorder(DarkTheme.BORDER, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        field.setBackground(new Color(36, 36, 36));
        field.setForeground(DarkTheme.TEXT);

        if (field instanceof JTextField textField) {
            textField.setCaretColor(DarkTheme.TEXT);
        }
        if (field instanceof JPasswordField passwordField) {
            passwordField.setCaretColor(DarkTheme.TEXT);
        }
    }

    private void registerEvents() {
        loginButton.addActionListener(e -> doLogin());
        passwordField.addActionListener(e -> doLogin());
        usernameField.addActionListener(e -> doLogin());
        icNumberField.addActionListener(e -> doLogin());
    }

    private void doLogin() {
        String icNumber = icNumberField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (icNumber.isEmpty()) {
            DialogUtil.styleMessageDialog();
            JOptionPane.showMessageDialog(this, "IC number is required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            icNumberField.requestFocus();
            return;
        }

        if (username.isEmpty()) {
            DialogUtil.styleMessageDialog();
            JOptionPane.showMessageDialog(this, "Username is required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            usernameField.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            DialogUtil.styleMessageDialog();
            JOptionPane.showMessageDialog(this, "Password is required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            passwordField.requestFocus();
            return;
        }

        try {
            LoginResponseDto response = jdbc.logIn(icNumber, username, password);

            if (response == null) {
                DialogUtil.styleMessageDialog();
                JOptionPane.showMessageDialog(this, "Login failed. No response from server.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!response.isSuccess()) {
                DialogUtil.styleMessageDialog();
                JOptionPane.showMessageDialog(this, response.getMessage(), "Login Failed", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (response.isDefaultPassword()) {
                boolean changed = showChangePasswordDialog(response.getIcNumber());

                if (!changed) {
                    DialogUtil.styleMessageDialog();
                    JOptionPane.showMessageDialog(
                            this,
                            "You must change your default password before continuing.",
                            "Password Change Required",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }
            }

            openDashboard(response);

        } catch (Exception ex) {
            ex.printStackTrace();
            DialogUtil.styleMessageDialog();
            JOptionPane.showMessageDialog(
                    this,
                    "Error during login: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private boolean showChangePasswordDialog(String icNumber) {
        while (true) {
            JPasswordField newPasswordField = new JPasswordField(20);
            JPasswordField confirmPasswordField = new JPasswordField(20);

            styleDialogPasswordField(newPasswordField);
            styleDialogPasswordField(confirmPasswordField);

            JLabel newPasswordLabel = new JLabel("New Password:");
            JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
            newPasswordLabel.setForeground(DarkTheme.TEXT);
            confirmPasswordLabel.setForeground(DarkTheme.TEXT);

            JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
            panel.setBackground(DarkTheme.PANEL);
            panel.setBorder(new EmptyBorder(10, 10, 10, 10));
            panel.add(newPasswordLabel);
            panel.add(newPasswordField);
            panel.add(confirmPasswordLabel);
            panel.add(confirmPasswordField);

            boolean confirmed = DialogUtil.showFormDialog(
                    this,
                    "Change Default Password",
                    panel,
                    "OK"
            );

            if (!confirmed) {
                return false;
            }

            String newPassword = new String(newPasswordField.getPassword()).trim();
            String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                DialogUtil.styleMessageDialog();
                JOptionPane.showMessageDialog(this, "Both password fields are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                continue;
            }

            if (!newPassword.equals(confirmPassword)) {
                DialogUtil.styleMessageDialog();
                JOptionPane.showMessageDialog(this, "Passwords do not match.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                continue;
            }

            if (newPassword.length() < 6) {
                DialogUtil.styleMessageDialog();
                JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                continue;
            }

            try {
                boolean changed = jdbc.changePassword(icNumber, newPassword);

                if (changed) {
                    DialogUtil.styleMessageDialog();
                    JOptionPane.showMessageDialog(this, "Password changed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } else {
                    DialogUtil.styleMessageDialog();
                    JOptionPane.showMessageDialog(this, "Failed to change password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                DialogUtil.styleMessageDialog();
                JOptionPane.showMessageDialog(this, "Error changing password: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void styleDialogPasswordField(JPasswordField field) {
        field.setBackground(new Color(36, 36, 36));
        field.setForeground(DarkTheme.TEXT);
        field.setCaretColor(DarkTheme.TEXT);
        field.setBorder(new CompoundBorder(
                new LineBorder(DarkTheme.BORDER, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private void openDashboard(LoginResponseDto response) {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }

        try {
            if (response.getUserType() == UserType.HR) {
                HrDashboardFrame hrDashboardFrame = new HrDashboardFrame(jdbc, response);
                hrDashboardFrame.setVisible(true);
            } else if (response.getUserType() == UserType.EMPLOYEE) {
                EmployeeDashboardFrame employeeDashboardFrame = new EmployeeDashboardFrame(jdbc, response);
                employeeDashboardFrame.setVisible(true);
            } else {
                DialogUtil.styleMessageDialog();
                JOptionPane.showMessageDialog(this, "Unknown user role.", "Error", JOptionPane.ERROR_MESSAGE);
                new LoginFrame(jdbc).setVisible(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            DialogUtil.styleMessageDialog();
            JOptionPane.showMessageDialog(this, "Failed to open dashboard: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            new LoginFrame(jdbc).setVisible(true);
        }
    }
}