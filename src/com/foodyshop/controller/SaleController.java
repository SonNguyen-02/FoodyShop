/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.SaleHelper;
import com.foodyshop.model.SaleModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author APlaptop
 */
public class SaleController implements Initializable {
    
    @FXML
    private TableView<SaleModel> tblSale;

    @FXML
    private TableColumn<SaleModel, Integer> tcStt;

    @FXML
    private TableColumn<SaleModel, String> tcProductName;

    @FXML
    private TableColumn<SaleModel, Integer> tcDiscount;

    @FXML
    private TableColumn<SaleModel, String> tcContent;

    @FXML
    private TableColumn<SaleModel, String> tcImg;

    @FXML
    private TableColumn<SaleModel, String> tcStart;

    @FXML
    private TableColumn<SaleModel, String> tcEnd;

    @FXML
    private TableColumn<SaleModel, String> tcCreated;

    @FXML
    private TableColumn<SaleModel, Integer> tcStatus;
  
    ObservableList<SaleModel> listSale = FXCollections.observableArrayList();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        tcStt.setCellValueFactory(cellValue -> cellValue.getValue().getIdProperty());
        tcProductName.setCellValueFactory(cellValue -> cellValue.getValue().getProductNameProperty());
        tcDiscount.setCellValueFactory(cellValue -> cellValue.getValue().getDiscountProperty());
        tcContent.setCellValueFactory(cellValue -> cellValue.getValue().getContentProperty());
        tcStart.setCellValueFactory(cellValue -> cellValue.getValue().getStart_dateProperty());
        tcEnd.setCellValueFactory(cellValue -> cellValue.getValue().getEnd_dateProperty());
        tcCreated.setCellValueFactory(cellValue -> cellValue.getValue().getCreatedProperty());
        tcStatus.setCellValueFactory(cellValue -> cellValue.getValue().getStatusProperty());
        
        listSale = SaleHelper.getAllSale();
        tblSale.setItems(listSale);
    }        
}
