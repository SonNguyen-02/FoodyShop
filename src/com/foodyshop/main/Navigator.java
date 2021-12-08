package com.foodyshop.main;

import com.foodyshop.controller.AddCategoryController;
import com.foodyshop.controller.AddCategoryController.IOnAddSuccess;
import com.foodyshop.controller.AddStaffController;
import com.foodyshop.controller.AddTopicController;
import com.foodyshop.controller.AddTopicController.IOnInsertTopicSuccess;

import com.foodyshop.controller.EditCategoryController;
import com.foodyshop.controller.EditOrderController;
import com.foodyshop.controller.EditOrderController.IOnUpdateOrderSuccess;
import com.foodyshop.controller.EditTopicController;
import com.foodyshop.controller.EditTopicController.IOnUpdateSuccess;
import com.foodyshop.controller.Order_DetailController;
import com.foodyshop.controller.TestDemoController;
import com.foodyshop.model.CategoryModel;
import com.foodyshop.model.FeedbackModel;
import com.foodyshop.model.OrderModel;
import com.foodyshop.model.Order_DetailModel;
import com.foodyshop.model.TopicModel;
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
    private static final String CUSTOMER_PAGE = ROOT_FOLDER + "CustomerPage.fxml";
    private static final String ORDER_DETAIL = ROOT_FOLDER + "Order_DetailPage.fxml";
    private static final String CATEGORY_PAGE = ROOT_FOLDER + "CategoryPage.fxml";
    private static final String TOPIC_PAGE = ROOT_FOLDER + "TopicPage.fxml";
    private static final String FEEDBACK_PAGE = ROOT_FOLDER + "FeedbackUI.fxml";
    private static final String STAFF_PAGE = ROOT_FOLDER + "StaffUI.fxml";

    // FORM
    private static final String ADD_CATEGORY_FORM = ROOT_FOLDER + "AddCategoryForm.fxml";
    private static final String EDIT_CATEGORY_FORM = ROOT_FOLDER + "EditCategoryForm.fxml";
    private static final String ADD_TOPIC_FORM = ROOT_FOLDER + "AddTopicForm.fxml";
    private static final String EDIT_TOPIC_FORM = ROOT_FOLDER + "EditTopicForm.fxml";
    private static final String EDIT_ORDER_FORM = ROOT_FOLDER + "EditOrderForm.fxml";
    private static final String ADD_STAFF = ROOT_FOLDER + "AddStaff.fxml";
    
    // Khai báo di chuyển giữa các màn hình
    public void goToLoginUI() {
        redirectTo("Login", LOGIN_UI);
    }

    public void goToMainLayout() {
        redirectTo("Admin", MAIN_UI);
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

    public void loadCustomer(BorderPane borderPane) {
        borderPane.setCenter(getParent(CUSTOMER_PAGE));
    }

    public void loadCategory(BorderPane borderPane) {
        borderPane.setCenter(getParent(CATEGORY_PAGE));
    }

    public void loadTopic(BorderPane borderPane) {
        borderPane.setCenter(getParent(TOPIC_PAGE));
    }

    public void loadStaff(BorderPane borderPane) {
        borderPane.setCenter(getParent(STAFF_PAGE));
    }

    // Show Modal
    public void showOrder_Detail(OrderModel order) {
        showModal("Order Detail", ORDER_DETAIL);
        Order_DetailController controller = fxLoader.getController();
        controller.initOrderModel(order);
    }

    public void showAddCategory(IOnAddSuccess mIOnAddSuccess) {
        showModal("Category ", ADD_CATEGORY_FORM);
        AddCategoryController controller = fxLoader.getController();
        controller.initCallback(mIOnAddSuccess);
    }

    public void showEditCategory(CategoryModel category) {
        showModal("Edit Category ", EDIT_CATEGORY_FORM);
        EditCategoryController controller = fxLoader.getController();
        controller.setData(category);
    }

    public void showAddTopic(IOnInsertTopicSuccess mIOnInsertTopicSuccess) {
        showModal("Topic ", ADD_TOPIC_FORM);
        AddTopicController controller = fxLoader.getController();
        controller.initData(modalStage, mIOnInsertTopicSuccess);
    }

    public void showEditTopic(TopicModel topic, IOnUpdateSuccess mIOnUpdateSuccess) {
        showModal("Topic ", EDIT_TOPIC_FORM);
        EditTopicController controller = fxLoader.getController();
        controller.initData(modalStage, topic, mIOnUpdateSuccess);
    }
    
    public void showEditOrder(OrderModel order,IOnUpdateOrderSuccess mIOnUpdateOrderSuccess) {
        showModal("Edit Order ", EDIT_ORDER_FORM);
        EditOrderController controller = fxLoader.getController();
        controller.setData(order,modalStage,mIOnUpdateOrderSuccess);
    }
    public void showAddStaff() {
        showModal("Add Staff ", ADD_STAFF);
        AddStaffController controller = fxLoader.getController();

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
