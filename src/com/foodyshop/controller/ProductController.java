/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.Order_DetailHelper;
import com.foodyshop.helper.ProductHelper;
import com.foodyshop.main.Const;
import com.foodyshop.main.CurrentAccount;
import com.foodyshop.main.Navigator;
import com.foodyshop.main.UploadImageToApi;
import com.foodyshop.model.ProductModel;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author X PC
 */
public class ProductController implements Initializable {

    @FXML
    private TableView<ProductModel> tblProduct;

    @FXML
    private TableColumn<ProductModel, Integer> tcStt;

    @FXML
    private TableColumn<ProductModel, String> tcCateName;

    @FXML
    private TableColumn<ProductModel, String> tcName;

    @FXML
    private TableColumn<ProductModel, Integer> tcPrice;

    @FXML
    private TableColumn<ProductModel, ImageView> tcImg;

    @FXML
    private TableColumn<ProductModel, String> tcCreated;

    @FXML
    private TableColumn<ProductModel, String> tcStatus;

    @FXML
    private Button btnAdd, btnEdit, btnDelete, btnProductDetail;

    @FXML
    private TextField txtSearch;

    ObservableList<ProductModel> listProduct = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tcStt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper((tblProduct.getItems().indexOf(cellData.getValue()) + 1) + ""));
        tcCateName.setCellValueFactory(cellValue -> cellValue.getValue().getCategoryNameProperty());
        tcName.setCellValueFactory(cellValue -> cellValue.getValue().getNameProperty());
        tcPrice.setCellValueFactory(cellValue -> cellValue.getValue().getPriceProperty());
        tcCreated.setCellValueFactory(cellValue -> cellValue.getValue().getCreatedProperty());
        tcStatus.setCellValueFactory(cellValue -> cellValue.getValue().getStatusVal());
        tcImg.setCellValueFactory(cellValue -> cellValue.getValue().getImgView());
        listProduct = ProductHelper.getAllProduct();
        tblProduct.setItems(listProduct);
        btnProductDetail.setOnMouseClicked(this::onclickshowProduct_Detail);
        if (CurrentAccount.getInstance().isStaff()) {
            btnAdd.setVisible(false);
            btnEdit.setVisible(false);
            btnDelete.setVisible(false);
        }else{   
        btnDelete.setOnMouseClicked(this::onClickDelete);
        btnEdit.setOnMouseClicked(this::onClickEdit);      
        btnAdd.setOnMouseClicked(e -> Navigator.getInstance().showAddProduct(new AddProductController.IOnInsertProductSuccess() {
            @Override
            public void callback(ProductModel product) {
                product.setImg(product.getImg());
                listProduct.add(0, product);
            }
        }));
                    }       
        FilteredList<ProductModel> filteredData = new FilteredList<>(listProduct, b -> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ProductModel -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if (ProductModel.getCategoryName().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (ProductModel.getName().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<ProductModel> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(tblProduct.comparatorProperty());

        tblProduct.setItems(sortedData);
    }

    private void onClickDelete(MouseEvent e) {

        ProductModel product = tblProduct.getSelectionModel().getSelectedItem();
        if (product != null) {
            Alert alerts = new Alert(Alert.AlertType.CONFIRMATION);
            alerts.setTitle("CONFIRMATION");
            alerts.setHeaderText("Do you want delete " + product.getName());
            alerts.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.OK) {
                    boolean isHasLink = Order_DetailHelper.isProductHasLinkToOrder_detail(product);
                    if (isHasLink) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR");
                        alert.setHeaderText("product has link with any order you must delete order first");
                        alert.show();
                    } else {
                        if (ProductHelper.delete(product)) {
                            try {
                                UploadImageToApi.removeImageFromApi(Const.TYPE_FOOD, product.getImg());
                                UploadImageToApi.removeImageFromApi(Const.TYPE_FOOD, product.getImgDetail());
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("ERROR");
                                alert.setHeaderText("Delete success!");
                                alert.show();
                                listProduct.remove(product);
                            } catch (IOException ex) {
                                Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("ERROR");
                            alert.setHeaderText("Delete error");
                            alert.show();
                        }
                    }
                }
            });

        }
    }

    private void onclickshowProduct_Detail(MouseEvent e) {
        ProductModel product = tblProduct.getSelectionModel().getSelectedItem();
        if (product != null) {
            Navigator.getInstance().showProductDetail(product);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("You Must choose!!!");
            alert.show();
        }
    }

    private void onClickEdit(MouseEvent e) {
        ProductModel product = tblProduct.getSelectionModel().getSelectedItem();
        if (product != null) {
            Navigator.getInstance().showEditProduct(product);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Please choose product");
            alert.show();
        }
    }
}
