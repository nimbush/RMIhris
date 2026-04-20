package com.techspire.project.client;

import com.techspire.project.client.controller.ClientController;
import com.techspire.project.client.view.CreateUserPanel;
import com.techspire.project.client.view.LeaveStatusPanel;
import com.techspire.project.client.view.ReportPanel;
import com.techspire.project.client.view.UserDetailsPanel;
import com.techspire.project.client.view.ui.DarkTheme;
import com.techspire.project.client.view.ui.ModernButton;
import com.techspire.project.dto.LoginResponseDto;
import com.techspire.project.remote.Jdbc;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HrDashboardFrame extends JFrame {

    private final ClientController controller;
    private final LoginResponseDto loginResponse;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    private ModernButton createUserBtn;
    private ModernButton userDetailsBtn;
    private ModernButton leaveStatusBtn;
    private ModernButton reportBtn;
    private ModernButton logoutBtn;

    public HrDashboardFrame(Jdbc jdbc, LoginResponseDto loginResponse) {
        this.controller = new ClientController(jdbc);
        this.loginResponse = loginResponse;
        this.cardLayout = new CardLayout();
        this.cardPanel = new JPanel(cardLayout);

        setTitle("HR Dashboard - " + loginResponse.getUsername());
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

        createUserBtn = new ModernButton("Create User");
        userDetailsBtn = new ModernButton("User Details");
        leaveStatusBtn = new ModernButton("Leave Status");
        reportBtn = new ModernButton("Yearly Report");
        logoutBtn = new ModernButton("Logout");
        logoutBtn.setDangerStyle();

        Dimension navSize = new Dimension(150, 42);

        createUserBtn.setPreferredSize(navSize);
        userDetailsBtn.setPreferredSize(navSize);
        leaveStatusBtn.setPreferredSize(navSize);
        reportBtn.setPreferredSize(navSize);
        logoutBtn.setPreferredSize(new Dimension(140, 42));

        createUserBtn.setMaximumSize(navSize);
        userDetailsBtn.setMaximumSize(navSize);
        leaveStatusBtn.setMaximumSize(navSize);
        reportBtn.setMaximumSize(navSize);
        logoutBtn.setMaximumSize(new Dimension(140, 42));

        topBar.add(createUserBtn);
        topBar.add(Box.createHorizontalStrut(14));
        topBar.add(userDetailsBtn);
        topBar.add(Box.createHorizontalStrut(14));
        topBar.add(leaveStatusBtn);
        topBar.add(Box.createHorizontalStrut(14));
        topBar.add(reportBtn);
        topBar.add(Box.createHorizontalGlue());
        topBar.add(logoutBtn);

        CreateUserPanel createUserPanel = new CreateUserPanel(controller);
        UserDetailsPanel userDetailsPanel = new UserDetailsPanel(controller);
        LeaveStatusPanel leaveStatusPanel = new LeaveStatusPanel(controller);
        ReportPanel reportPanel = new ReportPanel(controller);

        cardPanel.add(createUserPanel, "CREATE_USER");
        cardPanel.add(userDetailsPanel, "USER_DETAILS");
        cardPanel.add(leaveStatusPanel, "LEAVE_STATUS");
        cardPanel.add(reportPanel, "REPORT");
        cardPanel.setBackground(DarkTheme.PANEL);
        cardPanel.setBorder(BorderFactory.createLineBorder(DarkTheme.BORDER));

        createUserBtn.addActionListener(e -> {
            setActiveButton(createUserBtn, createUserBtn, userDetailsBtn, leaveStatusBtn, reportBtn);
            cardLayout.show(cardPanel, "CREATE_USER");
        });

        userDetailsBtn.addActionListener(e -> {
            setActiveButton(userDetailsBtn, createUserBtn, userDetailsBtn, leaveStatusBtn, reportBtn);
            userDetailsPanel.loadUsers();
            cardLayout.show(cardPanel, "USER_DETAILS");
        });

        leaveStatusBtn.addActionListener(e -> {
            setActiveButton(leaveStatusBtn, createUserBtn, userDetailsBtn, leaveStatusBtn, reportBtn);
            leaveStatusPanel.loadLeaveRecords();
            cardLayout.show(cardPanel, "LEAVE_STATUS");
        });

        reportBtn.addActionListener(e -> {
            setActiveButton(reportBtn, createUserBtn, userDetailsBtn, leaveStatusBtn, reportBtn);
            reportPanel.loadReports();
            cardLayout.show(cardPanel, "REPORT");
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

        setActiveButton(createUserBtn, createUserBtn, userDetailsBtn, leaveStatusBtn, reportBtn);
        cardLayout.show(cardPanel, "CREATE_USER");
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