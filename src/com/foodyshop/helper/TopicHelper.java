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
public class TopicHelper {

    private static DBQuery db = DBQueryBuilder.newDBQuery();

    public static ObservableList<TopicModel> getAllTopic(){
        ObservableList<TopicModel> listTopic = FXCollections.observableArrayList();
        String sql = db.select().from("fs_topic").orderByDESC("id").getCompiledSelect(true);
        ResultSet rs = DBConnection.execSelect(sql);
        try {
            while (rs.next()) {
                TopicModel topic = new TopicModel();
                topic.setId(rs.getInt("id"));
                topic.setName(rs.getString("name"));
                topic.setImg(rs.getString("img"));
                topic.setCreated(rs.getString("created"));
                topic.setStatus(rs.getInt("status"));
                listTopic.add(topic);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close();
        }
        return listTopic;
    }
    public static boolean delete(TopicModel topic) {
        try {
            String sql = "delete from fs_topic where id = ?";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sql);
            stm.setInt(1, topic.getId());
            if(stm.executeUpdate() > 0){
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
 
}
