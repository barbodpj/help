package model.building.defence;

import constants.enums.DefendingMode;
import model.Location;
import model.Village;

import java.io.Serializable;

public class AirDefence extends DefenceBuilding implements Serializable {
    public AirDefence(Location location, Village village) {
        super(location, village);
        this.defendingMode = DefendingMode.AirForce;
    }

    public void defend()
    {

    }
}