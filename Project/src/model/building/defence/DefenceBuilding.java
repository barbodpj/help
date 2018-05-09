package model.building.defence;

import constants.Constants;
import model.Location;
import model.Village;
import model.building.*;
import model.interfaces.*;
import constants.enums.*;
import model.troop.*;

//TODO edit chooseTarget
public abstract class DefenceBuilding extends Building implements Attacker {

	protected TroopType target;
	protected Troop currentTarget;
	protected int currentTargetsDamage;

	public String getAttackInfo() {
		return "Target: " + TroopType.valueOf(currentTarget.toString()).getValue() + "\n" + "Damage: " + currentTargetsDamage + "\n" + "Damage Range: " + getAttackRange();
	}

	/**
	 * 
	 * @param type
	 */
	public void chooseTarget(TroopType type) {
		target = type;
	}

	@Override
	public int getAttackRange() {
		return Constants.troopsAttackRange.get(TroopType.valueOf(this.getClass().toString()));
	}

	@Override
	public int getDamage() {

	}

	public DefenceBuilding(Location location, Village village) {
		super(location, village);
	}
}