package controller;

import constants.Constants;
import constants.enums.BuildingType;
import constants.enums.Section;
import constants.enums.TroopType;
import controller.exception.InvalidCommandException;
import controller.exception.UnsupportedOperationException;
import controller.exception.gameException.BusyBuildersException;
import controller.exception.gameException.InvalidCellException;
import controller.exception.gameException.LevelBoundaryException;
import controller.exception.gameException.LowResourcesException;
import model.Location;
import model.Village;
import model.building.Building;
import model.building.defence.*;
import model.building.headquarter.Builder;
import model.building.headquarter.TownHall;
import model.building.resource.*;
import model.building.troopPreparation.Barracks;
import model.building.troopPreparation.Camp;
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
                                if(menuController.getCurrentSection() == Section.Camp) {
                                    building.upgrade();
                                }
                                model.useResources(building.getUpgradeCost().getGold(), building.getUpgradeCost().getElixir());
                                building.upgrade();
                                break outer;
                            }
                            else {
                                view.println(menuController.changeSection(menuController.getCurrentSection()));
                                break outer;
                            }
                        }
                        catch (InvalidCommandException | UnsupportedOperationException ignore) {

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
                                switch (menuController.getCurrentSection().getDynamicValidCommands().get(number - 1)) {
                                    case "Gold mine":
                                        try {
                                            GoldMine goldMine = new GoldMine(location, model);
                                            model.getMap().getCell(location).addBuilding(goldMine);
                                            ((Building) goldMine).setBuilder(builder);
                                            builder.build(goldMine);
                                            return;
                                        }
                                        catch (InvalidCellException e) {
                                            e.printAnnouncement();
                                            break;
                                        }
                                    case "Elixir mine":
                                        try {
                                            ElixirMine elixirMine = new ElixirMine(location, model)
                                            model.getMap().getCell(location).addBuilding(elixirMine);
                                            ((Building) elixirMine).setBuilder(builder);
                                            builder.build(elixirMine);
                                            return;
                                        }
                                        catch (InvalidCellException e) {
                                            e.printAnnouncement();
                                            break;
                                        }
                                    case "Gold storage":
                                        try {
                                            GoldStorage goldStorage = new GoldStorage(location, model, 0);
                                            model.getMap().getCell(location).addBuilding(goldStorage);
                                            ((Building) goldStorage).setBuilder(builder);
                                            builder.build(goldStorage);
                                            return;
                                        }
                                        catch (InvalidCellException e) {
                                            e.printAnnouncement();
                                            break;
                                        }
                                    case "Elixir storage":
                                        try {
                                            ElixirStorage elixirStorage = new ElixirStorage(location, model, 0);
                                            model.getMap().getCell(location).addBuilding(elixirStorage);
                                            ((Building) elixirStorage).setBuilder(builder);
                                            builder.build(elixirStorage);
                                            return;
                                        }
                                        catch (InvalidCellException e) {
                                            e.printAnnouncement();
                                            break;
                                        }
                                    case "Town hall":
                                        try {
                                            TownHall townHall = new TownHall(location, model);
                                            model.getMap().getCell(location).addBuilding(townHall);
                                            ((Building) townHall).setBuilder(builder);
                                            builder.build(townHall);
                                            return;
                                        }
                                        catch (InvalidCellException e) {
                                            e.printAnnouncement();
                                            break;
                                        }
                                    case "Barracks":
                                        try {
                                            Barracks barracks = new Barracks(location, model);
                                            model.getMap().getCell(location).addBuilding(barracks);
                                            ((Building) barracks).setBuilder(builder);
                                            builder.build(barracks);
                                            return;
                                        }
                                        catch (InvalidCellException e) {
                                            e.printAnnouncement();
                                            break;
                                        }
                                    case "Camp":
                                        try {
                                            Camp camp = new Camp(location, model);
                                            model.getMap().getCell(location).addBuilding(camp);
                                            ((Building) camp).setBuilder(builder);
                                            builder.build(camp);
                                            return;
                                        }
                                        catch (InvalidCellException e) {
                                            e.printAnnouncement();
                                            break;
                                        }
                                    case "Archer tower":
                                        try {
                                            ArcherTower archerTower = new ArcherTower(location, model);
                                            model.getMap().getCell(location).addBuilding(archerTower);
                                            ((Building) archerTower).setBuilder(builder);
                                            builder.build(archerTower);
                                            return;
                                        }
                                        catch (InvalidCellException e) {
                                            e.printAnnouncement();
                                            break;
                                        }
                                    case "Cannon":
                                        try {
                                            Cannon cannon = new Cannon(location, model);
                                            model.getMap().getCell(location).addBuilding(cannon);
                                            ((Building) cannon).setBuilder(builder);
                                            builder.build(cannon);
                                            return;
                                        }
                                        catch (InvalidCellException e) {
                                            e.printAnnouncement();
                                            break;
                                        }
                                    case "Air defence":
                                        try {
                                            AirDefence airDefence = new AirDefence(location, model);
                                            model.getMap().getCell(location).addBuilding(airDefence);
                                            ((Building) airDefence).setBuilder(builder);
                                            builder.build(airDefence);
                                            return;
                                        }
                                        catch (InvalidCellException e) {
                                            e.printAnnouncement();
                                            break;
                                        }
                                    case "Wizard tower":
                                        try {
                                            WizardTower wizardTower = new WizardTower(location, model);
                                            model.getMap().getCell(location).addBuilding(wizardTower);
                                            ((Building) wizardTower).setBuilder(builder);
                                            builder.build(wizardTower);
                                            return;
                                        }
                                        catch (InvalidCellException e) {
                                            e.printAnnouncement();
                                            break;
                                        }
                                    case "Wall":
                                        try {
                                            Wall wall = new Wall(location, model);
                                            model.getMap().getCell(location).addBuilding(wall);
                                            ((Building) wall).setBuilder(builder);
                                            builder.build(wall);
                                            return;
                                        }
                                        catch (InvalidCellException e) {
                                            e.printAnnouncement();
                                            break;
                                        }
                                    case "Trap":
                                        try {
                                            Trap trap = new Trap(location, model);
                                            model.getMap().getCell(location).addBuilding(trap);
                                            ((Building) trap).setBuilder(builder);
                                            builder.build(trap);
                                            return;
                                        }
                                        catch (InvalidCellException e) {
                                            e.printAnnouncement();
                                            break;
                                        }
                                    case "Giants castle":
                                        try {
                                            GiantsCastle giantsCastle = new GiantsCastle(location, model);
                                            model.getMap().getCell(location).addBuilding(giantsCastle);
                                            ((Building) giantsCastle).setBuilder(builder);
                                            builder.build(giantsCastle);
                                            return;
                                        }
                                        catch (InvalidCellException e) {
                                            e.printAnnouncement();
                                            break;
                                        }
                                }
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

    }

    public void handleTroopsConstruction(int number) {

    }

    public void handleBarracksStatus() {
        view.printOutput(((Barracks)model.getBuilding(BuildingType.Barracks, menuController.getBuildingNumber())).showStatus());
    }

    public void handleBuildSoldiers() {
        view.println(menuController.changeSection(Section.BuildSoldiers));
    }

    public void handleCampSoldiers() {
        view.printOutput(((Camp)model.getBuilding(BuildingType.Camp, menuController.getBuildingNumber())).showTroops());
    }

    public void handleCapacityInfo() {
        view.println("Your camps capacity is " + model.getNumberOfTroops() + " / " + Camp.getOverallCapacity() + ".");
    }

    public void handleMine() {
        ((Mine)model.getBuilding(menuController.getBuildingType(), menuController.getBuildingNumber())).moveToStorage();
    }

    public void handleSourcesInfo() {
        if(menuController.getBuildingType() == BuildingType.GoldStorage) {
            view.println("Your gold storage is " + model.getAvailableGold() + " / " + model.getGoldStorageCapacity() + " loaded");
        }
        else {
            view.println("Your elixir storage is " + model.getAvailableElixir() + " / " + model.getElixirStorageCapacity() + " loaded");
        }
    }

    public void handleChooseTarget() {

    }

    public void handleAttackInfo() {
        view.println(((DefenceBuilding)model.getBuilding(menuController.getBuildingType(), menuController.getBuildingNumber())).getAttackInfo());
    }

}