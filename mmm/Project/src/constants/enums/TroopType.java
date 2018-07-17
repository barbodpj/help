package constants.enums;

public enum TroopType {
	Guardian("Guardian"),
	Giant("Giant"),
	Dragon("Dragon"),
	Archer("Archer"),
	WallBreaker("WallBreaker"),
	Healer("Healer");

	private String value;

	/**
	 *
	 * @param value
	 */
	private TroopType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}


}