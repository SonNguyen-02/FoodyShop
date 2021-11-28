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
        String sql = db.select().from("fs_order").orderByDESC("id").getCompiledSelect(true);
        
        ResultSet rs = DBConnection.execSelect(sql);
        System.out.println(rs);
        try {
            while (rs.next()) {
                OrderModel order = new OrderModel();
                order.setId(rs.getInt("id"));
                System.out.println(rs.getInt("id"));
                order.setOrder_code(rs.getString("order_code"));
                order.setName(rs.getString("name"));
                order.setAddress(rs.getString("address"));
                order.setPhone(rs.getString("phone"));
                order.setNote(rs.getString("note"));
                order.setShip_price(rs.getInt("ship_price"));
                order.setTotal_money(rs.getInt("total_money"));
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
}
