package controller;

import com.google.gson.Gson;
import model.*;
import view.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Controller {
    Village model;
    View view;
    AttackController attackController;
    VillageController villageController;
    MenuController menuController;

    public Controller(Village model, View view, MenuController menuController) {
        this.model = model;
        this.view = view;
        this.menuController = menuController;
        villageController = new VillageController();
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
}