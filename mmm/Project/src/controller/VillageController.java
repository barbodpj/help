package controller;

import constants.Constants;
import constants.enums.BuildingType;
import constants.enums.Section;
import constants.enums.TroopType;
import controller.exception.*;
import controller.exception.UnsupportedOperationException;
import controller.exception.gameException.*;
import model.Location;
import model.Village;
import model.building.Building;
import model.building.defence.*;
import model.building.headquarter.Builder;
import model.building.headquarter.TownHall;
import model.building.resource.*;
import model.building.troopPreparation.Barracks;
import model.building.troopPreparation.Camp;
import model.map.Map;
import view.View;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class VillageController {
    private Village model;
    private View view;
    private MenuController menuController;
    private int time = 10;
    private Thread timeThread;
    boolean paused = false;
    private double speed = 1;

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public Map getMap() {
        return model.getMap();
    }

    public VillageController(Village model, View view, MenuController menuController) {
        this.model = model;
        ((Building)model.getBuilding(BuildingType.TownHall, 1)).undoChangeNumbers();
        this.view = view;
        this.menuController = menuController;
        timeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep((long) (time / speed * 1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    goNextTurn();
                }
            }
        });
        timeThread.start();
    }

    public void pause() {
        if(!paused) {
            timeThread.suspend();
            paused = true;
        }
        else {
            timeThread.resume();
            paused = false;
        }
    }

    public void goNextTurn() {
        model.produceResources();
        ((TownHall)model.getBuilding(BuildingType.TownHall, 1)).updateBuilders();
        for (int i = 1; i <= Building.getBuildingNumbers().get(BuildingType.Barracks); i++) {
            ((Barracks)model.getBuilding(BuildingType.Barracks, i)).decreaseRemainingTime();
        }
        model.updateUpgradeQueue();
        view.update = true;
    }

    public ArrayList<Building> getNewBuildings() {
        ArrayList<Building> buildings = new ArrayList<>();
        for(BuildingType type: ((TownHall)model.getBuilding(BuildingType.TownHall, 1)).getBuildingQueue().keySet()) {
            buildings.addAll(((TownHall) model.getBuilding(BuildingType.TownHall, 1)).getBuildingQueue().get(type));
        }
        return buildings;
    }

    public String getBuildings() {
        String buildings = "";
        for (int i = 0; i < model.getBuildings().size(); i++) {
            if(buildings.equals("")) {
                buildings = buildings + model.getBuildings().get(i) ;
            }
            else {
                buildings = buildings + "\n" + model.getBuildings().get(i) ;
            }
        }
        return buildings;
    }

    public void handleShowBuildings(String input)  {
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
            if(buildingType.getValue().equals("Gold mine") || buildingType.getValue().equals("Elixir mine")) {
                view.showMenu(menuController.changeSection(Section.Mine));
            }
            else if(buildingType.getValue().equals("Gold storage") || buildingType.getValue().equals("Elixir storage")) {
                view.showMenu(menuController.changeSection(Section.Storage));
            }
            else if(buildingType.getValue().equals("Archer tower") || buildingType.getValue().equals("Air defence") || buildingType.getValue().equals("Giants castle") || buildingType.getValue().equals("Cannon") || buildingType.getValue().equals("Cannon")) {
                view.showMenu(menuController.changeSection(Section.DefenceTower));
            }
            else {
                for(Section section: Section.Village.getSubdivisions()) {
                    if(section.getValue().equals(buildingType.getValue())) {
                        view.showMenu(menuController.changeSection(section));
                    }
                }
            }
        }
    }

    public void handleShowResources() {
        view.showResources(model.showResources());
    }

    //TODO print menu when back?
    public void handleUpgrade() {
        Building building = model.getBuilding(menuController.getBuildingType(), menuController.getBuildingNumber());
        view.showUpgrade(building.ShowUpgradeInfo());
    }

    public void handleUpgradeQuestion() {
        Building building = model.getBuilding(menuController.getBuildingType(), menuController.getBuildingNumber());

        try {
            view.yesNoQuestion("Do you want to upgrade " + menuController.getBuildingType().getValue() + " " + menuController.getBuildingNumber() + " for " + building.getUpgradeCost().getGold() + " golds?", "upgrade", null, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    public void handleUpgradeBuilding() {
        Building building = model.getBuilding(menuController.getBuildingType(), menuController.getBuildingNumber());
        try {
            if(menuController.getCurrentSection() == Section.Camp) {
                building.upgrade();
            }
            model.useResources(building.getUpgradeCost().getGold(), building.getUpgradeCost().getElixir());
            view.updateInformationLabel();;
            building.upgrade();
        }
        catch (LevelBoundaryException | UnsupportedOperationException ignored) {

        }
        catch (LowResourcesException e) {
            e.printAnnouncement();
        }
    }

    public void handleInfo() {
        view.showMenu(menuController.changeSection(menuController.getCurrentSection().getSubdivisions().get(0)));
    }

    public void handleAvailableBuildings() {
        Section.AvailableBuildings.updateValidCommands(((TownHall)model.getBuilding(BuildingType.TownHall, 1)).getAvailableBuildings());
        Section.updateMenu();
        view.showMenu(menuController.changeSection(Section.AvailableBuildings));
    }

    public void handleTownHallStatus() {
        view.showTownHallStatus(((TownHall)model.getBuilding(BuildingType.TownHall, 1)).showStatus());
    }

    public void handleOverallInfo() {
        Building building = model.getBuilding(menuController.getBuildingType(), menuController.getBuildingNumber());
        view.showInfo(building.ShowOverallInfo());
    }


    public void handleConstruction(Builder builder, Integer number, Location location) {
        BuildingType buildingType = null;
        for(BuildingType type: BuildingType.values()) {
            if(type.getValue().equals(menuController.getCurrentSection().getDynamicValidCommands().get(number - 1))) {
                buildingType = type;
            }
        }
        try {
            model.useResources(Constants.buildingBuildCost.get(buildingType).getGold(), Constants.buildingBuildCost.get(buildingType).getElixir());
            view.updateInformationLabel();;

        } catch (LowResourcesException e) {
            e.printAnnouncement();
        }
        //view.showMap(model.getMap().getBooleanMap());
        //view.showAnnouncement("Where do you want to build " + menuController.getCurrentSection().getDynamicValidCommands().get(number - 1) + "?");

        switch (menuController.getCurrentSection().getDynamicValidCommands().get(number - 1)) {
            case "Gold mine":
                try {
                    GoldMine goldMine = new GoldMine(location, model);
                    model.getMap().getCell(location).addBuilding(goldMine);
                    menuController.showMenu();
                    ((Building) goldMine).setBuilder(builder);
                    builder.build(goldMine);
                    Section.AvailableBuildings.updateValidCommands(((TownHall) model.getBuilding(BuildingType.TownHall, 1)).getAvailableBuildings());
                    Section.updateMenu();
                    view.startBuilding();
                    view.buildMode = false;
                    view.updateInformationLabel();
                    view.showMenu(menuController.showMenu());
                    return;
                } catch (InvalidCellException e) {
                    e.printAnnouncement();
                    break;
                }
            case "Elixir mine":
                try {
                    ElixirMine elixirMine = new ElixirMine(location, model);
                    model.getMap().getCell(location).addBuilding(elixirMine);
                    menuController.showMenu();
                    ((Building) elixirMine).setBuilder(builder);
                    builder.build(elixirMine);
                    Section.AvailableBuildings.updateValidCommands(((TownHall) model.getBuilding(BuildingType.TownHall, 1)).getAvailableBuildings());
                    Section.updateMenu();
                    view.startBuilding();
                    view.buildMode = false;
                    view.updateInformationLabel();
                    view.showMenu(menuController.showMenu());
                    return;
                } catch (InvalidCellException e) {
                    e.printAnnouncement();
                    break;
                }
            case "Gold storage":
                try {
                    GoldStorage goldStorage = new GoldStorage(location, model, 0);
                    model.getMap().getCell(location).addBuilding(goldStorage);
                    menuController.showMenu();
                    ((Building) goldStorage).setBuilder(builder);
                    builder.build(goldStorage);
                    Section.AvailableBuildings.updateValidCommands(((TownHall) model.getBuilding(BuildingType.TownHall, 1)).getAvailableBuildings());
                    Section.updateMenu();
                    view.startBuilding();
                    view.buildMode = false;
                    view.updateInformationLabel();
                    view.showMenu(menuController.showMenu());
                    return;
                } catch (InvalidCellException e) {
                    e.printAnnouncement();
                    break;
                }
            case "Elixir storage":
                try {
                    ElixirStorage elixirStorage = new ElixirStorage(location, model, 0);
                    model.getMap().getCell(location).addBuilding(elixirStorage);
                    menuController.showMenu();
                    ((Building) elixirStorage).setBuilder(builder);
                    builder.build(elixirStorage);
                    Section.AvailableBuildings.updateValidCommands(((TownHall) model.getBuilding(BuildingType.TownHall, 1)).getAvailableBuildings());
                    Section.updateMenu();
                    view.startBuilding();
                    view.buildMode = false;
                    view.updateInformationLabel();
                    view.showMenu(menuController.showMenu());
                    return;
                } catch (InvalidCellException e) {
                    e.printAnnouncement();
                    break;
                }
            case "Town hall":
                try {
                    TownHall townHall = new TownHall(location, model);
                    model.getMap().getCell(location).addBuilding(townHall);
                    menuController.showMenu();
                    ((Building) townHall).setBuilder(builder);
                    builder.build(townHall);
                    Section.AvailableBuildings.updateValidCommands(((TownHall) model.getBuilding(BuildingType.TownHall, 1)).getAvailableBuildings());
                    Section.updateMenu();
                    view.startBuilding();
                    view.buildMode = false;
                    view.updateInformationLabel();
                    view.showMenu(menuController.showMenu());
                    return;
                } catch (InvalidCellException e) {
                    e.printAnnouncement();
                    break;
                }
            case "Barracks":
                try {
                    Barracks barracks = new Barracks(location, model);
                    model.getMap().getCell(location).addBuilding(barracks);
                    menuController.showMenu();
                    ((Building) barracks).setBuilder(builder);
                    builder.build(barracks);
                    Section.AvailableBuildings.updateValidCommands(((TownHall) model.getBuilding(BuildingType.TownHall, 1)).getAvailableBuildings());
                    Section.updateMenu();
                    view.startBuilding();
                    view.buildMode = false;
                    view.updateInformationLabel();
                    view.showMenu(menuController.showMenu());
                    return;
                } catch (InvalidCellException e) {
                    e.printAnnouncement();
                    break;
                }
            case "Camp":
                try {
                    Camp camp = new Camp(location, model);
                    model.getMap().getCell(location).addBuilding(camp);
                    menuController.showMenu();
                    ((Building) camp).setBuilder(builder);
                    builder.build(camp);
                    Section.AvailableBuildings.updateValidCommands(((TownHall) model.getBuilding(BuildingType.TownHall, 1)).getAvailableBuildings());
                    Section.updateMenu();
                    view.startBuilding();
                    view.buildMode = false;
                    view.updateInformationLabel();
                    view.showMenu(menuController.showMenu());
                    return;
                } catch (InvalidCellException e) {
                    e.printAnnouncement();
                    break;
                }
            case "Archer tower":
                try {
                    ArcherTower archerTower = new ArcherTower(location, model);
                    model.getMap().getCell(location).addBuilding(archerTower);
                    menuController.showMenu();
                    ((Building) archerTower).setBuilder(builder);
                    builder.build(archerTower);
                    Section.AvailableBuildings.updateValidCommands(((TownHall) model.getBuilding(BuildingType.TownHall, 1)).getAvailableBuildings());
                    Section.updateMenu();
                    view.startBuilding();
                    view.buildMode = false;
                    view.updateInformationLabel();
                    view.showMenu(menuController.showMenu());
                    return;
                } catch (InvalidCellException e) {
                    e.printAnnouncement();
                    break;
                }
            case "Cannon":
                try {
                    Cannon cannon = new Cannon(location, model);
                    model.getMap().getCell(location).addBuilding(cannon);
                    menuController.showMenu();
                    ((Building) cannon).setBuilder(builder);
                    builder.build(cannon);
                    Section.AvailableBuildings.updateValidCommands(((TownHall) model.getBuilding(BuildingType.TownHall, 1)).getAvailableBuildings());
                    Section.updateMenu();
                    view.startBuilding();
                    view.buildMode = false;
                    view.updateInformationLabel();
                    view.showMenu(menuController.showMenu());
                    return;
                } catch (InvalidCellException e) {
                    e.printAnnouncement();
                    break;
                }
            case "Air defence":
                try {
                    AirDefence airDefence = new AirDefence(location, model);
                    model.getMap().getCell(location).addBuilding(airDefence);
                    menuController.showMenu();
                    ((Building) airDefence).setBuilder(builder);
                    builder.build(airDefence);
                    Section.AvailableBuildings.updateValidCommands(((TownHall) model.getBuilding(BuildingType.TownHall, 1)).getAvailableBuildings());
                    Section.updateMenu();
                    view.startBuilding();
                    view.buildMode = false;
                    view.updateInformationLabel();
                    view.showMenu(menuController.showMenu());
                    return;
                } catch (InvalidCellException e) {
                    e.printAnnouncement();
                    break;
                }
            case "Wizard tower":
                try {
                    WizardTower wizardTower = new WizardTower(location, model);
                    model.getMap().getCell(location).addBuilding(wizardTower);
                    menuController.showMenu();
                    ((Building) wizardTower).setBuilder(builder);
                    builder.build(wizardTower);
                    Section.AvailableBuildings.updateValidCommands(((TownHall) model.getBuilding(BuildingType.TownHall, 1)).getAvailableBuildings());
                    Section.updateMenu();
                    view.startBuilding();
                    view.buildMode = false;
                    view.updateInformationLabel();
                    view.showMenu(menuController.showMenu());
                    return;
                } catch (InvalidCellException e) {
                    e.printAnnouncement();
                    break;
                }
            case "Wall":
                try {
                    Wall wall = new Wall(location, model);
                    model.getMap().getCell(location).addBuilding(wall);
                    menuController.showMenu();
                    ((Building) wall).setBuilder(builder);
                    builder.build(wall);
                    Section.AvailableBuildings.updateValidCommands(((TownHall) model.getBuilding(BuildingType.TownHall, 1)).getAvailableBuildings());
                    Section.updateMenu();
                    view.startBuilding();
                    view.buildMode = false;
                    view.updateInformationLabel();
                    view.showMenu(menuController.showMenu());
                    return;
                } catch (InvalidCellException e) {
                    e.printAnnouncement();
                    break;
                }
            case "Trap":
                try {
                    Trap trap = new Trap(location, model);
                    model.getMap().getCell(location).addBuilding(trap);
                    menuController.showMenu();
                    ((Building) trap).setBuilder(builder);
                    builder.build(trap);
                    Section.AvailableBuildings.updateValidCommands(((TownHall) model.getBuilding(BuildingType.TownHall, 1)).getAvailableBuildings());
                    Section.updateMenu();
                    view.startBuilding();
                    view.buildMode = false;
                    view.updateInformationLabel();
                    view.showMenu(menuController.showMenu());
                    return;
                } catch (InvalidCellException e) {
                    e.printAnnouncement();
                    break;
                }
            case "Giants castle":
                try {
                    GiantsCastle giantsCastle = new GiantsCastle(location, model);
                    model.getMap().getCell(location).addBuilding(giantsCastle);
                    menuController.showMenu();
                    ((Building) giantsCastle).setBuilder(builder);
                    builder.build(giantsCastle);
                    Section.AvailableBuildings.updateValidCommands(((TownHall) model.getBuilding(BuildingType.TownHall, 1)).getAvailableBuildings());
                    Section.updateMenu();
                    view.startBuilding();
                    view.buildMode = false;
                    view.updateInformationLabel();
                    view.showMenu(menuController.showMenu());
                    return;
                } catch (InvalidCellException e) {
                    e.printAnnouncement();
                    break;
                }
        }
    }

    public void handleConstructBuilding(int number) {
        if(number == menuController.getCurrentSection().getDynamicValidCommands().size() + 1) {
            view.showMenu(menuController.back());
            return;
        }
        Builder builder = null;
        try {
            builder = ((TownHall)model.getBuilding(BuildingType.TownHall, 1)).callBuilder();
            BuildingType buildingType = null;
            for(BuildingType type: BuildingType.values()) {
                if(type.getValue().equals(menuController.getCurrentSection().getDynamicValidCommands().get(number - 1))) {
                    buildingType = type;
                }
            }

            try {
                view.yesNoQuestion("Do you want to build " + menuController.getCurrentSection().getDynamicValidCommands().get(number - 1) + " for " + Constants.buildingBuildCost.get(buildingType).getGold() + " golds and " + Constants.buildingBuildCost.get(buildingType).getElixir() + " elixirs?", "build", number, builder);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        catch (BusyBuildersException e) {
            e.printAnnouncement();
        }
    }

    public void handleTroopsConstruction(int number) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if(number == menuController.getCurrentSection().getDynamicValidCommands().size() + 1) {
            view.showMenu(menuController.back());
            return;
        }
        String[] input = menuController.getCurrentSection().getDynamicValidCommands().get(number - 1).split(" ");
        try {
            if(input[1].equals("U")) {
                throw new UnavailableTroopException();
            }


            view.getVillageView().getTroopNumber(Integer.parseInt(input[2].substring(1)) ,number);

        }
        catch (UnavailableTroopException e) {
            e.printAnnouncement();
        }
        catch (InvalidCommandException ignore) {

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void train(int amount, int number) throws LowResourcesException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String[] input = menuController.getCurrentSection().getDynamicValidCommands().get(number - 1).split(" ");
        if(amount > Integer.parseInt(input[2].substring(1))) {
            throw new LowResourcesException();
        }
        TroopType type = null;
        for(TroopType troopType: TroopType.values()) {
            if(troopType.getValue().equals(input[0])) {
                type = troopType;
                break;
            }
        }
        model.useResources(Constants.troopsBuildCost.get(type).getGold(), Constants.troopsBuildCost.get(type).getElixir());
        view.updateInformationLabel();;
        Section.BuildSoldiers.updateValidCommands(((Barracks)model.getBuilding(menuController.getBuildingType(), menuController.getBuildingNumber())).getAvailableTroops());
        Section.updateMenu();
        ((Barracks)model.getBuilding(BuildingType.Barracks, menuController.getBuildingNumber())).build(type, amount);
    }

    public void handleBarracksStatus() throws FileNotFoundException {
        view.showStatus(((Barracks)model.getBuilding(BuildingType.Barracks, menuController.getBuildingNumber())).showStatus());
    }

    public void handleBuildSoldiers() throws IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
        Section.BuildSoldiers.updateValidCommands(((Barracks)model.getBuilding(menuController.getBuildingType(), menuController.getBuildingNumber())).getAvailableTroops());
        Section.updateMenu();
        view.showMenu(menuController.changeSection(Section.BuildSoldiers));
    }

    public void handleCampSoldiers() throws FileNotFoundException {
        view.showStatus(((Camp)model.getBuilding(BuildingType.Camp, menuController.getBuildingNumber())).showTroops());
    }

    public void handleCapacityInfo() {
        view.showAnnouncement("Your camps capacity is " + model.getNumberOfTroops() + " / " + Camp.getOverallCapacity() + ".");
    }

    public void handleMine() {
        ((Mine)model.getBuilding(menuController.getBuildingType(), menuController.getBuildingNumber())).moveToStorage();
        view.updateInformationLabel();
    }

    public void handleSourcesInfo() {
        if(menuController.getBuildingType() == BuildingType.GoldStorage) {
            view.showAnnouncement( + model.getAvailableGold() + " / " + model.getGoldStorageCapacity() + " loaded");
        }
        else {
            view.showAnnouncement( model.getAvailableElixir() + " / " + model.getElixirStorageCapacity() + " loaded");
        }
    }

    public void handleChooseTarget() {
        view.showAnnouncement("choose your priority:");
        String target = null;
        while (true) {
            try {
                target = view.getVillageView().getTarget();
                break;
            }
            catch (InvalidCommandException ignore) {

            }
        }
        TroopType type = null;
        for(TroopType troopType: TroopType.values()) {
            if(troopType.getValue().equals(target)) {
                type = troopType;
                break;
            }
        }
        ((DefenceBuilding)model.getBuilding(menuController.getBuildingType(), menuController.getBuildingNumber())).chooseTarget(type);
    }

    public void handleAttackInfo() throws InvalidCellException {
        view.showInfo(((DefenceBuilding)model.getBuilding(menuController.getBuildingType(), menuController.getBuildingNumber())).getAttackInfo(model.getMap()));
    }

}