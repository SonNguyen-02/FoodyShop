/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.main;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author N.C.Son
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Navigator.getInstance().setPrimaryStage(primaryStage);
        Navigator.getInstance().goToLoginUI();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // for (){
        // print(123)
        //}
        //11111
//        123125123
        launch(args);
    }
//<<<<<<< HEAD
////    hello cac chau nha ba la ba Tan ve loc
//=======
////    hello cac chau nha
//    //hiiii
//>>>>>>> origin/master
    
//   henho
}
