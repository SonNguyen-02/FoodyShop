/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author N.C.Son
 */
public class DashboardController implements Initializable {

    @FXML
    private Label lbTotalDeliver;

    @FXML
    private Label lbSuccessDe;

    @FXML
    private Label lbOrderNumber;

    @FXML
    private Label lbOrderCancel;

    @FXML
    private Label lbOrderWait;

    @FXML
    private Label lbOrderConfirm;

    @FXML
    private Label lbSaleLeft;

    @FXML
    private Label lbAlmostEnd;

    @FXML
    private Label lbSaleEnd;

    @FXML
    private Label lbOrder;

    @FXML
    private Label lbMember;

    @FXML
    private Label lbFeedback;

    @FXML
    private DatePicker dpStart;

    @FXML
    private DatePicker dpEnd;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
