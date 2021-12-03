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
import javafx.collections.FXCollections;
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
    
    private CategoryModel category;
    
    @FXML
    private TextField txtName;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    @FXML
    private ComboBox<TopicModel> cbTopic;

    @FXML
    private ComboBox<String> cbStatus;

    // nhận hàng từ bảng Category
    public void setData(CategoryModel category){
        this.category = category;
        
        txtName.setText(category.getName());

        topicList = TopicHelper.getAllTopic();
        if (topicList != null && !topicList.isEmpty()) {
            cbTopic.setItems(topicList);
//            cbTopic.setValue(topicList.get(0));
            for (TopicModel topic : topicList) {
                if(topic.getId() == category.getTopic_id()){
                    cbTopic.setValue(topic);
                    break;
                }
            }
        }
        // set mac dinh val cho checkbox
        cbStatus.setItems(FXCollections.observableArrayList(CategoryModel.SHOW, CategoryModel.HIDDEN));
        cbStatus.setValue(category.getStatusVal().get());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnSave.setOnMouseClicked(this::onClickSave);
        btnCancel.setOnMouseClicked(this::onClickCancel);
    }  
    private void onClickSave(MouseEvent e) {
        
        System.out.println(cbStatus.getValue());
        category.setStatusVal(cbStatus.getValue());
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
