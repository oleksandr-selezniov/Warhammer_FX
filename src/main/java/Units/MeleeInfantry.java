package Units;

import Board.Board;
import Units.Interfaces.MeleeUnit;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;

import java.util.Locale;
import java.util.ResourceBundle;
import Board.Utils.LoggerUtils;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by Glazyrin.D on 11/9/2016.
 */
public class MeleeInfantry extends Unit implements MeleeUnit {
    private static Locale locale = new Locale("en", "US");
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("MeleeInfantry", locale);
    private double insetsY = 2.0;
    private String closeEfficiency;
    public MeleeInfantry(){}

    public MeleeInfantry(String unitName, int team){
        this.team = team;
        this.name = resourceBundle.getString(unitName+".name");
        this.setHealth(Integer.parseInt(resourceBundle.getString(unitName+".health")));
        this.armor = Double.parseDouble(resourceBundle.getString(unitName+".armor"));
        this.minCloseDamage = Integer.parseInt(resourceBundle.getString(unitName+".minCloseDamage"));
        this.maxCloseDamage = Integer.parseInt(resourceBundle.getString(unitName+".maxCloseDamage"));
        this.walkRange = Integer.parseInt(resourceBundle.getString(unitName+".walkRange"));
        this.picturePath = resourceBundle.getString(unitName+".picturePath");
        this.key = resourceBundle.getString(unitName+".key");
        this.cost = Integer.parseInt(resourceBundle.getString(unitName+".cost"));
        this.heightCoeff = 1;
        this.widthCoeff = 1;
        this.isActive = false;
        this.closeEfficiency = resourceBundle.getString(unitName+".closeEfficiency");
        this.setUnitImageView(getImageView(1.0));

        try {
            this.heightCoeff = Double.parseDouble(resourceBundle.getString(unitName+".height"));
            this.widthCoeff = Double.parseDouble(resourceBundle.getString(unitName+".width"));
            this.insetsY = Double.parseDouble(resourceBundle.getString(unitName+".insetsY"));
        }catch (Exception e){
            System.out.println("INFO: some resources were not present");
        }
    }

    @Override
    public javafx.geometry.Insets getInsetsY() {
        if(Board.getScaleCoefficient()!=null){
            return new Insets(2,2,Board.getScaleCoefficient()* insetsY,2);
        }
        return new Insets(2,2,insetsY,2);
    }

    public void performCloseAttack(Unit victim){
        int closeDamage = getCloseDamage(victim);
        victim.setHealth(victim.getHealth() - closeDamage);
        LoggerUtils.writeCloseAttackLog(this, victim, closeDamage);
        this.setActive(false);
    }

    public void performRapidMeleeAttack(Unit victim){
        int rapidCloseDamage = getCloseDamage(victim)/2;  //two times less damage
        victim.setHealth(victim.getHealth() - rapidCloseDamage);
        LoggerUtils.writeCloseAttackLog(this, victim, rapidCloseDamage);
        //this.setActive(false);
    }

    public int getCloseDamage(Unit victim){
        double efficiency = getCurrentCloseEfficiency(victim);
        int minActualDamage = minCloseDamage;
        int maxActualDamage = maxCloseDamage;
        if(efficiency>=1){
            minActualDamage = (int)(minActualDamage*efficiency);
            if (minActualDamage >= maxActualDamage){minActualDamage = maxActualDamage;}
        }else{
            maxActualDamage = (int)(maxActualDamage*efficiency);
            if(maxActualDamage <= minActualDamage){maxActualDamage = minActualDamage;}
        }
        return minActualDamage + (int)(Math.random() * ((maxActualDamage - minActualDamage) + 1));
    }

    private double getCurrentCloseEfficiency(Unit victim){
        String effString = getCloseEfficiency();
        String tokens = "/";
        String[] splited = effString.split(tokens);

        double LE = Double.parseDouble(splited[0]);
        double HE = Double.parseDouble(splited[1]);
        double VE = Double.parseDouble(splited[2]);

        if(victim instanceof MeleeInfantry){return LE;}
        if(victim instanceof LightInfantry){return LE;}
        if(victim instanceof HeavyInfantry){return HE;}
        if(victim instanceof Vehicle){return VE;}
        return 1;
    }

    private String getCloseEfficiency() {
        return closeEfficiency;
    }

    public String getInfo(){
        return "Name: "+name+"\n"+"Efficiency "+closeEfficiency+"\n"+"Health "+getHealth()+"/"+maxHealth+"\n"
                +"Close Damage "+ minCloseDamage +"-"+maxCloseDamage +"\n"+"Walk range "+walkRange+"\n"
                + "Cost: " +cost;
    }

