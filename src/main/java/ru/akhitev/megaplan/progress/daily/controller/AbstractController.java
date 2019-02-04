package ru.akhitev.megaplan.progress.daily.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AbstractController {
    protected void showMessage(String message) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        Button ok = new Button("Ok");
        ok.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> dialogStage.close());
        VBox vBox = new VBox(new Text(message), ok);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(15));
        dialogStage.setScene(new Scene(vBox));
        dialogStage.show();
    }
}
