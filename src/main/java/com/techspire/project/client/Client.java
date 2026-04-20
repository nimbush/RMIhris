package com.techspire.project.client;

import com.techspire.project.client.view.ui.DarkTheme;
import com.techspire.project.remote.Jdbc;

import javax.swing.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String[] args) {
        try {
            DarkTheme.apply();
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            Jdbc jdbc = (Jdbc) registry.lookup("nimesh");

            SwingUtilities.invokeLater(() -> {
                LoginFrame frame = new LoginFrame(jdbc);
                frame.setVisible(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Failed to connect to server: " + e.getMessage(),
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}