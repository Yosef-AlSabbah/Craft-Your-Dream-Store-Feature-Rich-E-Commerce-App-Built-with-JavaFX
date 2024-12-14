package Views.PublicMainPage.Stores;

import Models.Store;
import Views.Links;
import Views.PublicMainPage.PublicMainPageController;
import Views.StorePage.StorePageController;
import animatefx.animation.FadeIn;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class StoreCardController {

    @FXML
    private HBox image;

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

    private Store store;

    private int storeRating;

    private PublicMainPageController.StoreCard storeCard;

    public StoreCardController(PublicMainPageController.StoreCard storeCard) {
        this.storeCard = storeCard;
        this.store = storeCard.getStore();
        this.storeRating = storeCard.getRating();
        Platform.runLater(this::loadData);
    }

    public void loadData() {
        store.getIcon().setFitWidth(200);
        store.getIcon().setFitHeight(200);
        this.image.getChildren().add(store.getIcon());
        this.title.setText(store.getName());
        this.categoryName.setText(store.getCategory().getName());
        this.address.setText(store.getAddress());
        this.phoneNumber.setText(store.getPhone());

        int numOfStars = -1;
        for (Node node : starContainer.getChildren()) {
            numOfStars++;
            if (numOfStars < storeRating)
                node.getStyleClass().add("star-checked");
        }
    }

    @FXML
    void openStorePage(MouseEvent ignored) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Links.STORE_PAGE.getValue()));
        StorePageController spc = new StorePageController(storeCard);
        loader.setController(spc);
        Parent newRoot = loader.load();
        new FadeIn(newRoot).play();
        starContainer.getScene().setRoot(newRoot);
    }
}
