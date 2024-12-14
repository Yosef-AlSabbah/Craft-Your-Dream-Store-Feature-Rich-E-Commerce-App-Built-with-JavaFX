package FixedFrame;

import Dialogs.DialogTypes;
import Models.Category;
import Views.Links;
import animatefx.animation.FadeIn;
import helpers.DbConnect;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PublicController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private TextField searchBar;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Menu categoriesDropDownMenu;

    @FXML
    private MenuBar menuBar;

    private ObservableList<Category> categories = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Category> categoriesList = DbConnect.getEntityManager().createNamedQuery("Category.fetchAllASC", Category.class).getResultList();
        categories = FXCollections.observableList(categoriesList);
        categoriesDropDownMenu.getItems().addAll(categories.stream().map(e -> new MenuItem(e.toString())).collect(Collectors.toList()));
        categoriesDropDownMenu.setId("");
        menuBar.setId("menuBar");
        loadView(Links.PUBLIC_MAIN_PAGE.getValue());
        Platform.runLater(() -> ((HBox) menuBar.lookup("HBox")).getChildren().get(0).setOnMouseMoved(e -> categoriesDropDownMenu.show()));
    }

    @FXML
    void goHome(ActionEvent ignore) {
        loadView(Links.PUBLIC_MAIN_PAGE.getValue());
    }

    @FXML
    void moveToLoginPage(MouseEvent ignored) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(Links.LOGIN_PAGE_VIEW.getValue())));
        menuBar.getScene().setRoot(loader.load());
    }


    @FXML
    void close(MouseEvent ignored) {
        DialogTypes.getWarningDialog(stackPane, "Are you sure?", "You're going to close the program!", "Close the Program", "Cancel", 100, e -> System.exit(0)).show();
    }

    public void loadView(String viewName) {
        try {
            StackPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(viewName)));
            loadView(pane);
        } catch (IOException ignored) {
        }
    }

    private void loadView(StackPane pane) {
        new FadeIn(pane).play();
        anchorPane.getChildren().setAll(pane);
    }

    public void loadView(String viewName, Object controller) {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(viewName)));
        loader.setController(controller);
        try {
            loadView((StackPane) loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

