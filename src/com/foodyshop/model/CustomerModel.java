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
 * @author APlaptop
 */
public class CustomerModel {
    public static final String LOCK = "Lock";
    public static final String UNLOCK = "Unlock";
    
    private int id;
    private StringProperty phone;
    private String password;
    private StringProperty address;
    private ObjectProperty<Integer> gender;
    private StringProperty datebirth;
    private StringProperty name;
    private String img;
    private ObjectProperty<Integer> status;

    private ObservableValue<ImageView> imgView;
    private StringProperty created;
    private StringProperty statusVal;

    public CustomerModel() {
        this.name = new SimpleStringProperty();
        this.phone = new SimpleStringProperty();
        this.address = new SimpleStringProperty();
        this.gender = new SimpleObjectProperty<>();
        this.datebirth = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
        this.created = new SimpleStringProperty();
        this.statusVal = new SimpleStringProperty();
        this.status = new SimpleObjectProperty<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone.getValue();
    }

    public StringProperty getPhoneProperty() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone.setValue(phone);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address.getValue();
    }

    public StringProperty getAddressProperty() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address.setValue(address);
    }

    public int getGender() {
        return gender.getValue();
    }
    
    public ObjectProperty<Integer> getGenderProperty() {
        return gender;
    }
    
    public void setGender(Integer gender) {
        this.gender.setValue(gender);
    }

    public String getDatebirth() {
        return datebirth.getValue();
    }
    
    public StringProperty getDatebirthProperty() {
        return datebirth;
    }
    
    public void setDatebirth(String datebirth) {
        this.datebirth.setValue(datebirth);
    }

    public StringProperty getNameProperty() {
        return name;
    }
    public  String getname(){
        return name.getValue();
    }
    public void setName(String name) {
        this.name.setValue(name);
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

    public Integer getStatus() {
        return status.getValue();
    }
     
    public ObjectProperty<Integer> getStatusProperty() {
        return status;
    }
    
    public ObservableValue<ImageView> getImgView() {
        return imgView;
    }

    public void setImgView(ObservableValue<ImageView> imgView) {
        this.imgView = imgView;
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

    public StringProperty getStatusVal() {
        return statusVal;
    }

//    public void setStatus(String statusVal){
//        this.statusVal.set(statusVal);
//        if(statusVal.equals(LOCK)){
//            this.status = 0;
//        }else{
//            this.status = 1;
//        }
//    }
//
//    public void setStatus(Integer status) {
//        this.status = status;
//        if(status == 0){
//            this.statusVal.set(LOCK);
//        }else{
//            this.statusVal.set(UNLOCK);
//        }
//    }
     public void setStatus(Integer status) {
        this.status.setValue(status);
    }
}
