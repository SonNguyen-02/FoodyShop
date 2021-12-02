/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.Order_DetailHelper;
import com.foodyshop.main.Navigator;
import com.foodyshop.model.OrderModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author APlaptop
 */
public class EditOrderController implements Initializable {

    private OrderModel mOrder;
    
    @FXML
    private GridPane EditOrderForm;

    @FXML
    private Label lbOrder_Code;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    @FXML
    private ChoiceBox<String> cbStatus;

    @FXML
    private TextField txtPrice;

     public void initOrderModel(OrderModel order){
        this.mOrder = order;
        order.getOrderCode();
        lbOrder_Code.setText(order.getOrderCode().toString());
        txtPrice.setText(order.getShipPrice().toString());
        cbStatus.getItems().add(order.getStatus().toString());
    }
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnCancel.setOnMouseClicked(this::onClickCancel);
        
    }

    private void onClickSave(MouseEvent e) {
        if (true) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Đã thêm order");
            alert.show();
            Navigator.getInstance().getModalStage().close();
        }
    }

    private void onClickCancel(MouseEvent e) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close");
        alert.setHeaderText("Do you want close?");
        alert.showAndWait().ifPresent(btnType -> {
            if (btnType == ButtonType.OK) {
                Navigator.getInstance().getModalStage().close();
            }
        });

    }
}
