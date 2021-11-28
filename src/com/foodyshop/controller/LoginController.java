/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.controller;

import com.foodyshop.helper.StaffHelper;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author N.C.Son
 */
public class LoginController implements Initializable {

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    //login
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
//        StaffHelper.getAll();
//loginnn
    }    
    
//      @FXML
//    void onclickConfirm(ActionEvent event) {
//        if (FormHelper.IsEmptyField(txtCode, lbCode, "Please enter the product code")
//                || FormHelper.IsEmptyField(txtName, lbName, "Please enter the product name")
//                || FormHelper.IsEmptyField(dpManufacturing, lbManufacturing, "Please enter date manufacturing")) {
//            return;
//        }
//        if (cbType.getValue().equals(ProductModel.ProductType.FOOD_PRODUCT.typeValue)) {
//            if (FormHelper.IsEmptyField(dpExpired, lbExpired, "Please enter date expired")) {
//                return;
//            }
//        }
//        if (product == null || !txtCode.getText().equals(product.getCode())) {
//            if (ProductHelper.findProductByCode(txtCode.getText()) != null) {
//                txtCode.requestFocus();
//                FormHelper.showErr(txtCode, lbCode, "The product code is exists");
//                return;
//            }
//        }
//        if (!imgPath.isEmpty()) {
//            if (!isImage(imgPath)) {
//                btnChooseImage.requestFocus();
//                System.out.println("check");
//                lbChooseImage.setText("The file is not an image");
//                return;
//            }
//        }
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Confirm");
//        alert.setHeaderText("Click ok to save product");
//        if (alert.showAndWait().filter(button -> !button.equals(ButtonType.OK)).isPresent()) {
//            return;
//        }
//        if (action.equals(Action.ADD)) {
//            ProductModel newProduct = new ProductModel();
//            newProduct.setCode(txtCode.getText());
//            newProduct.setName(txtName.getText());
//            newProduct.setType(ProductModel.ProductType.getProductType(cbType.getValue()));
//            newProduct.setManufacturingDate(dpManufacturing.getValue().toString());
//            if (cbType.getValue().equals(ProductModel.ProductType.FOOD_PRODUCT.typeValue)) {
//                newProduct.setExpiredDate(dpExpired.getValue().toString());
//            } else {
//                newProduct.setExpiredDate(null);
//            }
//            if (!imgPath.equals("")) {
//                // check có trùng tên k
//                String newName = FileHelper.checkFileAndRename(imgPath, Config.IMAGE_FOLDER);
//                // copy vào image folder
//                FileHelper.copyDiretoryAndFiles(imgPath, Config.IMAGE_FOLDER);
//                newProduct.setImgName(Paths.get(newName).getFileName().toString());
//            }
//            ProductHelper.addProduct(newProduct);
//            productManagerController.addProduct(newProduct);
//        } else {
//            product.setCode(txtCode.getText());
//            product.setName(txtName.getText());
//            product.setType(ProductModel.ProductType.getProductType(cbType.getValue()));
//            product.setManufacturingDate(dpManufacturing.getValue().toString());
//            if (cbType.getValue().equals(ProductModel.ProductType.FOOD_PRODUCT.typeValue)) {
//                product.setExpiredDate(dpExpired.getValue().toString());
//            } else {
//                product.setExpiredDate(null);
//            }
//            if (!Paths.get(imgPath).getFileName().toString().equals(product.getImageName())) {
//                if (product.getImageName() != null) {
//                    //xóa ảnh
//                    FileHelper.deleteDiretoryAndFiles(Paths.get(Config.IMAGE_FOLDER + product.getImageName()).toAbsolutePath().toString());
//                }
//                if (!imgPath.equals("")) {
//                    // check có trùng tên k
//                    String newName = FileHelper.checkFileAndRename(imgPath, Config.IMAGE_FOLDER);
//                    // copy vào image folder
//                    FileHelper.copyDiretoryAndFiles(imgPath, Config.IMAGE_FOLDER);
//                    product.setImgName(Paths.get(newName).getFileName().toString());
//                } else {
//                    product.setImgName(null);
//                }
//            }
//            ProductHelper.editProduct(product);
//            productManagerController.refreshTbl();
//        }
//        stage.close();
//    }

}
