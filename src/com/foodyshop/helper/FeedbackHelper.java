/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.helper;

import com.foodyshop.database.DBConnection;
import com.foodyshop.database.DBQuery;
import com.foodyshop.database.DBQueryBuilder;
import com.foodyshop.model.FeedbackModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author DELL
 */
public class FeedbackHelper {

    private static DBQuery db = DBQueryBuilder.newDBQuery();

    public static ObservableList<FeedbackModel> showAllFeedback() throws SQLException {
        ObservableList<FeedbackModel> listFb = FXCollections.observableArrayList();
        String sql = db.select("fb.id,fb.customer_id,fb.product_id,fb.order_detail_id,fb.content,fb.created,fb.updated,fb.status,customer.name,pd.name")
                .from("fs_feedback fb").join("fs_customer customer", "fb.customer_id = customer.id")
                .join("fs_product pd", "fb.product_id = pd.id")
                .orderByDESC("id")
                .getCompiledSelect(true);
        ResultSet rs = DBConnection.execSelect(sql);
        try {
            while (rs.next()) {
                FeedbackModel fbmodel = new FeedbackModel();
                fbmodel.setID(rs.getInt("id"));
                fbmodel.setCustomerID(rs.getString("customer_id"));
                fbmodel.setProductID(rs.getString("product_id"));
                fbmodel.setOrderDetailID(rs.getInt("order_detail_id"));
                fbmodel.setContent(rs.getString("content"));
                fbmodel.setCreated(rs.getString("created"));
                fbmodel.setUpdated(rs.getString("updated"));
                fbmodel.setStatus(rs.getString("status"));
                fbmodel.setCustomerName(rs.getString("customer.name"));
                fbmodel.setProductName(rs.getString("pd.name"));

                listFb.add(fbmodel);

            }
        } finally {
            DBConnection.close();
        }
        return listFb;
    }

    public static boolean updateStatusFb(FeedbackModel feedback) {
        String sql = db.update("fs_feedback")
                .set("status", feedback.getStatus())
                .where("id", String.valueOf(feedback.getID()))
                .getCompiledUpdate(true);
        int n = DBConnection.execUpdate(sql);
        DBConnection.close();
        return n > 0;
    }
    
    public static int getFeedback(LocalDate stDate, LocalDate endDate) {
        String sql = db.select("COUNT(*) AS total")
                .from("fs_feedback")
                .where("created >=", stDate.toString() + " 00:00:00")
                .where("created <=", endDate.toString() + " 23:59:59")
                .getCompiledSelect(true);
        ResultSet rs = DBConnection.execSelect(sql);
        int feedback = 0;
        try {
            if (rs.next()) {
                feedback = rs.getInt("total");
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return feedback;
    }
}
