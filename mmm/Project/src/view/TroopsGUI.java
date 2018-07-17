package view;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import model.map.Map;
import model.troop.Troop;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class TroopsGUI {
    private int condition = 1;
    private ImageView imageViewR1;
    private ImageView imageViewR2;
    private ImageView imageViewL1;
    private ImageView imageViewL2;
    private ImageView imageViewD1;
    private ImageView imageViewD2;
    private ImageView imageViewU1;
    private ImageView imageViewU2;
    private String type;
    private Troop troop;
    private ArrayList<Double> xList = new ArrayList<>();
    private ArrayList<Double> yList = new ArrayList<>();
    private ImageView previousImage;

    public TroopsGUI(String type, Troop troop, double x, double y) {
        this.troop = troop;
        troop.setTroopsGUI(this);
        this.type = type;
        xList.add(x);
        yList.add(y);
        try {
            imageViewR1 = new ImageView(new Image(new FileInputStream("assets/units/" + type + "/right/" + type + "_0right.png")));
            imageViewL1 = new ImageView(new Image(new FileInputStream("assets/units/" + type + "/left/" + type + "_0left.png")));
            imageViewD1 = new ImageView(new Image(new FileInputStream("assets/units/" + type + "/down/" + type + "_0down.png")));
            imageViewU1 = new ImageView(new Image(new FileInputStream("assets/units/" + type + "/up/" + type + "_0up.png")));
            imageViewR2 = new ImageView(new Image(new FileInputStream("assets/units/" + type + "/right/" + type + "_1right.png")));
            imageViewL2 = new ImageView(new Image(new FileInputStream("assets/units/" + type + "/left/" + type + "_1left.png")));
            imageViewD2 = new ImageView(new Image(new FileInputStream("assets/units/" + type + "/down/" + type + "_1down.png")));
            imageViewU2 = new ImageView(new Image(new FileInputStream("assets/units/" + type + "/up/" + type + "_1up.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        previousImage = getDefault();
    }

    public void update() {
        if((troop.getLocation().getX() - xList.get(xList.size() - 1) > 0.01 || troop.getLocation().getX() - xList.get(xList.size() - 1) < -0.01) || (troop.getLocation().getY() - yList.get(yList.size() - 1) > 0.01 || troop.getLocation().getY() - yList.get(yList.size() - 1) < -0.01)) {
            if(troop.getLocation().getX() - xList.get(xList.size() - 1) > 0.01 || troop.getLocation().getX() - xList.get(xList.size() - 1) < -0.01) {
                double delta = (troop.getLocation().getX() - xList.get(xList.size() - 1)) / View.NUMBER_OF_STATES;
                xList.add(delta + xList.get(xList.size() - 1));
                yList.add(yList.get(yList.size() - 1));
                xList.add(delta + xList.get(xList.size() - 1));
                yList.add(yList.get(yList.size() - 1));
                xList.add(delta + xList.get(xList.size() - 1));
                yList.add(yList.get(yList.size() - 1));
                xList.add(delta + xList.get(xList.size() - 1));
                yList.add(yList.get(yList.size() - 1));
                xList.add(delta + xList.get(xList.size() - 1));
                yList.add(yList.get(yList.size() - 1));
                xList.add(delta + xList.get(xList.size() - 1));
                yList.add(yList.get(yList.size() - 1));
                xList.add(delta + xList.get(xList.size() - 1));
                yList.add(yList.get(yList.size() - 1));
                xList.add(delta + xList.get(xList.size() - 1));
                yList.add(yList.get(yList.size() - 1));
                xList.add(delta + xList.get(xList.size() - 1));
                yList.add(yList.get(yList.size() - 1));
                xList.add(delta + xList.get(xList.size() - 1));
                yList.add(yList.get(yList.size() - 1));
            }
            else {
                double delta = (troop.getLocation().getY() - yList.get(yList.size() - 1)) / View.NUMBER_OF_STATES;
                yList.add(delta + yList.get(yList.size() - 1));
                xList.add(xList.get(xList.size() - 1));
                yList.add(delta + yList.get(yList.size() - 1));
                xList.add(xList.get(xList.size() - 1));
                yList.add(delta + yList.get(yList.size() - 1));
                xList.add(xList.get(xList.size() - 1));
                yList.add(delta + yList.get(yList.size() - 1));
                xList.add(xList.get(xList.size() - 1));
                yList.add(delta + yList.get(yList.size() - 1));
                xList.add(xList.get(xList.size() - 1));
                yList.add(delta + yList.get(yList.size() - 1));
                xList.add(xList.get(xList.size() - 1));
                yList.add(delta + yList.get(yList.size() - 1));
                xList.add(xList.get(xList.size() - 1));
                yList.add(delta + yList.get(yList.size() - 1));
                xList.add(xList.get(xList.size() - 1));
                yList.add(delta + yList.get(yList.size() - 1));
                xList.add(xList.get(xList.size() - 1));
                yList.add(delta + yList.get(yList.size() - 1));
                xList.add(xList.get(xList.size() - 1));
            }
        }
    }

    public ImageView getPreviousImage() {
        return previousImage;
    }

    public ImageView getDefault() {
        return imageViewD1;
    }

    public ImageView changeImage() {
        if(xList.size() <= 1 && yList.size() <= 1) {
            return previousImage;
        }
        double x = xList.get(0);
        double y = yList.get(0);
        double x2 = xList.get(1);
        double y2 = yList.get(1);
        if(condition == 1) {
            condition = 2;
        }
        else {
            condition = 1;
        }
        String direction = "";
        if(x2 - x < 0) {
            direction = "left";
        }
        else if(x2 - x > 0) {
            direction = "right";
        }
        else {
            if(y2 - y < 0) {
                direction = "up";
            }
            else if(y2 - y > 0) {
                direction = "down";
            }
        }
        xList.remove(0);
        yList.remove(0);
        switch (direction) {
            case "right":
                if (condition == 1) {
                    previousImage = imageViewR1;
                    return imageViewR1;
                }
                else {
                    previousImage = imageViewR2;
                    return imageViewR2;
                }
            case "left":
                if (condition == 1) {
                    previousImage = imageViewL1;
                    return imageViewL1;
                }
                else {
                    previousImage = imageViewL2;
                    return imageViewL2;
                }
            case "up":
                if (condition == 1) {
                    previousImage = imageViewU1;
                    return imageViewU1;
                }
                else {
                    previousImage = imageViewU2;
                    return imageViewU2;
                }
            case "down":
                if (condition == 1) {
                    previousImage = imageViewD1;
                    return imageViewD1;
                }
                else {
                    previousImage = imageViewD2;
                    return imageViewD2;
                }
        }
        return null;
    }

    public Troop getTroop() {
        return troop;
    }

    public ArrayList<Double> getxList() {
        return xList;
    }

    public ArrayList<Double> getyList() {
        return yList;
    }
}
