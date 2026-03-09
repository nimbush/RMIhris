package com.techspire.project.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DbUtil {

    public static void closeResources(ResultSet rs, PreparedStatement ps, Connection connection) {
        try {
            if (rs != null) rs.close();
        } catch (Exception ignored) {
        }

        try {
            if (ps != null) ps.close();
        } catch (Exception ignored) {
        }

        try {
            if (connection != null) connection.close();
        } catch (Exception ignored) {
        }
    }
}