package com.techspire.project.client.view;

import com.techspire.project.client.controller.ClientController;
import com.techspire.project.client.view.ui.BasePanel;
import com.techspire.project.client.view.ui.DarkTheme;
import com.techspire.project.client.view.ui.DialogUtil;
import com.techspire.project.client.view.ui.ModernButton;
import com.techspire.project.dto.CreateUserDto;
import com.techspire.project.model.enums.UserType;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;

public class CreateUserPanel extends BasePanel {

    private final ClientController controller;

    private final JTextField firstNameField = new JTextField(24);
    private final JTextField lastNameField = new JTextField(24);
    private final JTextField icNumberField = new JTextField(24);
    private final JTextField usernameField = new JTextField(24);
    private final JTextField leaveCountField = new JTextField(24);
    private final JComboBox<String> userTypeDropdown = new JComboBox<>();

    public CreateUserPanel(ClientController controller) {
        this.controller = controller;

        userTypeDropdown.addItem("Select Role");
        userTypeDropdown.addItem("HR");
        userTypeDropdown.addItem("EMPLOYEE");
        userTypeDropdown.setSelectedIndex(0);

        initUi();
    }

    private void initUi() {
        setLayout(new GridBagLayout());
        setBackground(DarkTheme.BG);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(DarkTheme.PANEL);
        card.setBorder(new CompoundBorder(
                new LineBorder(DarkTheme.BORDER, 1, true),
                new EmptyBorder(24, 24, 24, 24)
        ));

        Font titleFont = new Font("Segoe UI", Font.BOLD, 22);
        Font labelFont = new Font("Segoe UI", Font.BOLD, 15);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 15);

        Dimension fieldSize = new Dimension(320, 42);

        JLabel titleLabel = new JLabel("Create User");
        titleLabel.setForeground(DarkTheme.TEXT);
        titleLabel.setFont(titleFont);

        styleTextField(firstNameField, fieldFont, fieldSize);
        styleTextField(lastNameField, fieldFont, fieldSize);
        styleTextField(icNumberField, fieldFont, fieldSize);
        styleTextField(usernameField, fieldFont, fieldSize);
        styleTextField(leaveCountField, fieldFont, fieldSize);
        styleComboBox(userTypeDropdown, fieldFont, fieldSize);

        ModernButton createBtn = new ModernButton("Create User");
        createBtn.setPreferredSize(new Dimension(220, 44));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 20, 10);
        card.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);

        addRow(card, gbc, 1, "First Name:", firstNameField, labelFont);
        addRow(card, gbc, 2, "Last Name:", lastNameField, labelFont);
        addRow(card, gbc, 3, "IC Number:", icNumberField, labelFont);
        addRow(card, gbc, 4, "Username:", usernameField, labelFont);
        addRow(card, gbc, 5, "User Type:", userTypeDropdown, labelFont);
        addRow(card, gbc, 6, "Leave Count:", leaveCountField, labelFont);

        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 0, 10);
        card.add(createBtn, gbc);

        add(card);

        createBtn.addActionListener(e -> createUser());
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int y,
                        String labelText, JComponent field, Font font) {

        gbc.gridx = 0;
        gbc.gridy = y;
        JLabel label = new JLabel(labelText);
        label.setFont(font);
        label.setForeground(DarkTheme.TEXT);
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void styleTextField(JTextField field, Font font, Dimension size) {
        field.setFont(font);
        field.setPreferredSize(size);
        field.setBackground(new Color(28, 28, 28));
        field.setForeground(DarkTheme.TEXT);
        field.setCaretColor(DarkTheme.TEXT);
        field.setBorder(new CompoundBorder(
                new LineBorder(DarkTheme.BORDER, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
    }

    private void styleComboBox(JComboBox<String> comboBox, Font font, Dimension size) {

        comboBox.setFont(font);
        comboBox.setPreferredSize(size);
        comboBox.setBackground(new Color(28, 28, 28));
        comboBox.setForeground(DarkTheme.TEXT);
        comboBox.setBorder(new CompoundBorder(
                new LineBorder(DarkTheme.BORDER, 1, true),
                new EmptyBorder(2, 8, 2, 8)
        ));
        comboBox.setFocusable(false);

        comboBox.setUI(new BasicComboBoxUI() {

            @Override
            protected JButton createArrowButton() {

                return new JButton() {

                    {
                        setPreferredSize(new Dimension(40, 40));
                        setBackground(new Color(28, 28, 28));
                        setForeground(Color.WHITE);
                        setBorder(BorderFactory.createEmptyBorder());
                        setFocusPainted(false);
                        setOpaque(true);
                        setContentAreaFilled(false);
                    }

                    @Override
                    protected void paintComponent(Graphics g) {
                        g.setColor(new Color(28, 28, 28));
                        g.fillRect(0, 0, getWidth(), getHeight());

                        g.setColor(Color.WHITE);
                        int x = getWidth() / 2;
                        int y = getHeight() / 2;

                        int size = 6;
                        Polygon arrow = new Polygon();
                        arrow.addPoint(x - size, y - 2);
                        arrow.addPoint(x + size, y - 2);
                        arrow.addPoint(x, y + size);

                        g.fillPolygon(arrow);
                    }
                };
            }
        });

        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus
            ) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus
                );

                label.setFont(font);
                label.setBorder(new EmptyBorder(6, 8, 6, 8));
                label.setOpaque(true);

                if (index == -1) {
                    label.setBackground(new Color(28, 28, 28));
                    label.setForeground(DarkTheme.TEXT);
                } else if (isSelected) {
                    label.setBackground(DarkTheme.ACCENT);
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(new Color(32, 33, 36));
                    label.setForeground(new Color(232, 234, 237));
                }

                return label;
            }
        });

        SwingUtilities.invokeLater(() -> {
            for (Component c : comboBox.getComponents()) {
                if (c instanceof JButton btn) {
                    btn.setBackground(new Color(28, 28, 28));
                    btn.setForeground(Color.WHITE);
                    btn.setOpaque(true);
                    btn.setContentAreaFilled(true);
                    btn.setBorder(BorderFactory.createEmptyBorder());
                    btn.setFocusPainted(false);
                }
            }
        });
    }

    private void createUser() {
        try {
            String role = (String) userTypeDropdown.getSelectedItem();

            if (role == null || role.equals("Select Role")) {
                DialogUtil.styleMessageDialog();
                JOptionPane.showMessageDialog(this, "Select role", "Message", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            boolean confirmed = DialogUtil.showConfirmDialog(
                    this,
                    "Confirm Create User",
                    "Are you sure you want to create this user?"
            );

            if (!confirmed) {
                return;
            }

            CreateUserDto dto = new CreateUserDto();
            dto.setFirstName(firstNameField.getText().trim());
            dto.setLastName(lastNameField.getText().trim());
            dto.setIcNumber(icNumberField.getText().trim());
            dto.setUsername(usernameField.getText().trim());
            dto.setUserType(UserType.valueOf(role));
            dto.setLeaveCount(Integer.parseInt(leaveCountField.getText().trim()));

            if (controller.createUser(dto)) {
                DialogUtil.styleMessageDialog();
                JOptionPane.showMessageDialog(this, "User created (Password: Pass@123)", "Success", JOptionPane.INFORMATION_MESSAGE);
                clear();
            }

        } catch (Exception ex) {
            DialogUtil.styleMessageDialog();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clear() {
        firstNameField.setText("");
        lastNameField.setText("");
        icNumberField.setText("");
        usernameField.setText("");
        leaveCountField.setText("");
        userTypeDropdown.setSelectedIndex(0);
    }
}