/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.model;

import static com.foodyshop.main.Config.IMG_TOPIC_DIR;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author X PC
 */
public class TopicModel {

    int id;
    StringProperty name;
    String img;

    ObservableValue<ImageView> imgView;
    StringProperty created;
    ObjectProperty<Integer> status;

    public TopicModel() {
        this.name = new SimpleStringProperty();
        this.created = new SimpleStringProperty();
        this.status = new SimpleObjectProperty<>();
    }

    public int getIdProperty() {
        return id;
    }

    public String getImgProperty() {
        return img;
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

    public int getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public String getImg() {
        return img;
    }

    public String getCreated() {
        return created.getValue();
    }

    public Integer getStatus() {
        return status.getValue();
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public void setImg(String img) {
        this.img = img;
        String url = IMG_TOPIC_DIR + img;
        Image image = new Image(url, 100, 100, false, true, true);
        this.imgView = new SimpleObjectProperty<>(new ImageView(image));
    }

    public void setCreated(String created) {
        this.created.setValue(created);
    }

    public void setStatus(Integer status) {
        this.status.setValue(status);
    }

    public ObservableValue<ImageView> getImgView() {
        return imgView;
    }
  
    @Override
    public String toString() {
        return this.name.get();
    }

}
