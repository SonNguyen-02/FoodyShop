/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author DELL
 */
public class AddStaffController implements Initializable{

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

     @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbType.getItems().add("admin");
        cbType.getItems().add("staff");
        cbStatus.getItems().add("lock");
        cbStatus.getItems().add("unlock");
    }
    
    @FXML
    void onClickCancel(ActionEvent event) {

    }

    @FXML
    void onClickSubmit(ActionEvent event) {

    }

 
    
}
