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
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author N.C.Son
 */
public class LoginController implements Initializable {

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    //login
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private PasswordField passPassword;

    @FXML
    private TextField txtEmail;

    @FXML
    void onClickSignIn(ActionEvent event) {
        // Navigator.getInstance().goToMainLayout();

        String username = txtEmail.getText().trim();
        String password = passPassword.getText().trim();

        if (!username.matches(Const.REGEX_EMAIL)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("error email");
            alert.setContentText("email invalid");
            alert.show();
            return;
        }
        if (!password.matches(Const.REGEX_PASSWORD)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error password");
            alert.setContentText("password invalid");
            alert.show();
            return;
        }

        StaffModel staff = StaffHelper.getStaffByEmail(username);
        if (staff == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Account not exited!");
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
                    alert.setContentText("Your account is locked!");
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Password is incorected!");
                alert.show();
            }
        }

    }
}