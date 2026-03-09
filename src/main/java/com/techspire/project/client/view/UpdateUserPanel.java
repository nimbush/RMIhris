package com.techspire.project.client.view;

import com.techspire.project.client.controller.ClientController;
import com.techspire.project.dto.UpdateUserDto;
import com.techspire.project.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

public class UpdateUserPanel extends JPanel {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^(97|98)\\d{8}$");

    private final ClientController controller;
    private final String icNumber;

    private final JTextField icNumberField;
    private final JTextField fatherNameField;
    private final JTextField motherNameField;
    private final JTextField phoneNumberField;
    private final JTextField addressField;
    private final JButton updateButton;

    public UpdateUserPanel(ClientController controller, String icNumber) {
        this.controller = controller;
        this.icNumber = icNumber;

        this.icNumberField = new JTextField(24);
        this.fatherNameField = new JTextField(24);
        this.motherNameField = new JTextField(24);
        this.phoneNumberField = new JTextField(24);
        this.addressField = new JTextField(24);
        this.updateButton = new JButton("Update Details");

        this.icNumberField.setText(icNumber);
        this.icNumberField.setEditable(false);

        initUi();
        registerEvents();
    }

    private void initUi() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Update Profile Details"));
        setBackground(Color.WHITE);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 18);

        Dimension fieldSize = new Dimension(320, 42);
        Dimension buttonSize = new Dimension(240, 48);

        icNumberField.setFont(fieldFont);
        fatherNameField.setFont(fieldFont);
        motherNameField.setFont(fieldFont);
        phoneNumberField.setFont(fieldFont);
        addressField.setFont(fieldFont);

        icNumberField.setPreferredSize(fieldSize);
        fatherNameField.setPreferredSize(fieldSize);
        motherNameField.setPreferredSize(fieldSize);
        phoneNumberField.setPreferredSize(fieldSize);
        addressField.setPreferredSize(fieldSize);

        updateButton.setFont(buttonFont);
        updateButton.setPreferredSize(buttonSize);
        updateButton.setFocusPainted(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel icLabel = new JLabel("IC Number:");
        icLabel.setFont(labelFont);
        add(icLabel, gbc);

        gbc.gridx = 1;
        add(icNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel fatherLabel = new JLabel("Father Name:");
        fatherLabel.setFont(labelFont);
        add(fatherLabel, gbc);

        gbc.gridx = 1;
        add(fatherNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel motherLabel = new JLabel("Mother Name:");
        motherLabel.setFont(labelFont);
        add(motherLabel, gbc);

        gbc.gridx = 1;
        add(motherNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setFont(labelFont);
        add(phoneLabel, gbc);

        gbc.gridx = 1;
        add(phoneNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(labelFont);
        add(addressLabel, gbc);

        gbc.gridx = 1;
        add(addressField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        add(updateButton, gbc);
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
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void updateUserDetails() {
        try {
            String phone = phoneNumberField.getText().trim();

            if (!phone.isEmpty() && !PHONE_PATTERN.matcher(phone).matches()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Phone number must be a valid Nepali mobile number starting with 97 or 98 and be 10 digits long.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );
                phoneNumberField.requestFocus();
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
                JOptionPane.showMessageDialog(
                        this,
                        "Details updated successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                loadUserDetails();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to update details.",
                        "Update Failed",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearEditableFields() {
        fatherNameField.setText("");
        motherNameField.setText("");
        phoneNumberField.setText("");
        addressField.setText("");
    }
}