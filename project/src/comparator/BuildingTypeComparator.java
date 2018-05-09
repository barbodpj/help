package comparator;

import constants.enums.BuildingType;
import model.building.Building;

import java.util.Comparator;

public class BuildingTypeComparator implements Comparator<BuildingType>{
    @Override
    public int compare(BuildingType buildingType1 , BuildingType buildingType2 ) {

        return buildingType1.getValue().compareTo(buildingType2.getValue());
    }
}
