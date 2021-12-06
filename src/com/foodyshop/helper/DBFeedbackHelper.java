/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.helper;

import com.foodyshop.database.DBConnection;
import com.foodyshop.model.FeedbackModel;
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
       
        try{
             String query = "SELECT * FROM `fs_feedback`";
            ResultSet rs = DBConnection.execSelect(query);
            while(rs.next()){
                int idDB = rs.getInt("id");
                String customerIDDB = rs.getString("customer_id");
                String productIDDB = rs.getString("product_id");
                int orderDetailIDDB = rs.getInt("order_detail_id");
                String contentDB = rs.getString("content");
                String createdDB = rs.getString("created");
                String updatedDB = rs.getString("updated");
                String statusDB = rs.getString("status");
                
                FeedbackModel fb = new FeedbackModel(idDB, customerIDDB, productIDDB, orderDetailIDDB, contentDB, createdDB, updatedDB, statusDB);
                listFb.add(fb);
            }
        }finally{
            DBConnection.close();
        }
        return listFb;
    }
}
