package model.building.defence;

import model.Location;
import model.Village;
import model.building.*;

import java.io.Serializable;

//TODO
public class GiantsCastle extends Building implements Serializable {
    public GiantsCastle(Location location, Village village) {
        super(location, village);
    }
}