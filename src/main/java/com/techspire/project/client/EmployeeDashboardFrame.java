package com.techspire.project.client;

import com.techspire.project.client.controller.ClientController;
import com.techspire.project.client.view.ApplyLeavePanel;
import com.techspire.project.client.view.LeaveStatusPanel;
import com.techspire.project.client.view.UpdateUserPanel;
import com.techspire.project.client.view.ui.DarkTheme;
import com.techspire.project.client.view.ui.ModernButton;
import com.techspire.project.dto.LoginResponseDto;
import com.techspire.project.remote.Jdbc;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class EmployeeDashboardFrame extends JFrame {

    private final ClientController controller;
    private final LoginResponseDto loginResponse;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    private ModernButton updateDetailsBtn;
    private ModernButton applyLeaveBtn;
    private ModernButton leaveStatusBtn;
    private ModernButton logoutBtn;

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
        JPanel topBar = new JPanel();
        topBar.setLayout(new BoxLayout(topBar, BoxLayout.X_AXIS));
        topBar.setBorder(new EmptyBorder(14, 20, 14, 20));
        topBar.setBackground(DarkTheme.BG);

        updateDetailsBtn = new ModernButton("Update Details");
        applyLeaveBtn = new ModernButton("Apply Leave");
        leaveStatusBtn = new ModernButton("My Leave Status");
        logoutBtn = new ModernButton("Logout");
        logoutBtn.setDangerStyle();

        Dimension navSize = new Dimension(170, 42);

        updateDetailsBtn.setPreferredSize(navSize);
        applyLeaveBtn.setPreferredSize(navSize);
        leaveStatusBtn.setPreferredSize(navSize);
        logoutBtn.setPreferredSize(new Dimension(140, 42));

        updateDetailsBtn.setMaximumSize(navSize);
        applyLeaveBtn.setMaximumSize(navSize);
        leaveStatusBtn.setMaximumSize(navSize);
        logoutBtn.setMaximumSize(new Dimension(140, 42));

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
        cardPanel.setBackground(DarkTheme.PANEL);
        cardPanel.setBorder(BorderFactory.createLineBorder(DarkTheme.BORDER));

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
        contentWrapper.setBackground(DarkTheme.BG);
        contentWrapper.setBorder(new EmptyBorder(16, 20, 20, 20));
        contentWrapper.add(cardPanel, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(DarkTheme.BG);
        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(contentWrapper, BorderLayout.CENTER);

        setContentPane(mainPanel);

        updateUserPanel.loadUserDetails();
        setActiveButton(updateDetailsBtn, updateDetailsBtn, applyLeaveBtn, leaveStatusBtn);
        cardLayout.show(cardPanel, "UPDATE_DETAILS");
    }

    private void setActiveButton(ModernButton active, ModernButton... buttons) {
        for (ModernButton btn : buttons) {
            btn.setSecondaryStyle();
            btn.setForeground(DarkTheme.TEXT);
        }

        active.setBackground(DarkTheme.ACCENT);
        active.setForeground(Color.WHITE);
    }
}