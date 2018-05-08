package model.troop.army;

import constants.enums.BuildingType;
import model.Village;
import model.building.Building;
import model.troop.*;

import java.util.ArrayList;

public class Giant extends Troop {
    @Override
    protected ArrayList<BuildingType> getTarget() {
        ArrayList<BuildingType> buildingTypes = new ArrayList<BuildingType>();

        buildingTypes.add(BuildingType.ElixirMine);

        buildingTypes.add(BuildingType.GoldMine);

        buildingTypes.add(BuildingType.ElixirStorage);

        buildingTypes.add(BuildingType.GoldStorage);

        return buildingTypes;
    }


    public Giant(int level , Village village)
    {
        super(level , village);
    }

}