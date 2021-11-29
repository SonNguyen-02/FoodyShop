/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author APlaptop
 */
public class Order_DetailModel {

    ObjectProperty<Integer> id;
    ObjectProperty<Integer> product_id;
    ObjectProperty<Integer> number;
    ObjectProperty<Integer> price;
    ObjectProperty<Integer> discount;

    public Order_DetailModel() {
        this.id = new SimpleObjectProperty<>();
        this.product_id = new SimpleObjectProperty<>();
        this.number = new SimpleObjectProperty<>();
        this.price = new SimpleObjectProperty<>();
        this.discount = new SimpleObjectProperty<>();
    }

    public ObjectProperty<Integer> getIdProperty() {
        return id;
    }

    public ObjectProperty<Integer> getProduct_idProperty() {
        return product_id;
    }

    public ObjectProperty<Integer> getNumberProperty() {
        return number;
    }

    public ObjectProperty<Integer> getDiscountProperty() {
        return discount;
    }

    public ObjectProperty<Integer> getPriceProperty() {
        return price;
    }

    
    
    public Integer getId() {
        return id.getValue();
    }

    public Integer getProduct_id() {
        return product_id.getValue();
    }

    public Integer getNumber() {
        return number.getValue();
    }

    public Integer getDiscount() {
        return discount.getValue();
    }

    public Integer getPrice() {
        return price.getValue();
    }

 
       
    public void setId(Integer id) {
        this.id.setValue(id);
    }

    public void setProduct_id(Integer product_id) {
        this.product_id.setValue(product_id);
    }

    public void setNumber(Integer number) {
        this.number.setValue(number);
    }

    public void setPrice(Integer price) {
        this.price.setValue(price);
    }

    public void setDiscount(Integer discount) {
        this.discount.setValue(discount);
    }

}
