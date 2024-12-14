package FixedFrame;

import Dialogs.DialogTypes;
import Views.Links;
import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private Label drawerImage;

    @FXML
    private ImageView exit;

    @FXML
    private Text title;

    @FXML
    private VBox sideBar;

    @FXML
    private JFXButton categoriesMenuButton;

    @FXML
    private VBox categoriesOperationsVBox;

    @FXML
    private JFXButton storeMenuButton;

    @FXML
    private VBox storeOperationsVBox;

    @FXML
    private VBox categoriesMenuVBox;

    @FXML
    private VBox storesMenuVBox;

    @FXML
    private JFXButton ratingJFXButton;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    void close(MouseEvent ignored) {
        DialogTypes.getWarningDialog(stackPane, "Are you sure?", "You're going to close the program!", "Close the Program", "Cancel", 100, e -> System.exit(0)).show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getHome(null);
        DialogTypes.getConfirmationDialog(stackPane, "Logged In Successfully!", "Welcome back Mr. Yousef", 100).show();
        Platform.runLater(() -> {
            subMenusAssociateEvents(categoriesMenuVBox, categoriesMenuButton, categoriesOperationsVBox);
            subMenusAssociateEvents(storesMenuVBox, storeMenuButton, storeOperationsVBox);
            storeMenuButton.getStyleClass().add("arrow");
        });
    }

    @FXML
    private void getHome(MouseEvent ignored) {
        title.setText("Home");
        Platform.runLater(() -> setSelected((JFXButton) sideBar.getChildren().get(0), null));
        loadView(Links.HOME_CONTENT.getValue());
    }

    @FXML
    private void getRatings(MouseEvent e) {
        title.setText("Ratings");
        setSelected((JFXButton) e.getSource(), null);
        loadView(Links.RATINGS_VIEW.getValue());
    }

    @FXML
    private void getShowStores(MouseEvent e) {
        title.setText("Show Stores");
        setSelected(storeMenuButton, (JFXButton) e.getSource());
        loadView(Links.SHOW_STORES_VIEW.getValue());
    }

    @FXML
    private void getCreateStores(MouseEvent e) {
        title.setText("Create Stores");
        setSelected(storeMenuButton, (JFXButton) e.getSource());
        loadView(Links.CREATE_STORES_VIEW.getValue());
    }

    @FXML
    private void getShowCategories(MouseEvent e) {
        title.setText("Show Categories");
        setSelected(categoriesMenuButton, (JFXButton) e.getSource());
        loadView(Links.SHOW_CATEGORIES_VIEW.getValue());
    }

    @FXML
    private void getCreateCategories(MouseEvent e) {
        title.setText("Create Categories");
        setSelected(categoriesMenuButton, (JFXButton) e.getSource());
        loadView(Links.CREATE_CATEGORIES_VIEW.getValue());
    }

    @FXML
    void logout(MouseEvent ignored) {
        DialogTypes.getWarningDialog(stackPane, "Are you sure?", "You are going to logout!", "Yes, Logout!", "Cancel", 100, e -> {
            try {
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Links.PUBLIC_PAGE.getValue())));
                anchorPane.getScene().setRoot(root);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }).show();
    }

    public void loadView(String viewName) {
        try {
            StackPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(viewName)));
            new FadeIn(pane).play();
            anchorPane.getChildren().setAll(pane);
        } catch (IOException e) {
        }
    }

    private void subMenusAssociateEvents(@NotNull VBox container, @NotNull Button menuButton, @NotNull VBox subMenu) {
        sideBar.getChildren().remove(subMenu);

        menuButton.setOnAction(e -> {
            if (!container.getChildren().contains(subMenu)) {
                container.getChildren().add(subMenu);
                new FadeIn(container).play();
            } else {
                container.getChildren().remove(subMenu);
                new FadeIn(container).play();
            }
        });
    }

    private JFXButton currentSelectedButton = null;
    private JFXButton currentSubMenuSelectedItem = null;

    private void setSelected(@NotNull JFXButton selectedButton, JFXButton subMenuSelectedItem) {
        if (currentSelectedButton != null) {
            resetDefaults(currentSelectedButton, currentSubMenuSelectedItem);
        }

        currentSelectedButton = selectedButton;
        currentSubMenuSelectedItem = subMenuSelectedItem;
        currentSelectedButton.getStyleClass().add("expanded");
        if (currentSubMenuSelectedItem != null) {
            currentSubMenuSelectedItem.getStyleClass().add("subMenuSelection");
//            currentSubMenuSelectedItem.getGraphic().setEffect();
        }
        ColorAdjust selectedColorAdjust = new ColorAdjust();
        selectedColorAdjust.setBrightness(1.0);
        selectedButton.getGraphic().setEffect(selectedColorAdjust);
    }

    private void resetDefaults(@NotNull JFXButton selectedButton, JFXButton subMenuSelectedItem) {
        try {
            ColorAdjust colorEffect = (ColorAdjust) selectedButton.getGraphic().getEffect();
            colorEffect.setBrightness(0.0);
            selectedButton.getStyleClass().remove("expanded");
            if (subMenuSelectedItem != null)
                currentSubMenuSelectedItem.getStyleClass().remove("subMenuSelection");
        } catch (NullPointerException ignored) {
        }
    }
}
