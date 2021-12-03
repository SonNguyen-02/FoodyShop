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

public class CategoryModel {
    
    public static final String HIDDEN = "Hidden";
    public static final String SHOW = "Show";

    private ObjectProperty<Integer> id;
    private ObjectProperty<Integer> topic_id;
    private StringProperty name;
    private StringProperty created;
    private ObjectProperty<Integer> status;
    private StringProperty statusVal;

    public CategoryModel() {
        this.id = new SimpleObjectProperty<>();
        this.topic_id = new SimpleObjectProperty<>();
        this.name = new SimpleStringProperty();
        this.created = new SimpleStringProperty();
        this.status = new SimpleObjectProperty<>();
        statusVal = new SimpleStringProperty();
    }

    public Integer getId() {
        return id.getValue();
    }

    public Integer getTopic_id() {
        return topic_id.getValue();
    }

    public String getName() {
        return name.getValue();
    }
    
    public String getCreated() {
        return created.getValue();
    }
    
    public Integer getStatus() {
        return status.getValue();
    }

    public ObjectProperty<Integer> getIdProperty() {
        return id;
    }

    public ObjectProperty<Integer> getTopic_idProperty() {
        return topic_id;
    }

    public StringProperty getNameProperty() {
        return name;
    }
    
     public StringProperty getCreatedProperty() {
        return created;
    }

    public ObjectProperty<Integer> getStatusProperty() {
        return status;
    }
    
    public void setStatusVal(String status){
        this.statusVal.set(status);
        if(status.equals(SHOW)){
            this.status.set(0);
        }else{
            this.status.set(1);
        }
    }
    
    public StringProperty getStatusVal(){
        return statusVal;
    }

    public void setId(Integer id) {
        this.id.setValue(id);
    }

    public void setTopic_id(Integer topic_id) {
        this.topic_id.setValue(topic_id);
    }

    public void setName(String name) {
        this.name.setValue(name);
    }
    
    public void setCreated(String created) {
        this.created.setValue(created);
    }

    public void setStatus(Integer status) {
        this.status.setValue(status);
        if(status == 0){
            this.statusVal.setValue(SHOW);
        }else{
            this.statusVal.setValue(HIDDEN);
        }
    }
    


}
