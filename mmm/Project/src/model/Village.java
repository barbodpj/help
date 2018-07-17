package model;

import comparator.BuildingTypeComparator;
import constants.enums.BuildingType;
import constants.enums.TroopType;
import controller.exception.gameException.InvalidCellException;
import controller.exception.gameException.NotEnoughTroopsException;
import model.building.*;
import model.building.defence.GiantsCastle;
import model.building.headquarter.TownHall;
import model.building.resource.ElixirMine;
import model.building.resource.ElixirStorage;
import model.building.resource.GoldMine;
import model.building.resource.GoldStorage;
import model.building.troopPreparation.Camp;
import model.map.*;
import controller.exception.gameException.LowResourcesException;
import model.map.Map;
import model.troop.Troop;

import java.io.Serializable;
import java.util.*;

public class Village implements Serializable {


	private HashMap<BuildingType, ArrayList<Building>> buildings = new HashMap<BuildingType,  ArrayList<Building>>();
	private Map map = new Map();
	private int score = 0;
	private ArrayList<Building> upgradeQueue = new ArrayList<>();
	public static final int INITIAL_GOLD = 300;
	public static final int INITIAL_ELIXIR = 60;

	public HashMap<BuildingType, ArrayList<Building>> getB() {
		return buildings;
	}

	public void updateUpgradeQueue() {
	    Iterator<Building> iterator = upgradeQueue.iterator();
	    while (iterator.hasNext()) {
	        Building building = iterator.next();
	        if(building.decreaseRemainingUpgradeTime() == 0) {
	            if(building.getClass().getSimpleName().equals("TownHall")) {
                    ((TownHall)building).addBuilder();
                }
                building.incrementLevel();
                iterator.remove();
            }
        }
	}

	public int getScore() {
		return score;
	}

	public void addScore(int number) {
		score += number;
	}

	public ArrayList<String> getBuildings() {
		ArrayList<String> building = new ArrayList<>();
		ArrayList<BuildingType> types = new ArrayList<>(Arrays.asList(BuildingType.values()));
		Collections.sort(types, new BuildingTypeComparator());
		for (int i = 0; i < types.size(); i++) {
			if(types.get(i) != BuildingType.GiantsCastle && types.get(i) != BuildingType.Wall && types.get(i) != BuildingType.Trap) {
				for (int j = 0; j < buildings.get(types.get(i)).size(); j++) {
					building.add( types.get(i).getValue() + " " + buildings.get(types.get(i)).get(j).getNumber());
				}
			}
		}
		return building;
	}

	public String showResources() {
		return "Gold: " + getAvailableGold() + "\nElixir: " + getAvailableElixir() + "\nScore: " + score;
	}

	/**
	 * 
	 * @param buildingType
	 * @param number
	 */

	public Building getBuilding(BuildingType buildingType, int number) {
		return buildings.get(buildingType).get(number - 1);
	}

	/**
	 * 
	 * @param gold
	 * @param elixir
	 */
	public void useResources(int gold, int elixir) throws LowResourcesException {
		if(getAvailableGold() < gold || getAvailableElixir() < elixir) {
			throw new LowResourcesException();
		}
		else {
			for (int i = 0; i < buildings.get(BuildingType.GoldStorage).size() && gold != 0; i++) {
				gold = ((GoldStorage) buildings.get(BuildingType.GoldStorage).get(i)).use(gold);
			}

			for (int i = 0; i < buildings.get(BuildingType.ElixirStorage).size() && elixir != 0; i++) {
				elixir = ((ElixirStorage) buildings.get(BuildingType.ElixirStorage).get(i)).use(elixir);
			}
		}
	}

	/**
	 * 
	 * @param building
	 */
	public void addBuilding(Building building) {
		buildings.get(BuildingType.valueOf(building.getClass().getSimpleName())).add(building);
		Building.incrementBuildingNumber(BuildingType.valueOf(building.getClass().getSimpleName()));
	}

	public int getAvailableGold() {
		int availableGold = 0;
		for (int i = 0; i < buildings.get(BuildingType.GoldStorage).size(); i++) {
			availableGold += ((GoldStorage) buildings.get(BuildingType.GoldStorage).get(i)).getResource();
		}
		return availableGold;
	}

	public int getAvailableElixir() {
		int availableElixir = 0;
		for (int i = 0; i < buildings.get(BuildingType.ElixirStorage).size(); i++) {
			availableElixir += ((ElixirStorage) buildings.get(BuildingType.ElixirStorage).get(i)).getResource();
		}
		return availableElixir;
	}

	public int getNumberOfTroops() {
		int troopNumber = 0;
		for (int i = 0; i < buildings.get(BuildingType.Camp).size(); i++) {
			troopNumber +=((Camp) buildings.get(BuildingType.Camp).get(i)).getTroopNumber();
		}
		return troopNumber;
	}

