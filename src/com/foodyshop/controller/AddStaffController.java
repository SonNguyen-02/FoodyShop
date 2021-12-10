/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.StaffHelper;
import com.foodyshop.main.Navigator;
import com.foodyshop.model.StaffModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

/**
 *
 * @author DELL
 */
public class AddStaffController implements Initializable {

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPassword;

    @FXML
    private ChoiceBox<String> cbType;

    @FXML
    private ChoiceBox<String> cbStatus;

    @FXML
    private Button btnSubmit;

    @FXML
    private Button btnCancel;
    
    private IOnAddSuccess mIOnAddSuccess;


    public interface IOnAddSuccess {

        void onAddSuccess(StaffModel staff);
    }

    public void initCallback(IOnAddSuccess mIOnAddSuccess) {
        this.mIOnAddSuccess = mIOnAddSuccess;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbType.getItems().add("admin");
        cbType.getItems().add("staff");
        cbStatus.getItems().add("lock");
        cbStatus.getItems().add("unlock");

//        btnSubmit.setOnMouseClicked(this::onClickSubmit);
//        btnCancel.setOnMouseClicked(this::onClickCancel);
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
        //StaffModel staff = StaffHelper.insertStaff(txtUsername.getText(),txtPassword.getText(),txtName.getText());
        String userName = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        String name = txtName.getText().trim();

        if (userName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Please enter username!");
            alert.show();
            return;
        }
        if (password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Please enter password!");
            alert.show();
            return;
        }
        if (name.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Please enter name!");
            alert.show();
            return;
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Submit");
            alert.setHeaderText("Do you want submit?");
            alert.showAndWait().ifPresent(btnType -> {
                if (btnType == ButtonType.OK) {
                    Navigator.getInstance().getModalStage().close();
                }
            });
        }

        StaffModel staff = new StaffModel();
        staff.setUsername(userName);
        staff.setPassword(password);
        staff.setName(name);
    }

}
