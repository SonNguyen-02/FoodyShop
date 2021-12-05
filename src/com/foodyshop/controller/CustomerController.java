/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.main.Navigator;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author APlaptop
 */
public class CustomerController implements Initializable {

    @FXML
    private TableView<?> tblCustomer;

    @FXML
    private TableColumn<?, ?> ctStt;

    @FXML
    private TableColumn<?, ?> ctPhone;

    @FXML
    private TableColumn<?, ?> ctName;

    @FXML
    private TableColumn<?, ?> ctGender;

    @FXML
    private TableColumn<?, ?> ctDatebirth;

    @FXML
    private TableColumn<?, ?> ctAddress;

    @FXML
    private TableColumn<?, ?> ctImage;

    @FXML
    private TableColumn<?, ?> ctStatus;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
