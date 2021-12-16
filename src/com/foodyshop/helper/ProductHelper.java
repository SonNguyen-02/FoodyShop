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

    public static ObservableList<ProductModel> getAllProduct() {
        ObservableList<ProductModel> listProduct = FXCollections.observableArrayList();
        String sql = db.select("pd.id,pd.category_id,pd.name,pd.description,pd.price,pd.created,pd.status,pd.img,pd.img_detail,ct.name")
                .from("fs_product pd").join("fs_category ct", "pd.category_id = ct.id")
                .orderByDESC("id")
                .getCompiledSelect(true);
        ResultSet rs = DBConnection.execSelect(sql);
        try {
            while (rs.next()) {
                ProductModel product = new ProductModel();
                product.setId(rs.getInt("id"));
                product.setCategoryId(rs.getInt("category_id"));
                product.setCategoryName(rs.getString("ct.name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getInt("price"));
                product.setName(rs.getString("pd.name"));
                product.setCreated(rs.getString("created"));
                product.setStatus(rs.getInt("status"));
                product.setImg(rs.getString("img"));
                product.setImgDetail(rs.getString("img_detail"));

                listProduct.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close();
        }
        return listProduct;
    }

    public static boolean delete(ProductModel product) {
        try {
            String sql = "delete from fs_product where id = ?";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sql);
            stm.setInt(1, product.getId());
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

    public static ProductModel insertProduct(ProductModel product) {
        String sql = db.insert("fs_product")
                .set("category_id", String.valueOf(product.getCategoryId()))
                .set("name", product.getName())
                .set("price", product.getPrice().toString())
                .set("description", product.getDescription())
                .set("img", product.getImg())
                .set("img_detail", product.getImgDetail())
                .getCompiledInsert(true);

        int lastId = DBConnection.execInsert(sql);
        if (lastId > 0) {
            try {
                sql = db.select().from("fs_product").where("id", String.valueOf(lastId)).getCompiledSelect(true);
                ResultSet rs = DBConnection.execSelect(sql);
                if (rs.next()) {
                    product.setId(lastId);
                    product.setCreated(rs.getString("created"));
                    product.setStatus(0);
                    return product;
                }
            } catch (SQLException ex) {
                Logger.getLogger(CategoryHelper.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                DBConnection.close();
            }
        }
        return null;
    }
    public static boolean updateProduct(ProductModel productModel){
        String sql = db.update("fs_product")  
                .set("category_id", String.valueOf(productModel.getCategoryId()))
                .set("name", productModel.getName())
                .set("price", productModel.getPrice().toString())
                .set("description", productModel.getDescription())
                .set("img", productModel.getImg())
                .set("img_detail", productModel.getImgDetail())
                .set("status", String.valueOf(productModel.getStatus()))
                .where("id", String.valueOf(productModel.getId()))
                .getCompiledUpdate(true);      
        int result = DBConnection.execUpdate(sql);
        if(result > 0){
            return true;
        }
        return false;
    }
}
