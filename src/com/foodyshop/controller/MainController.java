/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.main.Navigator;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
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

    private HBox currentPage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // auto load dashboard
        Navigator.getInstance().loadDashboard(rightLayout);
        // init currentPage item
        currentPage = (HBox) navBar.getChildren().get(1);
        currentPage.setCursor(Cursor.DEFAULT);
            
        // init nav event
        navBar.getChildren().forEach(node -> {
            if(node != navBar.getChildren().get(0)){
                HBox item = ((HBox) node);
                // set default opacity
                if(node != navBar.getChildren().get(1)){
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
    }
    
    private void activateNavItem(HBox item){
        item.setStyle("-fx-background-color: linear-gradient(#4568DC, #B06AB3); -fx-border-color: transparent transparent #fff transparent");
        item.setOpacity(NAV_ACTIVE_OPACITY);
    }
    
    private void inactivateNavItem(HBox item){
        item.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent #fff transparent");
        item.setOpacity(NAV_DEF_OPACITY);
    }

}
