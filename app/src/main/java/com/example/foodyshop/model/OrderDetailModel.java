package com.example.foodyshop.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderDetailModel implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("order_id")
    @Expose
    private int orderId;
    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("sale_id")
    @Expose
    private Integer saleId;
    @SerializedName("number")
    @Expose
    private int amount;
    @SerializedName("price")
    @Expose
    private int price;
    @SerializedName("discount")
    @Expose
    private Integer discount;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("img")
    @Expose
    private String img;
    private boolean isChecked;

    public OrderDetailModel() {
    }

    public OrderDetailModel(@NonNull ProductModel product, int amount) {
        this.productId = product.getId();
        this.saleId = product.getSaleId();
        this.amount = amount;
        this.price = product.getPrice();
        this.discount = product.getDiscount();
        this.name = product.getName();
        this.img = product.getImg();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Integer getSaleId() {
        return saleId;
    }

    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getPriceSale() {
        if (discount == null) {
            return price;
        } else {
            return price - (price * discount / 100);
        }
    }
}
