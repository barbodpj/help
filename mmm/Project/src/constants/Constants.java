package constants;

import constants.enums.*;
import model.*;

import java.util.HashMap;

public class Constants {

	public static HashMap<BuildingType, Cost> buildingBuildCost = new HashMap<>();
	public static HashMap<BuildingType, Integer> buildingBuildTime = new HashMap<>();
	public static HashMap<BuildingType, Integer> attackScoreGained = new HashMap<>();
	public static HashMap<TroopType, Cost> troopsBuildCost = new HashMap<>();
	public static HashMap<TroopType, Integer> troopsBuildTime = new HashMap<>();
	public static HashMap<TroopType, Integer> troopsAttackRange = new HashMap<>();
	public static HashMap<TroopType, Integer> troopsMaxSpeed = new HashMap<>();
	public static HashMap<BuildingType, Integer> buildingsAttackRange = new HashMap<>();
	public static HashMap<TroopType, Cost> troopsUpgradeCost = new HashMap<>();
	public static HashMap<BuildingType, Cost> buildingsUpgradeCost = new HashMap<>();
	public static HashMap<BuildingType, Integer> buildingsUpgradeTime = new HashMap<>();
	public static HashMap<BuildingType, Integer> buildingsInitialDamage = new HashMap<>();
	public static HashMap<TroopType, Integer> troopsInitialDamage = new HashMap<>();
	public static HashMap<BuildingType, Integer> buildingInitialHealth = new HashMap<>();
	public static HashMap<TroopType, Integer> troopsInitialHealth = new HashMap<>();
	public static HashMap<BuildingType, Integer> squareOfDamageRadius = new HashMap<>();

}