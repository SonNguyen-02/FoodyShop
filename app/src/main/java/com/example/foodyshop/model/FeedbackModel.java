package com.example.foodyshop.model;

import com.example.foodyshop.helper.Helper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FeedbackModel implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("customer_id")
    @Expose
    private int customerId;
    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("order_detail_id")
    @Expose
    private int orderDetailId;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("customer_img")
    @Expose
    private String customerImg;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("created")
    @Expose
    private String created;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerImg() {
        return customerImg;
    }

    public void setCustomerImg(String customerImg) {
        this.customerImg = customerImg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public long getTime() {
        return Helper.parseDate(created);
    }
}
