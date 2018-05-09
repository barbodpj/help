package model.troop;

import constants.Constants;
import constants.enums.BuildingType;
import constants.enums.TroopType;
import controller.exception.gameException.InvalidCellException;
import model.Cost;
import model.Location;
import model.Village;
import model.interfaces.*;
import model.building.*;
import model.map.Map;
import model.troop.airForce.Dragon;
import model.troop.airForce.Healer;

import java.util.ArrayList;
import java.util.Collections;

//TODO refactoring cost name

public abstract class Troop implements Constructible, Attacker{


	protected  static final int DAMAGE_INCREASE = 1; // upgrade

	protected  static final int HEALTH_INCREASE = 5; // upgrade

	protected boolean isDead = false;

	protected int health; // its constant always

	protected int attackingHealth; //this health is important only in attack mode

	protected Location location ; // this is important only in attack mode

	protected int level;

	protected Building target;

	protected Village village;

	public Troop (int level , Village village)
	{
		this.health = Constants.troopsInitialHealth.get(TroopType.valueOf(this.getClass().toString())) + this.level * HEALTH_INCREASE ;

		this.level = level;

		this.village = village;
	}

	@Override
	public Cost getBuildCost() {
		return Constants.troopsBuildCost.get(TroopType.valueOf(this.getClass().toString()));
	}

	public int getBuildTime(){
		return Constants.troopsBuildTime.get(TroopType.valueOf(this.getClass().toString()));
	}

	public Cost getUpgradeCost()
	{
		return Constants.troopsUpgradeCost.get(TroopType.valueOf(this.getClass().toString()));
	}

	public int getMaxSpeed() {
		return Constants.troopsMaxSpeed.get(TroopType.valueOf(this.getClass().toString()));
	}

	protected abstract ArrayList<BuildingType> getTarget();

	public void decreaseHealth(int amount ) // TODO override in healer
	{
		if (this.attackingHealth > amount)
			this.attackingHealth -= amount;
		else
			this.dead();
	}

	public void increaseHealth(int amount)
	{
		this.attackingHealth = Integer.min(this.health , this.attackingHealth + amount);
	}

	protected void dead()
	{
		this.isDead = true;
	}

	public void startAttack(Location startLocation)
	{
		this.location = location;

		this.attackingHealth = this.health;

		this.isDead = false;

		this.target = null;

	}

	public Cost nextMoveInAttack(Map map ) throws InvalidCellException//overrides in healer //TODO implementing this method
	{
		Cost cost = null ;
		if( this.isDead )
			return null;

		if (target.isDestoryed())
			target = null;

		this.findTargetInAttack( map );

		this.move( map );

		if (this.distance( this.location.getX() , this.location.getY() , target.getLocation().getX() , target.getLocation().getY())
				<= this.getAttackRange()) {
			cost = target.decreaseHealth(this.getDamage());

		}

		return cost;


	}

	protected void findTargetInAttack( Map map ) throws InvalidCellException {

		Location startLocation = this.location;
		if (target == null) {
			ArrayList<BuildingType> targetBuildingTypes = this.getTarget();
			Location location = BFS(map, startLocation, targetBuildingTypes);
			if (location == null)
				location = BFS(map, startLocation, null);
			this.target = map.getCell(location).getBuilding();
		}

	}

	protected Location BFS( Map map , Location startLocation , ArrayList<BuildingType> targetBuildingTypes ) throws InvalidCellException
	{
		int[] xMove = {-1, 0, 0, 1 };
		int[] yMove = {0, 1, -1, 0 };

		boolean isVisited[][] = new boolean[30][30];


		ArrayList<Location> queue = new ArrayList<Location>();
		queue.add(startLocation);

		outer:
		while ( queue.size() > 0) {

			isVisited [queue.get(0).getX()][queue.get(0).getY()] = true;
			if (targetBuildingTypes != null) {
				for (BuildingType buildingType : targetBuildingTypes) {
					if (BuildingType.valueOf(map.getCell(queue.get(0)).getBuilding().getClass().toString()) == buildingType)
						break outer;
				}
			}else
			{
				if(map.getCell(queue.get(0)).getBuilding() != null)
					break outer;
			}

			for (int i = 0 ; i < xMove.length ; i++)
			{
				int x = queue.get(0).getX() + xMove[i];
				int y = queue.get(0).getY() + yMove[i];
				if (x<0 || x > 29 || y < 0 || y > 29 || isVisited[x][y])
					continue;
				else {

					Location location = new Location( x , y);
					queue.add( location);
				}
			}

			queue.remove(location);


		}

		if (queue.size()>0)
			return queue.get(0);
		else
			return null;
	}

	protected void move(Map map) throws InvalidCellException
	{
		if (this.distance( this.location.getX() , this.location.getY() , target.getLocation().getX() , target.getLocation().getY())
				<= this.getAttackRange())
			return;

		int[] xMove = { -1 , -1 , -1 , 0  , 0 , 1 , 1 , 1};
		int[] yMove = { -1 , 0 , 1 , -1 , 1 , -1 , 0 , 1};

		ArrayList<Integer> distances = new ArrayList<Integer>();
		ArrayList<Integer> index = new ArrayList<Integer>();

		for ( int i = 0 ; i< xMove.length ; i++)
		{
			int x = this.location.getX() + xMove[i];
			int y = this.location.getY() + yMove[i];

			Location location = new Location( x , y);

			boolean isOpstackle = false;

			if (map.getCell(location).getBuilding() != null)
				isOpstackle = true;
			else
				isOpstackle = false;

			if (this.getClass() == Dragon.class || this.getClass() == Healer.class )
				isOpstackle = false;

			if ( x < 0 || x > 29 || y < 0 || y > 29  || map.getCell( location ).getBuilding() != null )
				continue;

			int distance  = this.distance( target.getLocation().getX() , target.getLocation().getY() , x , y);

			index.add(i);
			distances.add(distance);

		}

		int finalX = this.location.getX() + xMove [distances.indexOf(Collections.min(distances))] ;
		int finalY = this.location.getY() + yMove [distances.indexOf(Collections.min(distances))] ;

		Location location = new Location( finalX , finalY);
		this.location = location;

	}

	public int getAttackRange() {
		return Constants.troopsAttackRange.get(TroopType.valueOf(this.getClass().toString())) ;
	}

	public int getDamage() {
		return Constants.troopsAttackRange.get(TroopType.valueOf(this.getClass().toString())) + this.level * DAMAGE_INCREASE ;
	}

	public int distance ( int x1 , int y1 , int x2 , int y2)
	{
		int distance = (x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1);
		return distance;
	}
}