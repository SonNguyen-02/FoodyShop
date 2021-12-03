/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.FormHelper;
import com.foodyshop.helper.TopicHelper;
import com.foodyshop.main.Navigator;
import com.foodyshop.model.CategoryModel;
import com.foodyshop.model.TopicModel;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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
public class AddTopicController implements Initializable {

    private ISOK mISOK;
    private Stage stage;

    

    public interface ISOK {

        void OK(TopicModel topicModel);
    }

    public void initTopic(ISOK mISOK) {
        this.mISOK = mISOK;
    }
    private File fileChoose;
    @FXML
    private TextField txtName;
    
    @FXML
    private TextField txtID;
    @FXML
    private ImageView img;

    @FXML
    private Button btnChooseFile;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    @FXML
    private Label lbChooseFile;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
       btnSave.setOnMouseClicked(this::onClickSave);
       btnCancel.setOnMouseClicked(this::onClickCancel);
       btnChooseFile.setOnMouseClicked(this::onClickChooseFile);
    }    
    private void onClickSave(MouseEvent e) {
        if ( txtID.getText().equals("") && txtName.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Name and id must be entered");
            alert.show();
            return;}           
//        } else {           
//            TopicModel topicModel = TopicHelper.insertTopic(txtName.getText(),img.getImage(), txtID.getText());
//            if (topicModel != null) {
//                mISOK.OK(topicModel);
//                Navigator.getInstance().getModalStage().close();
//                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setTitle("SUCCESS");
//                alert.setHeaderText("Insert success");
//                alert.show();
//            } else {
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("ERROR");
//                alert.setHeaderText("Insert false!");
//                alert.show();
//            }
//        }
    }
    
    private void onClickCancel(MouseEvent e){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close");
        alert.setHeaderText("Do you want close?");
        alert.showAndWait().ifPresent(btnType -> {
            if(btnType == ButtonType.OK){
                Navigator.getInstance().getModalStage().close();
            }
        });
        
    }
    private void onClickChooseFile(MouseEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose image to upload");
        fileChoose = fileChooser.showOpenDialog(stage);
        if (fileChoose != null) {
            btnChooseFile.setText(fileChoose.getName());
            if (isImage(fileChoose.getName())) {
                img.setImage(new Image("file:" + fileChoose.getPath()));
                FormHelper.resetErr(btnChooseFile, lbChooseFile);
            } else {
                img.setImage(null);
                lbChooseFile.setText("The file is not an image");
            }
        } else {
            img.setImage(null);
            btnChooseFile.setText("Choose File");
            FormHelper.resetErr(btnChooseFile, lbChooseFile);
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
