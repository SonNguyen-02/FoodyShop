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
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author DELL
 */
public class AddStaffController implements Initializable {

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtName;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private ComboBox<String> cbType;

    @FXML
    private Button btnSubmit;

    @FXML
    private Button btnCancel;

    private IOnAddStaffSuccess mIOnAddStaffSuccess;

    private Stage stage;

    public interface IOnAddStaffSuccess {

        void IOnAddStaffSuccess(StaffModel staff);
    }

    public void initCallback(Stage stage, IOnAddStaffSuccess mIOnAddStaffSuccess) {
        this.mIOnAddStaffSuccess = mIOnAddStaffSuccess;
        this.stage = stage;
        cbType.setItems(FXCollections.observableArrayList(StaffModel.TYPE_ADMIN, StaffModel.TYPE_STAFF));
        cbType.setValue(StaffModel.TYPE_STAFF);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnSubmit.setOnMouseClicked(this::onClickSubmit);
        btnCancel.setOnMouseClicked(this::onClickCancel);
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

    private void onClickSubmit(MouseEvent e) {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        String name = txtName.getText().trim();

        if (username.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Please enter username!");
            alert.show();
            return;
        }
        if (password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Please enter password!");
            alert.show();
            return;
        }
        if (name.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Please enter name!");
            alert.show();
            return;
        }
        
        if(StaffHelper.isAccountExists(username)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Account is exists!");
            alert.show();
            txtUsername.requestFocus();
            return;
        }
        
        StaffModel staff = new StaffModel();
        staff.setUsername(username);
        staff.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        System.out.println(password);
        staff.setName(name);
        staff.setTypeVal(cbType.getValue());
        staff = StaffHelper.insertStaff(staff);
        if (staff != null) {
            stage.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucess");
            alert.setHeaderText("Insert success");
            alert.show();
            mIOnAddStaffSuccess.IOnAddStaffSuccess(staff);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Insert false!");
            alert.show();
        }
    }
}
