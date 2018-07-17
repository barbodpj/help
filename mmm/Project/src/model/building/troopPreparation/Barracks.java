package model.building.troopPreparation;

import constants.Constants;
import controller.exception.UnsupportedOperationException;
import controller.exception.gameException.LevelBoundaryException;
import model.Cost;
import model.Location;
import model.ReflectionUI;
import model.Village;
import model.building.*;
import model.troop.*;
import constants.enums.*;
import view.View;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import java.util.Collections;

public class Barracks extends Building implements Serializable {

	private ArrayList<Troop> buildQueue = new ArrayList<>();

	private ArrayList<Integer> remainingTime = new ArrayList<>();


    public Barracks(Location location, Village village) {
        super(location, village);
        this.village = village;
    }


	public ArrayList<String> getAvailableTroops() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
		ArrayList <String> availableTroops = new ArrayList<String>();

        for (TroopType troopType : TroopType.values()) {

            String troopName = troopType.getValue();

            String troopTypeString = null;
            if (troopType == TroopType.Dragon || troopType == TroopType.Healer)
                troopTypeString = "model.troop.airForce.";
            else
                troopTypeString = "model.troop.army.";

            Troop troop = (Troop) ReflectionUI.getInstanceFromName(troopTypeString + troopType.toString());

            int maxTroopNumbers = village.getAvailableElixir() / troop.getBuildCost().getElixir();

            String maximumTroopNumbers = Integer.toString(maxTroopNumbers);

            String troopInformation;


            if (maxTroopNumbers == 0 || (troopType == TroopType.Dragon && this.level <= 1)) {
                troopInformation = troopName + " U";
            } else {
                troopInformation = troopName + " A x" + maximumTroopNumbers;
            }

            availableTroops.add(troopInformation);
        }


        return availableTroops;
    }

    public void decreaseRemainingTime() {
        int flag = 0;
        for (int i = 0; i < remainingTime.size(); i++) {

            if (this.remainingTime.get(i) != 0) {
                this.remainingTime.set(i, this.remainingTime.get(i) - 1);
                flag = 1;
            }

            if (this.remainingTime.get(i) == 0) {
                for (int j = 1; j <= Building.buildingNumbers.get(BuildingType.Camp); j++) {
                    Camp camp = (Camp) village.getBuilding(BuildingType.Camp, j);
                    if (camp.getTroopNumber() < Camp.CAPACITY) {
                        this.moveToCamp(buildQueue.get(i), camp);
                        this.buildQueue.remove(i);
                        this.remainingTime.remove(i);
                        break;
                    }
                }
            }
            if (flag == 1)
                break;
        }
    }


    /**
     * @param type
     * @param number
     */
    public void build(TroopType type, int number) throws IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {

        String packageName = null;

        if ( type == TroopType.Dragon || type == TroopType.Healer )
            packageName = "model.troop.airForce.";
        else
            packageName = "model.troop.army.";

        Troop troop = (Troop) ReflectionUI.getInstanceFromName(packageName + type.toString(), this.level , this.village);

        for (int i = 0; i < number; i++) {

            this.buildQueue.add(troop);
            this.remainingTime.add(troop.getBuildTime() - this.level);
        }

    }

    private void moveToCamp(Troop troop, Camp camp) {
        View.playSoldierConstructed();
        camp.addTroop(troop);
    }

    public ArrayList<String> showStatus() {

        ArrayList<String> output = new ArrayList<String>();

        for (int i = 0; i < buildQueue.size(); i++) {
            output.add(this.buildQueue.get(i).getClass().getSimpleName() + " " + this.remainingTime.get(i));
        }

        return output;
    }

    @Override
    public void upgrade() throws LevelBoundaryException, UnsupportedOperationException {
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