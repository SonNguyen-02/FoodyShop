/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.CustomerDetailHelper;
import static com.foodyshop.main.Config.IMG_AVATAR_DIR;
import static com.foodyshop.main.Const.PLACEHOLDER_NO_IMG_PATH;
import com.foodyshop.main.Navigator;
import com.foodyshop.model.CustomerDetailModel;
import com.foodyshop.model.CustomerModel;
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
import javafx.scene.text.TextAlignment;

/**
 * FXML Controller class
 *
 * @author APlaptop
 */
public class CustomerDetailController implements Initializable {

    @FXML
    private TableView<CustomerDetailModel> tblCustomerDetail;

    @FXML
    private TableColumn<CustomerDetailModel, Integer> tcStt;

    @FXML
    private TableColumn<CustomerDetailModel, String> tcOrderCode;

    @FXML
    private TableColumn<CustomerDetailModel, Integer> tcTotalPrice;

    @FXML
    private TableColumn<CustomerDetailModel, String> tcCreated;

    @FXML
    private TableColumn<CustomerDetailModel, String> tcStatusOrder;

    @FXML
    private ImageView imgView;

    @FXML
    private Label lbCustomerName;

    @FXML
    private Label lbNote, lbFeedback;

    @FXML
    private Label lbAddress;

    @FXML
    private Label lbPhone;

    @FXML
    private Label lbTotalOrder;
    
    @FXML
    private Button btnCancel;


    private CustomerModel mCustomer;

    ObservableList<CustomerDetailModel> listCustomerDetail = FXCollections.observableArrayList();

    public void initCustomerModel(CustomerModel customer) {
        mCustomer = customer;
        lbCustomerName.setText(customer.getName());
        lbAddress.setText(customer.getAddress());
        lbPhone.setText(customer.getPhone());
        Image image = new Image(IMG_AVATAR_DIR + customer.getImg(), 80, 80, false, true, true);
        if(IMG_AVATAR_DIR != null){
            imgView.setImage(image);
        }
        
        listCustomerDetail = CustomerDetailHelper.getAllCustomerDetail(customer);
        tblCustomerDetail.setItems(listCustomerDetail);
        lbTotalOrder.setText(String.valueOf(listCustomerDetail.size()) + " order");
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setDefaultImg(imgView);
        btnCancel.setOnMouseClicked(this::onClickCancel);
        lbFeedback.setWrapText(true);
        lbFeedback.setTextAlignment(TextAlignment.JUSTIFY);
        tcStt.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper((tblCustomerDetail.getItems().indexOf(cellData.getValue()) + 1) + ""));
        tcOrderCode.setCellValueFactory(cellValue -> cellValue.getValue().getOrderCode());
        tcTotalPrice.setCellValueFactory(cellValue -> cellValue.getValue().getTotalMoney());
        tcCreated.setCellValueFactory(cellValue -> cellValue.getValue().getCreated());
        tcStatusOrder.setCellValueFactory(cellValue -> cellValue.getValue().getStatusVal());

        tblCustomerDetail.setRowFactory(v -> {
            final TableRow<CustomerDetailModel> row = new TableRow<>();
            row.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                final int index = row.getIndex();
                if (e.getButton().equals(MouseButton.PRIMARY) && index >= 0 && index < tblCustomerDetail.getItems().size()) {
//                    if (e.getClickCount() == 2) {
//                        initOpenFileOrFolder();
//                        return;
//                    }
                    if (e.getClickCount() == 1) {
                        lbFeedback.setText(row.getItem().getContent());
                        lbNote.setText(row.getItem().getNote());
                    }
                }
            });
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
                final int index = row.getIndex();
                if (index < 0 || index >= tblCustomerDetail.getItems().size()) {
                    tblCustomerDetail.getSelectionModel().clearSelection();
                    lbFeedback.setText("Choose one!");
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
                Navigator.getInstance().getModalStage().close();
            }
        });
    }
    
     private void setDefaultImg(ImageView imgView) {
        imgView.setImage(new Image("file:" + PLACEHOLDER_NO_IMG_PATH));
    }
}
