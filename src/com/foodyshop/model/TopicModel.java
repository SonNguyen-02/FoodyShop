/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.model;

import static com.foodyshop.main.Config.IMG_TOPIC_DIR;
import com.foodyshop.main.Const;
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

    public static final String SHOW = "Show";
    public static final String HIDDEN = "Hidden";
    
    private int id;
    private StringProperty name;
    private String img;
    private int status;
    private ImageView mImageView;
    private ObservableValue<ImageView> imgView;
    private StringProperty created;
    private StringProperty statusVal;

    public TopicModel() {
        this.name = new SimpleStringProperty();
        this.created = new SimpleStringProperty();
        this.statusVal = new SimpleStringProperty();
        mImageView = new ImageView();
        imgView = new SimpleObjectProperty<>(mImageView);
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

    public int getStatus() {
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

    public StringProperty getStatusVal() {
        return statusVal;
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
        mImageView.setImage(image);
        image.errorProperty().addListener((observable, oldValue, isErr) -> {
            if(isErr){
                System.out.println(image.getException().getMessage());
                mImageView.setImage(Const.NO_IMAGE_OBJ);
            }
        });
        
    }

    public void setCreated(String created) {
        this.created.setValue(created);
    }
    
    public void setStatus(String statusVal){
        this.statusVal.set(statusVal);
        if(statusVal.equals(SHOW)){
            this.status = 0;
        }else{
            this.status = 1;
        }
    }

    public void setStatus(Integer status) {
        this.status = status;
        if(status == 0){
            this.statusVal.set(SHOW);
        }else{
            this.statusVal.set(HIDDEN);
        }
    }

    public ObservableValue<ImageView> getImgView() {
        return imgView;
    }
  
    @Override
    public String toString() {
        return this.name.get();
    }

}
