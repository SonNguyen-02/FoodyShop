/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.helper;

import com.foodyshop.model.FeedbackModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DELL
 */
public class DBFeedbackHelper {
    public static List<FeedbackModel> showAllFeedback() throws SQLException{
        List<FeedbackModel> listFb = new ArrayList<>();
        String query = "SELECT * FROM `fs_feedback`";
        try(
                Connection cnn = DBHelper.getConnection();
                PreparedStatement stm = cnn.prepareStatement(query);
                ){
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                int idDB = rs.getInt("id");
                String customerIDDB = rs.getString("customer_id");
                String productIDDB = rs.getString("product_id");
                String contentDB = rs.getString("content");
                String statusDB = rs.getString("status");
                
                FeedbackModel fb = new FeedbackModel(idDB, customerIDDB, productIDDB, contentDB, statusDB);
                listFb.add(fb);
            }
        }
        return listFb;
    }
}
