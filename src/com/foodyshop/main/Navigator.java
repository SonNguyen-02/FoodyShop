package com.foodyshop.main;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Navigator {

    private static Navigator instance;
    private Stage primaryStage;     // dùng để di chuyển các màn hình
    private Stage modalStage;       // dùng để hiện thị thêm một màn hình phụ có thể truyền vào init của class

    private FXMLLoader fxLoader;    // dùng để lấy instance của controller sau khi load

    // <editor-fold defaultstate="collapsed" desc="Declare view">
    // folder chứa các view
    private static final String ROOT_FOLDER = "/com/foodyshop/view/";

    private static final String LOGIN_UI = ROOT_FOLDER + "LoginUI.fxml";
    private static final String MAIN_UI = ROOT_FOLDER + "MainUI.fxml";

    private static final String DASHBOARD_UI = ROOT_FOLDER + "DashboardUI.fxml";

    // Khai báo di chuyển giữa các màn hình
    public void goToLoginUI() {
        redirectTo("Product Manager", LOGIN_UI);
    }

    public void goToMainLayout(String name) {
        redirectTo(name, MAIN_UI);
    }

    public void loadDashboard(BorderPane borderPane) {
        borderPane.setCenter(getParent(DASHBOARD_UI));
    }

    // </editor-fold> 
    private Navigator() {
    }

    public static Navigator getInstance() {
        if (instance == null) {
            instance = new Navigator();
        }
        return instance;
    }

    public void redirectTo(String title, String URL) {
        try {
            fxLoader = new FXMLLoader(getClass().getResource(URL));
            Parent root = fxLoader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle(title);
            primaryStage.centerOnScreen();
            if (!primaryStage.isShowing()) {
                primaryStage.show();
            }
        } catch (IOException ex) {
            Logger.getLogger(Navigator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showModal(String title, String URL) {
        try {
            modalStage = new Stage();
            fxLoader = new FXMLLoader(getClass().getResource(URL));
            Parent root = fxLoader.load();
            Scene scene = new Scene(root);
            modalStage.setScene(scene);
            modalStage.setTitle(title);
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.show();
        } catch (IOException ex) {
            Logger.getLogger(Navigator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Parent getParent(String URL) {
        try {
            fxLoader = new FXMLLoader(getClass().getResource(URL));
            return fxLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(Navigator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage modalStage) {
        this.primaryStage = modalStage;
    }

    public Stage getModalStage() {
        return modalStage;
    }

    public void setModalStage(Stage modalStage) {
        this.modalStage = modalStage;
    }

}
