/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.FormHelper;
import com.foodyshop.helper.TopicHelper;
import static com.foodyshop.main.Config.IMG_TOPIC_DIR;
import com.foodyshop.main.Const;
import com.foodyshop.main.Navigator;
import com.foodyshop.main.UploadImageToApi;
import com.foodyshop.model.CategoryModel;
import com.foodyshop.model.Respond;
import com.foodyshop.model.TopicModel;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
public class EditTopicController implements Initializable {

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

    @FXML
    private ComboBox<String> cbStatus;

    private TopicModel mTopicModel;

    public void initData(Stage stage, TopicModel topic) {
        this.stage = stage;
        mTopicModel = topic;
        txtName.setText(topic.getName());

        Image image = new Image(IMG_TOPIC_DIR + topic.getImg(), 300, 300, false, true);
        imgTopic.setImage(image);
        cbStatus.setItems(FXCollections.observableArrayList(TopicModel.SHOW, TopicModel.HIDDEN));
        cbStatus.setValue(topic.getStatusVal().get());
        
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
        fileChooser.setTitle("Choose image to upload");
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
        if (imgTopicFile != null) {
            if (!isImage(imgTopicFile.getName())) {
                setDefaultImg(btnChooseFile, imgTopic);
                alert.setHeaderText("File isn't image!");
                alert.show();
                return;
            }
        }

        try {
            mTopicModel.setName(name);
            mTopicModel.setStatus(cbStatus.getValue());
            if (imgTopicFile != null) {
                // call API
                Respond respond = UploadImageToApi.uploadImageToApi(imgTopicFile, Const.TYPE_TOPIC, mTopicModel.getImg());
                if (respond.isSuccess()) {
                    mTopicModel.setImg(respond.getMsg());
                    // Update to database
                } else {
                    Alert alerts = new Alert(Alert.AlertType.ERROR);
                    alerts.setTitle("Error");
                    alerts.setHeaderText("Add false");
                    alerts.show();
                    return;
                }
            }
            boolean resutl = TopicHelper.updateTopic(mTopicModel);
            if (resutl) {
                stage.close();
                Alert alerts = new Alert(Alert.AlertType.INFORMATION);
                alerts.setTitle("Success");
                alerts.setHeaderText("Edit success!");
                alerts.show();
                Navigator.getInstance().getModalStage().close();
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
        imgView.setImage(new Image("file:" + PLACEHOLDER_NO_IMG_PATH));
    }

}
