/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.helper;

import com.foodyshop.database.DBConnection;
import com.foodyshop.database.DBQuery;
import com.foodyshop.database.DBQueryBuilder;
import com.foodyshop.model.ProductModel;
import com.foodyshop.model.StaffModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author N.C.Son
 */
public class StaffHelper {

    private static DBQuery db = DBQueryBuilder.newDBQuery();

    public static StaffModel getStaffByEmail(String username) throws SQLException {
        StaffModel staff = null;

        try {
//            String query = "SELECT * FROM fs_staff WHERE username=?";
            String sql = db.select().from("fs_staff").where("username", username).getCompiledSelect(true);
            System.out.println(sql);
            ResultSet rs = DBConnection.execSelect(sql);
            if (rs.next()) {
                int idDb = rs.getInt("id");
                String passwordDb = rs.getString("password");
                String nameDb = rs.getString("name");
                String typeDb = rs.getString("type");
                String statusDb = rs.getString("status");

//                staff = new StaffModel(idDb, usernameDb, passwordDb, nameDb, typeDb, statusDb);
                staff = new StaffModel(idDb, username, passwordDb, nameDb, nameDb, nameDb, typeDb, statusDb);
            }
        } finally {
            DBConnection.close();
        }
        return staff;

    }

    public static List<StaffModel> getAllStaff() throws SQLException {
        List<StaffModel> listStaff = new ArrayList<>();
        try {
            String query = "SELECT * FROM `fs_staff`";
            ResultSet rs = DBConnection.execSelect(query);
            while (rs.next()) {
                int idDB = rs.getInt("id");
                String usernameDB = rs.getString("username");
                String passwordDB = rs.getString("password");
                String nameDB = rs.getString("name");
                String typeDB = rs.getString("type");
                String createdDB = rs.getString("created");
                String updatedDB = rs.getString("updated");
                String statusDB = rs.getString("status");

                StaffModel ls = new StaffModel(idDB, usernameDB, passwordDB, nameDB, typeDB, createdDB, updatedDB, statusDB);
                listStaff.add(ls);
            }
        } finally {
            DBConnection.close();
        }
        return listStaff;
    }

    public static StaffModel insertStaff(StaffModel staffModel) {
        String sql = db.insert("fs_staff")
                .set("username", staffModel.getUsername())
                .set("password", staffModel.getPassword())
                .set("name", staffModel.getName())
                .set("type", staffModel.getType())
                .getCompiledInsert(true);

        int username = DBConnection.execInsert(sql);
        if (username > 0) {
            try {
                sql = db.select().from("fs_staff").where("username", String.valueOf(username)).getCompiledSelect(true);
                ResultSet rs = DBConnection.execSelect(sql);
                if (rs.next()) {
                   // staffModel.setId(lastId);
                    staffModel.setUsername(rs.getString("username"));
                    staffModel.setPassword(rs.getString("password"));
                    staffModel.setName(rs.getString("name"));
                    staffModel.setType(rs.getString("type"));
                    return staffModel;
                }
            } catch (SQLException ex) {
                //Logger.getLogger(StaffHelper.class.getName()).log(Level.SEVERE,null,ex);
               // Logger.getLogger(StaffHelper.class.getName()).log(Level.SEVERE, null, ex);
               ex.printStackTrace();
            } finally {
                DBConnection.close();
            }
        }
        return null;
//        try{
//            String query = "INSERT INTO `fs_staff`( `username`, `password`, `name`, `type`, `status`) VALUES(?,?,?,?,?)";
//        ResultSet rs = DBConnection.execSelect(query);
//        }
//        return false;
    }

    public static ObservableList<ProductModel> getAll() {
        ObservableList<ProductModel> list = FXCollections.observableArrayList();
//        String sql = db.select().from("fs_product").where("product_name > ", "abc").orderByDESC("id").getCompiledSelect(true);
//        sql = "SELECT * FROM fs_product WHERE product_name = 'dsf'sdf' ORDER BY id DESC";
        HashMap<String, String> map = new HashMap();
        map.put("sdt", "23456789");
        map.put("address", "hà nội");
        String sql = db.insert("product")
                .set(map)
                .set("id", "1")
                .set("name", "a")
                .set("age", "18")
                .getCompiledInsert(true);

        System.out.println(sql);

        int lastId = DBConnection.execInsert(sql);
        if (lastId > 0) {
            System.out.println("Update success");
        } else {
            System.out.println("Update false");

        }
        return list;
    }
}
