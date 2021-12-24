/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.CustomerHelper;
import com.foodyshop.helper.FeedbackHelper;
import com.foodyshop.helper.OrderHelper;
import com.foodyshop.helper.SaleHelper;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

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

    @FXML
    private Button btnSearch;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        dpStart.setValue(LocalDate.now());
        dpEnd.setValue(LocalDate.now());
        btnSearch.setOnMouseClicked(this::onClickSearch);
        initPage();
        

    }

    private void onClickSearch(MouseEvent e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        if (dpStart.getValue() == null) {
            dpStart.requestFocus();
            alert.setHeaderText("Please Select Day!");
            alert.show();
            return;
        }
        if (dpEnd.getValue() == null) {
            dpEnd.requestFocus();
            alert.setHeaderText("Please Select Day!");
            alert.show();
            return;
        }
        if (!dpEnd.getValue().isBefore(dpEnd.getValue())) {
        } else {
            alert.setHeaderText("Date End more than");
            alert.show();
            return;
        }
        initPage();
        
        
    }
    
    private void initPage(){
        int totalDelivery = OrderHelper.getTotalDelivery(dpStart.getValue(), dpEnd.getValue());
        lbTotalDeliver.setText(String.valueOf(totalDelivery));
        
        int SuccessDe = OrderHelper.getSuccessfulDelivery(dpStart.getValue(), dpEnd.getValue());
        lbSuccessDe.setText(String.valueOf(SuccessDe));

        int orderNumber = OrderHelper.getOrderNumber(dpStart.getValue(), dpEnd.getValue());
        lbOrderNumber.setText(String.valueOf(orderNumber));

        int orderCancel = OrderHelper.getOrderCancel(dpStart.getValue(), dpEnd.getValue());
        lbOrderCancel.setText(String.valueOf(orderCancel));

        int onSale = SaleHelper.getOnSale(dpStart.getValue(), dpEnd.getValue());
        lbSaleLeft.setText(String.valueOf(onSale));
        
        int AlmostEnd = SaleHelper.getAlmostEnd();
        lbAlmostEnd.setText(String.valueOf(AlmostEnd));

        int discountEnd = SaleHelper.getHasEnd(dpStart.getValue(), dpEnd.getValue());
        lbSaleEnd.setText(String.valueOf(discountEnd));

        int order = OrderHelper.getOrderNumber(dpStart.getValue(), dpEnd.getValue());
        lbOrder.setText(String.valueOf(order));

        int feedback = FeedbackHelper.getFeedback(dpStart.getValue(), dpEnd.getValue());
        lbFeedback.setText(String.valueOf(feedback));

        int member = CustomerHelper.getMember(dpStart.getValue(), dpEnd.getValue());
        lbMember.setText(String.valueOf(member));
    }
}
