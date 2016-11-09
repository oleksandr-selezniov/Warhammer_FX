package Units;

import Board.Board;
import Size.Size;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Locale;
import java.util.ResourceBundle;
import Board.LoggerUtils;

/**
 * Created by Glazyrin.D on 11/9/2016.
 */
public class Animal extends LightInfantry {
    private static Locale locale = new Locale("en", "US");
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("Animals", locale);
    private Insets insets = new Insets(2,2,2,2);
    private String closeEfficiency;

    public Animal(String unitName, int team){
        this.team = team;
        this.name = resourceBundle.getString(unitName+".name");
        this.health = Integer.parseInt(resourceBundle.getString(unitName+".health"));
        this.maxHealth = health;
        this.ammo = Integer.parseInt(resourceBundle.getString(unitName+".ammo"));
        this.maxAmmo = ammo;
        this.armor = Double.parseDouble(resourceBundle.getString(unitName+".armor"));
//        this.minRangeDamage = Integer.parseInt(resourceBundle.getString(unitName+".minRangeDamage"));
//        this.maxRangeDamage = Integer.parseInt(resourceBundle.getString(unitName+".maxRangeDamage"));
//        this.accuracy = resourceBundle.getString(unitName+".accuracy");
        this.minCloseDamage = Integer.parseInt(resourceBundle.getString(unitName+".minCloseDamage"));
        this.maxCloseDamage = Integer.parseInt(resourceBundle.getString(unitName+".maxCloseDamage"));
        this.walkRange = Integer.parseInt(resourceBundle.getString(unitName+".walkRange"));
//        this.shotRange = Integer.parseInt(resourceBundle.getString(unitName+".shotRange"));
        this.picturePath = resourceBundle.getString(unitName+".picturePath");
        this.heightCoeff = 1;
        this.widthCoeff = 1;
        this.isActive = false;
        this.closeEfficiency = resourceBundle.getString(unitName+".closeEfficiency");
        this.setUnitImageView(getImageView(1.0));

        try {
            this.heightCoeff = Double.parseDouble(resourceBundle.getString(unitName+".height"));
            this.widthCoeff = Double.parseDouble(resourceBundle.getString(unitName+".width"));
        }catch (Exception e){
            System.out.println("INFO: some resources were not present");
        }
    }

    @Override
    public ImageView getImageView(Double opacity){
        String imageUrl = null;
        ImageView imageView = new ImageView();
        try {
            File file = new File(this.picturePath);
            imageUrl = file.toURI().toURL().toString();
        }catch (MalformedURLException e){e.printStackTrace();}
        Image image = new Image(imageUrl, false);
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
    public javafx.geometry.Insets getInsets() {
        return insets;
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

}
