package model.building.defence;

import constants.Constants;
import controller.exception.gameException.InvalidCellException;
import javafx.scene.control.TreeTableRow;
import model.Location;
import model.Village;
import model.building.*;
import model.interfaces.*;
import constants.enums.*;
import model.map.Map;
import model.troop.Troop;

import java.io.Serializable;
import java.lang.annotation.Target;
import java.util.ArrayList;


//TODO getAttackInfo atrin

public abstract class DefenceBuilding extends Building implements Attacker,Serializable {

    protected TroopType target;

//	protected Troop currentTarget;

    protected int currentTargetsDamage;

    protected DefendingMode defendingMode;

    public String getAttackInfo(Map map) throws InvalidCellException {

        Troop troop = null;

        if (this.getTroopFromLocation(this.findTarget(map) , map).get(0) != null)
            troop = this.getTroopFromLocation(this.findTarget(map) , map).get(0);

        if (troop == null)
            return "Target: none" + "\n" + "Damage: " + currentTargetsDamage + "\n" + "Damage Range: " + getAttackRange();
        else
            return "Target: " + TroopType.valueOf(troop.getClass().getSimpleName()).getValue() + "\n" + "Damage: " + currentTargetsDamage + "\n" + "Damage Range: " + getAttackRange();

    }

    protected ArrayList<Troop> getTroopFromLocation(Location location , Map map) throws InvalidCellException {

        ArrayList<Troop> troops = null;

        if (location == null)
            return null;

        if (this.defendingMode == DefendingMode.GroundForce)
            troops = map.getCell(location).getGroundTroops();
        else
            troops = map.getCell(location).getAirTroops();

        if (troops == null && this.defendingMode == DefendingMode.AirGroundForce)
            troops = super.village.getMap().getCell(location).getGroundTroops();

        return troops;

    }

    /**
     * @param
     */
    public Location findTarget( Map map) throws InvalidCellException {
        return this.BFS(map);
    }

    public void chooseTarget(TroopType target) {
        this.target = target;
    }


    public void attack(Map map) throws InvalidCellException {

        Location location = this.findTarget(map);

        if (location == null)
            return;

        for (int x = location.getX() - 2; x <= location.getX() + 2; x++) {
            for (int y = location.getY() - 2; y <= location.getY() + 2; y++) {

                if (x < 0 || x >= Map.HEIGHT || y < 0 || y >= Map.WIDTH ||
                        distance(x, y, location.getX(), location.getY()) > this.getDamageRadius())
                    continue;

                Location location1 = new Location(x, y);

                ArrayList<Troop> troops = getTroopFromLocation(location1 , map);

                for (Troop item : troops)
                    item.decreaseHealth(this.getDamage());

            }
        }

    }

    @Override
    public int getAttackRange() {
        return Constants.buildingsAttackRange.get(BuildingType.valueOf(this.getClass().getSimpleName()));
    }

    @Override
    public int getDamage() {
        return Constants.buildingsInitialDamage.get(BuildingType.valueOf(this.getClass().getSimpleName())) + this.level;
    }

    public DefenceBuilding(Location location, Village village) {
        super(location, village);
    }

    protected Location BFS(Map map) throws InvalidCellException {
        int[] xMove = {-1, 0, 0, 1};
        int[] yMove = {0, 1, -1, 0};

        boolean isVisited[][] = new boolean[30][30];


        ArrayList<Location> queue = new ArrayList<Location>();
        queue.add(this.location);

        outer:
        while (queue.size() > 0) {

            isVisited[queue.get(0).getX()][queue.get(0).getY()] = true;

            boolean isGroundTroop = (map.getCell(queue.get(0)).getGroundTroops() != null);
            boolean isAirTroop = (map.getCell(queue.get(0)).getAirTroops() != null);

            boolean isTarget = (isGroundTroop && this.defendingMode != DefendingMode.AirForce) ||
                    (isAirTroop && this.defendingMode != DefendingMode.GroundForce);


            if (isTarget)
                break outer;


            for (int i = 0; i < xMove.length; i++) {
                int x = queue.get(0).getX() + xMove[i];
                int y = queue.get(0).getY() + yMove[i];
                if (x < 0 || x > 29 || y < 0 || y > 29 || isVisited[x][y] ||
                        this.distance(x, y, this.location.getX(), this.location.getY()) > this.getAttackRange() * this.getAttackRange())
                    continue;
                else {
                    Location location = new Location(x, y);
                    queue.add(location);
                }
            }

            queue.remove(location);
        }

        if (queue.size() > 0)
            return queue.get(0);
        else
            return null;
    }

    public int distance(int x1, int y1, int x2, int y2) {
        int distance = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
        return distance;
    }

    protected int getDamageRadius() {
        return Constants.squareOfDamageRadius.get(BuildingType.valueOf(this.getClass().getSimpleName()));
    }

}