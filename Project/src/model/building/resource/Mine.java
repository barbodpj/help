package model.building.resource;

import constants.enums.BuildingType;
import model.Village;
import model.building.*;

public abstract class Mine extends Building {

	protected int resource;

	public static final int initiaRate = 10;

	public static final double increaseRate = 0.6;


	protected static final int MAX = 200;

	public Mine ()
	{
		this.village = village;
	}

	public abstract void produce();


	public abstract void moveToStorage();

	public int getProductionRate() {

		int rate=initiaRate;

		for (int i=1 ; i<=level ; i++)
		{
			rate*=increaseRate;
		}

		return rate;


	}

}