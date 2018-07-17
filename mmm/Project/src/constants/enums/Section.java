package constants.enums;

import java.util.ArrayList;

//TODo update updateMenu
//TODO add upgrade
public enum Section {
    Attack("attack"),
    Village("village"),
    TownHall("Town hall"),
    TownHallInfo("Info"),
    AvailableBuildings("Available buildings"),
    Barracks("Barracks"),
    BarracksInfo("Info"),
    BuildSoldiers("Build soldiers"),
    Camp("Camp"),
    CampInfo("Info"),
    Mine("Mine"),
    MineInfo("Info"),
    Storage("Storage"),
    StorageInfo("Info"),
    DefenceTower("Defence Tower"),
    DefenceTowerInfo("Info"),
    AttackMap("Map");

    private ArrayList<Section> subdivisions = new ArrayList<>();
    private ArrayList<String> validCommands = new ArrayList<>();
    private ArrayList<String> dynamicValidCommands = new ArrayList<>();
    private String value;
    private String menu = "";
    private Section parent;

    static  {
        Attack.subdivisions.add(AttackMap);
        Attack.validCommands.add("turn \\d+");
        Attack.validCommands.add("resources");
        Attack.validCommands.add("showMenu");
        Attack.validCommands.add("whereAmI");
        Attack.validCommands.add("save .* .*");
        Attack.validCommands.add("Back");
        Attack.validCommands.add("Load map");

        Village.subdivisions.add(Attack);
        Village.subdivisions.add(TownHall);
        Village.subdivisions.add(Storage);
        Village.subdivisions.add(Barracks);
        Village.subdivisions.add(Camp);
        Village.subdivisions.add(Mine);
        Village.subdivisions.add(DefenceTower);
        Village.validCommands.add("turn \\d+");
        Village.validCommands.add("resources");
        Village.validCommands.add("showMenu");
        Village.validCommands.add("whereAmI");
        Village.validCommands.add("save .* .*");
        Village.validCommands.add("showBuildings");

        TownHall.subdivisions.add(TownHallInfo);
        TownHall.subdivisions.add(AvailableBuildings);
        TownHall.validCommands.add("turn \\d+");
        TownHall.validCommands.add("resources");
        TownHall.validCommands.add("showMenu");
        TownHall.validCommands.add("whereAmI");
        TownHall.validCommands.add("save .* .*");
        TownHall.validCommands.add("Back");
        TownHall.validCommands.add("Status");

        Barracks.subdivisions.add(BarracksInfo);
        Barracks.subdivisions.add(BuildSoldiers);
        Barracks.validCommands.add("turn \\d+");
        Barracks.validCommands.add("resources");
        Barracks.validCommands.add("showMenu");
        Barracks.validCommands.add("whereAmI");
        Barracks.validCommands.add("save .* .*");
        Barracks.validCommands.add("Back");
        Barracks.validCommands.add("Status");

        Camp.subdivisions.add(CampInfo);
        Camp.validCommands.add("turn \\d+");
        Camp.validCommands.add("resources");
        Camp.validCommands.add("showMenu");
        Camp.validCommands.add("whereAmI");
        Camp.validCommands.add("save .* .*");
        Camp.validCommands.add("Back");
        Camp.validCommands.add("Soldiers");

        Mine.subdivisions.add(MineInfo);
        Mine.validCommands.add("turn \\d+");
        Mine.validCommands.add("resources");
        Mine.validCommands.add("showMenu");
        Mine.validCommands.add("whereAmI");
        Mine.validCommands.add("save .* .*");
        Mine.validCommands.add("Back");
        Mine.validCommands.add("Mine");

        Storage.subdivisions.add(StorageInfo);
        Storage.validCommands.add("turn \\d+");
        Storage.validCommands.add("resources");
        Storage.validCommands.add("showMenu");
        Storage.validCommands.add("whereAmI");
        Storage.validCommands.add("save .* .*");
        Storage.validCommands.add("Back");

        DefenceTower.subdivisions.add(DefenceTowerInfo);
        DefenceTower.validCommands.add("turn \\d+");
        DefenceTower.validCommands.add("resources");
        DefenceTower.validCommands.add("showMenu");
        DefenceTower.validCommands.add("whereAmI");
        DefenceTower.validCommands.add("save .* .*");
        DefenceTower.validCommands.add("Back");
        DefenceTower.validCommands.add("Target");

        TownHallInfo.validCommands.add("turn \\d+");
        TownHallInfo.validCommands.add("resources");
        TownHallInfo.validCommands.add("showMenu");
        TownHallInfo.validCommands.add("whereAmI");
        TownHallInfo.validCommands.add("save .* .*");
        TownHallInfo.validCommands.add("Back");
        TownHallInfo.validCommands.add("Overall info");
        TownHallInfo.validCommands.add("Upgrade info");

        AvailableBuildings.validCommands.add("turn \\d+");
        AvailableBuildings.validCommands.add("resources");
        AvailableBuildings.validCommands.add("showMenu");
        AvailableBuildings.validCommands.add("whereAmI");
        AvailableBuildings.validCommands.add("save .* .*");
        AvailableBuildings.validCommands.add("Back");

        BarracksInfo.validCommands.add("turn \\d+");
        BarracksInfo.validCommands.add("resources");
        BarracksInfo.validCommands.add("showMenu");
        BarracksInfo.validCommands.add("whereAmI");
        BarracksInfo.validCommands.add("save .* .*");
        BarracksInfo.validCommands.add("Back");
        BarracksInfo.validCommands.add("Overall info");
        BarracksInfo.validCommands.add("Upgrade info");

        BuildSoldiers.validCommands.add("turn \\d+");
        BuildSoldiers.validCommands.add("resources");
        BuildSoldiers.validCommands.add("showMenu");
        BuildSoldiers.validCommands.add("whereAmI");
        BuildSoldiers.validCommands.add("save .* .*");
        BuildSoldiers.validCommands.add("Back");

        CampInfo.validCommands.add("turn \\d+");
        CampInfo.validCommands.add("resources");
        CampInfo.validCommands.add("showMenu");
        CampInfo.validCommands.add("whereAmI");
        CampInfo.validCommands.add("save .* .*");
        CampInfo.validCommands.add("Back");
        CampInfo.validCommands.add("Overall info");
        CampInfo.validCommands.add("Upgrade info");
        CampInfo.validCommands.add("Capacity info");

        MineInfo.validCommands.add("turn \\d+");
        MineInfo.validCommands.add("resources");
        MineInfo.validCommands.add("showMenu");
        MineInfo.validCommands.add("whereAmI");
        MineInfo.validCommands.add("save .* .*");
        MineInfo.validCommands.add("Back");
        MineInfo.validCommands.add("Overall info");
        MineInfo.validCommands.add("Upgrade info");

        StorageInfo.validCommands.add("turn \\d+");
        StorageInfo.validCommands.add("resources");
        StorageInfo.validCommands.add("showMenu");
        StorageInfo.validCommands.add("whereAmI");
        StorageInfo.validCommands.add("save .* .*");
        StorageInfo.validCommands.add("Back");
        StorageInfo.validCommands.add("Overall info");
        StorageInfo.validCommands.add("Upgrade info");
        StorageInfo.validCommands.add("Sources info");

        DefenceTowerInfo.validCommands.add("turn \\d+");
        DefenceTowerInfo.validCommands.add("resources");
        DefenceTowerInfo.validCommands.add("showMenu");
        DefenceTowerInfo.validCommands.add("whereAmI");
        DefenceTowerInfo.validCommands.add("save .* .*");
        DefenceTowerInfo.validCommands.add("Back");
        DefenceTowerInfo.validCommands.add("Overall info");
        DefenceTowerInfo.validCommands.add("Upgrade info");
        DefenceTowerInfo.validCommands.add("Attack info");

        AttackMap.validCommands.add("turn \\d+");
        AttackMap.validCommands.add("resources");
        AttackMap.validCommands.add("showMenu");
        AttackMap.validCommands.add("whereAmI");
        AttackMap.validCommands.add("save .* .*");
        AttackMap.validCommands.add("Back");
        AttackMap.validCommands.add("Map info");
        AttackMap.validCommands.add("Attack map");

        TownHall.menu = TownHall.menu + "1. " + TownHall.subdivisions.get(0).value + "\n2. " + TownHall.subdivisions.get(1).value + "\n3. " + TownHall.validCommands.get(6) + "\n4. Back";

        TownHallInfo.menu = TownHallInfo.menu + "1. Overall info\n2. Upgrade info\n3. Back";

        Barracks.menu = Barracks.menu + "1. Info\n2. Build soldiers\n3. Status\n4. Back";

        BarracksInfo.menu = BarracksInfo.menu + "1. Overall info\n2. Upgrade info\n3. Back";

        Camp.menu = Camp.menu + "1. Info\n2. Soldiers\n3. Back";

        CampInfo.menu = CampInfo.menu + "1. Overall Info\n2. Upgrade Info\n3. Capacity Info\n4. Back";

        Mine.menu = Mine.menu + "1. Info\n2. Mine\n3. Back";

        MineInfo.menu = MineInfo.menu + "1. Overall info\n2. Upgrade info\n3. Back";

        Storage.menu = Storage.menu + "1. Info\n2. Back";

        StorageInfo.menu = StorageInfo.menu + "1. Overall info\n2. Upgrade info\n3. Sources info\n4. Back";

        DefenceTower.menu = DefenceTower.menu + "1. Info\n2. Target\n3. Back";

        DefenceTowerInfo.menu = DefenceTowerInfo.menu + "1. Overall info\n2. Upgrade info\n3. Attack info\n4. Back";

        AttackMap.menu = AttackMap.menu + "1. Map info\n2. Attack map\n3. Back";

        Attack.parent = Village;
        Village.parent = null;
        TownHall.parent = Village;
        TownHallInfo.parent = TownHall;
        AvailableBuildings.parent = TownHall;
        Barracks.parent = Village;
        BarracksInfo.parent = Barracks;
        BuildSoldiers.parent = Barracks;
        Camp.parent = Village;
        CampInfo.parent = Camp;
        Mine.parent = Village;
        MineInfo.parent = Mine;
        Storage.parent = Village;
        StorageInfo.parent = Storage;
        DefenceTower.parent = Village;
        DefenceTowerInfo.parent = DefenceTower;
        AttackMap.parent = Attack;
    }

