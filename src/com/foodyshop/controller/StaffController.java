/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.BCrypt;
import com.foodyshop.helper.StaffHelper;
import com.foodyshop.main.Navigator;
import com.foodyshop.model.StaffModel;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 *
 * @author DELL
 */
public class StaffController implements Initializable {

    @FXML
    private TableView<StaffModel> tvStaff;

    @FXML
    private TableColumn<StaffModel, Integer> tcStt;

    @FXML
    private TableColumn<StaffModel, String> tcUsername;

    @FXML
    private TableColumn<StaffModel, String> tcPassword;

    @FXML
    private TableColumn<StaffModel, String> tcName;

    @FXML
    private TableColumn<StaffModel, String> tcType;

    @FXML
    private TableColumn<StaffModel, String> tcCreated;

    @FXML
    private TableColumn<StaffModel, String> tcUpdated;

    @FXML
    private TableColumn<StaffModel, String> tcStatus;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnLock;

    @FXML
    private Button btnUnLock;

    @FXML
    private Button btnChangePassword;

    @FXML
    private Button btnDelete;

    ObservableList<StaffModel> listStaff = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listStaff = FXCollections.observableArrayList();
        try {
            listStaff.addAll(StaffHelper.getAllStaff());
            tvStaff.setItems(listStaff);
            tcStt.setCellValueFactory(cellData -> cellData.getValue().getIdProperty());
            tcUsername.setCellValueFactory(cellData -> cellData.getValue().getUsernameProperty());
            tcPassword.setCellValueFactory(cellData -> cellData.getValue().getPasswordProperty());
            tcName.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
            tcType.setCellValueFactory(cellData -> cellData.getValue().getTypeProperty());
            tcCreated.setCellValueFactory(cellData -> cellData.getValue().getCreatedProperty());
            tcUpdated.setCellValueFactory(cellData -> cellData.getValue().getUpdatedProperty());
            tcStatus.setCellValueFactory(cellData -> cellData.getValue().getStatusProperty());

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        // btnAdd.setOnMouseClicked(e -> Navigator.getInstance().showAddStaff());
    }

    @FXML
    void onClickAdd(ActionEvent event) {
//        String password = "123456";
//        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
//        System.out.println();
        btnAdd.setOnMouseClicked(e -> Navigator.getInstance().showAddStaff());
//        btnAdd.setOnMouseClicked(e -> Navigator.getInstance().showAddStaff(new AddStaffController().IOnAddSuccess()
//        {
//            @Override
//            public void onAddSuccess(StaffModel staff){
//            listStaff.add(0, staff);
//            }
//        }));

    }

    @FXML
    void onClickChangePassword(ActionEvent event) {

    }

    @FXML
    void onClickDelete(ActionEvent event) {
        StaffModel staffModel = tvStaff.getSelectionModel().getSelectedItem();
        if (staffModel != null) {
            Alert alerts = new Alert(Alert.AlertType.CONFIRMATION);
            alerts.setTitle("ERROR");
            alerts.setHeaderText("Do you want delete " + staffModel.getUsername());
            alerts.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.OK) {
                    //  else {

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Delete success!");
                    alert.show();
                    listStaff.remove(staffModel);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Delete error");
                    alert.show();
                }
                // }

            });

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Please choose staff");
            alert.show();
        }
    }

    @FXML
    void onClickEdit(ActionEvent event) {
        StaffModel staff = tvStaff.getSelectionModel().getSelectedItem();
        if (staff != null) {
            btnEdit.setOnMouseClicked(e -> Navigator.getInstance().showEditStaff());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Please choose staff!");
            alert.show();
        }

    }

    @FXML
    void onClickLock(ActionEvent event) {
        StaffModel staffModel = tvStaff.getSelectionModel().getSelectedItem();
        if (staffModel == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No selected data");
            alert.setContentText("Select a staff before do this action!");
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("confirm");
            alert.setContentText("Are you sure to lock this staff?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.out.println("Click Ok");
                staffModel.setStatus(staffModel.STATUS_LOCK);
                if (updStatusStaff(staffModel)) {
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Update success!");
                    alert.show();
                } else {
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
    void onClickUnLock(ActionEvent event) {
        StaffModel staffModel = tvStaff.getSelectionModel().getSelectedItem();
        if (staffModel == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No selected data");
            alert.setContentText("Select a staff before do this action!");
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("confirm");
            alert.setContentText("Are you sure to unlock this staff?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.out.println("Click Ok");
                staffModel.setStatus(staffModel.STATUS_UNLOCK);
                if (updStatusStaff(staffModel)) {
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Update success!");
                    alert.show();
                } else {
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

    private boolean updStatusStaff(StaffModel staff) {
        return StaffHelper.updateStatusStaff(staff);
    }
}
