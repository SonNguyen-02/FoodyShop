/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.FormHelper;
import com.foodyshop.main.Config;
import com.foodyshop.main.Const;
import com.foodyshop.main.UploadImageToApi;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author N.C.Son
 */
public class TestDemoController implements Initializable {

    @FXML
    private Button btnChooseFile, btnCall;
    @FXML
    private Label lbChooseFile;
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
        btnCall.setOnMouseClicked(this::onClickCallApi);
    }

    private void onClickChooseFile(MouseEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose image to upload");
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
    
    private void onClickCallApi(MouseEvent e){
        try {
            UploadImageToApi.uploadImageToApi(fileChoose, Const.TYPE_FOOD, "211127c9b4ec1f19109d20c2ff239f38.jpg");
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
