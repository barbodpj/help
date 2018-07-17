package Main;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import view.View;
import view.Window;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class StartMenu extends Application {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 400;
    private Media mediaName = new Media(new File("menu/sound/music.mp3").toURI().toString());
    private MediaPlayer mediaPlayer = new MediaPlayer(mediaName);
    private static final String GAME_NAME = "Clash";
    private boolean gameMusicPlaying = true;
    private boolean menuMusicPlaying = true;

    public boolean isMenuMusicPlaying() {
        return menuMusicPlaying;
    }

    public boolean isGameMusicPlaying() {

        return gameMusicPlaying;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setOpacity(1);
        Group root = new Group();
        Scene startScene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setTitle(GAME_NAME);
        primaryStage.setResizable(false);
        Image background = new Image(new FileInputStream("menu/pic/clash.jpg"));
        ImageView imageView = new ImageView(background);
        imageView.setFitWidth(startScene.getWidth());
        imageView.setFitHeight(startScene.getHeight());
        root.getChildren().add(imageView);
        primaryStage.setScene(startScene);

        if (menuMusicPlaying)
            mediaPlayer.play();
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });


        String greeting = "Welcome To " + GAME_NAME + "!";
        Label welcome = new Label(greeting);
        welcome.setTextFill(Color.BLACK);
        welcome.setTranslateX(20);
        welcome.setTranslateY(20);
        root.getChildren().add(welcome);
        FileInputStream fileInputStream = new FileInputStream(new File("menu/font/dark.ttf"));
        welcome.setFont(Font.loadFont(fileInputStream, 30));
        setCursor(startScene);

        GridPane gridPane = new GridPane();
        root.getChildren().add(gridPane);


        Button play = new Button("Play");
        Button load = new Button("load");
        Button settings = new Button("Settings");
        Button exit = new Button("Quit");

        Image image = new Image(new FileInputStream("menu/pic/wood.JPG"));
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT
                , BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background buttonBackground = new Background(backgroundImage);

        configureMenuButton(play);
        configureMenuButton(settings);
        configureMenuButton(exit);
        configureMenuButton(load);

        exit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.exit(1);
            }
        });

        play.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                newGame(primaryStage);
            }
        });

        load.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                getLoadMapPath(primaryStage);
            }
        });

        settings.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    primaryStage.setOpacity(0);
                    Settings.start(StartMenu.this, primaryStage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        final int[] selected = {1};

        ImageView radiButton = new ImageView(new Image(new FileInputStream("menu/pic/radioButton.png"), 30, 30, false, true));
        gridPane.add(play, 1, 1);
        gridPane.add(settings, 1, 3);
        gridPane.add(exit, 1, 4);
        gridPane.add(load, 1, 2);
        gridPane.add(radiButton, 0, selected[0]);

        gridPane.setVgap(10);
        gridPane.setHgap(20);
        gridPane.setTranslateX(70);
        gridPane.setTranslateY(90);

        EventHandler<KeyEvent> eventEventHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.UP)
                    selected[0] -= 1;
                if (event.getCode() == KeyCode.DOWN)
                    selected[0] += 1;

                selected[0] += 4;
                selected[0] %= 4;
                if (selected[0] == 0)
                    selected[0] = 4;

                GridPane.setRowIndex(radiButton, selected[0]);

            }
        };

        play.addEventFilter(KeyEvent.KEY_PRESSED, eventEventHandler);
        settings.addEventFilter(KeyEvent.KEY_PRESSED, eventEventHandler);
        exit.addEventFilter(KeyEvent.KEY_PRESSED, eventEventHandler);
        load.addEventFilter(KeyEvent.KEY_PRESSED, eventEventHandler);

        EventHandler<KeyEvent> exitHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    switch (selected[0]) {
                        case 4:
                            System.exit(1);
                            break;
                        case 3:
                            try {
                                primaryStage.setOpacity(0);
                                Settings.start(StartMenu.this, primaryStage);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case 2:
                            //TODO
                            break;
                        case 1:
                            newGame(primaryStage);
                    }
                }
            }
        };

        play.addEventFilter(KeyEvent.KEY_PRESSED, exitHandler);
        settings.addEventFilter(KeyEvent.KEY_PRESSED, exitHandler);
        exit.addEventFilter(KeyEvent.KEY_PRESSED, exitHandler);

        primaryStage.show();


    }

    public static void setCursor(Scene startScene) throws FileNotFoundException {
        ImageCursor imageCursor = new ImageCursor(new Image(new FileInputStream("menu/pic/cursor.png")));
        startScene.setCursor(imageCursor);
    }

    private void newGame(Stage primaryStage) {

        try {
            mediaPlayer.stop();
            menuMusicPlaying = false;

            if (gameMusicPlaying) {
                StartMenu.this.mediaName = new Media(new File("game/sound/background.mp3").toURI().toString());
                mediaPlayer = new MediaPlayer(mediaName);
                mediaPlayer.play();
            }

            Main.main(primaryStage , true  , "" , this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configureMenuButton(Button button) throws FileNotFoundException {
        Image image = new Image(new FileInputStream("menu/pic/wood.JPG"));
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT
                , BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background buttonBackground = new Background(backgroundImage);

        button.setBackground(buttonBackground);
        button.setMinWidth(170);
        button.setFont(Font.loadFont(new FileInputStream("menu/font/dark.ttf"), 30));
        button.setTextFill(Color.WHITE);

        button.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                button.setTextFill(Color.RED);
                Media mediaName = new Media(new File("menu/sound/pop.mp3").toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(mediaName);
                mediaPlayer.play();
            }
        });

        button.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                button.setTextFill(Color.WHITE);
            }
        });

    }

    public void startStopMenuMusic() {
        if (isMenuMusicPlaying()) {
            mediaPlayer.stop();
            menuMusicPlaying = false;
        } else {
            mediaPlayer.stop();
            Media mediaName = new Media(new File("menu/sound/music.mp3").toURI().toString());
            mediaPlayer = new MediaPlayer(mediaName);
            mediaPlayer.play();
            menuMusicPlaying = true;
        }

    }

    public void startStopGameMusic() {
        if (isGameMusicPlaying())
            gameMusicPlaying = false;
        else
            gameMusicPlaying = true;
    }


    public void getLoadMapPath(Stage primaryStage) {
        Stage stage = Window.getNewStage();
        HBox hBox = new HBox();
        hBox.setTranslateX(130);
        stage.show();
        hBox.setTranslateY(60);

        TextField path = new TextField();
        path.setPrefWidth(300);
        path.setPrefWidth(300);
        path.setPromptText("Enter map path");
        Button button = new Button("...");
        hBox.setSpacing(3);
        hBox.getChildren().addAll(path, button);
        ((Group) stage.getScene().getRoot()).getChildren().add(hBox);
        path.getParent().requestFocus();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("map path");
        fileChooser.setInitialDirectory(new File("./"));


        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        Button okButton = new Button("Ok");

        try {
            View.configureGameButton(okButton);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        okButton.setMinWidth(140);

        okButton.setTranslateY(120);
        okButton.setTranslateX(230);
        ((Group) stage.getScene().getRoot()).getChildren().add(okButton);

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                File selectedFile = fileChooser.showOpenDialog(primaryStage);
                stage.setAlwaysOnTop(true);

                try {
                    int t=3;
                    path.setText(selectedFile.getPath());
                } catch (NullPointerException e) {
                    ;
                }

            }
        });

        StartMenu thisMenu = this;
        okButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    mediaPlayer.stop();
                    menuMusicPlaying = false;

                    if (gameMusicPlaying) {
                        StartMenu.this.mediaName = new Media(new File("game/sound/background.mp3").toURI().toString());
                        mediaPlayer = new MediaPlayer(mediaName);
                        mediaPlayer.play();
                    }

                    Main.main(primaryStage , false  , path.getText() , thisMenu);
                    stage.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
