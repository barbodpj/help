package model.building.resource;

import model.Location;
import model.Village;
import model.building.*;

import java.io.Serializable;

public abstract class Storage extends Building implements Serializable {

	protected int resource;

	public int getResource() {
		return this.resource;
	}

	public abstract int getCapacity();


	public int use(int quantity) { //returns the remaining
		int spend = Integer.min(quantity , this.resource);
		this.resource -= spend;
		return quantity - spend;

	}



	public Storage(Location location, Village village, int initialAmount) {
		super(location, village);
		this.resource = initialAmount;
	}


}