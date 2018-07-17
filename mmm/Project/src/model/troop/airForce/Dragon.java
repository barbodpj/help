package model.troop.airForce;

import constants.enums.BuildingType;
import model.Village;
import model.building.Building;
import model.troop.*;

import java.io.Serializable;
import java.util.ArrayList;

public class Dragon extends Troop implements Serializable {

    public Dragon(int level , Village village)
    {
        super(level , village);
        this.canFly = true;
    }


    @Override
    protected ArrayList<BuildingType> getTarget() {
        return null;
    }

}
