package com.example.foodyshop.model;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foodyshop.R;
import com.example.foodyshop.config.Config;
import com.example.foodyshop.helper.Helper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CustomerModel implements Serializable {

    public static final int MALE = 0;
    public static final int FEMALE = 1;
    public static final int OTHER = 2;

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
    private Integer gender;
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
        return Config.IMG_AVATAR_DIR + img;
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

    public boolean isNotMatch(CustomerModel matchObj) {
        return (name != null && !name.isEmpty() && !name.equals(matchObj.getName()))
                || (gender != null && gender != matchObj.getGender())
                || (datebirth != null && !datebirth.isEmpty() && !datebirth.equals(matchObj.getDatebirth()))
                || (address != null && !address.isEmpty() && !address.equals(matchObj.getAddress()));
    }

    public String getPhoneHide() {
        return phone.substring(1, phone.length() - 3).replaceAll("\\d", "*") + phone.substring(phone.length() - 3);
    }

    public String getGenderString(Context context) {
        if (gender == MALE) {
            return context.getString(R.string.male);
        }
        if (gender == FEMALE) {
            return context.getString(R.string.female);
        }
        return context.getString(R.string.other);
    }

    public String getDateBirthLocal() {
        if (datebirth == null || datebirth.isEmpty()) {
            return "00/00/0000";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("vi_VN"));
        return dateFormat.format(new Date(Helper.parseDate(datebirth)));
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
