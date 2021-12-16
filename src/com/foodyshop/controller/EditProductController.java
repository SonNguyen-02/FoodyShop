/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.CategoryHelper;
import com.foodyshop.helper.ProductHelper;
import static com.foodyshop.main.Config.IMG_FOOD_DIR;
import com.foodyshop.main.Const;
import static com.foodyshop.main.Const.PLACEHOLDER_NO1_IMG_PATH;
import static com.foodyshop.main.Const.PLACEHOLDER_NO_IMG_PATH;
import com.foodyshop.main.Navigator;
import com.foodyshop.main.UploadImageToApi;
import com.foodyshop.model.CategoryModel;
import com.foodyshop.model.ProductModel;
import com.foodyshop.model.Respond;
import com.foodyshop.model.TopicModel;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author X PC
 */
public class EditProductController implements Initializable {

    private Stage stage;
    private File imgProductFile;
    private File imgDetailProductFile;
    @FXML
    private ComboBox<CategoryModel> cbCategory;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPrice;

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

    @FXML
    private ComboBox<String> cbStatus;

    private ProductModel mProductModel;
    private ObservableList<CategoryModel> categoryList;

    public void initData(ProductModel product, Stage stage) {
        this.stage = stage;
        mProductModel = product;
        txtName.setText(product.getName());
        txtPrice.setText(product.getPrice().toString());
        txtDescription.setText(product.getDescription());

        Image image = new Image(IMG_FOOD_DIR + product.getImg(), 100, 100, false, true, true);
        Image imageDetail = new Image(IMG_FOOD_DIR + product.getImgDetail(), 256, 192, false, true, true);
        img.setImage(image);
        imgDetail.setImage(imageDetail);
        categoryList = CategoryHelper.getAllCategory();
        if (categoryList != null && !categoryList.isEmpty()) {
            cbCategory.setItems(categoryList);
            for (CategoryModel category : categoryList) {
                if (category.getId() == product.getCategoryId()) {
                    cbCategory.setValue(category);
                    break;
                }
            }
        }
        cbStatus.setItems(FXCollections.observableArrayList(ProductModel.STILL, ProductModel.SOLDOUT));
        cbStatus.setValue(product.getStatusVal().get());

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setDefaultImg();
        setDefaultImgDetail();
        img.setOnMouseClicked(this::onClickImg);
        imgDetail.setOnMouseClicked(this::onClickImgDetail);
        btnCancel.setOnMouseClicked(this::onClickCancel);
        btnSave.setOnMouseClicked(this::onClickSave);
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

    private void setDefaultImg() {
        img.setImage(new Image("file:" + PLACEHOLDER_NO_IMG_PATH));
    }

    private void setDefaultImgDetail() {
        imgDetail.setImage(new Image("file:" + PLACEHOLDER_NO1_IMG_PATH));
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
        if (imgProductFile != null) {
            if (!isImage(imgProductFile.getName())) {
                setDefaultImg();
                alert.setHeaderText("File isn't image!");
                alert.show();
                return;
            }
        }

        if (imgDetailProductFile != null) {
            if (!isImage(imgDetailProductFile.getName())) {
                setDefaultImgDetail();
                alert.setHeaderText("File isn't image!");
                alert.show();
                return;
            }
        }

        try {
            mProductModel.setName(name);
            mProductModel.setCategoryId(cbCategory.getValue().getId());
            mProductModel.setCategoryName(cbCategory.getValue().getName());
            mProductModel.setPrice(Integer.parseInt(price));
            mProductModel.setDescription(description);
            if (imgProductFile != null) {
                Respond norImgRespond = UploadImageToApi.uploadImageToApi(imgProductFile, Const.TYPE_FOOD, mProductModel.getImg());
                if (norImgRespond.isSuccess()) {
                    mProductModel.setImg(norImgRespond.getMsg());
                }
            }
            if (imgDetailProductFile != null) {
                Respond desImgRespond = UploadImageToApi.uploadImageToApi(imgDetailProductFile, Const.TYPE_FOOD, mProductModel.getImgDetail());
                if (desImgRespond.isSuccess()) {
                    mProductModel.setImgDetail(desImgRespond.getMsg());
                }
            }
            boolean resutl = ProductHelper.updateProduct(mProductModel);
            if (resutl) {
                stage.close();
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
        } catch (IOException ex) {
            Logger.getLogger(TestDemoController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
