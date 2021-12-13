/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.helper;

import com.foodyshop.database.DBConnection;
import com.foodyshop.database.DBQuery;
import com.foodyshop.database.DBQueryBuilder;
import com.foodyshop.model.Order_DetailModel;
import com.foodyshop.model.SaleModel;
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
public class SaleHelper {

    private static DBQuery db = DBQueryBuilder.newDBQuery();

    public static ObservableList<SaleModel> getAllSale() {
        ObservableList<SaleModel> listSale = FXCollections.observableArrayList();
        String sql = db.select("sl.product_id,sl.id,sl.discount,sl.content,sl.img,sl.start_date,sl.end_date,sl.created,sl.status,prd.name")
                .from("fs_sale sl").join("fs_product prd", "sl.product_id = prd.id")
                .orderByDESC("id").getCompiledSelect(true);
        ResultSet rs = DBConnection.execSelect(sql);
        try {
            while (rs.next()) {
                SaleModel sale = new SaleModel();
                sale.setId(rs.getInt("id"));
                sale.setProductName(rs.getString("name"));
                sale.setProductId(rs.getInt("product_id"));
                sale.setDiscount(rs.getInt("discount"));
                sale.setContent(rs.getString("content"));
                sale.setImg(rs.getString("img"));
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

    public static boolean isSaleHasLinkToOrderDetail(SaleModel sale) {
        try {
            String sql = "SELECT * FROM `fs_order_detail` WHERE sale_id = ? LIMIT 1";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sql);
            stm.setInt(1, sale.getId());
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Order_DetailModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean deleteSale(SaleModel sale) {
        try {
            String sql = "delete from fs_sale where id = ?";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sql);
            stm.setInt(1, sale.getId());
            if (stm.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryHelper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBConnection.close();
        }
        return false;
    }

    public static SaleModel insertSale(SaleModel sale) {
        String sql = db.insert("fs_sale")
                .set("product_id", String.valueOf(sale.getProductId()))
                .set("discount", String.valueOf(sale.getDiscount()))
                .set("start_date", sale.getStart_date())
                .set("end_date", sale.getEnd_date())
                .set("content", sale.getContent())
                .set("img", sale.getImg())
                .getCompiledInsert(true);

        int lastId = DBConnection.execInsert(sql);
        if (lastId > 0) {
            try {
                sql = db.select().from("fs_sale").where("id", String.valueOf(lastId)).getCompiledSelect(true);
                ResultSet rs = DBConnection.execSelect(sql);
                if (rs.next()) {
                    sale.setId(lastId);
                    sale.setCreated(rs.getString("created"));
                    sale.setStatus(0);
                    return sale;
                }
            } catch (SQLException ex) {
                Logger.getLogger(CategoryHelper.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                DBConnection.close();
            }
        }
        return null;
    }

    public static boolean updateStatusSale(SaleModel saleModel) {
        String sql = db.update("fs_sale")
                .set("status", String.valueOf(saleModel.getStatus()))    
                .where("id", String.valueOf(saleModel.getId()))
                .getCompiledUpdate(true);
        int result = DBConnection.execUpdate(sql);
        DBConnection.close();
        if (result > 0) {
            return true;
        }
        return false;
    }

}
