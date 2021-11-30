/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.TopicHelper;
import com.foodyshop.model.TopicModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author X PC
 */
public class TopicPageController implements Initializable {

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
    private TableColumn<TopicModel, Integer> tcStatus;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnDelete;

    ObservableList<TopicModel> listTopic = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //btnAdd.setOnMouseClicked(e -> Navigator.getInstance().showAddCategory());
        tcId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper((tblTopic.getItems().indexOf(cellData.getValue()) + 1) + ""));
        tcName.setCellValueFactory(cellValue -> cellValue.getValue().getNameProperty());
        tcImg.setCellValueFactory(cellValue -> cellValue.getValue().getImgView());
        tcCreated.setCellValueFactory(cellValue -> cellValue.getValue().getCreatedProperty());
        tcStatus.setCellValueFactory(cellValue -> cellValue.getValue().getStatusProperty());

        listTopic = TopicHelper.getAllTopic();

        tblTopic.setItems(listTopic);

    }

}
