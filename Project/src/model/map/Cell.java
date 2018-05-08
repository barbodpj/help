package model.map;

import model.building.*;
import model.troop.*;
import model.*;

import java.util.ArrayList;

public class Cell {

	private Building building;
	private ArrayList<Troop> troops;
	private Location location;

	/**
	 * 
	 * @param buiding
	 */
	public void addBuilding(Building buiding) {
		// TODO - implement Cell.addBuilding
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param location
	 */
	public Cell(Location location) {
		// TODO - implement Cell.Cell
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param buiding
	 */
	public Building getBuilding() {
		return building;
	}
}