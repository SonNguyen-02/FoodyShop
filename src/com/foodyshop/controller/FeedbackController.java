/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.DBFeedbackHelper;
import com.foodyshop.model.FeedbackModel;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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

    @FXML
    private TableColumn<FeedbackModel, String> tcContent;

    @FXML
    private TableColumn<FeedbackModel, String> tcStatus;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnShow;

    @FXML
    private Button btnHidden;

    ObservableList<FeedbackModel> listFeedback;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        listFeedback = FXCollections.observableArrayList();
        tvFeedback.setItems(listFeedback);
        tcID.setCellValueFactory(cellData->cellData.getValue().getIDproperty());
        tcCustomerID.setCellValueFactory(cellData->cellData.getValue().getCustomerIDProperty());
        tcProductID.setCellValueFactory(cellData->cellData.getValue().getProductIDProperty());
        tcContent.setCellValueFactory(cellData->cellData.getValue().getContentProperty());
        tcStatus.setCellValueFactory(cellData->cellData.getValue().getStatusProperty());
        
        
        try {
            listFeedback.addAll(DBFeedbackHelper.showAllFeedback());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void onClickDelete(ActionEvent event) {

    }

    @FXML
    void onClickHidden(ActionEvent event) {

    }

    @FXML
    void onClickShow(ActionEvent event) {

    }

}
