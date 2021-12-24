/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.database;

import com.foodyshop.main.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author WIN1064
 */
public class DBConnection {

    private static final String HOSTNAME = Config.HOSTNAME;
    private static final String USERNAME = Config.USERNAME;
    private static final String PASSWORD = Config.PASSWORD;
    private static final String DATABASE = Config.DATABASE;

    private static Connection connectionInstance = null;

    private DBConnection() {
    }

    private static void initialize() {
        if (connectionInstance != null) {
            return;
        }
        try {
            String url = "jdbc:mysql://" + HOSTNAME + "/" + DATABASE + "?useUnicode=yes&characterEncoding=utf8&autoReconnect=true";
            connectionInstance = DriverManager.getConnection(url, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    // chạy câu select
    public static ResultSet execSelect(String sql) {
        initialize();
        ResultSet rs = null;
        try {
            Statement stmt = connectionInstance.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }

    // chạy câu update, delete | insert
    public static int execUpdate(String sql) {
        initialize();
        try {
            Statement stmt = connectionInstance.createStatement();
            return stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    // chạy câu insert và return last id inserted
    public static int execInsert(String sql) {
        initialize();
        try {
            Statement stmt = connectionInstance.createStatement();
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static Connection getConnection() {
        initialize();
        return connectionInstance;
    }

    public static void close() {
        if (connectionInstance != null) {
            try {
                connectionInstance.close();
                connectionInstance = null;
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
