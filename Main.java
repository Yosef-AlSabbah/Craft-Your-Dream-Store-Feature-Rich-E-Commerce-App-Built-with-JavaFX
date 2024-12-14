import Views.Links;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.logging.Logger;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Links.ADMIN_DASHBOARD.getValue())));
        Scene scene = new Scene(root);
        primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("file: /FixedFrame/images/icon.ico"));
        primaryStage.show();
    }

    public static void main(String[] args) {
        Logger rootLogger = Logger.getLogger("");

//        rootLogger.setLevel(Level.OFF);
        launch(args);
    }
}
