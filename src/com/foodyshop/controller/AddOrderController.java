/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.main.Navigator;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author N.C.Son
 */
public class AddOrderController implements Initializable {

    @FXML
    private Button btnSave, btnCancel;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btnSave.setOnMouseClicked(this::onClickSave);
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
    
    private void onClickCancel(MouseEvent e){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close");
        alert.setHeaderText("Do you want close?");
        alert.showAndWait().ifPresent(btnType -> {
            if(btnType == ButtonType.OK){
                Navigator.getInstance().getModalStage().close();
            }
        });
        
    }
}
