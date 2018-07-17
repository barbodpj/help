package constants.enums;

import model.building.defence.*;
import model.building.headquarter.TownHall;
import model.building.resource.ElixirMine;
import model.building.resource.ElixirStorage;
import model.building.resource.GoldMine;
import model.building.resource.GoldStorage;
import model.building.troopPreparation.Barracks;
import model.building.troopPreparation.Camp;
import model.troop.army.Giant;

import java.util.HashMap;

public enum BuildingType {
	GoldMine("Gold mine", model.building.resource.GoldMine.class.getName()),
	ElixirMine("Elixir mine", model.building.resource.ElixirMine.class.getName()),
	GoldStorage("Gold storage", model.building.resource.GoldStorage.class.getName()),
	ElixirStorage("Elixir storage", model.building.resource.ElixirStorage.class.getName()),
	TownHall("Town hall", model.building.headquarter.TownHall.class.getName()),
	Barracks("Barracks", model.building.troopPreparation.Barracks.class.getName()),
	Camp("Camp", model.building.troopPreparation.Camp.class.getName()),
	ArcherTower("Archer tower", model.building.defence.ArcherTower.class.getName()),
	Cannon("Cannon", model.building.defence.Cannon.class.getName()),
	AirDefence("Air defence", model.building.defence.AirDefence.class.getName()),
	WizardTower("Wizard tower", model.building.defence.WizardTower.class.getName()),
	Wall("Wall", model.building.defence.Wall.class.getName()),
	Trap("Trap", model.building.defence.Trap.class.getName()),
	GiantsCastle("Giants castle", model.building.defence.GiantsCastle.class.getName());


	private String fullName;
	private String value;

	/**
	 *
	 * @param value
	 */
	BuildingType(String value, String fullName) {
		this.value = value;
		this.fullName = fullName;
	}

	public String getValue() {
		return value;
	}

	public String getFullName() {
		return fullName;
	}
}