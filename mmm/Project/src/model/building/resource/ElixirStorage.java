package model.building.resource;

import model.Location;
import model.Village;

import java.io.Serializable;

public class ElixirStorage extends Storage implements Serializable {
    public final static int initialCapacity = 100;

    public final static int maxCapacity = 200;

    public final static double rate = 0.6;



    public ElixirStorage (Location location, Village village, int initialAmount)
    {
        super(location, village, initialAmount);
    }


    @Override
    public int getCapacity() {
        int capacity = initialCapacity;

        for (int i=1 ; i<level ; i++)
        {
            capacity *= rate;
        }

        return Integer.min( capacity , maxCapacity);
    }

    public int addResources(int amount) {//returns the remaining
        if (amount > this.getCapacity() - this.resource ) {
            this.resource = this.getCapacity();
            return amount - this.getCapacity() + this.getResource();
        }
        else
        {
            this.resource+=amount;
            return 0;
        }
    }
}