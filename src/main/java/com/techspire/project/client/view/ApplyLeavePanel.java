package com.techspire.project.client.view;

import com.techspire.project.client.controller.ClientController;
import com.techspire.project.dto.LeaveApplyDto;

import javax.swing.*;
import java.awt.*;

public class ApplyLeavePanel extends JPanel {

    private final ClientController controller;
    private final JTextField icNumberField;
    private final JTextField leaveDaysField;
    private final JButton applyButton;

    public ApplyLeavePanel(ClientController controller, String icNumber) {
        this.controller = controller;

        this.icNumberField = new JTextField(24);
        this.leaveDaysField = new JTextField(24);
        this.applyButton = new JButton("Apply Leave");

        this.icNumberField.setText(icNumber);
        this.icNumberField.setEditable(false);

        initUi();
        registerEvents();
    }

    private void initUi() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Apply Leave"));
        setBackground(Color.WHITE);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 18);

        Dimension fieldSize = new Dimension(320, 42);
        Dimension buttonSize = new Dimension(240, 48);

        icNumberField.setFont(fieldFont);
        leaveDaysField.setFont(fieldFont);

        icNumberField.setPreferredSize(fieldSize);
        leaveDaysField.setPreferredSize(fieldSize);

        applyButton.setFont(buttonFont);
        applyButton.setPreferredSize(buttonSize);
        applyButton.setFocusPainted(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(18, 18, 18, 18);
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
        JLabel leaveDaysLabel = new JLabel("Leave Days:");
        leaveDaysLabel.setFont(labelFont);
        add(leaveDaysLabel, gbc);

        gbc.gridx = 1;
        add(leaveDaysField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(applyButton, gbc);
    }

    private void registerEvents() {
        applyButton.addActionListener(e -> applyLeave());
    }

    private void applyLeave() {
        try {
            String leaveDaysText = leaveDaysField.getText().trim();

            if (leaveDaysText.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Leave days are required.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int leaveDays = Integer.parseInt(leaveDaysText);

            LeaveApplyDto dto = new LeaveApplyDto();
            dto.setIcNumber(icNumberField.getText().trim());
            dto.setLeaveDays(leaveDays);

            String result = controller.applyLeave(dto);

            JOptionPane.showMessageDialog(
                    this,
                    result,
                    "Leave Application",
                    JOptionPane.INFORMATION_MESSAGE
            );

            leaveDaysField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Leave days must be a valid number.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
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
}