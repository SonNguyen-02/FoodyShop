/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.model;

import java.time.LocalDateTime;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author APlaptop
 */
public class OrderModel {

    ObjectProperty<Integer> id;
    StringProperty order_code;
    ObjectProperty<Integer> customer_id;
    StringProperty name;
    StringProperty address;
    StringProperty phone;
    StringProperty note;
    StringProperty created;
    StringProperty updated;
    ObjectProperty<Integer> ship_price;
    ObjectProperty<Integer> total_money;
    ObjectProperty<Integer> status;

    public OrderModel() {
        this.id = new SimpleObjectProperty<>();
        this.order_code = new SimpleStringProperty();
        this.customer_id = new SimpleObjectProperty<>();
        this.name = new SimpleStringProperty();
        this.address = new SimpleStringProperty();
        this.phone = new SimpleStringProperty();
        this.note = new SimpleStringProperty();
        this.created = new SimpleStringProperty();
        this.updated = new SimpleStringProperty();
        this.ship_price = new SimpleObjectProperty<>();
        this.total_money = new SimpleObjectProperty<>();
        this.status = new SimpleObjectProperty<>();
    }
 
    
    //Getter    
    public Integer getId() {
        return id.getValue();
    }

    public String getOrder_code() {
        return order_code.getValue();
    }

    public Integer getCustomer_id() {
        return customer_id.getValue();
    }

    public String getName() {
        return name.getValue();
    }

    public String getAddress() {
        return address.getValue();
    }

    public Integer getStatus() {
        return status.getValue();
    }

    public Integer getTotal_money() {
        return total_money.getValue();
    }

    public Integer getShip_price() {
        return ship_price.getValue();
    }

    public String getUpdated() {
        return updated.getValue();
    }

    public String getCreated() {
        return created.getValue();
    }

    public String getNote() {
        return note.getValue();
    }

    public String getPhone() {
        return phone.getValue();
    }
    
    
    
    //Getter Property    
     public ObjectProperty<Integer> getIdProperty() {
        return id;
    }

    public StringProperty getOrder_codeProperty() {
        return order_code;
    }

    public ObjectProperty<Integer> getCustomer_idProperty() {
        return customer_id;
    }

    public StringProperty getNameProperty() {
        return name;
    }

    public StringProperty getAddressProperty() {
        return address;
    }

    public ObjectProperty<Integer> getStatusProperty() {
        return status;
    }

    public ObjectProperty<Integer> getTotal_moneyProperty() {
        return total_money;
    }

    public ObjectProperty<Integer> getShip_priceProperty() {
        return ship_price;
    }

    public StringProperty getUpdatedProperty() {
        return updated;
    }

    public StringProperty getCreatedProperty() {
        return created;
    }

    public StringProperty getNoteProperty() {
        return note;
    }

    public StringProperty getPhoneProperty() {
        return phone;
    }
    
    
    //Setter
    public void setId(Integer id) {
        this.id.setValue(id);
    }

    public void setOrder_code(String order_code) {
        this.order_code.setValue(order_code);
    }

    public void setCustomer_id(Integer customer_id) {
        this.customer_id.setValue(customer_id);
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public void setAddress(String address) {
        this.address.setValue(address);
    }

    public void setPhone(String phone) {
        this.phone.setValue(phone);
    }

    public void setNote(String note) {
        this.note.setValue(note);
    }

    public void setCreated(String created) {
        this.created.setValue(created);
    }

    public void setUpdated(String updated) {
        this.updated.setValue(updated);
    }

    public void setShip_price(Integer ship_price) {
        this.ship_price.setValue(ship_price);
    }

    public void setTotal_money(Integer total_money) {
        this.total_money.setValue(total_money);
    }

    public void setStatus(Integer status) {
        this.status.setValue(status);
    }

}
