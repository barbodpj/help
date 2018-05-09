package model.building;

import constants.Constants;
import controller.exception.gameException.LevelBoundaryException;
import model.building.resource.ElixirStorage;
import model.building.resource.GoldStorage;
import model.interfaces.*;
import constants.enums.*;
import model.*;
import model.building.headquarter.*;

import java.util.HashMap;

public abstract class Building implements Constructible {
	//TODO overriding upgrade method in cannon and ...
	//TODO edit constructor
	//TODO getHealth

	protected Village village;

	protected int health;

	protected int attackingHealth; // we use this only in attack mode

	protected boolean isDestoryed; //we use this only in attack mode

	protected int level = 0;

	public boolean isDestoryed() {
		return isDestoryed;
	}

	protected static HashMap<BuildingType, Integer> buildingNumbers = new HashMap<>();

	protected int number;

	protected int remainingUpgradeTime = 0;

	protected Location location;

	protected Builder builder;

	static  {
		for(BuildingType type: BuildingType.values()) {
			buildingNumbers.put(type, 0);
		}
	}

	public int getNumber() {
		return number;
	}

	public String ShowOverallInfo() {

		String level = "Level: " + Integer.toString(this.level);
		String health = "Health: " + Integer.toString(this.health);

		String output = level + "\n" + health ;
		return output;

	}

	public String ShowUpgradeInfo() {
		String output = "Upgrade Cost: " + this.getUpgradeCost() + " gold";
		return output;
	}

	public int getAttackScoreGained() {
		return Constants.attackScoreGained.get(BuildingType.valueOf(this.getClass().toString()));
	}

	public int getUpgradeTime() {
		return Constants.buildingsUpgradeTime.get(BuildingType.valueOf(this.getClass().toString()));
	}

	public void upgrade() throws LevelBoundaryException {
		if (this.level == village.getBuilding(BuildingType.TownHall , 1).level)
			throw new LevelBoundaryException();
		else
		{
			village.addToUpgradeQueue(this);
			this.remainingUpgradeTime = this.getUpgradeTime();
		}
	}

	public void decreaseRemainingUpgradeTime() {
		this.remainingUpgradeTime--;
		if (this.remainingUpgradeTime == 0)
			village.removeFromUpgradeQueue(this);
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
		number = buildingNumbers.get(BuildingType.valueOf(this.getClass().toString())) + ((TownHall)village.getBuilding(BuildingType.TownHall, 1)).getBuildingQueue().get(BuildingType.valueOf(this.getClass().toString())).size() + 1;

	}

	@Override
	public Cost getBuildCost() {
		return Constants.buildingBuildCost.get(BuildingType.valueOf(this.getClass().toString()));
	}

	@Override
	public int getBuildTime() {
		return Constants.buildingBuildTime.get(BuildingType.valueOf(this.getClass().toString()));
	}

	@Override
	public Cost getUpgradeCost() {
		return Constants.buildingsUpgradeCost.get(BuildingType.valueOf(this.getClass().toString()));
	}

	public static void incrementBuildingNumber(BuildingType buildingType) {
		buildingNumbers.replace(buildingType, buildingNumbers.get(buildingType) + 1);
	}

	public void startAttack()
	{
		this.attackingHealth = this.health;
		this.isDestoryed = false;
	}

	protected void destroy()
	{
		this.isDestoryed = true;
	}

	public Cost decreaseHealth(int amount)
	{

		Cost cost = null;

		if (this.attackingHealth > amount)
			this.attackingHealth -= amount;
		else {
			this.destroy();
			cost = this.getBuildCost();
			if (BuildingType.valueOf(this.getClass().toString()) == BuildingType.GoldStorage) {
				int gold = this.getBuildCost().getGold() + ((GoldStorage) this).getResource();
				int elixir = this.getBuildCost().getElixir();
				cost = new Cost(gold, elixir);
			}

			if (BuildingType.valueOf(this.getClass().toString()) == BuildingType.ElixirStorage) {
				int gold = this.getBuildCost().getGold();
				int elixir = this.getBuildCost().getElixir() + ((ElixirStorage) this).getResource();
				cost = new Cost(gold, elixir);
			}
		}

		return cost;
	}

	public void nextMoveInAttack()//TODO
	{
		if (this.isDestoryed )
			return;

	}

	public static HashMap<BuildingType, Integer> getBuildingNumbers() {
		return buildingNumbers;
	}

	public int getLevel() {
		return level;
	}

	public int getHealth() {
		return health;
	}

	@Override
	public int getInitialHealth() {
		return Constants.buildingInitialHealth.get(BuildingType.valueOf(this.getClass().toString()));
	}
}