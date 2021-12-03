/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.Order_DetailHelper;
import com.foodyshop.model.OrderModel;
import com.foodyshop.model.Order_DetailModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.shape.Rectangle;

/**
 * FXML Controller class
 *
 * @author APlaptop
 */
public class Order_DetailController implements Initializable {
    
    private OrderModel mOrder;
    
    @FXML
    private Label lbOrder_Code;

    @FXML
    private TableView<Order_DetailModel> tblOrder_detail;

    @FXML
    private TableColumn<Order_DetailModel, Integer> tcStt;

    @FXML
    private TableColumn<Order_DetailModel, String> tcProduct;

    @FXML
    private TableColumn<Order_DetailModel, Integer> tcNumber;

    @FXML
    private TableColumn<Order_DetailModel, Integer> tcPrice;

    @FXML
    private TableColumn<Order_DetailModel, Integer> tcDiscount;
    /**
     * Initializes the controller class.
     */
    
    ObservableList<Order_DetailModel> listOrder_Detail =  FXCollections.observableArrayList();
    
    public void initOrderModel(OrderModel order){
        this.mOrder = order;
        order.getOrderCode();
        lbOrder_Code.setText(order.getOrderCode().toString());
        listOrder_Detail = Order_DetailHelper.getAllOrder_Detail(order);
        tblOrder_detail.setItems(listOrder_Detail);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        tcStt.setCellValueFactory(cellValue -> cellValue.getValue().getIdProperty());
        tcProduct.setCellValueFactory(cellValue -> cellValue.getValue().getProduct_nameProperty());
        tcNumber.setCellValueFactory(cellValue -> cellValue.getValue().getNumberProperty());
        tcPrice.setCellValueFactory(cellValue -> cellValue.getValue().getPriceProperty());
        tcDiscount.setCellValueFactory(cellValue -> cellValue.getValue().getDiscountProperty());
    }       
}
