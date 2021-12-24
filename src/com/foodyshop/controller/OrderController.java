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
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
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
    private TableColumn<OrderModel, String> tcPhone;

    @FXML
    private TableColumn<OrderModel, Integer> tcShip_price;

    @FXML
    private TableColumn<OrderModel, Integer> tcTotal_money;

    @FXML
    private TableColumn<OrderModel, String> tcCreated;

    @FXML
    private TableColumn<OrderModel, String> tcStatus;

    @FXML
    private Button btnOrder_detail, btnReceive, btnEditShipPrice, btnCancel, btnShipping, btnDelivery;

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

        btnOrder_detail.setOnMouseClicked(this::onclickShowOrderDetail);

        tcId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper((tblOrder.getItems().indexOf(cellData.getValue()) + 1) + ""));
        tcOrder_code.setCellValueFactory(cellValue -> cellValue.getValue().getOrderCodeProperty());
        tcName.setCellValueFactory(cellValue -> cellValue.getValue().getNameProperty());
        tcPhone.setCellValueFactory(cellValue -> cellValue.getValue().getPhoneProperty());
        tcShip_price.setCellValueFactory(cellValue -> cellValue.getValue().getShipPriceProperty());
        tcTotal_money.setCellValueFactory(cellValue -> cellValue.getValue().getTotalMoneyProperty());
        tcCreated.setCellValueFactory(cellValue -> cellValue.getValue().getCreatedProperty());
        tcStatus.setCellValueFactory(cellValue -> cellValue.getValue().getStatusVal());
        listOrder = OrderHelper.getAllOrder();
        tblOrder.setItems(listOrder);

        FilteredList<OrderModel> filteredData = new FilteredList<>(listOrder, b -> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(OrderModel -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();
                if (OrderModel.getName().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (OrderModel.getOrderCode().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (OrderModel.getAddress().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (OrderModel.getPhone().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (OrderModel.getTotalMoney().toString().contains(searchKeyword)) {
                    return true;
                } else if (OrderModel.getStatusVal().get().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<OrderModel> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(tblOrder.comparatorProperty());

        tblOrder.setItems(sortedData);

        tblOrder.setRowFactory(v -> {
            final TableRow<OrderModel> row = new TableRow<>();
            row.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                final int index = row.getIndex();
                if (e.getButton().equals(MouseButton.PRIMARY) && index >= 0 && index < tblOrder.getItems().size()) {
                    initHBoxBar(tblOrder.getSelectionModel().getSelectedItem().getStatus());
                } else {
                    showButtons();
                }
            });
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
                final int index = row.getIndex();
                if (index < 0 || index >= tblOrder.getItems().size()) {
                    tblOrder.getSelectionModel().clearSelection();
                    event.consume();
                }
            });
            return row;
        });

        Arrays.asList(btnReceive, btnEditShipPrice, btnCancel, btnShipping, btnDelivery).forEach(btn -> btn.managedProperty().bind(btn.visibleProperty()));
        showButtons();
        btnReceive.setOnMouseClicked(event -> {
            OrderModel order = tblOrder.getSelectionModel().getSelectedItem();
            showAlert(Alert.AlertType.CONFIRMATION, "Confirmation",
                    "Accept order " + order.getOrderCode() + " ?", t -> {
                order.setStatus(1);
                OrderHelper.updateStatusOrder(order);
                initHBoxBar(order.getStatus());
            });
        });
        btnEditShipPrice.setOnMouseClicked(e -> {
            OrderModel order = tblOrder.getSelectionModel().getSelectedItem();
            Navigator.getInstance().showEditOrder(order, () -> initHBoxBar(order.getStatus()));
        });
        btnCancel.setOnMouseClicked(e -> {
            OrderModel order = tblOrder.getSelectionModel().getSelectedItem();
            showAlert(Alert.AlertType.CONFIRMATION, "Confirmation",
                    "Cancel order " + order.getOrderCode() + " ?", t -> {
                order.setStatus(-1);
                OrderHelper.updateStatusOrder(order);
                initHBoxBar(order.getStatus());
            });
        });
        btnShipping.setOnMouseClicked(e -> {
            OrderModel order = tblOrder.getSelectionModel().getSelectedItem();
            showAlert(Alert.AlertType.CONFIRMATION, "Confirmation",
                    "Confirm shipping order " + order.getOrderCode() + " ?", t -> {
                order.setStatus(4);
                OrderHelper.updateStatusOrder(order);
                initHBoxBar(order.getStatus());
            });
        });
        btnDelivery.setOnMouseClicked(e -> {
            OrderModel order = tblOrder.getSelectionModel().getSelectedItem();
            showAlert(Alert.AlertType.CONFIRMATION, "Confirmation",
                    "Confirm successful delivery order " + order.getOrderCode() + " ?", t -> {
                order.setStatus(5);
                OrderHelper.updateStatusOrder(order);
                initHBoxBar(order.getStatus());
            });
        });

    }

    private void showAlert(Alert.AlertType type, String title, String header, Consumer<ButtonType> cb) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait().filter(b -> b == ButtonType.OK).ifPresent(cb);
    }

    private void initHBoxBar(int status) {
        switch (status) {
            case 0:
                showButtons(btnReceive, btnCancel);
                break;
            case 1:
                showButtons(btnEditShipPrice, btnCancel);
                break;
            case 3:
                showButtons(btnShipping);
                break;
            case 4:
                showButtons(btnDelivery);
                break;
            case -1:
            case -2:
            case 2:
            case 5:
            default:
                showButtons();
                break;
        }
    }

    private void showButtons(Button... btns) {
        List<Button> listBtnShow = Arrays.asList(btns);
        List<Button> listBtnDefault = Arrays.asList(btnReceive, btnEditShipPrice, btnCancel, btnShipping, btnDelivery);
        listBtnDefault.forEach(btn -> btn.setVisible(listBtnShow.contains(btn)));
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

}
