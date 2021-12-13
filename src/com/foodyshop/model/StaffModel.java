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
public class StaffModel {
    int id;
    String username;
    String password;
    String name;
    String created;
    String updated;
    String type;
    String status;
    
    public static final String TYPE_ADMIN = "admin";
    public static final String TYPE_STAFF ="staff";
    public static final String STATUS_LOCK = "lock";
    public static final String STATUS_UNLOCK = "unlock";
    
    ObjectProperty<Integer> idProperty;
    StringProperty usernameProperty;
    StringProperty passwordProperty;
    StringProperty nameProperty;
    StringProperty typeProperty;
    StringProperty createdProperty;
    StringProperty updatedProperty;
    StringProperty statusProperty;

    public StaffModel() {
    }

    public StaffModel(int id, String username, String password, String name, String created, String updated, String type, String status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.created = created;
        this.updated = updated;
        this.type = type;
        this.status = status;
        
        idProperty = new SimpleObjectProperty<>(id);
        usernameProperty = new SimpleStringProperty(username);
        passwordProperty = new SimpleStringProperty(password);
        nameProperty = new SimpleStringProperty(name);
        typeProperty = new SimpleStringProperty(type);
        createdProperty = new SimpleStringProperty(created);
        updatedProperty = new SimpleStringProperty(updated);
        statusProperty = new SimpleStringProperty(status);
        
        if(this.type.equals("0")){
            this.typeProperty.set(TYPE_ADMIN);
        }else{
            this.typeProperty.set(TYPE_STAFF);
        }
        
        if (this.status.equals("0")) {
            this.statusProperty.set(STATUS_UNLOCK);
        }else{
            this.statusProperty.set(STATUS_LOCK);
        }
    }

    public ObjectProperty<Integer> getIdProperty() {
        return idProperty;
    }

    public StringProperty getUsernameProperty() {
        return usernameProperty;
    }

    public StringProperty getPasswordProperty() {
        return passwordProperty;
    }

    public StringProperty getNameProperty() {
        return nameProperty;
    }

    public StringProperty getTypeProperty() {
        return typeProperty;
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
    
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        this.idProperty.setValue(id);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        this.usernameProperty.setValue(username);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        this.passwordProperty.setValue(password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.nameProperty.setValue(name);
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

    public String getType() {
        return type;
    }

    public void setType(String typeVal) {
        this.typeProperty.setValue(type);
        if(typeVal.equals(TYPE_ADMIN)){
            this.type = "0";
        }else{
            this.type = "1";
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String statusVal) {
        this.statusProperty.setValue(status);
        if(statusVal.equals(STATUS_UNLOCK)){
            this.status ="0";
        }else{
            this.status = "1";
        }
    }

   
}
