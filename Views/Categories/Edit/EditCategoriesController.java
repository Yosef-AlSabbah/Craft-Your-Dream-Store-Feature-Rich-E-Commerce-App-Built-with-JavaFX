package Views.Categories.Edit;

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

import javax.persistence.PersistenceException;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class EditCategoriesController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextField category_name;

    @FXML
    private JFXButton editCategoryButton;

    private Category category;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    void editCategory(MouseEvent ignored) {
        String name = category_name.getText().trim();
        if (!name.isEmpty()) {
            try {
                category.setName(name);
                DbConnect.updateItem(category);
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Links.SHOW_CATEGORIES_VIEW.getValue())));
                AnchorPane parent = (AnchorPane) stackPane.getParent();
                parent.getChildren().setAll(root);
                DialogTypes.getConfirmationDialog(((StackPane) parent.getParent().getParent()), "Updated Successfully", "Category Updated Successfully!", 100).show();
            } catch (PersistenceException ignored1) {
                Notifications.createFailNotification("Choose a unique name!", "Category name already exists!", stackPane.getScene(), Pos.TOP_CENTER);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public EditCategoriesController(Category category) {
        this.category = category;
    }

    public EditCategoriesController() {
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
        this.category_name.setText(category.getName());
    }
}
