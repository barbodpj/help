package model.troop;

import constants.Constants;
import constants.enums.BuildingType;
import constants.enums.TroopType;
import controller.exception.gameException.InvalidCellException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import model.Cost;
import model.Location;
import model.Village;
import model.interfaces.*;
import model.building.*;
import model.map.Map;
import view.TroopsGUI;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

//TODO refactoring cost name

public abstract class Troop implements Constructible, Attacker, Serializable{

	private TroopsGUI troopsGUI;

	public void setTroopsGUI(TroopsGUI troopsGUI) {
		this.troopsGUI = troopsGUI;
	}

	private static final int DAMAGE_INCREASE = 1; // upgrade

	private static final int HEALTH_INCREASE = 5; // upgrade

	private boolean isDead = false;


	public int getLevel() {
		return level;
	}

	public boolean isDead() {
		return isDead;
	}


	private int healthInWar; //this health is important only in attack mode

	protected Location location ; // this is important only in attack mode

	protected int level;

	public Location getLocation() {
		return location;
	}

	private Building target;

	protected Village village;

	protected boolean canFly;

	public boolean canFly() {
		return canFly;
	}

	private int getHealth() {
		return this.getInitialHealth() + this.level*HEALTH_INCREASE;
	}

	public int getHealthInWar() {

		return healthInWar;
	}


	public Troop (int level , Village village)
	{

		this.level = level;

		this.village = village;
	}

	@Override
	public Cost getBuildCost() {
		return Constants.troopsBuildCost.get(TroopType.valueOf(this.getClass().getSimpleName()));
	}

	public int getBuildTime(){
		return Constants.troopsBuildTime.get(TroopType.valueOf(this.getClass().getSimpleName()));
	}

	public Cost getUpgradeCost()
	{
		return Constants.troopsUpgradeCost.get(TroopType.valueOf(this.getClass().getSimpleName()));
	}

	public int getMaxSpeed() {
		return Constants.troopsMaxSpeed.get(TroopType.valueOf(this.getClass().getSimpleName()));
	}

	protected abstract ArrayList<BuildingType> getTarget();

	public void decreaseHealth(int amount ) // TODO override in healer
	{
		if (this.healthInWar > amount)
			this.healthInWar -= amount;
		else
			this.dead();
	}

	public void increaseHealth(int amount)
	{
		this.healthInWar = Integer.min(this.getHealth() , this.healthInWar + amount);
	}

	private void dead()
	{
		this.isDead = true;
	}

	public void startAttack(Location startLocation)
	{
		this.location = startLocation;

		this.healthInWar = this.getHealth();

		this.isDead = false;

		this.target = null;

	}

	public Building nextMoveInAttack(Map map ) throws InvalidCellException//overrides in healer //TODO implementing this method
	{
		Building building = null ;
		if( this.isDead )
			return null;

		if ( target != null && target.isDestroyed())
			target = null;

		this.findTargetInAttack( map );

		for ( int i=0 ; i < this.getMaxSpeed() ; i++) {
			this.move(map);
			troopsGUI.update();
		}

		if (this.distance( this.location.getX() , this.location.getY() , target.getLocation().getX() , target.getLocation().getY())
				<= this.getAttackRange() * this.getAttackRange() ) {
			Media mediaName = new Media(new File("assets/SoundTracks/swhit91.mp3").toURI().toString());
			MediaPlayer mediaPlayer = new MediaPlayer(mediaName);
			mediaPlayer.play();
			building = target.decreaseHealth(this.getDamage());

		}

		return building;


	}

	private void findTargetInAttack(Map map) throws InvalidCellException {

		Location startLocation = this.location;
		if (target == null) {
			ArrayList<BuildingType> targetBuildingTypes = this.getTarget();
			Location location = BFS(map, startLocation, targetBuildingTypes);
			if (location == null)
				location = BFS(map, startLocation, null);
			this.target = map.getCell(location).getBuilding();
		}

	}

