/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.foodyshop.main.Navigator;
import com.foodyshop.model.StaffModel;
import javafx.collections.ObservableList;

/**
 *
 * @author DELL
 */
public class AccountController implements Initializable{
    private ObservableList<StaffModel> listStaff;
    private StaffModel staff;
     @FXML
    private AnchorPane aPaneAccount;
    
     @FXML
    private Button btnLogOut;

    @FXML
    private Button btnChangePassword;
    
    Stage stage;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      //btnChangePassword.setOnMouseClicked(e -> com.foodyshop.main.Navigator.getInstance().showChangePasswordAccount(staff));
    }
    


    public AccountController() {
    }

    @FXML
    void onClickChangePassword(ActionEvent event) {
 //StaffModel staff = tvStaff.getSelectionModel().getSelectedItem();
//        if (staff != null) {
            btnChangePassword.setOnMouseClicked(e -> Navigator.getInstance().showChangePasswordAccount(staff));
//.showEditStaff(staff, new EditStaffController.IOnEditStaffSuccess()
//{
//                @Override
//                public void callback() {
//                 tvStaff.refresh();
//                }
//            }));
//        } else {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("ERROR");
//            alert.setHeaderText("Please choose staff!");
//            alert.show();
//        }
    }

    @FXML
    void onClickLogOut(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("LogOut");
        alert.setHeaderText("You'are about to logout!");
        alert.setContentText("Do you want to save before exiting?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            stage = (Stage) aPaneAccount.getScene().getWindow();
            stage.close();
        }
        
        
    }

    
}
