/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.helper;

import com.foodyshop.database.DBConnection;
import com.foodyshop.database.DBQuery;
import com.foodyshop.database.DBQueryBuilder;
import com.foodyshop.model.SaleModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author APlaptop
 */
public class SaleHelper {
     private static DBQuery db = DBQueryBuilder.newDBQuery();

    public static ObservableList<SaleModel> getAllSale() {
        ObservableList<SaleModel> listSale = FXCollections.observableArrayList();
        String sql = db.select("sl.id,prd.name,sl.discount,sl.content,sl.start_date,sl.end_date,sl.created,sl.status")
                .from("fs_sale sl").join("fs_product prd", "sl.product_id = prd.id")
                .orderByASC("id").getCompiledSelect(true);
        ResultSet rs = DBConnection.execSelect(sql);
        try {
            while (rs.next()) {
                SaleModel sale = new SaleModel();
                sale.setId(rs.getInt("id"));
                sale.setProductName(rs.getString("name"));
                sale.setDiscount(rs.getInt("discount"));
                sale.setContent(rs.getString("content"));
                sale.setStart_date(rs.getString("start_date"));
                sale.setEnd_date(rs.getString("end_date"));
                sale.setCreated(rs.getString("created"));
                sale.setStatus(rs.getInt("status"));
                listSale.add(sale);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close();
        }
        return listSale;
    }
}
