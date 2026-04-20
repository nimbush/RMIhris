package com.techspire.project.server;

import com.techspire.project.config.DatabaseConfig;
import com.techspire.project.remote.Jdbc;
import com.techspire.project.remote.JdbcRemoteImpl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    public static void main(String[] args) {
        try {
            System.out.println("Starting server...");

            DatabaseConfig.initializeDatabase();
            System.out.println("Database initialized");

            Registry registry = LocateRegistry.createRegistry(1099);
            System.out.println("Registry created");

            Jdbc jdbc = new JdbcRemoteImpl();
            System.out.println("Remote object created");

            registry.rebind("nimesh", jdbc);
            System.out.println("Service bound");

            System.out.println("Server is ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}