	public Village() {
		for(BuildingType buildingType: BuildingType.values()) {
			buildings.put(buildingType, new ArrayList<>());
		}
		Location location = new Location(Map.WIDTH / 2, Map.HEIGHT / 2);
		Location location4 = new Location(Map.WIDTH / 2 - 1, Map.HEIGHT / 2);
		Location location5 = new Location(Map.WIDTH / 2 - 1, Map.HEIGHT / 2 - 1);
		Location location3 = new Location(Map.WIDTH / 2, Map.HEIGHT / 2 - 1);
		TownHall townHall = new TownHall(location, this);
		addBuilding(townHall);
		try {
			map.getCell(location).addBuilding(townHall);
			map.getCell(location4).addBuilding(townHall);
			map.getCell(location5).addBuilding(townHall);
			map.getCell(location3).addBuilding(townHall);
		}
		catch (InvalidCellException ignore) {

		}
		Location location1 = new Location(Map.WIDTH / 3, Map.HEIGHT / 3);
		ElixirStorage elixirStorage = new ElixirStorage(location1, this, INITIAL_ELIXIR);
		addBuilding(elixirStorage);
		try {
			map.getCell(location1).addBuilding(elixirStorage);
		}
		catch (InvalidCellException ignore) {

		}
		Location location2 = new Location(Map.WIDTH * 2 / 3, Map.HEIGHT * 2 / 3);
		GoldStorage goldStorage = new GoldStorage(location2, this, INITIAL_GOLD);
		addBuilding(goldStorage);
		try {
			map.getCell(location2).addBuilding(goldStorage);
		}
		catch (InvalidCellException ignore) {

		}
	}

	public int getGoldStorageCapacity() {
		int overallCapacity = 0;
		for (int i = 0; i < buildings.get(BuildingType.GoldStorage).size(); i++) {
			overallCapacity += ((GoldStorage) buildings.get(BuildingType.GoldStorage).get(i)).getCapacity();
		}
		return overallCapacity;
	}

	public int getElixirStorageCapacity() {
		int overallCapacity = 0;
		for (int i = 0; i < buildings.get(BuildingType.ElixirStorage).size(); i++) {
			overallCapacity += ((ElixirStorage) buildings.get(BuildingType.ElixirStorage).get(i)).getCapacity();
		}
		return overallCapacity;
	}

	public void addToUpgradeQueue(Building building) {
		upgradeQueue.add(building);
	}

	public Map getMap() {
		return map;
	}

    public int addGold(int goldAmount) {
        for (Building building : buildings.get(BuildingType.GoldStorage)) {
            goldAmount = ((GoldStorage)building).addResources(goldAmount);
        }
        return goldAmount;
    }

    public int addElixir ( int elixirAmount )
    {
        for (Building building : buildings.get(BuildingType.ElixirStorage)) {
            elixirAmount = ((ElixirStorage)building).addResources(elixirAmount);
        }
        return elixirAmount;
    }

    public int getAvailableTroops(TroopType type) {
		int availableTroops = 0;
		for(Building camp: buildings.get(BuildingType.Camp)) {
			availableTroops += ((Camp) camp).getTroops(type).size();
		}
		return availableTroops;
	}


    public ArrayList<Troop> getRandomTroops(TroopType type, int amount) throws NotEnoughTroopsException {
	    int availableTroops = 0;
        for(Building camp: buildings.get(BuildingType.Camp)) {
            availableTroops += ((Camp) camp).getTroops(type).size();
        }
        if(availableTroops < amount) {
            throw new NotEnoughTroopsException(type, availableTroops);
        }
        ArrayList<Troop> warTroops = new ArrayList<>();
        if(buildings.get(BuildingType.Camp).size() == 0) {
        	return warTroops;
		}
        Random random = new Random();
        int randomNumber = random.nextInt(buildings.get(BuildingType.Camp).size());
        outer1:
        for (int i = randomNumber; i < buildings.get(BuildingType.Camp).size(); i++) {
            ArrayList<Troop> campTroops = ((Camp)buildings.get(BuildingType.Camp).get(i)).getTroops(type);
            for(Troop troop: campTroops) {
                if(warTroops.size() < amount) {
                    warTroops.add(troop);
                }
                else {
                    break outer1;
                }
            }
        }
        outer2:
        for (int i = 0; i < randomNumber; i++) {
            ArrayList<Troop> campTroops = ((Camp)buildings.get(BuildingType.Camp).get(i)).getTroops(type);
            for(Troop troop: campTroops) {
                if(warTroops.size() < amount) {
                    warTroops.add(troop);
                }
                else {
                    break outer2;
                }
            }
        }
        return warTroops;
    }

    public void produceResources() {
		for (int i = 0; i < buildings.get(BuildingType.GoldMine).size(); i++) {
			((GoldMine)buildings.get(BuildingType.GoldMine).get(i)).produce();
		}
		for (int i = 0; i < buildings.get(BuildingType.ElixirMine).size(); i++) {
			((ElixirMine)buildings.get(BuildingType.ElixirMine).get(i)).produce();
		}
	}
}