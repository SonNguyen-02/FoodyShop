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
    private ObjectProperty<Integer> id;
    private StringProperty productName;
    private ObjectProperty<Integer> discount;
    private StringProperty content;
    private String img;
    private StringProperty start_date;
    private StringProperty end_date;
    private StringProperty created;
    private ObjectProperty<Integer> status;

    public SaleModel() {
        this.id = new SimpleObjectProperty<>();
        this.productName = new SimpleStringProperty();
        this.discount = new SimpleObjectProperty<>();
        this.content = new SimpleStringProperty();
        this.start_date = new SimpleStringProperty();
        this.end_date = new SimpleStringProperty();
        this.created = new SimpleStringProperty();
        this.status = new SimpleObjectProperty<>();
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

    public ObjectProperty<Integer> getStatusProperty() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status.setValue(status);
    }
    
    
}
