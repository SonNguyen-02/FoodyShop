/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.helper;

import com.foodyshop.database.DBConnection;
import com.foodyshop.database.DBQuery;
import com.foodyshop.database.DBQueryBuilder;
import com.foodyshop.model.CategoryModel;
import com.foodyshop.model.ProductModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author X PC
 */
public class ProductHelper {
     private static DBQuery db = DBQueryBuilder.newDBQuery();

    public static ObservableList<ProductModel> getAllCategory() {
        ObservableList<ProductModel> listProduct = FXCollections.observableArrayList();
        String sql = db.select("pd.id,pd.name,pd.description,pd.price,pd.created,pd.status,ct.name").from("fs_product pd").join("fs_category ct", "pd.category_id = ct.id").orderByDESC("id").getCompiledSelect(true);
        ResultSet rs = DBConnection.execSelect(sql);
        try {
            while (rs.next()) {
                ProductModel product = new ProductModel();
                product.setId(rs.getInt("id"));
                product.setCategoryName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getInt("price"));
                product.setName(rs.getString("name"));
                product.setCreated(rs.getString("created"));
                product.setStatus(rs.getInt("status"));
                listProduct.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close();
        }
        return listProduct;
    }
    
    
    
    
    
    
    
    public static boolean isCategoryHasLinkToProducts(CategoryModel category) {

        try {
            String sql = "SELECT * FROM `fs_product` WHERE category_id = ? LIMIT 1";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sql);

            stm.setInt(1, category.getId());
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
