package com.techspire.project.client.view;

import com.techspire.project.client.controller.ClientController;
import com.techspire.project.dto.UserReportDto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReportPanel extends JPanel {

    private final ClientController controller;
    private final DefaultTableModel tableModel;
    private final JTable table;

    public ReportPanel(ClientController controller) {
        this.controller = controller;
        this.tableModel = new DefaultTableModel(
                new String[]{"User ID", "Full Name", "User Type", "Remaining Leave", "Total Leave"}, 0
        );
        this.table = new JTable(tableModel);

        initUi();
    }

    private void initUi() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        table.setRowHeight(48);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 17));
        table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 36));

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void loadReports() {
        try {
            tableModel.setRowCount(0);
            List<UserReportDto> reports = controller.getYearlyReport();

            for (UserReportDto r : reports) {
                tableModel.addRow(new Object[]{
                        r.getUserId(),
                        r.getFullName(),
                        r.getUserType(),
                        r.getRemainingLeave(),
                        r.getTotalLeave()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}