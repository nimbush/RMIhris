package com.techspire.project.client.view;

import com.techspire.project.client.controller.ClientController;
import com.techspire.project.model.LeaveRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

public class LeaveStatusPanel extends JPanel {

    private final ClientController controller;
    private final DefaultTableModel tableModel;
    private final JTable table;

    public LeaveStatusPanel(ClientController controller) {
        this.controller = controller;

        this.tableModel = new DefaultTableModel(
                new String[]{"ID", "User ID", "Leave Days", "Status", "Applied Date", "Action"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        this.table = new JTable(tableModel);

        initUi();
    }

    private void initUi() {
        setLayout(new BorderLayout());

        table.setRowHeight(38);
        table.getColumn("Action").setMinWidth(180);
        table.getColumn("Action").setPreferredWidth(210);
        table.getColumn("Action").setMaxWidth(230);

        table.getColumn("Action").setCellRenderer(new LeaveActionCellRenderer());
        table.getColumn("Action").setCellEditor(
                new LeaveActionCellEditor(new JCheckBox(), table, controller, this)
        );

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void loadLeaveRecords() {
        try {
            tableModel.setRowCount(0);

            table.getColumnModel().getColumn(1).setHeaderValue("User ID");
            table.getTableHeader().repaint();

            List<LeaveRecord> records = controller.getLeaveStatus();

            for (LeaveRecord r : records) {
                tableModel.addRow(new Object[]{
                        r.getId(),
                        r.getUserId(),
                        r.getLeaveDays(),
                        r.getStatus(),
                        r.getAppliedDate(),
                        "Action"
                });
            }

            showActionColumn();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadLeaveRecordsByIcNumber(String icNumber) {
        try {
            tableModel.setRowCount(0);

            table.getColumnModel().getColumn(1).setHeaderValue("IC Number");
            table.getTableHeader().repaint();

            List<LeaveRecord> records = controller.getLeaveStatusByIcNumber(icNumber);

            for (LeaveRecord r : records) {
                tableModel.addRow(new Object[]{
                        r.getId(),
                        icNumber,
                        r.getLeaveDays(),
                        r.getStatus(),
                        r.getAppliedDate(),
                        ""
                });
            }

            hideActionColumn();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showActionColumn() {
        table.getColumn("Action").setMinWidth(180);
        table.getColumn("Action").setPreferredWidth(210);
        table.getColumn("Action").setMaxWidth(230);
    }

    private void hideActionColumn() {
        table.getColumn("Action").setMinWidth(0);
        table.getColumn("Action").setMaxWidth(0);
        table.getColumn("Action").setPreferredWidth(0);
        table.getColumn("Action").setWidth(0);
    }

    private static class LeaveActionCellRenderer extends JPanel implements TableCellRenderer {

        private final JButton approveButton = new JButton("Approve");
        private final JButton rejectButton = new JButton("Reject");

        public LeaveActionCellRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 6, 2));
            setOpaque(true);

            styleApproveButton(approveButton);
            styleRejectButton(rejectButton);

            add(approveButton);
            add(rejectButton);
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
            String status = table.getValueAt(row, 3).toString();
            boolean pending = status.equalsIgnoreCase("PENDING");

            approveButton.setEnabled(pending);
            rejectButton.setEnabled(pending);

            return this;
        }
    }

    private static class LeaveActionCellEditor extends AbstractCellEditor implements TableCellEditor {

        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 2));
        private final JButton approveButton = new JButton("Approve");
        private final JButton rejectButton = new JButton("Reject");

        private final JTable table;
        private final ClientController controller;
        private final LeaveStatusPanel parentPanel;

        private int row;

        public LeaveActionCellEditor(
                JCheckBox checkBox,
                JTable table,
                ClientController controller,
                LeaveStatusPanel parentPanel
        ) {
            this.table = table;
            this.controller = controller;
            this.parentPanel = parentPanel;

            styleApproveButton(approveButton);
            styleRejectButton(rejectButton);

            panel.add(approveButton);
            panel.add(rejectButton);

            approveButton.addActionListener(e -> approve());
            rejectButton.addActionListener(e -> reject());
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table,
                Object value,
                boolean isSelected,
                int row,
                int column
        ) {
            this.row = row;

            String status = table.getValueAt(row, 3).toString();
            boolean pending = status.equalsIgnoreCase("PENDING");

            approveButton.setEnabled(pending);
            rejectButton.setEnabled(pending);

            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "ACTION";
        }

        private void approve() {
            try {
                Long leaveId = Long.parseLong(table.getValueAt(row, 0).toString());
                boolean result = controller.approveLeave(leaveId);

                if (result) {
                    JOptionPane.showMessageDialog(parentPanel, "Leave Approved");
                    parentPanel.loadLeaveRecords();
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parentPanel, ex.getMessage());
            }

            fireEditingStopped();
        }

        private void reject() {
            try {
                Long leaveId = Long.parseLong(table.getValueAt(row, 0).toString());
                boolean result = controller.rejectLeave(leaveId);

                if (result) {
                    JOptionPane.showMessageDialog(parentPanel, "Leave Rejected");
                    parentPanel.loadLeaveRecords();
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parentPanel, ex.getMessage());
            }

            fireEditingStopped();
        }
    }

    private static void styleApproveButton(JButton button) {
        button.setBackground(new Color(13, 110, 253));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    private static void styleRejectButton(JButton button) {
        button.setBackground(new Color(220, 53, 69));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }
}