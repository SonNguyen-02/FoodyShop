package com.foodyshop.controller;

import com.foodyshop.helper.CategoryHelper;
import com.foodyshop.helper.ProductHelper;
import com.foodyshop.main.Navigator;
import com.foodyshop.model.CategoryModel;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author X PC
 */
public class CategoryController implements Initializable {

    @FXML
    private TableView<CategoryModel> tblCategory;

    @FXML
    private TableColumn<CategoryModel, Integer> tcID;

    @FXML
    private TableColumn<CategoryModel, Integer> tcTopicID;

    @FXML
    private TableColumn<CategoryModel, String> tcName;

    @FXML
    private TableColumn<CategoryModel, Integer> tcStatus;

    @FXML
    private TableColumn<CategoryModel, String> tcCreated;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnDelete;

    ObservableList<CategoryModel> listCategory = FXCollections.observableArrayList();
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        tcID.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper((tblCategory.getItems().indexOf(cellData.getValue()) + 1) + ""));
        tcTopicID.setCellValueFactory(cellValue -> cellValue.getValue().getTopic_idProperty());
        tcName.setCellValueFactory(cellValue -> cellValue.getValue().getNameProperty());
        tcCreated.setCellValueFactory(cellValue -> cellValue.getValue().getCreatedProperty());
        tcStatus.setCellValueFactory(cellValue -> cellValue.getValue().getStatusProperty());
        listCategory = CategoryHelper.getAllCategory();
        tblCategory.setItems(listCategory);
        btnAdd.setOnMouseClicked(e -> Navigator.getInstance().showAddCategory(new AddCategoryController.IOnAddSuccess() {
            @Override
            public void onAddSuccess(CategoryModel categoryModel) {
                listCategory.add(0, categoryModel);
            }
        }));
        btnDelete.setOnMouseClicked(this::onClickDelete);
        btnEdit.setOnMouseClicked(this::onClickEdit);
            
    }

    private void onClickDelete(MouseEvent e) {

        CategoryModel category = tblCategory.getSelectionModel().getSelectedItem();
        if (category != null) {
            Alert alerts = new Alert(Alert.AlertType.CONFIRMATION);
            alerts.setTitle("ERROR");
            alerts.setHeaderText("Do you want delete " + category.getName());
            alerts.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.OK) {
                    boolean isHasLink = ProductHelper.isCategoryHasLinkToProducts(category);
                    if (isHasLink) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR");
                        alert.setHeaderText("Category has link with any product you must delete product first");
                        alert.show();
                    } else {
                        if (CategoryHelper.delete(category)) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("ERROR");
                            alert.setHeaderText("Delete success!");
                            alert.show();
                            listCategory.remove(category);
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("ERROR");
                            alert.setHeaderText("Delete error");
                            alert.show();
                        }
                    }
                }
            });

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Please choose category");
            alert.show();
        }
    }
    private void onClickEdit(MouseEvent e){
        CategoryModel category = tblCategory.getSelectionModel().getSelectedItem();
        if(category !=null){
            Navigator.getInstance().showEditCategory();
//            String id = tblCategory.getSelectionModel(getSelectedItem,0);
            
            
        }else{
             Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Please choose category");
            alert.show();
        }
    }
}
