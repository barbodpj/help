package constants.enums;

public enum BuildingType {
	GoldMine("Gold mine"),
	ElixirMine("Elixir mine"),
	GoldStorage("Gold storage"),
	ElixirStorage("Elixir storage"),
	TownHall("Town hall"),
	Barracks("Barracks"),
	Camp("Camp"),
	ArcherTower("Archer tower"),
	Cannon("Cannon"),
	AirDefence("Air defence"),
	WizardTower("Wizard tower"),
	Wall("Wall"),
	Trap("Trap"),
	GiantsCastle("Giants castle");

	private String value;

	/**
	 *
	 * @param value
	 */
	private BuildingType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}