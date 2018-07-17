package model.troop.army;

import constants.enums.BuildingType;
import model.Village;
import model.building.Building;
import model.troop.*;

import java.io.Serializable;
import java.util.ArrayList;

public class WallBreaker extends Troop implements Serializable { // spped change when there are no walls
    @Override
    protected ArrayList<BuildingType> getTarget() {
        ArrayList<BuildingType> buildingTypes = new ArrayList<BuildingType>();

        buildingTypes.add(BuildingType.Wall);

        return buildingTypes;
    }

    public WallBreaker (int level , Village village)
    {
        super(level , village);
        this.canFly = false;
    }

}