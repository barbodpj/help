package model.map;

import controller.exception.gameException.InvalidCellException;
import model.building.*;
import model.troop.*;
import model.*;
import controller.exception.*;

import java.util.ArrayList;

public class Cell {

	private Building building = null;
	private ArrayList<Troop> troops = new ArrayList<>();
	private Location location;

	/**
	 * 
	 * @param building
	 */
	public void addBuilding(Building building) throws InvalidCellException {
		if(this.building != null) {
			throw new InvalidCellException();
		}
		else {
			this.building = building;
		}
	}

	/**
	 * 
	 * @param location
	 */
	public Cell(Location location) {
		this.location = location;
	}


	public Building getBuilding() {
		return building;
	}
}