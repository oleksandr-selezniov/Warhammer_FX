package Units;

import Board.Board;
import Board.LoggerUtils;
import Size.Size;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Dmitriy on 02.11.2016.
 */
public class Artillery extends Vehicle {
    private static Locale locale = new Locale("en", "US");
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("Artillery", locale);
    private int deadZone;
    private double insetsY = 37;
    private double insetsX = 50;
    public Artillery(){}

    public Artillery(String unitName, int team){
        this.team = team;
        this.name = resourceBundle.getString(unitName+".name");
        this.setHealth(Integer.parseInt(resourceBundle.getString(unitName+".health")));
        this.setAmmo(Integer.parseInt(resourceBundle.getString(unitName+".ammo")));
        this.maxAmmo = getAmmo();
        this.armor = Double.parseDouble(resourceBundle.getString(unitName+".armor"));
        this.minRangeDamage = Integer.parseInt(resourceBundle.getString(unitName+".minRangeDamage"));
        this.maxRangeDamage = Integer.parseInt(resourceBundle.getString(unitName+".maxRangeDamage"));
        this.accuracy = resourceBundle.getString(unitName+".accuracy");
        this.minCloseDamage = Integer.parseInt(resourceBundle.getString(unitName+".minCloseDamage"));
        this.maxCloseDamage = Integer.parseInt(resourceBundle.getString(unitName+".maxCloseDamage"));
        this.walkRange = Integer.parseInt(resourceBundle.getString(unitName+".walkRange"));
        this.shotRange = Integer.parseInt(resourceBundle.getString(unitName+".shotRange"));
        this.deadZone = Integer.parseInt(resourceBundle.getString(unitName+".deadZone"));
        this.picturePath = resourceBundle.getString(unitName+".picturePath");
        this.key = resourceBundle.getString(unitName+".key");
        this.cost = Integer.parseInt(resourceBundle.getString(unitName+".cost"));
        this.heightCoeff = 1;
        this.widthCoeff = 1;
        this.isActive = false;
        this.rangeEfficiency = resourceBundle.getString(unitName+".rangeEfficiency");
        this.setUnitImageView(getImageView(1.0));

        try {
            this.heightCoeff = Double.parseDouble(resourceBundle.getString(unitName+".height"));
            this.widthCoeff = Double.parseDouble(resourceBundle.getString(unitName+".width"));
            this.insetsY = Double.parseDouble(resourceBundle.getString(unitName+".insetsY"));
            this.insetsX = Double.parseDouble(resourceBundle.getString(unitName+".insetsX"));
        }catch (Exception e){
            System.out.println("INFO: some resources were not present");
        }
    }

    @Override
    public javafx.geometry.Insets getInsetsY() {
        if(Board.getScaleCoefficient()!=null){
            return new Insets(2,2,Board.getScaleCoefficient()* insetsY,-Size.getCellHeight()*Board.getScaleCoefficient()*0.4);
        }
        return new Insets(2,2, insetsY,2);
    }

    public void performCloseAttack(Unit victim){
        int closeDamage = getCloseDamage();
        victim.setHealth(victim.getHealth() - closeDamage);
        LoggerUtils.writeCloseAttackLog(this, victim, closeDamage);
        this.setActive(false);
    }

    public void performRangeAttack(Unit victim){
        int rangeDamage = getRangeDamage(victim);
        double chance = Math.random();
        if (this.getAmmo() > 0) {
            if(chance < getCurrentAccuracy(victim)) {
                victim.setHealth(victim.getHealth() - rangeDamage);
                LoggerUtils.writeRangeAttackLog(this, victim, rangeDamage);
                this.setAmmo(this.getAmmo() - 1);
                this.setActive(false);
            }else{
                LoggerUtils.writeMissedLog(this);
                this.setAmmo(this.getAmmo() - 1);
                this.setActive(false);
            }
        } else {
            LoggerUtils.writeOutOfAmmoLog(this);
        }
    }

    private int getRangeDamage(Unit victim){
        double efficiency = getCurrentRangeEfficiency(victim);
        int minActualDamage = minRangeDamage;
        int maxActualDamage = maxRangeDamage;
        if(efficiency>=1){
            minActualDamage = (int)(minActualDamage*efficiency);
            if (minActualDamage >= maxActualDamage){minActualDamage = maxActualDamage;}
        }else{
            maxActualDamage = (int)(maxActualDamage*efficiency);
            if(maxActualDamage <= minActualDamage){maxActualDamage = minActualDamage;}
        }
        return minActualDamage + (int)(Math.random() * ((maxActualDamage - minActualDamage) + 1));
    }

