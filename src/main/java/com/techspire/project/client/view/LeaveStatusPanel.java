package com.techspire.project.client.view;

import com.techspire.project.client.controller.ClientController;
import com.techspire.project.client.view.ui.BasePanel;
import com.techspire.project.client.view.ui.DarkTheme;
import com.techspire.project.client.view.ui.DialogUtil;
import com.techspire.project.client.view.ui.ModernButton;
import com.techspire.project.model.LeaveRecord;

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

public class LeaveStatusPanel extends BasePanel {

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
        setBackground(DarkTheme.BG);

        JPanel card = new JPanel(new BorderLayout(0, 16));
        card.setBackground(DarkTheme.PANEL);
        card.setBorder(new CompoundBorder(
                new LineBorder(DarkTheme.BORDER, 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel("Leave Status");
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
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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
                label.setFont(new Font("Segoe UI", Font.BOLD, 14));
                label.setBorder(BorderFactory.createLineBorder(DarkTheme.BORDER));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setOpaque(true);

                return label;
            }
        });

        for (int i = 0; i < table.getColumnCount() - 1; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new StatusAwareRenderer());
        }

        table.getColumn("Action").setMinWidth(180);
        table.getColumn("Action").setPreferredWidth(210);
        table.getColumn("Action").setMaxWidth(230);
        table.getColumn("Action").setCellRenderer(new LeaveActionCellRenderer());
        table.getColumn("Action").setCellEditor(
                new LeaveActionCellEditor(new JCheckBox(), table, controller, this)
        );
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
            DialogUtil.styleMessageDialog();
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
            DialogUtil.styleMessageDialog();
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

    private static class StatusAwareRenderer extends DefaultTableCellRenderer {

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
                return label;
            }

            label.setBackground(DarkTheme.PANEL);
            label.setForeground(DarkTheme.TEXT);

            Object statusObj = table.getValueAt(row, 3);
            String status = statusObj == null ? "" : statusObj.toString().toUpperCase();

            if (column == 3) {
                switch (status) {
                    case "APPROVED" -> {
                        label.setForeground(DarkTheme.SUCCESS);
                        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    }
                    case "REJECTED" -> {
                        label.setForeground(DarkTheme.ERROR);
                        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    }
                    case "PENDING" -> {
                        label.setForeground(DarkTheme.WARNING);
                        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    }
                    default -> {
                        label.setForeground(DarkTheme.TEXT);
                        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    }
                }
            } else {
                label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            }

            return label;
        }
    }

    private static class LeaveActionCellRenderer extends JPanel implements TableCellRenderer {

        private final ModernButton approveButton = new ModernButton("Approve");
        private final ModernButton rejectButton = new ModernButton("Reject");

        public LeaveActionCellRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 8, 4));
            setOpaque(true);
            setBackground(DarkTheme.PANEL);

            approveButton.setSuccessStyle();
            rejectButton.setDangerStyle();

            approveButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
            rejectButton.setFont(new Font("Segoe UI", Font.BOLD, 12));

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

            setBackground(isSelected ? DarkTheme.ACCENT : DarkTheme.PANEL);
            return this;
        }
    }

    private static class LeaveActionCellEditor extends AbstractCellEditor implements TableCellEditor {

        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        private final ModernButton approveButton = new ModernButton("Approve");
        private final ModernButton rejectButton = new ModernButton("Reject");

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

            panel.setBackground(DarkTheme.PANEL);

            approveButton.setSuccessStyle();
            rejectButton.setDangerStyle();

            approveButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
            rejectButton.setFont(new Font("Segoe UI", Font.BOLD, 12));

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

            panel.setBackground(DarkTheme.PANEL);
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "ACTION";
        }

        private void approve() {
            try {
                boolean confirmed = DialogUtil.showConfirmDialog(
                        parentPanel,
                        "Confirm Approval",
                        "Are you sure you want to approve this leave?"
                );

                if (!confirmed) {
                    fireEditingStopped();
                    return;
                }

                Long leaveId = Long.parseLong(table.getValueAt(row, 0).toString());
                boolean result = controller.approveLeave(leaveId);

                if (result) {
                    DialogUtil.styleMessageDialog();
                    JOptionPane.showMessageDialog(parentPanel, "Leave Approved");
                    parentPanel.loadLeaveRecords();
                }

            } catch (Exception ex) {
                DialogUtil.styleMessageDialog();
                JOptionPane.showMessageDialog(parentPanel, ex.getMessage());
            }

            fireEditingStopped();
        }

        private void reject() {
            try {
                boolean confirmed = DialogUtil.showConfirmDialog(
                        parentPanel,
                        "Confirm Rejection",
                        "Are you sure you want to reject this leave?"
                );

                if (!confirmed) {
                    fireEditingStopped();
                    return;
                }

                Long leaveId = Long.parseLong(table.getValueAt(row, 0).toString());
                boolean result = controller.rejectLeave(leaveId);

                if (result) {
                    DialogUtil.styleMessageDialog();
                    JOptionPane.showMessageDialog(parentPanel, "Leave Rejected");
                    parentPanel.loadLeaveRecords();
                }

            } catch (Exception ex) {
                DialogUtil.styleMessageDialog();
                JOptionPane.showMessageDialog(parentPanel, ex.getMessage());
            }

            fireEditingStopped();
        }
    }
}