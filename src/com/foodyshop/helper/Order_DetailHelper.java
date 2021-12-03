/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.helper;

import com.foodyshop.database.DBConnection;
import com.foodyshop.database.DBQuery;
import com.foodyshop.database.DBQueryBuilder;
import com.foodyshop.model.OrderModel;
import com.foodyshop.model.Order_DetailModel;
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
public class Order_DetailHelper {

    private static DBQuery db = DBQueryBuilder.newDBQuery();

    public static ObservableList<Order_DetailModel> getAllOrder_Detail(OrderModel order) {
       
        ObservableList<Order_DetailModel> listOrder_Detail = FXCollections.observableArrayList();
        
        try {
            String sql = "SELECT odt.id,odt.number,odt.price,odt.discount,odt.order_id,odt.product_id, prd.name "
                    + "FROM fs_order_detail odt JOIN fs_product prd on odt.product_id = prd.id where odt.order_id = ?";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sql);
            stm.setInt(1, order.getId());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Order_DetailModel order_detail = new Order_DetailModel();
                order_detail.setId(rs.getInt("id"));
                order_detail.setProduct_name(rs.getString("name"));
                order_detail.setNumber(rs.getInt("number"));
                order_detail.setPrice(rs.getInt("price"));
                order_detail.setDiscount(rs.getInt("discount"));
                listOrder_Detail.add(order_detail);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Order_DetailHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listOrder_Detail;
    }
}
