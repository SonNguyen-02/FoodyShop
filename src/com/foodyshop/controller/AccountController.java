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
import sun.plugin.javascript.navig4.Navigator;

/**
 *
 * @author DELL
 */
public class AccountController implements Initializable{
     @FXML
    private AnchorPane aPaneAccount;
    
     @FXML
    private Button btnLogOut;

    @FXML
    private Button btnChangePassword;
    
    Stage stage;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      
    }
    


    public AccountController() {
    }

    @FXML
    void onClickChangePassword(ActionEvent event) {

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
