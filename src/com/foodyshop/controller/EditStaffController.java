/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.StaffHelper;
import com.foodyshop.main.Navigator;
import com.foodyshop.model.Respond;
import com.foodyshop.model.StaffModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author DELL
 */
public class EditStaffController implements Initializable {

    private ObservableList<StaffModel> listStaff;
    private StaffModel staff;
    private IOnEditStaffSuccess mIOnEditStaffSuccess;
    private Stage stage;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPassword;

    @FXML
    private ChoiceBox<String> cbType;

    @FXML
    private Button btnSubmit;

    @FXML
    private Button btnCancel;
    
    public void setDataStaff(Stage stage, StaffModel staff, IOnEditStaffSuccess mIOnEditStaffSuccess) {
        this.stage = stage;
        this.staff = staff;
        txtUsername.setText(staff.getUsername());
        txtPassword.setText(staff.getPassword());
        txtName.setText(staff.getName());
        cbType.setItems(FXCollections.observableArrayList(StaffModel.TYPE_ADMIN,StaffModel.TYPE_STAFF));
        cbType.setValue(staff.getTypeVal().get());
        this.mIOnEditStaffSuccess = this.mIOnEditStaffSuccess;
    }

    public interface IOnEditStaffSuccess {

        void callback();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void onClickCancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close");
        alert.setHeaderText("Do you want close?");
        alert.showAndWait().ifPresent(btnType -> {
            if (btnType == ButtonType.OK) {
                Navigator.getInstance().getModalStage().close();
            }
        });
    }

    @FXML
    void onClickSubmit(ActionEvent event) {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        String name = txtName.getText().trim();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        if (name.isEmpty()) {
            txtName.requestFocus();
            alert.setHeaderText("Please enter name");
            alert.show();
            return;
        }
        staff.setUsername(username);
        staff.setPassword(password);
        staff.setName(name);
        staff.setTypeVal(cbType.getValue());
        boolean result = StaffHelper.editStaff(staff);
        if (result) {
            Alert alerts = new Alert(Alert.AlertType.INFORMATION);
            alerts.setTitle("Success");
            alerts.setHeaderText("Edit success!");
            alerts.show();
            mIOnEditStaffSuccess.callback();
            Navigator.getInstance().getModalStage().close();
        } else {
            Alert alerts = new Alert(Alert.AlertType.ERROR);
            alerts.setTitle("Error");
            alerts.setHeaderText("Edit false");
            alerts.show();
        }

    }

}
