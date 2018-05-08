package model.building.troopPreparation;

import model.building.*;
import constants.enums.*;
import model.troop.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Camp extends Building {

	private HashMap<TroopType, ArrayList<Troop>> troops = new HashMap<TroopType , ArrayList<Troop>>();

	public static int CAPACITY = 50;

	public static int getOverallCapacity() {
		return Building.buildingNumbers.get(BuildingType.Camp)*Camp.CAPACITY;
	}

	@Override
	public void upgrade() {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 * @param troop
	 */

	void addTroop(Troop troop) {
		TroopType troopType = TroopType.valueOf(troop.getClass().toString());
		this.troops.get(troopType).add(troop);
	}

	public ArrayList<String> showTroops() {

		ArrayList<String> output = new ArrayList<String>();
		ArrayList<TroopType> troopTypes = new ArrayList<TroopType>(troops.keySet());
		Collections.sort(troopTypes);

		for (TroopType item:troopTypes ) {

			String troopName = item.toString();
			String number = "x" + this.troops.get(item).size();

			String information = troopName + " " + number;
			output.add(information);

		}
		return output;
	}

	public int getTroopNumber()
	{
		int answer = 0;
		for ( TroopType troopType:this.troops.keySet() )
		{
			answer += this.troops.get(troopType).size();
		}
		return answer;
	}

}