package com.foodyshop.controller;

import com.foodyshop.helper.BCrypt;
import com.foodyshop.helper.StaffHelper;
import com.foodyshop.main.Const;
import com.foodyshop.main.Navigator;
import com.foodyshop.model.StaffModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author DELL
 */
public class ChangePasswordStaffController {

    private Stage mStage;
    private StaffModel mStaff;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lbEmail;

    public void initAccount(Stage stage, StaffModel staff) {
        mStage = stage;
        mStaff = staff;
        lbEmail.setText(staff.getUsername());
    }

    @FXML
    void onClickCancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close");
        alert.setHeaderText("Do you want close?");
        alert.showAndWait().ifPresent(btnType -> {
            if (btnType == ButtonType.OK) {
                Navigator.getInstance().getModalStage().close();
            }
        });
    }

    @FXML
    void onClickSubmit(ActionEvent event) {
        String password = txtPassword.getText().trim();
        if(isInvalidPassword(password)){
            txtPassword.requestFocus();
            return;
        }

        mStaff.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        if (StaffHelper.editPasswordStaff(mStaff)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("SUCCESS");
            alert.setHeaderText("Change password success!");
            alert.show();
            mStage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Change password false");
            alert.show();
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
