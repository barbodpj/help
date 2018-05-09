package controller;

import constants.Constants;
import constants.enums.BuildingType;
import constants.enums.Section;
import constants.enums.TroopType;
import controller.exception.InvalidCommandException;
import controller.exception.gameException.BusyBuildersException;
import controller.exception.gameException.LevelBoundaryException;
import controller.exception.gameException.LowResourcesException;
import model.Location;
import model.Village;
import model.building.Building;
import model.building.headquarter.Builder;
import model.building.headquarter.TownHall;
import model.building.resource.GoldMine;
import view.View;

public class VillageController {
    private Village model;
    private View view;
    private MenuController menuController;

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
        if(number == menuController.getCurrentSection().getDynamicValidCommands().size() + 1) {
            view.println(menuController.back());
            return;
        }
        Location location = null;
        Builder builder = null;
        try {
            builder = ((TownHall)model.getBuilding(BuildingType.TownHall, 1)).callBuilder();
            view.println("Do you want to build " + menuController.getCurrentSection().getDynamicValidCommands().get(number - 1) + " for " + Constants.buildingBuildCost.get(BuildingType.valueOf(menuController.getCurrentSection().getDynamicValidCommands().get(number - 1))).getGold() + " golds and " + Constants.buildingBuildCost.get(BuildingType.valueOf(menuController.getCurrentSection().getDynamicValidCommands().get(number - 1))).getElixir() + " elixirs? [Y/N]");
            outer:
            while (true) {
                try {
                    if(view.getVillageView().yesNoQuestion().matches("Y")) {
                        model.useResources(Constants.buildingBuildCost.get(BuildingType.valueOf(menuController.getCurrentSection().getDynamicValidCommands().get(number - 1))).getGold(), Constants.buildingBuildCost.get(BuildingType.valueOf(menuController.getCurrentSection().getDynamicValidCommands().get(number - 1))).getElixir());
                        view.println(model.getMap().getBooleanMap());
                        view.println("Where do you want to build " + menuController.getCurrentSection().getDynamicValidCommands().get(number - 1) + " ?");
                        while (true) {
                            try {
                                String position = view.getVillageView().getLocation();
                                String[] loc = position.split(" ");
                                location = new Location(Integer.parseInt(loc[0].substring(1, loc[0].length() - 1)), Integer.parseInt(loc[1].substring(0, loc[1].length() - 1)));
                                break outer;
                            }
                            catch (InvalidCommandException ignore) {

                            }
                        }
                    }
                    else {
                        view.println(menuController.changeSection(menuController.getCurrentSection()));
                        return;
                    }
                }
                catch (InvalidCommandException ignored) {

                }
                catch (LowResourcesException e) {
                    e.printAnnouncement();
                    return;
                }
            }
        }
        catch (BusyBuildersException e) {
            e.printAnnouncement();
            return;
        }
        switch (menuController.getCurrentSection().getDynamicValidCommands().get(number - 1)) {
            case "Gold mine":
                GoldMine goldMine = new GoldMine(location, model);
                model.getMap().getCell(location).addBuilding(goldMine);
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