/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.controller.AddSaleController.IOnInsertSaleSuccess;
import com.foodyshop.helper.SaleHelper;
import com.foodyshop.main.Const;
import com.foodyshop.main.Navigator;
import com.foodyshop.main.UploadImageToApi;
import com.foodyshop.model.Order_DetailModel;
import com.foodyshop.model.SaleModel;
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
 * @author APlaptop
 */
public class SaleController implements Initializable {

    @FXML
    private TableView<SaleModel> tblSale;

    @FXML
    private TableColumn<SaleModel, Integer> tcStt;

    @FXML
    private TableColumn<SaleModel, String> tcProductName;

    @FXML
    private TableColumn<SaleModel, Integer> tcDiscount;

    @FXML
    private TableColumn<SaleModel, String> tcContent;

    @FXML
    private TableColumn<SaleModel, ImageView> tcImg;

    @FXML
    private TableColumn<SaleModel, String> tcStart;

    @FXML
    private TableColumn<SaleModel, String> tcEnd;

    @FXML
    private TableColumn<SaleModel, String> tcCreated;

    @FXML
    private TableColumn<SaleModel, String> tcStatus;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnDelete;

    ObservableList<SaleModel> listSale = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        SaleModel sale = new SaleModel();
        btnDelete.setOnMouseClicked(this::onClickDelete);
        btnAdd.setOnMouseClicked(e -> Navigator.getInstance().showAddSale(sale, new AddSaleController.IOnInsertSaleSuccess() {
            @Override
            public void callback(SaleModel sale) {
               listSale.add(0, sale);
            }
        }));
        tcStt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper((tblSale.getItems().indexOf(cellData.getValue()) + 1) + ""));
        tcProductName.setCellValueFactory(cellValue -> cellValue.getValue().getProductNameProperty());
        tcDiscount.setCellValueFactory(cellValue -> cellValue.getValue().getDiscountProperty());
        tcContent.setCellValueFactory(cellValue -> cellValue.getValue().getContentProperty());
        tcImg.setCellValueFactory(cellValue -> cellValue.getValue().getImgView());
        tcStart.setCellValueFactory(cellValue -> cellValue.getValue().getStart_dateProperty());
        tcEnd.setCellValueFactory(cellValue -> cellValue.getValue().getEnd_dateProperty());
        tcCreated.setCellValueFactory(cellValue -> cellValue.getValue().getCreatedProperty());
        tcStatus.setCellValueFactory(cellValue -> cellValue.getValue().getStatusVal());

        listSale = SaleHelper.getAllSale();
        tblSale.setItems(listSale);
    }

    private void onClickAdd(MouseEvent e){
        SaleModel sale = tblSale.getSelectionModel().getSelectedItem();
        
    }
    
    private void onClickDelete(MouseEvent e) {
        SaleModel sale = tblSale.getSelectionModel().getSelectedItem();
        //        Order_DetailModel OrderDetail = new Order_DetailModel();
        if (sale != null) {
            Alert alerts = new Alert(Alert.AlertType.CONFIRMATION);
            alerts.setTitle("ERROR");
            alerts.setHeaderText("Do you want delete sale of product " + sale.getProductName());
            alerts.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.OK) {
                    boolean isHasLink = SaleHelper.isSaleHasLinkToOrderDetail(sale);
                    System.out.println(isHasLink);
                    if (isHasLink) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR");
                        alert.setHeaderText("Sale has link with any product you must delete product first");
                        alert.show();
                    } else {
                        try {
                            UploadImageToApi.removeImageFromApi(Const.TYPE_TOPIC, sale.getImg());

                            if (SaleHelper.deleteSale(sale)) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("ERROR");
                                alert.setHeaderText("Delete success!");
                                alert.show();
                                listSale.remove(sale);
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("ERROR");
                                alert.setHeaderText("Delete error");
                                alert.show();
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(SaleController.class.getName()).log(Level.SEVERE, null, ex);
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
