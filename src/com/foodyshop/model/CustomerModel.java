/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.model;

import static com.foodyshop.main.Config.IMG_TOPIC_DIR;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author APlaptop
 */
public class CustomerModel {
    public static final String LOCK = "Lock";
    public static final String UNLOCK = "Unlock";
    
    private int id;
    private String phone;
    private String password;
    private String address;
    private int gender;
    private String datebirth;
    private StringProperty name;
    private String img;
    private int status;

    private ObservableValue<ImageView> imgView;
    private StringProperty created;
    private StringProperty statusVal;

    public CustomerModel() {
        this.name = new SimpleStringProperty();
        this.created = new SimpleStringProperty();
        this.statusVal = new SimpleStringProperty();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDatebirth() {
        return datebirth;
    }

    public void setDatebirth(String datebirth) {
        this.datebirth = datebirth;
    }

    public StringProperty getName() {
        return name;
    }

    public void setName(StringProperty name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
        String url = IMG_TOPIC_DIR + img;
        Image image = new Image(url, 100, 100, false, true, true);
        this.imgView = new SimpleObjectProperty<>(new ImageView(image));
    }

    public int getStatus() {
        return status;
    }

    public ObservableValue<ImageView> getImgView() {
        return imgView;
    }

    public void setImgView(ObservableValue<ImageView> imgView) {
        this.imgView = imgView;
    }

    public StringProperty getCreated() {
        return created;
    }

    public void setCreated(StringProperty created) {
        this.created = created;
    }

    public StringProperty getStatusVal() {
        return statusVal;
    }

    public void setStatusVal(String statusVal){
        this.statusVal.set(statusVal);
        if(statusVal.equals(LOCK)){
            this.status = 0;
        }else{
            this.status = 1;
        }
    }

    public void setStatus(Integer status) {
        this.status = status;
        if(status == 0){
            this.statusVal.set(LOCK);
        }else{
            this.statusVal.set(UNLOCK);
        }
    }
}
