package controller;

import com.google.gson.Gson;
import controller.exception.InvalidCommandException;
import model.*;
import view.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

//TODO add save option
public class Controller {
    private Village model = null;
    private View view;
    private AttackController attackController;
    private VillageController villageController;
    private MenuController menuController;

    public Controller(View view, MenuController menuController) {
        this.view = view;
        this.menuController = menuController;
    }

    public void readConstants() {
        ArrayList<String> input = view.fileReader()
    }

    public void start() {
        while (true) {
            String command = null;
            try {
                command = view.enterGame();
                if(command.matches("newGame")) {
                    model = new Village();
                    break;
                }
                if(command.matches("load .*")) {
                    String[] input = command.split(" ");
                    model = load(input[1]);
                    if(model != null) {
                        break;
                    }
                }
            }
            catch (InvalidCommandException ignored) {

            }
        }
        villageController = new VillageController(model, view, menuController);
        attackController = new AttackController();
    }

    private void save(String filePath) {
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            Gson gson = new Gson();
            String jsonString = gson.toJson(model);
            fileWriter.write(jsonString);
            fileWriter.close();

        } catch (IOException e) {
            view.println("No such path");
        }

    }

    private Village load(String filePath) {
        try {
            Scanner scanner = new Scanner(new FileReader(filePath));
            String jsonString = scanner.nextLine();
            Gson gson = new Gson();
            return gson.fromJson(jsonString, Village.class);
        } catch (FileNotFoundException e) {
            view.println("File not found");
            return null;
        }
    }

    public AttackController getAttackController() {
        return attackController;
    }

    public VillageController getVillageController() {
        return villageController;
    }

    public void handleMenuBack() {
        view.println(menuController.back());
    }

    public void handleShowMenu() {
        view.println(menuController.showMenu());
    }

    public void handleWhereAmI() {
        if(menuController.getBuildingType() == null) {
            view.println(menuController.getCurrentSection().getValue());
        }
        else {
            view.println(menuController.getBuildingType().getValue() + " " + menuController.getBuildingNumber());
        }
    }
}