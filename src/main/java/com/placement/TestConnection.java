package com.placement;

import com.placement.config.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestConnection {
    
public static void main(String[] args) {

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DATABASE(), VERSION()")) {

            if (rs.next()) {
                System.out.println("Database Connected Successfully!");
                System.out.println("Database: " + rs.getString(1));
                System.out.println("MySQL Version: " + rs.getString(2));
            }

        } catch (Exception e) {
            System.out.println("Database Connection Failed!");
            e.printStackTrace();
        }
    }
}
