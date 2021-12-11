    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.helper;

import com.foodyshop.database.DBConnection;
import com.foodyshop.database.DBQuery;
import com.foodyshop.database.DBQueryBuilder;
import com.foodyshop.model.CustomerDetailModel;
import com.foodyshop.model.CustomerModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author APlaptop
 */
public class CustomerDetailHelper {

    private static DBQuery db = DBQueryBuilder.newDBQuery();

    public static ObservableList<CustomerDetailModel> getAllCustomerDetail(CustomerModel customer) {

        ObservableList<CustomerDetailModel> listCustomerDetail = FXCollections.observableArrayList();
        
        try {
             String sql = "SELECT ct.id,od.order_code,od.total_money,od.created,od.status,od.note,fb.content "
                    + "FROM fs_customer ct LEFT JOIN fs_order od on ct.id = od.customer_id "
                     + "LEFT JOIN fs_feedback fb on ct.id = od.customer_id where ct.id = ?";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sql);
            stm.setInt(1, customer.getId());
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                CustomerDetailModel customerDetail = new CustomerDetailModel();
                customerDetail.setId(rs.getInt("id"));
                customerDetail.setOrderCode(rs.getString("order_code"));
                customerDetail.setNote(rs.getString("note"));
                customerDetail.setContent(rs.getString("content"));
                customerDetail.setTotalMoney(rs.getInt("total_money"));
                customerDetail.setCreated(rs.getString("created"));
                customerDetail.setStatus(rs.getInt("status"));
                listCustomerDetail.add(customerDetail);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Order_DetailHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listCustomerDetail;
    }
}
