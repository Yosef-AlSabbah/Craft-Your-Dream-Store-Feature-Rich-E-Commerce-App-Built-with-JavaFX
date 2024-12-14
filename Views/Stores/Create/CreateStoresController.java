package Views.Stores.Create;

import Dialogs.DialogTypes;
import Models.Category;
import Models.Store;
import Notifications.Notifications;
import Views.Links;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import helpers.DbConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import javax.persistence.PersistenceException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class CreateStoresController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextField store_name;

    @FXML
    private JFXButton addStoreButton;

    @FXML
    private JFXTextField phoneNumber;

    @FXML
    private JFXTextField address;

    @FXML
    private JFXComboBox<Category> category;

    @FXML
    private Text imageText;

    @FXML
    private JFXButton imageButton;

    @FXML
    private Text addressText;

    @FXML
    private Text categoryText;

    @FXML
    private Text phoneBumberText;

    @FXML
    private Text storeNameText;

    @FXML
    private Text imageErrorText;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
            List<Category> categories = DbConnect.getEntityManager().createNamedQuery("Category.fetchAllASC", Category.class).getResultList();
            categories.forEach(this.category.getItems()::add);
    }

    private final CheckInputs checkInputs = new CheckInputs();

    @FXML
    void addStore(MouseEvent ignored) {
        if (!checkInputs.validate()) {
            String storeName = this.store_name.getText().trim(), storePhoneNumber = phoneNumber.getText().trim(), storeAddress = this.address.getText().trim();
            Category storeCategory = category.getSelectionModel().getSelectedItem();
            String copiedImageName = storeName.toLowerCase() + "." + imageFile.getName().split("\\.")[1];
            Store newStore = new Store(storeName, "../users_imgs/" + copiedImageName, storePhoneNumber, storeAddress, storeCategory);
            try {
                DbConnect.addItem(newStore);
                // If every thing works fine copy image
                Path sourcePath = Paths.get(imageFile.toURI());
                Path targetPath = Paths.get("C:/xampp/htdocs/HYPEX/users_imgs/", copiedImageName.toLowerCase());
                Files.copy(sourcePath, targetPath);

                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Links.SHOW_STORES_VIEW.getValue())));
                AnchorPane parent = (AnchorPane) stackPane.getParent();
                parent.getChildren().setAll(root);
                DialogTypes.getConfirmationDialog(((StackPane) parent.getParent().getParent()), "Added Successfully", "Store Added Successfully!", 50).show();
            } catch (PersistenceException ignored1) {
                Notifications.createFailNotification("Choose a unique name!", "Store name already exists!", stackPane.getScene(), Pos.TOP_CENTER);
            } catch (IOException e) {
                Notifications.createFailNotification("Something went wrong!", "Something went wrong try again later!", stackPane.getScene(), Pos.TOP_CENTER);
            }
        } else {
            System.out.println("Error");
        }
    }

    private final FileChooser fileChooser = new FileChooser();

    private File imageFile = null;

    @FXML
    void chooseImage(ActionEvent ignored) {
        fileChooser.setTitle("Choose an image file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        imageFile = fileChooser.showOpenDialog(imageButton.getScene().getWindow());
        if (imageFile != null) {
            imageText.setText(imageFile.getName());
            imageButton.setText("");
            ImageView chosenImage = new ImageView("file:" + imageFile.getAbsolutePath());
            imageButton.setMaxWidth(70);
            imageButton.setPadding(new Insets(0));
            chosenImage.setFitHeight(70);
            chosenImage.setFitWidth(70);
            imageButton.setGraphic(chosenImage);
        }
    }

    private class CheckInputs {
        boolean fail = false;

        public boolean validate() {
            checkStoreName();
            checkStoreAddress();
            storePhoneNumber();
            checkCategory();
            checkImage();
            return fail;
        }

        private void checkStoreName() {
            boolean error = false;
            String storeName = store_name.getText().trim();
            if (!storeName.isEmpty()) {
                if (!storeName.matches("^[a-zA-Z0-9 ]{2,25}$")) {
                    storeNameText.setText("Store name must be 2-25 chars & Alphanumeric.");
                    error = true;
                }
            } else {
                storeNameText.setText("Store name can't be empty!");
                error = true;
            }
            if (error) {
                imageErrorText.setStyle("-fx-text-fill: RED");
                fail = true;
            } else
                imageErrorText.setText("Make sure to use a unique name.");
        }

        private void checkStoreAddress() {
            boolean error = false;
            String storeAddress = address.getText().trim();
            if (!storeAddress.isEmpty()) {
                // Could have address check here.
            } else {
                addressText.setText("Address cannot be empty!");
                error = true;
            }
            if (error) {
                fail = true;
                imageErrorText.setStyle("-fx-text-fill: RED");
            } else
                addressText.setText(null);
        }

        private void storePhoneNumber() {
            boolean error = false;
            String storePhoneNumber = phoneNumber.getText().trim();
            if (!storePhoneNumber.isEmpty()) {
                if (storePhoneNumber.length() < 8) {
                    phoneBumberText.setText("Enter a valid phone number.");
                    error = true;
                }
            } else {
                phoneBumberText.setText("Phone Number cannot be empty!");
                error = true;
            }
            if (error) {
                fail = true;
                imageErrorText.setStyle("-fx-text-fill: RED !important;");
            } else
                phoneBumberText.setText("We'll never share your phone number with anyone else.");
        }

        private void checkCategory() {
            boolean error = false;
            if (category.getSelectionModel().getSelectedItem() == null) {
                categoryText.setText("Category cannot be empty!");
                error = true;
            }
            if (error) {
                fail = true;
                imageErrorText.setStyle("-fx-text-fill: RED !important");
            } else
                categoryText.setText(null);
        }

        private void checkImage() {
            boolean error = false;
            if (imageFile == null) {
                imageErrorText.setText("Image cannot be empty!");
                error = true;
            }
            if (error) {
                fail = true;
                imageErrorText.setStyle("-fx-text-fill: RED !important; -fx-font-weight: normal;");
            } else
                imageErrorText.setText(null);
        }
    }
}

