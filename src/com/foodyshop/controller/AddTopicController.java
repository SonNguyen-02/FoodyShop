/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.FileHelper;
import com.foodyshop.helper.TopicHelper;
import com.foodyshop.main.Const;
import com.foodyshop.main.Navigator;
import com.foodyshop.main.APIService;
import com.foodyshop.model.Respond;
import com.foodyshop.model.TopicModel;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import static com.foodyshop.main.Const.PLACEHOLDER_NO_IMG_PATH;

/**
 * FXML Controller class
 *
 * @author X PC
 */
public class AddTopicController implements Initializable {

    private Stage stage;

    private File imgTopicFile;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnChooseFile;

    @FXML
    private Button btnSave;

    @FXML
    private ImageView imgTopic;

    @FXML
    private TextField txtName;
    
    private IOnInsertTopicSuccess mIOnInsertTopicSuccess;

    public interface IOnInsertTopicSuccess {

        void callback(TopicModel topic);
    }

    public void initData(Stage stage, IOnInsertTopicSuccess mIOnInsertTopicSuccess) {
        this.stage = stage;
        this.mIOnInsertTopicSuccess = mIOnInsertTopicSuccess;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDefaultImg(btnChooseFile, imgTopic);
        btnSave.setOnMouseClicked(this::onClickSave);
        btnCancel.setOnMouseClicked(this::onClickCancel);
        btnChooseFile.setOnMouseClicked(this::onClickChooseFile);
    }

    private void onClickChooseFile(MouseEvent e) {
        FileChooser fileChooser = new FileChooser();
        FileHelper.configureFileImageChooser(fileChooser);
        File fileChoose = fileChooser.showOpenDialog(stage);
        imgTopicFile = fileChoose;
        if (fileChoose != null) {
            btnChooseFile.setText(fileChoose.getName());
            if (isImage(fileChoose.getName())) {
                imgTopic.setImage(new Image("file:" + fileChoose.getPath()));
                System.out.println(fileChoose.getPath());
            } else {
                setDefaultImg(btnChooseFile, imgTopic);
                System.out.println("File isn't image!");
            }
        } else {
            setDefaultImg(btnChooseFile, imgTopic);
        }
    }

    private void onClickSave(MouseEvent e) {
        String name = txtName.getText().trim();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        if (name.isEmpty()) {
            txtName.requestFocus();
            alert.setHeaderText("Please enter topic name");
            alert.show();
            return;
        }
        if(TopicHelper.isTopicNameExists(name)){
            txtName.requestFocus();
            alert.setHeaderText("This name is exists!");
            alert.show();
            return;
        }
        if (imgTopicFile == null) {
            alert.setHeaderText("Please choose a file img");
            alert.show();
            return;
        }
        if (!isImage(imgTopicFile.getName())) {
            setDefaultImg(btnChooseFile, imgTopic);
            alert.setHeaderText("File isn't image!");
            alert.show();
            return;
        }

        try {
                // call API
                Respond respond = APIService.uploadImageToApi(imgTopicFile, Const.TYPE_TOPIC);
                if (respond.isSuccess()) {
                    TopicModel topic = new TopicModel();
                    topic.setName(name);
                    topic.setImg(respond.getMsg());
                    // Insert to database
                    topic = TopicHelper.insertTopic(topic);
                    if (topic == null) {
                        Alert alerts = new Alert(Alert.AlertType.ERROR);
                        alerts.setTitle("Error");
                        alerts.setHeaderText("Add false");
                        alerts.show();
                    } else {
                        stage.close();
                        Alert alerts = new Alert(Alert.AlertType.INFORMATION);
                        alerts.setTitle("Success");
                        alerts.setHeaderText("Add success!");
                        alerts.show();
                        mIOnInsertTopicSuccess.callback(topic);
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
