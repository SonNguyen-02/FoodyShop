/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.main.Navigator;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 *
 * @author N.C.Son
 */
public class MainController implements Initializable {

    private static final double NAV_DEF_OPACITY = 0.65;
    private static final double NAV_ACTIVE_OPACITY = 1;

    @FXML
    private BorderPane rightLayout;

    @FXML
    private GridPane navBar;

    @FXML
    private HBox dashboardPage, orderPage, feedbackPage, customerPage, salePage, productPage, categoryPage, topicPage, revenuePage, staffPage, accountSettingPage;

    private HBox currentPage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // auto load dashboard
        Navigator.getInstance().loadDashboard(rightLayout);
        // init currentPage item
        currentPage = (HBox) navBar.getChildren().get(1);
        currentPage.setCursor(Cursor.DEFAULT);
//        deleteRow(navBar, 9);
//        deleteRow(navBar, 9);
        navBar.getChildren().forEach(node -> {
            if (node != navBar.getChildren().get(0)) {
                HBox item = ((HBox) node);
                // set default opacity
                if (node != navBar.getChildren().get(1)) {
                    node.setOpacity(NAV_DEF_OPACITY);
                }
                // set onclick
                item.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                    inactivateNavItem(currentPage);
                    currentPage.setCursor(Cursor.HAND);
                    currentPage = item;
                    currentPage.setCursor(Cursor.DEFAULT);
                    activateNavItem(currentPage);
                });
                // set on mouse in
                item.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> {
                    if (item != currentPage) {
                        activateNavItem(item);
                    }
                });
                // set on mouse out
                item.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> {
                    if (item != currentPage) {
                        inactivateNavItem(item);
                    }
                });
            }
        });
        initClickNavItem();
    }

    private void deleteRow(GridPane grid, final int row) {
        Set<Node> deleteNodes = new HashSet<>();
        grid.getChildren().forEach((child) -> {
            // get index from child
            Integer rowIndex = GridPane.getRowIndex(child);

            // handle null values for index=0
            int r = rowIndex == null ? 0 : rowIndex;

            if (r > row) {
                // decrement rows for rows after the deleted row
                GridPane.setRowIndex(child, r - 1);
            } else if (r == row) {
                // collect matching rows for deletion
                deleteNodes.add(child);
            }
        });
        // remove nodes from row
        grid.getChildren().removeAll(deleteNodes);
    }

    private void activateNavItem(HBox item) {
        item.setStyle("-fx-background-color: linear-gradient(#4568DC, #B06AB3); -fx-border-color: transparent transparent #fff transparent");
        item.setOpacity(NAV_ACTIVE_OPACITY);
    }

    private void inactivateNavItem(HBox item) {
        item.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent #fff transparent");
        item.setOpacity(NAV_DEF_OPACITY);
    }

      
    private void initClickNavItem() {
        dashboardPage.setOnMouseClicked(e -> {
            Navigator.getInstance().loadDashboard(rightLayout);
        });
        orderPage.setOnMouseClicked(e -> {
            Navigator.getInstance().loadOrder(rightLayout);
        });   
        customerPage.setOnMouseClicked(e ->{
             Navigator.getInstance().loadCustomer(rightLayout);
        });
        feedbackPage.setOnMouseClicked(e ->{
           Navigator.getInstance().loadFeedback(rightLayout);

        }); 
        categoryPage.setOnMouseClicked(e ->{
            Navigator.getInstance().loadCategory(rightLayout);
        });
        topicPage.setOnMouseClicked(e ->{
            Navigator.getInstance().loadTopic(rightLayout);
        });
        staffPage.setOnMouseClicked(e ->{
            Navigator.getInstance().loadStaff(rightLayout);
        });
        productPage.setOnMouseClicked(e ->{
            Navigator.getInstance().loadProduct(rightLayout);
        });
    }

}
