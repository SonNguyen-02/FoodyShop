/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.CategoryHelper;
import com.foodyshop.helper.TopicHelper;
import com.foodyshop.main.Navigator;
import com.foodyshop.model.CategoryModel;
import com.foodyshop.model.TopicModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author X PC
 */
public class EditCategoryController implements Initializable {
    private ObservableList<TopicModel> topicList;
    private ObservableList<CategoryModel> statusList;
    @FXML
    private TextField txtName;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    @FXML
    private ComboBox<TopicModel> cbTopic;

    @FXML
    private ComboBox<CategoryModel> cbStatus;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnSave.setOnMouseClicked(this::onClickSave);
        btnCancel.setOnMouseClicked(this::onClickCancel);
        topicList = TopicHelper.getAllTopic();
        if (topicList != null && !topicList.isEmpty()) {
            cbTopic.setItems(topicList);
            cbTopic.setValue(topicList.get(0));
        }
        statusList = CategoryHelper.getAllCategory();
        if (statusList != null && !statusList.isEmpty()) {
            cbStatus.setItems(statusList);
            cbStatus.setValue(statusList.get(0));
        
        } 
    }  
    private void onClickSave(MouseEvent e) {
        
        
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
