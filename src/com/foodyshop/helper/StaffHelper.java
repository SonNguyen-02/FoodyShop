/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.helper;

import com.foodyshop.database.DBConnection;
import com.foodyshop.database.DBQuery;
import com.foodyshop.database.DBQueryBuilder;
import com.foodyshop.model.StaffModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author N.C.Son
 */
public class StaffHelper {

    private static DBQuery db = DBQueryBuilder.newDBQuery();

    public static StaffModel getStaffByEmail(String username) throws SQLException {
        StaffModel staff = null;

        try {
            String sql = db.select().from("fs_staff").where("username", username).getCompiledSelect(true);
            System.out.println(sql);
            ResultSet rs = DBConnection.execSelect(sql);
            if (rs.next()) {
                int idDb = rs.getInt("id");
                String passwordDb = rs.getString("password");
                String nameDb = rs.getString("name");
                String typeDb = rs.getString("type");
                String statusDb = rs.getString("status");

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

    public static StaffModel insertStaff(String username, String password, String name, String type) {
        String sql = db.insert("fs_staff")
                .set("username", username)
                .set("password", password)
                .set("name", name)
                .set("type", type)
                .getCompiledInsert(true);

        int lastId = DBConnection.execInsert(sql);
        if (lastId > 0) {
            try {
                sql = db.select().from("fs_staff").where("id", String.valueOf(lastId)).getCompiledSelect(true);
                ResultSet rs = DBConnection.execSelect(sql);
                if (rs.next()) {
                    StaffModel staffModel = new StaffModel();
                    staffModel.setId(lastId);
                    staffModel.setUsername(username);
                    staffModel.setPassword(password);
                    staffModel.setName(name);
                    staffModel.setType(type);
                    return staffModel;
                }
            } catch (SQLException ex) {
                // Logger.getLogger(StaffHelper.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            } finally {
                DBConnection.close();
            }
        }
        return null;

    }

    public static boolean updateStatusStaff(StaffModel staff) {
        String sql = db.update("fs_staff")
                .set("status", staff.getStatus())
                .where("id", String.valueOf(staff.getId()))
                .getCompiledUpdate(true);
        int n = DBConnection.execUpdate(sql);
        DBConnection.close();
        return n > 0;
    }

    public static boolean editStaff(StaffModel staffModel) {
        String sql = db.update("fs_staff")
                .set("username", staffModel.getUsername())
                .set("password", staffModel.getPassword())
                .set("name", staffModel.getName())
                .set("type", staffModel.getType())
                .where("id", String.valueOf(staffModel.getId()))
                .getCompiledUpdate(true);
        int result = DBConnection.execUpdate(sql);
        if (result > 0) {
            return true;
        }
        return false;
    }

    public static boolean delete(StaffModel staffModel) {
        try {
            String sql = "delete from fs_staff where id = ?";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sql);
            stm.setInt(1, staffModel.getId());
            if (stm.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(StaffHelper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBConnection.close();
        }
        return false;
    }
}
