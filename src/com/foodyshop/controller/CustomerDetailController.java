
package com.foodyshop.controller;

import com.foodyshop.helper.OrderHelper;
import static com.foodyshop.main.Config.IMG_AVATAR_DIR;
import com.foodyshop.main.Const;
import com.foodyshop.main.Navigator;
import com.foodyshop.model.CustomerModel;
import com.foodyshop.model.OrderModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author APlaptop
 */
public class CustomerDetailController implements Initializable {

    private Stage mStage;
    
    @FXML
    private TableView<OrderModel> tblCustomerDetail;

    @FXML
    private TableColumn<OrderModel, Integer> tcStt;

    @FXML
    private TableColumn<OrderModel, String> tcOrderCode;

    @FXML
    private TableColumn<OrderModel, Integer> tcTotalPrice;

    @FXML
    private TableColumn<OrderModel, String> tcCreated;

    @FXML
    private TableColumn<OrderModel, String> tcStatusOrder;

    @FXML
    private ImageView imgView;

    @FXML
    private Label lbCustomerName;

    @FXML
    private Label lbNote;

    @FXML
    private Label lbAddress;

    @FXML
    private Label lbPhone;

    @FXML
    private Label lbTotalOrder;

    @FXML
    private Button btnCancel,btnOrderDetail;

    private CustomerModel mCustomer;

    ObservableList<OrderModel> listOrder = FXCollections.observableArrayList();

    public void initCustomerModel(CustomerModel customer, Stage stage) {
        mCustomer = customer;
        mStage = stage;
        lbCustomerName.setText(customer.getName());
        lbAddress.setText(customer.getAddress());
        lbPhone.setText(customer.getPhone());

        String url = IMG_AVATAR_DIR + mCustomer.getImg();
        Image image = new Image(url, 80, 80, false, true, true);
        imgView.setImage(image);
        image.errorProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                imgView.setImage(Const.NO_IMAGE_OBJ);
            }
        });

        listOrder = OrderHelper.getListOrderByCustomer(customer);
        tblCustomerDetail.setItems(listOrder);
        lbTotalOrder.setText(String.valueOf(listOrder.size()) + " order");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btnOrderDetail.setOnMouseClicked(this::onclickShowOrderDetail);
        btnCancel.setOnMouseClicked(this::onClickCancel);
        tcStt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper((tblCustomerDetail.getItems().indexOf(cellData.getValue()) + 1) + ""));
        tcOrderCode.setCellValueFactory(cellValue -> cellValue.getValue().getOrderCodeProperty());
        tcTotalPrice.setCellValueFactory(cellValue -> cellValue.getValue().getTotalMoneyProperty());
        tcCreated.setCellValueFactory(cellValue -> cellValue.getValue().getCreatedProperty());
        tcStatusOrder.setCellValueFactory(cellValue -> cellValue.getValue().getStatusVal());

        tblCustomerDetail.setRowFactory(v -> {
            final TableRow<OrderModel> row = new TableRow<>();
            row.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                final int index = row.getIndex();
                if (e.getButton().equals(MouseButton.PRIMARY) && index >= 0 && index < tblCustomerDetail.getItems().size()) {
                    if (e.getClickCount() == 1) {
                        lbNote.setText(row.getItem().getNote());
                    }
                }
            });
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
                final int index = row.getIndex();
                if (index < 0 || index >= tblCustomerDetail.getItems().size()) {
                    tblCustomerDetail.getSelectionModel().clearSelection();
                    lbNote.setText("");
                    event.consume();
                }
            });
            return row;
        });
    }

    private void onClickCancel(MouseEvent e) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close");
        alert.setHeaderText("Do you want close?");
        alert.showAndWait().ifPresent(btnType -> {
            if (btnType == ButtonType.OK) {
                mStage.close();
            }
        });
    }
    
    private void onclickShowOrderDetail(MouseEvent e) {
        OrderModel order = tblCustomerDetail.getSelectionModel().getSelectedItem();
        if (order != null) {
            Navigator.getInstance().showOrder_Detail(order);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("You Must choose!!!");
            alert.show();
        }
    }

}