    public GridPane getRightInfoGridPane(){
        GridPane infoGP = new GridPane();
        infoGP.setVgap(1);
        infoGP.setHgap(1);

        ImageView cdLIImV = getIcon("icons/icon_22.png", 40);
        ProgressBar cdAcLI = getNewProgressbar(getCurrentCloseEfficiency(new LightInfantry()), "red", 140, 20);
        infoGP.add(cdLIImV, 0,0);
        infoGP.add(cdAcLI, 1,0);

        ImageView cdHIImV = getIcon("icons/icon_23.png", 40);
        ProgressBar cdAcHI = getNewProgressbar(getCurrentCloseEfficiency(new HeavyInfantry()), "red", 140, 20);
        infoGP.add(cdHIImV, 0,1);
        infoGP.add(cdAcHI, 1,1);

        ImageView cdVEImV = getIcon("icons/icon_24.png", 40);
        ProgressBar cdAcVE = getNewProgressbar(getCurrentCloseEfficiency(new Vehicle()), "red", 140, 20);
        infoGP.add(cdVEImV, 0,2);
        infoGP.add(cdAcVE, 1,2);

        ImageView rangeDamageImV = getIcon("icons/icon_6.png", 40);
        Label rangeDamageLabel = getLabel("N/A", 12);
        infoGP.add(rangeDamageImV, 0,3);
        infoGP.add(rangeDamageLabel, 1,3);

        ImageView ammoIV = getIcon("icons/icon_12.png", 40);
        HBox ammoHBox = new HBox();
        ammoHBox.setAlignment(Pos.CENTER_LEFT);
        Label maxAmmoLabel = getLabel("N/A", 12);
        ammoHBox.getChildren().addAll(maxAmmoLabel);
        infoGP.add(ammoIV, 0,4);
        infoGP.add(ammoHBox, 1,4);

        return infoGP;
    }

    public GridPane getLeftInfoGridPane(){
        GridPane infoGP = new GridPane();
        infoGP.setVgap(1);
        infoGP.setHgap(1);

        ImageView nameImV = getIcon("icons/icon_14.png", 40);
        Label nameLabel = getLabel(name, 12);
        infoGP.add(nameImV, 0,0);
        infoGP.add(nameLabel, 1,0);

        ImageView healthImV = getIcon("icons/icon_7.png", 40);
        HBox healthHBox = new HBox();
        healthHBox.setAlignment(Pos.CENTER_LEFT);
        Label maxHealthLabel = getLabel(Integer.toString(maxHealth), 12);
        healthHBox.getChildren().addAll(maxHealthLabel);
        infoGP.add(healthImV, 0,1);
        infoGP.add(healthHBox, 1,1);

        ImageView closeDamageImV = getIcon("icons/icon_2.png", 40);
        Label closeDamageLabel = getLabel(minCloseDamage + " - " +maxCloseDamage, 12);
        infoGP.add(closeDamageImV, 0,2);
        infoGP.add(closeDamageLabel, 1,2);

        ImageView walkrangeImV = getIcon("icons/icon_13.png", 40);
        Label walkrangeLabel = getLabel(Integer.toString(walkRange), 12);
        infoGP.add(walkrangeImV, 0,3);
        infoGP.add(walkrangeLabel, 1,3);

        ImageView costImV = getIcon("icons/icon_3.png", 40);
        Label costLabel = getLabel(Integer.toString(cost), 12);
        infoGP.add(costImV, 0,4);
        infoGP.add(costLabel, 1,4);

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
        ProgressBar healthPB = getNewProgressbar(0, "red", 90, 12);
        healthPB.progressProperty().bind(healthPercentageProperty());
        HBox healthHBox = new HBox();
        Label currentHealthLabel = getLabel("", 10);
        healthHBox.setAlignment(Pos.CENTER_LEFT);
        currentHealthLabel.textProperty().bind(healthProperty().asString());
        Label maxHealthLabel = getLabel("/"+maxHealth, 10);
        healthHBox.getChildren().addAll(healthPB, currentHealthLabel, maxHealthLabel);
        infoGP.add(healthImV, 0,1);
        infoGP.add(healthHBox, 1,1);

        ImageView closeDamageEffImV = getIcon("icons/icon_25.png", 25);
        Label closeDamageEffLabel = getLabel(getCloseEfficiency(), 10);
        infoGPLowLeft.add(closeDamageEffImV, 0,0);
        infoGPLowLeft.add(closeDamageEffLabel, 1,0);

        ImageView walkrangeImV = getIcon("icons/icon_13.png", 25);
        Label walkrangeLabel = getLabel(Integer.toString(walkRange), 10);
        infoGPLowLeft.add(walkrangeImV, 0,1);
        infoGPLowLeft.add(walkrangeLabel, 1,1);

        ImageView closeDamageImV = getIcon("icons/icon_2.png", 25);
        Label closeDamageLabel = getLabel(minCloseDamage + " - " +maxCloseDamage, 10);
        infoGPLowRight.add(closeDamageImV, 0,1);
        infoGPLowRight.add(closeDamageLabel, 1,1);

        ImageView costImV = getIcon("icons/icon_3.png", 25);
        Label costLabel = getLabel(Integer.toString(cost), 10);
        infoGPLowRight.add(costImV, 0,2);
        infoGPLowRight.add(costLabel, 1,2);

        hBox.getChildren().addAll(infoGPLowLeft, infoGPLowRight);
        vBox.getChildren().addAll(infoGP,hBox);
        return vBox;
    }
}
