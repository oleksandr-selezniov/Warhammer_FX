package Units;

import Board.Board;
import Size.Size;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import Board.LoggerUtils;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * Created by Glazyrin.D on 11/9/2016.
 */
public class MeleeInfantry extends LightInfantry {
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
    public ImageView getImageView(double opacity){
        ImageView imageView = new ImageView();
        ClassLoader classLoader = getClass().getClassLoader();
        URL urlToImage = classLoader.getResource(this.picturePath);
        Image image = new Image(urlToImage.toString(), false);
        imageView.setImage(image);
        imageView.setOpacity(opacity);
        if (Board.getScaleCoefficient() != null) {
            imageView.setFitHeight(Size.getUnitHeight() * this.heightCoeff * Board.getScaleCoefficient());
            imageView.setFitWidth(Size.getUnitWidth() * this.widthCoeff * Board.getScaleCoefficient());
        }
        else {
            imageView.setFitHeight(Size.getUnitHeight() * this.heightCoeff);
            imageView.setFitWidth(Size.getUnitWidth() * this.widthCoeff);
        }
        return imageView;
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

    private int getCloseDamage(Unit victim){
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

        ImageView cdLIImV = getIcon("icons/icon_22.png");
        ProgressBar cdAcLI = getNewProgressbar(getCurrentCloseEfficiency(new LightInfantry()), "red");
        infoGP.add(cdLIImV, 0,0);
        infoGP.add(cdAcLI, 1,0);

        ImageView cdHIImV = getIcon("icons/icon_23.png");
        ProgressBar cdAcHI = getNewProgressbar(getCurrentCloseEfficiency(new HeavyInfantry()), "red");
        infoGP.add(cdHIImV, 0,1);
        infoGP.add(cdAcHI, 1,1);

        ImageView cdVEImV = getIcon("icons/icon_24.png");
        ProgressBar cdAcVE = getNewProgressbar(getCurrentCloseEfficiency(new Vehicle()), "red");
        infoGP.add(cdVEImV, 0,2);
        infoGP.add(cdAcVE, 1,2);

        ImageView rangeDamageImV = getIcon("icons/icon_6.png");
        Label rangeDamageLabel = getLabel("N/A");
        infoGP.add(rangeDamageImV, 0,3);
        infoGP.add(rangeDamageLabel, 1,3);

        ImageView ammoIV = getIcon("icons/icon_12.png");
        HBox ammoHBox = new HBox();
        ammoHBox.setAlignment(Pos.CENTER_LEFT);
        Label maxAmmoLabel = getLabel("N/A");
        ammoHBox.getChildren().addAll(maxAmmoLabel);
        infoGP.add(ammoIV, 0,4);
        infoGP.add(ammoHBox, 1,4);

        return infoGP;
    }

    public GridPane getLeftInfoGridPane(){
        GridPane infoGP = new GridPane();
        infoGP.setVgap(1);
        infoGP.setHgap(1);

        ImageView nameImV = getIcon("icons/icon_14.png");
        Label nameLabel = getLabel(name);
        infoGP.add(nameImV, 0,0);
        infoGP.add(nameLabel, 1,0);

        ImageView healthImV = getIcon("icons/icon_7.png");
        HBox healthHBox = new HBox();
        healthHBox.setAlignment(Pos.CENTER_LEFT);
        Label maxHealthLabel = getLabel(Integer.toString(maxHealth));
        healthHBox.getChildren().addAll(maxHealthLabel);
        infoGP.add(healthImV, 0,1);
        infoGP.add(healthHBox, 1,1);

        ImageView closeDamageImV = getIcon("icons/icon_2.png");
        Label closeDamageLabel = getLabel(minCloseDamage + " - " +maxCloseDamage);
        infoGP.add(closeDamageImV, 0,2);
        infoGP.add(closeDamageLabel, 1,2);

        ImageView walkrangeImV = getIcon("icons/icon_13.png");
        Label walkrangeLabel = getLabel(Integer.toString(walkRange));
        infoGP.add(walkrangeImV, 0,3);
        infoGP.add(walkrangeLabel, 1,3);

        ImageView costImV = getIcon("icons/icon_3.png");
        Label costLabel = getLabel(Integer.toString(cost));
        infoGP.add(costImV, 0,4);
        infoGP.add(costLabel, 1,4);

        return infoGP;
    }
}
