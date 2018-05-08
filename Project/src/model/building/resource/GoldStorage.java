package model.building.resource;

public class GoldStorage extends Storage {
    public final static int initialCapacity = 100;

    public final static int maxCapacity = 500;

    public final static double rate = 0.6;


    @Override
    public int getCapacity() {
        int capacity = initialCapacity;

        for (int i=1 ; i<level ; i++)
        {
            capacity *= rate;
        }

        return capacity;
    }

    public GoldStorage (int initialAmount)
    {
        super(initialAmount);
    }

    int addResources(int amount) {//returns the remaining
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