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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 *
 * @author DELL
 */
public class AddStaffController implements Initializable {

    private ObservableList<StaffModel> listStaff;
    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPassword;

     @FXML
    private ComboBox<String> cbType;

    @FXML
    private Button btnSubmit;

    @FXML
    private Button btnCancel;

    private IOnAddStaffSuccess mIOnAddStaffSuccess;

    public interface IOnAddStaffSuccess {

        void IOnAddStaffSuccess(StaffModel staffModel);
    }

    public void initCallback(IOnAddStaffSuccess mIOnAddStaffSuccess) {
        this.mIOnAddStaffSuccess = mIOnAddStaffSuccess;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbType.getItems().add("admin");
        cbType.getItems().add("staff");

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
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        String name = txtName.getText().trim();

        if (username.isEmpty()) {
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
       }  else {            
            StaffModel staffModel = StaffHelper.insertStaff(txtUsername.getText(), txtPassword.getText(), txtName.getText(),cbType.getValue());        
            if (staffModel != null) {
                mIOnAddStaffSuccess.IOnAddStaffSuccess(staffModel);
                Navigator.getInstance().getModalStage().close();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("SUCCESS");
                alert.setHeaderText("Insert success");
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Insert false!");
                alert.show();
            }
        }
//else {
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//            alert.setTitle("Submit");
//            alert.setHeaderText("Do you want submit?");
//            alert.showAndWait().ifPresent(btnType -> {
//                if (btnType == ButtonType.OK) {
//                    Navigator.getInstance().getModalStage().close();
//                }
//            });
//        }
        

//        StaffModel staff = new StaffModel();
//        staff.setUsername(username);
//        staff.setPassword(password);
//        staff.setName(name);
    }

}
