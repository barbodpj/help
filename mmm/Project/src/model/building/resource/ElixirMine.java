package model.building.resource;

import constants.enums.BuildingType;
import constants.enums.Section;
import model.Location;
import model.Village;
import model.building.Building;
import model.building.headquarter.TownHall;

import java.io.Serializable;

public class ElixirMine extends Mine implements Serializable {

    public static final int INITIAL_RATE = 10;

    public static final double INCREASE_RATE = 0.6;

    @Override
    public void produce() {

        if(this.resource >= super.MAX )
            return;

        int flag = 0;
        for (int i = 1; i <= Building.buildingNumbers.get(BuildingType.ElixirStorage); i++)
        {
            Storage storage = ((Storage)village.getBuilding(BuildingType.ElixirStorage , i));
            if (storage.getCapacity() > storage.getResource())
            {
                flag = 1;
                break;
            }
        }

        if (flag == 1)
            this.resource += getProductionRate();
    }

    @Override
    public void moveToStorage() {
        int resource = this.resource;
        for (int i = 1; i <= Building.buildingNumbers.get(BuildingType.ElixirStorage) ; i++)
        {
            ElixirStorage elixirStorage = ((ElixirStorage)village.getBuilding(BuildingType.ElixirStorage , i));
            resource = elixirStorage.addResources(resource);
            if (resource == 0)
                break;
        }
        this.resource = resource;
    }

    public ElixirMine(Location location, Village village) {
        super(location, village);
    }

    public int getProductionRate() {

        int rate= INITIAL_RATE;

        for (int i=1 ; i<=level ; i++)
        {
            rate *= INCREASE_RATE;
        }

        return rate;


    }
}