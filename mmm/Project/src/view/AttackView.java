package view;

import constants.enums.BuildingType;
import constants.enums.TroopType;
import controller.Controller;
import controller.exception.InvalidCommandException;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Village;
import model.building.Building;
import model.troop.Troop;
import model.troop.army.Guardian;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class AttackView {

    private Scene scene;

    private ImageView map;

    private Stage stage;

    Controller controller;

    AnimationTimer animationTimer;

    public Troop selectedTroop = null;

    private GridPane menuPane = new GridPane();
    private GridPane informationPane = new GridPane();
    private Label informationLabel = new Label();

    private Scene villageScene;

    public String getMapPath() {
        return View.scanner.nextLine();
    }

    public void init(Controller controller) {
        this.controller = controller;
    }

    public void setVillageScene(Scene villageScene) {
        this.villageScene = villageScene;
    }

    public String selectUnits() throws InvalidCommandException {
        String commandLine = View.scanner.nextLine();

        if (commandLine.matches("End select")) {
            return commandLine;
        }
        for (TroopType type : TroopType.values()) {
            if (commandLine.matches("Select " + type.getValue() + " \\d+")) {
                return commandLine;
            }
        }
        throw new InvalidCommandException();
    }

    public String startSelection() throws InvalidCommandException {
        String commandLine = View.scanner.nextLine();
        if (commandLine.matches("Start select")) {
            return commandLine;
        }
        throw new InvalidCommandException();
    }

    public String putUnits() throws InvalidCommandException {
        String commandLine = View.scanner.nextLine();

        if (commandLine.matches("Go next turn")) {
            return commandLine;
        }
        for (TroopType type : TroopType.values()) {
            if (commandLine.matches("Put " + type.getValue() + " \\d+ in \\d+,\\d+")) {
                return commandLine;
            }
        }
        throw new InvalidCommandException();
    }

    public String getAttackCommand() throws InvalidCommandException {
        String commandLine = View.scanner.nextLine();

        if (commandLine.matches("status resources") || commandLine.matches("status units") || commandLine.matches("status towers") || commandLine.matches("status all")) {
            return commandLine;
        }
        if (commandLine.matches("Quit attack") || commandLine.matches("turn \\d+")) {
            return commandLine;
        }
        for (TroopType type : TroopType.values()) {
            if (commandLine.matches("status unit " + type.getValue())) {
                return commandLine;
            }
        }
        for (BuildingType type : BuildingType.values()) {
            if (commandLine.matches("status tower " + type.getValue())) {
                return commandLine;
            }
        }
        throw new InvalidCommandException();
    }

    public void configureAttackScene(Stage stage) throws FileNotFoundException {
        this.stage = stage;
        Group root = new Group();
        this.scene = new Scene(root, View.WIDTH, View.HEIGHT);
        scene.getStylesheets().add("style.css");
        configureInformationLabel();

        this.scene.setFill(Color.BLACK);
        javafx.scene.image.ImageView map = new ImageView(new Image(new FileInputStream("game/pic/map.jpg")));
        this.map = map;
        map.setTranslateX(View.WIDTH - View.MAP_SIZE);
        this.informationPane.setMinWidth(View.WIDTH - View.MAP_SIZE);
        this.menuPane.setHgap(40);
        this.menuPane.setVgap(5);
        root.getChildren().add(map);

        root.getChildren().add(controller.getView().attackMap(map));

        ImageView hall = new ImageView(new Image(new FileInputStream("game/pic/hall.jpg")));
        hall.setFitWidth(View.WIDTH - View.MAP_SIZE);
        hall.setFitHeight(View.MAP_SIZE);
        hall.setTranslateY(50);
        root.getChildren().add(hall);

        ScrollPane menuScrollPane = new ScrollPane();
        menuScrollPane.setContent(menuPane);
        menuScrollPane.setMaxHeight(280);
        menuScrollPane.setMaxWidth(208);
        menuScrollPane.setMinWidth(188);
        menuScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


        Label informationLabel = new Label();
        informationLabel.setFont(Font.loadFont(new FileInputStream("game/font/buttonFont.ttf"), 30));
        informationLabel.setTextFill(Color.YELLOW);

        UIShowAllTroops(controller.getAttackController().getSelectedTroops());

        configureSpeed(root);
        configureToolbar(root);

//        VBox vBox = new VBox();
//        vBox.setSpacing(10);
//        ScrollPane informationLabelScrollPane = new ScrollPane();
//        informationLabelScrollPane.setContent(informationLabel);
//        informationLabelScrollPane.setMaxHeight(300);
//        vBox.getChildren().add(informationLabelScrollPane);
//        this.informationPane.add(vBox, 0, 0);
//        this.informationLabel = informationLabel;


        this.informationPane.add(menuScrollPane, 0, 0);
        this.informationPane.setVgap(40);
        this.informationPane.setTranslateX(30);
        this.informationPane.setTranslateY(60);


        ImageCursor imageCursor = new ImageCursor(new Image(new FileInputStream("menu/pic/cursor.png")));
        scene.setCursor(imageCursor);

        informationPane.add(informationLabel , 0 , 1);
        root.getChildren().add(informationPane);


        stage.setScene(scene);



    }

    private void configureInformationLabel() throws FileNotFoundException {
        this.informationLabel.setTextFill(Color.YELLOW);
        informationLabel.setFont(Font.loadFont(new FileInputStream("game/font/buttonFont.ttf"), 25));
        informationLabel.setTranslateX(30);
        informationLabel.setTranslateY(400);
    }

    public void updateInformationLabel() {

    }

    public void UIShowAllTroops(HashMap<TroopType, ArrayList<Troop>> troops) throws FileNotFoundException {
        menuPane.getChildren().clear();
        int number = 0;
        int row = 0;

        boolean flag = false;

        for (TroopType troopType : troops.keySet()) {
            number = 0;
            for (Troop troop : troops.get(troopType)) {
                number++;
                row++;
                String name = troop.getClass().getSimpleName() + number;
                Button troopButton = new Button(name);
                View.configureGameButton(troopButton);
                menuPane.add(troopButton, 0, row);
                flag = true;
                troopButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        selectedTroop = troop;
                        menuPane.getChildren().remove(troopButton);
                        if(menuPane.getChildren().size() == 0 )
                        {
                            Button button = new Button("No troops");
                            try {
                                View.configureGameButton(button);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            menuPane.add(button , 0 , 0);
                        }
                    }
                });
            }
        }

        if(!flag){
            Button button = new Button("No troops");
            View.configureGameButton(button);
            menuPane.add(button , 0 , 0);
        }


    }

    private void configureSpeed(Group root) throws FileNotFoundException {
        ScrollBar scrollBar = new ScrollBar();
        scrollBar.setPrefWidth(200);
        scrollBar.setTranslateX(View.WIDTH - View.MAP_SIZE/2 - 200);
        scene.getStylesheets().add("scroll-bar.css");
        scrollBar.setTranslateY(10);
        root.getChildren().add(scrollBar);
        scrollBar.setMin(1);
        scrollBar.setMax(10);

        scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                controller.getAttackController().setSpeed(scrollBar.getValue());
                updateInformationLabel();
            }
        });

        ImageView pauseImage = new ImageView(new Image(new FileInputStream("game/pic/pause.png")));
        pauseImage.setFitHeight(30);
        pauseImage.setFitWidth(30);
        root.getChildren().add(pauseImage);

        pauseImage.setX(View.WIDTH - View.MAP_SIZE/2 + 30);
        pauseImage.setY(3);
        pauseImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                HBox hBox = new HBox();
                hBox.setPrefWidth(View.WIDTH);
                hBox.setPrefHeight(View.HEIGHT);
                hBox.setStyle("-fx-background-color: transparent");
                Label label = new Label("Paused");
                label.setTextFill(Color.BLACK);
                try {
                    label.setFont(Font.loadFont(new FileInputStream("game/font/buttonFont.ttf"), 60));
                    label.setTranslateY(View.HEIGHT/2 - 70);
                    label.setTranslateX(View.WIDTH/2 );
                    Button resumeButton = new Button("Resume");
                    View.configureGameButton(resumeButton);
                    resumeButton.setTranslateY(View.HEIGHT/2 + 30 );
                    resumeButton.setTranslateX(View.WIDTH/2 - 15);
                    root.getChildren().add(label);
                    root.getChildren().add(hBox);
                    root.getChildren().add(resumeButton);
                    controller.getAttackController().pause();
                    resumeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            root.getChildren().remove(hBox);
                            root.getChildren().remove(resumeButton);
                            root.getChildren().remove(label);
                            controller.getAttackController().pause();
                        }
                    });





                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void configureToolbar(Group root) throws FileNotFoundException {
        HBox toolbar = new HBox();
        toolbar.setPrefWidth(View.WIDTH - View.MAP_SIZE );
        double space = (View.WIDTH - View.MAP_SIZE - 100)/3;
        toolbar.setSpacing(space);
        toolbar.setStyle("-fx-background-color: black");
        toolbar.setPadding(new Insets(0 , 0 , 0 , space));

        ImageView exitImage = new ImageView(new Image(new FileInputStream("game/pic/exit.png")));


        exitImage.setFitWidth(50);
        exitImage.setFitHeight(50);

        exitImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    controller.getAttackController().stopAttack();
                    stage.setScene(villageScene);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        ImageView attackImage = new ImageView(new Image(new FileInputStream("game/pic/attack.png")));
        attackImage.setFitWidth(50);
        attackImage.setFitHeight(50);
        attackImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                new AnimationTimer() {
                    @Override
                    public void handle(long now) {
                        if(controller.getAttackController().isAttackFinished()) {
                            animationTimer.stop();
                            controller.getAttackController().attackerVillage.addScore(controller.getAttackController().scoreGained);
                            controller.getAttackController().scoreGained = (int)(Math.random() * 40);
                            Platform.runLater(() -> {
                                Optional<ButtonType> answer = new Alert(Alert.AlertType.INFORMATION, "The attacker has gained " + controller.getAttackController().scoreGained).showAndWait();
                                if (answer.isPresent()) {
                                    if (answer.get() == ButtonType.OK) {
                                        controller.getAttackController().stopAttack();
                                        stage.setScene(villageScene);
                                    }
                                }
                            });
                            stop();
                        }
                    }
                }.start();
                controller.getView().putMode = false;
                animationTimer = new AnimationTimer() {
                    @Override
                    public void handle(long now) {
                        controller.getView().updateAttackMap();
                        controller.getView().remove();
                    }
                };
                animationTimer.start();

            }
        });

        toolbar.setTranslateX(0);


        toolbar.getChildren().addAll(exitImage, attackImage);
        root.getChildren().add(toolbar);

        View.configureMouseEntered(exitImage);
        View.configureMouseEntered(attackImage);
    }


    public void showTroopStatus(Troop troop)
    {
        Platform.runLater(() -> {
            Optional<ButtonType> answer = new Alert(Alert.AlertType.INFORMATION, troop.getClass().getSimpleName() + "\n" + "Level: " +troop.getLevel() + "\nLocation: (" + troop.getLocation().getX() + " ," + troop.getLocation().getY() + ")" + "\n health: " + troop.getHealthInWar()).showAndWait();
            if (answer.isPresent()) {
                if (answer.get() == ButtonType.OK) {

                }
            }
        });
    }

    public void showBuildingStatus(Building building)
    {
        Platform.runLater(() -> {
            Optional<ButtonType> answer = new Alert(Alert.AlertType.INFORMATION, building.getClass().getSimpleName() + "\n" + "Level: " +building.getLevel() + "\nLocation: (" + building.getLocation().getX() + " ," + building.getLocation().getY() + ")" + "\n health: " + building.getHealthInWar()).showAndWait();
            if (answer.isPresent()) {
                if (answer.get() == ButtonType.OK) {

                }
            }
        });


    }
}



