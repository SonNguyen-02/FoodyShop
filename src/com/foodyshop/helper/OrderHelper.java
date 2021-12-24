/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.helper;

import com.foodyshop.database.DBConnection;
import com.foodyshop.database.DBQuery;
import com.foodyshop.database.DBQueryBuilder;
import com.foodyshop.model.CustomerModel;
import com.foodyshop.model.OrderModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public static boolean updateShipAndStatusOrder(OrderModel orderModel) {
        String sql = db.update("fs_order")
                .set("ship_price", String.valueOf(orderModel.getShipPrice()))
                .set("status", String.valueOf(orderModel.getStatus()))
                .where("id", String.valueOf(orderModel.getId()))
                .getCompiledUpdate(true);
        int result = DBConnection.execUpdate(sql);
        if (result > 0) {
            return true;
        }
        return false;
    }

    public static boolean updateStatusOrder(OrderModel orderModel) {
        String sql = db.update("fs_order")
                .set("status", String.valueOf(orderModel.getStatus()))
                .where("id", String.valueOf(orderModel.getId()))
                .getCompiledUpdate(true);
        int result = DBConnection.execUpdate(sql);
        if (result > 0) {
            return true;
        }
        return false;
    }

    public static ObservableList<OrderModel> getListOrderByCustomer(CustomerModel customer) {
        ObservableList<OrderModel> listOrder = FXCollections.observableArrayList();
        String sql = db.select().from("fs_order").where("customer_id", String.valueOf(customer.getId())).getCompiledSelect(true);
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

    public static int getTotalDelivery(LocalDate stDate, LocalDate endDate) {
        String sql = db.select("SUM(total_money) AS total")
                .from("fs_order")
                .where("status", "5")
                .where("updated >=", stDate.toString() + " 00:00:00")
                .where("updated <=", endDate.toString() + " 23:59:59")
                .getCompiledSelect(true);
        ResultSet rs = DBConnection.execSelect(sql);
        int totalMoney = 0;
        try {
            if (rs.next()) {
                totalMoney = rs.getInt("total");
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return totalMoney;
    }

    public static int getSuccessfulDelivery(LocalDate stDate, LocalDate endDate) {
        String sql = db.select("COUNT(status) AS successDe")
                .from("fs_order")
                .where("status", "5")
                .where("created >=", stDate.toString() + " 00:00:00")
                .where("created <=", endDate.toString() + " 23:59:59")
                .getCompiledSelect(true);
        ResultSet rs = DBConnection.execSelect(sql);
        int successDelivery = 0;
        try {
            if (rs.next()) {
                successDelivery = rs.getInt("successDe");
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return successDelivery;
    }

    public static int getOrderNumber(LocalDate stDate, LocalDate endDate) {
        String sql = db.select("COUNT(*) AS total")
                .from("fs_order")
                .where("created >=", stDate.toString() + " 00:00:00")
                .where("created <=", endDate.toString() + " 23:59:59")
                .getCompiledSelect(true); 
        ResultSet rs = DBConnection.execSelect(sql);
        
        int orderNumber = 0;
        try {
            if (rs.next()) {
                orderNumber = rs.getInt("total");
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return orderNumber;
    }

    public static int getOrderCancel(LocalDate stDate, LocalDate endDate) {
        String sql = db.select("COUNT(status) AS cancel")
                .from("fs_order")
                .where("status <", "0")
                .where("updated >=", stDate.toString() + " 00:00:00")
                .where("updated <=", endDate.toString() + " 23:59:59")
                .getCompiledSelect(true);
        ResultSet rs = DBConnection.execSelect(sql);
        int orderCancel = 0;
        try {
            if (rs.next()) {
                orderCancel = rs.getInt("cancel");
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return orderCancel;
    }
}
