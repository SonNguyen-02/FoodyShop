/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.model;

import static com.foodyshop.main.Config.IMG_FOOD_DIR;
import static com.foodyshop.main.Config.IMG_SALE_DIR;
import com.foodyshop.main.Const;
import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author APlaptop
 */
public class SaleModel {

    public static final String ON_SALE = "On sale";
    public static final String DISCOUNT_END = "Discount end";

    private ObjectProperty<Integer> id;
    private StringProperty productName;
    private ObjectProperty<Integer> discount;
    private StringProperty content;
    private String img;
    private ImageView mImageView;
    private ObservableValue<ImageView> imgView;
    private StringProperty start_date;
    private StringProperty end_date;
    private StringProperty created;
    private int status;
    private ObjectProperty<Integer> productId;
    private StringProperty statusVal;

    public SaleModel() {
        this.id = new SimpleObjectProperty<>();
        this.productName = new SimpleStringProperty();
        this.discount = new SimpleObjectProperty<>();
        this.content = new SimpleStringProperty();
        this.start_date = new SimpleStringProperty();
        this.end_date = new SimpleStringProperty();
        this.created = new SimpleStringProperty();
        this.productId = new SimpleObjectProperty<>();
        statusVal = new SimpleStringProperty();
        this.mImageView = new ImageView();
        this.imgView = new SimpleObjectProperty<>(mImageView);
    }

    public Integer getId() {
        return id.getValue();
    }

    public Integer getProductId() {
        return productId.getValue();
    }

    public ObjectProperty<Integer> getProductIdProperty() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId.setValue(productId);
    }

    public Integer getDiscount() {
        return discount.getValue();
    }

    
    public String getContent() {
        return content.getValue();
    }

    public ObjectProperty<Integer> getIdProperty() {
        return id;
    }

    public void setId(Integer id) {
        this.id.setValue(id);
    }

    public StringProperty getProductNameProperty() {
        return productName;
    }

    public String getProductName() {
        return productName.getValue();
    }

    public String getStart_date() {
        return start_date.getValue();
    }
    
    public String getEnd_date() {
        return end_date.getValue();
    }
    
    public void setProductName(String productName) {
        this.productName.setValue(productName);
    }

    public ObjectProperty<Integer> getDiscountProperty() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount.setValue(discount);
    }

    public StringProperty getContentProperty() {
        return content;
    }

    public void setContent(String content) {
        this.content.setValue(content);
    }

    public String getImgProperty() {
        return img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
        String url = IMG_SALE_DIR + img;
        Image image = new Image(url, 100, 100, false, true, true);
        mImageView.setImage(image);
        image.errorProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                System.out.println(image.getException().getMessage());
                mImageView.setImage(Const.NO_IMAGE_OBJ);
            }
        });
      
    }

    public ObservableValue<ImageView> getImgView() {
        return imgView;
    }

    public StringProperty getStart_dateProperty() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date.setValue(start_date);
    }

    public StringProperty getEnd_dateProperty() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date.setValue(end_date);
    }

    public StringProperty getCreatedProperty() {
        return created;
    }

    public void setCreated(String created) {
        this.created.setValue(created);
    }

    public int getStatus() {
        return status;
    }

    public StringProperty getStatusValProperty() {
        return statusVal;
    }
    
    public String getStatusVal() {
        return statusVal.getValue();
    }

    public void setStatus(Integer status) {
        this.status = status;
        if (status == 0) {
            this.statusVal.setValue(ON_SALE);
        } else {
            this.statusVal.setValue(DISCOUNT_END);
        }
    }

    public void setStatusVal(String statusVal) {
        this.statusVal.set(statusVal);
        if (statusVal.equals(ON_SALE)) {
            this.status = 0;
        } else {
            this.status = 1;
        }
    }
}
