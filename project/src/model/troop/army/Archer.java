package model.troop.army;

import constants.Constants;
import constants.enums.BuildingType;
import constants.enums.TroopType;
import model.Village;
import model.building.Building;
import model.troop.*;

import java.util.ArrayList;

public class Archer extends Troop {

    public Archer(int level , Village village)
    {
        super(level , village);
    }


    @Override
    protected ArrayList<BuildingType> getTarget() {
        ArrayList<BuildingType> buildingTypes = new ArrayList<BuildingType>();

        buildingTypes.add(BuildingType.AirDefence);

        buildingTypes.add(BuildingType.ArcherTower);

        buildingTypes.add(BuildingType.Cannon);

        buildingTypes.add(BuildingType.GiantsCastle);

        return buildingTypes;
    }


}