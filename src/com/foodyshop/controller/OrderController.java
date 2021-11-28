/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.OrderHelper;
import com.foodyshop.main.Navigator;
import com.foodyshop.model.OrderModel;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author N.C.Son
 */
public class OrderController implements Initializable {
    @FXML
    private TableView<OrderModel> tblOrder;
    
    @FXML
    private TableColumn<OrderModel, Integer> tcId;

    @FXML
    private TableColumn<OrderModel, String> tcOrder_code;

    @FXML
    private TableColumn<OrderModel, String> tcName;

    @FXML
    private TableColumn<OrderModel, String> tcAddress;

    @FXML
    private TableColumn<OrderModel, String> tcPhone;

    @FXML
    private TableColumn<OrderModel, String> tcNote;

    @FXML
    private TableColumn<OrderModel, Integer> tcShip_price;

    @FXML
    private TableColumn<OrderModel, Integer> tcTotal_money;

    @FXML
    private TableColumn<OrderModel, Integer> tcStatus;
    
    @FXML
    Button btnAdd;
    
    ObservableList<OrderModel> listOrder = FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        btnAdd.setOnMouseClicked(e -> Navigator.getInstance().showAddOrder());
        
        tcId.setCellValueFactory(cellValue -> cellValue.getValue().getIdProperty());
        tcOrder_code.setCellValueFactory(cellValue -> cellValue.getValue().getOrder_codeProperty());
        tcName.setCellValueFactory(cellValue -> cellValue.getValue().getNameProperty());
        tcAddress.setCellValueFactory(cellValue -> cellValue.getValue().getAddressProperty());
        tcPhone.setCellValueFactory(cellValue -> cellValue.getValue().getPhoneProperty());
        tcNote.setCellValueFactory(cellValue -> cellValue.getValue().getNoteProperty());
        tcShip_price.setCellValueFactory(cellValue -> cellValue.getValue().getShip_priceProperty());
        tcTotal_money.setCellValueFactory(cellValue -> cellValue.getValue().getTotal_moneyProperty());
        tcStatus.setCellValueFactory(cellValue -> cellValue.getValue().getStatusProperty());
        
        listOrder = OrderHelper.getAllOrder();
        tblOrder.setItems(listOrder);
    }      
}
