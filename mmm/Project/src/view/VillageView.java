package view;

import constants.enums.BuildingType;
import constants.enums.Section;
import constants.enums.TroopType;
import controller.Controller;
import controller.MenuController;
import controller.exception.InvalidCommandException;
import controller.exception.gameException.LowResourcesException;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class VillageView {

    private Controller controller;
    
    public void init(Controller controller) {
        this.controller = controller;
    }

    public String yesNoQuestion() throws InvalidCommandException {
        String commandLine = View.scanner.nextLine();
        if(commandLine.matches("Y") || commandLine.matches("N")) {
            return commandLine;
        }
        throw new InvalidCommandException();
    }

    public String getLocation() throws InvalidCommandException {
        String commandLine = View.scanner.nextLine();
        if(commandLine.matches("\\(\\d+, \\d+\\)")) {
            return commandLine;
        }
        throw new InvalidCommandException();
    }

    public void getTroopNumber(int max , int typeNumber) throws InvalidCommandException, FileNotFoundException {
        Stage stage = Window.getNewStage();
        Label label = new Label("How many of this soldier do you want to train?");
        label.setFont(Font.loadFont(new FileInputStream("game/font/buttonFont.ttf") , 30));
        label.setTextFill(Color.BLACK);
        label.setTranslateX(40);
        label.setTranslateY(50);

        ChoiceBox choiceBox = new ChoiceBox();
        for( int i = 1  ; i <= max ; i++)
        {
            choiceBox.getItems().add(i);
        }

        choiceBox.setTranslateX(Window.WIDTH / 2);
        choiceBox.setTranslateY(100);
        Button okButton = new Button("Ok");
        View.configureGameButton(okButton);
        okButton.setTranslateX(Window.WIDTH/2 - 70);
        okButton.setTranslateY(150);

        okButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int amount = ((Integer)choiceBox.getValue());
                try {
                    controller.getVillageController().train(amount,typeNumber);
                    stage.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        ((Group)stage.getScene().getRoot()).getChildren().addAll(label , choiceBox , okButton);
        stage.show();

    }

    public String getTarget() throws InvalidCommandException {
        String commandLine = View.scanner.nextLine();
        for(TroopType type: TroopType.values()) {
            if(commandLine.matches(type.getValue())) {
                return commandLine;
            }
        }
        throw new InvalidCommandException();
    }

}