	private Location BFS(Map map, Location startLocation, ArrayList<BuildingType> targetBuildingTypes) throws InvalidCellException
	{
		int min = 1000000000;
		Location destLocation = null;

		for(int i=0 ; i<30 ;i++){
			for(int j=0 ; j<30 ; j++){
				Location location = new Location(i,j);
				if(targetBuildingTypes != null){
					for (BuildingType buildingType : targetBuildingTypes) {
						if (map.getCell(location).getBuilding() != null && BuildingType.valueOf(map.getCell(location).getBuilding().getClass().getSimpleName()) == buildingType
								&& !map.getCell(location).getBuilding().isDestroyed()){
							if(distance(i,j,startLocation.getX() , startLocation.getY()) < min){
								min = distance(i,j,startLocation.getX() , startLocation.getY());
								destLocation = location;
							}
						}

					}

				}else{
					if (map.getCell(location).getBuilding() != null && !map.getCell(location).getBuilding().isDestroyed() &&
							BuildingType.valueOf(map.getCell(location).getBuilding().getClass().getSimpleName()) != BuildingType.Wall)
					{
						if(distance(i,j,startLocation.getX() , startLocation.getY()) < min){
							min=distance(i,j,startLocation.getX() , startLocation.getY());
							destLocation = location;
						}
					}
				}
			}
		}
		return destLocation;
//		int[] xMove = {-1, 0, 0, 1 };
//		int[] yMove = {0, 1, -1, 0 };
//
//		boolean isVisited[][] = new boolean[30][30];
//
//
//		ArrayList<Location> queue = new ArrayList<Location>();
//		queue.add(startLocation);
//
//		outer:
//		while ( queue.size() > 0) {
//			isVisited [queue.get(0).getX()][queue.get(0).getY()] = true;
//			if (targetBuildingTypes != null) {
//				for (BuildingType buildingType : targetBuildingTypes) {
//					System.out.println(buildingType);
//					if (map.getCell(queue.get(0)).getBuilding()!= null)
//						System.out.println(BuildingType.valueOf(map.getCell(queue.get(0)).getBuilding().getClass().getSimpleName()));
//					if (map.getCell(queue.get(0)).getBuilding()!= null && BuildingType.valueOf(map.getCell(queue.get(0)).getBuilding().getClass().getSimpleName()) == buildingType
//							&& !map.getCell(queue.get(0)).getBuilding().isDestroyed())
//						break outer;
//				}
//			}else
//			{
//				if(map.getCell(queue.get(0)).getBuilding() != null && !map.getCell(queue.get(0)).getBuilding().isDestroyed() &&
//						BuildingType.valueOf(map.getCell(queue.get(0)).getBuilding().getClass().getSimpleName())!= BuildingType.Wall)
//					break;
//			}
//
//			for (int i = 0 ; i < xMove.length ; i++)
//			{
//				int x = queue.get(0).getX() + xMove[i];
//				int y = queue.get(0).getY() + yMove[i];
//				if (x<0 || x >= Map.WIDTH || y < 0 || y >= Map.WIDTH || isVisited[x][y]) {
//				}
//				else {
//
//					Location location = new Location( x , y);
//					queue.add( location);
//				}
//			}
//
//			queue.remove(0);
//
//
//		}
//
//		if (queue.size()>0)
//			return queue.get(0);
//		else
//			return null;
	}

	private void move(Map map) throws InvalidCellException
	{
		if (this.distance( this.location.getX() , this.location.getY() , target.getLocation().getX() , target.getLocation().getY())
				<= this.getAttackRange() * this.getAttackRange())
			return;

		int[] xMove = {  -1 , 0  , 0 , 1 };
		int[] yMove = {   0 , -1 , 1 , 0};

		ArrayList<Integer> distances = new ArrayList<Integer>();
		ArrayList<Integer> index = new ArrayList<Integer>();

		for ( int i = 0 ; i< xMove.length ; i++)
		{
			int x = this.location.getX() + xMove[i];
			int y = this.location.getY() + yMove[i];

			Location location = new Location( x , y);

			boolean isOpstackle = false;

			try {
				isOpstackle = map.getCell(location).getBuilding() != null;
			}
			catch (Exception e)
			{
				isOpstackle = false;
			}

			if (this.canFly )
				isOpstackle = false;

			if ( x < 0 || x >= Map.WIDTH || y < 0 || y >= Map.HEIGHT  || isOpstackle )
				continue;

			int distance  = this.distance( target.getLocation().getX() , target.getLocation().getY() , x , y);

			index.add(i);
			distances.add(distance);

		}

		int finalX = this.location.getX() + xMove [index.get(distances.indexOf(Collections.min(distances)))] ;
		int finalY = this.location.getY() + yMove [index.get(distances.indexOf(Collections.min(distances)))] ;

		Location location = new Location( finalX , finalY);
		map.getCell(this.getLocation()).moveTroop(this,location);
		this.location = location;

	}

	public int getAttackRange() {
		return Constants.troopsAttackRange.get(TroopType.valueOf(this.getClass().getSimpleName())) ;
	}

	public int getDamage() {
		return Constants.troopsInitialDamage.get(TroopType.valueOf(this.getClass().getSimpleName())) + this.level * DAMAGE_INCREASE ;
	}

	private int distance(int x1, int y1, int x2, int y2)
	{
		return (x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1);
	}

	@Override
	public int getInitialHealth() {
		return Constants.troopsInitialHealth.get(TroopType.valueOf(this.getClass().getSimpleName()));
	}



}