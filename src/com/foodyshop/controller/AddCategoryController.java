/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.CategoryHelper;
import com.foodyshop.helper.TopicHelper;
import com.foodyshop.main.Navigator;
import com.foodyshop.model.TopicModel;
import com.foodyshop.model.CategoryModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;






/**
 *
 * @author X PC
 */
public class AddCategoryController implements Initializable{
    
    private ObservableList<TopicModel> topicList;
    
    @FXML
    private Button btnSave, btnCancel;
    
    
    @FXML
    private TextField txtName;
    
    @FXML
    private ComboBox<TopicModel> cbTopic;
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btnSave.setOnMouseClicked(this::onClickSave);
        btnCancel.setOnMouseClicked(this::onClickCancel);
        topicList = TopicHelper.getAllTopic();
        if(topicList != null && !topicList.isEmpty()){
            cbTopic.setItems(topicList);
            cbTopic.setValue(topicList.get(0));
        }
        
    }

    private void onClickSave(MouseEvent e) {
        
        //int topicId = cbTopic.getValue().getId();
            StringBuilder errors = new StringBuilder();
            if(txtName.getText().equals("")) {
               errors.append("Name must be entered");              
            }
            if(cbTopic.getSelectionModel().equals("")){
                errors.append("topic must be entered");
            }   
            if(errors.length()>0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("add error");
                alert.show();
            }
           txtName.getText();
           cbTopic.getValue();
          
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
