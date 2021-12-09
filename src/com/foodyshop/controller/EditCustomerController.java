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
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author APlaptop
 */
public class EditCustomerController implements Initializable {

    private CustomerModel mCustomer;

    @FXML
    private Label lbCustomerName;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    @FXML
    private ComboBox<String> cbStatus;

    private IOnUpdateCustomer mIOnUpdateCustomer;

    private Stage stage;
    
    public interface IOnUpdateCustomer {

        void callback();
    }

    public void initData(CustomerModel customer,Stage stage,IOnUpdateCustomer mIOnUpdateCustomer) {
        mCustomer = customer;
        this.stage = stage;
        this.mIOnUpdateCustomer = mIOnUpdateCustomer;
        lbCustomerName.setText(customer.getName());
        cbStatus.setItems(FXCollections.observableArrayList(CustomerModel.LOCK, CustomerModel.UNLOCK));
        cbStatus.setValue(customer.getStatusVal().get());
    }

    /**
     * Initializes the controller class.
     */
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
        mCustomer.setStatusVal(cbStatus.getValue());
        boolean result = CustomerHelper.updateCustomer(mCustomer);
        if (result) {
            Alert alerts = new Alert(Alert.AlertType.INFORMATION);
            alerts.setTitle("Success");
            alerts.setHeaderText("Edit success!");
            alerts.show();
            mIOnUpdateCustomer.callback();
            Navigator.getInstance().getModalStage().close();
        } else {
            Alert alerts = new Alert(Alert.AlertType.ERROR);
            alerts.setTitle("Error");
            alerts.setHeaderText("Edit false");
            alerts.show();
        }
    }

}
