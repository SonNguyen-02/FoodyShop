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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author APlaptop
 */
public class EditOrderController implements Initializable {

    private OrderModel mOrder;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    @FXML
    private Label lbOrderCode;

    @FXML
    private TextField txtShipPrice;
    
    private IOnUpdateOrderSuccess mIOnUpdateOrderSuccess;
    
    public interface IOnUpdateOrderSuccess {
        void callback();
    }

    public void setData(OrderModel order, IOnUpdateOrderSuccess mIOnUpdateOrderSuccess) {
        mOrder = order;
        this.mIOnUpdateOrderSuccess = mIOnUpdateOrderSuccess;
        lbOrderCode.setText(order.getOrderCode());
        txtShipPrice.requestFocus();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btnCancel.setOnMouseClicked(this::onClickCancel);
        btnSave.setOnMouseClicked(this::onclickSave);
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
        String regexShipPrice = "^[0-9]{0,10}$";
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        if (shipPrice.isEmpty()) {
            txtShipPrice.requestFocus();
            alert.setHeaderText("Please enter ship price");
            alert.show();
            return;
        }
        if (!shipPrice.matches(regexShipPrice)) {
            txtShipPrice.requestFocus();
            alert.setHeaderText("Ship price is invalid!");
            alert.show();
            return;
        }
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Confirm the shipping price is " + Integer.parseInt(shipPrice) + " ?");
        alert.showAndWait().filter(b -> b == ButtonType.OK).ifPresent(t -> {
            mOrder.setStatus(2);
            mOrder.setShipPrice(Integer.parseInt(shipPrice));
            boolean result = OrderHelper.updateShipAndStatusOrder(mOrder);
            if (result) {
                Navigator.getInstance().getModalStage().close();
                mIOnUpdateOrderSuccess.callback();
                Alert alerts = new Alert(Alert.AlertType.INFORMATION);
                alerts.setTitle("Success");
                alerts.setHeaderText("Edit success!");
                alerts.show();
            } else {
                Alert alerts = new Alert(Alert.AlertType.ERROR);
                alerts.setTitle("Error");
                alerts.setHeaderText("Edit false");
                alerts.show();
            }
        });

    }
}
