package com.techspire.project.client.view;

import com.techspire.project.client.controller.ClientController;
import com.techspire.project.dto.CreateUserDto;
import com.techspire.project.model.enums.UserType;

import javax.swing.*;
import java.awt.*;

public class CreateUserPanel extends JPanel {

    private final ClientController controller;
    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField icNumberField;
    private final JTextField usernameField;
    private final JComboBox<String> userTypeDropdown;
    private final JTextField leaveCountField;

    public CreateUserPanel(ClientController controller) {
        this.controller = controller;
        this.firstNameField = new JTextField(24);
        this.lastNameField = new JTextField(24);
        this.icNumberField = new JTextField(24);
        this.usernameField = new JTextField(24);
        this.leaveCountField = new JTextField(24);

        this.userTypeDropdown = new JComboBox<>();
        userTypeDropdown.addItem("Select Role");
        userTypeDropdown.addItem("HR");
        userTypeDropdown.addItem("EMPLOYEE");
        userTypeDropdown.setSelectedIndex(0);

        initUi();
    }

    private void initUi() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Create User"));
        setBackground(Color.WHITE);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 18);

        Dimension fieldSize = new Dimension(300, 40);
        Dimension comboSize = new Dimension(300, 40);
        Dimension buttonSize = new Dimension(220, 45);

        firstNameField.setFont(fieldFont);
        lastNameField.setFont(fieldFont);
        icNumberField.setFont(fieldFont);
        usernameField.setFont(fieldFont);
        leaveCountField.setFont(fieldFont);
        userTypeDropdown.setFont(fieldFont);

        firstNameField.setPreferredSize(fieldSize);
        lastNameField.setPreferredSize(fieldSize);
        icNumberField.setPreferredSize(fieldSize);
        usernameField.setPreferredSize(fieldSize);
        leaveCountField.setPreferredSize(fieldSize);
        userTypeDropdown.setPreferredSize(comboSize);

        JButton createBtn = new JButton("Create User");
        createBtn.setFont(buttonFont);
        createBtn.setPreferredSize(buttonSize);
        createBtn.setFocusPainted(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(14, 14, 14, 14);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setFont(labelFont);
        add(firstNameLabel, gbc);

        gbc.gridx = 1;
        add(firstNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setFont(labelFont);
        add(lastNameLabel, gbc);

        gbc.gridx = 1;
        add(lastNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel icLabel = new JLabel("IC Number:");
        icLabel.setFont(labelFont);
        add(icLabel, gbc);

        gbc.gridx = 1;
        add(icNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(labelFont);
        add(usernameLabel, gbc);

        gbc.gridx = 1;
        add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel userTypeLabel = new JLabel("User Type:");
        userTypeLabel.setFont(labelFont);
        add(userTypeLabel, gbc);

        gbc.gridx = 1;
        add(userTypeDropdown, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel leaveCountLabel = new JLabel("Leave Count:");
        leaveCountLabel.setFont(labelFont);
        add(leaveCountLabel, gbc);

        gbc.gridx = 1;
        add(leaveCountField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        add(createBtn, gbc);

        createBtn.addActionListener(e -> createUser());
    }

    private void createUser() {
        try {
            String selectedRole = (String) userTypeDropdown.getSelectedItem();

            if (selectedRole == null || selectedRole.equals("Select Role")) {
                JOptionPane.showMessageDialog(this, "Please select a user role.");
                return;
            }

            CreateUserDto dto = new CreateUserDto();
            dto.setFirstName(firstNameField.getText().trim());
            dto.setLastName(lastNameField.getText().trim());
            dto.setIcNumber(icNumberField.getText().trim());
            dto.setUsername(usernameField.getText().trim());
            dto.setUserType(UserType.valueOf(selectedRole));
            dto.setLeaveCount(Integer.parseInt(leaveCountField.getText().trim()));

            boolean result = controller.createUser(dto);

            if (result) {
                JOptionPane.showMessageDialog(this, "User created successfully.\nDefault password: Pass@123");
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create user");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Leave count must be a valid number.");
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void clearForm() {
        firstNameField.setText("");
        lastNameField.setText("");
        icNumberField.setText("");
        usernameField.setText("");
        leaveCountField.setText("");
        userTypeDropdown.setSelectedIndex(0);
        firstNameField.requestFocus();
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}