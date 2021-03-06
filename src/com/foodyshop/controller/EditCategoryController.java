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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author X PC
 */
public class EditCategoryController implements Initializable {

    private ObservableList<TopicModel> topicList;

    private CategoryModel category;

    private Stage stage;
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

    public void setData(Stage stage, CategoryModel category) {
        this.stage = stage;
        this.category = category;
        txtName.setText(category.getName());
        topicList = TopicHelper.getAllTopic();
        if (topicList != null && !topicList.isEmpty()) {
            cbTopic.setItems(topicList);
            for (TopicModel topic : topicList) {
                if (topic.getId() == category.getTopic_id()) {
                    cbTopic.setValue(topic);
                    break;
                }
            }
        }
        // set mac dinh val cho checkbox
        cbStatus.setItems(FXCollections.observableArrayList(TopicModel.SHOW, TopicModel.HIDDEN));
        cbStatus.setValue(category.getStatusVal().get());

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnSave.setOnMouseClicked(this::onClickSave);
        btnCancel.setOnMouseClicked(this::onClickCancel);
    }

    private void onClickSave(MouseEvent e) {
        String name = txtName.getText().trim();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        if (name.isEmpty()) {
            txtName.requestFocus();
            alert.setHeaderText("Please enter name");
            alert.show();
            return;
        }
        category.setName(name);
        category.setTopic_id(cbTopic.getValue().getId());
        category.setStatusVal(cbStatus.getValue());
        if (CategoryHelper.updateCategory(category)) {
            category.setTopicName(cbTopic.getValue().getName());
            Navigator.getInstance().getModalStage().close();
            Alert alerts = new Alert(Alert.AlertType.INFORMATION);
            alerts.setTitle("Success");
            alerts.setHeaderText("Edit success!");
            alerts.show();
        } else {
            Alert alerts = new Alert(Alert.AlertType.ERROR);
            alerts.setTitle("Error");
            alerts.setHeaderText("Edit false");
            alerts.show();
        }

    }

    private void onClickCancel(MouseEvent e) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close");
        alert.setHeaderText("Do you want close?");
        alert.showAndWait().ifPresent(btnType -> {
            if (btnType == ButtonType.OK) {
                stage.close();
            }
        });

    }
}
