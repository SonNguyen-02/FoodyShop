package com.example.foodyshop.model;

import com.example.foodyshop.config.Config;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductModel implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("category_id")
    @Expose
    private int categoryId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("price")
    @Expose
    private int price;
    @SerializedName("discount")
    @Expose
    private int discount;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("img_detail")
    @Expose
    private String imgDetail;
    @SerializedName("status")
    @Expose
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getImg() {
        return Config.IMG_FOOD_DIR + img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImgDetail() {
        return imgDetail;
    }

    public void setImgDetail(String imgDetail) {
        this.imgDetail = imgDetail;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPriceSale(){
        return (int) (price - price * discount / 100);
    }

}
