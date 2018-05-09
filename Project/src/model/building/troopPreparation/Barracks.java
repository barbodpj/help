package model.building.troopPreparation;

import constants.Constants;
import controller.exception.gameException.LevelBoundaryException;
import model.Cost;
import model.Location;
import model.ReflectionUI;
import model.Village;
import model.building.*;
import model.troop.*;
import constants.enums.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import java.util.Collections;

public class Barracks extends Building {

	private ArrayList<Troop> buildQueue;

	private ArrayList<Integer> remainingTime;


	public  Barracks(Location location, Village village)
	{
		super(location, village);
		this.village = village;
	}

	public ArrayList<String> getAvailableTroops() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		ArrayList <String> availabeTroops = new ArrayList<String>();

		int number = 0;
		for (TroopType troopType : TroopType.values()){

			number++;
			String troopName = Integer.toString(number) + ". " + troopType.toString();

			Troop troop  = (Troop) ReflectionUI.getInstanceFromName(troopType.toString());

			int goldLimited = village.getAvailableGold() / troop.getBuildCost().getGold();
			int elixirLimited = village.getAvailableElixir() / troop.getBuildCost().getElixir();

			int maxTroopNumbers = Integer.min( goldLimited , elixirLimited );

			String maximumtroopNumbers = Integer.toString( maxTroopNumbers );
			String troopInformation;

			if ( maxTroopNumbers == 0 || ( troopType == TroopType.Dragon && this.level<= 1 ) )
			{
				troopInformation = troopName + " U";
			}else{
				troopInformation = troopName + " A x" + maximumtroopNumbers;
			}

			availabeTroops.add(troopInformation);
		}

		return availabeTroops;
	}

	public void decreaseRemainingTime() {
		int flag = 0;
		for (int i = 0; i < remainingTime.size(); i++) {

			if (this.remainingTime.get(i) != 0) {
				this.remainingTime.add(i, this.remainingTime.get(i) - 1);
				flag = 1;
			}

			if (this.remainingTime.get(i) == 0) {
				for (int j = 1; j <= Building.buildingNumbers.get(BuildingType.Camp); j++) {
					Camp camp = (Camp) village.getBuilding(BuildingType.Camp, j);
					if (camp.getTroopNumber() < Camp.CAPACITY) {
						this.moveToCamp(buildQueue.get(i) , camp);
						this.buildQueue.remove(i);
						this.remainingTime.remove(i);
						break;
					}
				}
			}
			if (flag==1)
				break;
		}
	}


	/**
	 * 
	 * @param type
	 * @param number
	 */
	public void build(TroopType type, int number) throws IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {

			Troop troop = (Troop) ReflectionUI.getInstanceFromName(type.toString() , this.level);

		for (int i=0 ; i < number ; i++) {

				this.buildQueue.add(troop);
				this.remainingTime.add(troop.getBuildTime()-this.level);
		}

	}

	private void moveToCamp(Troop troop , Camp camp ){
			camp.addTroop(troop);
	}

	public ArrayList <String> showStatus() {

		ArrayList<String> output = new ArrayList<String>();

		for ( int i = 0 ; i < buildQueue.size() ; i++)
		{
			output.add(this.buildQueue.get(i).getClass() + " " + this.remainingTime.get(i));
		}

		return output;
	}

	@Override
	public void upgrade() throws UnsupportedOperationException, LevelBoundaryException {
			if (Collections.max(Constants.troopsBuildTime.values()) <= this.level)
				throw new UnsupportedOperationException();
		super.upgrade();
	}

	@Override
	public Cost getBuildCost() {
		return super.getBuildCost();
	}

	@Override
	public int getBuildTime() {
		return super.getBuildTime();

	}

	@Override
	public Cost getUpgradeCost() {
		return super.getUpgradeCost();
	}
}