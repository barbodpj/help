package controller;

import constants.enums.BuildingType;
import constants.enums.TroopType;
import controller.exception.InvalidCommandException;
import controller.exception.gameException.InvalidAttackCellException;
import controller.exception.gameException.InvalidCellException;
import controller.exception.gameException.NotEnoughTroopsException;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Cost;
import model.Location;
import model.Village;
import model.building.Building;
import model.building.defence.DefenceBuilding;
import model.building.resource.ElixirStorage;
import model.building.resource.GoldStorage;
import model.map.Map;
import model.troop.Troop;
import view.View;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;


public class AttackController {

    private Cost moneyGained;

    private HashMap<TroopType, ArrayList<Troop>> selectedTroops = new HashMap<TroopType, ArrayList<Troop>>();

    private HashMap<TroopType, ArrayList<Troop>> putTroops = new HashMap<TroopType, ArrayList<Troop>>();

    public Village attackerVillage;

    private HashMap<BuildingType, ArrayList<Building>> defenderBuildings;

    private Map defenderMap;

    public int scoreGained = 0;

    private View view;

    private double time = 0.5;

    public Thread timeThread;

    public HashMap<TroopType, ArrayList<Troop>> getSelectedTroops() {
        return selectedTroops;
    }

    boolean paused = false;
    private double speed = 1;

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
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

    public Map getDefenderMap() {
        return defenderMap;
    }

    private Cost availableCost() {
        int availableGold = 0;
        int availableElixir = 0;

        for (Building goldStorage : this.defenderBuildings.get(BuildingType.GoldStorage)) {
            availableGold += ((GoldStorage) goldStorage).getResource();
        }

        for (Building elixirStorage : this.defenderBuildings.get(BuildingType.ElixirStorage)) {
            availableElixir += ((ElixirStorage) elixirStorage).getResource();
        }

        return new Cost(availableGold, availableElixir);
    }

