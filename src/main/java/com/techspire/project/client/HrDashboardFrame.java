package com.techspire.project.client;

import com.techspire.project.client.controller.ClientController;
import com.techspire.project.client.view.CreateUserPanel;
import com.techspire.project.client.view.LeaveStatusPanel;
import com.techspire.project.client.view.ReportPanel;
import com.techspire.project.client.view.UserDetailsPanel;
import com.techspire.project.dto.LoginResponseDto;
import com.techspire.project.remote.Jdbc;

import javax.swing.*;
import java.awt.*;

public class HrDashboardFrame extends JFrame {

    private final ClientController controller;
    private final LoginResponseDto loginResponse;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    private JButton createUserBtn;
    private JButton userDetailsBtn;
    private JButton leaveStatusBtn;
    private JButton reportBtn;
    private JButton logoutBtn;

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
        Color pageBg = new Color(240, 242, 245);

        JPanel topBar = new JPanel();
        topBar.setLayout(new BoxLayout(topBar, BoxLayout.X_AXIS));
        topBar.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        topBar.setBackground(pageBg);

        createUserBtn = new JButton("Create User");
        userDetailsBtn = new JButton("User Details");
        leaveStatusBtn = new JButton("Leave Status");
        reportBtn = new JButton("Yearly Report");
        logoutBtn = new JButton("Logout");

        Dimension navSize = new Dimension(150, 40);

        createUserBtn.setPreferredSize(navSize);
        userDetailsBtn.setPreferredSize(navSize);
        leaveStatusBtn.setPreferredSize(navSize);
        reportBtn.setPreferredSize(navSize);
        logoutBtn.setPreferredSize(navSize);

        styleNavButton(createUserBtn);
        styleNavButton(userDetailsBtn);
        styleNavButton(leaveStatusBtn);
        styleNavButton(reportBtn);
        styleLogoutButton(logoutBtn);

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

        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

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
        contentWrapper.setBackground(pageBg);
        contentWrapper.setBorder(BorderFactory.createEmptyBorder(16, 20, 20, 20));
        contentWrapper.add(cardPanel, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(pageBg);
        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(contentWrapper, BorderLayout.CENTER);

        add(mainPanel);

        setActiveButton(createUserBtn, createUserBtn, userDetailsBtn, leaveStatusBtn, reportBtn);
        cardLayout.show(cardPanel, "CREATE_USER");
    }

    private void styleNavButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
        button.setMaximumSize(new Dimension(150, 40));
    }

    private void styleLogoutButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(new Color(220, 53, 69));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setOpaque(true);
        button.setMaximumSize(new Dimension(150, 40));
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