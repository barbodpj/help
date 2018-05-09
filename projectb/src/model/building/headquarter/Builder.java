package model.building.headquarter;

import constants.enums.BuildingType;
import model.Village;
import model.building.*;

public class Builder {

	private Building workPlace = null;
	private boolean busy = false;
	private Integer remainingTime = null;
	private Village village;

	public boolean isBusy() {
		return this.busy;
	}

	/**
	 * 
	 * @param building
	 */
	public void build(Building building) {
		workPlace = building;
		busy = true;
		remainingTime = building.getBuildTime();
		building.setBuilder(this);
		((TownHall)village.getBuilding(BuildingType.TownHall, 1)).addBuilding(building);
	}

	public void decreaseRemainingTime() {
		if(remainingTime != null) {
			if(remainingTime > 0) {
				remainingTime --;
			}
			if(remainingTime == 0) {
				workPlace.setBuilder(null);
				((TownHall)village.getBuilding(BuildingType.TownHall, 1)).removeBuilding(workPlace);
				village.addBuilding(workPlace);
				workPlace = null;
				busy = false;
				remainingTime = null;
			}
		}
	}

	public Building getWorkPlace() {
		return this.workPlace;
	}

	public Integer getRemainingTime() {
		return remainingTime;
	}

	public Builder(Village village) {
		this.village = village;
	}
}