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
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author APlaptop
 */
public class CustomerHelper {

    private static DBQuery db = DBQueryBuilder.newDBQuery();

    public static ObservableList<CustomerModel> getAllCustomer() {
        ObservableList<CustomerModel> listCustomer = FXCollections.observableArrayList();
        String sql = db.select().from("fs_customer").orderByDESC("id").getCompiledSelect(true);
        ResultSet rs = DBConnection.execSelect(sql);
        try {
            while (rs.next()) {
                CustomerModel customer = new CustomerModel();
                customer.setId(rs.getInt("id"));
                customer.setPhone(rs.getString("phone"));
                customer.setName(rs.getString("name"));
                customer.setGender(rs.getInt("gender"));
                customer.setDatebirth(rs.getString("datebirth"));
                customer.setAddress(rs.getString("address"));
                customer.setCreated(rs.getString("created"));
                customer.setImg(rs.getString("img"));
                customer.setStatus(rs.getInt("status"));
                listCustomer.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close();
        }
        return listCustomer;
    }
    
    public static boolean updateCustomer(CustomerModel customerModel){
        String sql = db.update("fs_customer")  
                .set("status", String.valueOf(customerModel.getStatus()))
                .where("id", String.valueOf(customerModel.getId()))
                .getCompiledUpdate(true);      
        int result = DBConnection.execUpdate(sql);
        if(result > 0){
            return true;
        }
        return false;
    }
}
