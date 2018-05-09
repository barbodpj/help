package view;

import com.google.gson.Gson;
import constants.enums.BuildingType;
import constants.enums.Section;
import constants.enums.TroopType;
import controller.MenuController;
import controller.exception.InvalidCommandException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;


//TODO change menu later and add back feature
public class View {
    static Scanner scanner = new Scanner(System.in);
    private MenuController menuController;
    private VillageView villageView;
    private AttackView attackView;

    public View(MenuController menuController) {
        this.menuController = menuController;
        villageView = new VillageView();
        attackView = new AttackView();

        Gson gson = new Gson();
        String jsonString = gson.toJson(attackView);

        View view = gson.fromJson(jsonString, View.class);
    }

    public void printOutput(ArrayList<String> output) {
        for(int i = 0; i < output.size(); i++) {
            System.out.println(output.get(i));
        }
    }

    public void println(String line) {
        System.out.println(line);
    }

    public String enterGame() throws InvalidCommandException {
        String commandLine = View.scanner.nextLine();
        if(commandLine.matches("newGame") || commandLine.matches("load .*")) {
            return commandLine;
        }
        throw new InvalidCommandException();
    }

    public ArrayList<String> fileReader(String path) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(new FileReader(path));
        ArrayList<String> out = new ArrayList<>();
        while (scanner.hasNext()) {
            out.add(scanner.nextLine());
        }
        return out;
    }

    public VillageView getVillageView() {
        return villageView;
    }

    public AttackView getAttackView() {
        return attackView;
    }

    public String getMenuInput() throws InvalidCommandException {
        String commandLine = View.scanner.nextLine();
        if(menuController.getCurrentSection() == Section.Village) {
            for(String command: menuController.getCurrentSection().getValidCommands()) {
                if(commandLine.matches(command)) {
                    return commandLine;
                }
            }
            if(commandLine.matches(Section.Attack.getValue())) {
                return commandLine;
            }
            throw new InvalidCommandException();
        }
        else {
            try {
                if(Integer.parseInt(commandLine) <= menuController.getCurrentSection().getValidCommands().size() + menuController.getCurrentSection().getSubdivisions().size() + menuController.getCurrentSection().getDynamicValidCommands().size() - 4) {
                    return commandLine;
                }
            }
            catch (NumberFormatException e) {
                for (int i = 0; i < 4; i++) {
                    if(commandLine.matches(menuController.getCurrentSection().getValidCommands().get(i))) {
                        return commandLine;
                    }
                }
            }
            throw new InvalidCommandException();
        }
    }
}