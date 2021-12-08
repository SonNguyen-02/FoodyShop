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
    public static final String MALE = "Male";
    public static final String FEMALE = "Female";
    public static final String ORTHER = "Orther";

    private int id;
    private StringProperty phone;
    private String password;
    private StringProperty address;
    private ObjectProperty<Integer> gender;
    private StringProperty genderVal;
    private StringProperty datebirth;
    private StringProperty name;
    private String img;
    private int status;
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
        statusVal = new SimpleStringProperty();
        genderVal = new SimpleStringProperty();
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

    public String getname() {
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

    public int getStatus() {
        return status;
    }
    
    public StringProperty getStatusVal() {
        return statusVal;
    }

    public StringProperty getGenderVal() {
        return genderVal;
    }
    
     public void setGender(Integer gender) {
        this.gender.setValue(gender);
        if (gender == 0) {
            this.genderVal.setValue(MALE);
        }else if(gender == 1){
            this.genderVal.setValue(FEMALE);
        }
        else {
            this.genderVal.setValue(ORTHER);
        }
    }
    
    public void setStatus(Integer status) {
        this.status = status;
        if (status == 1) {
            this.statusVal.setValue(LOCK);
        } else {
            this.statusVal.setValue(UNLOCK);
        }
    }
    
    public  void setStatusVal(String statusval){
        this.statusVal.set(statusval);
        if(statusVal.equals(UNLOCK)){
            this.status = 0;
        }else{
            this.status = 1;
        }
    }
}