/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author DELL
 */
public class StaffModel {
  
    public static final String TYPE_ADMIN = "admin";
    public static final String TYPE_STAFF ="staff";
    
    public static final String STATUS_LOCK = "lock";
    public static final String STATUS_UNLOCK = "unlock";
    
    private int id;
    private int status;
    private int type;  
    private StringProperty username;
    private StringProperty password;
    private StringProperty name;
    private StringProperty typeVal;
    private StringProperty created;
    private StringProperty statusVal;

    public StaffModel() {
        this.username = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
        this.created = new SimpleStringProperty();
        typeVal = new SimpleStringProperty();
        statusVal = new SimpleStringProperty();
    
    }
          
    
    public StringProperty getStatusVal() {
        return statusVal;
    }

    public StringProperty getTypeVal() {
        return typeVal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
        if (status == 0) {
            this.statusVal.setValue(STATUS_UNLOCK);
        } else {
            this.statusVal.setValue(STATUS_LOCK);
        }
    }
    
    
    public void setStatusVal(String statusVal) {
        this.statusVal.setValue(statusVal);
        if(statusVal.equals(STATUS_UNLOCK)){
            this.status = 0;
        }else{
            this.status = 1;
        }
    }

    public int getType() {
        return type;
    }
    
   
    public void setType(Integer type) {
        this.type = type;
        if (type == 0) {
            this.typeVal.setValue(TYPE_ADMIN);
        } else {
            this.typeVal.setValue(TYPE_STAFF);
        }
    }
    
    public void setTypeVal(String typeVal) {
        this.typeVal.set(typeVal);
        if(typeVal.equals(TYPE_ADMIN)){
            this.type = 0;
        }else{
            this.type = 1;
        }
    }

    

    public StringProperty getUsernameProperty() {
        return username;
    }

    public String getUsername() {
        return username.getValue();
    }
    
    public void setUsername(String username) {
        this.username.setValue(username);
    }

    public StringProperty getPasswordProperty() {
        return password;
    }

    public String getPassword() {
        return password.getValue();
    }
    
    public void setPassword(String password) {
        this.password.setValue(password);
    }

    public StringProperty getNameProperty() {
        return name;
    }

    public String getName() {
        return name.getValue();
    }
    
    public void setName(String name) {
        this.name.setValue(name);
    }

    public StringProperty getCreatedProperty() {
        return created;
    }

    public String getCreated() {
        return created.getValue();
    }
    
    public void setCreated(String created) {
        this.created.setValue(created);
    }

   
}