    private int getCloseDamage(){
        return minCloseDamage + (int)(Math.random() * ((maxCloseDamage - minCloseDamage) + 1));
    }

    public int getDeadZone() {
        return deadZone;
    }

    public void setDeadZone(int deadZone) {
        this.deadZone = deadZone;
    }

    public String getInfo(){
        return "Name: "+name+"\n"+"Efficiency "+rangeEfficiency+"\n"+"Health "+getHealth()+"/"+maxHealth+"\n"+"Accuracy "+accuracy+
                "\n"+"Ammo "+getAmmo()+"/"+maxAmmo+"\n"+"Close Damage "+ minCloseDamage +"-"+maxCloseDamage +"\n"
                + "Range Damage "+ minRangeDamage +"-"+ maxRangeDamage +"\n"+"Walk range "+walkRange+"\n"+"ShotRange "+shotRange+"\n"+
                "Dead Zone Range "+deadZone+"\n"+ "Cost: " +cost;
    }

    public GridPane getRightInfoGridPane(){
        GridPane infoGP = new GridPane();
        infoGP.setVgap(1);
        infoGP.setHgap(1);

        ImageView acLIImV = getIcon("icons/icon_16.png", 40);
        ProgressBar pbAcLI = getNewProgressbar(getCurrentAccuracy(new LightInfantry()), "red", 140, 20);
        infoGP.add(acLIImV, 0,0);
        infoGP.add(pbAcLI, 1,0);

        ImageView acHIImV = getIcon("icons/icon_17.png", 40);
        ProgressBar pbAcHI = getNewProgressbar(getCurrentAccuracy(new HeavyInfantry()), "red", 140, 20);
        infoGP.add(acHIImV, 0,1);
        infoGP.add(pbAcHI, 1,1);

        ImageView acVEImV = getIcon("icons/icon_18.png", 40);
        ProgressBar pbAcVE = getNewProgressbar(getCurrentAccuracy(new Vehicle()), "red", 140, 20);
        infoGP.add(acVEImV, 0,2);
        infoGP.add(pbAcVE, 1,2);

        ImageView efLIImV = getIcon("icons/icon_19.png", 40);
        ProgressBar pbEfLI = getNewProgressbar(getCurrentRangeEfficiency(new LightInfantry()), "red", 140, 20);
        infoGP.add(efLIImV, 0,3);
        infoGP.add(pbEfLI, 1,3);

        ImageView efHIImV = getIcon("icons/icon_20.png", 40);
        ProgressBar pbEfHI = getNewProgressbar(getCurrentRangeEfficiency(new HeavyInfantry()), "red", 140, 20);
        infoGP.add(efHIImV, 0,4);
        infoGP.add(pbEfHI, 1,4);

        ImageView efVEImV = getIcon("icons/icon_21.png", 40);
        ProgressBar pbEfVE = getNewProgressbar(getCurrentRangeEfficiency(new Vehicle()), "red", 140, 20);
        infoGP.add(efVEImV, 0,5);
        infoGP.add(pbEfVE, 1,5);

        ImageView closeDamageImV = getIcon("icons/icon_2.png", 40);
        Label closeDamageLabel = getLabel("CONSTANT", 12);
        infoGP.add(closeDamageImV, 0,6);
        infoGP.add(closeDamageLabel, 1,6);

        ImageView deadZoneImV = getIcon("icons/icon_10.png", 40);
        Label deadZoneLabel = getLabel(Integer.toString(deadZone), 12);
        infoGP.add(deadZoneImV, 0,7);
        infoGP.add(deadZoneLabel, 1,7);

        return infoGP;
    }

