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
    StringProperty orderCode;
    ObjectProperty<Integer> customerId;
    StringProperty name;
    StringProperty address;
    StringProperty phone;
    StringProperty note;
    StringProperty created;
    StringProperty updated;
    ObjectProperty<Integer> shipPrice;
    ObjectProperty<Integer> totalMoney;
    ObjectProperty<Integer> status;

    public OrderModel() {
        this.id = new SimpleObjectProperty<>();
        this.orderCode = new SimpleStringProperty();
        this.customerId = new SimpleObjectProperty<>();
        this.name = new SimpleStringProperty();
        this.address = new SimpleStringProperty();
        this.phone = new SimpleStringProperty();
        this.note = new SimpleStringProperty();
        this.created = new SimpleStringProperty();
        this.updated = new SimpleStringProperty();
        this.shipPrice = new SimpleObjectProperty<>();
        this.totalMoney = new SimpleObjectProperty<>();
        this.status = new SimpleObjectProperty<>();
    }

    //Getter    
    public Integer getId() {
        return id.getValue();
    }

    public String getOrderCode() {
        return orderCode.getValue();
    }

    public Integer getCustomerId() {
        return customerId.getValue();
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

    public Integer getTotalMoney() {
        return totalMoney.getValue();
    }

    public Integer getShipPrice() {
        return shipPrice.getValue();
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

    public StringProperty getOrderCodeProperty() {
        return orderCode;
    }

    public ObjectProperty<Integer> getCustomerIdProperty() {
        return customerId;
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

    public ObjectProperty<Integer> getTotalMoneyProperty() {
        return totalMoney;
    }

    public ObjectProperty<Integer> getShipPriceProperty() {
        return shipPrice;
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

    public void setOrderCode(String order_code) {
        this.orderCode.setValue(order_code);
    }

    public void setCustomerId(Integer customer_id) {
        this.customerId.setValue(customer_id);
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

    public void setShipPrice(Integer ship_price) {
        this.shipPrice.setValue(ship_price);
    }

    public void setTotalMoney(Integer total_money) {
        this.totalMoney.setValue(total_money);
    }

    public void setStatus(Integer status) {
        this.status.setValue(status);
    }
}
