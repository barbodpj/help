package model;

import Main.Client;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import view.View;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ChatroomUI extends Application {
    private Stage stage;
    private Scene scene;
    private Group root = new Group();
    View view;
    public ChatroomUI(View view)
    {
        this.view = view;
    }

    public TextArea getTextArea() {
        return textArea;
    }

    private TextArea textArea;

    public static final int WIDTH = 500;
    public static final int HEIGHT = 600;

    public void init(Client client) throws FileNotFoundException {
        scene = new Scene(root, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.setX(100);
        stage.setY(100);
        stage.setResizable(false);
        ImageView imageView = null;
        try {
            imageView = new ImageView(new Image(new FileInputStream("game/pic/chatBackground.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        root.getChildren().add(imageView);
        textArea = new TextArea();
        textArea.setTranslateY(20);
        textArea.setPrefWidth(ChatroomUI.WIDTH - 10);
        textArea.setPrefHeight(ChatroomUI.HEIGHT - 70);
        textArea.setFont(Font.loadFont(new FileInputStream("game/font/buttonFont.ttf"), 25));
        scene.getStylesheets().add("textArea.css");
        root.getChildren().add(textArea);

        TextField textField = new TextField();
        textField.setPrefWidth(ChatroomUI.WIDTH - 10);

        textArea.setEditable(false);

        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (textField.getText().matches("\\s*"))
                    return;
                if (event.getCode() == KeyCode.ENTER) {
                    try {
                        view.getClient().sendMessageToServer(view.getName() + ": " + textField.getText());
                        textField.clear();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        root.getChildren().add(textField);

        textField.setTranslateY(ChatroomUI.HEIGHT - 40);


    }

    public void setText(String text){
        textArea.setText(text);
    }

    public void addText(String text) {
        if (!textArea.getText().equals(""))
            textArea.setText(textArea.getText() + "\n\n" + text);
        else
            textArea.setText(text);
    }

    public void hide() {
        stage.hide();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
    }


    public void show() {
        stage.show();
    }

}
