package com.example.foodyshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Respond {

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("msg")
    @Expose
    private String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess(){
        return status == 1;
    }

    @Override
    public String toString() {
        return "Respond " +
                "status " + status +
                ", msg '" + msg;
    }
}
