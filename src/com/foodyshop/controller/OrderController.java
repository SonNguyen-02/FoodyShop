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
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

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
    private TableColumn<OrderModel, String> tcCreated;

    @FXML
    private TableColumn<OrderModel, String> tcStatus;

    @FXML
    private Button btnEditStatus;

    @FXML
    private Button btnEditPrice;

    @FXML
    private Button btnOrder_detail;

    @FXML
    private TextField txtSearch;

    ObservableList<OrderModel> listOrder = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnEditStatus.setOnMouseClicked(this::onclickShowEditOrder);
        btnOrder_detail.setOnMouseClicked(this::onclickShowOrderDetail);

        tcId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper((tblOrder.getItems().indexOf(cellData.getValue()) + 1) + ""));
        tcOrder_code.setCellValueFactory(cellValue -> cellValue.getValue().getOrderCodeProperty());
        tcName.setCellValueFactory(cellValue -> cellValue.getValue().getNameProperty());
        tcAddress.setCellValueFactory(cellValue -> cellValue.getValue().getAddressProperty());
        tcPhone.setCellValueFactory(cellValue -> cellValue.getValue().getPhoneProperty());
        tcNote.setCellValueFactory(cellValue -> cellValue.getValue().getNoteProperty());
        tcShip_price.setCellValueFactory(cellValue -> cellValue.getValue().getShipPriceProperty());
        tcTotal_money.setCellValueFactory(cellValue -> cellValue.getValue().getTotalMoneyProperty());
        tcCreated.setCellValueFactory(cellValue -> cellValue.getValue().getCreatedProperty());
        tcStatus.setCellValueFactory(cellValue -> cellValue.getValue().getStatusVal());
        listOrder = OrderHelper.getAllOrder();
        tblOrder.setItems(listOrder);

        FilteredList<OrderModel> filteredData = new FilteredList<>(listOrder, b -> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(OrderModel -> {

                if (newValue.isEmpty() || newValue == null) {
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if (OrderModel.getName().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (OrderModel.getName().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (OrderModel.getOrderCode().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (OrderModel.getAddress().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (OrderModel.getPhone().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (OrderModel.getNote().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (OrderModel.getTotalMoney().toString().indexOf(searchKeyword) > -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<OrderModel> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(tblOrder.comparatorProperty());

        tblOrder.setItems(sortedData);

    }

    private void onclickShowOrderDetail(MouseEvent e) {
        OrderModel order = tblOrder.getSelectionModel().getSelectedItem();
        if (order != null) {
            Navigator.getInstance().showOrder_Detail(order);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("You Must choose!!!");
            alert.show();
        }
    }

    private void onclickShowEditOrder(MouseEvent e) {
        OrderModel order = tblOrder.getSelectionModel().getSelectedItem();
        if (order != null) {
            Navigator.getInstance().showEditOrder(order, new EditOrderController.IOnUpdateOrderSuccess() {
                @Override
                public void callback() {
                    tblOrder.refresh();
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
