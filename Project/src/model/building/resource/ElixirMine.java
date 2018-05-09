package model.building.resource;

import constants.enums.BuildingType;
import model.Location;
import model.Village;
import model.building.Building;

public class ElixirMine extends Mine {
    @Override
    public void produce() {

        if(this.resource >= super.MAX )
            return;

        int flag = 0;
        for (int i = 0; i < Building.buildingNumbers.get(BuildingType.ElixirStorage); i++)
        {
            Storage storage = ((Storage)village.getBuilding(BuildingType.ElixirStorage , i));
            if (storage.getCapacity() > storage.getResource())
            {
                flag = 1;
                break;
            }
        }

        if (flag == 1)
            this.resource += super.getProductionRate();
    }

    @Override
    public void moveToStorage() {
        int resource = this.resource;
        for (int i=0; i<Building.buildingNumbers.get(BuildingType.ElixirStorage) ; i++)
        {
            ElixirStorage elixirStorage = ((ElixirStorage)village.getBuilding(BuildingType.ElixirStorage , i));
            resource = elixirStorage.addResources(resource);
            if (resource == 0)
                break;
        }
    }

    public ElixirMine(Location location, Village village) {
        super(location, village);
    }
}