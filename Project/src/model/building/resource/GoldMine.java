package model.building.resource;

import constants.enums.BuildingType;
import model.Location;
import model.Village;
import model.building.Building;

public class GoldMine extends Mine {
    @Override
    public void produce() {

        if(this.resource >= super.MAX )
            return;

        int flag = 0;
        for (int i = 0 ; i < Building.buildingNumbers.get(BuildingType.GoldStorage); i++)
        {
            Storage storage = ((Storage)village.getBuilding(BuildingType.GoldStorage , i));
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
        for (int i=0; i<Building.buildingNumbers.get(BuildingType.GoldStorage) ; i++)
        {
            GoldStorage goldStorage = ((GoldStorage)village.getBuilding(BuildingType.GoldStorage , i));
            resource = goldStorage.addResources(resource);
            if (resource == 0)
                break;
        }
    }

    public GoldMine(Location location, Village village) {
        super(location, village);
    }
}