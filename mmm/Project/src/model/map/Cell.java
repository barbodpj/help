package model.map;

import constants.enums.TroopType;
import controller.exception.gameException.InvalidAttackCellException;
import controller.exception.gameException.InvalidCellException;
import model.building.*;
import model.troop.*;
import model.*;

import java.io.Serializable;
import java.util.ArrayList;

public class Cell implements Serializable {

	private Building building = null;

	private ArrayList<Troop> groundTroops = new ArrayList<>();

	private ArrayList<Troop> airTroops = new ArrayList<>();

	private Location location;

	private Map map;




	/**
	 * 
	 * @param building
	 */
	public void addBuilding(Building building) throws InvalidCellException {
		if(this.building != null) {
			throw new InvalidCellException();
		}
		else {
			if(location.getX() == 0 || location.getY() == 0 || location.getX() == Map.WIDTH - 1 || location.getY() == Map.HEIGHT - 1) {
				throw new InvalidCellException();
			}
			else {
				this.building = building;
			}
		}
	}

	/**
	 * 
	 * @param location
	 */
	public Cell(Location location , Map map) {

		this.map = map;
		this.location = location;
	}


	public Building getBuilding() {
		return building;
	}

	public void addGroundTroop(Troop troop) throws InvalidAttackCellException
	{
		if (this.location.getX() != 0 && this.location.getY() != 0 && this.location.getX() != Map.WIDTH - 1 && this.location.getY() != Map.WIDTH - 1 )
			throw new InvalidAttackCellException();

		if ( this.groundTroops.size() == 0) {

			this.groundTroops.add(troop);
			return;
		}else if ( this.groundTroops.size() < 5 && TroopType.valueOf(this.groundTroops.get(0).getClass().getSimpleName()) ==
				TroopType.valueOf( troop.getClass().getSimpleName()) ) {

			this.groundTroops.add(troop);
			return;
		}

		throw new InvalidAttackCellException();

	}

	public void addAirTroop( Troop troop) throws InvalidAttackCellException
	{
		if (this.location.getX() != 0 || this.location.getY() != 0 || this.location.getX() != Map.WIDTH - 1 || this.location.getY() != Map.HEIGHT - 1 )
			throw new InvalidAttackCellException();

		this.airTroops.add( troop );

	}

	public ArrayList<Troop> getGroundTroops()
	{
		return this.groundTroops;
	}

	public ArrayList<Troop> getAirTroops()
	{
		return this.airTroops;
	}

	public void moveTroopFrom(Troop troop , int destinationX , int destinationY) throws InvalidCellException {

		Location location = new Location(  destinationX , destinationY);
		if (troop.canFly())
		{
			map.getCell(location).airTroops.add(troop);
			this.airTroops.remove(troop);
		}else
		{
			map.getCell(location).groundTroops.add(troop);
			this.groundTroops.remove(troop);
		}
	}

	public void moveTroop(Troop troop, Location destination) throws InvalidCellException {
		if(troop.canFly()){
			this.airTroops.remove(troop);
			map.getCell(destination).airTroops.add(troop);
		}else
		{
			this.groundTroops.remove(troop);
			map.getCell(destination).groundTroops.add(troop);
		}
	}

}