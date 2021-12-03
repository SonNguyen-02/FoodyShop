package com.foodyshop.main;

import com.foodyshop.controller.TestDemoController;
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
    private static final String DEMO_UI = ROOT_FOLDER + "TestDemo.fxml";

    public void goToDemoUI() {
        redirectTo("Demo", DEMO_UI);
        TestDemoController demoController = fxLoader.getController();
        demoController.initStage(primaryStage);
    }
    // PAGE
    private static final String DASHBOARD_PAGE = ROOT_FOLDER + "DashboardPage.fxml";
    private static final String ORDER_PAGE = ROOT_FOLDER + "OrderPage.fxml";
    private static final String ORDER_DETAIL = ROOT_FOLDER + "Order_DetailPage.fxml";
      private static final String FEEDBACK_PAGE= ROOT_FOLDER + "FeedbackUI.fxml";

    // FORM
    private static final String ADD_ORDER_FORM = ROOT_FOLDER + "AddOrderForm.fxml";

    // Khai báo di chuyển giữa các màn hình
    public void goToLoginUI() {
        redirectTo("Login", LOGIN_UI);
    }

    public void goToMainLayout() {
        redirectTo("Admin", MAIN_UI);
    }
    public  void goToOrder(){
        redirectTo("Order", ORDER_PAGE);
    }
    // Load Page
    public void loadDashboard(BorderPane borderPane) {
        borderPane.setCenter(getParent(DASHBOARD_PAGE));
    }

    
    public void loadOrder(BorderPane borderPane) {
        borderPane.setCenter(getParent(ORDER_PAGE));
    }
     public void loadFeedback(BorderPane borderPane) {
        borderPane.setCenter(getParent(FEEDBACK_PAGE));
    }

    // Show Modal
    public void showAddOrder() {
        showModal("Add Order", ADD_ORDER_FORM);
    }
    public void showOrder_Detail() {
        showModal("Order Detail", ORDER_DETAIL);
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
