package Units;

import Board.Board;
import Board.LoggerUtils;
import Size.Size;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

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

        ImageView acLIImV = getIcon("icons/icon_16.png");
        ProgressBar pbAcLI = getNewProgressbar(getCurrentAccuracy(new LightInfantry()), "red");
        infoGP.add(acLIImV, 0,0);
        infoGP.add(pbAcLI, 1,0);

        ImageView acHIImV = getIcon("icons/icon_17.png");
        ProgressBar pbAcHI = getNewProgressbar(getCurrentAccuracy(new HeavyInfantry()), "red");
        infoGP.add(acHIImV, 0,1);
        infoGP.add(pbAcHI, 1,1);

        ImageView acVEImV = getIcon("icons/icon_18.png");
        ProgressBar pbAcVE = getNewProgressbar(getCurrentAccuracy(new Vehicle()), "red");
        infoGP.add(acVEImV, 0,2);
        infoGP.add(pbAcVE, 1,2);

        ImageView efLIImV = getIcon("icons/icon_19.png");
        ProgressBar pbEfLI = getNewProgressbar(getCurrentRangeEfficiency(new LightInfantry()), "red");
        infoGP.add(efLIImV, 0,3);
        infoGP.add(pbEfLI, 1,3);

        ImageView efHIImV = getIcon("icons/icon_20.png");
        ProgressBar pbEfHI = getNewProgressbar(getCurrentRangeEfficiency(new HeavyInfantry()), "red");
        infoGP.add(efHIImV, 0,4);
        infoGP.add(pbEfHI, 1,4);

        ImageView efVEImV = getIcon("icons/icon_21.png");
        ProgressBar pbEfVE = getNewProgressbar(getCurrentRangeEfficiency(new Vehicle()), "red");
        infoGP.add(efVEImV, 0,5);
        infoGP.add(pbEfVE, 1,5);

        ImageView closeDamageImV = getIcon("icons/icon_2.png");
        Label closeDamageLabel = getLabel("CONSTANT");
        infoGP.add(closeDamageImV, 0,6);
        infoGP.add(closeDamageLabel, 1,6);

        ImageView deadZoneImV = getIcon("icons/icon_10.png");
        Label deadZoneLabel = getLabel(Integer.toString(deadZone));
        infoGP.add(deadZoneImV, 0,7);
        infoGP.add(deadZoneLabel, 1,7);

        return infoGP;
    }
}
