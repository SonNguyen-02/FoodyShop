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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public static ObservableList<CategoryModel> getAllCategory() {
        ObservableList<CategoryModel> listCategory = FXCollections.observableArrayList();
        String sql = db.select("ct.id,ct.name,ct.created,ct.status,tp.name").from("fs_category ct").join("fs_topic tp", "ct.topic_id = tp.id").orderByDESC("id").getCompiledSelect(true);
        ResultSet rs = DBConnection.execSelect(sql);
        try {
            while (rs.next()) {
                CategoryModel category = new CategoryModel();
                category.setId(rs.getInt("id"));
                category.setTopicName(rs.getString("name"));
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
            if (stm.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryHelper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBConnection.close();
        }
        return false;
    }

    public static CategoryModel insertCategory(String name, int topic_id) {

        String sql = db.insert("fs_category")
                .set("topic_id", String.valueOf(topic_id))
                .set("name", name).getCompiledInsert(true);

        int lastId = DBConnection.execInsert(sql);
        if (lastId > 0) {
            try {
                sql = db.select().from("fs_category").where("id", String.valueOf(lastId)).getCompiledSelect(true);
                ResultSet rs = DBConnection.execSelect(sql);
                if (rs.next()) {
                    CategoryModel category = new CategoryModel();
                    category.setId(lastId);
                    category.setName(name);
                    category.setStatus(0);
                    category.setTopic_id(topic_id);
                    category.setCreated(rs.getString("created"));
                    return category;
                }
            } catch (SQLException ex) {
                Logger.getLogger(CategoryHelper.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                DBConnection.close();
            }
        }
        return null;
    }
    public static boolean updateCategory(CategoryModel categoryModel){
        String sql = db.update("fs_category")  
                .set("name", categoryModel.getName())
                .set("status", String.valueOf(categoryModel.getStatus()))
                .set("topic_id", String.valueOf(categoryModel.getTopic_id()))
                .where("id", String.valueOf(categoryModel.getId()))
                .getCompiledUpdate(true);      
        int result = DBConnection.execUpdate(sql);
        if(result > 0){
            return true;
        }
        return false;
    }

}
