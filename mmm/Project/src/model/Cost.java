package model;

import java.io.Serializable;

public class Cost implements Serializable {

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