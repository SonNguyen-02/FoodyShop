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
    String content;
    String status;

    ObjectProperty<Integer> IDproperty;
    StringProperty customerIDProperty;
    StringProperty productIDProperty;
    StringProperty contentProperty;
    StringProperty statusProperty;

    public FeedbackModel() {
    }

    public FeedbackModel(int ID, String customerID, String productID, String content, String status) {
        this.ID = ID;
        this.customerID = customerID;
        this.productID = productID;
        this.content = content;
        this.status = status;

        IDproperty = new SimpleObjectProperty<>(ID);
        customerIDProperty = new SimpleStringProperty(customerID);
        productIDProperty = new SimpleStringProperty(productID);
        contentProperty = new SimpleStringProperty(content);
        statusProperty = new SimpleStringProperty(status);
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

    public StringProperty getContentProperty() {
        return contentProperty;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        this.contentProperty.setValue(content);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.statusProperty.setValue(status);
    }

}
