/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.main;

import com.foodyshop.database.DBConnection;
import com.foodyshop.helper.BCrypt;
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

        // username admin@gmail.com
        // username staff@gmail.com
        // password azz123456
     //   Navigator.getInstance().goToMainLayout();
//        System.out.println(BCrypt.hashpw("azz123456", BCrypt.gensalt()));
  Navigator.getInstance().goToLoginUI();
//
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
