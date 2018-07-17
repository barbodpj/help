package model.building.defence;

import model.Location;
import model.Village;
import model.building.*;

import java.io.Serializable;

//TODO
public class Wall extends Building implements Serializable {
    public Wall(Location location, Village village) {
        super(location, village);
    }
}