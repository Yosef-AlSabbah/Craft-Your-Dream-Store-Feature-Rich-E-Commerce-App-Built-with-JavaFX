package Views.StorePage;

import Dialogs.DialogTypes;
import Models.Category;
import Models.Store;
import Views.Links;
import Views.PublicMainPage.PublicMainPageController;
import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXButton;
import helpers.DbConnect;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.Rating;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class StorePageController implements Initializable {
    @FXML
    private StackPane stackPane;

    @FXML
    private MenuBar menuBar;

    @FXML
    private ImageView exit;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label title;

    @FXML
    private Label categoryName;

    @FXML
    private Label phoneNumber;

    @FXML
    private Label address;

    @FXML
    private HBox starContainer;

    @FXML
    private Rating rating;

    @FXML
    private Label currentRateText;

    @FXML
    private Label currentRateValue;

    @FXML
    private JFXButton confirmRatingButton;

    @FXML
    private ImageView image;

    private PublicMainPageController.StoreCard storeCard;

    private Store store;

    private Models.Rating usersRating;

    private String userIP;

    public StorePageController(PublicMainPageController.StoreCard storeCard) {
        this.storeCard = storeCard;
        this.store = storeCard.getStore();
    }

    public StorePageController() {
    }

    private ObservableList<Category> categories = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::loadData);
    }

    @FXML
    void goHome(ActionEvent ignore) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(Links.PUBLIC_PAGE.getValue())));
        Parent newRoot = loader.load();
        new FadeIn(newRoot).play();
        anchorPane.getScene().setRoot(newRoot);
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

    @FXML
    void confirmRating(MouseEvent ignored) {
        if (usersRating == null) {
            usersRating = new Models.Rating(userIP, store, (byte) rating.getRating(), LocalDate.now());
        } else usersRating.setRating((byte) rating.getRating());
        DbConnect.updateItem(usersRating);
        DialogTypes.getConfirmationDialog(stackPane, "Done Successfully", "Your Rating added Successfully!", 100).show();
        currentRateText.setText("Your Current Rate: ");
        currentRateValue.setText(String.valueOf(usersRating.getRating()));
    }

    public void loadData() {
        this.image.setImage(store.getIcon().getImage());
        this.title.setText(store.getName());
        this.categoryName.setText(store.getCategory().getName());
        this.address.setText(store.getAddress());
        this.phoneNumber.setText(store.getPhone());

        int numOfStars = -1;
        for (Node node : starContainer.getChildren()) {
            numOfStars++;
            if (numOfStars < storeCard.getRating())
                node.getStyleClass().add("star-checked");
        }
        userIP = DbConnect.getMacAddress();
        try {
            usersRating = DbConnect.getEntityManager().createNamedQuery("Rating.getRatingAssociatedWithUserIP", Models.Rating.class)
                    .setParameter(1, userIP)
                    .setParameter(2, store)
                    .getSingleResult();

            currentRateValue.setText(String.valueOf(usersRating.getRating()));
        } catch (NoResultException ignored) {
            currentRateText.setText(null);
            currentRateValue.setText(null);
        }
        rating.setRating(usersRating != null ? usersRating.getRating() : 0);
    }
}

