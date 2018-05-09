package model.building.resource;

import model.Location;
import model.Village;
import model.building.*;

public abstract class Storage extends Building {

	protected int resource;

	public int getResource() {
		return this.resource;
	}

	public abstract int getCapacity();


	public int use(int quantity) { //returns the remaining
		this.resource -= Integer.min(quantity  , this.resource);
		return quantity - Integer.min(quantity , this.resource);

	}



	public Storage(Location location, Village village, int initialAmount) {
		super(location, village);
		this.resource = initialAmount;
	}


}