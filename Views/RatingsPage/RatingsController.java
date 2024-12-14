package Views.RatingsPage;

import Models.Store;
import animatefx.animation.FadeIn;
import helpers.DbConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class RatingsController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private TableView<Rating> tbData;

    @FXML
    private TableColumn<Rating, Integer> rating_Id;

    @FXML
    private TableColumn<Rating, ImageView> image;

    @FXML
    private TableColumn<Rating, String> store_name;

    @FXML
    private TableColumn<Rating, Integer> total_ratings;

    @FXML
    private TableColumn<Rating, Integer> number_of_ratings;

    @FXML
    private TableColumn<Rating, Integer> store_rating;

    @FXML
    private TextField searchBar;

    @FXML
    private HBox pagination;

    private int number_of_rows_per_page = 5;
    private int show_from_row = 0;

    private List<Rating> storesRating = new ArrayList<>();

    private List<Rating> showingStoresRating = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchBar.getStyleClass().add("search-bar");
        // Set a placeholder label for empty table
        Label placeholderLabel = new Label("No Stores Available!");
        tbData.setPlaceholder(placeholderLabel);
        tbData.setRowFactory(tv -> {
            TableRow<Rating> row = new TableRow<>();
            row.setPrefHeight(100); // set the row height to 100 pixels
            return row;
        });

        rating_Id.setCellValueFactory(new PropertyValueFactory<>("rating_id"));
        image.setCellValueFactory(new PropertyValueFactory<>("image"));
        store_name.setCellValueFactory(new PropertyValueFactory<>("store_name"));
        total_ratings.setCellValueFactory(new PropertyValueFactory<>("total_ratings"));
        number_of_ratings.setCellValueFactory(new PropertyValueFactory<>("number_of_ratings"));
        store_rating.setCellValueFactory(new PropertyValueFactory<>("store_rating"));
        rating_Id.setPrefWidth(100);
        image.setPrefWidth(200);
        tbData.getColumns().setAll(FXCollections.observableArrayList(Arrays.asList(rating_Id, image, store_name, total_ratings, number_of_ratings, store_rating)));
        try {

        List<Object[]> storeRatings = DbConnect.getEntityManager().createNamedQuery("Store.getStoreInfo", Object[].class).getResultList();
        storeRatings.forEach(e -> {
            Store store = (Store) e[0];
            storesRating.add(new Rating(store.getId(), store.getIcon(), store.getName(),
                    ((Long) (e[3] != null? e[3] : 0L)).intValue(),
                    ((Long) (e[2] != null? e[2] : 0L)).intValue(),
                    ((Double) (e[1] != null? e[1] : 0D)).intValue()));
        });
        } catch (Exception e) {
            e.printStackTrace();
        }
        ObservableList<Rating> clonedList = FXCollections.observableArrayList();
        clonedList.addAll(storesRating);
        TextFields.bindAutoCompletion(searchBar, storesRating);
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
                    storesRating.clear();
                    storesRating.addAll(
                            clonedList.stream()
                                    .filter(rating -> rating.toString().toLowerCase().contains(newValue.toLowerCase()))
                                    .collect(Collectors.toList()));
                    tbData.setItems(FXCollections.observableList(storesRating));
                    pagination.getChildren().clear();
                    if (!newValue.isEmpty())
                        getPagination(storesRating.size());
                    else {
                        getPagination();
                        storesRating.clear();
                        storesRating.addAll(clonedList);
                        tbData.setItems(FXCollections.observableList(storesRating));
                    }
                }
        );
        getByPageNumber(currentPageNum);
    }

    private int current = -1, numberOfStores = 5, currentPageNum = 1;

    public void getPagination() {
        getPagination(storesRating.size());
    }

    public void getPagination(int size) {
        int numberOfPages = (int) Math.ceil((double) size / numberOfStores);
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
        getByPageNumber(++currentPageNum);
    }

    public void getByPageNumber(int pageNumber) {
        pagination.getChildren().clear();
        currentPageNum = pageNumber;
        int startCardIndex = (pageNumber - 1) * numberOfStores, endCardIndex = startCardIndex + numberOfStores - 1;
        current = startCardIndex;
        tbData.getItems().clear();
        showingStoresRating.clear();
        for (int i = startCardIndex; (i <= endCardIndex) && (current + 1 < storesRating.size()); i++) {
            showingStoresRating.add(storesRating.get(i));
            current = i;
        }
        updateTable();
        getPagination();
        new FadeIn(tbData).play();
    }

    private void updateTable() {
        showingStoresRating.forEach(tbData.getItems()::add);
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

    public int getShow_from_row() {
        return show_from_row;
    }

    public void setShow_from_row(int show_from_row) {
        this.show_from_row = show_from_row;
    }
}
