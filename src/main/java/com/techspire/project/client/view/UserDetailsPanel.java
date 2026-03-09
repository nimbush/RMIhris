package com.techspire.project.client.view;

import com.techspire.project.client.controller.ClientController;
import com.techspire.project.dto.UpdateUserDto;
import com.techspire.project.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;


public class UserDetailsPanel extends JPanel {

    private final ClientController controller;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(97|98)\\d{8}$");

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

        table.setRowHeight(40);
        table.getColumn("Action").setMinWidth(180);
        table.getColumn("Action").setPreferredWidth(200);
        table.getColumn("Action").setMaxWidth(220);

        table.getColumn("Action").setCellRenderer(new UserActionCellRenderer());
        table.getColumn("Action").setCellEditor(
                new UserActionCellEditor(new JCheckBox(), table, controller, this)
        );

        add(new JScrollPane(table), BorderLayout.CENTER);
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
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private static class UserActionCellRenderer extends JPanel implements TableCellRenderer {

        private final JButton editButton = new JButton("Edit");
        private final JButton deleteButton = new JButton("Delete");

        public UserActionCellRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));

            editButton.setFocusPainted(false);
            deleteButton.setFocusPainted(false);

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
            return this;
        }
    }

    private static class UserActionCellEditor extends AbstractCellEditor implements TableCellEditor {

        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        private final JButton editButton = new JButton("Edit");
        private final JButton deleteButton = new JButton("Delete");

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

            editButton.setFocusPainted(false);
            deleteButton.setFocusPainted(false);

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
                addLeaveField.setToolTipText("Enter extra leave to add");

                Object[] fields = {
                        "Father Name:", fatherField,
                        "Mother Name:", motherField,
                        "Phone Number:", phoneField,
                        "Address:", addressField,
                        "Current Leave Count:", new JLabel(String.valueOf(currentLeaveCount)),
                        "Add Leave Count:", addLeaveField
                };

                int option = JOptionPane.showConfirmDialog(
                        parentPanel,
                        fields,
                        "Edit User - " + icNumber,
                        JOptionPane.OK_CANCEL_OPTION
                );

                if (option == JOptionPane.OK_OPTION) {
                    String phoneNumber = phoneField.getText().trim();

                    if (!phoneNumber.isEmpty() && !PHONE_PATTERN.matcher(phoneNumber).matches()) {
                        JOptionPane.showMessageDialog(
                                parentPanel,
                                "Phone number must be a valid Nepali mobile number starting with 97 or 98 and be 10 digits long.",
                                "Validation Error",
                                JOptionPane.WARNING_MESSAGE
                        );
                        fireEditingStopped();
                        return;
                    }

                    int extraLeave = 0;
                    String addLeaveText = addLeaveField.getText().trim();

                    if (!addLeaveText.isEmpty()) {
                        extraLeave = Integer.parseInt(addLeaveText);
                        if (extraLeave < 0) {
                            JOptionPane.showMessageDialog(parentPanel, "Added leave cannot be negative.");
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
                                JOptionPane.showMessageDialog(parentPanel, "User updated, but failed to add leave count.");
                                parentPanel.loadUsers();
                                fireEditingStopped();
                                return;
                            }
                        }

                        JOptionPane.showMessageDialog(parentPanel, "User updated successfully.");
                        parentPanel.loadUsers();
                    } else {
                        JOptionPane.showMessageDialog(parentPanel, "Failed to update user.");
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parentPanel, "Add Leave Count must be a valid number.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        parentPanel,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            } finally {
                fireEditingStopped();
            }
        }

        private void deleteUser() {
            try {
                String icNumber = table.getValueAt(currentRow, 3).toString();

                int confirm = JOptionPane.showConfirmDialog(
                        parentPanel,
                        "Delete user with IC Number: " + icNumber + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean deleted = controller.deleteUser(icNumber);

                    if (deleted) {
                        JOptionPane.showMessageDialog(parentPanel, "User deleted successfully.");
                        parentPanel.loadUsers();
                    } else {
                        JOptionPane.showMessageDialog(parentPanel, "Failed to delete user.");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        parentPanel,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            } finally {
                fireEditingStopped();
            }
        }

        private String safeValue(int row, int column) {
            Object value = table.getValueAt(row, column);
            return value == null ? "" : value.toString();
        }
    }
}