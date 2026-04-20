package com.techspire.project.client.view;

import com.techspire.project.client.controller.ClientController;
import com.techspire.project.client.view.ui.BasePanel;
import com.techspire.project.client.view.ui.DarkTheme;
import com.techspire.project.client.view.ui.DialogUtil;
import com.techspire.project.client.view.ui.ModernButton;
import com.techspire.project.dto.UpdateUserDto;
import com.techspire.project.model.User;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.regex.Pattern;

public class UpdateUserPanel extends BasePanel {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^(97|98)\\d{8}$");

    private final ClientController controller;
    private final String icNumber;

    private final JTextField icNumberField = new JTextField(24);
    private final JTextField fatherNameField = new JTextField(24);
    private final JTextField motherNameField = new JTextField(24);
    private final JTextField phoneNumberField = new JTextField(24);
    private final JTextField addressField = new JTextField(24);
    private final ModernButton updateButton = new ModernButton("Update Details");

    public UpdateUserPanel(ClientController controller, String icNumber) {
        this.controller = controller;
        this.icNumber = icNumber;

        icNumberField.setText(icNumber);
        icNumberField.setEditable(false);

        initUi();
        registerEvents();
    }

    private void initUi() {
        setLayout(new GridBagLayout());
        setBackground(DarkTheme.BG);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(DarkTheme.PANEL);
        card.setBorder(new CompoundBorder(
                new LineBorder(DarkTheme.BORDER, 1, true),
                new EmptyBorder(25, 25, 25, 25)
        ));

        Font titleFont = new Font("Segoe UI", Font.BOLD, 22);
        Font labelFont = new Font("Segoe UI", Font.BOLD, 15);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 15);

        JLabel titleLabel = new JLabel("Update Profile Details");
        titleLabel.setForeground(DarkTheme.TEXT);
        titleLabel.setFont(titleFont);

        styleField(icNumberField, fieldFont, true);
        styleField(fatherNameField, fieldFont, false);
        styleField(motherNameField, fieldFont, false);
        styleField(phoneNumberField, fieldFont, false);
        styleField(addressField, fieldFont, false);

        updateButton.setPreferredSize(new Dimension(220, 44));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 20, 10);
        card.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);

        addRow(card, gbc, 1, "IC Number:", icNumberField, labelFont);
        addRow(card, gbc, 2, "Father Name:", fatherNameField, labelFont);
        addRow(card, gbc, 3, "Mother Name:", motherNameField, labelFont);
        addRow(card, gbc, 4, "Phone Number:", phoneNumberField, labelFont);
        addRow(card, gbc, 5, "Address:", addressField, labelFont);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 0, 10);
        card.add(updateButton, gbc);

        add(card);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int y, String label, JComponent field, Font font) {
        gbc.gridx = 0;
        gbc.gridy = y;
        JLabel l = new JLabel(label);
        l.setForeground(DarkTheme.TEXT);
        l.setFont(font);
        panel.add(l, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void styleField(JTextField field, Font font, boolean readOnly) {
        field.setFont(font);
        field.setPreferredSize(new Dimension(320, 42));
        field.setBackground(readOnly ? new Color(40, 40, 40) : DarkTheme.PANEL);
        field.setForeground(readOnly ? new Color(180, 180, 180) : DarkTheme.TEXT);
        field.setCaretColor(DarkTheme.TEXT);
        field.setBorder(new CompoundBorder(
                new LineBorder(DarkTheme.BORDER, 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
    }

    private void registerEvents() {
        updateButton.addActionListener(e -> updateUserDetails());
    }

    public void loadUserDetails() {
        try {
            User user = controller.getUserByIcNumber(icNumber);

            if (user != null) {
                fatherNameField.setText(user.getFatherName() != null ? user.getFatherName() : "");
                motherNameField.setText(user.getMotherName() != null ? user.getMotherName() : "");
                phoneNumberField.setText(user.getPhoneNumber() != null ? user.getPhoneNumber() : "");
                addressField.setText(user.getAddress() != null ? user.getAddress() : "");
            } else {
                clearEditableFields();
            }

        } catch (Exception ex) {
            DialogUtil.styleMessageDialog();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUserDetails() {
        try {
            String phone = phoneNumberField.getText().trim();

            if (!phone.isEmpty() && !PHONE_PATTERN.matcher(phone).matches()) {
                DialogUtil.styleMessageDialog();
                JOptionPane.showMessageDialog(
                        this,
                        "Phone number must be a valid Nepali mobile number starting with 97 or 98 and be 10 digits long.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );
                phoneNumberField.requestFocus();
                return;
            }

            boolean confirmed = DialogUtil.showConfirmDialog(
                    this,
                    "Confirm Update",
                    "Are you sure you want to update these details?"
            );

            if (!confirmed) {
                return;
            }

            UpdateUserDto dto = new UpdateUserDto();
            dto.setIcNumber(icNumberField.getText().trim());
            dto.setFatherName(fatherNameField.getText().trim());
            dto.setMotherName(motherNameField.getText().trim());
            dto.setPhoneNumber(phone);
            dto.setAddress(addressField.getText().trim());

            boolean updated = controller.updateUser(dto);

            if (updated) {
                DialogUtil.styleMessageDialog();
                JOptionPane.showMessageDialog(
                        this,
                        "Details updated successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                loadUserDetails();
            } else {
                DialogUtil.styleMessageDialog();
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to update details.",
                        "Update Failed",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (Exception ex) {
            DialogUtil.styleMessageDialog();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearEditableFields() {
        fatherNameField.setText("");
        motherNameField.setText("");
        phoneNumberField.setText("");
        addressField.setText("");
    }
}