package controller;

import com.gilecode.yagson.YaGson;

import constants.Constants;
import constants.enums.BuildingType;
import constants.enums.Section;
import constants.enums.TroopType;
import controller.exception.InvalidCommandException;
import json.JMap;
import model.*;
import model.map.Map;
import view.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

//TODO add save option
public class Controller {
    private Village model = null;
    private View view;
    private AttackController attackController;
    private VillageController villageController;
    private MenuController menuController;

    public View getView() {
        return view;
    }

    public Controller(View view, MenuController menuController) {
        this.view = view;
        this.menuController = menuController;
    }

    public void readConstants() throws FileNotFoundException {
        ArrayList<String> input = view.fileReader("Config.txt");
        ArrayList<String[]> splitInput = new ArrayList<>();
        for(int i = 0; i < input.size(); i++) {
            splitInput.add(input.get(i).split(" "));
        }
        for(int i = 1; i < 15; i++) {
            BuildingType type = BuildingType.valueOf(splitInput.get(i)[0]);
            if(splitInput.get(i)[1].equals("null") || splitInput.get(i)[2].equals("null")) {
                Constants.buildingBuildCost.put(type, null);
            }
            else {
                Constants.buildingBuildCost.put(type, new Cost(Integer.parseInt(splitInput.get(i)[1]), Integer.parseInt(splitInput.get(i)[2])));
            }
            Constants.buildingBuildTime.put(type, Integer.parseInt(splitInput.get(i)[3]));
            Constants.attackScoreGained.put(type, Integer.parseInt(splitInput.get(i)[4]));
            if(splitInput.get(i)[5].equals("null")) {
                Constants.buildingsAttackRange.put(type, null);
            }
            else {
                Constants.buildingsAttackRange.put(type, Integer.parseInt(splitInput.get(i)[5]));
            }
            if(splitInput.get(i)[6].equals("null")) {
                Constants.buildingsUpgradeCost.put(type, null);
            }
            else {
                Constants.buildingsUpgradeCost.put(type, new Cost(Integer.parseInt(splitInput.get(i)[6]), 0));
            }
            if(splitInput.get(i)[7].equals("null")) {
                Constants.buildingsUpgradeTime.put(type, null);
            }
            else {
                Constants.buildingsUpgradeTime.put(type, Integer.parseInt(splitInput.get(i)[7]));
            }
            if(splitInput.get(i)[8].equals("null")) {
                Constants.buildingsInitialDamage.put(type, null);
            }
            else {
                Constants.buildingsInitialDamage.put(type, Integer.parseInt(splitInput.get(i)[8]));
            }
            if(splitInput.get(i)[9].equals("null")) {
                Constants.buildingInitialHealth.put(type, null);
            }
            else {
                Constants.buildingInitialHealth.put(type, Integer.parseInt(splitInput.get(i)[9]));
            }
            if(splitInput.get(i)[10].equals("null")) {
                Constants.squareOfDamageRadius.put(type, null);
            }
            else {
                Constants.squareOfDamageRadius.put(type, Integer.parseInt(splitInput.get(i)[10]));
            }
        }

        for(int i = 16; i < 22; i++) {
            TroopType type = TroopType.valueOf(splitInput.get(i)[0]);
            Constants.troopsBuildCost.put(type, new Cost(0, Integer.parseInt(splitInput.get(i)[1])));
            Constants.troopsBuildTime.put(type, Integer.parseInt(splitInput.get(i)[2]));
            Constants.troopsAttackRange.put(type, Integer.parseInt(splitInput.get(i)[3]));
            Constants.troopsMaxSpeed.put(type, Integer.parseInt(splitInput.get(i)[4]));
            Constants.troopsInitialDamage.put(type, Integer.parseInt(splitInput.get(i)[5]));
            Constants.troopsInitialHealth.put(type, Integer.parseInt(splitInput.get(i)[6]));
            Constants.troopsUpgradeCost.put(type, new Cost(0, 0));
        }
    }

    public void newGame()
    {
        model = new Village();
        villageController = new VillageController(model, view, menuController);
    }

    public void loadGame(String path)
    {
        model = load(path);
        villageController = new VillageController(model, view, menuController);
    }

    public void save(String filePath) {
//        try {
//            FileWriter fileWriter = new FileWriter(filePath);
//            YaGson yaGson = new YaGson();
//            String jsonString = yaGson.toJson(model);
//            fileWriter.write(jsonString);
//            fileWriter.close();
//
//        } catch (IOException e) {
//            view.println("No such path");
//        }
        try {
            File file = new File(filePath);
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(model);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Village load(String filePath) {
        try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Village) inputStream.readObject();
        } catch (FileNotFoundException e) {
            view.println("File not found");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AttackController getAttackController() {
        return attackController;
    }

    public VillageController getVillageController() {
        return villageController;
    }

    public void handleMenuBack() {
        view.showMenu(menuController.back());
    }

    public void handleShowMenu() {
        view.showMenu(menuController.showMenu());
    }

    public void handleWhereAmI() {
        if(menuController.getBuildingType() == null) {
            view.showSection(menuController.getCurrentSection().getValue());
        }
        else {
            view.showSection(menuController.getBuildingType().getValue() + " " + menuController.getBuildingNumber());
        }
    }

    public void handleAttackMap(String path) throws Exception {
        try {
            JMap jMap = JMap.readMap(path);
            Map map = jMap.toMap();
            model.getMap().addMap(path);
            Section.Attack.updateValidCommands(model.getMap().getAttackedMaps());
            Section.updateMenu();
            attackController = new AttackController(model, map.getMapBuilding(), map, view);
            view.showMenu(menuController.changeSection(Section.AttackMap));
        }
        catch (FileNotFoundException e) {
            view.println("There is no valid file in this location.");
        }
    }

    public void handleLoadMap(int number) throws Exception {
        if(number == 1) {
            view.getAttackMapPath();
            return;
        }
        if(number == model.getMap().getAttackedMaps().size() + 2) {
            view.showMenu(menuController.back());
            return;
        }
        String path = model.getMap().getAttackedMapPaths(number - 1);
        try {
            JMap jMap = JMap.readMap(path);
            Map map = jMap.toMap();
            attackController = new AttackController(model, map.getMapBuilding(), map, view);
            view.showMenu(menuController.changeSection(Section.AttackMap));
        }
        catch (FileNotFoundException e) {
            view.println("There is no valid file in this location.");
        }
    }

    public void handleAttack() {
        Section.Attack.updateValidCommands(model.getMap().getAttackedMaps());
        Section.updateMenu();
        view.showMenu(menuController.changeSection(Section.Attack));
    }

}