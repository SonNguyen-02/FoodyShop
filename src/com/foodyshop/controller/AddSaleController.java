/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.ProductHelper;
import com.foodyshop.helper.SaleHelper;
import com.foodyshop.main.Const;
import static com.foodyshop.main.Const.PLACEHOLDER_NO_IMG_PATH;
import com.foodyshop.main.Navigator;
import com.foodyshop.main.UploadImageToApi;
import com.foodyshop.model.ProductModel;
import com.foodyshop.model.Respond;
import com.foodyshop.model.SaleModel;
import static com.sun.javafx.fxml.expression.Expression.add;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;

/**
 * FXML Controller class
 *
 * @author APlaptop
 */
public class AddSaleController implements Initializable {

    private Stage stage;

    private File imgSaleFile;

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
    private TextArea txtContent;

    @FXML
    private TextField txtDiscount;

    @FXML
    private DatePicker dpStartDate;

    @FXML
    private DatePicker dpEndDate;

    private IOnInsertSaleSuccess mIOnInsertSaleSuccess;

    private ObservableList<ProductModel> productList;

    public interface IOnInsertSaleSuccess {

        void callback(SaleModel sale);
    }

    public void initDataSale(Stage stage, IOnInsertSaleSuccess mIOnInsertSaleSuccess) {
        setDefaultImg(btnChooseFile, imgSale);
        this.stage = stage;
        this.mIOnInsertSaleSuccess = mIOnInsertSaleSuccess;
        productList = ProductHelper.getAllProduct();
        if (productList != null && !productList.isEmpty()) {
            cbProductName.setItems(productList);
            cbProductName.setValue(productList.get(0));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btnCancel.setOnMouseClicked(this::onClickCancel);
        btnSave.setOnMouseClicked(this::onClickSave);
        btnChooseFile.setOnMouseClicked(this::onClickChooseFile);
    }

    private void onClickChooseFile(MouseEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose image to upload");
        File fileChoose = fileChooser.showOpenDialog(stage);
        imgSaleFile = fileChoose;
        if (fileChoose != null) {
            btnChooseFile.setText(fileChoose.getName());
            if (isImage(fileChoose.getName())) {
                imgSale.setImage(new Image("file:" + fileChoose.getPath()));
            } else {
                setDefaultImg(btnChooseFile, imgSale);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("File isn't image");
                alert.show();
            }
        } else {
            setDefaultImg(btnChooseFile, imgSale);
        }
    }

    private void onClickSave(MouseEvent e) {
        String discount = txtDiscount.getText().trim();
        String content = txtContent.getText().trim();
        String regaxDiscount = "^[1-9]{1,2}$";
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        if (discount.isEmpty()) {
            txtDiscount.requestFocus();
            alert.setHeaderText("Please enter Discount!!");
            alert.show();
            return;
        }
        if (!discount.matches(regaxDiscount)) {
            alert.setHeaderText("Please enter two or one number and different zero in Discount");
            alert.show();
            return;
        }
        if (content.isEmpty()) {
            txtContent.requestFocus();
            alert.setHeaderText("Please enter Content!!");
            alert.show();
            return;
        }
        if (imgSaleFile == null) {
            alert.setHeaderText("Please choose a file img");
            alert.show();
            return;
        }
        if (!isImage(imgSaleFile.getName())) {
            setDefaultImg(btnChooseFile, imgSale);
            alert.setHeaderText("File isn't image!");
            alert.show();
            return;
        }
        if (dpStartDate.getValue() == null) {
            dpStartDate.requestFocus();
            alert.setHeaderText("Please Select Day!");
            alert.show();
            return;
        }
        if (dpEndDate.getValue() == null) {
            dpEndDate.requestFocus();
            alert.setHeaderText("Please Select Day!");
            alert.show();
            return;
        }
        if (dpStartDate.getValue().isAfter(LocalDate.now())) {
        } else {
            alert.setHeaderText("Date Start more than now!");
            alert.show();
            return;
        }
        if (!dpEndDate.getValue().isBefore(dpStartDate.getValue()) && !dpEndDate.getValue().equals(dpStartDate.getValue())) {
        } else {
            alert.setHeaderText("Date End more than Date Start!");
            alert.show();
            return;
        }
        // call API
        try {
            Respond respond = UploadImageToApi.uploadImageToApi(imgSaleFile, Const.TYPE_SALE);
            if (respond.isSuccess()) {
                SaleModel sale = new SaleModel();
                sale.setDiscount(Integer.parseInt(discount));
                sale.setContent(content);
                sale.setProductId(cbProductName.getValue().getId());
                sale.setProductName(cbProductName.getValue().getName());
                sale.setStart_date(String.valueOf(dpStartDate.getValue()));
                sale.setEnd_date(String.valueOf(dpEndDate.getValue()));
                sale.setImg(respond.getMsg());
                // Insert to database
                sale = SaleHelper.insertSale(sale);

                if (sale == null) {
                    alert.setHeaderText("Add false");
                    alert.show();
                } else {
                    stage.close();
                    Alert alerts = new Alert(Alert.AlertType.INFORMATION);
                    alerts.setTitle("Success");
                    alerts.setHeaderText("Add success!");
                    alerts.show();
                    mIOnInsertSaleSuccess.callback(sale);
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

    private void setDefaultImg(Button btnChoose, ImageView imgView) {
        btnChoose.setText("Choose files");
        imgView.setImage(new Image("file:" + PLACEHOLDER_NO_IMG_PATH));

    }
}
