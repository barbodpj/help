package view;


import Main.Main;
import Main.StartMenu;
import constants.enums.BuildingType;
import constants.enums.Section;
import constants.enums.TroopType;
import controller.Controller;
import controller.MenuController;
import controller.exception.InvalidCommandException;
import controller.exception.gameException.InvalidAttackCellException;
import controller.exception.gameException.InvalidCellException;
import javafx.animation.AnimationTimer;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.ChatroomUI;
import model.Location;
import model.building.Building;
import model.building.headquarter.Builder;
import model.map.Map;
import model.troop.Troop;
import model.troop.airForce.Healer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.security.Key;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import Main.Client;

//TODO change menu later and add back feature
@SuppressWarnings("Duplicates")
public class View {
    static Scanner scanner = new Scanner(System.in);
    private MenuController menuController;
    private VillageView villageView;
    private AttackView attackView;
    private Stage primaryStage;
    private Scene scene;
    private GridPane menuPane = new GridPane();
    private GridPane informationPane = new GridPane();
    private Controller controller;
    private static final String MIDDLE = " ";
    private double zoom = 1;
    private ImageView[][] mapNodes = new ImageView[30][30];
    private Node[][] grassNodes = new Node[30][30];
    private String[][] files = new String[30][30];
    private ImageView[][] newBuildings = new ImageView[30][30];
    private ImageView map;
    private Group root;
    public boolean buildMode = false;
    private Builder builder;
    private Integer num;
    public boolean update = false;
    public boolean putMode = true;
    private ChatroomUI chatroomUI;
    private Client client;
    private String name;

