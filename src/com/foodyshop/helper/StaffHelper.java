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

    public static ObservableList<StaffModel> getAllStaff() {
        ObservableList<StaffModel> listStaff = FXCollections.observableArrayList();
        String sql = db.select().from("fs_staff").orderByDESC("id").getCompiledSelect(true);
        ResultSet rs = DBConnection.execSelect(sql);
        try {
            while (rs.next()) {
                StaffModel staff = new StaffModel();
                staff.setId(rs.getInt("id"));
                staff.setUsername(rs.getString("username"));
                staff.setPassword(rs.getString("password"));
                staff.setName(rs.getString("name"));
                staff.setType(rs.getInt("type"));
                staff.setCreated(rs.getString("created"));
                staff.setStatus(rs.getInt("status"));
                listStaff.add(staff);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close();
        }
        return listStaff;
    }

    public static StaffModel getStaffByEmail(String username) {
        StaffModel staff = null;
        try {
            String sql = db.select().from("fs_staff").where("username", username).getCompiledSelect(true);
            System.out.println(sql);
            ResultSet rs = DBConnection.execSelect(sql);
            if (rs.next()) {
                staff = new StaffModel();
                staff.setId(rs.getInt("id"));
                staff.setUsername(rs.getString("username"));
                staff.setPassword(rs.getString("password"));
                staff.setName(rs.getString("name"));
                staff.setType(rs.getInt("type"));
                staff.setCreated(rs.getString("created"));
                staff.setStatus(rs.getInt("status"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(StaffHelper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBConnection.close();
        }
        return staff;

    }

    public static boolean isAccountExists(String username) {
        return getStaffByEmail(username) != null;
    }

    public static StaffModel insertStaff(StaffModel staff) {
        String sql = db.insert("fs_staff")
                .set("username", staff.getUsername())
                .set("password", staff.getPassword())
                .set("name", staff.getName())
                .set("type", String.valueOf(staff.getType()))
                .getCompiledInsert(true);

        int lastId = DBConnection.execInsert(sql);
        if (lastId > 0) {
            try {
                sql = db.select().from("fs_staff").where("id", String.valueOf(lastId)).getCompiledSelect(true);
                ResultSet rs = DBConnection.execSelect(sql);
                if (rs.next()) {
                    staff.setId(lastId);
                    staff.setStatus(0);
                    staff.setCreated(rs.getString("created"));
                    return staff;
                }
            } catch (SQLException ex) {
                Logger.getLogger(StaffHelper.class.getName()).log(Level.SEVERE, null, ex);
//                ex.printStackTrace();
            } finally {
                DBConnection.close();
            }
        }
        return null;

    }

    public static boolean updateStatusStaff(StaffModel staff) {
        String sql = db.update("fs_staff")
                .set("status", String.valueOf(staff.getStatus()))
                .where("id", String.valueOf(staff.getId()))
                .getCompiledUpdate(true);
        int n = DBConnection.execUpdate(sql);
        DBConnection.close();
        return n > 0;
    }

    public static boolean editStaff(StaffModel staffModel) {
        String sql = db.update("fs_staff")
                .set("name", staffModel.getName())
                .set("type", String.valueOf(staffModel.getType()))
                .where("id", String.valueOf(staffModel.getId()))
                .getCompiledUpdate(true);
        int result = DBConnection.execUpdate(sql);
        if (result > 0) {
            return true;
        }
        return false;
    }

    public static boolean editPasswordStaff(StaffModel staffModel) {
        String sql = db.update("fs_staff")
                .set("password", staffModel.getPassword())
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
