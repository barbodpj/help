package model;

import comparator.BuildingTypeComparator;
import constants.enums.BuildingType;
import model.building.*;
import model.building.resource.ElixirStorage;
import model.building.resource.GoldStorage;
import model.building.troopPreparation.Camp;
import model.map.*;
import controller.exception.gameException.LowResourcesException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class Village {

	private HashMap<BuildingType, ArrayList<Building>> buildings = new HashMap<BuildingType,  ArrayList<Building>>();
	private Map map = new Map();
	private int score = 0;
	private ArrayList<Building> upgradeQueue = new ArrayList<>();

	public ArrayList<String> getBuildings() {
		ArrayList<String> building = new ArrayList<>();
		ArrayList<BuildingType> types = new ArrayList<>(Arrays.asList(BuildingType.values()));
		Collections.sort(types, new BuildingTypeComparator());
		int number = 1;
		for (int i = 0; i < types.size(); i++) {
			for (int j = 0; j < buildings.get(types.get(i)).size(); j++) {
				building.add(number + ". " + types.get(i).getValue() + " " + buildings.get(types.get(i)).get(j).getNumber());
				number++;
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
		return buildings.get(buildingType).get(number);

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
		buildings.get(BuildingType.valueOf(building.getClass().toString())).add(building);
		Building.incrementBuildingNumber(BuildingType.valueOf(building.getClass().toString()));
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

	}

	public void addToUpgradeQueue(Building building) {
		upgradeQueue.add(building);
	}

	public void removeFromUpgradeQueue(Building building) {
		upgradeQueue.remove(building);
	}

	public Map getMap() {
		return map;
	}
}