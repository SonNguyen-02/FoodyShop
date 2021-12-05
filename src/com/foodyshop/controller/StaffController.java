/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.model.StaffModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 *
 * @author DELL
 */
public class StaffController implements Initializable{
    @FXML
    private TableView<StaffModel> tvStaff;

    @FXML
    private TableColumn<StaffModel, Integer> tcStt;

    @FXML
    private TableColumn<StaffModel, String> tcUsername;

    @FXML
    private TableColumn<StaffModel, String> tcPassword;

    @FXML
    private TableColumn<StaffModel, String> tcName;

    @FXML
    private TableColumn<StaffModel, String> tcType;

    @FXML
    private TableColumn<StaffModel, String> tcCreated;

    @FXML
    private TableColumn<StaffModel, String> tcUpdated;

    @FXML
    private TableColumn<StaffModel, String> tcStatus;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnLock;

    @FXML
    private Button btnUnLock;

    @FXML
    private Button btnChangePassword;

    @FXML
    private Button btnDelete;
    
    
     @Override
    public void initialize(URL location, ResourceBundle resources) {
        //
    }
    
    

    @FXML
    void onClickAdd(ActionEvent event) {

    }

    @FXML
    void onClickChangePassword(ActionEvent event) {

    }

    @FXML
    void onClickDelete(ActionEvent event) {

    }

    @FXML
    void onClickEdit(ActionEvent event) {

    }

    @FXML
    void onClickLock(ActionEvent event) {

    }

    @FXML
    void onClickUnLock(ActionEvent event) {

    }

   
}
