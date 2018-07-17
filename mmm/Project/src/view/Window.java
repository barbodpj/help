package view;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.troop.airForce.Healer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Window {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 250;

    public static Stage getNewStage()
    {
        Stage stage = new Stage();
        stage.setX(150);
        stage.setY(150);
        stage.setResizable(false);
        Group root = new Group();
        Scene scene = new Scene(root , WIDTH , HEIGHT);
        stage.setScene(scene);
        Image image = null;
        try {
            image = new Image(new FileInputStream("game/pic/window.jpg"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(WIDTH);
        imageView.setFitHeight(HEIGHT);
        root.getChildren().add(imageView);
        return stage;

    }
}
