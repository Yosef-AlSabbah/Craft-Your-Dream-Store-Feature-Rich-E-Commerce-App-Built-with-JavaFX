package Views.LoginPage;

import Dialogs.DialogTypes;
import Notifications.Notifications;
import Views.Links;
import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import helpers.DbConnect;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class LoginPageController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXTextField emailTextField;

    @FXML
    private JFXPasswordField passwordTextField;

    @FXML
    private JFXCheckBox keepMeLoggedIn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new FadeIn(anchorPane).play();
        Platform.runLater(() -> {
            EventHandler<KeyEvent> enterClicked = e -> {
                if (e.getCode() == KeyCode.ENTER)
                    try {
                        login(null);
                    } catch (IOException ignored) {
                    }
            };
            emailTextField.setOnKeyReleased(enterClicked);
            passwordTextField.setOnKeyReleased(enterClicked);
        });
    }

    @FXML
    void forgetPassword(MouseEvent ignored) {

    }

    @FXML
    void login(MouseEvent ignored) throws IOException {
        Scene scene = keepMeLoggedIn.getScene();
        if (DbConnect.loginCheck(emailTextField.getText().trim(), passwordTextField.getText().trim(), scene)) {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(Links.ADMIN_DASHBOARD.getValue())));
            scene.setRoot(loader.load());
        }
    }

    @FXML
    void registerNow(MouseEvent ignored) {

    }

    @FXML
    void goHome(MouseEvent ignored) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(Links.PUBLIC_PAGE.getValue())));
        keepMeLoggedIn.getScene().setRoot(loader.load());
    }

    @FXML
    void close(MouseEvent ignored) {
        DialogTypes.getWarningDialog(stackPane, "Are you sure?", "You're going to close the program!", "Close the Program", "Cancel", 100, e -> System.exit(0)).show();
    }
}

