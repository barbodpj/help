package model.map;

import controller.exception.gameException.InvalidCellException;
import model.*;
import model.building.Building;

public class Map {

	private Cell[][] map;
	public static int WIDTH = 30;
	public static int HEIGHT = 30;

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
		if(location.getX() >= 1 && location.getX() <= WIDTH && location.getY() >= 1 && location.getY() <= 30) {
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
				map[i][j] = new Cell(location);
			}
		}
	}
}