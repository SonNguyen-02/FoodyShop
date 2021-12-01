/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.helper;

import com.foodyshop.database.DBConnection;
import com.foodyshop.database.DBQuery;
import com.foodyshop.database.DBQueryBuilder;
import com.foodyshop.model.CategoryModel;
import com.foodyshop.model.TopicModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author X PC
 */
public class CategoryHelper {

    private static DBQuery db = DBQueryBuilder.newDBQuery();

    public static ObservableList<CategoryModel> getAllCategory() throws SQLException {
        ObservableList<CategoryModel> listCategory = FXCollections.observableArrayList();
        String sql = db.select().from("fs_category").orderByDESC("id").getCompiledSelect(true);
        ResultSet rs = DBConnection.execSelect(sql);
        try {
            while (rs.next()) {
                CategoryModel category = new CategoryModel();
                category.setId(rs.getInt("id"));
                category.setTopic_id(rs.getInt("topic_id"));
                category.setName(rs.getString("name"));
                category.setCreated(rs.getString("created"));
                category.setStatus(rs.getInt("status"));
                listCategory.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close();
        }
        return listCategory;
    }
    
     public static boolean isTopicHasLinkToCategorys(TopicModel topic) {

        try {
            String sql = "SELECT * FROM `fs_category` WHERE topic_id = ? LIMIT 1";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sql);

            stm.setInt(1, topic.getId());
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TopicModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;

    }
     
    public static boolean delete(CategoryModel category) {
        try {
            String sql = "delete from fs_category where id = ?";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sql);
            stm.setInt(1, category.getId());
            if(stm.executeUpdate() > 0){
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

//    public static boolean insertCategory(String name,int topic_id) throws SQLException {
//        String query = "INSERT INTO `fs_category`( `name`, `topic_id`) VALUES (?,?)";
//        try ( PreparedStatement preStm = DBConnection.getConnection().prepareStatement(query);) {
//            preStm.setString(1, name);
//            preStm.setInt(2, topic_id);
//            if (preStm.executeUpdate() > 0) {
//                ResultSet rs = preStm.getGeneratedKeys();
//                if (rs.next()) {
//                    int id = rs.getInt(1);
//                }
//
//                return true;
//            }
//        }
//
//        return false;
//    }
//    
}
