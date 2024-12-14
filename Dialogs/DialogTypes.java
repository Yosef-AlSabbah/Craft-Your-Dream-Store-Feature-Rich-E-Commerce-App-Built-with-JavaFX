package Dialogs;

import animatefx.animation.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

public final class DialogTypes {

    public static JFXDialog getDialog(StackPane stackPane, ImageView icon, String headerText, String bodyText, Pane buttonsContainer, JFXButton closingButton, double animationsDelay) {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        icon.setFitHeight(120);
        icon.setFitWidth(120);

        HBox iconHBox = new HBox(icon);
        iconHBox.getStyleClass().add("icon-box");
        iconHBox.setAlignment(Pos.CENTER);
        AnimationFX iconAnimation2 = new ZoomIn(icon);
        iconAnimation2.setDelay(new Duration(animationsDelay - 100 >= 0? animationsDelay - 100 : 20));
        iconAnimation2.play();
        AnimationFX iconAnimation = new Jello(icon);
        iconAnimation.setDelay(new Duration(animationsDelay));
        iconAnimation.play();

        dialogLayout.setHeading(iconHBox);
        dialogLayout.getStyleClass().add("my-dialog");

        Text bodyHeaderText = new Text(headerText);
        bodyHeaderText.getStyleClass().add("bodyHeaderText");
        Text bodyBodyText = new Text(bodyText);
        bodyBodyText.getStyleClass().add("bodyBodyText");

        VBox bodyContainer = new VBox(20, bodyHeaderText, bodyBodyText, buttonsContainer);
        buttonsContainer.getStyleClass().add("btn-container");
        bodyContainer.setAlignment(Pos.CENTER);
        dialogLayout.setBody(bodyContainer);

        JFXDialog dialog = new JFXDialog();
        dialog.setDialogContainer(stackPane);
        dialog.setContent(dialogLayout);
        dialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        closingButton.setOnAction(e -> dialog.close());

        AnimationFX animation = new Pulse(dialog);
        animation.setDelay(new Duration(animationsDelay));
        animation.setSpeed(1.5);
        animation.play();
        return dialog;
    }

    public static JFXDialog getConfirmationDialog(StackPane stackPane, String headerText, String bodyText, double animationsDelay) {
        ImageView icon = new ImageView(new Image("Dialogs/Resources/Images/Confirmation.png"));
        JFXButton okButton = new JFXButton("OK");
        okButton.getStyleClass().add("ok");
        HBox buttonsContainer = new HBox(okButton);
        buttonsContainer.setAlignment(Pos.CENTER);
        return getDialog(stackPane, icon, headerText, bodyText, buttonsContainer, okButton, animationsDelay);
    }

    public static JFXDialog getWarningDialog(StackPane stackPane, String headerText, String bodyText, String confirmText, String rejectText, double animationsDelay, EventHandler<ActionEvent> confirmAction) {
        ImageView icon = new ImageView(new Image("Dialogs/Resources/Images/Warning.png"));

        JFXButton confirmButton = new JFXButton(confirmText);

        confirmButton.getStyleClass().add("confirm");

        JFXButton rejectButton = new JFXButton(rejectText);
        rejectButton.getStyleClass().add("reject");

        confirmButton.setOnAction(e -> {
            confirmAction.handle(e);
            rejectButton.fire();
        });

        HBox buttonsContainer = new HBox(20, confirmButton, rejectButton);
        buttonsContainer.setAlignment(Pos.CENTER);
        return getDialog(stackPane, icon, headerText, bodyText, buttonsContainer, rejectButton, animationsDelay);
    }

    public static JFXDialog getErrorDialog(StackPane stackPane, String headerText, String bodyText, double animationsDelay) {
        ImageView icon = new ImageView(new Image("Dialogs/Resources/Images/Error.png"));
        JFXButton okButton = new JFXButton("OK");
        okButton.getStyleClass().add("ok");
        HBox buttonsContainer = new HBox(okButton);
        buttonsContainer.setAlignment(Pos.CENTER);
        return getDialog(stackPane, icon, headerText, bodyText, buttonsContainer, okButton, animationsDelay);
    }
}