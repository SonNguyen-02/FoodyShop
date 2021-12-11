/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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
    private StringProperty start_date;
    private StringProperty end_date;
    private StringProperty created;
    private int status;
    private StringProperty statusVal;

    public SaleModel() {
        this.id = new SimpleObjectProperty<>();
        this.productName = new SimpleStringProperty();
        this.discount = new SimpleObjectProperty<>();
        this.content = new SimpleStringProperty();
        this.start_date = new SimpleStringProperty();
        this.end_date = new SimpleStringProperty();
        this.created = new SimpleStringProperty();
        statusVal = new SimpleStringProperty();
    }

     public Integer getId() {
        return id.getValue();
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

    public StringProperty getStatusVal() {
        return statusVal;
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
