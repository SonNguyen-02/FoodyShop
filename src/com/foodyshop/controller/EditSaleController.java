/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.ProductHelper;
import com.foodyshop.helper.SaleHelper;
import static com.foodyshop.main.Config.IMG_SALE_DIR;
import com.foodyshop.model.ProductModel;
import com.foodyshop.model.SaleModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author APlaptop
 */
public class EditSaleController implements Initializable {

    
    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnChooseFile;

    @FXML
    private ImageView imgSale;

    @FXML
    private ComboBox<ProductModel> cbProductName;

    @FXML
    private TextField txtContent;

    @FXML
    private TextField txtDiscount;

    @FXML
    private DatePicker dpStartDate;

    @FXML
    private DatePicker dpEndDate;
    
    private SaleModel mSaleModel;
    
    private Stage stage;
    
    private ObservableList<ProductModel> productList;

    public void initData(Stage stage, SaleModel sale) {
        this.stage = stage;
        mSaleModel = sale;
//        txtName.setText(topic.getName());

        Image image = new Image(IMG_SALE_DIR + sale.getImg(), 300, 300, false, true);
        imgSale.setImage(image);
        productList = ProductHelper.getAllProduct();
        if (productList != null && !productList.isEmpty()) {
            cbProductName.setItems(productList);           
            for (ProductModel product : productList) {                           
                if(product.getId() == sale.getProductId()){
                    cbProductName.setValue(product);
                    break;
                }
            }
        }       
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
