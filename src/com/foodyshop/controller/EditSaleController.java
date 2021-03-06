/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.FileHelper;
import com.foodyshop.helper.SaleHelper;
import static com.foodyshop.main.Config.IMG_SALE_DIR;
import com.foodyshop.main.Const;
import static com.foodyshop.main.Const.PLACEHOLDER_NO_IMG_PATH;
import com.foodyshop.main.Navigator;
import com.foodyshop.main.APIService;
import com.foodyshop.model.ProductModel;
import com.foodyshop.model.Respond;
import com.foodyshop.model.SaleModel;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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
    
    private Image mImageDefault;

    private File imgSaleFile;

    @FXML
    private TextArea txtContent;

    @FXML
    private TextField txtDiscount;

    @FXML
    private DatePicker dpStartDate;

    @FXML
    private DatePicker dpEndDate;
    
    @FXML
    private Label lbProductName;

    private SaleModel mSaleModel;

    private Stage stage;

    public void initData(Stage stage, SaleModel sale) {
        this.stage = stage;
        mSaleModel = sale;
   
        
        String url = IMG_SALE_DIR + mSaleModel.getImg();
        mImageDefault = new Image(url, 230, 230, false, true, true);
        imgSale.setImage(mImageDefault);
        mImageDefault.errorProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                mImageDefault = new Image("file:" + PLACEHOLDER_NO_IMG_PATH);
                imgSale.setImage(mImageDefault);
            }
        });

        txtContent.setText(sale.getContent());
        txtDiscount.setText(sale.getDiscount().toString());
        dpStartDate.setValue(LocalDate.parse(sale.getStart_date()));
        dpEndDate.setValue(LocalDate.parse(sale.getEnd_date()));
        lbProductName.setText(sale.getProductName());        
    }
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btnSave.setOnMouseClicked(this::onClickSave);
        btnCancel.setOnMouseClicked(this::onClickCancel);
        btnChooseFile.setOnMouseClicked(this::onClickChooseFile);
    }

    private void onClickChooseFile(MouseEvent e) {
        FileChooser fileChooser = new FileChooser();
        FileHelper.configureFileImageChooser(fileChooser);
        File fileChoose = fileChooser.showOpenDialog(stage);
        imgSaleFile = fileChoose;
        if (fileChoose != null) {
            btnChooseFile.setText(fileChoose.getName());
            if (isImage(fileChoose.getName())) {
                imgSale.setImage(new Image("file:" + fileChoose.getPath()));
                System.out.println(fileChoose.getPath());
            } else {
                setDefaultImg(btnChooseFile, imgSale);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("File isn't image!");
                alert.show();
            }
        } else {
            setDefaultImg(btnChooseFile, imgSale);
        }
    }

    private void onClickSave(MouseEvent e) {
        String discount = txtDiscount.getText().trim();
        String content = txtContent.getText().trim();
        String regaxDiscount = "^[^0][0-9]{0,1}$";
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        if (discount.isEmpty()) {
            txtDiscount.requestFocus();
            alert.setHeaderText("Please enter Discount!");
            alert.show();
            return;
        }
        if (!discount.matches(regaxDiscount)) {
            alert.setHeaderText("Please enter two or one number and different zero before!");
            alert.show();
            return;
        }
        if (content.isEmpty()) {
            txtContent.requestFocus();
            alert.setHeaderText("Please enter Content!!");
            alert.show();
            return;
        }
        if (imgSale == null) {
            alert.setHeaderText("Please choose a file img");
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
        if (imgSaleFile != null) {
            if (!isImage(imgSaleFile.getName())) {
                setDefaultImg(btnChooseFile, imgSale);
                alert.setHeaderText("File isn't image!");
                alert.show();
                return;
            }
        }
        try {
            mSaleModel.setDiscount(Integer.parseInt(discount));
            mSaleModel.setContent(content);
            mSaleModel.setStart_date(String.valueOf(dpStartDate.getValue()));
            mSaleModel.setEnd_date(String.valueOf(dpEndDate.getValue()));
            if (imgSaleFile != null) {
                // call API
                Respond respond = APIService.uploadImageToApi(imgSaleFile, Const.TYPE_SALE, mSaleModel.getImg());
                if (respond.isSuccess()) {
                    mSaleModel.setImg(respond.getMsg());
                }
            }
            boolean resutl = SaleHelper.updateSale(mSaleModel);
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
        imgView.setImage(mImageDefault);
    }

}
