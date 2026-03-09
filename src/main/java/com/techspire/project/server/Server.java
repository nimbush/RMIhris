package com.techspire.project.server;

import com.techspire.project.config.DatabaseConfig;
import com.techspire.project.remote.Jdbc;
import com.techspire.project.remote.JdbcRemoteImpl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    public static void main(String[] args) {
        try {

            // initialize DB and tables
            DatabaseConfig.initializeDatabase();

            // start RMI registry
            Registry registry = LocateRegistry.createRegistry(1099);

            // create remote object
            Jdbc jdbc = new JdbcRemoteImpl();

            // bind service
            registry.rebind("nimesh", jdbc);

            System.out.println("Server is ready");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}