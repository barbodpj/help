package json;

import com.gilecode.yagson.YaGson;
import constants.enums.BuildingType;
import controller.exception.gameException.InvalidCellException;
import model.Location;
import model.ReflectionUI;
import model.building.Building;
import model.building.defence.Wall;
import model.map.Map;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class JMap {
    private int[] size;
    private JWall[] walls;
    private JResource resources;
    private JBuilding[] buildings;


    public Map toMap() throws Exception {
        Map map = new Map();
        if(size[0] != Map.WIDTH || size[1] != Map.HEIGHT) {
            throw new Exception();
        }
        for(JWall jWall: walls) {
            Location location = new Location(jWall.getX(), jWall.getY());
            map.getCell(location).addBuilding(new Wall(location, null));
            while (map.getCell(location).getBuilding().getLevel() < jWall.getLevel())
            {
                map.getCell(location).getBuilding().incrementLevel();
            }
            map.addMapBuildings(map.getCell(location).getBuilding());
        }
        for (JBuilding jBuilding : buildings) {
            Location location = new Location(jBuilding.getX(), jBuilding.getY());
            if(jBuilding.getType() == 3 || jBuilding.getType() == 4) {
                map.getCell(location).addBuilding(((Building)ReflectionUI.getInstanceFromNameAndLocation(BuildingType.values()[jBuilding.getType() - 1].getFullName(), location, jBuilding.getAmount())));
            }
            else {
                map.getCell(location).addBuilding(((Building)ReflectionUI.getInstanceFromNameAndLocation(BuildingType.values()[jBuilding.getType() - 1].getFullName(), location)));
            }
            while (map.getCell(location).getBuilding().getLevel() < jBuilding.getLevel())
            {
                map.getCell(location).getBuilding().incrementLevel();
            }
            map.addMapBuildings(map.getCell(location).getBuilding());
        }
        return map;
    }

    public static JMap readMap(String path) throws FileNotFoundException {
        Scanner jScanner = new Scanner(new FileReader(path));
        StringBuilder stringBuilder = new StringBuilder();
        while (jScanner.hasNext()) {
            stringBuilder.append(jScanner.nextLine());
        }
        String jString = stringBuilder.toString();
        jString = jString.replace(" ", "").replace("\t", "");
        YaGson yaGson = new YaGson();
        return yaGson.fromJson(jString, JMap.class);
    }
}














