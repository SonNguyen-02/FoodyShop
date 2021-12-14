/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.StaffHelper;
import com.foodyshop.main.Navigator;
import com.foodyshop.model.StaffModel;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.scene.input.MouseEvent;

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
        // TO DO
        tcStt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper((tvStaff.getItems().indexOf(cellData.getValue()) + 1) + ""));
        tcUsername.setCellValueFactory(cellData -> cellData.getValue().getUsernameProperty());
        tcPassword.setCellValueFactory(cellData -> cellData.getValue().getPasswordProperty());
        tcName.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        tcType.setCellValueFactory(cellData -> cellData.getValue().getTypeVal());
        tcCreated.setCellValueFactory(cellData -> cellData.getValue().getCreatedProperty());
        tcStatus.setCellValueFactory(cellData -> cellData.getValue().getStatusVal());
        
        listStaff = StaffHelper.getAllStaff();
        tvStaff.setItems(listStaff);
        
        StaffModel staff = new StaffModel();
        btnAdd.setOnMouseClicked(e -> Navigator.getInstance().showAddStaff(staff, new AddStaffController.IOnAddStaffSuccess() {
            @Override
            public void IOnAddStaffSuccess(StaffModel staffModel) {
                listStaff.add(0, staffModel);
            }
        }));
    }

    @FXML
    void onClickAdd(ActionEvent event) throws SQLException {
//        String password = "123456";
//        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
//        System.out.println();

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
                    if (StaffHelper.delete(staffModel)) {
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

                }

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
                staffModel.setStatusVal(staffModel.STATUS_LOCK);
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
                staffModel.setStatusVal(staffModel.STATUS_UNLOCK);
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
