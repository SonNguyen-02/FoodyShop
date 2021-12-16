/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.main;

import com.foodyshop.model.StaffModel;

/**
 *
 * @author N.C.Son
 */
public class CurrentAccount {
    
    private StaffModel staff;
    
    private CurrentAccount(){  
    }
    
    private static CurrentAccount instance;
    
    public static CurrentAccount getInstance() {
        if(instance == null){
            instance = new CurrentAccount();
        }
        return instance;
    }

    public StaffModel getStaff() {
        return staff;
    }

    public void setStaff(StaffModel staff) {
        this.staff = staff;
    }
    
    public boolean isStaff(){
        return staff.getType() == 1;
    }
}
