package model.building.defence;

import model.Location;
import model.Village;
import model.building.*;

import java.io.Serializable;

//TODO
public class Trap extends Building implements Serializable {
    public Trap(Location location, Village village) {
        super(location, village);
    }
}