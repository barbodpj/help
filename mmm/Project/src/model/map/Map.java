package model.map;

import constants.enums.BuildingType;
import controller.exception.gameException.InvalidCellException;
import model.*;
import model.building.Building;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Map implements Serializable {

	private Cell[][] map;
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	private ArrayList<String> attackedMapPaths = new ArrayList<>();
	private HashMap<BuildingType, ArrayList<Building>> mapBuilding = new HashMap<>();
	{
		for (BuildingType buildingType: BuildingType.values()) {
			mapBuilding.put(buildingType, new ArrayList<>());
		}
	}

	public String getBooleanMap() {
		String booleanMap = "";
		for(int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				if(map[j][i].getBuilding() == null) {
					booleanMap = booleanMap + "0 ";
				}
				else {
					booleanMap = booleanMap + "1 ";
				}
			}
			if(i != HEIGHT - 1) {
				booleanMap = booleanMap + "\n";
			}
		}
		return booleanMap;
	}

	/**
	 * 
	 * @param location
	 */
	public Cell getCell(Location location) throws InvalidCellException {

		if ( location == null )
			throw new InvalidCellException();

		if(location.getX() >= 0 && location.getX() <= WIDTH - 1 && location.getY() >= 0 && location.getY() <= HEIGHT - 1) {
			return map[location.getX()][location.getY()];
		}
		else {
			throw new InvalidCellException();
		}
	}

	public Map() {
		map = new Cell[WIDTH][HEIGHT];
		for (int j = 0; j < HEIGHT; j++) {
			for (int i = 0; i < WIDTH; i++) {
				Location location = new Location(i, j);
				map[i][j] = new Cell(location, this);
			}
		}
	}

	public void addMap(String path) {
		attackedMapPaths.add(path);
	}

	public void addMapBuildings(Building building) {
		mapBuilding.get(BuildingType.valueOf(building.getClass().getSimpleName())).add(building);
	}

	public HashMap<BuildingType, ArrayList<Building>> getMapBuilding() {
		return mapBuilding;
	}

	public ArrayList<String> getAttackedMaps() {
		ArrayList<String> names = new ArrayList<>();
		for(int i = 0; i < attackedMapPaths.size(); i++) {
			String name = attackedMapPaths.get(i).split("/")[attackedMapPaths.get(i).split("/").length - 1];
			name = name.split("\\.")[0];
			names.add(name);
		}
		return names;
	}

	public String getAttackedMapPaths(int number) {
		return attackedMapPaths.get(number - 1);
	}
}