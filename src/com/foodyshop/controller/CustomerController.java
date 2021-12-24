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
import com.foodyshop.main.CurrentAccount;
import java.util.Optional;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author APlaptop
 */
public class CustomerController implements Initializable {

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
    private TextField txtSearch;

    @FXML
    private Button btnCustomerDetail;

    @FXML
    private Button btnLock, btnUnclock;

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
        if (CurrentAccount.getInstance().isStaff()) {
            btnUnclock.setVisible(false);
            btnLock.setVisible(false);
        } else {
            btnUnclock.setOnMouseClicked(this::onClickUnClock);
            btnLock.setOnMouseClicked(this::onClickLock);
        }
        btnCustomerDetail.setOnMouseClicked(this::onclickShowCustomerDetail);

        tcStt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper((tblCustomer.getItems().indexOf(cellData.getValue()) + 1) + ""));
        tcPhone.setCellValueFactory(cellValue -> cellValue.getValue().getPhoneProperty());
        tcName.setCellValueFactory(cellValue -> cellValue.getValue().getNameProperty());
        tcGender.setCellValueFactory(cellValue -> cellValue.getValue().getGenderVal());
        tcDatebirth.setCellValueFactory(cellValue -> cellValue.getValue().getDatebirthProperty());
        tcAddress.setCellValueFactory(cellValue -> cellValue.getValue().getAddressProperty());
        tcCreated.setCellValueFactory(cellValue -> cellValue.getValue().getCreatedProperty());
        tcImage.setCellValueFactory(cellValue -> cellValue.getValue().getImgView());
        tcStatus.setCellValueFactory(cellValue -> cellValue.getValue().getStatusVal());

        listCustomer = CustomerHelper.getAllCustomer();
        tblCustomer.setItems(listCustomer);

        FilteredList<CustomerModel> filteredData = new FilteredList<>(listCustomer, b -> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(CustomerModel -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if (CustomerModel.getName().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (CustomerModel.getPhone().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<CustomerModel> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(tblCustomer.comparatorProperty());

        tblCustomer.setItems(sortedData);
    }

    void onClickUnClock(MouseEvent e) {
        CustomerModel customer = tblCustomer.getSelectionModel().getSelectedItem();
        if (customer == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please select one!");
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setHeaderText("Are you sure to Unclock Customer ?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                customer.setStatusVal(CustomerModel.UNLOCK);
                if (updStatus(customer)) {
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Update success!");
                    alert.show();
                } else {
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Update false!");
                    alert.show();
                }
            } else {
                System.out.println("Click Cancel");
            }

        }
    }

    private void onClickLock(MouseEvent e) {
        CustomerModel customer = tblCustomer.getSelectionModel().getSelectedItem();
        if (customer == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please select one!");
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("confirm");
            alert.setHeaderText("Are you sure to Lock Customer ?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                customer.setStatusVal(CustomerModel.LOCK);
                if (updStatus(customer)) {
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Update success!");
                    alert.show();
                } else {
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Update false!");
                    alert.show();
                }
            } else {
                System.out.println("Click Cancel");
            }
        }
    }

    private boolean updStatus(CustomerModel customerModel) {
        return CustomerHelper.updateCustomer(customerModel);
    }

    private void onclickShowCustomerDetail(MouseEvent e) {
        CustomerModel customer = tblCustomer.getSelectionModel().getSelectedItem();
        if (customer != null) {
            Navigator.getInstance().showCustomerDetail(customer);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("You Must choose!!!");
            alert.show();
        }
    }
}
