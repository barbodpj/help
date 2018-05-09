package view;

import constants.enums.BuildingType;
import constants.enums.Section;
import constants.enums.TroopType;
import controller.MenuController;
import controller.exception.InvalidCommandException;

import java.util.Scanner;

public class VillageView {

    public String getBuildingInput() throws InvalidCommandException {
        String commandLine = View.scanner.nextLine();
        for(BuildingType type: BuildingType.values()) {
            if(commandLine.matches(type.getValue() + " \\d")) {
                return commandLine;
            }
        }
        throw new InvalidCommandException();
    }

    public String yesNoQuestion() throws InvalidCommandException {
        String commandLine = View.scanner.nextLine();
        if(commandLine.matches("Y") || commandLine.matches("N")) {
            return commandLine;
        }
        throw new InvalidCommandException();
    }

    public String getLocation() throws InvalidCommandException {
        String commandLine = View.scanner.nextLine();
        if(commandLine.matches("\\(\\d, \\d\\)")) {
            return commandLine;
        }
        throw new InvalidCommandException();
    }

    public String getTroopNumber() throws InvalidCommandException {
        String commandLine = View.scanner.nextLine();
        if(commandLine.matches("\\d")) {
            return commandLine;
        }
        throw new InvalidCommandException();
    }

    public String getTarget() throws InvalidCommandException {
        String commandLine = View.scanner.nextLine();
        for(TroopType type: TroopType.values()) {
            if(commandLine.matches(type.getValue())) {
                return commandLine;
            }
        }
        throw new InvalidCommandException();
    }

    public String upgradeInfo() throws InvalidCommandException {
        String commandLine = View.scanner.nextLine();
        if(commandLine.matches("upgrade") || commandLine.matches("Back")) {
            return commandLine;
        }
        throw new InvalidCommandException();
    }
}
