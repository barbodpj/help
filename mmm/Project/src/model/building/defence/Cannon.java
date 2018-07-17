package model.building.defence;

import constants.enums.DefendingMode;
import model.Location;
import model.Village;

import java.io.Serializable;

public class Cannon extends DefenceBuilding implements Serializable {
    public Cannon(Location location, Village village) {
        super(location, village);
        this.defendingMode = DefendingMode.GroundForce;
    }
}