    public VBox getBattleInfoGridPane(){
        HBox hBox = new HBox();
        VBox vBox = new VBox();
        GridPane infoGP = new GridPane();
        infoGP.setVgap(1);
        infoGP.setHgap(1);

        GridPane infoGPLowLeft = new GridPane();
        infoGPLowLeft.setMinWidth(60);
        infoGPLowLeft.setVgap(1);
        infoGPLowLeft.setHgap(1);

        GridPane infoGPLowRight = new GridPane();
        infoGPLowRight.setMinWidth(60);
        infoGPLowRight.setVgap(1);
        infoGPLowRight.setHgap(1);

        ImageView nameImV = getIcon("icons/icon_14.png", 25);
        Label nameLabel = getLabel(name, 10);
        infoGP.add(nameImV, 0,0);
        infoGP.add(nameLabel, 1,0);

        ImageView healthImV = getIcon("icons/icon_7.png", 25);
        ProgressBar healthPB = getNewProgressbar(0, "red", 110, 12);
        healthPB.progressProperty().bind(healthPercentageProperty());
        HBox healthHBox = new HBox();
        Label currentHealthLabel = getLabel("", 10);
        healthHBox.setAlignment(Pos.CENTER_LEFT);
        currentHealthLabel.textProperty().bind(healthProperty().asString());
        Label maxHealthLabel = getLabel("/"+maxHealth, 10);
        healthHBox.getChildren().addAll(healthPB, currentHealthLabel, maxHealthLabel);
        infoGP.add(healthImV, 0,1);
        infoGP.add(healthHBox, 1,1);

        ImageView ammoIV = getIcon("icons/icon_12.png", 25);
        ProgressBar ammoPB = getNewProgressbar(0, "orange", 110, 12);
        ammoPB.progressProperty().bind(ammoPercentageProperty());
        ammoPB.setMaxSize(100, 10);
        HBox ammoHBox = new HBox();
        Label currentAmmoLabel = getLabel("", 10);
        ammoHBox.setAlignment(Pos.CENTER_LEFT);
        currentAmmoLabel.textProperty().bind(ammoProperty().asString());
        Label maxAmmoLabel = getLabel("/"+maxAmmo, 10);
        ammoHBox.getChildren().addAll(ammoPB, currentAmmoLabel, maxAmmoLabel);
        infoGP.add(ammoIV, 0,2);
        infoGP.add(ammoHBox, 1,2);

        ImageView rangeDamageEffImV = getIcon("icons/icon_26.png", 25);
        Label rangeDamageEffLabel = getLabel(getRangeEfficiency(), 10);
        infoGPLowLeft.add(rangeDamageEffImV, 0,0);
        infoGPLowLeft.add(rangeDamageEffLabel, 1,0);

        ImageView accuracyImV = getIcon("icons/icon_27.png", 25);
        Label accuracyLabel = getLabel(getAccuracy(), 10);
        infoGPLowLeft.add(accuracyImV, 0,1);
        infoGPLowLeft.add(accuracyLabel, 1,1);

        ImageView walkrangeImV = getIcon("icons/icon_13.png", 25);
        Label walkrangeLabel = getLabel(Integer.toString(walkRange), 10);
        infoGPLowLeft.add(walkrangeImV, 0,2);
        infoGPLowLeft.add(walkrangeLabel, 1,2);

        ImageView shotRangeImV = getIcon("icons/icon_5.png", 25);
        Label shotRangeLabel = getLabel(Integer.toString(shotRange), 10);
        infoGPLowLeft.add(shotRangeImV, 0,3);
        infoGPLowLeft.add(shotRangeLabel, 1,3);

        ImageView rangeDamageImV = getIcon("icons/icon_6.png", 25);
        Label rangeDamageLabel = getLabel(minRangeDamage + " - " +maxRangeDamage, 10);
        infoGPLowRight.add(rangeDamageImV, 0,0);
        infoGPLowRight.add(rangeDamageLabel, 1,0);

        ImageView closeDamageImV = getIcon("icons/icon_2.png", 25);
        Label closeDamageLabel = getLabel(minCloseDamage + " - " +maxCloseDamage, 10);
        infoGPLowRight.add(closeDamageImV, 0,1);
        infoGPLowRight.add(closeDamageLabel, 1,1);

        ImageView costImV = getIcon("icons/icon_3.png", 25);
        Label costLabel = getLabel(Integer.toString(cost), 10);
        infoGPLowRight.add(costImV, 0,2);
        infoGPLowRight.add(costLabel, 1,2);

        ImageView deadZoneImV = getIcon("icons/icon_10.png", 25);
        Label deadZoneLabel = getLabel(Integer.toString(deadZone), 10);
        infoGPLowRight.add(deadZoneImV, 0,3);
        infoGPLowRight.add(deadZoneLabel, 1,3);

        hBox.getChildren().addAll(infoGPLowLeft, infoGPLowRight);
        vBox.getChildren().addAll(infoGP,hBox);
        return vBox;
    }
}
