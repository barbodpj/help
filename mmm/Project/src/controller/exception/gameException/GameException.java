package controller.exception.gameException;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Village;
import view.View;
import view.Window;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

//TODO mvc
public class GameException extends Exception {
    protected String message;

    public void printAnnouncement() {
        Stage stage = Window.getNewStage();
        Label label = new Label(message);
        try {
            label.setFont(Font.loadFont(new FileInputStream("game/font/buttonFont.ttf"), 25));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        label.setTextFill(Color.RED);

        ((Group) (stage.getScene().getRoot())).getChildren().add(label);

        label.setTranslateY(60);
        Button okButton = new Button("Ok");
        try {
            View.configureGameButton(okButton);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        okButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.close();
            }
        });

        okButton.setMinWidth(70);
        okButton.setTranslateX(Window.WIDTH / 2 - 35);
        okButton.setTranslateY(150);
        ((Group) (stage.getScene().getRoot())).getChildren().add(okButton);
        stage.show();


    }

}