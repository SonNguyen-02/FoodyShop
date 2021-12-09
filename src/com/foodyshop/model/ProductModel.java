/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.model;

import com.foodyshop.database.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;

/**
 *
 * @author N.C.Son
 */
public class ProductModel {
    public static final String STILL = "Still";
    public static final String SOLD_OUT = "Sold_out";
    private int id;
    private StringProperty name;
    private StringProperty description;
    private ObjectProperty<Integer> price;
    private String imgDetail;
    private String img;
    private ObjectProperty<Integer> status;
    private StringProperty created;
    private StringProperty statusVal;
    private StringProperty categoryName;



    public ProductModel() {
        this.name = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.price = new SimpleObjectProperty<>();
        this.created = new SimpleStringProperty();
        this.status = new SimpleObjectProperty<>();
        statusVal = new SimpleStringProperty();
        this.categoryName = new SimpleStringProperty();
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name.getValue();
    }
    public String getDescription() {
        return description.getValue();
    }
    public Integer getPrice() {
        return price.getValue();
    }
    public String getImgDetail() {
        return imgDetail;
    }
    public String getImg() {
        return img;
    }
    public Integer getStatus() {
        return status.getValue();
    }
    public String getCreated() {
        return created.getValue();
    }

    public String getCategoryName() {
        return categoryName.getValue();
    }
   
    
    public int getIdProperty() {
        return id;
    }
    public StringProperty getNameProperty() {
        return name;
    }
    public StringProperty getDescriptionProperty() {
        return description;
    }
    public ObjectProperty<Integer> getPriceProperty() {
        return price;
    }
    public String getImgDetailProperty() {
        return imgDetail;
    }
    public String getImgProperty() {
        return img;
    }
    public ObjectProperty<Integer> getStatusProperty() {
        return status;
    }
    public StringProperty getCreatedProperty() {
        return created;
    }
    public StringProperty getCategoryNameProperty() {
        return categoryName;
    }
    public StringProperty getStatusVal() {
        return statusVal;
    }
   
    


    
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name.setValue(name);
    }
    public void setDescription(String description) {
        this.description.setValue(description);
    }
    public void setPrice(Integer price) {
        this.price.setValue(price);
    }
    public void setImgDetail(String imgDetail) {
        this.imgDetail = imgDetail;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public void setCreated(String created) {
        this.created.setValue(created);
    }
    public void setStatusVal(StringProperty statusVal) {
        this.statusVal = statusVal;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName.setValue(categoryName);
    }
    
     public void setStatusVal(String statusVal){
        this.statusVal.set(statusVal);
        if(statusVal.equals(STILL)){
            this.status.set(0);
        }else{
            this.status.set(1);
        }
    }

     public void setStatus(Integer status) {
        this.status.setValue(status);
        if (status == 0) {
            this.statusVal.setValue(STILL);
        } else {
            this.statusVal.setValue(SOLD_OUT);
        }
    }
    
    
}

