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

    ObjectProperty<Integer> id;
    ObjectProperty<Integer> topic_id;
    StringProperty name;
    StringProperty created;
    ObjectProperty<Integer> status;

    public CategoryModel() {
        this.id = new SimpleObjectProperty<>();
        this.topic_id = new SimpleObjectProperty<>();
        this.name = new SimpleStringProperty();
        this.created = new SimpleStringProperty();
        this.status = new SimpleObjectProperty<>();
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
    }

}
