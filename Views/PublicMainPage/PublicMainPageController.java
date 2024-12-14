package Views.PublicMainPage;

import Models.Store;
import Views.Links;
import Views.PublicMainPage.Stores.StoreCardController;
import animatefx.animation.FadeIn;
import helpers.DbConnect;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PublicMainPageController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private VBox categoriesLeftSide;

    @FXML
    private HBox cardsContainer;

    @FXML
    private HBox pagination;

    private final ArrayList<StoreCard> storeCards = new ArrayList<>();

    private List<String> storeNames = new ArrayList<>();

    private final ArrayList<StoreCard> showingStoreCards = new ArrayList<>();

    private int current = -1, numOfCardsPerPage = 3, currentPageNum = 1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {

        categoriesLeftSide.getStyleClass().add("categories-side-bar");

        Long numOfStores = DbConnect.getEntityManager().createNamedQuery("Store.getNumberOfStores", Long.class).getSingleResult();
        addToCategoriesLeftBox("All", numOfStores, false);
        DbConnect.getEntityManager().createNamedQuery("Category.getCategoriesWithStoresCount", Object[].class).getResultList().forEach(e -> addToCategoriesLeftBox((String) e[0], (Long) e[1]));

        DbConnect.getEntityManager().createNamedQuery("Store.getStoreInfo", Object[].class).getResultList().forEach(e -> storeCards.add(new StoreCard(e)));

        getByPageNumber(currentPageNum);
        Platform.runLater(this::searchBox);
        Platform.runLater(() ->
                ((MenuBar) anchorPane.getScene().lookup("#menuBar"))
                        .getMenus()
                        .get(0)
                        .getItems()
                        .forEach(e -> e.setOnAction(e1 -> loadStoresByCategoryName(e.getText()))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchBox() {
        TextField searchBar = (TextField) anchorPane.getScene().lookup("#searchBar");
        storeNames = storeCards.stream().map(e -> e.store.getName()).collect(Collectors.toList());
        TextFields.bindAutoCompletion(searchBar, storeNames);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            showingStoreCards.clear();
            List<StoreCard> stores = storeCards.stream().filter(storeCard -> storeCard.getStore().toString().toLowerCase().contains(newValue.toLowerCase()))
                    .limit(3)
                    .collect(Collectors.toList());
            showingStoreCards.addAll(stores);
            updateCards();
            pagination.getChildren().clear();
            if (!newValue.isEmpty())
                getPagination(stores.size());
            else
                getPagination();
        });
    }

    public void addToCategoriesLeftBox(String categoryName, Long numberOfStores) {
        addToCategoriesLeftBox(categoryName, numberOfStores, true);
    }

    public void addToCategoriesLeftBox(String categoryName, Long numberOfStores, boolean addLine) {
        Label categoryNameText = new Label(categoryName);
        Label numberOfStoresText = new Label(String.valueOf(numberOfStores));
        categoryNameText.getStyleClass().add("text-side-bar");
        numberOfStoresText.getStyleClass().add("number-text");

        HBox categoryNameHBox = new HBox(categoryNameText), numberOfStoresHBox = new HBox(numberOfStoresText);
        numberOfStoresHBox.getStyleClass().add("stores-number-box");
        // To align text to left and number to right.
        HBox.setHgrow(categoryNameHBox, Priority.ALWAYS);
        HBox.setHgrow(numberOfStoresHBox, Priority.NEVER);
        // Group all items in a single node and add it to left side box.
        HBox container = new HBox(categoryNameHBox, numberOfStoresHBox);
        if (addLine) container.getStyleClass().add("categories-side-bar-content");
        container.getStyleClass().add("content");
        container.setPadding(new Insets(10, 0, 10, 0));
        categoriesLeftSide.getChildren().add(container);
        container.setOnMouseReleased(e -> {
            if (!categoryName.equals("All"))
                loadStoresByCategoryName(categoryName);
            else getByPageNumber(1);
        });
    }

    private void updateCards() {
        cardsContainer.getChildren().clear();
        if (showingStoreCards.isEmpty()) {
            Label errorMessage = new Label("There are no stores available H E R E!");
            errorMessage.setStyle("-fx-text-fill: DarkRed; -fx-font-size: 25pt;");
            cardsContainer.getChildren().add(errorMessage);
        }
        try {
            for (StoreCard showingStoreCard : showingStoreCards) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Links.PUBLIC_MAIN_PAGE_STORE_CARD.getValue()));
                StoreCardController card = new StoreCardController(showingStoreCard);
                loader.setController(card);
                Node storeCard = loader.load();
                cardsContainer.getChildren().add(storeCard);
            }
        } catch (IOException ignored) {
        }
        new FadeIn(cardsContainer).play();
    }

    public void getPagination() {
        getPagination(storeCards.size());
    }

    public void getPagination(int size) {
        int numberOfPages = (int) Math.ceil((double) size / numOfCardsPerPage);
        if (numberOfPages > 1) {
            this.createPrevPagePaginationElement();
            for (int i = 1; i <= numberOfPages; i++)
                this.normalPagePaginationElement(i);
            this.createNextPagePaginationElement();
            Label prevLabel = (Label) pagination.getChildren().get(0), nextLabel = (Label) pagination.getChildren().get(pagination.getChildren().size() - 1);
            pagination.getChildren().get(currentPageNum).getStyleClass().add("pagination-element-active");
            if (currentPageNum == 1) {
                prevLabel.getStyleClass().add("pagination-element-disabled");
                prevLabel.setOnMouseReleased(null);
            }
            if (currentPageNum == numberOfPages) {
                nextLabel.getStyleClass().add("pagination-element-disabled");
                nextLabel.setOnMouseReleased(null);
            }
        }
    }

    public void getNextPage() {
        try {

            getByPageNumber(++currentPageNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getByPageNumber(int pageNumber) {
        pagination.getChildren().clear();
        currentPageNum = pageNumber;
        int startCardIndex = (pageNumber - 1) * 3;
        int endCardIndex = Math.min(startCardIndex + numOfCardsPerPage - 1, storeCards.size() - 1);
        current = startCardIndex;
        showingStoreCards.clear();
        for (int i = startCardIndex; i <= endCardIndex && i < storeCards.size(); i++) {
            showingStoreCards.add(storeCards.get(i));
            current = i;
        }
        updateCards();
        getPagination();
        new FadeIn(cardsContainer).play();
    }

    private void createPrevPagePaginationElement() {
        createPaginationElement("«", e -> getPervPage());
    }

    private void createNextPagePaginationElement() {
        createPaginationElement("»", e -> getNextPage());
    }

    public void getPervPage() {
        getByPageNumber(--currentPageNum);
    }

    private void normalPagePaginationElement(int pageNumber) {
        createPaginationElement(String.valueOf(pageNumber), e -> getByPageNumber(pageNumber));
    }

    private void createPaginationElement(String text, EventHandler<MouseEvent> event) {
        Label paginationElementText = new Label(text);
        paginationElementText.getStyleClass().add("pagination-elements");
        paginationElementText.setOnMouseReleased(event);
        pagination.getChildren().add(paginationElementText);
    }

    private void loadStoresByCategoryName(String categoryName) {
        showingStoreCards.clear();
        List<StoreCard> stores = storeCards.stream()
                .filter(storeCard -> storeCard.getStore().getCategory().getName().equals(categoryName))
                .collect(Collectors.toList());
        showingStoreCards.addAll(stores);
        pagination.getChildren().clear();
        getPagination(stores.size());
        updateCards();
    }


    public static class StoreCard {
        private Store store;
        private Integer rating;

        private StoreCard(Store store, double rating) {
            this.store = store;
            this.rating = (int) rating;
        }

        public StoreCard(Object[] storeInfo) {
            this((Store) storeInfo[0], (Double) storeInfo[1]);
        }

        public Store getStore() {
            return store;
        }

        public void setStore(Store store) {
            this.store = store;
        }

        public Integer getRating() {
            return rating;
        }

        public void setRating(Integer rating) {
            this.rating = rating;
        }
    }
}