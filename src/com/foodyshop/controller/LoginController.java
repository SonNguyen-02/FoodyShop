/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.BCrypt;
import com.foodyshop.helper.StaffHelper;
import com.foodyshop.main.Const;
import com.foodyshop.main.CurrentAccount;
import com.foodyshop.main.Navigator;
import com.foodyshop.model.StaffModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author N.C.Son
 */
public class LoginController {


    @FXML
    private PasswordField passPassword;

    @FXML
    private TextField txtEmail;

    @FXML
    void onClickSignIn(ActionEvent event) {

        String username = txtEmail.getText().trim();
        String password = passPassword.getText().trim();

        if(isInvalidPassword(password)){
            passPassword.requestFocus();
            return;
        }

        StaffModel staff = StaffHelper.getStaffByEmail(username);
        if (staff == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Account not exited!");
            alert.show();
        } else {
            if (BCrypt.checkpw(password, staff.getPassword())) {
                // check account bi lock k
                if (staff.getStatus() == 0) {
                    CurrentAccount.getInstance().setStaff(staff);
                    Navigator.getInstance().goToMainLayout();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Your account is locked!");
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Password is incorected!");
                alert.show();
            }
        }
    }
    
        
    private boolean isInvalidPassword(String password) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        if (password.isEmpty()) {
            alert.setHeaderText("Please enter password");
            alert.show();
            return true;
        }
        if (password.length() < 8) {
            alert.setHeaderText("Password min length is 8");
            alert.show();
            return true;
        }
        if (!password.matches(Const.REGEX_PASSWORD)) {
            alert.setHeaderText("Password must contain at least 1 number and 1 character");
            alert.show();
            return true;
        }
        return false;
    }
}