    public Client getClient() {
        return client;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static final int MAP_SIZE = 544;
    public static final int WIDTH = MAP_SIZE + 250;
    public static final int HEIGHT = MAP_SIZE;

    public boolean nextState = false;
    private int step = 0;
    public static final int NUMBER_OF_STATES = 10;
    private double attackZoom = 1;
    private ImageView[][] attackMapNodes = new ImageView[30][30];
    private Node[][] attackGrassNodes = new Node[30][30];
    private ArrayList<TroopsGUI> troops = new ArrayList<>();
    private String[][] attackFiles = new String[30][30];
    private ImageView attackMap;
    private Group attackRoot = new Group();
    private Group groundTroopRoot = new Group();
    private Group airTroopRoot = new Group();

    public Group getAttackRoot() {
        return attackRoot;
    }


    private StartMenu startMenu;


    public  void showLeaderBoard(String line){
        Stage stage = Window.getNewStage();
        Label label = new Label("line");
        ((Group) stage.getScene().getRoot()).getChildren().add(label);
        stage.show();
    }

    public String getName() {
        return name;
    }

    public View(MenuController menuController, Stage primaryStage , StartMenu startMenu , Client client) throws FileNotFoundException {

        primaryStage.setX(100);
        primaryStage.setY(100);
        this.startMenu = startMenu;
        this.client = client;

        Stage nameStage = Window.getNewStage();
        TextField textField = new TextField();

        textField.setPrefWidth(300);
        textField.setPromptText("Enter your name");
        textField.setTranslateX(130);
        textField.setTranslateY(60);
        Button button = new Button("ok");
        configureGameButton(button);
        button.setMinWidth(140);

        button.setTranslateY(120);
        button.setTranslateX(230);


        new AnimationTimer() {
            @Override
            public void handle(long now) {
//                System.out.println("in view:    " + client.getChatText());
                    chatroomUI.setText(client.getChatText());
            }
        }.start();

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                name = textField.getText();
                nameStage.close();
            }
        });

        nameStage.setAlwaysOnTop(true);

        ((Group) nameStage.getScene().getRoot()).getChildren().addAll(textField , button);
        nameStage.show();

        this.menuController = menuController;
        villageView = new VillageView();
        attackView = new AttackView();
        this.primaryStage = primaryStage;
        map.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.L)
                {
                    showLeaderBoard("");
                }
            }
        });
    }

    public void init(Controller controller) throws FileNotFoundException {
        ChatroomUI chatroomUI = new ChatroomUI(this);
        try {
            Stage stage = new Stage();
            chatroomUI.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        chatroomUI.init(client);
        this.chatroomUI = chatroomUI;

        this.controller = controller;
        attackView.init(controller);
        villageView.init(controller);
        Group root = new Group();
        this.scene = new Scene(root, WIDTH, HEIGHT);
        scene.getStylesheets().add("style.css");
        this.scene.setFill(Color.BLACK);
        javafx.scene.image.ImageView map = new ImageView(new Image(new FileInputStream("game/pic/map.jpg")));
        this.map = map;
        map.setTranslateX(WIDTH - MAP_SIZE);
        this.informationPane.setMinWidth(WIDTH - MAP_SIZE);
        this.menuPane.setHgap(40);
        this.menuPane.setVgap(5);
        root.getChildren().add(map);
        map(root);
        ImageView hall = new ImageView(new Image(new FileInputStream("game/pic/hall.jpg")));
        hall.setFitWidth(this.WIDTH - MAP_SIZE);
        hall.setFitHeight(MAP_SIZE);
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


        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.getChildren().addAll(informationLabel);
        this.informationPane.add(vBox, 0, 0);

        configureToolbar(root);



        this.informationPane.add(menuScrollPane, 0, 1);
        this.informationPane.setVgap(40);
        this.informationPane.setTranslateX(30);
        this.informationPane.setTranslateY(60);



        configureSpeed(root);

        ImageCursor imageCursor = new ImageCursor(new Image(new FileInputStream("menu/pic/cursor.png")));
        scene.setCursor(imageCursor);

        root.getChildren().add(informationPane);
        primaryStage.setScene(scene);

        Platform.runLater(new Runnable() {
            public boolean flag = false;

            @Override
            public void run() {
                while (true) {
                    updateInformationLabel();

                    if (flag)
                        break;

                    if (controller.getVillageController() != null)
                        flag = true;
                }
            }
        });



        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(update) {
                    updateMap();
                    update = false;
                }
            }
        }.start();
        this.root = root;


        map.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if(map.getX() + map.getTranslateX() + event.getDeltaX() <= WIDTH - MAP_SIZE && map.getY() + map.getTranslateY() + event.getDeltaY() <= 0 && map.getY() + map.getTranslateY() + event.getDeltaY() + MAP_SIZE * zoom >= HEIGHT && map.getX() + map.getTranslateX() + event.getDeltaX() + MAP_SIZE * zoom >= WIDTH) {
                    map.setTranslateX(map.getTranslateX() + event.getDeltaX());
                    map.setTranslateY(map.getTranslateY() + event.getDeltaY());
                    for (int i = 0; i < Map.HEIGHT; i++) {
                        for (int j = 0; j < Map.WIDTH; j++) {
                            grassNodes[j][i].setTranslateX(grassNodes[j][i].getTranslateX() + event.getDeltaX());
                            grassNodes[j][i].setTranslateY(grassNodes[j][i].getTranslateY() + event.getDeltaY());
                            if(mapNodes[j][i] != null) {
                                mapNodes[j][i].setTranslateX(mapNodes[j][i].getTranslateX() + event.getDeltaX());
                                mapNodes[j][i].setTranslateY(mapNodes[j][i].getTranslateY() + event.getDeltaY());
                            }
                            if(newBuildings[j][i] != null) {
                                newBuildings[j][i].setTranslateX(newBuildings[j][i].getTranslateX() + event.getDeltaX());
                                newBuildings[j][i].setTranslateY(newBuildings[j][i].getTranslateY() + event.getDeltaY());
                            }
                        }
                    }
                }
            }
        });

        map.setOnZoom(new EventHandler<ZoomEvent>() {
            @Override
            public void handle(ZoomEvent event) {
                if(zoom * event.getZoomFactor() > 1 &&  zoom * event.getZoomFactor() < 3) {
                    zoom *= event.getZoomFactor();
                    map.setFitWidth(MAP_SIZE * zoom);
                    map.setFitHeight(MAP_SIZE * zoom);
                    map.setX(WIDTH - MAP_SIZE);
                    map.setY(0);
                    map.setTranslateY(0);
                    map.setTranslateX(0);
                    zoomMap();
                }
            }
        });


        if (menuController.getCurrentSection() == Section.Village) {
            showMenu(controller.getVillageController().getBuildings());
        } else {
            showMenu(menuController.showMenu());
        }
    }

    private void configureSpeed(Group root) throws FileNotFoundException {
        ScrollBar scrollBar = new ScrollBar();
        scrollBar.setPrefWidth(200);
        scrollBar.setTranslateX(WIDTH - MAP_SIZE/2 - 200);
        scene.getStylesheets().add("scroll-bar.css");
        scrollBar.setTranslateY(10);
        root.getChildren().add(scrollBar);
        scrollBar.setMin(1);
        scrollBar.setMax(70);

        scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                controller.getVillageController().setSpeed(scrollBar.getValue());
                updateInformationLabel();
            }
        });

        ImageView pauseImage = new ImageView(new Image(new FileInputStream("game/pic/pause.png")));
        pauseImage.setFitHeight(30);
        pauseImage.setFitWidth(30);
        root.getChildren().add(pauseImage);

        pauseImage.setX(WIDTH - MAP_SIZE/2 + 30);
        pauseImage.setY(3);
        pauseImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                HBox hBox = new HBox();
                hBox.setPrefWidth(WIDTH);
                hBox.setPrefHeight(HEIGHT);
                hBox.setStyle("-fx-background-color: transparent");
                Label label = new Label("Paused");
                label.setTextFill(Color.BLACK);
                try {
                    label.setFont(Font.loadFont(new FileInputStream("game/font/buttonFont.ttf"), 60));
                    label.setTranslateY(HEIGHT/2 - 70);
                    label.setTranslateX(WIDTH/2 );
                    Button resumeButton = new Button("Resume");
                    configureGameButton(resumeButton);
                    resumeButton.setTranslateY(HEIGHT/2 + 30 );
                    resumeButton.setTranslateX(WIDTH/2 - 15);
                    root.getChildren().add(label);
                    root.getChildren().add(hBox);
                    root.getChildren().add(resumeButton);
                    controller.getVillageController().pause();
                    resumeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            root.getChildren().remove(hBox);
                            root.getChildren().remove(resumeButton);
                            root.getChildren().remove(label);
                            controller.getVillageController().pause();
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
        toolbar.setPrefWidth(WIDTH - MAP_SIZE );
        double space = (WIDTH - MAP_SIZE - 200)/5;
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
                    Building.changeNumbers();
                    startMenu.startStopMenuMusic();
                    startMenu.start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ImageView saveImage = new ImageView(new Image(new FileInputStream("game/pic/save.png")));
        saveImage.setFitWidth(50);
        saveImage.setFitHeight(50);
        saveImage.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                controller.save("game/" + name + ".txt");
            }
        });

        ImageView attackImage = new ImageView(new Image(new FileInputStream("game/pic/attack.png")));
        attackImage.setFitWidth(50);
        attackImage.setFitHeight(50);
        attackImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                controller.handleAttack();
            }
        });

        ImageView chatImage= new ImageView(new Image(new FileInputStream("game/pic/chat.png")));
        chatImage.setFitWidth(50);
        chatImage.setFitHeight(50);
        chatImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
               chatroomUI.show();
            }
        });

        configureMouseEntered(chatImage);
        configureMouseEntered(exitImage);
        configureMouseEntered(attackImage);
        configureMouseEntered(saveImage);
        toolbar.setTranslateX(0);


        toolbar.getChildren().addAll(exitImage , saveImage , attackImage , chatImage);
        root.getChildren().add(toolbar);


    }

    public static void configureMouseEntered(ImageView exitImage) {
        exitImage.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setBrightness(0.1);
                exitImage.setEffect(colorAdjust);
            }
        });

        exitImage.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setBrightness(-0.09);
                exitImage.setEffect(colorAdjust);
            }
        });
    }

    public void zoomMap() {
        double start = 4*32*(MAP_SIZE * zoom/(34*32));
        for (int i = 0; i < Map.HEIGHT; i++) {
            for (int j = 0; j < Map.WIDTH; j++) {
                ((ImageView)grassNodes[j][i]).setFitHeight(32*(26.0/30.0)*(MAP_SIZE * zoom/(34*32)));
                ((ImageView)grassNodes[j][i]).setFitWidth(32*(26.0/30.0)*(MAP_SIZE * zoom/(34*32)));
                ((ImageView)grassNodes[j][i]).setX(start + WIDTH - MAP_SIZE + (j) * 32*(26.0/30.0) * (MAP_SIZE * zoom/(34*32)));
                ((ImageView)grassNodes[j][i]).setY(start + (i) * 32*(26.0/30.0) * (MAP_SIZE * zoom/(34*32)));
                ((ImageView)grassNodes[j][i]).setTranslateY(0);
                ((ImageView)grassNodes[j][i]).setTranslateX(0);
                if(mapNodes[j][i] != null) {
                    ((ImageView)mapNodes[j][i]).setFitHeight(32*(26.0/30.0)*(MAP_SIZE * zoom/(34*32)));
                    ((ImageView)mapNodes[j][i]).setFitWidth(32*(26.0/30.0)*(MAP_SIZE * zoom/(34*32)));
                    ((ImageView)mapNodes[j][i]).setX(start + WIDTH - MAP_SIZE + (j) * 32*(26.0/30.0) * (MAP_SIZE * zoom/(34*32)));
                    ((ImageView)mapNodes[j][i]).setY(start + (i) * 32*(26.0/30.0) * (MAP_SIZE * zoom/(34*32)));
                    ((ImageView)mapNodes[j][i]).setTranslateY(0);
                    ((ImageView)mapNodes[j][i]).setTranslateX(0);
                }
                if(newBuildings[j][i] != null) {
                    ((ImageView)newBuildings[j][i]).setFitHeight(32*(26.0/30.0)*(MAP_SIZE * zoom/(34*32)));
                    ((ImageView)newBuildings[j][i]).setFitWidth(32*(26.0/30.0)*(MAP_SIZE * zoom/(34*32)));
                    ((ImageView)newBuildings[j][i]).setX(start + WIDTH - MAP_SIZE + (j) * 32*(26.0/30.0) * (MAP_SIZE * zoom/(34*32)));
                    ((ImageView)newBuildings[j][i]).setY(start + (i) * 32*(26.0/30.0) * (MAP_SIZE * zoom/(34*32)));
                    ((ImageView)newBuildings[j][i]).setTranslateY(0);
                    ((ImageView)newBuildings[j][i]).setTranslateX(0);
                }
            }
        }
    }

    public void updateInformationLabel()
    {
        try {
            controller.getVillageController().handleShowResources();
            controller.handleWhereAmI();
            DecimalFormat df = new DecimalFormat("#.00");
            String speed = df.format(controller.getVillageController().getSpeed());
            getInformationLabel().setText(getInformationLabel().getText() + "\n" + "Speed: " + speed);
            Label informationLabel = getInformationLabel();
        } catch (Exception e) {
        }

    }

    public void println(String line) {
        System.err.println(line);
    }

    public ArrayList<String> fileReader(String path) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(new FileReader(path));
        ArrayList<String> out = new ArrayList<>();
        while (fileScanner.hasNext()) {
            out.add(fileScanner.nextLine());
        }
        return out;
    }

    public VillageView getVillageView() {
        return villageView;
    }

    public AttackView getAttackView() {
        return attackView;
    }

    public String getMenuInput() throws InvalidCommandException {
        String commandLine = View.scanner.nextLine();
        if (menuController.getCurrentSection() == Section.Village) {
            for (String command : menuController.getCurrentSection().getValidCommands()) {
                if (commandLine.matches(command)) {
                    return commandLine;
                }
            }
            if (commandLine.matches(Section.Attack.getValue())) {
                return commandLine;
            }
            throw new InvalidCommandException();
        } else {
            try {
                if (Integer.parseInt(commandLine) <= menuController.getCurrentSection().getValidCommands().size() + menuController.getCurrentSection().getSubdivisions().size() + menuController.getCurrentSection().getDynamicValidCommands().size() - 5) {
                    return commandLine;
                }
            } catch (NumberFormatException e) {
                for (int i = 0; i < 5; i++) {
                    if (commandLine.matches(menuController.getCurrentSection().getValidCommands().get(i))) {
                        return commandLine;
                    }
                }
            }
            throw new InvalidCommandException();
        }
    }

    //TODO implement these


    public void yesNoQuestion(String question, String type, Integer number, Builder builder)throws FileNotFoundException {

        Stage stage = Window.getNewStage();
        Scene scene = stage.getScene();
        Group root = (Group)scene.getRoot();

        Label label = new Label(question);
        label.setContentDisplay(ContentDisplay.CENTER);
        try {
            label.setFont(Font.loadFont(new FileInputStream("game/font/buttonFont.ttf") , 25));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        label.setTextFill(Color.PURPLE);
        label.setTranslateX(20);
        label.setTranslateY(60);
        root.getChildren().add(label);

        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");
        configureGameButton(yesButton);
        yesButton.setMinWidth(155);
        configureGameButton(noButton);
        noButton.setMinWidth(155);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(yesButton , noButton);
        hBox.setTranslateX(130);
        hBox.setTranslateY(140);
        hBox.setSpacing(30);

        root.getChildren().add(hBox);
        stage.show();

        noButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.close();
            }
        });

        if (type.equals("upgrade")) {
            yesButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.close();
                    controller.getVillageController().handleUpgradeBuilding();
                }
            });
        }

        if (type.equals("build"))
        {
            View view = this;
            yesButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.close();
                    buildMode = true;
                    view.builder = builder;
                    view.num = number;
                }
            });
        }



    }

    public void showMap(String line) {
        System.out.println(line);
    }

    public void startBuilding() {
        Map realMap = controller.getVillageController().getMap();
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);
        double start = 4 * 32 * (MAP_SIZE * zoom / (34 * 32));
        ArrayList<Building> buildings = controller.getVillageController().getNewBuildings();
        for(Building building: buildings) {
            int x = building.getLocation().getX();
            int y = building.getLocation().getY();
            if(newBuildings[x][y] == null) {
                ImageView building1 = null;
                try {
                    building1 = new ImageView(new Image(new FileInputStream("assets/building and tower/Barracks_Ruin.png")));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                building1.setFitHeight(32 * (26.0 / 30.0) * (MAP_SIZE * zoom / (34 * 32)));
                building1.setFitWidth(32 * (26.0 / 30.0) * (MAP_SIZE * zoom / (34 * 32)));
                building1.setX(start + WIDTH - MAP_SIZE + (x) * 32 * (26.0 / 30.0) * (MAP_SIZE * zoom / (34 * 32)));
                building1.setY(start + (y) * 32 * (26.0 / 30.0) * (MAP_SIZE * zoom / (34 * 32)));
                newBuildings[x][y] = building1;

                building1.setOnZoom(new EventHandler<ZoomEvent>() {
                    @Override
                    public void handle(ZoomEvent event) {
                        if(zoom * event.getZoomFactor() > 1 &&  zoom * event.getZoomFactor() < 3) {
                            zoom *= event.getZoomFactor();
                            map.setFitWidth(MAP_SIZE * zoom);
                            map.setFitHeight(MAP_SIZE * zoom);
                            map.setX(WIDTH - MAP_SIZE);
                            map.setY(0);
                            map.setTranslateY(0);
                            map.setTranslateX(0);
                            zoomMap();
                        }
                    }
                });

                building1.setOnScroll(new EventHandler<ScrollEvent>() {
                    @Override
                    public void handle(ScrollEvent event) {
                        if(map.getX() + map.getTranslateX() + event.getDeltaX() <= WIDTH - MAP_SIZE && map.getY() + map.getTranslateY() + event.getDeltaY() <= 0 && map.getY() + map.getTranslateY() + event.getDeltaY() + MAP_SIZE * zoom >= HEIGHT && map.getX() + map.getTranslateX() + event.getDeltaX() + MAP_SIZE * zoom >= WIDTH) {
                            map.setTranslateX(map.getTranslateX() + event.getDeltaX());
                            map.setTranslateY(map.getTranslateY() + event.getDeltaY());
                            for (int i = 0; i < Map.HEIGHT; i++) {
                                for (int j = 0; j < Map.WIDTH; j++) {
                                    grassNodes[j][i].setTranslateX(grassNodes[j][i].getTranslateX() + event.getDeltaX());
                                    grassNodes[j][i].setTranslateY(grassNodes[j][i].getTranslateY() + event.getDeltaY());
                                    if(mapNodes[j][i] != null) {
                                        mapNodes[j][i].setTranslateX(mapNodes[j][i].getTranslateX() + event.getDeltaX());
                                        mapNodes[j][i].setTranslateY(mapNodes[j][i].getTranslateY() + event.getDeltaY());
                                    }
                                    if(newBuildings[j][i] != null) {
                                        newBuildings[j][i].setTranslateX(newBuildings[j][i].getTranslateX() + event.getDeltaX());
                                        newBuildings[j][i].setTranslateY(newBuildings[j][i].getTranslateY() + event.getDeltaY());
                                    }
                                }
                            }
                        }
                    }
                });


                building1.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {

                    grassNodes[x][y].setEffect(colorAdjust);

                });
                building1.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
                    grassNodes[x][y].setEffect(null);
                });
                grassNodes[x][y].setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(!buildMode) {
                            System.out.println(122112);
                            controller.getVillageController().handleShowBuildings(BuildingType.valueOf(building.getClass().getSimpleName()).getValue() + " " + building.getNumber());
                        }
                        else {
                            try {
                                Location location = new Location(x, y);
                                if(x == 0 || y == 0 || x == Map.WIDTH - 1 || y == Map.HEIGHT - 1 || realMap.getCell(location).getBuilding() != null) {
                                    throw new InvalidCellException();
                                }
                                controller.getVillageController().handleConstruction(builder, num, location);
                            }
                            catch (InvalidCellException e) {
                                e.printAnnouncement();
                            }
                        }
                    }
                });
                building1.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(!buildMode) {
                            controller.getVillageController().handleShowBuildings(BuildingType.valueOf(building.getClass().getSimpleName()).getValue() + " " + building.getNumber());
                        }
                        else {
                            try {
                                Location location = new Location(x, y);
                                if(x == 0 || y == 0 || x == Map.WIDTH - 1 || y == Map.HEIGHT - 1 || realMap.getCell(location).getBuilding() != null) {
                                    throw new InvalidCellException();
                                }
                                controller.getVillageController().handleConstruction(builder, num, location);
                            }
                            catch (InvalidCellException e) {
                                e.printAnnouncement();
                            }
                        }
                    }
                });

                root.getChildren().add(building1);

            }
        }
    }

    public void updateMap() {
        Map realMap = controller.getVillageController().getMap();
        double start = 4 * 32 * (MAP_SIZE * zoom / (34 * 32));

        for (int i = 0; i < Map.HEIGHT; i++) {
            for (int j = 0; j < Map.WIDTH; j++) {
                try {
                    if (realMap.getCell(new Location(j, i)).getBuilding() != null) {
                        ColorAdjust colorAdjust = new ColorAdjust();
                        colorAdjust.setBrightness(-0.5);
                        Building building1 = realMap.getCell(new Location(j, i)).getBuilding();
                        String name = building1.getClass().getSimpleName();
                        File directory = new File("assets/building and tower");
                        String[] files = directory.list();
                        for (int k = 0; k < files.length; k++) {
                            if (files[k].matches(name + "\\dB?.*\\.png")) {
                                if (Integer.parseInt(files[k].substring(name.length(), name.length() + 1)) - 1 <= building1.getLevel()) {
                                    if(k != files.length - 1 && Integer.parseInt(files[k + 1].substring(name.length(), name.length() + 1)) - 1 <= building1.getLevel()) {
                                        continue;
                                    }
                                    if(this.files[j][i] != null && !this.files[j][i].equals(files[k])) {
                                        if(name.matches("TownHall")) {
                                            if(files[k].length() >= name.length() + 3) {
                                                switch (files[k].substring(name.length() + 1, name.length() + 3)) {
                                                    case "se":
                                                        if(j != Map.WIDTH / 2 || i != Map.HEIGHT / 2) {
                                                            continue;
                                                        }
                                                        break;
                                                    case "sw":
                                                        if(j != Map.WIDTH / 2 - 1 || i != Map.HEIGHT / 2) {
                                                            continue;
                                                        }
                                                        break;
                                                    case "nw":
                                                        if(j != Map.WIDTH / 2 - 1 || i != Map.HEIGHT / 2 - 1) {
                                                            continue;
                                                        }
                                                        break;
                                                    case "ne":
                                                        if(j != Map.WIDTH / 2 || i != Map.HEIGHT / 2 - 1) {
                                                            continue;
                                                        }
                                                        break;
                                                }
                                            }
                                        }
                                        File file = directory.listFiles()[k];
                                        root.getChildren().remove(mapNodes[j][i]);
                                        this.files[j][i] = files[k];
                                        ImageView building = new ImageView(new Image(new FileInputStream(file)));
                                        building.setFitHeight(32 * (26.0 / 30.0) * (MAP_SIZE * zoom / (34 * 32)));
                                        building.setFitWidth(32 * (26.0 / 30.0) * (MAP_SIZE * zoom / (34 * 32)));
                                        building.setX(start + WIDTH - MAP_SIZE + (j) * 32 * (26.0 / 30.0) * (MAP_SIZE * zoom / (34 * 32)));
                                        building.setY(start + (i) * 32 * (26.0 / 30.0) * (MAP_SIZE * zoom / (34 * 32)));
                                        mapNodes[j][i] = building;

                                        building.setOnZoom(new EventHandler<ZoomEvent>() {
                                            @Override
                                            public void handle(ZoomEvent event) {
                                                if(zoom * event.getZoomFactor() > 1 &&  zoom * event.getZoomFactor() < 3) {
                                                    zoom *= event.getZoomFactor();
                                                    map.setFitWidth(MAP_SIZE * zoom);
                                                    map.setFitHeight(MAP_SIZE * zoom);
                                                    map.setX(WIDTH - MAP_SIZE);
                                                    map.setY(0);
                                                    map.setTranslateY(0);
                                                    map.setTranslateX(0);
                                                    zoomMap();
                                                }
                                            }
                                        });

                                        building.setOnScroll(new EventHandler<ScrollEvent>() {
                                            @Override
                                            public void handle(ScrollEvent event) {
                                                if(map.getX() + map.getTranslateX() + event.getDeltaX() <= WIDTH - MAP_SIZE && map.getY() + map.getTranslateY() + event.getDeltaY() <= 0 && map.getY() + map.getTranslateY() + event.getDeltaY() + MAP_SIZE * zoom >= HEIGHT && map.getX() + map.getTranslateX() + event.getDeltaX() + MAP_SIZE * zoom >= WIDTH) {
                                                    map.setTranslateX(map.getTranslateX() + event.getDeltaX());
                                                    map.setTranslateY(map.getTranslateY() + event.getDeltaY());
                                                    for (int i = 0; i < Map.HEIGHT; i++) {
                                                        for (int j = 0; j < Map.WIDTH; j++) {
                                                            grassNodes[j][i].setTranslateX(grassNodes[j][i].getTranslateX() + event.getDeltaX());
                                                            grassNodes[j][i].setTranslateY(grassNodes[j][i].getTranslateY() + event.getDeltaY());
                                                            if(mapNodes[j][i] != null) {
                                                                mapNodes[j][i].setTranslateX(mapNodes[j][i].getTranslateX() + event.getDeltaX());
                                                                mapNodes[j][i].setTranslateY(mapNodes[j][i].getTranslateY() + event.getDeltaY());
                                                            }
                                                            if(newBuildings[j][i] != null) {
                                                                newBuildings[j][i].setTranslateX(newBuildings[j][i].getTranslateX() + event.getDeltaX());
                                                                newBuildings[j][i].setTranslateY(newBuildings[j][i].getTranslateY() + event.getDeltaY());
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        });

                                        int finalJ = j;
                                        int finalI = i;
                                        building.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {

                                            grassNodes[finalJ][finalI].setEffect(colorAdjust);

                                        });
                                        building.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
                                            grassNodes[finalJ][finalI].setEffect(null);
                                        });
                                        grassNodes[finalJ][finalI].setOnMousePressed(new EventHandler<MouseEvent>() {
                                            @Override
                                            public void handle(MouseEvent event) {
                                                if(!buildMode) {
                                                    controller.getVillageController().handleShowBuildings(BuildingType.valueOf(building1.getClass().getSimpleName()).getValue() + " " + building1.getNumber());
                                                }
                                                else {
                                                    try {
                                                        Location location = new Location(finalJ, finalI);
                                                        if(finalJ == 0 || finalI == 0 || finalJ == Map.WIDTH - 1 || finalI == Map.HEIGHT - 1 || realMap.getCell(location).getBuilding() != null) {
                                                            throw new InvalidCellException();
                                                        }
                                                        controller.getVillageController().handleConstruction(builder, num, location);
                                                    }
                                                    catch (InvalidCellException e) {
                                                        e.printAnnouncement();
                                                    }
                                                }
                                            }
                                        });

                                        building.setOnMousePressed(new EventHandler<MouseEvent>() {
                                            @Override
                                            public void handle(MouseEvent event) {
                                                if(!buildMode) {
                                                    controller.getVillageController().handleShowBuildings(BuildingType.valueOf(building1.getClass().getSimpleName()).getValue() + " " + building1.getNumber());
                                                }
                                                else {
                                                    try {
                                                        Location location = new Location(finalJ, finalI);
                                                        if(finalJ == 0 || finalI == 0 || finalJ == Map.WIDTH - 1 || finalI == Map.HEIGHT - 1 || realMap.getCell(location).getBuilding() != null) {
                                                            throw new InvalidCellException();
                                                        }
                                                        controller.getVillageController().handleConstruction(builder, num, location);
                                                    }
                                                    catch (InvalidCellException e) {
                                                        e.printAnnouncement();
                                                    }
                                                }
                                            }
                                        });

                                        root.getChildren().add(building);
                                        break;
                                    }
                                    else if(this.files[j][i] == null) {
                                        boolean exist = false;
                                        String[] buil = controller.getVillageController().getBuildings().split("\n");
                                        for(String b: buil) {
                                            if(b.equals(BuildingType.valueOf(building1.getClass().getSimpleName()).getValue() + " " + building1.getNumber())) {
                                                exist = true;
                                                break;
                                            }
                                        }
                                        if(!exist) {
                                            break;
                                        }
                                        root.getChildren().remove(newBuildings[j][i]);
                                        newBuildings[j][i] = null;
                                        File file = directory.listFiles()[k];
                                        this.files[j][i] = files[k];
                                        ImageView building = new ImageView(new Image(new FileInputStream(file)));
                                        building.setFitHeight(32 * (26.0 / 30.0) * (MAP_SIZE * zoom / (34 * 32)));
                                        building.setFitWidth(32 * (26.0 / 30.0) * (MAP_SIZE * zoom / (34 * 32)));
                                        building.setX(start + WIDTH - MAP_SIZE + (j) * 32 * (26.0 / 30.0) * (MAP_SIZE * zoom / (34 * 32)));
                                        building.setY(start + (i) * 32 * (26.0 / 30.0) * (MAP_SIZE * zoom / (34 * 32)));
                                        mapNodes[j][i] = building;

                                        building.setOnZoom(new EventHandler<ZoomEvent>() {
                                            @Override
                                            public void handle(ZoomEvent event) {
                                                if(zoom * event.getZoomFactor() > 1 &&  zoom * event.getZoomFactor() < 3) {
                                                    zoom *= event.getZoomFactor();
                                                    map.setFitWidth(MAP_SIZE * zoom);
                                                    map.setFitHeight(MAP_SIZE * zoom);
                                                    map.setX(WIDTH - MAP_SIZE);
                                                    map.setY(0);
                                                    map.setTranslateY(0);
                                                    map.setTranslateX(0);
                                                    zoomMap();
                                                }
                                            }
                                        });

                                        building.setOnScroll(new EventHandler<ScrollEvent>() {
                                            @Override
                                            public void handle(ScrollEvent event) {
                                                if(map.getX() + map.getTranslateX() + event.getDeltaX() <= WIDTH - MAP_SIZE && map.getY() + map.getTranslateY() + event.getDeltaY() <= 0 && map.getY() + map.getTranslateY() + event.getDeltaY() + MAP_SIZE * zoom >= HEIGHT && map.getX() + map.getTranslateX() + event.getDeltaX() + MAP_SIZE * zoom >= WIDTH) {
                                                    map.setTranslateX(map.getTranslateX() + event.getDeltaX());
                                                    map.setTranslateY(map.getTranslateY() + event.getDeltaY());
                                                    for (int i = 0; i < Map.HEIGHT; i++) {
                                                        for (int j = 0; j < Map.WIDTH; j++) {
                                                            grassNodes[j][i].setTranslateX(grassNodes[j][i].getTranslateX() + event.getDeltaX());
                                                            grassNodes[j][i].setTranslateY(grassNodes[j][i].getTranslateY() + event.getDeltaY());
                                                            if(mapNodes[j][i] != null) {
                                                                mapNodes[j][i].setTranslateX(mapNodes[j][i].getTranslateX() + event.getDeltaX());
                                                                mapNodes[j][i].setTranslateY(mapNodes[j][i].getTranslateY() + event.getDeltaY());
                                                            }
                                                            if(newBuildings[j][i] != null) {
                                                                newBuildings[j][i].setTranslateX(newBuildings[j][i].getTranslateX() + event.getDeltaX());
                                                                newBuildings[j][i].setTranslateY(newBuildings[j][i].getTranslateY() + event.getDeltaY());
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        });

                                        int finalJ = j;
                                        int finalI = i;
                                        building.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {

                                            grassNodes[finalJ][finalI].setEffect(colorAdjust);

                                        });
                                        building.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
                                            grassNodes[finalJ][finalI].setEffect(null);
                                        });
                                        grassNodes[finalJ][finalI].setOnMousePressed(new EventHandler<MouseEvent>() {
                                            @Override
                                            public void handle(MouseEvent event) {
                                                if(!buildMode) {
                                                    controller.getVillageController().handleShowBuildings(BuildingType.valueOf(building1.getClass().getSimpleName()).getValue() + " " + building1.getNumber());
                                                }
                                                else {
                                                    try {
                                                        Location location = new Location(finalJ, finalI);
                                                        if(finalJ == 0 || finalI == 0 || finalJ == Map.WIDTH - 1 || finalI == Map.HEIGHT - 1 || realMap.getCell(location).getBuilding() != null) {
                                                            throw new InvalidCellException();
                                                        }
                                                        controller.getVillageController().handleConstruction(builder, num, location);
                                                    }
                                                    catch (InvalidCellException e) {
                                                        e.printAnnouncement();
                                                    }
                                                }
                                            }
                                        });

                                        building.setOnMousePressed(new EventHandler<MouseEvent>() {
                                            @Override
                                            public void handle(MouseEvent event) {
                                                if(!buildMode) {
                                                    controller.getVillageController().handleShowBuildings(BuildingType.valueOf(building1.getClass().getSimpleName()).getValue() + " " + building1.getNumber());
                                                }
                                                else {
                                                    try {
                                                        Location location = new Location(finalJ, finalI);
                                                        if(finalJ == 0 || finalI == 0 || finalJ == Map.WIDTH - 1 || finalI == Map.HEIGHT - 1 || realMap.getCell(location).getBuilding() != null) {
                                                            throw new InvalidCellException();
                                                        }
                                                        controller.getVillageController().handleConstruction(builder, num, location);
                                                    }
                                                    catch (InvalidCellException e) {
                                                        e.printAnnouncement();
                                                    }
                                                }
                                            }
                                        });

                                        root.getChildren().add(building);
                                        break;
                                    }

                                }
                            }
                        }
                    }
                } catch (InvalidCellException e) {
                    e.printAnnouncement();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void map(Group root) {
        Map realMap = controller.getVillageController().getMap();
        double start = 4 * 32 * (MAP_SIZE * zoom / (34 * 32));


        for (int i = 0; i < Map.HEIGHT; i++) {
            for (int j = 0; j < Map.WIDTH; j++) {
                try {
                    ImageView grass;
                    if (j == 0) {
                        grass = new ImageView(new Image(new FileInputStream("assets/map/lgrass.png")));
                    } else if (j == Map.WIDTH - 1) {
                        grass = new ImageView(new Image(new FileInputStream("assets/map/rgrass.png")));
                    } else {
                        grass = new ImageView(new Image(new FileInputStream("assets/map/grass.png")));
                    }
                    grass.setFitHeight(32 * (26.0 / 30.0) * (MAP_SIZE * zoom / (34 * 32)));
                    grass.setFitWidth(32 * (26.0 / 30.0) * (MAP_SIZE * zoom / (34 * 32)));
                    grass.setX(start + WIDTH - MAP_SIZE + (j) * 32 * (26.0 / 30.0) * (MAP_SIZE * zoom / (34 * 32)));
                    grass.setY(start + (i) * 32 * (26.0 / 30.0) * (MAP_SIZE * zoom / (34 * 32)));
                    grassNodes[j][i] = grass;

                    grass.setOnZoom(new EventHandler<ZoomEvent>() {
                        @Override
                        public void handle(ZoomEvent event) {
                            if(zoom * event.getZoomFactor() > 1 &&  zoom * event.getZoomFactor() < 3) {
                                zoom *= event.getZoomFactor();
                                map.setFitWidth(MAP_SIZE * zoom);
                                map.setFitHeight(MAP_SIZE * zoom);
                                map.setX(WIDTH - MAP_SIZE);
                                map.setY(0);
                                map.setTranslateY(0);
                                map.setTranslateX(0);
                                zoomMap();
                            }
                        }
                    });

                    grass.setOnScroll(new EventHandler<ScrollEvent>() {
                        @Override
                        public void handle(ScrollEvent event) {
                            if(map.getX() + map.getTranslateX() + event.getDeltaX() <= WIDTH - MAP_SIZE && map.getY() + map.getTranslateY() + event.getDeltaY() <= 0 && map.getY() + map.getTranslateY() + event.getDeltaY() + MAP_SIZE * zoom >= HEIGHT && map.getX() + map.getTranslateX() + event.getDeltaX() + MAP_SIZE * zoom >= WIDTH) {
                                map.setTranslateX(map.getTranslateX() + event.getDeltaX());
                                map.setTranslateY(map.getTranslateY() + event.getDeltaY());
                                for (int i = 0; i < Map.HEIGHT; i++) {
                                    for (int j = 0; j < Map.WIDTH; j++) {
                                        grassNodes[j][i].setTranslateX(grassNodes[j][i].getTranslateX() + event.getDeltaX());
                                        grassNodes[j][i].setTranslateY(grassNodes[j][i].getTranslateY() + event.getDeltaY());
                                        if(mapNodes[j][i] != null) {
                                            mapNodes[j][i].setTranslateX(mapNodes[j][i].getTranslateX() + event.getDeltaX());
                                            mapNodes[j][i].setTranslateY(mapNodes[j][i].getTranslateY() + event.getDeltaY());
                                        }
                                        if(newBuildings[j][i] != null) {
                                            newBuildings[j][i].setTranslateX(newBuildings[j][i].getTranslateX() + event.getDeltaX());
                                            newBuildings[j][i].setTranslateY(newBuildings[j][i].getTranslateY() + event.getDeltaY());
                                        }
                                    }
                                }
                            }
                        }
                    });

                    ColorAdjust colorAdjust = new ColorAdjust();
                    colorAdjust.setBrightness(-0.5);

                    grass.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {

                        grass.setEffect(colorAdjust);

                    });
                    grass.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
                        grass.setEffect(null);
                    });

                    int finalJ = j;
                    int finalI = i;
                    grass.setOnMousePressed(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if(buildMode) {
                                try {
                                    Location location = new Location(finalJ, finalI);
                                    if(finalJ == 0 || finalI == 0 || finalJ == Map.WIDTH - 1 || finalI == Map.HEIGHT - 1 || realMap.getCell(location).getBuilding() != null) {
                                        throw new InvalidCellException();
                                    }
                                    controller.getVillageController().handleConstruction(builder, num, location);
                                }
                                catch (InvalidCellException e) {
                                    e.printAnnouncement();
                                }
                            }
                        }
                    });

                    root.getChildren().add(grass);
                    if (realMap.getCell(new Location(j, i)).getBuilding() != null) {
                        Building building1 = realMap.getCell(new Location(j, i)).getBuilding();
                        String name = building1.getClass().getSimpleName();
                        File directory = new File("assets/building and tower");
                        String[] files = directory.list();
                        for (int k = 0; k < files.length; k++) {
                            if (files[k].matches(name + "\\dB?.*\\.png")) {
                                if (Integer.parseInt(files[k].substring(name.length(), name.length() + 1)) - 1 <= building1.getLevel()) {
                                    if(name.matches("TownHall")) {
                                        if(files[k].length() >= name.length() + 3) {
                                            switch (files[k].substring(name.length() + 1, name.length() + 3)) {
                                                case "se":
                                                    if(j != Map.WIDTH / 2 || i != Map.HEIGHT / 2) {
                                                        continue;
                                                    }
                                                    break;
                                                case "sw":
                                                    if(j != Map.WIDTH / 2 - 1 || i != Map.HEIGHT / 2) {
                                                        continue;
                                                    }
                                                    break;
                                                case "nw":
                                                    if(j != Map.WIDTH / 2 - 1 || i != Map.HEIGHT / 2 - 1) {
                                                        continue;
                                                    }
                                                    break;
                                                case "ne":
                                                    if(j != Map.WIDTH / 2 || i != Map.HEIGHT / 2 - 1) {
                                                        continue;
                                                    }
                                                    break;
                                            }
                                        }
                                    }
                                    File file = directory.listFiles()[k];
                                    this.files[j][i] = files[k];
                                    ImageView building = new ImageView(new Image(new FileInputStream(file)));
                                    building.setFitHeight(32 * (26.0 / 30.0) * (MAP_SIZE * zoom / (34 * 32)));
                                    building.setFitWidth(32 * (26.0 / 30.0) * (MAP_SIZE * zoom / (34 * 32)));
                                    building.setX(start + WIDTH - MAP_SIZE + (j) * 32 * (26.0 / 30.0) * (MAP_SIZE * zoom / (34 * 32)));
                                    building.setY(start + (i) * 32 * (26.0 / 30.0) * (MAP_SIZE * zoom / (34 * 32)));
                                    mapNodes[j][i] = building;

                                    building.setOnZoom(new EventHandler<ZoomEvent>() {
                                        @Override
                                        public void handle(ZoomEvent event) {
                                            if(zoom * event.getZoomFactor() > 1 &&  zoom * event.getZoomFactor() < 3) {
                                                zoom *= event.getZoomFactor();
                                                map.setFitWidth(MAP_SIZE * zoom);
                                                map.setFitHeight(MAP_SIZE * zoom);
                                                map.setX(WIDTH - MAP_SIZE);
                                                map.setY(0);
                                                map.setTranslateY(0);
                                                map.setTranslateX(0);
                                                zoomMap();
                                            }
                                        }
                                    });

                                    building.setOnScroll(new EventHandler<ScrollEvent>() {
                                        @Override
                                        public void handle(ScrollEvent event) {
                                            if(map.getX() + map.getTranslateX() + event.getDeltaX() <= WIDTH - MAP_SIZE && map.getY() + map.getTranslateY() + event.getDeltaY() <= 0 && map.getY() + map.getTranslateY() + event.getDeltaY() + MAP_SIZE * zoom >= HEIGHT && map.getX() + map.getTranslateX() + event.getDeltaX() + MAP_SIZE * zoom >= WIDTH) {
                                                map.setTranslateX(map.getTranslateX() + event.getDeltaX());
                                                map.setTranslateY(map.getTranslateY() + event.getDeltaY());
                                                for (int i = 0; i < Map.HEIGHT; i++) {
                                                    for (int j = 0; j < Map.WIDTH; j++) {
                                                        grassNodes[j][i].setTranslateX(grassNodes[j][i].getTranslateX() + event.getDeltaX());
                                                        grassNodes[j][i].setTranslateY(grassNodes[j][i].getTranslateY() + event.getDeltaY());
                                                        if(mapNodes[j][i] != null) {
                                                            mapNodes[j][i].setTranslateX(mapNodes[j][i].getTranslateX() + event.getDeltaX());
                                                            mapNodes[j][i].setTranslateY(mapNodes[j][i].getTranslateY() + event.getDeltaY());
                                                        }
                                                        if(newBuildings[j][i] != null) {
                                                            newBuildings[j][i].setTranslateX(newBuildings[j][i].getTranslateX() + event.getDeltaX());
                                                            newBuildings[j][i].setTranslateY(newBuildings[j][i].getTranslateY() + event.getDeltaY());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    });

                                    building.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {

                                        grass.setEffect(colorAdjust);

                                    });
                                    building.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
                                        grass.setEffect(null);
                                    });
                                    grass.setOnMousePressed(new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent event) {
                                            if(!buildMode) {
                                                controller.getVillageController().handleShowBuildings(BuildingType.valueOf(building1.getClass().getSimpleName()).getValue() + " " + building1.getNumber());
                                            }
                                            else {
                                                try {
                                                    Location location = new Location(finalJ, finalI);
                                                    if(finalJ == 0 || finalI == 0 || finalJ == Map.WIDTH - 1 || finalI == Map.HEIGHT - 1 || realMap.getCell(location).getBuilding() != null) {
                                                        throw new InvalidCellException();
                                                    }
                                                    controller.getVillageController().handleConstruction(builder, num, location);
                                                }
                                                catch (InvalidCellException e) {
                                                    e.printAnnouncement();
                                                }
                                            }
                                        }
                                    });

                                    building.setOnMousePressed(new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent event) {
                                            if(!buildMode) {
                                                controller.getVillageController().handleShowBuildings(BuildingType.valueOf(building1.getClass().getSimpleName()).getValue() + " " + building1.getNumber());
                                            }
                                            else {
                                                try {
                                                    Location location = new Location(finalJ, finalI);
                                                    if(finalJ == 0 || finalI == 0 || finalJ == Map.WIDTH - 1 || finalI == Map.HEIGHT - 1 || realMap.getCell(location).getBuilding() != null) {
                                                        throw new InvalidCellException();
                                                    }
                                                    controller.getVillageController().handleConstruction(builder, num, location);
                                                }
                                                catch (InvalidCellException e) {
                                                    e.printAnnouncement();
                                                }
                                            }
                                        }
                                    });

                                    root.getChildren().add(building);
                                    break;
                                }
                            }
                        }
                    }
                } catch (InvalidCellException e) {
                    e.printAnnouncement();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void showMenu(String line) {
        String[] output = line.split("\n");
        try {
            UIShowMenu(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UIShowInfo(String line , boolean isUpgrade ) throws FileNotFoundException {

        int i = 0;
        menuPane.getChildren().clear();
        String[] output = line.split("\n");

        for (i = 0; i < output.length; i++) {
            if(output[i].equals(""))
                break;
            Button button = new Button(output[i]);
            configureGameButton(button);
            menuPane.add(button, 0, i);
        }
        Button backButton = new Button("Back");
        configureGameButton(backButton);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE)
                    showMenu(menuController.showMenu());
            }
        });
        backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                showMenu(menuController.showMenu());
            }
        });

        if (isUpgrade)
        {
            Button upgradeButton = new Button("Upgrade");
            configureGameButton(upgradeButton);
            upgradeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    controller.getVillageController().handleUpgradeQuestion();
                }
            });
            menuPane.add(upgradeButton , 0 , i);
            menuPane.add(backButton , 0 , i + 1);
        }else {
            menuPane.add(backButton, 0,i);
        }
    }


    public void showSection(String line) {
        Label informationLabel = getInformationLabel();
        String firstLine = informationLabel.getText().split("\n")[1];
        String secondLine = informationLabel.getText().split("\n")[2];
        String thirdLine = informationLabel.getText().split("\n")[3];
        informationLabel.setText(MIDDLE + line + "\n" + firstLine + "\n" + secondLine + "\n" + thirdLine);
    }

    public void showAnnouncement(String line)  {
        try {
            UIShowInfo(line , false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void showResources(String line) {
        String goldInfo = line.split("\n")[0];
        String elixirInfo = line.split("\n")[1];
        String score = line.split("\n")[2];

        Label informationLabel = getInformationLabel();

        String whereAmI;
        try {
            whereAmI = informationLabel.getText().split("\n")[0];
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            whereAmI = "";
        }
        informationLabel.setText(MIDDLE + whereAmI + "\n" + goldInfo + "\n" + elixirInfo + "\n" + score);
    }

    private Label getInformationLabel() {
        return ((Label) ((VBox) informationPane.getChildren().get(0)).getChildren().get(0));
    }

    public void showUpgrade(String line) {
        try {
            UIShowInfo(line , true );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void showTownHallStatus(String line) {
        showInfo(line);
    }

    public void showInfo(String line )  {

        try {
            UIShowInfo(line , false );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void UIShowMenu(String[] output) throws Exception {
        menuPane.getChildren().clear();
        updateInformationLabel();



        for (int i = 0; i < output.length; i++) {
            Button button = new Button(output[i]);
            configureGameButton(button);

            menuPane.add(button, 0, i);
            if (getInformationLabel().getText().split("\n")[0].matches("\\s*village")) {
                int finalI = i;
                button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        controller.getVillageController().handleShowBuildings(output[finalI]);
                    }
                });
            } else {
                int finalI1 = i;
                button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        try {
                            Main.handleMenu(controller,menuController , finalI1 + 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

        }
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE) {
                    try {
                        Main.handleMenu(controller , menuController , output.length);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

        public  static void configureGameButton(Button button) throws FileNotFoundException {
        Image image = new Image(new FileInputStream("menu/pic/wood.JPG"));
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT
                , BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background buttonBackground = new Background(backgroundImage);

        button.setBackground(buttonBackground);
        button.setFont(Font.loadFont(new FileInputStream("game/font/buttonFont.ttf"), 20));
        button.setTextFill(Color.WHITE);
        button.setMinWidth(195);
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

    public static void configureGameButtonStone(Button button) throws FileNotFoundException {
        Image image = new Image(new FileInputStream("game/pic/bricks.jpg"));
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT
                , BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background buttonBackground = new Background(backgroundImage);

        button.setBackground(buttonBackground);
        button.setFont(Font.loadFont(new FileInputStream("game/font/buttonFont.ttf"), 25));
        button.setTextFill(Color.WHITE);
        button.setPrefWidth(95);
        button.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                button.setTextFill(Color.RED);
                Media mediaName = new Media(new File("menu/sound/pop.mp3").toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(mediaName);
                mediaPlayer.play();
                int t = 3;
            }
        });
        button.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                button.setTextFill(Color.WHITE);
            }
        });

    }


    public void showAttackStatus(ArrayList<String> output) throws FileNotFoundException {

    }

    public void showAttackInfo(ArrayList<String> output) {
        String outputLine = "";
        for (int i = 0 ; i < output.size() ; i++){
            outputLine = outputLine + output.get(i);
            if(i < output.size() - 1)
                outputLine = outputLine + "\n";
        }
        try {
            showInfo(outputLine);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showStatus(ArrayList<String> input) throws FileNotFoundException {
        String output = "";

        for (int i = 0 ; i < input.size() ; i++) {
            output = output + input.get(i);
            if( i < input.size() - 1){
                output = output + "\n";
            }
        }

        showInfo(output);
    }

    public void addBuilding() {

    }

    public static void playSoldierConstructed(){
        Media mediaName = new Media(new File("game/sound/soldierConstructed.m4a").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(mediaName);
        mediaPlayer.play();
    }

    public void getAttackMapPath()
    {
        Stage stage = Window.getNewStage();
        HBox hBox = new HBox();
        hBox.setTranslateX(130);
        hBox.setTranslateY(60);

        TextField path = new TextField();
        path.setPrefWidth(300);
        path.setPrefWidth(300);
        path.setPromptText("Enter attack map path");
        Button button = new Button("...");
        hBox.setSpacing(3);
        hBox.getChildren().addAll(path , button);
        ((Group)stage.getScene().getRoot()).getChildren().add(hBox);
        path.getParent().requestFocus();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Attack map path");
        fileChooser.setInitialDirectory(new File("./"));


        fileChooser.getExtensionFilters().add( new  FileChooser.ExtensionFilter("Json Files", "*.json"));
        Button okButton = new Button("Ok");

        try {
            configureGameButton(okButton);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        okButton.setMinWidth(140);

        okButton.setTranslateY(120);
        okButton.setTranslateX(230);
        ((Group)stage.getScene().getRoot()).getChildren().add(okButton);

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                File selectedFile = fileChooser.showOpenDialog(primaryStage);
                stage.setAlwaysOnTop(true);

                try {
                    path.setText(selectedFile.getPath());
                }catch (NullPointerException e)
                {
                    ;
                }

            }
        });

        okButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    stage.close();
                    controller.handleAttackMap(path.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        stage.show();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////





    public void updateAttackMap() {
        if(nextState) {
            nextState = false;
            if(step == 0) {
                try {
                    controller.getAttackController().nextTurn();
                } catch (InvalidCellException e) {
                    e.printStackTrace();
                }
            }
            step = (step + 1) % NUMBER_OF_STATES;
            System.out.println(troops);
            for(TroopsGUI troopsGUI: troops) {
                System.out.println(1);
                for (int i = 0; i < troopsGUI.getTroop().getMaxSpeed(); i++) {
                    if(troopsGUI.getTroop().canFly()) {
                        airTroopRoot.getChildren().remove(troopsGUI.getPreviousImage());
                    }
                    else {
                        groundTroopRoot.getChildren().remove(troopsGUI.getPreviousImage());
                    }
                    ImageView troop = troopsGUI.changeImage();
                    configure(troop, troopsGUI.getxList().get(0), troopsGUI.getyList().get(0), troopsGUI.getTroop());
                    if(troopsGUI.getTroop().canFly()) {
                        airTroopRoot.getChildren().add(troop);
                    }
                    else {
                        groundTroopRoot.getChildren().add(troop);
                    }
                }
            }
        }
    }


    private void zoomAttackMap() {
        double start = 4*32*(MAP_SIZE * attackZoom/(34*32));
        for (int i = 0; i < Map.HEIGHT; i++) {
            for (int j = 0; j < Map.WIDTH; j++) {
                ((ImageView)attackGrassNodes[j][i]).setFitHeight(32*(26.0/30.0)*(MAP_SIZE * attackZoom/(34*32)));
                ((ImageView)attackGrassNodes[j][i]).setFitWidth(32*(26.0/30.0)*(MAP_SIZE * attackZoom/(34*32)));
                ((ImageView)attackGrassNodes[j][i]).setX(start + WIDTH - MAP_SIZE + (j) * 32*(26.0/30.0) * (MAP_SIZE * attackZoom/(34*32)));
                ((ImageView)attackGrassNodes[j][i]).setY(start + (i) * 32*(26.0/30.0) * (MAP_SIZE * attackZoom/(34*32)));
                ((ImageView)attackGrassNodes[j][i]).setTranslateY(0);
                ((ImageView)attackGrassNodes[j][i]).setTranslateX(0);
                if(attackMapNodes[j][i] != null) {
                    ((ImageView)attackMapNodes[j][i]).setFitHeight(32*(26.0/30.0)*(MAP_SIZE * attackZoom/(34*32)));
                    ((ImageView)attackMapNodes[j][i]).setFitWidth(32*(26.0/30.0)*(MAP_SIZE * attackZoom/(34*32)));
                    ((ImageView)attackMapNodes[j][i]).setX(start + WIDTH - MAP_SIZE + (j) * 32*(26.0/30.0) * (MAP_SIZE * attackZoom/(34*32)));
                    ((ImageView)attackMapNodes[j][i]).setY(start + (i) * 32*(26.0/30.0) * (MAP_SIZE * attackZoom/(34*32)));
                    ((ImageView)attackMapNodes[j][i]).setTranslateY(0);
                    ((ImageView)attackMapNodes[j][i]).setTranslateX(0);
                }
            }
        }

        for(TroopsGUI troopsGUI: troops) {
            Node troop = troopsGUI.getPreviousImage();
            double j = troopsGUI.getxList().get(0);
            double i = troopsGUI.getyList().get(0);
            ((ImageView)troop).setFitHeight(32*(26.0/30.0)*(MAP_SIZE * attackZoom/(34*32)));
            ((ImageView)troop).setFitWidth(32*(26.0/30.0)*(MAP_SIZE * attackZoom/(34*32)));
            ((ImageView)troop).setX(start + WIDTH - MAP_SIZE + (j) * 32*(26.0/30.0) * (MAP_SIZE * attackZoom/(34*32)));
            ((ImageView)troop).setY(start + (i) * 32*(26.0/30.0) * (MAP_SIZE * attackZoom/(34*32)));
            ((ImageView)troop).setTranslateY(0);
            ((ImageView)troop).setTranslateX(0);
        }
    }

    public Group attackMap(ImageView map) {
        attackMap = map;
        Map realMap = controller.getAttackController().getDefenderMap();
        double start = 4 * 32 * (MAP_SIZE * attackZoom / (34 * 32));


        for (int i = 0; i < Map.HEIGHT; i++) {
            for (int j = 0; j < Map.WIDTH; j++) {
                try {
                    ImageView grass;
                    if (j == 0) {
                        grass = new ImageView(new Image(new FileInputStream("assets/map/lgrass.png")));
                    } else if (j == Map.WIDTH - 1) {
                        grass = new ImageView(new Image(new FileInputStream("assets/map/rgrass.png")));
                    } else {
                        grass = new ImageView(new Image(new FileInputStream("assets/map/grass.png")));
                    }
                    grass.setFitHeight(32 * (26.0 / 30.0) * (MAP_SIZE * attackZoom / (34 * 32)));
                    grass.setFitWidth(32 * (26.0 / 30.0) * (MAP_SIZE * attackZoom / (34 * 32)));
                    grass.setX(start + WIDTH - MAP_SIZE + (j) * 32 * (26.0 / 30.0) * (MAP_SIZE * attackZoom / (34 * 32)));
                    grass.setY(start + (i) * 32 * (26.0 / 30.0) * (MAP_SIZE * attackZoom / (34 * 32)));
                    attackGrassNodes[j][i] = grass;

                    grass.setOnZoom(new EventHandler<ZoomEvent>() {
                        @Override
                        public void handle(ZoomEvent event) {
                            if (attackZoom * event.getZoomFactor() > 1 && attackZoom * event.getZoomFactor() < 3) {
                                attackZoom *= event.getZoomFactor();
                                attackMap.setFitWidth(MAP_SIZE * attackZoom);
                                attackMap.setFitHeight(MAP_SIZE * attackZoom);
                                attackMap.setX(WIDTH - MAP_SIZE);
                                attackMap.setY(0);
                                attackMap.setTranslateY(0);
                                attackMap.setTranslateX(0);
                                zoomAttackMap();
                            }
                        }
                    });

                    grass.setOnScroll(new EventHandler<ScrollEvent>() {
                        @Override
                        public void handle(ScrollEvent event) {
                            if (attackMap.getX() + attackMap.getTranslateX() + event.getDeltaX() <= WIDTH - MAP_SIZE && attackMap.getY() + attackMap.getTranslateY() + event.getDeltaY() <= 0 && attackMap.getY() + attackMap.getTranslateY() + event.getDeltaY() + MAP_SIZE * attackZoom >= HEIGHT && attackMap.getX() + attackMap.getTranslateX() + event.getDeltaX() + MAP_SIZE * attackZoom >= WIDTH) {
                                attackMap.setTranslateX(attackMap.getTranslateX() + event.getDeltaX());
                                attackMap.setTranslateY(attackMap.getTranslateY() + event.getDeltaY());
                                for (int i = 0; i < Map.HEIGHT; i++) {
                                    for (int j = 0; j < Map.WIDTH; j++) {
                                        attackGrassNodes[j][i].setTranslateX(attackGrassNodes[j][i].getTranslateX() + event.getDeltaX());
                                        attackGrassNodes[j][i].setTranslateY(attackGrassNodes[j][i].getTranslateY() + event.getDeltaY());
                                        if (attackMapNodes[j][i] != null) {
                                            attackMapNodes[j][i].setTranslateX(attackMapNodes[j][i].getTranslateX() + event.getDeltaX());
                                            attackMapNodes[j][i].setTranslateY(attackMapNodes[j][i].getTranslateY() + event.getDeltaY());
                                        }
                                    }
                                }
                                for(Node troop: groundTroopRoot.getChildren()) {
                                    troop.setTranslateX(troop.getTranslateX() + event.getDeltaX());
                                    troop.setTranslateY(troop.getTranslateY() + event.getDeltaY());
                                }
                                for(Node troop: airTroopRoot.getChildren()) {
                                    troop.setTranslateX(troop.getTranslateX() + event.getDeltaX());
                                    troop.setTranslateY(troop.getTranslateY() + event.getDeltaY());
                                }
                            }
                        }
                    });

                    ColorAdjust colorAdjust = new ColorAdjust();
                    colorAdjust.setBrightness(-0.5);

                    grass.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
                        if(putMode) {
                            grass.setEffect(colorAdjust);
                        }

                    });
                    grass.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
                        if(putMode) {
                            grass.setEffect(null);
                        }
                    });

                    int finalJ1 = j;
                    int finalI1 = i;
                    grass.setOnMousePressed(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if(putMode && attackView.selectedTroop != null) {
                                try {
                                    controller.getAttackController().putTroop(finalJ1, finalI1);
                                    System.out.println(finalJ1 + " " + finalI1);
                                    addTroops(attackView.selectedTroop);
                                    System.out.println(attackView.selectedTroop.getLocation().getX() + " " + attackView.selectedTroop.getLocation().getY());
                                    getAttackView().selectedTroop = null;
                                } catch (InvalidAttackCellException | InvalidCellException e) {
                                    e.printAnnouncement();
                                }
                            }
                        }
                    });

                    int finalJ = j;
                    int finalI = i;

                    attackRoot.getChildren().add(grass);
                    if (realMap.getCell(new Location(j, i)).getBuilding() != null) {
                        Building building1 = realMap.getCell(new Location(j, i)).getBuilding();
                        String name = building1.getClass().getSimpleName();
                        File directory = new File("assets/building and tower");
                        String[] files = directory.list();
                        for (int k = 0; k < files.length; k++) {
                            if (files[k].matches(name + "\\dB?.*\\.png")) {
                                if (Integer.parseInt(files[k].substring(name.length(), name.length() + 1)) - 1 <= building1.getLevel()) {
                                    if (name.matches("TownHall")) {
                                        if (files[k].length() >= name.length() + 3) {
                                            switch (files[k].substring(name.length() + 1, name.length() + 3)) {
                                                case "se":
                                                    if (j != Map.WIDTH / 2 || i != Map.HEIGHT / 2) {
                                                        continue;
                                                    }
                                                    break;
                                                case "sw":
                                                    if (j != Map.WIDTH / 2 - 1 || i != Map.HEIGHT / 2) {
                                                        continue;
                                                    }
                                                    break;
                                                case "nw":
                                                    if (j != Map.WIDTH / 2 - 1 || i != Map.HEIGHT / 2 - 1) {
                                                        continue;
                                                    }
                                                    break;
                                                case "ne":
                                                    if (j != Map.WIDTH / 2 || i != Map.HEIGHT / 2 - 1) {
                                                        continue;
                                                    }
                                                    break;
                                            }
                                        }
                                    }
                                    File file = directory.listFiles()[k];
                                    this.attackFiles[j][i] = files[k];
                                    ImageView building = new ImageView(new Image(new FileInputStream(file)));
                                    building.setFitHeight(32 * (26.0 / 30.0) * (MAP_SIZE * attackZoom / (34 * 32)));
                                    building.setFitWidth(32 * (26.0 / 30.0) * (MAP_SIZE * attackZoom / (34 * 32)));
                                    building.setX(start + WIDTH - MAP_SIZE + (j) * 32 * (26.0 / 30.0) * (MAP_SIZE * attackZoom / (34 * 32)));
                                    building.setY(start + (i) * 32 * (26.0 / 30.0) * (MAP_SIZE * attackZoom / (34 * 32)));
                                    attackMapNodes[j][i] = building;

                                    building.setOnZoom(new EventHandler<ZoomEvent>() {
                                        @Override
                                        public void handle(ZoomEvent event) {
                                            if (attackZoom * event.getZoomFactor() > 1 && attackZoom * event.getZoomFactor() < 3) {
                                                attackZoom *= event.getZoomFactor();
                                                attackMap.setFitWidth(MAP_SIZE * attackZoom);
                                                attackMap.setFitHeight(MAP_SIZE * attackZoom);
                                                attackMap.setX(WIDTH - MAP_SIZE);
                                                attackMap.setY(0);
                                                attackMap.setTranslateY(0);
                                                attackMap.setTranslateX(0);
                                                zoomAttackMap();
                                            }
                                        }
                                    });

                                    building.setOnScroll(new EventHandler<ScrollEvent>() {
                                        @Override
                                        public void handle(ScrollEvent event) {
                                            if (attackMap.getX() + attackMap.getTranslateX() + event.getDeltaX() <= WIDTH - MAP_SIZE && attackMap.getY() + attackMap.getTranslateY() + event.getDeltaY() <= 0 && attackMap.getY() + attackMap.getTranslateY() + event.getDeltaY() + MAP_SIZE * attackZoom >= HEIGHT && attackMap.getX() + attackMap.getTranslateX() + event.getDeltaX() + MAP_SIZE * attackZoom >= WIDTH) {
                                                attackMap.setTranslateX(attackMap.getTranslateX() + event.getDeltaX());
                                                attackMap.setTranslateY(attackMap.getTranslateY() + event.getDeltaY());
                                                for (int i = 0; i < Map.HEIGHT; i++) {
                                                    for (int j = 0; j < Map.WIDTH; j++) {
                                                        attackGrassNodes[j][i].setTranslateX(attackGrassNodes[j][i].getTranslateX() + event.getDeltaX());
                                                        attackGrassNodes[j][i].setTranslateY(attackGrassNodes[j][i].getTranslateY() + event.getDeltaY());
                                                        if (attackMapNodes[j][i] != null) {
                                                            attackMapNodes[j][i].setTranslateX(attackMapNodes[j][i].getTranslateX() + event.getDeltaX());
                                                            attackMapNodes[j][i].setTranslateY(attackMapNodes[j][i].getTranslateY() + event.getDeltaY());
                                                        }
                                                    }
                                                }
                                                for(Node troop: groundTroopRoot.getChildren()) {
                                                    troop.setTranslateX(troop.getTranslateX() + event.getDeltaX());
                                                    troop.setTranslateY(troop.getTranslateY() + event.getDeltaY());
                                                }
                                                for(Node troop: airTroopRoot.getChildren()) {
                                                    troop.setTranslateX(troop.getTranslateX() + event.getDeltaX());
                                                    troop.setTranslateY(troop.getTranslateY() + event.getDeltaY());
                                                }
                                            }
                                        }
                                    });

                                    building.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
                                        if(putMode) {
                                            grass.setEffect(colorAdjust);
                                        }

                                    });
                                    building.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
                                        if(putMode) {
                                            grass.setEffect(null);
                                        }
                                    });

                                    building.setOnMousePressed(new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent event) {
                                            System.out.println(putMode);
                                            if(putMode && attackView.selectedTroop != null) {
                                                System.out.println("VSVDVDSCFVDVFDVFd");
                                                try {
                                                    controller.getAttackController().putTroop(finalJ1, finalI1);
                                                    addTroops(attackView.selectedTroop);
                                                    getAttackView().selectedTroop = null;
                                                } catch (InvalidAttackCellException | InvalidCellException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            else if(!putMode) {
                                                System.out.println("SDFsdfsdfsdfsdfsddf");
                                                attackView.showBuildingStatus(building1);
                                            }
                                        }
                                    });

                                    attackRoot.getChildren().add(building);
                                    break;
                                }
                            }
                        }
                    }




                }  catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (InvalidCellException e) {
                    e.printStackTrace();
                }
            }
        }

        attackRoot.getChildren().add(groundTroopRoot);

        attackRoot.getChildren().add(airTroopRoot);

        return attackRoot;
    }

    public void addTroops(Troop troop) {
        double start = 4 * 32 * (MAP_SIZE * attackZoom / (34 * 32));
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);

        TroopsGUI troopsGUI = new TroopsGUI(troop.getClass().getSimpleName(), troop, troop.getLocation().getX(), troop.getLocation().getY());
        troops.add(troopsGUI);
        ImageView troop1 = troopsGUI.getDefault();
        int j = troop.getLocation().getX();
        int i = troop.getLocation().getY();
        troop1.setFitHeight(32 * (26.0 / 30.0) * (MAP_SIZE * attackZoom / (34 * 32)));
        troop1.setFitWidth(32 * (26.0 / 30.0) * (MAP_SIZE * attackZoom / (34 * 32)));
        troop1.setX(start + WIDTH - MAP_SIZE + (j) * 32 * (26.0 / 30.0) * (MAP_SIZE * attackZoom / (34 * 32)));
        troop1.setY(start + (i) * 32 * (26.0 / 30.0) * (MAP_SIZE * attackZoom / (34 * 32)));

        troop1.setOnZoom(new EventHandler<ZoomEvent>() {
            @Override
            public void handle(ZoomEvent event) {
                if (attackZoom * event.getZoomFactor() > 1 && attackZoom * event.getZoomFactor() < 3) {
                    attackZoom *= event.getZoomFactor();
                    attackMap.setFitWidth(MAP_SIZE * attackZoom);
                    attackMap.setFitHeight(MAP_SIZE * attackZoom);
                    attackMap.setX(WIDTH - MAP_SIZE);
                    attackMap.setY(0);
                    attackMap.setTranslateY(0);
                    attackMap.setTranslateX(0);
                    zoomAttackMap();
                }
            }
        });

        troop1.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (attackMap.getX() + attackMap.getTranslateX() + event.getDeltaX() <= WIDTH - MAP_SIZE && attackMap.getY() + attackMap.getTranslateY() + event.getDeltaY() <= 0 && attackMap.getY() + attackMap.getTranslateY() + event.getDeltaY() + MAP_SIZE * attackZoom >= HEIGHT && attackMap.getX() + attackMap.getTranslateX() + event.getDeltaX() + MAP_SIZE * attackZoom >= WIDTH) {
                    attackMap.setTranslateX(attackMap.getTranslateX() + event.getDeltaX());
                    attackMap.setTranslateY(attackMap.getTranslateY() + event.getDeltaY());
                    for (int i = 0; i < Map.HEIGHT; i++) {
                        for (int j = 0; j < Map.WIDTH; j++) {
                            attackGrassNodes[j][i].setTranslateX(attackGrassNodes[j][i].getTranslateX() + event.getDeltaX());
                            attackGrassNodes[j][i].setTranslateY(attackGrassNodes[j][i].getTranslateY() + event.getDeltaY());
                            if (attackMapNodes[j][i] != null) {
                                attackMapNodes[j][i].setTranslateX(attackMapNodes[j][i].getTranslateX() + event.getDeltaX());
                                attackMapNodes[j][i].setTranslateY(attackMapNodes[j][i].getTranslateY() + event.getDeltaY());
                            }
                        }
                    }
                    for(Node troop: groundTroopRoot.getChildren()) {
                        troop.setTranslateX(troop.getTranslateX() + event.getDeltaX());
                        troop.setTranslateY(troop.getTranslateY() + event.getDeltaY());
                    }
                    for(Node troop: airTroopRoot.getChildren()) {
                        troop.setTranslateX(troop.getTranslateX() + event.getDeltaX());
                        troop.setTranslateY(troop.getTranslateY() + event.getDeltaY());
                    }
                }

            }
        });

        int finalJ = j;
        int finalI = i;
        troop1.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {

            attackGrassNodes[finalJ][finalI].setEffect(colorAdjust);

        });
        troop1.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
            attackGrassNodes[finalJ][finalI].setEffect(null);
        });

        troop1.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(putMode && attackView.selectedTroop != null) {
                    try {
                        controller.getAttackController().putTroop(finalJ, finalI);
                        addTroops(attackView.selectedTroop);
                        getAttackView().selectedTroop = null;
                    } catch (InvalidAttackCellException | InvalidCellException e) {
                        e.printStackTrace();
                    }
                }
                else if(!putMode) {
                    attackView.showTroopStatus(troop);
                }
            }
        });

        if(troop.canFly()) {
            airTroopRoot.getChildren().add(troop1);
        }
        else {
            groundTroopRoot.getChildren().add(troop1);
        }
    }

    public void configure(ImageView troop1, double j, double i, Troop troop) {
        double start = 4 * 32 * (MAP_SIZE * attackZoom / (34 * 32));
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);
        troop1.setFitHeight(32 * (26.0 / 30.0) * (MAP_SIZE * attackZoom / (34 * 32)));
        troop1.setFitWidth(32 * (26.0 / 30.0) * (MAP_SIZE * attackZoom / (34 * 32)));
        troop1.setX(start + WIDTH - MAP_SIZE + (j) * 32 * (26.0 / 30.0) * (MAP_SIZE * attackZoom / (34 * 32)));
        troop1.setY(start + (i) * 32 * (26.0 / 30.0) * (MAP_SIZE * attackZoom / (34 * 32)));

        troop1.setOnZoom(new EventHandler<ZoomEvent>() {
            @Override
            public void handle(ZoomEvent event) {
                if (attackZoom * event.getZoomFactor() > 1 && attackZoom * event.getZoomFactor() < 3) {
                    attackZoom *= event.getZoomFactor();
                    attackMap.setFitWidth(MAP_SIZE * attackZoom);
                    attackMap.setFitHeight(MAP_SIZE * attackZoom);
                    attackMap.setX(WIDTH - MAP_SIZE);
                    attackMap.setY(0);
                    attackMap.setTranslateY(0);
                    attackMap.setTranslateX(0);
                    zoomAttackMap();
                }
            }
        });

        troop1.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (attackMap.getX() + attackMap.getTranslateX() + event.getDeltaX() <= WIDTH - MAP_SIZE && attackMap.getY() + attackMap.getTranslateY() + event.getDeltaY() <= 0 && attackMap.getY() + attackMap.getTranslateY() + event.getDeltaY() + MAP_SIZE * attackZoom >= HEIGHT && attackMap.getX() + attackMap.getTranslateX() + event.getDeltaX() + MAP_SIZE * attackZoom >= WIDTH) {
                    attackMap.setTranslateX(attackMap.getTranslateX() + event.getDeltaX());
                    attackMap.setTranslateY(attackMap.getTranslateY() + event.getDeltaY());
                    for (int i = 0; i < Map.HEIGHT; i++) {
                        for (int j = 0; j < Map.WIDTH; j++) {
                            attackGrassNodes[j][i].setTranslateX(attackGrassNodes[j][i].getTranslateX() + event.getDeltaX());
                            attackGrassNodes[j][i].setTranslateY(attackGrassNodes[j][i].getTranslateY() + event.getDeltaY());
                            if (attackMapNodes[j][i] != null) {
                                attackMapNodes[j][i].setTranslateX(attackMapNodes[j][i].getTranslateX() + event.getDeltaX());
                                attackMapNodes[j][i].setTranslateY(attackMapNodes[j][i].getTranslateY() + event.getDeltaY());
                            }
                        }
                    }
                    for(Node troop: groundTroopRoot.getChildren()) {
                        troop.setTranslateX(troop.getTranslateX() + event.getDeltaX());
                        troop.setTranslateY(troop.getTranslateY() + event.getDeltaY());
                    }
                    for(Node troop: airTroopRoot.getChildren()) {
                        troop.setTranslateX(troop.getTranslateX() + event.getDeltaX());
                        troop.setTranslateY(troop.getTranslateY() + event.getDeltaY());
                    }
                }
            }
        });


        troop1.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!putMode) {
                    attackView.showTroopStatus(troop);
                }
            }
        });

    }

    public void updateChat(String text)
    {
        this.chatroomUI.getTextArea().setText(text);
    }

    public void remove() {
        Map realMap = controller.getAttackController().getDefenderMap();
        ArrayList<TroopsGUI> deadTroops = new ArrayList<>();
        for (TroopsGUI troop: troops) {
            if(troop.getTroop().isDead()) {
                deadTroops.add(troop);
                if(troop.getTroop().canFly()) {
                    airTroopRoot.getChildren().remove(troop.getPreviousImage());
                }
                else {
                    groundTroopRoot.getChildren().remove(troop.getPreviousImage());
                }
            }
        }
        for (TroopsGUI dead: deadTroops) {
            troops.remove(dead);
        }
        try {
            for (int i = 0; i < Map.HEIGHT; i++) {
                for (int j = 0; j < Map.WIDTH; j++) {
                    if(attackMapNodes[j][i] != null) {
                        if(realMap.getCell(new Location(j,i)).getBuilding() != null) {
                            if(realMap.getCell(new Location(j,i)).getBuilding().isDestroyed()) {
                                attackRoot.getChildren().remove(attackMapNodes[j][i]);
                                attackMapNodes[j][i] = null;
                            }
                        }
                    }
                }
            }
        } catch (InvalidCellException e) {
            e.printStackTrace();
        }

    }

}