package Views;

public enum Links {
    EDIT_STORES_VIEW("/Views/Stores/Edit/EditStores.fxml"),
    HOME_CONTENT("/Views/Home/Home.fxml"),
    RATINGS_VIEW("/Views/RatingsPage/Ratings.fxml"),
    SHOW_CATEGORIES_VIEW("/Views/Categories/Show/ShowCategories.fxml"),
    CREATE_CATEGORIES_VIEW("/Views/Categories/Create/CreateCategories.fxml"),
    SHOW_STORES_VIEW("/Views/Stores/Show/ShowStores.fxml"),
    CREATE_STORES_VIEW("/Views/Stores/Create/CreateStores.fxml"),
    ADMIN_DASHBOARD("/FixedFrame/Dashboard.fxml"),
    PUBLIC_PAGE("/FixedFrame/Public.fxml"),
    PUBLIC_MAIN_PAGE("/Views/PublicMainPage/PublicMainPage.fxml"),
    PUBLIC_MAIN_PAGE_STORE_CARD("/Views/PublicMainPage/Stores/StoreCard.fxml"),
    LOGIN_PAGE_VIEW("/Views/LoginPage/LoginPage.fxml"),
    STORE_PAGE("/Views/StorePage/StorePage.fxml");


    private final String link;

    Links(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return link;
    }

    public String getValue() {
        return link;
    }
}
