/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.StaffHelper;
import com.foodyshop.main.CurrentAccount;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author DELL
 */
public class EditStaffController implements Initializable {

    private ObservableList<StaffModel> listStaff;
    private StaffModel staff;
    private Stage stage;

    @FXML
    private Label lbUsername;

    @FXML
    private TextField txtName;

    @FXML
    private ComboBox<String> cbType;

    @FXML
    private Button btnSubmit;

    @FXML
    private Button btnCancel;

    public void setDataStaff(Stage stage, StaffModel staff) {
        this.stage = stage;
        this.staff = staff;
        lbUsername.setText(staff.getUsername());
        txtName.setText(staff.getName());
        cbType.setItems(FXCollections.observableArrayList(StaffModel.TYPE_ADMIN, StaffModel.TYPE_STAFF));
        cbType.setValue(staff.getTypeVal().get());
        if (CurrentAccount.getInstance().isCurrentAccount(staff.getId())) {
            cbType.setDisable(true);
        }
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
        String name = txtName.getText().trim();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        if (name.isEmpty()) {
            txtName.requestFocus();
            alert.setHeaderText("Please enter name");
            alert.show();
            return;
        }
        staff.setName(name);
        staff.setTypeVal(cbType.getValue());
        boolean result = StaffHelper.editStaff(staff);
        if (result) {
            Alert alerts = new Alert(Alert.AlertType.INFORMATION);
            alerts.setTitle("Success");
            alerts.setHeaderText("Edit success!");
            alerts.show();
            Navigator.getInstance().getModalStage().close();
        } else {
            Alert alerts = new Alert(Alert.AlertType.ERROR);
            alerts.setTitle("Error");
            alerts.setHeaderText("Edit false");
            alerts.show();
        }

    }

}
