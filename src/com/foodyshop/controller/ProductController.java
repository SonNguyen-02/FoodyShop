/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.ProductHelper;
import com.foodyshop.model.ProductModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
 * @author X PC
 */
public class ProductController implements Initializable {
    @FXML
    private TableView<ProductModel> tblProduct;
    
    @FXML
    private TableColumn<ProductModel, Integer> tcStt;

    @FXML
    private TableColumn<ProductModel, String> tcCateName;

    @FXML
    private TableColumn<ProductModel, String> tcName;

    @FXML
    private TableColumn<ProductModel, String> tcDes;

    @FXML
    private TableColumn<ProductModel, Integer> tcPrice;

    @FXML
    private TableColumn<ProductModel, ?> tcImg;

    @FXML
    private TableColumn<ProductModel, ?> tcImgdetail;

    @FXML
    private TableColumn<ProductModel, String> tcCreated;

    @FXML
    private TableColumn<ProductModel, String> tcStatus;

    @FXML
    private TableColumn<ProductModel, ?> tcLK;

    @FXML
    private Button btnAdd,btnEdit,btnDelete;

    ObservableList<ProductModel> listProduct = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tcStt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper((tblProduct.getItems().indexOf(cellData.getValue()) + 1) + ""));
        tcCateName.setCellValueFactory(cellValue -> cellValue.getValue().getCategoryNameProperty());
        tcName.setCellValueFactory(cellValue -> cellValue.getValue().getNameProperty());
        tcDes.setCellValueFactory(cellValue -> cellValue.getValue().getDescriptionProperty());
        tcPrice.setCellValueFactory(cellValue -> cellValue.getValue().getPriceProperty());
        tcCreated.setCellValueFactory(cellValue -> cellValue.getValue().getCreatedProperty());
        tcStatus.setCellValueFactory(cellValue -> cellValue.getValue().getStatusVal());
        listProduct = ProductHelper.getAllCategory();
        tblProduct.setItems(listProduct);
    }    
    
}
