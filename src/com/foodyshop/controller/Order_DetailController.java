/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.Order_DetailHelper;
import com.foodyshop.model.CustomerModel;
import com.foodyshop.model.FeedbackModel;
import com.foodyshop.model.OrderModel;
import com.foodyshop.model.Order_DetailModel;
import com.foodyshop.model.ProductModel;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

/**
 * FXML Controller class
 *
 * @author APlaptop
 */
public class Order_DetailController implements Initializable {

    private OrderModel mOrder;
    private Order_DetailModel mOrder_detail;

    @FXML
    private Label lbOrder_Code, lbTotalPrice, lbTotalProduct, lbName, lbPhone, lbAddress, lbFeedback, lbNote;

    @FXML
    private TableView<Order_DetailModel> tblOrder_detail;

    @FXML
    private TableColumn<Order_DetailModel, Integer> tcStt;

    @FXML
    private TableColumn<Order_DetailModel, String> tcProduct;

    @FXML
    private TableColumn<Order_DetailModel, Integer> tcNumber;

    @FXML
    private TableColumn<Order_DetailModel, Integer> tcPrice;

    @FXML
    private TableColumn<Order_DetailModel, Integer> tcDiscount;
    /**
     * Initializes the controller class.
     */

    ObservableList<Order_DetailModel> listOrder_Detail = FXCollections.observableArrayList();
    DecimalFormat formatter = new DecimalFormat("###,###,###");

    public void initOrderModel(OrderModel order) {
        mOrder = order;
        String fomatter1 = formatter.format(order.getTotalMoney());
        lbName.setText(order.getName());
        lbPhone.setText(order.getPhone());
        lbNote.setText(order.getNote());
        lbAddress.setText(order.getAddress());
        lbTotalPrice.setText(fomatter1 + " VNĐ");
        lbOrder_Code.setText(order.getOrderCode());
        listOrder_Detail = Order_DetailHelper.getAllOrder_Detail(order);
        tblOrder_detail.setItems(listOrder_Detail);
//        int size = listOrder_Detail.size();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        lbFeedback.setWrapText(true);
        //Setting the alignment to the label
        lbFeedback.setTextAlignment(TextAlignment.JUSTIFY);
        tcStt.setCellValueFactory(cellValue -> cellValue.getValue().getIdProperty());
        tcProduct.setCellValueFactory(cellValue -> cellValue.getValue().getProduct_nameProperty());
        tcNumber.setCellValueFactory(cellValue -> cellValue.getValue().getNumberProperty());
        tcPrice.setCellValueFactory(cellValue -> cellValue.getValue().getPriceProperty());
        tcDiscount.setCellValueFactory(cellValue -> cellValue.getValue().getDiscountProperty());
        tblOrder_detail.setRowFactory(v -> {
            final TableRow<Order_DetailModel> row = new TableRow<>();
            row.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                final int index = row.getIndex();
                if (e.getButton().equals(MouseButton.PRIMARY) && index >= 0 && index < tblOrder_detail.getItems().size()) {
//                    if (e.getClickCount() == 2) {
//                        initOpenFileOrFolder();
//                        return;
//                    }
                    if (e.getClickCount() == 1) {
                        lbFeedback.setText(row.getItem().getContent());
                    }
                }
            });
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
                final int index = row.getIndex();
                if (index < 0 || index >= tblOrder_detail.getItems().size()) {
                    tblOrder_detail.getSelectionModel().clearSelection();
                    lbFeedback.setText("");
                    event.consume();
                }
            });
            return row;
        });
    }
}
