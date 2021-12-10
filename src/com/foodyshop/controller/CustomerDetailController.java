/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.CustomerDetailHelper;
import com.foodyshop.model.CustomerDetailModel;
import com.foodyshop.model.CustomerModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author APlaptop
 */
public class CustomerDetailController implements Initializable {

    @FXML
    private TableView<CustomerDetailModel> tblCustomerDetail;

    @FXML
    private TableColumn<CustomerDetailModel, Integer> tcStt;

    @FXML
    private TableColumn<CustomerDetailModel, String> tcOrderCode;

    @FXML
    private TableColumn<CustomerDetailModel, Integer> tcTotalPrice;

    @FXML
    private TableColumn<CustomerDetailModel, String> tcCreated;

    @FXML
    private TableColumn<CustomerDetailModel, String> tcStatusOrder;

    @FXML
    private Label lbName;

    @FXML
    private Label lbNote;

    @FXML
    private Label lbAddress;

    @FXML
    private Label lbPhone;

    @FXML
    private Label lbTotalOrder;

    @FXML
    private Label lbTotalPrice;

    private CustomerModel mCustomer;

    ObservableList<CustomerDetailModel> listCustomerDetail = FXCollections.observableArrayList();

    public void initCustomerModel(CustomerModel customer) {
        mCustomer = customer;
        lbName.setText(customer.getName());    
        lbName.setText(customer.getName());
        lbAddress.setText(customer.getAddress());
        lbPhone.setText(customer.getPhone());
//        lbNote.setText(customer.getNote());
        lbTotalOrder.setText(String.valueOf(listCustomerDetail.size())+"ord");
        
        listCustomerDetail = CustomerDetailHelper.getAllCustomerDetail(customer);
        tblCustomerDetail.setItems(listCustomerDetail);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        tcStt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper((tblCustomerDetail.getItems().indexOf(cellData.getValue()) + 1) + ""));
        tcOrderCode.setCellValueFactory(cellValue -> cellValue.getValue().getOrderCode());
        tcTotalPrice.setCellValueFactory(cellValue -> cellValue.getValue().getTotalMoney());
        tcCreated.setCellValueFactory(cellValue -> cellValue.getValue().getCreated());
        tcStatusOrder.setCellValueFactory(cellValue -> cellValue.getValue().getStatusVal());
    }
}
