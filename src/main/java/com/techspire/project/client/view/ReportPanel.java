package com.techspire.project.client.view;

import com.techspire.project.client.controller.ClientController;
import com.techspire.project.dto.UserDetailedReportDto;
import com.techspire.project.dto.UserReportDto;
import com.techspire.project.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ReportPanel extends JPanel {

    private final ClientController controller;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JButton generateReportButton;

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
        this.generateReportButton = new JButton("Generate Report");

        initUi();
    }

    private void initUi() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        table.setRowHeight(48);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 17));
        table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 38));

        generateReportButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        generateReportButton.setPreferredSize(new Dimension(180, 44));
        generateReportButton.setFocusPainted(false);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(generateReportButton);

        generateReportButton.addActionListener(e -> generateReport());

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
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
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void generateReport() {
        try {
            if (loadedReports == null || loadedReports.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No users available in yearly report.");
                return;
            }

            String[] options = loadedReports.stream()
                    .map(r -> r.getFullName() + " (User ID: " + r.getUserId() + ")")
                    .toArray(String[]::new);

            String selected = (String) JOptionPane.showInputDialog(
                    this,
                    "Select a user:",
                    "Generate Report",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (selected == null) {
                return;
            }

            UserReportDto selectedUser = null;
            for (UserReportDto r : loadedReports) {
                String label = r.getFullName() + " (User ID: " + r.getUserId() + ")";
                if (label.equals(selected)) {
                    selectedUser = r;
                    break;
                }
            }

            if (selectedUser == null) {
                JOptionPane.showMessageDialog(this, "Selected user not found.");
                return;
            }

            List<User> users = controller.getUserDetails();
            User matchedUser = null;

            for (User user : users) {
                if (user.getId().equals(selectedUser.getUserId())) {
                    matchedUser = user;
                    break;
                }
            }

            if (matchedUser == null) {
                JOptionPane.showMessageDialog(this, "Unable to find full user details.");
                return;
            }

            String icNumber = matchedUser.getIcNumber();

            UserDetailedReportDto report = controller.generateUserReport(icNumber);

            if (report == null) {
                JOptionPane.showMessageDialog(this, "No report found for selected user.");
                return;
            }

            showDetailedReportDialog(report);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void showDetailedReportDialog(UserDetailedReportDto report) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Generated Report", true);
        dialog.setSize(950, 620);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JTextArea summaryArea = new JTextArea();
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        summaryArea.setRows(8);
        summaryArea.setText(
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
        historyTable.setRowHeight(36);
        historyTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        historyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));

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

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JScrollPane(summaryArea), BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeButton.addActionListener(e -> dialog.dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(closeButton);

        dialog.add(topPanel, BorderLayout.NORTH);
        dialog.add(new JScrollPane(historyTable), BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}