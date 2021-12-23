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
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import com.foodyshop.main.Navigator;
import com.foodyshop.model.StaffModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author DELL
 */
public class AccountController implements Initializable {

    @FXML
    private TextField edtCurrentPassword;

    @FXML
    private TextField edtNewPassword;

    @FXML
    private TextField edtRenewPassword;

    @FXML
    private Label lbName, lbEmail, lbPassword, lbCreated, lbType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        StaffModel currentAccount = CurrentAccount.getInstance().getStaff();
        lbName.setText(currentAccount.getName());
        lbEmail.setText(currentAccount.getUsername());
        lbPassword.setText(currentAccount.getPassword());
        lbType.setText(currentAccount.getTypeVal().get());
        lbCreated.setText(currentAccount.getCreated());
    }

    @FXML
    void onClickChangePassword(ActionEvent event) {
        String currentPass = edtCurrentPassword.getText().trim();
        String newPass = edtNewPassword.getText().trim();
        String renewPass = edtRenewPassword.getText().trim();
        if (isInvalidPassword(currentPass)) {
            edtCurrentPassword.requestFocus();
            return;
        }
        if (isInvalidPassword(newPass)) {
            edtNewPassword.requestFocus();
            return;
        }
        if (isInvalidPassword(renewPass)) {
            edtRenewPassword.requestFocus();
            return;
        }
        if (!newPass.equals(renewPass)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Re-entered password is not the same");
            alert.show();
            edtRenewPassword.requestFocus();
            return;
        }
        StaffModel currentAccount = CurrentAccount.getInstance().getStaff();
        if(!BCrypt.checkpw(currentPass, currentAccount.getPassword())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Password is incorrect");
            alert.show();
            edtCurrentPassword.requestFocus();
            return;
        }
        
        currentAccount.setPassword(BCrypt.hashpw(newPass, BCrypt.gensalt()));
        if (StaffHelper.editPasswordStaff(currentAccount)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("SUCCESS");
            alert.setHeaderText("Change password success!");
            alert.show();
            lbPassword.setText(currentAccount.getPassword());
            edtCurrentPassword.clear();
            edtNewPassword.clear();
            edtRenewPassword.clear();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Change password false");
            alert.show();
        }

    }

    @FXML
    void onClickLogOut(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("LogOut");
        alert.setHeaderText("You'are about to logout!");
        alert.setContentText("Do you want to save before exiting?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            CurrentAccount.getInstance().setStaff(null);
            Navigator.getInstance().goToLoginUI();
        }
    }

    private boolean isInvalidPassword(String password) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        if (password.isEmpty()) {
            alert.setHeaderText("Please enter this field");
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
