package com.techspire.project.client;

import com.techspire.project.client.controller.ClientController;
import com.techspire.project.client.view.ApplyLeavePanel;
import com.techspire.project.client.view.LeaveStatusPanel;
import com.techspire.project.client.view.UpdateUserPanel;
import com.techspire.project.dto.LoginResponseDto;
import com.techspire.project.remote.Jdbc;

import javax.swing.*;
import java.awt.*;

public class EmployeeDashboardFrame extends JFrame {

    private final ClientController controller;
    private final LoginResponseDto loginResponse;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    private JButton updateDetailsBtn;
    private JButton applyLeaveBtn;
    private JButton leaveStatusBtn;
    private JButton logoutBtn;

    public EmployeeDashboardFrame(Jdbc jdbc, LoginResponseDto loginResponse) {
        this.controller = new ClientController(jdbc);
        this.loginResponse = loginResponse;
        this.cardLayout = new CardLayout();
        this.cardPanel = new JPanel(cardLayout);

        setTitle("Employee Dashboard - " + loginResponse.getUsername());
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUi(jdbc);
    }

    private void initUi(Jdbc jdbc) {
        Color pageBg = new Color(240, 242, 245);

        JPanel topBar = new JPanel();
        topBar.setLayout(new BoxLayout(topBar, BoxLayout.X_AXIS));
        topBar.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        topBar.setBackground(pageBg);

        updateDetailsBtn = new JButton("Update Details");
        applyLeaveBtn = new JButton("Apply Leave");
        leaveStatusBtn = new JButton("My Leave Status");
        logoutBtn = new JButton("Logout");

        Dimension navSize = new Dimension(160, 40);

        updateDetailsBtn.setPreferredSize(navSize);
        applyLeaveBtn.setPreferredSize(navSize);
        leaveStatusBtn.setPreferredSize(navSize);
        logoutBtn.setPreferredSize(navSize);

        styleNavButton(updateDetailsBtn);
        styleNavButton(applyLeaveBtn);
        styleNavButton(leaveStatusBtn);
        styleLogoutButton(logoutBtn);

        topBar.add(updateDetailsBtn);
        topBar.add(Box.createHorizontalStrut(14));
        topBar.add(applyLeaveBtn);
        topBar.add(Box.createHorizontalStrut(14));
        topBar.add(leaveStatusBtn);
        topBar.add(Box.createHorizontalGlue());
        topBar.add(logoutBtn);

        UpdateUserPanel updateUserPanel = new UpdateUserPanel(controller, loginResponse.getIcNumber());
        ApplyLeavePanel applyLeavePanel = new ApplyLeavePanel(controller, loginResponse.getIcNumber());
        LeaveStatusPanel leaveStatusPanel = new LeaveStatusPanel(controller);

        cardPanel.add(updateUserPanel, "UPDATE_DETAILS");
        cardPanel.add(applyLeavePanel, "APPLY_LEAVE");
        cardPanel.add(leaveStatusPanel, "LEAVE_STATUS");

        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        updateDetailsBtn.addActionListener(e -> {
            setActiveButton(updateDetailsBtn, updateDetailsBtn, applyLeaveBtn, leaveStatusBtn);
            updateUserPanel.loadUserDetails();
            cardLayout.show(cardPanel, "UPDATE_DETAILS");
        });

        applyLeaveBtn.addActionListener(e -> {
            setActiveButton(applyLeaveBtn, updateDetailsBtn, applyLeaveBtn, leaveStatusBtn);
            cardLayout.show(cardPanel, "APPLY_LEAVE");
        });

        leaveStatusBtn.addActionListener(e -> {
            setActiveButton(leaveStatusBtn, updateDetailsBtn, applyLeaveBtn, leaveStatusBtn);
            leaveStatusPanel.loadLeaveRecordsByIcNumber(loginResponse.getIcNumber());
            cardLayout.show(cardPanel, "LEAVE_STATUS");
        });

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame(jdbc).setVisible(true);
        });

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(pageBg);
        contentWrapper.setBorder(BorderFactory.createEmptyBorder(16, 20, 20, 20));
        contentWrapper.add(cardPanel, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(pageBg);
        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(contentWrapper, BorderLayout.CENTER);

        add(mainPanel);

        updateUserPanel.loadUserDetails();
        setActiveButton(updateDetailsBtn, updateDetailsBtn, applyLeaveBtn, leaveStatusBtn);
        cardLayout.show(cardPanel, "UPDATE_DETAILS");
    }

    private void styleNavButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
        button.setMaximumSize(new Dimension(160, 40));
    }

    private void styleLogoutButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(new Color(220, 53, 69));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setOpaque(true);
        button.setMaximumSize(new Dimension(160, 40));
    }

    private void setActiveButton(JButton active, JButton... buttons) {
        for (JButton btn : buttons) {
            btn.setBackground(Color.WHITE);
            btn.setForeground(Color.BLACK);
            btn.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        }

        active.setBackground(new Color(235, 240, 250));
        active.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 170, 210), 2),
                BorderFactory.createEmptyBorder(1, 1, 1, 1)
        ));
        active.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }
}