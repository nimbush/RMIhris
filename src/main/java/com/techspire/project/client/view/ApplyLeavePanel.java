package com.techspire.project.client.view;

import com.techspire.project.client.controller.ClientController;
import com.techspire.project.dto.LeaveApplyDto;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class ApplyLeavePanel extends JPanel {

    private final ClientController controller;
    private final JTextField icNumberField;
    private final JDateChooser fromDateChooser;
    private final JDateChooser toDateChooser;
    private final JTextField leaveDaysField;
    private final JButton applyButton;

    public ApplyLeavePanel(ClientController controller, String icNumber) {
        this.controller = controller;

        this.icNumberField = new JTextField(24);
        this.fromDateChooser = new JDateChooser();
        this.toDateChooser = new JDateChooser();
        this.leaveDaysField = new JTextField(24);
        this.applyButton = new JButton("Apply Leave");

        this.icNumberField.setText(icNumber);
        this.icNumberField.setEditable(false);
        this.leaveDaysField.setEditable(false);

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

        fromDateChooser.setFont(fieldFont);
        toDateChooser.setFont(fieldFont);

        icNumberField.setPreferredSize(fieldSize);
        leaveDaysField.setPreferredSize(fieldSize);
        fromDateChooser.setPreferredSize(fieldSize);
        toDateChooser.setPreferredSize(fieldSize);

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
        JLabel fromLabel = new JLabel("From Date:");
        fromLabel.setFont(labelFont);
        add(fromLabel, gbc);

        gbc.gridx = 1;
        add(fromDateChooser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel toLabel = new JLabel("To Date:");
        toLabel.setFont(labelFont);
        add(toLabel, gbc);

        gbc.gridx = 1;
        add(toDateChooser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel leaveDaysLabel = new JLabel("Leave Days:");
        leaveDaysLabel.setFont(labelFont);
        add(leaveDaysLabel, gbc);

        gbc.gridx = 1;
        add(leaveDaysField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        add(applyButton, gbc);
    }

    private void registerEvents() {
        applyButton.addActionListener(e -> applyLeave());

        fromDateChooser.getDateEditor().addPropertyChangeListener(evt -> updateLeaveDays());
        toDateChooser.getDateEditor().addPropertyChangeListener(evt -> updateLeaveDays());
    }

    private void updateLeaveDays() {
        Date from = fromDateChooser.getDate();
        Date to = toDateChooser.getDate();

        if (from == null || to == null) {
            leaveDaysField.setText("");
            return;
        }

        LocalDate fromLocal = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate toLocal = to.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (toLocal.isBefore(fromLocal)) {
            leaveDaysField.setText("");
            return;
        }

        long days = ChronoUnit.DAYS.between(fromLocal, toLocal) + 1;
        leaveDaysField.setText(String.valueOf(days));
    }

    private void applyLeave() {
        try {
            Date from = fromDateChooser.getDate();
            Date to = toDateChooser.getDate();

            if (from == null || to == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please select both From Date and To Date.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            LocalDate fromLocal = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate toLocal = to.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (toLocal.isBefore(fromLocal)) {
                JOptionPane.showMessageDialog(
                        this,
                        "To Date cannot be before From Date.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int leaveDays = (int) (ChronoUnit.DAYS.between(fromLocal, toLocal) + 1);

            LeaveApplyDto dto = new LeaveApplyDto();
            dto.setIcNumber(icNumberField.getText().trim());
            dto.setFromDate(fromLocal);
            dto.setToDate(toLocal);
            dto.setLeaveDays(leaveDays);

            String result = controller.applyLeave(dto);

            JOptionPane.showMessageDialog(
                    this,
                    result,
                    "Leave Application",
                    JOptionPane.INFORMATION_MESSAGE
            );

            fromDateChooser.setDate(null);
            toDateChooser.setDate(null);
            leaveDaysField.setText("");

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