/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.helper;

import com.foodyshop.database.DBConnection;
import com.foodyshop.database.DBQuery;
import com.foodyshop.database.DBQueryBuilder;
import com.foodyshop.model.ProductModel;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author N.C.Son
 */
public class StaffHelper {
     
    private static DBQuery db = DBQueryBuilder.newDBQuery();
    
    public static ObservableList<ProductModel> getAll(){
        ObservableList<ProductModel> list = FXCollections.observableArrayList();
//        String sql = db.select().from("fs_product").where("product_name > ", "abc").orderByDESC("id").getCompiledSelect(true);
//        sql = "SELECT * FROM fs_product WHERE product_name = 'dsf'sdf' ORDER BY id DESC";
        HashMap<String, String> map = new HashMap();
        map.put("sdt", "23456789");
        map.put("address", "hà nội");
        String sql = db.insert("product")
                .set(map)
                .set("id", "1")
                .set("name", "a")
                .set("age", "18")
                .getCompiledInsert(true);
        
        System.out.println(sql);
            
        int lastId = DBConnection.execInsert(sql);
        if(lastId > 0){
            System.out.println("Update success");
        }else{
            System.out.println("Update false");
            
        }
        return list;
    }
    
}
