package view;

import constants.enums.BuildingType;
import constants.enums.TroopType;
import controller.MenuController;
import controller.exception.InvalidCommandException;

import java.util.ArrayList;

public class AttackView {

    public String getMapPath() {
        return View.scanner.nextLine();
    }

    public String selectUnits() throws InvalidCommandException {
        String commandLine = View.scanner.nextLine();

        if(commandLine.matches("End select")) {
            return commandLine;
        }
        for(TroopType type: TroopType.values()) {
            if(commandLine.matches("Select " + type.getValue() + " \\d")) {
                return commandLine;
            }
        }
        throw new InvalidCommandException();
    }

    public String startSelection() throws InvalidCommandException {
        String commandLine = View.scanner.nextLine();
        if(commandLine.matches("Start slect")) {
            return commandLine;
        }
        throw new InvalidCommandException();
    }

    public String putUnits() throws InvalidCommandException {
        String commandLine = View.scanner.nextLine();

        if(commandLine.matches("Go next turn")) {
            return commandLine;
        }
        for(TroopType type: TroopType.values()) {
            if(commandLine.matches("Put " + type.getValue() + " \\d in \\d,\\d")) {
                return commandLine;
            }
        }
        throw new InvalidCommandException();
    }

    public String getAttackCommand() throws InvalidCommandException {
        String commandLine = View.scanner.nextLine();

        if(commandLine.matches("status resources") || commandLine.matches("status units") || commandLine.matches("status towers") || commandLine.matches("status all")) {
            return commandLine;
        }
        if(commandLine.matches("Quit attack")) {
            return commandLine;
        }
        for(TroopType type: TroopType.values()) {
            if(commandLine.matches("status unit " + type.getValue()) ) {
                return commandLine;
            }
        }
        for(BuildingType type: BuildingType.values()) {
            if(commandLine.matches("status tower " + type.getValue()) ) {
                return commandLine;
            }
        }
        throw new InvalidCommandException();
    }
}
