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
import com.foodyshop.controller.EditCustomerController;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author APlaptop
 */
public class CustomerController implements Initializable {

    private CustomerModel mCustomer;

    @FXML
    private TableView<CustomerModel> tblCustomer;

    @FXML
    private TableColumn<CustomerModel, Integer> tcStt;

    @FXML
    private TableColumn<CustomerModel, String> tcPhone;

    @FXML
    private TableColumn<CustomerModel, String> tcName;

    @FXML
    private TableColumn<CustomerModel, String> tcGender;

    @FXML
    private TableColumn<CustomerModel, String> tcDatebirth;

    @FXML
    private TableColumn<CustomerModel, String> tcAddress;

    @FXML
    private TableColumn<CustomerModel, String> tcCreated;

    @FXML
    private TableColumn<CustomerModel, ImageView> tcImage;

    @FXML
    private TableColumn<CustomerModel, String> tcStatus;

    @FXML
    private Button btnCustomerDetail;

    @FXML
    private Button btnEditStatus;

    public void setData(CustomerModel customer) {
        mCustomer = customer;
    }

    
    ObservableList<CustomerModel> listCustomer = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btnEditStatus.setOnMouseClicked(this::onclickShowEditCustomer);

        tcStt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper((tblCustomer.getItems().indexOf(cellData.getValue()) + 1) + ""));
        tcPhone.setCellValueFactory(cellValue -> cellValue.getValue().getPhoneProperty());
        tcName.setCellValueFactory(cellValue -> cellValue.getValue().getNameProperty());
        tcGender.setCellValueFactory(cellValue -> cellValue.getValue().getGenderVal());
        tcDatebirth.setCellValueFactory(cellValue -> cellValue.getValue().getDatebirthProperty());
        tcAddress.setCellValueFactory(cellValue -> cellValue.getValue().getAddressProperty());
        tcImage.setCellValueFactory(cellValue -> cellValue.getValue().getImgView());
        tcCreated.setCellValueFactory(cellValue -> cellValue.getValue().getCreatedProperty());
        tcStatus.setCellValueFactory(cellValue -> cellValue.getValue().getStatusVal());

        listCustomer = CustomerHelper.getAllCustomer();
        tblCustomer.setItems(listCustomer);
    }

    private void onclickShowEditCustomer(MouseEvent e) {
        CustomerModel customer = tblCustomer.getSelectionModel().getSelectedItem();
        if (customer != null) {
            Navigator.getInstance().showEditCustomerForm(customer, new EditCustomerController.IOnUpdateCustomer() {
                @Override
                public void callback() {
                   tblCustomer.refresh();
                }
            });
                
                
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("You Must choose!!!");
            alert.show();
        }
    }
}
