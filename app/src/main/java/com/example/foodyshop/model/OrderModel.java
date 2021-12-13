package com.example.foodyshop.model;

import com.example.foodyshop.helper.Helper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OrderModel implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("order_code")
    @Expose
    private String orderCode;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("updated")
    @Expose
    private String updated;
    @SerializedName("ship_price")
    @Expose
    private int shipPrice;
    @SerializedName("total_money")
    @Expose
    private int totalMoney;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("order_details")
    @Expose
    private List<OrderDetailModel> orderDetails = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public int getShipPrice() {
        return shipPrice;
    }

    public void setShipPrice(int shipPrice) {
        this.shipPrice = shipPrice;
    }

    public int getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(int totalMoney) {
        this.totalMoney = totalMoney;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<OrderDetailModel> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailModel> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public long getTime() {
        return Helper.parseDate(created);
    }

    public int getTotalProduct() {
        if (orderDetails != null && !orderDetails.isEmpty()) {
            int total = 0;
            for (OrderDetailModel ord : orderDetails) {
                if (ord != null) {
                    total += ord.getAmount();
                }
            }
            return total;
        } else {
            return 0;
        }
    }
}
