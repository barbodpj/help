package model;

public class Cost {

	private int elixir;
	private int gold;

	public int getElixir() {
		return this.elixir;
	}

	public int getGold() {
		return this.gold;
	}

	/**
	 * 
	 * @param gold
	 * @param elixir
	 */
	public Cost(int gold, int elixir) {
		this.gold = gold;
		this.elixir = elixir;
	}

}