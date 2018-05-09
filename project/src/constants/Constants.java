package constants;

import constants.enums.*;
import model.*;

import java.util.HashMap;

public class Constants {

	public static HashMap<BuildingType, Cost> buildingBuildCost;
	public static HashMap<BuildingType, Integer> buildingBuildTime;
	public static HashMap<BuildingType, Integer> attackScoreGained;
	public static HashMap<TroopType, Cost> troopsBuildCost;
	public static HashMap<TroopType, Integer> troopsBuildTime;
	public static HashMap<TroopType, Integer> troopsAttackRange;
	public static HashMap<TroopType, Integer> troopsMaxSpeed;
	public static HashMap<BuildingType, Integer> buildingsAttackRange;
	public static HashMap<TroopType, Cost> troopsUpgradeCost;
	public static HashMap<BuildingType, Cost> buildingsUpgradeCost;
	public static HashMap<BuildingType, Integer> buildingsUpgradeTime;
	public static HashMap<BuildingType, Integer> buildingsInitialDamage;
	public static HashMap<TroopType, Integer> troopsInitialDamage;
	public static HashMap<BuildingType, Integer> buildingInitialHealth;
	public static HashMap<TroopType, Integer> troopsInitialHealth;


}