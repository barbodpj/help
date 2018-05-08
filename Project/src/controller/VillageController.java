package controller;

import constants.enums.BuildingType;
import constants.enums.Section;
import constants.enums.TroopType;
import controller.exception.InvalidCommandException;
import controller.exception.gameException.LevelBoundaryException;
import controller.exception.gameException.LowResourcesException;
import model.Village;
import model.building.Building;
import model.building.headquarter.TownHall;
import model.building.resource.GoldMine;
import view.View;

public class VillageController {
    Village model;
    View view;
    MenuController menuController;

    public VillageController(Village model, View view, MenuController menuController) {
        this.model = model;
        this.view = view;
        this.menuController = menuController;
    }

    public void goNextTurn() {

    }

    public void handleShowBuildings() {
        view.printOutput(model.getBuildings());
        try {
            String input = view.getVillageView().getBuildingInput();
            int number = Integer.parseInt(input.substring(input.lastIndexOf(" ") + 1));
            String type = input.substring(0, input.lastIndexOf(" "));
            BuildingType buildingType = null;
            for(BuildingType type1: BuildingType.values()) {
                if(type1.getValue().equals(type)) {
                    buildingType = type1;
                }
            }
            if(number <= Building.getBuildingNumbers().get(buildingType)) {
                menuController.setBuildingType(buildingType);
                menuController.setBuildingNumber(number);
                for(Section section: Section.Village.getSubdivisions()) {
                    if(section.getValue().equals(buildingType.getValue())) {
                        view.println(menuController.changeSection(section));
                    }
                }
            }
        }
        catch (InvalidCommandException ignore) {

        }
    }

    public void handleShowResources() {
        view.println(model.showResources());
    }

    //TODO print menu when back?
    public void handleUpgrade() {
        Building building = model.getBuilding(menuController.getBuildingType(), menuController.getBuildingNumber());
        view.println("Upgrade Cost: " + building.getUpgradeCost());
        outer:
        while (true) {
            try {
                String input = view.getVillageView().upgradeInfo();
                if(input.matches("upgrade")) {
                    view.println("Do you want to upgrade " + menuController.getBuildingType().getValue() + " " + menuController.getBuildingNumber() + " for " + building.getUpgradeCost().getGold() + " golds? [Y/N]");
                    while (true) {
                        try {
                            if(view.getVillageView().yesNoQuestion().matches("Y"))
                            {
                                model.useResources(building.getUpgradeCost().getGold(), building.getUpgradeCost().getElixir());
                                building.upgrade();
                                break outer;
                            }
                            else {
                                view.println(menuController.changeSection(menuController.getCurrentSection()));
                                break outer;
                            }
                        }
                        catch (InvalidCommandException ignore) {

                        }
                        catch (LowResourcesException e) {
                            e.printAnnouncement();
                            break outer;
                        }
                        catch (LevelBoundaryException e) {
                            break outer;
                        }
                    }
                }
                if(input.matches("Back")) {
                    view.println(menuController.changeSection(menuController.getCurrentSection()));
                    break;
                }
            }
            catch (InvalidCommandException ignore) {

            }
        }
    }

    public void handleMenuBack() {
        view.println(menuController.back());
    }

    public void handleShowMenu() {
        view.println(menuController.showMenu());
    }

    public void handleWhereAmI() {
        if(menuController.getBuildingType() == null) {
            view.println(menuController.getCurrentSection().getValue());
        }
        else {
            view.println(menuController.getBuildingType().getValue() + " " + menuController.getBuildingNumber());
        }
    }

    public void handleInfo() {
        view.println(menuController.changeSection(menuController.getCurrentSection().getSubdivisions().get(0)));
    }

    public void handleAvailableBuildings() {
        view.println(menuController.changeSection(Section.AvailableBuildings));
    }

    public void handleTownHallStatus() {
        view.println(((TownHall)model.getBuilding(BuildingType.TownHall, 1)).showStatus());
    }

    public void handleOverallInfo() {
        Building building = model.getBuilding(menuController.getBuildingType(), menuController.getBuildingNumber());
        view.println("Level: " + building.getLevel());
        view.println("Health: " + building.getHealth());
    }

    public void handleConstructBuilding(int number) {
        switch (menuController.getCurrentSection().getDynamicValidCommands().get(number - 1)) {
            case "Gold mine":
                GoldMine goldMine = new GoldMine();
                break;
            case "Elixir mine":
                break;
            case "Gold storage":
                break;
            case "Elixir storage":
                break;
            case "Town hall":
                break;
            case "Barracks":
                break;
            case "Camp":
                break;
            case "Archer tower":
                break;
            case "Cannon":
                break;
            case "Air defence":
                break;
            case "Wizard tower":
                break;
            case "Wall":
                break;
            case "Trap":
                break;
            case "Giants castle":
                break;
        }
    }

}