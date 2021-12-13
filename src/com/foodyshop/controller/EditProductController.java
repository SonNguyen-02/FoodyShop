/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.CategoryHelper;
import static com.foodyshop.main.Config.IMG_FOOD_DIR;
import com.foodyshop.model.CategoryModel;
import com.foodyshop.model.ProductModel;
import com.foodyshop.model.TopicModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author X PC
 */
public class EditProductController implements Initializable {
    private Stage stage;
    @FXML
    private ImageView imgDetail;

    @FXML
    private ImageView img;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextArea txtDes;

    @FXML
    private ComboBox<CategoryModel> cbCategory;

    @FXML
    private ComboBox<String> cbStatus;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;
    private IOnUpdateProduct mIOnUpdateProduct;
    private ProductModel mProductModel;
    private ObservableList<CategoryModel> categoryList;
    
     public interface IOnUpdateProduct{
        void callback();
    }
 
     public void initData(ProductModel product,Stage stage, IOnUpdateProduct mIOnUpdateProduct) {
        this.stage = stage;
        mProductModel = product;
        txtName.setText(product.getName());
        txtPrice.setText(product.getPrice().toString());
        

        Image image = new Image(IMG_FOOD_DIR + product.getImg(), 100, 100, false, true, true);
        Image imageDetail = new Image(IMG_FOOD_DIR + product.getImgDetail(), 256, 192, false, true, true);
        img.setImage(image);
        imgDetail.setImage(imageDetail);
        categoryList = CategoryHelper.getAllCategory();
        if (categoryList != null && !categoryList.isEmpty()) {
            cbCategory.setItems(categoryList);           
            for (CategoryModel category : categoryList) {                           
                if(category.getId() == product.getCategoryId()){
                    cbCategory.setValue(category);
                    break;
                }
            }
        }
        cbStatus.setItems(FXCollections.observableArrayList(ProductModel.STILL, ProductModel.SOLD_OUT));
        cbStatus.setValue(product.getStatusVal().get());
        
        this.mIOnUpdateProduct = mIOnUpdateProduct;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
