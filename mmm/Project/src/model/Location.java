package model;

import java.io.Serializable;

public class Location implements Serializable {

	private int x;
	private int y;

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public Location(int x, int y) {
		this.x = x;
		this.y= y;

	}

}