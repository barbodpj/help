package model.troop.army;

import constants.enums.BuildingType;
import model.Village;
import model.building.Building;
import model.troop.*;

import java.io.Serializable;
import java.util.ArrayList;

public class Guardian extends Troop implements Serializable {

    @Override
    protected ArrayList<BuildingType> getTarget() {
        return null;
    }


    public  Guardian (int level , Village village)
    {

        super(level , village);
        this.canFly = false;
    }


}