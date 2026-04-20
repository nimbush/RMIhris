package com.techspire.project.client.view;

import com.techspire.project.client.controller.ClientController;
import com.techspire.project.client.view.ui.BasePanel;
import com.techspire.project.client.view.ui.DarkTheme;
import com.techspire.project.client.view.ui.DialogUtil;
import com.techspire.project.client.view.ui.ModernButton;
import com.techspire.project.dto.UserDetailedReportDto;
import com.techspire.project.dto.UserReportDto;
import com.techspire.project.model.User;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ReportPanel extends BasePanel {

    private final ClientController controller;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final ModernButton generateReportButton;

    private List<UserReportDto> loadedReports = new ArrayList<>();

    public ReportPanel(ClientController controller) {
        this.controller = controller;

        this.tableModel = new DefaultTableModel(
                new String[]{"User ID", "Full Name", "User Type", "Remaining Leave", "Total Leave"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        this.table = new JTable(tableModel);
        this.generateReportButton = new ModernButton("Generate Report");

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

        JLabel title = new JLabel("Yearly Report");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(DarkTheme.TEXT);

        styleTable();

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(DarkTheme.PANEL);
        scroll.setBackground(DarkTheme.PANEL);

        generateReportButton.setPreferredSize(new Dimension(170, 42));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(DarkTheme.PANEL);
        bottom.add(generateReportButton);

        card.add(title, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);

        add(card, BorderLayout.CENTER);

        generateReportButton.addActionListener(e -> generateReport());
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

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
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

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    public void loadReports() {
        try {
            tableModel.setRowCount(0);
            loadedReports = controller.getYearlyReport();

            for (UserReportDto r : loadedReports) {
                tableModel.addRow(new Object[]{
                        r.getUserId(),
                        r.getFullName(),
                        r.getUserType(),
                        r.getRemainingLeave(),
                        r.getTotalLeave()
                });
            }
        } catch (Exception ex) {
            DialogUtil.styleMessageDialog();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateReport() {
        try {
            if (loadedReports == null || loadedReports.isEmpty()) {
                DialogUtil.styleMessageDialog();
                JOptionPane.showMessageDialog(this, "No users available.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String[] options = loadedReports.stream()
                    .map(r -> r.getFullName() + " (ID: " + r.getUserId() + ")")
                    .toArray(String[]::new);

            JComboBox<String> userDropdown = new JComboBox<>(options);
            styleComboBox(userDropdown);

            JLabel selectLabel = new JLabel("Select User");
            selectLabel.setForeground(DarkTheme.TEXT);
            selectLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

            JButton okButton = createStyledButton("Generate", new Color(76, 132, 255));
            okButton.setPreferredSize(new Dimension(120, 38));

            JButton cancelButton = createStyledButton("Cancel", new Color(220, 53, 69));
            cancelButton.setPreferredSize(new Dimension(120, 38));

            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Generate Report", true);
            dialog.setSize(360, 190);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout());
            dialog.getContentPane().setBackground(DarkTheme.BG);

            JPanel content = new JPanel(new BorderLayout(0, 12));
            content.setBackground(DarkTheme.PANEL);
            content.setBorder(new EmptyBorder(16, 16, 16, 16));
            content.add(selectLabel, BorderLayout.NORTH);
            content.add(userDropdown, BorderLayout.CENTER);

            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            actions.setBackground(DarkTheme.PANEL);
            actions.add(cancelButton);
            actions.add(okButton);

            content.add(actions, BorderLayout.SOUTH);
            dialog.setContentPane(content);

            final String[] selectedValue = {null};

            okButton.addActionListener(e -> {
                selectedValue[0] = (String) userDropdown.getSelectedItem();
                dialog.dispose();
            });

            cancelButton.addActionListener(e -> dialog.dispose());

            dialog.setVisible(true);

            String selected = selectedValue[0];
            if (selected == null) {
                return;
            }

            UserReportDto selectedUser = loadedReports.stream()
                    .filter(r -> (r.getFullName() + " (ID: " + r.getUserId() + ")").equals(selected))
                    .findFirst()
                    .orElse(null);

            if (selectedUser == null) {
                DialogUtil.styleMessageDialog();
                JOptionPane.showMessageDialog(this, "Selected user not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<User> users = controller.getUserDetails();

            User matched = users.stream()
                    .filter(u -> u.getId().equals(selectedUser.getUserId()))
                    .findFirst()
                    .orElse(null);

            if (matched == null) {
                DialogUtil.styleMessageDialog();
                JOptionPane.showMessageDialog(this, "Unable to find user details.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            UserDetailedReportDto report = controller.generateUserReport(matched.getIcNumber());

            if (report == null) {
                DialogUtil.styleMessageDialog();
                JOptionPane.showMessageDialog(this, "No report found for selected user.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            showReportDialog(report);

        } catch (Exception ex) {
            DialogUtil.styleMessageDialog();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showReportDialog(UserDetailedReportDto report) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Generated Report", true);
        dialog.setSize(950, 620);
        dialog.setLocationRelativeTo(this);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(DarkTheme.BG);
        root.setBorder(new EmptyBorder(15, 15, 15, 15));

        JTextArea summary = new JTextArea();
        summary.setEditable(false);
        summary.setLineWrap(true);
        summary.setWrapStyleWord(true);
        summary.setRows(8);
        summary.setBackground(DarkTheme.PANEL);
        summary.setForeground(DarkTheme.TEXT);
        summary.setCaretColor(DarkTheme.TEXT);
        summary.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        summary.setBorder(new CompoundBorder(
                new LineBorder(DarkTheme.BORDER, 1, true),
                new EmptyBorder(12, 12, 12, 12)
        ));

        summary.setText(
                "Full Name: " + report.getFullName() + "\n" +
                        "IC Number: " + report.getIcNumber() + "\n" +
                        "User Type: " + report.getUserType() + "\n" +
                        "Remaining Leave: " + report.getRemainingLeave() + "\n" +
                        "Total Leave: " + report.getTotalLeave() + "\n" +
                        "Total Applications: " + report.getTotalApplications()
        );

        DefaultTableModel historyModel = new DefaultTableModel(
                new String[]{"From Date", "To Date", "Leave Days", "Status", "Applied Date"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable historyTable = new JTable(historyModel);
        styleHistoryTable(historyTable);

        if (report.getLeaveHistory() != null) {
            for (UserDetailedReportDto.LeaveHistoryItemDto item : report.getLeaveHistory()) {
                historyModel.addRow(new Object[]{
                        item.getFromDate(),
                        item.getToDate(),
                        item.getLeaveDays(),
                        item.getStatus(),
                        item.getAppliedDate()
                });
            }
        }

        JScrollPane summaryScroll = new JScrollPane(summary);
        summaryScroll.setBorder(BorderFactory.createEmptyBorder());
        summaryScroll.getViewport().setBackground(DarkTheme.PANEL);
        summaryScroll.setBackground(DarkTheme.PANEL);

        JScrollPane historyScroll = new JScrollPane(historyTable);
        historyScroll.setBorder(BorderFactory.createEmptyBorder());
        historyScroll.getViewport().setBackground(DarkTheme.PANEL);
        historyScroll.setBackground(DarkTheme.PANEL);

        ModernButton closeButton = new ModernButton("Close");
        closeButton.setSecondaryStyle();
        closeButton.setPreferredSize(new Dimension(100, 40));
        closeButton.addActionListener(e -> dialog.dispose());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(DarkTheme.BG);
        bottom.add(closeButton);

        root.add(summaryScroll, BorderLayout.NORTH);
        root.add(historyScroll, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setVisible(true);
    }

    private void styleHistoryTable(JTable historyTable) {
        historyTable.setRowHeight(38);
        historyTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        historyTable.setBackground(DarkTheme.PANEL);
        historyTable.setForeground(DarkTheme.TEXT);
        historyTable.setSelectionBackground(DarkTheme.ACCENT);
        historyTable.setSelectionForeground(Color.WHITE);
        historyTable.setGridColor(DarkTheme.BORDER);
        historyTable.setShowGrid(true);
        historyTable.setIntercellSpacing(new Dimension(0, 1));
        historyTable.setFillsViewportHeight(true);

        JTableHeader header = historyTable.getTableHeader();
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
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setOpaque(true);

                return label;
            }
        });

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
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

                if (column == 3 && value != null && !isSelected) {
                    String status = value.toString().toUpperCase();
                    switch (status) {
                        case "APPROVED" -> label.setForeground(DarkTheme.SUCCESS);
                        case "REJECTED" -> label.setForeground(DarkTheme.ERROR);
                        case "PENDING" -> label.setForeground(DarkTheme.WARNING);
                    }
                }

                return label;
            }
        };

        for (int i = 0; i < historyTable.getColumnCount(); i++) {
            historyTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        Font font = new Font("Segoe UI", Font.PLAIN, 14);

        comboBox.setFont(font);
        comboBox.setPreferredSize(new Dimension(260, 38));
        comboBox.setBackground(new Color(36, 36, 36));
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

        ListCellRenderer<? super String> defaultRenderer = comboBox.getRenderer();
        comboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            Component c = defaultRenderer.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus
            );

            c.setFont(font);

            if (isSelected) {
                c.setBackground(DarkTheme.ACCENT);
                c.setForeground(Color.WHITE);
            } else {
                c.setBackground(new Color(36, 36, 36));
                c.setForeground(DarkTheme.TEXT);
            }

            if (c instanceof JComponent jc) {
                jc.setBorder(new EmptyBorder(6, 10, 6, 10));
                jc.setOpaque(true);
            }

            return c;
        });
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        return button;
    }
}