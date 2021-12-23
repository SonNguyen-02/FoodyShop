/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.CategoryHelper;
import com.foodyshop.helper.TopicHelper;
import com.foodyshop.main.Const;
import com.foodyshop.main.CurrentAccount;
import com.foodyshop.main.Navigator;
import com.foodyshop.main.APIService;
import com.foodyshop.model.TopicModel;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author X PC
 */
public class TopicController implements Initializable {

    @FXML
    private TableView<TopicModel> tblTopic;

    @FXML
    private TableColumn<TopicModel, Integer> tcId;

    @FXML
    private TableColumn<TopicModel, String> tcName;

    @FXML
    private TableColumn<TopicModel, ImageView> tcImg;

    @FXML
    private TableColumn<TopicModel, String> tcCreated;

    @FXML
    private TableColumn<TopicModel, String> tcStatus;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnDelete;

    ObservableList<TopicModel> listTopic = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        tcId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper((tblTopic.getItems().indexOf(cellData.getValue()) + 1) + ""));
        tcName.setCellValueFactory(cellValue -> cellValue.getValue().getNameProperty());
        tcImg.setCellValueFactory(cellValue -> cellValue.getValue().getImgView());
        tcCreated.setCellValueFactory(cellValue -> cellValue.getValue().getCreatedProperty());
        tcStatus.setCellValueFactory(cellValue -> cellValue.getValue().getStatusVal());
        
        listTopic = TopicHelper.getAllTopic();

        tblTopic.setItems(listTopic);
        if (CurrentAccount.getInstance().isStaff()) {
            btnAdd.setVisible(false);
            btnEdit.setVisible(false);
            btnDelete.setVisible(false);
        }else{   
        btnDelete.setOnMouseClicked(this::onClickDelete);
        btnAdd.setOnMouseClicked(e -> Navigator.getInstance().showAddTopic(new AddTopicController.IOnInsertTopicSuccess() {
            @Override
            public void callback(TopicModel topic) {
                listTopic.add(0, topic);
            }
        }));
        btnEdit.setOnMouseClicked(this::onClickEdit);
        }
    }

    private void onClickEdit(MouseEvent e){
        TopicModel topic = tblTopic.getSelectionModel().getSelectedItem();
        if(topic != null){
             Navigator.getInstance().showEditTopic(topic);
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Please choose topic");
            alert.show();
        }
    }
    
    private void onClickDelete(MouseEvent e) {
        TopicModel topic = tblTopic.getSelectionModel().getSelectedItem();
        if (topic != null) {
            Alert alerts = new Alert(Alert.AlertType.CONFIRMATION);
            alerts.setTitle("ERROR");
            alerts.setHeaderText("Do you want delete " + topic.getName());
            alerts.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.OK) {
                    boolean isHasLink = CategoryHelper.isTopicHasLinkToCategorys(topic);
                    if (isHasLink) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR");
                        alert.setHeaderText("Topic has link with any category you must delete product first");
                        alert.show();
                    } else {
                        try {
                            APIService.removeImageFromApi(Const.TYPE_TOPIC, topic.getImg());
                            if (TopicHelper.delete(topic)) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("ERROR");
                                alert.setHeaderText("Delete success!");
                                alert.show();
                                listTopic.remove(topic);
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("ERROR");
                                alert.setHeaderText("Delete error");
                                alert.show();
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(TopicController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Please choose topic");
            alert.show();
        }
    }
}
