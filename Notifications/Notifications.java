package Notifications;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Notifications {
    private Notifications() {
    }

    private static org.controlsfx.control.Notifications notifications;

    private static org.controlsfx.control.Notifications getInstance() {
        if (notifications == null) notifications = org.controlsfx.control.Notifications.create();
        return notifications;
    }

    private static void createNotification(String title, String message, Scene owner, Pos pos) {
        owner.getStylesheets().add("/Views/Categories/Create/CSS/style.css");
        getInstance()
                .title(title)
                .text(message)
                .hideAfter(Duration.seconds(200))
                .position(pos)
                .darkStyle();
        notifications.hideAfter(new Duration(5000));
    }

    private static final Image successIcon = new Image("file:src/Notifications/Resources/Images/correct.png");
    private static final Image failIcon = new Image("file:src/Notifications/Resources/Images/cross.png");

    public static void createSuccessNotification(String title, String message, Scene owner, Pos pos) {
        createNotification(title, message, owner, pos);
        notifications.showConfirm();
        notifications.show();
    }

    public static void createFailNotification(String title, String message, Scene owner, Pos pos) {
        createNotification(title, message, owner, pos);
        notifications.show();
    }
}