    public AttackController(Village attackerVillage, HashMap<BuildingType, ArrayList<Building>> defenderBuildings,
                            Map defenderMap, View view) throws InvalidCellException {

        this.attackerVillage = attackerVillage;
        this.defenderMap = defenderMap;
        this.defenderBuildings = defenderBuildings;
        this.view = view;

        Cost cost = new Cost( 0 , 0 );
        this.moneyGained = cost;

        for (int i = 0; i < Map.WIDTH; i++) {
            for (int j = 0; j < Map.HEIGHT; j++) {

                Location location = new Location(i, j);
                Building building = defenderMap.getCell(location).getBuilding();

                if (building != null)
                    (building).startAttack();

            }

            timeThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep((long) (time / speed * 1000));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        view.nextState = true;
                    }
                }
            });
            timeThread.start();
        }


    }

    public boolean isAttackFinished() {
        if (attackerVillage.getGoldStorageCapacity() - attackerVillage.getAvailableGold() == 0 &&
                attackerVillage.getElixirStorageCapacity() - attackerVillage.getAvailableGold() == 0 ||
                this.availableCost().getGold() == moneyGained.getGold() &&
                this.availableCost().getElixir() == moneyGained.getElixir()) {
            return true;
        }

        boolean allTroopsDead = true;

        for (TroopType troopType:this.putTroops.keySet())
        {
            for (Troop troop:this.putTroops.get(troopType))
            {
                if(!troop.isDead())
                {
                    allTroopsDead = false;
                    break;
                }
            }
        }
        if(allTroopsDead)
            return true;

        return false;
    }

    public void nextTurn() throws InvalidCellException {
        for (ArrayList<Troop> troops : this.putTroops.values()) {
            for (Troop troop : troops) {
                Building building = troop.nextMoveInAttack(defenderMap);

                if (building == null)
                    continue;

                Cost cost = building.getBuildCost();

                if (BuildingType.valueOf(building.getClass().getSimpleName()) == BuildingType.GoldStorage) {
                    int gold = building.getBuildCost().getGold() + ((GoldStorage) building).getResource();
                    int elixir = building.getBuildCost().getElixir();
                    cost = new Cost(gold, elixir);
                }

                if (BuildingType.valueOf(building.getClass().getSimpleName()) == BuildingType.ElixirStorage) {
                    int gold = building.getBuildCost().getGold();
                    int elixir = building.getBuildCost().getElixir() + ((ElixirStorage) building).getResource();
                    cost = new Cost(gold, elixir);
                }

                if (BuildingType.valueOf(building.getClass().getSimpleName()) == BuildingType.TownHall){

                    int gold = 10000;
                    int elixir = 500;
                    cost = new Cost(gold,elixir);
                }


                int remainingGold = attackerVillage.addGold(cost.getGold());

                int remainingElixir = attackerVillage.addElixir(cost.getElixir());

                int gold = this.moneyGained.getGold() + cost.getGold() - remainingGold;

                int elixir = this.moneyGained.getElixir() + cost.getElixir() - remainingElixir;

                moneyGained = new Cost(gold, elixir);

                this.scoreGained += building.getAttackScoreGained();
            }

        }

        for (int i = 0; i < Map.WIDTH; i++) {
            for (int j = 0; j < Map.HEIGHT; j++) {

                Location location = new Location(i, j);
                Building building = defenderMap.getCell(location).getBuilding();

                if (building != null && building.isDefending())
                    ((DefenceBuilding) building).attack(defenderMap);

            }
        }


    }


    public void handleTroopSelection() {
//        while (true) {
//            try {
//                view.getAttackView().startSelection();
//                break;
//            } catch (InvalidCommandException ignore) {
//
//            }
//        }

        for (TroopType troopType : TroopType.values()) {
            ArrayList<Troop> troops = new ArrayList<>();
            try {
                troops.addAll(attackerVillage.getRandomTroops(troopType, attackerVillage.getAvailableTroops(troopType)));
            } catch (NotEnoughTroopsException e) {
                e.printStackTrace();
            }
            selectedTroops.put(troopType, troops);
        }

//        while (true) {
//            try {
//                String input = view.getAttackView().selectUnits();
//                if (input.equals("End select")) {
//                    break;
//                }
//                String[] splitInput = input.split(" ");
//                TroopType type = null;
//                for (TroopType troopType : TroopType.values()) {
//                    if (troopType.getValue().equals(splitInput[1])) {
//                        type = troopType;
//                    }
//                }
//                if (type != null) {
//                    int amount = Integer.parseInt(splitInput[2]);
//                    selectedTroops.get(type).addAll(attackerVillage.getRandomTroops(type, amount));
//                }
//            } catch (InvalidCommandException ignore) {
//
//            } catch (NotEnoughTroopsException e) {
//                e.printAnnouncement();
//                try {
//                    selectedTroops.get(e.getType()).addAll(attackerVillage.getRandomTroops(e.getType(), e.getAmount()));
//                } catch (NotEnoughTroopsException ignore) {
//
//                }
//            }
//        }
    }

    public void putTroop(int x, int y) throws InvalidCellException, InvalidAttackCellException {
        TroopType type = null;
        for (TroopType troopType : TroopType.values()) {
            if (troopType.getValue().equals(view.getAttackView().selectedTroop.getClass().getSimpleName())) {
                type = troopType;
                break;
            }
        }
        if (type != null) {
            int amount = 1;
            if (selectedTroops.get(type).size() >= amount) {
                if (type == TroopType.Healer || type == TroopType.Dragon) {
                    while (amount != 0) {
                        defenderMap.getCell(new Location(x, y)).addAirTroop(view.getAttackView().selectedTroop);
                        putTroops.computeIfAbsent(type, k -> new ArrayList<>());
                        putTroops.get(type).add(view.getAttackView().selectedTroop);
                        Location location = new Location(x, y);
                        view.getAttackView().selectedTroop.startAttack(location);
                        selectedTroops.get(type).remove(view.getAttackView().selectedTroop);
                        amount--;
                    }
                } else {
                    while (amount != 0) {
                        defenderMap.getCell(new Location(x, y)).addGroundTroop(view.getAttackView().selectedTroop);
                        putTroops.computeIfAbsent(type, k -> new ArrayList<>());
                        putTroops.get(type).add(view.getAttackView().selectedTroop);
                        Location location = new Location(x, y);
                        view.getAttackView().selectedTroop.startAttack(location);
                        selectedTroops.get(type).remove(view.getAttackView().selectedTroop);
                        amount--;

                    }
                }
            }
        }
    }

    public void handlePutTroops() {
        view.showMap(defenderMap.getBooleanMap());
        while (true) {
            try {
                String input = view.getAttackView().putUnits();
                if (input.equals("Go next turn")) {
                    break;
                }
                String[] splitInput = input.split(" ");
                TroopType type = null;
                for (TroopType troopType : TroopType.values()) {
                    if (troopType.getValue().equals(splitInput[1])) {
                        type = troopType;
                        break;
                    }
                }
                if (type != null) {
                    int amount = Integer.parseInt(splitInput[2]);
                    int x = Integer.parseInt(splitInput[4].split(",")[0]);
                    int y = Integer.parseInt(splitInput[4].split(",")[1]);
                    if (selectedTroops.get(type).size() >= amount) {
                        if (type == TroopType.Healer || type == TroopType.Dragon) {
                            while (amount != 0) {
                                defenderMap.getCell(new Location(x - 1, y - 1)).addAirTroop(selectedTroops.get(type).get(0));
                                putTroops.computeIfAbsent(type, k -> new ArrayList<>());
                                putTroops.get(type).add(selectedTroops.get(type).get(0));
                                Location location = new Location(x - 1, y - 1);
                                this.selectedTroops.get(type).get(0).startAttack(location);
                                selectedTroops.get(type).remove(0);
                                amount--;
                            }
                        } else {
                            while (amount != 0) {
                                defenderMap.getCell(new Location(x - 1, y - 1)).addGroundTroop(selectedTroops.get(type).get(0));
                                putTroops.computeIfAbsent(type, k -> new ArrayList<>());
                                putTroops.get(type).add(selectedTroops.get(type).get(0));
                                Location location = new Location(x - 1, y - 1);
                                this.selectedTroops.get(type).get(0).startAttack(location);
                                selectedTroops.get(type).remove(0);
                                amount--;

                            }
                        }
                    }
                }
            } catch (InvalidAttackCellException e) {
                e.printAnnouncement();
            } catch (InvalidCommandException | InvalidCellException ignore) {

            }
        }
    }

    public ArrayList<String> statusResources() {
        ArrayList<String> output = new ArrayList<String>();

        String goldAchieved = "Gold achieved: " + Integer.toString(moneyGained.getGold());
        String elixirAchieved = "Elixir achieved: " + Integer.toString(moneyGained.getElixir());

        String goldRemained = "Gold remained in map: " + Integer.toString(this.availableCost().getGold() - moneyGained.getGold());
        String elixirRemained = "Elixir remained in map: " + Integer.toString(this.availableCost().getElixir() - moneyGained.getElixir());

        output.add(goldAchieved);
        output.add(elixirAchieved);

        output.add(goldRemained);
        output.add(elixirRemained);

        return output;

    }

    public ArrayList<String> statusUnitType(TroopType troopType) {


        ArrayList<String> output = new ArrayList<String>();

        if ( troopType == null)
        {
            output.add("");
            return output;
        }

        String type = troopType.getValue();

        for (Troop troop : this.putTroops.get(troopType)) {

            if (troop.isDead())
                continue;

            String level = "level = " + Integer.toString(troop.getLevel());
            String place = "in (" + Integer.toString(troop.getLocation().getX()) + "," + Integer.toString(troop.getLocation().getY()) + ")";
            String health = "health = " + Integer.toString(troop.getHealthInWar());

            String information = type + " " + level + " " + place + " with " + health;

            output.add(information);
        }


        return output;
    }

    public ArrayList<String> statusUnits() {
        ArrayList<String> output = new ArrayList<String>();

        for (TroopType troopType : this.putTroops.keySet()) {
            output.addAll(statusUnitType(troopType));
        }

        return output;
    }

    public ArrayList<String> statusTowerType(BuildingType buildingType) {

        ArrayList<String> output = new ArrayList<String>();

        int size = Building.getBuildingNumbers().get(buildingType);

        String type = buildingType.getValue();

        for (Building building : this.defenderBuildings.get(buildingType)) {

            if (building.isDestroyed())
                continue;

            String level = "level = " + Integer.toString(building.getLevel());
            String place = "in (" + Integer.toString(building.getLocation().getX()) + "," + Integer.toString(building.getLocation().getY()) + ")";
            String health = "health = " + Integer.toString(building.getHealthInWar());

            String information = type + " " + level + " " + place + " with " + health;

            output.add(information);
        }

        return output;
    }

    public ArrayList<String> statusTower() {
        ArrayList<String> output = new ArrayList<String>();

        output.addAll(statusTowerType(BuildingType.WizardTower));
        output.addAll(statusTowerType(BuildingType.Cannon));
        output.addAll(statusTowerType(BuildingType.ArcherTower));
        output.addAll(statusTowerType(BuildingType.AirDefence));

        return output;

    }

    public ArrayList<String> statusAll() {
        ArrayList<String> output = new ArrayList<String>();

        output.addAll(statusResources());
        output.addAll(statusTower());
        output.addAll(statusUnits());

        return output;
    }

    public void attack(Stage stage) throws InvalidCellException {
        handleTroopSelection();

        view.getAttackView().setVillageScene(stage.getScene());
        try {
            view.getAttackView().configureAttackScene(stage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        handlePutTroops();
//        while (true) {
//            try {
//                String input = view.getAttackView().getAttackCommand();
//                if (input.equals("Quit attack")) {
//                    break;
//                }
//                switch (input) {
//                    case "status resources":
//                        view.showAttackStatus(statusResources());
//                        break;
//                    case "status units":
//                        view.showAttackStatus(statusUnits());
//                        break;
//                    case "status towers":
//                        view.showAttackStatus(statusTower());
//                        break;
//                    case "status all":
//                        view.showAttackStatus(statusAll());
//                        break;
//                }
//                if (input.matches("status unit .*")) {
//                    String[] splitInput = input.split(" ");
//                    TroopType type = null;
//                    for (TroopType troopType : TroopType.values()) {
//                        if (troopType.getValue().equals(splitInput[2])) {
//                            type = troopType;
//                        }
//                    }
//                    view.showAttackStatus(statusUnitType(type));
//                }
//                if (input.matches("status tower .*")) {
//                    String[] splitInput = input.split(" ");
//                    BuildingType type = null;
//                    for (BuildingType buildingType : BuildingType.values()) {
//                        if (buildingType.getValue().equals(splitInput[2])) {
//                            type = buildingType;
//                        }
//                    }
//                    view.showAttackStatus(statusTowerType(type));
//                }
//                if (input.matches("turn \\d+")) {
//                    String[] splitInput = input.split(" ");
//                    int number = Integer.parseInt(splitInput[1]);
//                    while (number != 0) {
//                        this.nextTurn();
//                        if (isAttackFinished()) {
//                            attackerVillage.addScore(scoreGained);
//                            return;
//                        }
//                        number--;
//                    }
//                }
//            } catch (InvalidCommandException ignore) {
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//
//        }
    }

    public void attackMapInfo() throws FileNotFoundException {
        ArrayList<String> output = new ArrayList<>();

        String gold = "Gold: " + Integer.toString(this.availableCost().getGold() - this.moneyGained.getGold());
        String elixir = "Elixir: " + Integer.toString(this.availableCost().getElixir() - this.moneyGained.getElixir());

        output.add(gold);
        output.add(elixir);

        int number = this.defenderBuildings.get(BuildingType.ArcherTower).size();
        if (number != 0) {
            String towerInformation = "Archer tower : " + Integer.toString(number);
            output.add(towerInformation);
        }

        number = this.defenderBuildings.get(BuildingType.WizardTower).size();
        if (number != 0) {
            String towerInformation = "Wizard tower : " + Integer.toString(number);
            output.add(towerInformation);
        }

        number = this.defenderBuildings.get(BuildingType.Cannon).size();
        if (number != 0) {
            String towerInformation = "Cannon : " + Integer.toString(number);
            output.add(towerInformation);
        }

        number = this.defenderBuildings.get(BuildingType.AirDefence).size();
        if (number != 0) {
            String towerInformation = "Air defence : " + Integer.toString(number);
            output.add(towerInformation);
        }

        view.showAttackInfo(output);
    }

    public void stopAttack()
    {
        //TODO
    }

}