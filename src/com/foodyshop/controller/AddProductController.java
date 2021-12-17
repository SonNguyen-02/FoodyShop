/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.CategoryHelper;
import com.foodyshop.helper.ProductHelper;
import com.foodyshop.main.Const;
import static com.foodyshop.main.Const.PLACEHOLDER_NO1_IMG_PATH;
import static com.foodyshop.main.Const.PLACEHOLDER_NO_IMG_PATH;
import com.foodyshop.main.Navigator;
import com.foodyshop.main.UploadImageToApi;
import com.foodyshop.model.CategoryModel;
import com.foodyshop.model.ProductModel;
import com.foodyshop.model.Respond;
import static com.sun.javafx.fxml.expression.Expression.add;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextAlignment;
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
    private TextArea txtDescription;

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
        setDefaultImg();
        setDefaultImgDetail();
        btnCancel.setOnMouseClicked(this::onClickCancel);
        btnSave.setOnMouseClicked(this::onClickSave);
        img.setOnMouseClicked(this::onClickImg);
        imgDetail.setOnMouseClicked(this::onClickImgDetail);
        categoryList = CategoryHelper.getAllCategory();
        if (categoryList != null && !categoryList.isEmpty()) {
            cbCategory.setItems(categoryList);
            cbCategory.setValue(categoryList.get(0));
        }
    }
//
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

    private void setDefaultImg() {
        img.setImage(new Image("file:" + PLACEHOLDER_NO_IMG_PATH));
    }

    private void setDefaultImgDetail() {
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
                setDefaultImg();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("File isn't image");
                alert.show();
            }
        } else {
            setDefaultImg();
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
            } else {
                setDefaultImgDetail();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("File isn't image");
                alert.show();
            }
        } else {
            setDefaultImgDetail();
        }
    }

    private void onClickSave(MouseEvent e) {
        String name = txtName.getText().trim();
        String price = txtPrice.getText().trim();
        String description = txtDescription.getText().trim();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        if (name.isEmpty()) {
            txtName.requestFocus();
            alert.setHeaderText("Please enter name!!");
            alert.show();
            return;
        }
        if (price.isEmpty()) {
            txtPrice.requestFocus();
            alert.setHeaderText("Please enter price!!");
            alert.show();
            return;
        }
        String priceRegex = "^[1-9][0-9]{0,10}$";
        if (!price.matches(priceRegex)) {
            txtPrice.requestFocus();
            alert.setHeaderText("Price is invalid!!");
            alert.show();
            return;
        }
        if (description.isEmpty()) {
            txtDescription.requestFocus();
            alert.setHeaderText("Please enter description!!");
            alert.show();
            return;
        }
        if (imgProductFile == null) {
            alert.setHeaderText("Please choose a file img");
            alert.show();
            return;
        }
        if (!isImage(imgProductFile.getName())) {
            setDefaultImg();
            alert.setHeaderText("File isn't image!");
            alert.show();
            return;
        }
        if (imgDetailProductFile == null) {
            alert.setHeaderText("Please choose a file img");
            alert.show();
            return;
        }
        if (!isImage(imgDetailProductFile.getName())) {
            setDefaultImgDetail();
            alert.setHeaderText("File isn't image!");
            alert.show();
            return;
        }

        // call API
        try {
            Respond norImgRespond = UploadImageToApi.uploadImageToApi(imgProductFile, Const.TYPE_FOOD);
            Respond desImgRespond = UploadImageToApi.uploadImageToApi(imgDetailProductFile, Const.TYPE_FOOD);
            if (norImgRespond.isSuccess() && desImgRespond.isSuccess()) {
                ProductModel product = new ProductModel();
                product.setName(name);
                product.setCategoryId(cbCategory.getValue().getId());
                product.setCategoryName(cbCategory.getValue().getName());
                product.setPrice(Integer.parseInt(price));
                product.setDescription(description);
                product.setImg(norImgRespond.getMsg());
                product.setImgDetail(desImgRespond.getMsg());
                // Insert to database
                product = ProductHelper.insertProduct(product);

                if (product == null) {
                    alert.setHeaderText("Add false");
                    alert.show();
                } else {
                    stage.close();
                    Alert alerts = new Alert(Alert.AlertType.INFORMATION);
                    alerts.setTitle("Success");
                    alerts.setHeaderText("Add success!");
                    alerts.show();
                    mIOnInsertProductSuccess.callback(product);
                }
            } else {
                Alert alerts = new Alert(Alert.AlertType.ERROR);
                alerts.setTitle("Error");
                alerts.setHeaderText("Add false");
                alerts.show();
            }

        } catch (IOException ex) {
            Logger.getLogger(TestDemoController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
