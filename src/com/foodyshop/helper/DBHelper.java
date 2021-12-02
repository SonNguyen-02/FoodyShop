/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author DELL
 */
public class DBHelper {

    public static Connection getConnection() throws SQLException {
        String driver = "jdbc:mysql://localhost/foody_shop";
        return DriverManager.getConnection(driver, "root", "");
    }
}
