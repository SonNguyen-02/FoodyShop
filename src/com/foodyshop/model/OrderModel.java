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

    public static final String WAIT_AOS_CF = "Wait admin or staff confirm";
    public static final String AOS_CF = "Admin or staff confirmed";
    public static final String AOS_CL = "Admin or staff cancel";
    public static final String WAIT_CUS_CF = "Wait customer confirm";
    public static final String CUS_CL = "Cutomer cancel";
    public static final String CUS_CF = "Custom confirmed";
    public static final String SHIPPING = "Shipping";
    public static final String SUCCESS_DELIVERY = "Successful delivery";

    private ObjectProperty<Integer> id;
    private StringProperty orderCode;
    private ObjectProperty<Integer> customerId;
    private StringProperty name;
    private StringProperty address;
    private StringProperty phone;
    private StringProperty note;
    private StringProperty created;
    private StringProperty updated;
    private ObjectProperty<Integer> shipPrice;
    private ObjectProperty<Integer> totalMoney;
    int status;
    private StringProperty statusVal;

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
        statusVal = new SimpleStringProperty();
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

    public int getStatus() {
        return status;
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

    public StringProperty getStatusVal() {
        return statusVal;
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

    public void setStatusVal(String statusVal) {
        this.statusVal.set(statusVal);
        if (statusVal.equals(WAIT_AOS_CF)) {
            this.status = 0;
        } else if (statusVal.equals(AOS_CF)) {
            this.status = 1;
        } else if (statusVal.equals(AOS_CL)) {
            this.status = -1;
        } else if (statusVal.equals(WAIT_CUS_CF)) {
            this.status = 2;
        } else if (statusVal.equals(CUS_CL)) {
            this.status = -2;
        } else if (statusVal.equals(CUS_CF)) {
            this.status = 3;
        } else if (statusVal.equals(SHIPPING)) {
            this.status = 4;
        } else {
            this.status = 5;
        }
    }

    public void setStatus(Integer status) {
        this.status = status;
        switch(status){
            case 0:
                this.statusVal.set(WAIT_AOS_CF);
                break;
            case 1:
                this.statusVal.set(AOS_CF );
                break;
            case -1:
                this.statusVal.set(AOS_CL );
                break;
            case 2:
                this.statusVal.set(WAIT_CUS_CF);
                break;
            case -2:
                this.statusVal.set(CUS_CL );
                break;
            case 3:
                this.statusVal.set(CUS_CF );
                break;
            case 4:
                this.statusVal.set(SHIPPING);
                break;
            case 5:
                this.statusVal.set(SUCCESS_DELIVERY );
                break;
        }
    }
}
