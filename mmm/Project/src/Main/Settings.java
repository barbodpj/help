package Main;

import Main.StartMenu;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.controlsfx.control.ToggleSwitch;

public class Settings {
    public static void start(StartMenu startMenu , Stage primaryStage) throws Exception {
        primaryStage.setOpacity(1);
        Group root = new Group();
        Scene scene = new Scene(root , StartMenu.WIDTH , StartMenu.HEIGHT);
        primaryStage.setScene(scene);
        Image background = new Image(new FileInputStream("menu/pic/clash.jpg"));
        ImageView imageView = new ImageView(background);
        imageView.setFitWidth(scene.getWidth());
        imageView.setFitHeight(scene.getHeight());
        root.getChildren().add(imageView);
        StartMenu.setCursor(scene);

        Button back = configureBackButton(scene);

        GridPane gridPane = new GridPane();
        gridPane.setTranslateX(scene.getWidth()/2 - 150);
        gridPane.setTranslateY(scene.getHeight()/2 - 70);
        gridPane.setHgap(30);
        gridPane.setVgap(50);
        root.getChildren().add(gridPane);

        Label menuMusicLabel = new Label("Menu Music");
        menuMusicLabel.setFont(Font.loadFont(new FileInputStream("game/font/buttonFont.ttf"),30));
        ToggleSwitch menuMusicSwitch = new ToggleSwitch();
        gridPane.add(menuMusicLabel , 0 , 0);
        gridPane.add(menuMusicSwitch , 2, 0);

        Label gameMusicLabel = new Label("Game Music");
        gameMusicLabel.setFont(Font.loadFont(new FileInputStream("game/font/buttonFont.ttf"),30));
        ToggleSwitch gameMusicSwitch = new ToggleSwitch();
        gridPane.add(gameMusicLabel , 0 ,1);
        gridPane.add(gameMusicSwitch,2,1);

        gameMusicSwitch.setSelected(startMenu.isGameMusicPlaying());
        menuMusicSwitch.setSelected(startMenu.isMenuMusicPlaying());

       gameMusicSwitch.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                startMenu.startStopGameMusic();
            }
        });

       menuMusicSwitch.setOnMouseClicked(new EventHandler<MouseEvent>() {
           @Override
           public void handle(MouseEvent event) {
               startMenu.startStopMenuMusic();
           }
       });

        gridPane.add(back,1,2);

        back.setTranslateX(back.getTranslateX() - 20);




        back.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    primaryStage.setOpacity(0);
                    startMenu.start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



    }

    private static Button configureBackButton(Scene scene) throws FileNotFoundException {
        Button back = new Button("Back");

        Image image = new Image(new FileInputStream("menu/pic/wood.JPG"));
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT
                , BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background buttonBackground = new Background(backgroundImage);

        back.setBackground(buttonBackground);
        back.setMaxWidth(120);
        back.setFont(Font.loadFont(new FileInputStream("menu/font/dark.ttf"), 20));
        back.setTextFill(Color.WHITE);

        back.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Media mediaName = new Media(new File("menu/sound/pop.mp3").toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(mediaName);
                mediaPlayer.play();
                back.setTextFill(Color.RED);
            }
        });

        back.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                back.setTextFill(Color.WHITE);
            }
        });

        return back;
    }
}