    public ArrayList<Section> getSubdivisions() {
        return this.subdivisions;
    }

    public ArrayList<String> getValidCommands() {
        return this.validCommands;
    }

    /**
     *
     * @param validCommands
     */
    public void updateValidCommands(ArrayList<String> validCommands) {
        dynamicValidCommands = validCommands;
    }

    public static void updateMenu() {
        Attack.menu = "";
        Attack.menu = Attack.menu + "1. " + Attack.validCommands.get(6) + "\n";
        int i;
        for(i = 0; i < Attack.dynamicValidCommands.size(); i++) {
            Attack.menu = Attack.menu + (i + 2) + ". " + Attack.dynamicValidCommands.get(i) + "\n";
        }
        Attack.menu = Attack.menu + (i + 2) + ". " + Attack.validCommands.get(5);

        AvailableBuildings.menu = "";
        for(i = 0; i < AvailableBuildings.dynamicValidCommands.size(); i++) {
            AvailableBuildings.menu = AvailableBuildings.menu + (i + 1) + ". " + AvailableBuildings.dynamicValidCommands.get(i) + "\n";
        }
        AvailableBuildings.menu = AvailableBuildings.menu + (i + 1) + ". Back";

        BuildSoldiers.menu = "";
        for(i = 0; i < BuildSoldiers.dynamicValidCommands.size(); i++) {
            BuildSoldiers.menu = BuildSoldiers.menu + (i + 1) + ". " + BuildSoldiers.dynamicValidCommands.get(i) + "\n";
        }
        BuildSoldiers.menu = BuildSoldiers.menu + (i + 1) + ". Back";
    }

    public String getMenu() {
        return menu;
    }

    Section(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public ArrayList<String> getDynamicValidCommands() {
        return dynamicValidCommands;
    }

    public Section getParent() {
        return parent;
    }
}