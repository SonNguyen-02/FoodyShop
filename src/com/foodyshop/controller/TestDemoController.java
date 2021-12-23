/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.FileHelper;
import com.foodyshop.helper.FormHelper;
import com.foodyshop.main.Const;
import com.foodyshop.main.UploadImageToApi;
import com.foodyshop.model.Respond;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author N.C.Son
 */
public class TestDemoController implements Initializable {

    @FXML
    private Button btnChooseFile, btnCallInsert, btnCallDelete;
    @FXML
    private TextField txtFileName;
    @FXML
    private Label lbChooseFile, lbResult;
    @FXML
    private ImageView imgDemo, imgRespond;

    private Stage stage;

    private File fileChoose;

    public void initStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(imgRespond);
        String imgSource = "https://tse4.mm.bing.net/th?id=OIP.wX0A09xQ0T-5ZkJYg__SywHaJ4&pid=Api&P=0&w=150&h=200";
        imgRespond.setImage(new Image(imgSource, true));

        btnChooseFile.setOnMouseClicked(this::onClickChooseFile);
        btnCallInsert.setOnMouseClicked(this::onClickCallApiInsert);
        btnCallDelete.setOnMouseClicked(this::onClickCallApiDelete);
    }

    private void onClickChooseFile(MouseEvent e) {
        FileChooser fileChooser = new FileChooser();
        FileHelper.configureFileImageChooser(fileChooser);
        fileChoose = fileChooser.showOpenDialog(stage);
        if (fileChoose != null) {
            btnChooseFile.setText(fileChoose.getName());
            if (isImage(fileChoose.getName())) {
                imgDemo.setImage(new Image("file:" + fileChoose.getPath()));
                FormHelper.resetErr(btnChooseFile, lbChooseFile);
            } else {
                imgDemo.setImage(null);
                lbChooseFile.setText("The file is not an image");
            }
        } else {
            imgDemo.setImage(null);
            btnChooseFile.setText("Choose File");
            FormHelper.resetErr(btnChooseFile, lbChooseFile);
        }
    }

    private void onClickCallApiInsert(MouseEvent e) {
        try {
            if (fileChoose != null && isImage(fileChoose.getName())) {
                Respond respond = UploadImageToApi.uploadImageToApi(fileChoose, Const.TYPE_TOPIC, "abc.def");
                System.out.println(respond);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("File isn't image!");
                alert.show();
            }
        } catch (IOException ex) {
            Logger.getLogger(TestDemoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void onClickCallApiDelete(MouseEvent e) {
        try {
            String imgName = txtFileName.getText().trim();
            if (!imgName.isEmpty()) {
                Respond respond = UploadImageToApi.removeImageFromApi(Const.TYPE_TOPIC, imgName);
                lbResult.setText(respond.getMsg());
            } else {
                txtFileName.requestFocus();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Enter file name!");
                alert.show();
            }
        } catch (IOException ex) {
            Logger.getLogger(TestDemoController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
}
