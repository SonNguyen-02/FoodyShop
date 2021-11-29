/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.main.Navigator;
import java.awt.Label;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author APlaptop
 */
public class Order_DetailController implements Initializable {
      @FXML
    private TableView<?> tblOrder_detail;

    @FXML
    private TableColumn<?, ?> tcStt;

    @FXML
    private TableColumn<?, ?> tcProduct;

    @FXML
    private TableColumn<?, ?> tcNumber;

    @FXML
    private TableColumn<?, ?> tcPrice;

    @FXML
    private TableColumn<?, ?> tcDiscount;

    @FXML
    private Label lbOrder_code;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
   
    }    
    
}
