package model.building.resource;

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



	public Storage(int initialAmount) {
		this.resource = initialAmount;
	}


}