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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    private TableColumn<OrderModel, Integer> tcStatus;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnOrder_detail;

    ObservableList<OrderModel> listOrder = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btnAdd.setOnMouseClicked(e -> Navigator.getInstance().showAddOrder());
        btnOrder_detail.setOnMouseClicked(e -> {
            OrderModel order = tblOrder.getSelectionModel().getSelectedItem();
            if (order != null) {
                Navigator.getInstance().showOrder_Detail(order);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("You Must choose!!!");
                alert.show();
            }
        });
        btnEdit.setOnMouseClicked(e -> {
            OrderModel order = tblOrder.getSelectionModel().getSelectedItem();
            if (order != null) {
                Navigator.getInstance().showEditOrder(order);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("You Must choose!!!");
                alert.show();
            }
        });
        btnDelete.setOnMouseClicked(e -> {
            OrderModel order = tblOrder.getSelectionModel().getSelectedItem();
            if (order != null) {
                if(order.getStatus() < 0){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Delete");
                    alert.setHeaderText("Are you sure?");

                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.get() == ButtonType.OK) {
                        listOrder.remove(order);
                        OrderHelper.deleteOrder(order.getId());
                    } else if (option.get() == ButtonType.CANCEL) {
                        Navigator.getInstance().getModalStage().close();
                    }
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("You must choose a order has status > 0!");
                    alert.setTitle("Error");
                    alert.showAndWait();
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("You must choose!");
                alert.setTitle("Error");
                alert.showAndWait();
            }
        });

        tcId.setCellValueFactory(cellValue -> cellValue.getValue().getIdProperty());
        tcOrder_code.setCellValueFactory(cellValue -> cellValue.getValue().getOrderCodeProperty());
        tcName.setCellValueFactory(cellValue -> cellValue.getValue().getNameProperty());
        tcAddress.setCellValueFactory(cellValue -> cellValue.getValue().getAddressProperty());
        tcPhone.setCellValueFactory(cellValue -> cellValue.getValue().getPhoneProperty());
        tcNote.setCellValueFactory(cellValue -> cellValue.getValue().getNoteProperty());
        tcShip_price.setCellValueFactory(cellValue -> cellValue.getValue().getShipPriceProperty());
        tcTotal_money.setCellValueFactory(cellValue -> cellValue.getValue().getTotalMoneyProperty());
        tcCreated.setCellValueFactory(cellValue -> cellValue.getValue().getCreatedProperty());
        tcStatus.setCellValueFactory(cellValue -> cellValue.getValue().getStatusProperty());

        listOrder = OrderHelper.getAllOrder();
        tblOrder.setItems(listOrder);
    }

//    OrderModel order = tblOrder.getSelectionModel().getSelectedItem();
//    private void onclickDelete(MouseEvent e){
//        if(order.getStatus()< 0 && order!=null){
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//            alert.setTitle("Delete Account");
//            alert.setHeaderText("Are you sure want to move this account");
//
//            Optional<ButtonType> option = alert.showAndWait();
//
//            if (option.get() == ButtonType.OK) {
//                listOrder.remove(order);
//                OrderHelper.deleteOrder(order.getId());
//            } else if (option.get() == ButtonType.CANCEL) {
//                Navigator.getInstance().getModalStage().close();
//            }
//        } else {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setHeaderText("You must choose a order !");
//            alert.setTitle("Error");
//            alert.showAndWait();
//        }
//    }
}
