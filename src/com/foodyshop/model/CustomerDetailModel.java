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
public class CustomerDetailModel {

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
    private ObjectProperty<Integer> totalMoney;
    private StringProperty created;
    int status;
    private StringProperty statusVal;
    private StringProperty note;
    private StringProperty content;

    public CustomerDetailModel() {
        this.id = new SimpleObjectProperty<>();
        this.orderCode = new SimpleStringProperty();
        this.totalMoney = new SimpleObjectProperty<>();
        this.created = new SimpleStringProperty();
        statusVal = new SimpleStringProperty();
        this.note = new SimpleStringProperty();
        this.content = new SimpleStringProperty();
    }

    public Integer getId() {
        return id.getValue();
    }

    public ObjectProperty<Integer> getIdProperty() {
        return id;
    }

    public StringProperty getOrderCode() {
        return orderCode;
    }

    public ObjectProperty<Integer> getTotalMoney() {
        return totalMoney;
    }

    public StringProperty getCreated() {
        return created;
    }

    public int getStatus() {
        return status;
    }

    public StringProperty getStatusVal() {
        return statusVal;
    }

    public String getNote() {
        return note.getValue();
    }
    
    public String getContent() {
        return content.getValue();
    }
    
    public void setId(Integer id) {
        this.id.setValue(id);
    }

    public void setOrderCode(String orderCode) {
        this.orderCode.setValue(orderCode);
    }

    public void setTotalMoney(Integer totalMoney) {
        this.totalMoney.setValue(totalMoney);
    }

    public void setCreated(String created) {
        this.created.setValue(created);
    }

    public void setNote(String note) {
        this.note.setValue(note);
    }
    
    public void setContent(String content) {
        this.content.setValue(content);
    }
    
    public void setStatus(Integer status) {
        this.status = status;
        switch (status) {
            case 0:
                this.statusVal.set(WAIT_AOS_CF);
                break;
            case 1:
                this.statusVal.set(AOS_CF);
                break;
            case -1:
                this.statusVal.set(AOS_CL);
                break;
            case 2:
                this.statusVal.set(WAIT_CUS_CF);
                break;
            case -2:
                this.statusVal.set(CUS_CL);
                break;
            case 3:
                this.statusVal.set(CUS_CF);
                break;
            case 4:
                this.statusVal.set(SHIPPING);
                break;
            case 5:
                this.statusVal.set(SUCCESS_DELIVERY);
                break;
        }
    }

    public void setStatusVal(String statusVal) {
        this.statusVal.set(statusVal);
    }

}
