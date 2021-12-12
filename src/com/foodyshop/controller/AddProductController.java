/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.CategoryHelper;
import static com.foodyshop.main.Const.PLACEHOLDER_NO1_IMG_PATH;
import static com.foodyshop.main.Const.PLACEHOLDER_NO_IMG_PATH;
import com.foodyshop.main.Navigator;
import com.foodyshop.model.CategoryModel;
import com.foodyshop.model.ProductModel;
import java.io.File;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author X PC
 */
public class AddProductController implements Initializable {
    private Stage stage;
    private File imgProductFile;
    private File imgDetailProductFile;
    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPrice;

    @FXML
    private ComboBox<CategoryModel> cbCategory;

    @FXML
    private TextField txtDescription;

    @FXML
    private ImageView img;

    @FXML
    private ImageView imgDetail;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;
    private IOnInsertProductSuccess mIOnInsertProductSuccess;
    private ObservableList<CategoryModel> categoryList;
    public interface IOnInsertProductSuccess {

        void callback(ProductModel product);
    }
    
    public void initData(Stage stage, IOnInsertProductSuccess mIOnInsertProductSuccess) {
        this.stage = stage;
        this.mIOnInsertProductSuccess = mIOnInsertProductSuccess;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setDefaultImg(img);
        setDefaultImgDetail(imgDetail);
        btnCancel.setOnMouseClicked(this::onClickCancel);
        img.setOnMouseClicked(this::onClickImg);
        imgDetail.setOnMouseClicked(this::onClickImgDetail);
        categoryList = CategoryHelper.getAllCategory();
        if (categoryList != null && !categoryList.isEmpty()) {
            cbCategory.setItems(categoryList);
            cbCategory.setValue(categoryList.get(0));          
        }
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
    private void setDefaultImg(ImageView img) {
        
        img.setImage(new Image("file:" + PLACEHOLDER_NO_IMG_PATH));
        
    }
    private void setDefaultImgDetail(ImageView imgDetail) {
        
        imgDetail.setImage(new Image("file:" + PLACEHOLDER_NO1_IMG_PATH));
        
    }
        
        
    
    
    private boolean isImage(String name) {
        int index = name.lastIndexOf(".");
        if (!name.endsWith(".") && index != -1) {
            String fileExt = name.substring(index + 1);
            String[] listExt = "JPG|PNG|JPEG|GIF|BMP".split("\\|");
            for (String ext : listExt) {
                if (ext.equalsIgnoreCase(fileExt.toUpperCase())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void onClickImg(MouseEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose image to upload");
        File fileChoose = fileChooser.showOpenDialog(stage);
        imgProductFile = fileChoose;
        if (fileChoose != null) {
            if (isImage(fileChoose.getName())) {
                img.setImage(new Image("file:" + fileChoose.getPath()));
                System.out.println(fileChoose.getPath());
            } else {
                setDefaultImg(img);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("File isn't image");
                alert.show();
            }
        } else {
            setDefaultImg( img);
        }
        
    }
    private void onClickImgDetail(MouseEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose image to upload");
        File fileChoose = fileChooser.showOpenDialog(stage);
        imgDetailProductFile = fileChoose;
        if (fileChoose != null) {
            if (isImage(fileChoose.getName())) {
                imgDetail.setImage(new Image("file:" + fileChoose.getPath()));
                System.out.println(fileChoose.getPath());
            }else {
                setDefaultImg(imgDetail);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("File isn't image");
                alert.show();            
            }
        } else {
            setDefaultImg( img);
        }
    }
    

}
