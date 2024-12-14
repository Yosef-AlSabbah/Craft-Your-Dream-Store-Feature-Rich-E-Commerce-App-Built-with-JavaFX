package Views.Categories.Create;

import Dialogs.DialogTypes;
import Models.Category;
import Notifications.Notifications;
import Views.Links;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import helpers.DbConnect;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javax.persistence.PersistenceException;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class CreateCategoriesController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextField category_name;

    @FXML
    private JFXButton addCategoryButton;

    @FXML
    private Text categoryNameText;


    @FXML
    void createCategory(MouseEvent ignored) {
        String name = category_name.getText().trim();
        if (!name.isEmpty()) {
            try {
                setCategoryNameText("Make sure to use a unique name.", Color.web("#575962"));
                DbConnect.addItem(new Category(name));
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Links.SHOW_CATEGORIES_VIEW.getValue())));
                AnchorPane parent = (AnchorPane) stackPane.getParent();
                parent.getChildren().setAll(root);
                DialogTypes.getConfirmationDialog(((StackPane) parent.getParent().getParent()), "Added Successfully", "Category Added Successfully!", 50).show();
            } catch (PersistenceException ignored1) {
                Notifications.createFailNotification("Choose a unique name!", "Category name already exists!", stackPane.getScene(), Pos.TOP_CENTER);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Notifications.createFailNotification("Fill Category name!", "Category name is required!", stackPane.getScene(), Pos.TOP_CENTER);
            setCategoryNameText("Category names is required!", Color.DARKRED);
        }
    }

    private void setCategoryNameText(String text, Color color) {
        categoryNameText.setText(text);
        categoryNameText.setFill(color);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
