package model.troop.airForce;

import constants.enums.BuildingType;
import model.Cost;
import model.Village;
import model.map.Map;
import model.troop.*;

import java.io.Serializable;
import java.util.ArrayList;

public class Healer extends Troop implements Serializable { //TODO so many things

    //TODO healer health decrease in every time
    //TODO BFS

    private static final int INITIAL_LIFE_TIME = 10;

    public Healer(int level , Village village)
    {
        super(level , village);
        this.canFly = true;
    }
    @Override
    protected ArrayList<BuildingType> getTarget() {
        return null;
    }

    @Override
    public void decreaseHealth(int amount) {};

//    @Override
//    public Cost nextMoveInAttack(Map map) {
//        return null;
//    }

//    protected Location BFS(HashMap <TroopType , ArrayList < Troop > > troops ) {
//        int[] xMove = {-1, 0, 0, 1 };
//        int[] yMove = {0, 1, -1, 0 };
//
//        boolean isVisited[][] = new boolean[30][30];
//
//
//        ArrayList<Location> queue = new ArrayList<Location>();
//        queue.add(this.location);
//
//        outer:
//        while ( queue.size() > 0) {
//
//            isVisited [queue.get(0).getX()][queue.get(0).getY()] = true;
//                if (BuildingType.valueOf( map.getCell(queue.get(0)).getBuilding().getClass().toString()) == buildingType)
//                    break outer;
//            }else
//            {
//                if(map.getCell(queue.get(0)).getBuilding() != null)
//                    break outer;
//            }
//
//            for (int i = 0 ; i < xMove.length ; i++)
//            {
//                int xa = queue.get(0).getX() + xMove[i];
//                int y = queue.get(0).getY() + yMove[i];
//                if (x<0 || x > 29 || y < 0 || y > 29 || isVisited[x][y])
//                    continue;
//                else {
//                    Location location = new Location( x , y);
//                    queue.add( location);
//                }
//            }
//
//            queue.remove(location);
//
//
//        }
//
//        if (queue.size()>0)
//            return queue.get(0);
//        else
//            return null;
//    }


}