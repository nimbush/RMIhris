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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;

public class UserDetailsPanel extends BasePanel {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^(97|98)\\d{8}$");

    private final ClientController controller;
    private final DefaultTableModel tableModel;
    private final JTable table;

    public UserDetailsPanel(ClientController controller) {
        this.controller = controller;

        this.tableModel = new DefaultTableModel(
                new String[]{
                        "ID", "First Name", "Last Name", "IC Number", "User Type",
                        "Father Name", "Mother Name", "Phone", "Address", "Leave Count", "Action"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 10;
            }
        };

        this.table = new JTable(tableModel);

        initUi();
    }

    private void initUi() {
        setLayout(new BorderLayout());
        setBackground(DarkTheme.BG);

        JPanel card = new JPanel(new BorderLayout(0, 16));
        card.setBackground(DarkTheme.PANEL);
        card.setBorder(new CompoundBorder(
                new LineBorder(DarkTheme.BORDER, 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel("User Details");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(DarkTheme.TEXT);

        styleTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(DarkTheme.PANEL);
        scrollPane.setBackground(DarkTheme.PANEL);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);

        add(card, BorderLayout.CENTER);
    }

    private void styleTable() {
        table.setRowHeight(42);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setBackground(DarkTheme.PANEL);
        table.setForeground(DarkTheme.TEXT);
        table.setSelectionBackground(DarkTheme.ACCENT);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(DarkTheme.BORDER);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFillsViewportHeight(true);
        table.setBorder(BorderFactory.createEmptyBorder());

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column
            ) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column
                );

                label.setBackground(new Color(32, 33, 36));
                label.setForeground(new Color(232, 234, 237));
                label.setFont(new Font("Segoe UI", Font.BOLD, 13));
                label.setBorder(BorderFactory.createLineBorder(DarkTheme.BORDER));
                label.setOpaque(true);
                label.setHorizontalAlignment(SwingConstants.CENTER);

                return label;
            }
        });

        DefaultTableCellRenderer bodyRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column
            ) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column
                );

                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(new EmptyBorder(0, 8, 0, 8));

                if (isSelected) {
                    label.setBackground(DarkTheme.ACCENT);
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(DarkTheme.PANEL);
                    label.setForeground(DarkTheme.TEXT);
                }

                return label;
            }
        };

        for (int i = 0; i < table.getColumnCount() - 1; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(bodyRenderer);
        }

        table.getColumn("Action").setMinWidth(180);
        table.getColumn("Action").setPreferredWidth(200);
        table.getColumn("Action").setMaxWidth(220);

        table.getColumn("Action").setCellRenderer(new UserActionCellRenderer());
        table.getColumn("Action").setCellEditor(
                new UserActionCellEditor(new JCheckBox(), table, controller, this)
        );
    }

    public void loadUsers() {
        try {
            tableModel.setRowCount(0);

            List<User> users = controller.getUserDetails();

            for (User u : users) {
                tableModel.addRow(new Object[]{
                        u.getId(),
                        u.getFirstName(),
                        u.getLastName(),
                        u.getIcNumber(),
                        u.getUserType(),
                        u.getFatherName(),
                        u.getMotherName(),
                        u.getPhoneNumber(),
                        u.getAddress(),
                        u.getLeaveCount(),
                        "Action"
                });
            }
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void showError(String message) {
        DialogUtil.styleMessageDialog();
        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void showInfo(String message) {
        DialogUtil.styleMessageDialog();
        JOptionPane.showMessageDialog(
                this,
                message,
                "Info",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private static class UserActionCellRenderer extends JPanel implements TableCellRenderer {

        private final ModernButton editButton = new ModernButton("Edit");
        private final ModernButton deleteButton = new ModernButton("Delete");

        public UserActionCellRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 8, 4));
            setOpaque(true);
            setBackground(DarkTheme.PANEL);

            editButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
            deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
            deleteButton.setDangerStyle();

            add(editButton);
            add(deleteButton);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {
            setBackground(isSelected ? DarkTheme.ACCENT : DarkTheme.PANEL);
            return this;
        }
    }

    private static class UserActionCellEditor extends AbstractCellEditor implements TableCellEditor {

        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        private final ModernButton editButton = new ModernButton("Edit");
        private final ModernButton deleteButton = new ModernButton("Delete");

        private final JTable table;
        private final ClientController controller;
        private final UserDetailsPanel parentPanel;
        private int currentRow;

        public UserActionCellEditor(
                JCheckBox checkBox,
                JTable table,
                ClientController controller,
                UserDetailsPanel parentPanel
        ) {
            this.table = table;
            this.controller = controller;
            this.parentPanel = parentPanel;

            panel.setOpaque(true);
            panel.setBackground(DarkTheme.PANEL);

            editButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
            deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
            deleteButton.setDangerStyle();

            panel.add(editButton);
            panel.add(deleteButton);

            editButton.addActionListener(e -> editUser());
            deleteButton.addActionListener(e -> deleteUser());
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table,
                Object value,
                boolean isSelected,
                int row,
                int column
        ) {
            currentRow = row;
            panel.setBackground(DarkTheme.PANEL);
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "Action";
        }

        private void editUser() {
            try {
                String icNumber = table.getValueAt(currentRow, 3).toString();
                String fatherName = safeValue(currentRow, 5);
                String motherName = safeValue(currentRow, 6);
                String phone = safeValue(currentRow, 7);
                String address = safeValue(currentRow, 8);
                int currentLeaveCount = Integer.parseInt(table.getValueAt(currentRow, 9).toString());

                JTextField fatherField = new JTextField(fatherName);
                JTextField motherField = new JTextField(motherName);
                JTextField phoneField = new JTextField(phone);
                JTextField addressField = new JTextField(address);
                JTextField addLeaveField = new JTextField();

                styleDialogField(fatherField);
                styleDialogField(motherField);
                styleDialogField(phoneField);
                styleDialogField(addressField);
                styleDialogField(addLeaveField);

                JLabel currentLeaveLabel = new JLabel(String.valueOf(currentLeaveCount));
                currentLeaveLabel.setForeground(DarkTheme.TEXT);
                currentLeaveLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

                JPanel form = new JPanel(new GridBagLayout());
                form.setBackground(DarkTheme.PANEL);
                form.setBorder(new EmptyBorder(10, 10, 10, 10));

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(8, 8, 8, 8);
                gbc.anchor = GridBagConstraints.WEST;
                gbc.fill = GridBagConstraints.HORIZONTAL;

                addDialogRow(form, gbc, 0, "Father Name:", fatherField);
                addDialogRow(form, gbc, 1, "Mother Name:", motherField);
                addDialogRow(form, gbc, 2, "Phone Number:", phoneField);
                addDialogRow(form, gbc, 3, "Address:", addressField);
                addDialogRow(form, gbc, 4, "Current Leave Count:", currentLeaveLabel);
                addDialogRow(form, gbc, 5, "Add Leave Count:", addLeaveField);

                boolean confirmed = DialogUtil.showFormDialog(
                        parentPanel,
                        "Edit User - " + icNumber,
                        form,
                        "OK"
                );

                if (!confirmed) {
                    return;
                }

                String phoneNumber = phoneField.getText().trim();

                if (!phoneNumber.isEmpty() && !PHONE_PATTERN.matcher(phoneNumber).matches()) {
                    parentPanel.showError(
                            "Phone number must be a valid Nepali mobile number starting with 97 or 98 and be 10 digits long."
                    );
                    fireEditingStopped();
                    return;
                }

                int extraLeave = 0;
                String addLeaveText = addLeaveField.getText().trim();

                if (!addLeaveText.isEmpty()) {
                    extraLeave = Integer.parseInt(addLeaveText);
                    if (extraLeave < 0) {
                        parentPanel.showError("Added leave cannot be negative.");
                        fireEditingStopped();
                        return;
                    }
                }

                UpdateUserDto dto = new UpdateUserDto();
                dto.setIcNumber(icNumber);
                dto.setFatherName(fatherField.getText().trim());
                dto.setMotherName(motherField.getText().trim());
                dto.setPhoneNumber(phoneNumber);
                dto.setAddress(addressField.getText().trim());

                boolean updated = controller.updateUser(dto);

                if (updated) {
                    if (extraLeave > 0) {
                        boolean leaveUpdated = controller.addLeaveCount(icNumber, extraLeave);
                        if (!leaveUpdated) {
                            parentPanel.showError("User updated, but failed to add leave count.");
                            parentPanel.loadUsers();
                            fireEditingStopped();
                            return;
                        }
                    }

                    parentPanel.showInfo("User updated successfully.");
                    parentPanel.loadUsers();
                } else {
                    parentPanel.showError("Failed to update user.");
                }
            } catch (NumberFormatException ex) {
                parentPanel.showError("Add Leave Count must be a valid number.");
            } catch (Exception ex) {
                parentPanel.showError(ex.getMessage());
            } finally {
                fireEditingStopped();
            }
        }

        private void deleteUser() {
            try {
                String icNumber = table.getValueAt(currentRow, 3).toString();

                boolean confirmed = DialogUtil.showConfirmDialog(
                        parentPanel,
                        "Confirm Delete",
                        "Delete user with IC Number: " + icNumber + "?"
                );

                if (!confirmed) {
                    return;
                }

                boolean deleted = controller.deleteUser(icNumber);

                if (deleted) {
                    parentPanel.showInfo("User deleted successfully.");
                    parentPanel.loadUsers();
                } else {
                    parentPanel.showError("Failed to delete user.");
                }
            } catch (Exception ex) {
                parentPanel.showError(ex.getMessage());
            } finally {
                fireEditingStopped();
            }
        }

        private static void addDialogRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent field) {
            JLabel label = new JLabel(labelText);
            label.setForeground(DarkTheme.TEXT);
            label.setFont(new Font("Segoe UI", Font.BOLD, 14));

            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0;
            panel.add(label, gbc);

            gbc.gridx = 1;
            gbc.weightx = 1.0;
            panel.add(field, gbc);
        }

        private static void styleDialogField(JTextField field) {
            field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            field.setBackground(new Color(36, 36, 36));
            field.setForeground(DarkTheme.TEXT);
            field.setCaretColor(DarkTheme.TEXT);
            field.setBorder(new CompoundBorder(
                    new LineBorder(DarkTheme.BORDER, 1, true),
                    new EmptyBorder(8, 10, 8, 10)
            ));
        }

        private String safeValue(int row, int column) {
            Object value = table.getValueAt(row, column);
            return value == null ? "" : value.toString();
        }
    }
}