/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.FeedbackHelper;
import com.foodyshop.model.FeedbackModel;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 *
 * @author DELL
 */
public class FeedbackController implements Initializable {

    @FXML
    private TableView<FeedbackModel> tvFeedback;

    @FXML
    private TableColumn<FeedbackModel, Integer> tcID;

    @FXML
    private TableColumn<FeedbackModel, String> tcCustomerID;

    @FXML
    private TableColumn<FeedbackModel, String> tcProductID;
    
//    @FXML
//    private TableColumn<FeedbackModel, Integer> tcOrderDetailID;

    @FXML
    private TableColumn<FeedbackModel, String> tcContent;
    
     @FXML
    private TableColumn<FeedbackModel, String> tcCreated;

    @FXML
    private TableColumn<FeedbackModel, String> tcUpdated;

    @FXML
    private TableColumn<FeedbackModel, String> tcStatus;

//    @FXML
//    private Button btnShow;
//
//    @FXML
//    private Button btnHidden;

    ObservableList<FeedbackModel> listFeedback;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tcID.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper((tvFeedback.getItems().indexOf(cellData.getValue()) + 1) + ""));
        tcCustomerID.setCellValueFactory(cellData -> cellData.getValue().getCustomerNameProperty());
        tcProductID.setCellValueFactory(cellData -> cellData.getValue().getProductNameProperty());
//        tcOrderDetailID.setCellValueFactory(cellData -> cellData.getValue().getOrderDetailIDproperty());
        tcContent.setCellValueFactory(cellData -> cellData.getValue().getContentProperty());
        tcCreated.setCellValueFactory(cellData -> cellData.getValue().getCreatedProperty());
        tcUpdated.setCellValueFactory(cellData -> cellData.getValue().getUpdatedProperty());
        tcStatus.setCellValueFactory(cellData -> cellData.getValue().getStatusProperty());

        try {
            listFeedback = FeedbackHelper.showAllFeedback();
            tvFeedback.setItems(listFeedback);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void onClickHidden(ActionEvent event) {
        FeedbackModel fbmodel = tvFeedback.getSelectionModel().getSelectedItem();
        if (fbmodel == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No selected data");
            alert.setHeaderText("Select a feedback before do this action!");
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("confirm");
            alert.setHeaderText("Are you sure to hidden this feedback?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.out.println("Click Ok");
                fbmodel.setStatusVal(FeedbackModel.STATUS_HIDDEN);
                if(updStatus(fbmodel)){
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Update success!");
                    alert.show();
                }else{
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Update false!");
                    alert.show();
                }
            } else {
                System.out.println("Click Cancel");
            }
        }
    }

    @FXML
    void onClickShow(ActionEvent event) {
        FeedbackModel fbmodel = tvFeedback.getSelectionModel().getSelectedItem();
        if (fbmodel == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No selected data");
            alert.setHeaderText("Select a feedback before do this action!");
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setHeaderText("Are you sure to show this feedback?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.out.println("Click Ok");
                fbmodel.setStatusVal(FeedbackModel.STATUS_SHOW);
                if(updStatus(fbmodel)){
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Update success!");
                    alert.show();
                }else{
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Update false!");
                    alert.show();
                }
            } else {
                System.out.println("Click Cancel");
            }
        }
    }
    
    private boolean updStatus(FeedbackModel fb){
        return FeedbackHelper.updateStatusFb(fb);
    }

}
