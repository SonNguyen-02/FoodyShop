/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.CustomerHelper;
import com.foodyshop.main.Navigator;
import com.foodyshop.model.CustomerModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author APlaptop
 */
public class CustomerController implements Initializable {

    @FXML
    private TableView<CustomerModel> tblCustomer;

    @FXML
    private TableColumn<CustomerModel, Integer> ctStt;

    @FXML
    private TableColumn<CustomerModel, String> ctPhone;

    @FXML
    private TableColumn<CustomerModel, String> ctName;

    @FXML
    private TableColumn<CustomerModel, Integer> ctGender;

    @FXML
    private TableColumn<CustomerModel, String> ctDatebirth;

    @FXML
    private TableColumn<CustomerModel, String> ctAddress;

    @FXML
    private TableColumn<CustomerModel, String> ctCreated;

    @FXML
    private TableColumn<CustomerModel, ImageView> ctImage;

    @FXML
    private TableColumn<CustomerModel, Integer> ctStatus;

    ObservableList<CustomerModel> listCustomer = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ctStt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper((tblCustomer.getItems().indexOf(cellData.getValue()) + 1) + ""));
        ctPhone.setCellValueFactory(cellValue -> cellValue.getValue().getPhoneProperty());
        ctName.setCellValueFactory(cellValue -> cellValue.getValue().getNameProperty());
        ctGender.setCellValueFactory(cellValue -> cellValue.getValue().getGenderProperty()); 
        ctDatebirth.setCellValueFactory(cellValue -> cellValue.getValue().getDatebirthProperty());
        ctAddress.setCellValueFactory(cellValue -> cellValue.getValue().getAddressProperty()); 
        ctImage.setCellValueFactory(cellValue -> cellValue.getValue().getImgView());
        ctCreated.setCellValueFactory(cellValue -> cellValue.getValue().getCreatedProperty());
        ctStatus.setCellValueFactory(cellValue -> cellValue.getValue().getStatusProperty());

        listCustomer = CustomerHelper.getAllCustomer();
        tblCustomer.setItems(listCustomer);
    }
}
