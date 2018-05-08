package model.troop.airForce;

import constants.enums.BuildingType;
import model.Village;
import model.building.Building;
import model.troop.*;

import java.util.ArrayList;

public class Dragon extends Troop {

    public Dragon(int level , Village village)
    {
        super(level , village);
    }


    @Override
    protected ArrayList<BuildingType> getTarget() {
        return null;
    }

}
