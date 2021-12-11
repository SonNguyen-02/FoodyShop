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
import com.foodyshop.model.ProductModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
            String sql = "SELECT odt.id,odt.number,odt.price,odt.discount, prd.name,fb.content,sl.content,sl.start_date,sl.end_date"
                    + "FROM fs_order_detail odt JOIN fs_product prd on odt.product_id = prd.id "
                    + "JOIN fs_sale sl on odt.sale_id = sl.id "
                    + "LEFT JOIN fs_feedback fb on odt.id = fb.order_detail_id where odt.order_id = ?";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sql);
            stm.setInt(1, order.getId());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Order_DetailModel order_detail = new Order_DetailModel();
                order_detail.setId(rs.getInt("id"));
                order_detail.setProduct_name(rs.getString("name"));
                order_detail.setContent(rs.getString("fb.content"));
                order_detail.setContentSale(rs.getString("sl.content"));
                order_detail.setStartDate(rs.getString("start_date"));
                order_detail.setEndDate(rs.getString("end_date"));
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

    public static boolean isProductHasLinkToOrder_detail(ProductModel product) {

        try {
            String sql = "SELECT * FROM `fs_order_detail` WHERE product_id = ? LIMIT 1";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sql);

            stm.setInt(1, product.getId());
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;

    }

}
