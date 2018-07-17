package model.building.resource;

import constants.enums.BuildingType;
import model.Location;
import model.Village;
import model.building.*;

import java.io.Serializable;

public abstract class Mine extends Building implements Serializable {

	protected int resource;




	protected static final int MAX = 200;

	public Mine (Location location, Village village)
	{
		super(location, village);
	}

	public abstract void produce();


	public abstract void moveToStorage();



}