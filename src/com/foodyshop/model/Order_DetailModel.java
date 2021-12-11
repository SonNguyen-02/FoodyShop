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
public class Order_DetailModel {

    private ObjectProperty<Integer> id;
    private StringProperty order_code;
    private StringProperty product_name;
    private StringProperty content;
    private ObjectProperty<Integer> number;
    private ObjectProperty<Integer> price;
    private ObjectProperty<Integer> discount;
    private ObjectProperty<Integer> saleId;
    private StringProperty contentSale;
    private StringProperty startDate;
    private StringProperty endDate;

    public Order_DetailModel() {
        this.id = new SimpleObjectProperty<>();
        this.order_code = new SimpleStringProperty();
        this.product_name = new SimpleStringProperty();
        this.number = new SimpleObjectProperty<>();
        this.price = new SimpleObjectProperty<>();
        this.discount = new SimpleObjectProperty<>();
        this.content = new SimpleStringProperty();
        this.contentSale = new SimpleStringProperty();
        this.startDate = new SimpleStringProperty();
        this.endDate = new SimpleStringProperty();
        this.saleId = new SimpleObjectProperty<>();
    }

    public Integer getId() {
        return id.getValue();
    }

    public Integer getSaleId() {
        return saleId.getValue();
    }
    
    public String getOrder_code() {
        return order_code.getValue();
    }

    public String getProduct_name() {
        return product_name.getValue();
    }

    public Integer getNumber() {
        return number.getValue();
    }

    public Integer getPrice() {
        return price.getValue();
    }

    public Integer getDiscount() {
        return discount.getValue();
    }

    public String getContent() {
        return content.getValue();
    }

    public String getContentSale() {
        return contentSale.getValue();
    }

    public String getStartDate() {
        return startDate.getValue();
    }

    public String getEndDate() {
        return endDate.getValue();
    }

//
    public ObjectProperty<Integer> getIdProperty() {
        return id;
    }

    public ObjectProperty<Integer> getSaleIdProperty() {
        return saleId;
    }
    
    public StringProperty getOrder_codeProperty() {
        return order_code;
    }

    public StringProperty getProduct_nameProperty() {
        return product_name;
    }

    public StringProperty getContentProperty() {
        return content;
    }

    public StringProperty getContentSaleProperty() {
        return contentSale;
    }

    public StringProperty getStartDateProperty() {
        return startDate;
    }

    public StringProperty getEndDateProperty() {
        return endDate;
    }

    public ObjectProperty<Integer> getNumberProperty() {
        return number;
    }

    public ObjectProperty<Integer> getPriceProperty() {
        return price;
    }

    public ObjectProperty<Integer> getDiscountProperty() {
        return discount;
    }
//

    public void setId(Integer id) {
        this.id.setValue(id);
    }

    public void setSaleId(Integer saleId) {
        this.saleId.setValue(saleId);
    }
    
    public void setOrder_code(String order_code) {
        this.order_code.setValue(order_code);
    }

    public void setProduct_name(String product_name) {
        this.product_name.setValue(product_name);
    }

    public void setContent(String content) {
        this.content.setValue(content);
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

    public void setEndDate(String endDate) {
        this.endDate.setValue(endDate);
    }

    public void setStartDate(String startDate) {
        this.startDate.setValue(startDate);
    }

    public void setContentSale(String contentSale) {
        this.contentSale.setValue(contentSale);
    }

}
