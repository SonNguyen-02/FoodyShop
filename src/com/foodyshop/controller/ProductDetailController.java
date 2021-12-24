/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import static com.foodyshop.main.Config.IMG_FOOD_DIR;
import com.foodyshop.main.Navigator;
import com.foodyshop.model.ProductModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextAlignment;

/**
 * FXML Controller class
 *
 * @author X PC
 */
public class ProductDetailController implements Initializable {

    @FXML
    private Label lbName;

    @FXML
    private Label lbCategory;

    @FXML
    private Label lbPrice;

    @FXML
    private Label lbCreated;

    @FXML
    private TextArea txtDescription;
    
    @FXML
    private ImageView imgDetail;

    @FXML
    private Button btnCancel;


    public void initProductModel(ProductModel product) {
        lbName.setText(product.getName());
        lbCategory.setText(product.getCategoryName());
        lbCreated.setText(product.getCreated());
        txtDescription.setText(product.getDescription());
        lbPrice.setText(product.getPrice().toString() + "  VNĐ");

        Image imageDetail = new Image(IMG_FOOD_DIR + product.getImgDetail(), 256, 192, false, true, true);
        imgDetail.setImage(imageDetail);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnCancel.setOnMouseClicked(this::onClickCancel);
        txtDescription.setWrapText(true);
        txtDescription.setEditable(false);
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
}
