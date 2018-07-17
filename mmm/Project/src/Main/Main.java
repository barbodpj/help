package Main;

import controller.*;
import javafx.stage.Stage;
import view.*;

import java.io.FileNotFoundException;
import java.net.Socket;

//TODO refactor
public class Main {

    private static void generalInfoCommands(int newCommand, Controller controller) {
        switch (newCommand) {
            case 1:
                controller.getVillageController().handleOverallInfo();
                break;
            case 2:
                controller.getVillageController().handleUpgrade();
                break;
            case 3:
                controller.handleMenuBack();
                break;
        }
    }

    private static void TownHallCommands(int newCommand, Controller controller) {
        switch (newCommand) {
            case 1:
                controller.getVillageController().handleInfo();
                break;
            case 2:
                controller.getVillageController().handleAvailableBuildings();
                break;
            case 3:
                controller.getVillageController().handleTownHallStatus();
                break;
            case 4:
                controller.handleMenuBack();
                break;
        }
    }

    private static void generalCommands(String command, Controller controller) throws FileNotFoundException {
        if(command.matches("turn \\d+")) {
            String[] input = command.split(" ");
            int turns = Integer.parseInt(input[1]);
            for (int i = 0; i < turns; i++) {
                controller.getVillageController().goNextTurn();
            }
        }
        else if(command.matches("showBuildings")) {
            //controller.getVillageController().handleShowBuildings();
        }
        else if(command.matches("showMenu")) {
            controller.handleShowMenu();
        }
        else if(command.matches("whereAmI")) {
            controller.handleWhereAmI();
        }
        else if(command.matches("resources")) {
            controller.getVillageController().handleShowResources();
        }
        else if(command.matches("save .* .*")) {
            String[] input = command.split(" ");
            controller.save(input[1] + "/" + input[2] + ".json");
        }
        else if(command.matches("attack")) {
            controller.handleAttack();
        }
    }

    public static void main(Stage primaryStage , boolean newGame , String path , StartMenu startMenu) throws Exception {


        Socket socket = new Socket("localhost" , Server.port);

        System.out.println("hewr" );
        MenuController menuController = new MenuController();

        Client client = new Client(socket );


        View view = new View(menuController , primaryStage , startMenu , client );
        Controller controller = new Controller(view, menuController);
        controller.readConstants();
        view.init(controller);
\

        if (newGame)
            controller.newGame();
        else
            controller.loadGame(path);
        menuController.setController(controller);
    }

    public static void handleMenu(Controller controller, MenuController menuController, int newCommand) throws Exception {
        switch (menuController.getCurrentSection()) {
            case Attack:
                controller.handleLoadMap(newCommand);
                break;
            case Village:
                break;
            case TownHall:
                TownHallCommands(newCommand, controller);
                break;
            case TownHallInfo:
            case MineInfo:
            case BarracksInfo:
                generalInfoCommands(newCommand, controller);
                break;
            case AvailableBuildings:
                controller.getVillageController().handleConstructBuilding(newCommand);
                break;
            case Barracks:
                switch (newCommand) {
                    case 1:
                        controller.getVillageController().handleInfo();
                        break;
                    case 2:
                        controller.getVillageController().handleBuildSoldiers();
                        break;
                    case 3:
                        controller.getVillageController().handleBarracksStatus();
                        break;
                    case 4:
                        controller.handleMenuBack();
                        break;
                }
                break;
            case BuildSoldiers:
                controller.getVillageController().handleTroopsConstruction(newCommand);
                break;
            case Camp:
                switch (newCommand) {
                    case 1:
                        controller.getVillageController().handleInfo();
                        break;
                    case 2:
                        controller.getVillageController().handleCampSoldiers();
                        break;
                    case 3:
                        controller.handleMenuBack();
                        break;
                }
                break;
            case CampInfo:
                switch (newCommand) {
                    case 1:
                        controller.getVillageController().handleOverallInfo();
                        break;
                    case 2:
                        controller.getVillageController().handleUpgrade();
                        break;
                    case 3:
                        controller.getVillageController().handleCapacityInfo();
                        break;
                    case 4:
                        controller.handleMenuBack();
                        break;
                }
                break;
            case Mine:
                switch (newCommand) {
                    case 1:
                        controller.getVillageController().handleInfo();
                        break;
                    case 2:
                        controller.getVillageController().handleMine();
                        break;
                    case 3:
                        controller.handleMenuBack();
                        break;
                }
                break;
            case Storage:
                switch (newCommand) {
                    case 1:
                        controller.getVillageController().handleInfo();
                        break;
                    case 2:
                        controller.handleMenuBack();
                        break;
                }
                break;
            case StorageInfo:
                switch (newCommand) {
                    case 1:
                        controller.getVillageController().handleOverallInfo();
                        break;
                    case 2:
                        controller.getVillageController().handleUpgrade();
                        break;
                    case 3:
                        controller.getVillageController().handleSourcesInfo();
                        break;
                    case 4:
                        controller.handleMenuBack();
                        break;
                }
                break;
            case DefenceTower:
                switch (newCommand) {
                    case 1:
                        controller.getVillageController().handleOverallInfo();
                        break;
                    case 2:
                        controller.getVillageController().handleChooseTarget();
                        break;
                    case 3:
                        controller.handleMenuBack();
                        break;
                }
                break;
            case DefenceTowerInfo:
                switch (newCommand) {
                    case 1:
                        controller.getVillageController().handleOverallInfo();
                        break;
                    case 2:
                        controller.getVillageController().handleUpgrade();
                        break;
                    case 3:
                        controller.getVillageController().handleAttackInfo();
                        break;
                    case 4:
                        controller.handleMenuBack();
                        break;
                }
                break;
            case AttackMap:
                switch (newCommand) {
                    case 1:
                        controller.getAttackController().attackMapInfo();
                        break;
                    case 2:
                        controller.getAttackController().attack(controller.getView().getPrimaryStage());
                        break;
                    case 3:
                        controller.handleMenuBack();
                        break;
                }
                break;
        }
    }
}