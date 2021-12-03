package com.foodyshop.model;

public class Respond {
    
    private int status;

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
                ", msg " + msg;
    }
}
