import controller.*;
import controller.exception.InvalidCommandException;
import model.*;
import view.*;

public class Main {

    public static void main(String[] args) {
        MenuController menuController = new MenuController();
        View view = new View(menuController);
        Controller controller = new Controller(view, menuController);
        controller.start();

        while (true) {
            try {
                String command = view.getMenuInput();
                try {
                    int newCommand = Integer.parseInt(command);
                    switch (menuController.getCurrentSection()) {
                        case Attack:
                            break;
                        case Village:
                            break;
                        case TownHall:
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
                            break;
                        case TownHallInfo:
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
                        case BarracksInfo:
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
                            break;
                        case BuildSoldiers:

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
                        case MineInfo:
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
                                    break;
                                case 2:
                                    break;
                                case 3:
                                    break;
                            }
                            break;
                    }
                }
                catch (NumberFormatException e) {

                }
            }
            catch (InvalidCommandException ignore) {

            }
        }
    }
}