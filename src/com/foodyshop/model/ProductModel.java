/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.model;

import com.foodyshop.database.DBConnection;
import static com.foodyshop.main.Config.IMG_FOOD_DIR;
import static com.foodyshop.main.Config.IMG_SALE_DIR;

import com.foodyshop.main.Const;
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
import javafx.scene.image.ImageView;

/**
 *
 * @author N.C.Son
 */
public class ProductModel {
    public static final String STILL = "Still";
    public static final String SOLD_OUT = "Sold_out";
    private ObjectProperty<Integer> id;
    private StringProperty name;
    private Integer categoryId;
    private StringProperty description;
    private ObjectProperty<Integer> price;
    private String imgDetail;
    private String img;
    private ObjectProperty<Integer> status;
    private StringProperty created;
    private StringProperty statusVal;
    private StringProperty categoryName;
    private ImageView mImageView;
    private ObservableValue<ImageView> imgView;



    public ProductModel() {
        this.id = new SimpleObjectProperty<>();
        this.name = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.price = new SimpleObjectProperty<>();
        this.created = new SimpleStringProperty();
        this.status = new SimpleObjectProperty<>();
        this.statusVal = new SimpleStringProperty();
        this.categoryName = new SimpleStringProperty();
        this.mImageView = new ImageView();
        this.imgView = new SimpleObjectProperty<>(mImageView);
    }

    public Integer getId() {
        return id.getValue();
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
 
//    public int getIdProperty() {
//        return id;
//    }
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
 
  
    public void setId(Integer id) {
        this.id.setValue(id);
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
    public void setImg(String img) {
        this.img = img;
        String url = IMG_FOOD_DIR + img;
        Image image = new Image(url, 100, 100, false, true, true);
        mImageView.setImage(image);
        image.errorProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
//                System.out.println(image.getException().getMessage());
                mImageView.setImage(Const.NO_IMAGE_OBJ);
            }
        });
      
    }
    
    public ObservableValue<ImageView> getImgView() {
        return imgView;
    }
    public void setImgDetail(String imgDetail){
        this.imgDetail = imgDetail;
        String url = IMG_FOOD_DIR + img;
        Image image = null;

//        image = new Image(url, 100, 100, false, true);
//        tutu
//        System.out.println(image.getException().toString());
//        if (!image.getException().getMessage().isEmpty()) {
//            image = new Image("file:" + Const.PLACEHOLDER_NO_IMG_PATH, 100, 100, false, true, true);
//        }
        
//        this.imgView = new SimpleObjectProperty<>(new ImageView(image));      
    }
    public ObservableValue<ImageView> getImgViewDetail() {
        return imgView;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

   
       
    @Override
    public String toString() {
        return this.name.get();
    }
}

