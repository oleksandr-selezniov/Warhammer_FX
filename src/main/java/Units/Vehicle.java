package Units;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import Size.Size;
import Board.Board;
import Units.Interfaces.RangeUnit;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import Board.LoggerUtils;

/**
 * Created by Dmitriy on 02.11.2016.
 */
public class Vehicle extends Unit implements RangeUnit {
    private static Locale locale = new Locale("en", "US");
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("Vehicles", locale);
    private double insetsY = 80;
    private double insetsX = 50;
    public Vehicle(){}

    public Vehicle(String unitName, int team){
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
    public ImageView getImageView(double opacity){
        ImageView imageView = new ImageView();
        ClassLoader classLoader = getClass().getClassLoader();
        URL urlToImage = classLoader.getResource(this.picturePath);
        Image image = new Image(urlToImage.toString(), false);
        imageView.setImage(image);
        imageView.setOpacity(opacity);
        if (Board.getScaleCoefficient() != null) {
            imageView.setFitHeight(Size.getCellSize() * this.heightCoeff * Board.getScaleCoefficient());
            imageView.setFitWidth(Size.getCellSize() * this.widthCoeff * Board.getScaleCoefficient());
        }
        else {
            imageView.setFitHeight(Size.getCellSize() * this.heightCoeff);
            imageView.setFitWidth(Size.getCellSize() * this.widthCoeff);
        }
        return imageView;
    }

    @Override
    public ImageView getImageView(double opacity, double scaleCoeff){
        ImageView imageView = new ImageView();
        ClassLoader classLoader = getClass().getClassLoader();
        URL urlToImage = classLoader.getResource(this.picturePath);
        Image image = new Image(urlToImage.toString(), false);
        imageView.setImage(image);
        imageView.setOpacity(opacity);
        imageView.setFitHeight(Size.getCellSize() * this.heightCoeff * scaleCoeff * 0.7);
        imageView.setFitWidth(Size.getCellSize() * this.widthCoeff * scaleCoeff * 0.7);
        return imageView;
    }

    public javafx.geometry.Insets getInsetsY() {
        if(Board.getScaleCoefficient()!=null){
            return new Insets(2,2,Board.getScaleCoefficient()* insetsY,-Board.getScaleCoefficient()*insetsX);
        }
        return new Insets(2,2,insetsY,2);
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

    public int getRangeDamage(Unit victim){
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

    public int getCloseDamage(){
        return minCloseDamage + (int)(Math.random() * ((maxCloseDamage - minCloseDamage) + 1));
    }
}
