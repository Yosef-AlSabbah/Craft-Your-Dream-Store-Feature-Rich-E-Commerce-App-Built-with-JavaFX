package Views.Stores.Show;

import Dialogs.DialogTypes;
import Models.Category;
import Models.Store;
import Views.Links;
import Views.Stores.Edit.EditStoresController;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.hibernate.exception.ConstraintViolationException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class ShowStoresController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private TableView<Store> tbData;

    @FXML
    private TableColumn<Store, Integer> store_id;

    @FXML
    private TableColumn<Store, ImageView> image;

    @FXML
    private TableColumn<Store, String> store_name;

    @FXML
    private TableColumn<Store, String> store_phone_number;

    @FXML
    private TableColumn<Store, String> address;

    @FXML
    private TableColumn<Store, Category> category;

    @FXML
    private TableColumn<Store, String> actions;

    @FXML
    private VBox tableViewContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new FadeIn(tbData).play();
        Label placeholderLabel = new Label("No Store Available!");
        tbData.setPlaceholder(placeholderLabel);
        store_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        store_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        store_phone_number.setCellValueFactory(new PropertyValueFactory<>("phone"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        image.setCellValueFactory(new PropertyValueFactory<>("icon"));
        store_id.setPrefWidth(100);
        image.setPrefWidth(200);

        //add cell of button edit
        Callback<TableColumn<Store, String>, TableCell<Store, String>> cellFactory = (TableColumn<Store, String> param) -> new TableCell<Store, String>() {
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
                                Store selectedStore = tbData.getItems().get(getIndex());
                                DbConnect.removeItem(selectedStore);
                                Files.delete(Paths.get(selectedStore.getIcon().getImage().impl_getUrl().replaceFirst("file:", "")));
                                StackPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Links.SHOW_STORES_VIEW.getValue())));
                                pagesRoot.getChildren().setAll(pane);
                                DialogTypes.getConfirmationDialog((StackPane) pagesRoot.getParent().getParent(), "Deleted Successfully", "Store Deleted Successfully!", 50).show();
                            } catch (IOException ignored1) {
                            }
                        }).show();
                    });
                    editButton.setOnAction(event -> {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource(Links.EDIT_STORES_VIEW.getValue()));
                        try {
                            StackPane pane = loader.load();
                            new FadeIn(pane).play();
                            ((AnchorPane) stackPane.getParent()).getChildren().setAll(pane);
                            ((EditStoresController) loader.getController()).setStore(tbData.getItems().get(getIndex()));
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

        tbData.getColumns().setAll(Arrays.asList(store_id, image, store_name, store_phone_number, address, category, actions));


        // Retrieve data from Database
        ObservableList<Store> stores = FXCollections.observableList(DbConnect.getEntityManager().createNamedQuery("Store.fetchAllStoresASC", Store.class).getResultList());
        tbData.setItems(stores);

        double tableDefaultHeight = 986.0;
        Platform.runLater(() -> {
            tbData.setFixedCellSize(80);
            tableViewContainer.setPadding(new Insets(0, 0, tableDefaultHeight - (80 * stores.size()) - 55, 0));
        });
    }
}
