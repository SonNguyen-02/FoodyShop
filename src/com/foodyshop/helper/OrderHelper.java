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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author APlaptop
 */
public class OrderHelper {

    private static DBQuery db = DBQueryBuilder.newDBQuery();

    public static ObservableList<OrderModel> getAllOrder() {
        ObservableList<OrderModel> listOrder = FXCollections.observableArrayList();
        String sql = db.select().from("fs_order").orderByASC("id").getCompiledSelect(true);       
        ResultSet rs = DBConnection.execSelect(sql);
        try {
            while (rs.next()) {
                OrderModel order = new OrderModel();
                order.setId(rs.getInt("id"));
                order.setOrderCode(rs.getString("order_code"));
                order.setName(rs.getString("name"));
                order.setAddress(rs.getString("address"));
                order.setPhone(rs.getString("phone"));
                order.setNote(rs.getString("note"));
                order.setShipPrice(rs.getInt("ship_price"));
                order.setTotalMoney(rs.getInt("total_money"));
                order.setCreated(rs.getString("created"));
                order.setStatus(rs.getInt("status"));
                listOrder.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close();
        }
        return listOrder;
    }
    public static boolean deleteOrder ( int id )
    {
        OrderModel order = new OrderModel();
        String sql = "DELETE FROM `fs_order` WHERE `id` = ?";
        try(
            Connection cnn = DBConnection.getConnection();
            PreparedStatement stm = cnn.prepareStatement(sql);      
        ) 
        {
            stm.setInt(1, order.getId());
            
            int resultUpdate = stm.executeUpdate();
            if(resultUpdate > 0)
            {
                return true;
            }
            
        } catch (Exception e) {
        }
        return false;
    }
    
}
