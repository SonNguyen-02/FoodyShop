/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.OrderHelper;
import com.foodyshop.helper.TopicHelper;
import com.foodyshop.main.Navigator;
import com.foodyshop.model.OrderModel;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author APlaptop
 */
public class EditOrderController implements Initializable {

    private OrderModel mOrder;
    private Stage stage;

    @FXML
    private ComboBox<String> cbStatus;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    private Label lbOrderCode;

    private TextField txtShipPrice;

    private IOnUpdateOrderSuccess mIOnUpdateOrderSuccess;

    public interface IOnUpdateOrderSuccess {

        void callback();
    }

    public void setData(OrderModel order, Stage stage, IOnUpdateOrderSuccess mIOnUpdateOrderSuccess) {
        this.mOrder = order;
        this.stage = stage;
        this.mIOnUpdateOrderSuccess = mIOnUpdateOrderSuccess;
        lbOrderCode.setText(order.getOrderCode());
//        System.out.println(order.getOrderCode());
//        System.out.println(order.getShipPrice());
        
        txtShipPrice.setText(order.getShipPrice().toString());
        cbStatus.setItems(FXCollections.observableArrayList(OrderModel.WAIT_AOS_CF, OrderModel.AOS_CF, OrderModel.AOS_CL, OrderModel.WAIT_CUS_CF, OrderModel.CUS_CL, OrderModel.CUS_CF, OrderModel.SHIPPING, OrderModel.SUCCESS_DELIVERY));
        cbStatus.setValue(order.getStatusVal().get());
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btnCancel.setOnMouseClicked(this::onClickCancel);
        btnSave.setOnMouseClicked(this::onclickSave);
        System.out.println("123");
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

    private void onclickSave(MouseEvent e) {
        String shipPrice = txtShipPrice.getText().trim();
        String regaxShipPrice = "^[0-9]{1,11}$";
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        if (shipPrice.isEmpty()) {
            txtShipPrice.requestFocus();
            alert.setHeaderText("Please enter ship price");
            alert.show();
            return;
        }
        if(!shipPrice.matches(regaxShipPrice)){
            alert.setHeaderText("Please enter number");
            alert.show();
            return;
        }
        mOrder.setShipPrice(Integer.parseInt(shipPrice));
        mOrder.setStatusVal(cbStatus.getValue());
        boolean resutl = OrderHelper.updateOrder(mOrder);
        if (resutl) {
            Alert alerts = new Alert(Alert.AlertType.INFORMATION);
            alerts.setTitle("Success");
            alerts.setHeaderText("Edit success!");
            alerts.show();
            mIOnUpdateOrderSuccess.callback();
            Navigator.getInstance().getModalStage().close();
        } else {
            Alert alerts = new Alert(Alert.AlertType.ERROR);
            alerts.setTitle("Error");
            alerts.setHeaderText("Edit false");
            alerts.show();
        }
    }
}
