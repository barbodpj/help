package model.building;

import constants.Constants;
import controller.exception.UnsupportedOperationException;
import controller.exception.gameException.InvalidCellException;
import controller.exception.gameException.LevelBoundaryException;
import model.building.defence.DefenceBuilding;
import model.building.resource.ElixirStorage;
import model.building.resource.GoldStorage;
import model.interfaces.*;
import constants.enums.*;
import model.*;
import model.building.headquarter.*;

import java.io.Serializable;
import java.util.HashMap;

public abstract class Building implements Constructible, Serializable {
	//TODO overriding upgrade method in cannon and ...
	//TODO edit constructor
	//TODO getHealth
	//TODO unsupportedOperation

	protected Village village;



	private int healthInWar; // we use this only in attack mode

	private boolean isDestroyed; //we use this only in attack mode

	protected int level = 0;


	protected static HashMap<BuildingType, Integer> buildingNumbers = new HashMap<>();

	private int number;

	private int remainingUpgradeTime = 0;

	protected Location location;

	private Builder builder;

	private static final int DEFENCE_BUILDING_HEALTH_INCREASE_RATE = 10;
	private static final int RESOURCES_BUILDING_HEALTH_INCREASE_RATE = 0;
	private static final int TOWNHALL_HEALTH_INCREASE_RATE = 500;

	static  {
		for(BuildingType type: BuildingType.values()) {
			buildingNumbers.put(type, 0);
		}
	}

	public static void changeNumbers() {
		for(BuildingType type: BuildingType.values()) {
			buildingNumbers.put(type, 0);
		}
	}

	public void undoChangeNumbers() {
		for(BuildingType type: BuildingType.values()) {
			buildingNumbers.put(type, village.getB().get(type).size());
		}
	}

	public int getHealthInWar() {
		return healthInWar;
	}


	public boolean isDestroyed() {
		return isDestroyed;
	}

	private int getHealth()
	{
		if ( this.isDefending())
			return this.getInitialHealth() +  this.level * DEFENCE_BUILDING_HEALTH_INCREASE_RATE ;

		if ( BuildingType.valueOf( this.getClass().getSimpleName()) == BuildingType.TownHall )
			return this.getInitialHealth() + this.level * TOWNHALL_HEALTH_INCREASE_RATE;

		return this.getInitialHealth() + this.level * RESOURCES_BUILDING_HEALTH_INCREASE_RATE;
	}

	public int getNumber() {
		return number;
	}

	public String ShowOverallInfo() {

		String level = "Level: " + Integer.toString(this.level);
		String health = "Health: " + Integer.toString(this.getHealth());

		String output = level + "\n" + health ;
		return output;

	}

	public String ShowUpgradeInfo() {
		String output = "Upgrade Cost: " + this.getUpgradeCost().getGold() + " gold";
		return output;
	}

	public int getAttackScoreGained() {
		return Constants.attackScoreGained.get(BuildingType.valueOf(this.getClass().getSimpleName()));
	}

	private int getUpgradeTime() {
		return Constants.buildingsUpgradeTime.get(BuildingType.valueOf(this.getClass().getSimpleName()));
	}

	public void upgrade() throws LevelBoundaryException, UnsupportedOperationException {
		if ( ( this.getClass().getSimpleName().equals("Barracks")  || this.isDefending() ) && this.level == village.getBuilding(BuildingType.TownHall , 1).level)
			throw new LevelBoundaryException();
		else
		{
			village.addToUpgradeQueue(this);
			this.remainingUpgradeTime = this.getUpgradeTime();
		}
	}

	public int decreaseRemainingUpgradeTime() {
		this.remainingUpgradeTime--;
		return this.remainingUpgradeTime;

	}

	public Location getLocation() {
		return this.location;
	}

	/**
	 *
	 * @param builder
	 */
	public void setBuilder(Builder builder) {
		this.builder = builder;
	}

	public Builder getBuilder() {
		return this.builder;
	}

	/**
	 *
	 * @param location
	 */
	public Building(Location location , Village village ) {
		this.location = location;
		this.village = village;
		if(village != null) {
            if(BuildingType.valueOf(this.getClass().getSimpleName()) == BuildingType.TownHall) {
                number = 1;
            }
            else {
                number = buildingNumbers.get(BuildingType.valueOf(this.getClass().getSimpleName())) + ((TownHall)village.getBuilding(BuildingType.TownHall, 1)).getBuildingQueue().get(BuildingType.valueOf(this.getClass().getSimpleName())).size() + 1;
            }
        }
	}

	@Override
	public Cost getBuildCost() {
		return Constants.buildingBuildCost.get(BuildingType.valueOf(this.getClass().getSimpleName()));
	}

	@Override
	public int getBuildTime() {
		return Constants.buildingBuildTime.get(BuildingType.valueOf(this.getClass().getSimpleName()));
	}

	@Override
	public Cost getUpgradeCost() {
		return Constants.buildingsUpgradeCost.get(BuildingType.valueOf(this.getClass().getSimpleName()));
	}

	public static void incrementBuildingNumber(BuildingType buildingType) {
		buildingNumbers.replace(buildingType, buildingNumbers.get(buildingType) + 1);
	}



	protected void destroy()
	{
		this.isDestroyed = true;
	}

	public Building decreaseHealth(int amount)
	{

		Building building = null;

		if (this.healthInWar > amount)
			this.healthInWar -= amount;

		else {
			this.destroy();
			building = this;
		}

		return building;
	}



	public static HashMap<BuildingType, Integer> getBuildingNumbers() {
		return buildingNumbers;
	}

	public int getLevel() {
		return level;
	}


	@Override
	public int getInitialHealth() {
		return Constants.buildingInitialHealth.get(BuildingType.valueOf(this.getClass().getSimpleName()));
	}

	public boolean isDefending()
	{
		BuildingType buildingType = BuildingType.valueOf(this.getClass().getSimpleName());

		if ( buildingType ==  BuildingType.ArcherTower || buildingType == BuildingType.AirDefence || buildingType == BuildingType.Cannon
				|| buildingType == BuildingType.WizardTower) {
			return true;
		}else
		{
			return false;
		}
	}


	public void startAttack()
	{
		this.healthInWar = this.getHealth();
		this.isDestroyed = false;
	}

	public void incrementLevel() {
		level ++;
	}

}