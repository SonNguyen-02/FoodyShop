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
 * @author DELL
 */
public class FeedbackModel {

    int ID;
    String customerID;
    String productID;
    int orderDetailID;
    String content;
    String created;
    String updated;
    String status;

    ObjectProperty<Integer> IDproperty;
    StringProperty customerIDProperty;
    StringProperty productIDProperty;
    ObjectProperty<Integer> orderDetailIDproperty;
    StringProperty contentProperty;
    StringProperty createdProperty;
    StringProperty updatedProperty;
    StringProperty statusProperty;

    public static final String STATUS_SHOW = "show";
    public static final String STATUS_HIDDEN = "hidden";

    public FeedbackModel() {
    }

    public FeedbackModel(int ID, String customerID, String productID, int orderDetailID, String content,String created, String updated, String status) {
        this.ID = ID;
        this.customerID = customerID;
        this.productID = productID;
        this.orderDetailID = orderDetailID;
        this.content = content;
        this.created = created;
        this.updated = updated;
        this.status = status;

        IDproperty = new SimpleObjectProperty<>(ID);
        customerIDProperty = new SimpleStringProperty(customerID);
        productIDProperty = new SimpleStringProperty(productID);
        orderDetailIDproperty = new SimpleObjectProperty<>(orderDetailID);
        contentProperty = new SimpleStringProperty(content);
        createdProperty = new SimpleStringProperty(created);
        updatedProperty = new SimpleStringProperty(updated);
        statusProperty = new SimpleStringProperty();

        if (this.status.equals("0")) {
            this.statusProperty.set(STATUS_SHOW);
        }else{
            this.statusProperty.set(STATUS_HIDDEN);
        }
    }

    public ObjectProperty<Integer> getIDproperty() {
        return IDproperty;
    }

    public StringProperty getCustomerIDProperty() {
        return customerIDProperty;
    }

    public StringProperty getProductIDProperty() {
        return productIDProperty;
    }

    public ObjectProperty<Integer> getOrderDetailIDproperty() {
        return orderDetailIDproperty;
    }

    public StringProperty getContentProperty() {
        return contentProperty;
    }

    public StringProperty getCreatedProperty() {
        return createdProperty;
    }

    public StringProperty getUpdatedProperty() {
        return updatedProperty;
    }

    public StringProperty getStatusProperty() {
        return statusProperty;
    }
 
    
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
        this.IDproperty.setValue(ID);
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
        this.customerIDProperty.setValue(customerID);
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
        this.productIDProperty.setValue(productID);
    }

    public int getOrderDetailID() {
        return orderDetailID;
    }

    public void setOrderDetailID(int orderDetailID) {
        this.orderDetailID = orderDetailID;
        this.orderDetailIDproperty.setValue(orderDetailID);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        this.contentProperty.setValue(content);
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
        this.createdProperty.setValue(created);
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
        this.updatedProperty.setValue(updated);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String statusVal) {
        this.statusProperty.setValue(statusVal);
        if(statusVal.equals(STATUS_SHOW)){
            this.status = "0";
        }else{
            this.status = "1";
        }
    }

}
