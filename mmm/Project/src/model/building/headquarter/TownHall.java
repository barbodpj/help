package model.building.headquarter;

import comparator.BuildingTypeComparator;
import constants.Constants;
import constants.enums.BuildingType;
import controller.exception.UnsupportedOperationException;
import model.Location;
import model.Village;
import model.building.*;
import controller.exception.gameException.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class TownHall extends Building implements Serializable {

	private ArrayList<Builder> builders = new ArrayList<>();
	private HashMap<BuildingType, ArrayList<Building>> buildingQueue = new HashMap<>();
	private Village village;



	public ArrayList<String> getAvailableBuildings() {
		ArrayList<String> availableBuildings = new ArrayList<>();
		ArrayList<BuildingType> types = new ArrayList<>(Arrays.asList(BuildingType.values()));
		Collections.sort(types, new BuildingTypeComparator());
		for (int i = 0; i < types.size(); i++) {
			if(types.get(i) != BuildingType.TownHall) {
				if (Constants.buildingBuildCost.get(types.get(i)).getGold() <= village.getAvailableGold() && Constants.buildingBuildCost.get(types.get(i)).getElixir() <= village.getAvailableElixir()) {
					availableBuildings.add(types.get(i).getValue());
				}
			}
		}
		return availableBuildings;
	}

	public Builder callBuilder() throws BusyBuildersException {
		for (int i = 0; i < builders.size(); i++) {
			if(!builders.get(i).isBusy()) {
				return builders.get(i);
			}
		}
		throw new BusyBuildersException();
	}

	public String showStatus() {
		String status = "";
		ArrayList<BuildingType> types = new ArrayList<>(Arrays.asList(BuildingType.values()));
		Collections.sort(types, new BuildingTypeComparator());
		for (int i = 0; i < types.size(); i++) {
			for (int j = 0; j < buildingQueue.get(types.get(i)).size(); j++) {
				status = status + types.get(i).getValue() + " " + buildingQueue.get(types.get(i)).get(j).getBuilder().getRemainingTime() + "\n";
			}
		}
		if(!status.equals("")) {
            status = status.substring(0, status.length() - 1);
        }
		return status;
	}

	public TownHall(Location location, Village village) {
		super(location, village);
		this.village = village;
		Builder builder = new Builder(village);
		builders.add(builder);
		for(BuildingType buildingType: BuildingType.values()) {
			buildingQueue.put(buildingType, new ArrayList<>());
		}
	}

	void addBuilding(Building building) {
		buildingQueue.get(BuildingType.valueOf(building.getClass().getSimpleName())).add(building);
	}

	void removeBuilding(Building building) {
		buildingQueue.get(BuildingType.valueOf(building.getClass().getSimpleName())).remove(building);
	}

	public HashMap<BuildingType, ArrayList<Building>> getBuildingQueue() {
		return buildingQueue;
	}

	public void updateBuilders() {
		for(Builder builder: builders) {
			builder.decreaseRemainingTime();
		}
	}

	public void addBuilder() {
		Builder builder = new Builder(village);
		builders.add(builder);
	}
}