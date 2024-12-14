package Views.Categories.Show;

import Dialogs.DialogTypes;
import Models.Category;
import Views.Categories.Edit.EditCategoriesController;
import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXButton;
import helpers.DbConnect;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import javax.persistence.PersistenceException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ShowCategoriesController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private TableView<Category> tbData;

    @FXML
    private TableColumn<Category, Integer> category_id;

    @FXML
    private TableColumn<Category, String> category_name;

    @FXML
    private TableColumn<Category, String> actions;

    @FXML
    private VBox tableViewContainer;

    @Override
    public synchronized void initialize(URL location, ResourceBundle resources) {
        new FadeIn(tbData).play();
        Label placeholderLabel = new Label("No Stores Available!");
        tbData.setPlaceholder(placeholderLabel);
        category_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        category_name.setCellValueFactory(new PropertyValueFactory<>("name"));

        //add cell of button edit
        Callback<TableColumn<Category, String>, TableCell<Category, String>> cellFactory = (TableColumn<Category, String> param) -> new TableCell<Category, String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                //that cell created only on non-empty rows
                if (empty) {
                    setGraphic(null);
                    setText("");
                } else {

                    JFXButton deleteButton = new JFXButton("Delete");
                    JFXButton editButton = new JFXButton("Edit");

                    deleteButton.setStyle("-fx-background-color: #f25961; -fx-text-fill: #fff; -fx-background-radius: 5px; -fx-font-size: 12pt; -fx-padding: 10px 20px");
                    editButton.setStyle("-fx-background-color: #48abf7; -fx-text-fill: #fff; -fx-background-radius: 5px; -fx-font-size: 12pt; -fx-padding: 10px 20px");

                    deleteButton.setOnAction(event -> {
                        AnchorPane pagesRoot = ((AnchorPane) stackPane.getParent());
                        DialogTypes.getWarningDialog((StackPane) pagesRoot.getScene().getRoot(), "Are you sure?", "You won't be able to revert this!", "Yes, delete it!", "Cancel", 100, ignored -> {
                            try {
                                DbConnect.removeItem(tbData.getItems().get(getIndex()));
                                StackPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/Categories/Show/ShowCategories.fxml")));
                                pagesRoot.getChildren().setAll(pane);
                                DialogTypes.getConfirmationDialog((StackPane) pagesRoot.getParent().getParent(), "Deleted Successfully", "Category Deleted Successfully!", 100).show();
                            } catch (PersistenceException | IllegalArgumentException e) {
                                DialogTypes.getErrorDialog((StackPane) pagesRoot.getScene().getRoot(), "Deletion Failed", "One or more Stores are associated with this Category!", 100).show();
                            } catch (IOException ignored1) {
                            }
                        }).show();
                    });
                    editButton.setOnAction(event -> {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Categories/Edit/EditCategories.fxml"));
                        try {
                            StackPane pane = loader.load();
                            new FadeIn(pane).play();
                            ((AnchorPane) stackPane.getParent()).getChildren().setAll(pane);
                            ((EditCategoriesController) loader.getController()).setCategory(tbData.getItems().get(getIndex()));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    HBox studentManage = new HBox(editButton, deleteButton);
                    studentManage.setAlignment(Pos.CENTER);
                    HBox.setMargin(deleteButton, new Insets(2, 2, 0, 5));
                    HBox.setMargin(editButton, new Insets(2, 5, 0, 2));

                    setGraphic(studentManage);
                    setText(null);
                }
            }
        };
        actions.setCellFactory(cellFactory);

        tbData.getColumns().setAll(Arrays.asList(category_id, category_name, actions));


        // Retrieve data from Database
        ObservableList<Category> categoryObservableList = FXCollections.observableList(DbConnect.getEntityManager().createNamedQuery("Category.fetchAllASC", Category.class).getResultList());
        tbData.setItems(categoryObservableList);
        double tableDefaultHeight = 986.0;
        Platform.runLater(() -> Platform.runLater(() -> {
            tbData.setFixedCellSize(80);
            tableViewContainer.setPadding(new Insets(0, 0, tableDefaultHeight - (80 * categoryObservableList.size()) - 55, 0));
        }));
    }
}
