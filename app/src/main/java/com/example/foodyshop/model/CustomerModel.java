package com.example.foodyshop.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerModel {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("gender")
    @Expose
    private int gender;
    @SerializedName("datebirth")
    @Expose
    private String datebirth;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("status")
    @Expose
    private int status;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @NonNull
    @Override
    public String toString() {
        return "CustomerModel{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", datebirth='" + datebirth + '\'' +
                ", address='" + address + '\'' +
                ", img='" + img + '\'' +
                ", status=" + status +
                '}';
    }
}
