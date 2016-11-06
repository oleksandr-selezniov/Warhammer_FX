package Units;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Locale;
import java.util.ResourceBundle;

import Size.Size;
import Board.Board;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by Dmitriy on 03.11.2016.
 */
public class LightInfantry extends Unit {
    private static Locale locale = new Locale("en", "US");
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("LightInfantry", locale);
    private Insets insets = new Insets(2,2,2,2);

    public LightInfantry(String unitName, int team){
        this.team = team;
        this.name = resourceBundle.getString(unitName+".name");
        this.health = Integer.parseInt(resourceBundle.getString(unitName+".health"));
        this.maxHealth = health;
        this.morale = Integer.parseInt(resourceBundle.getString(unitName+".morale"));
        this.maxMorale = morale;
        this.ammo = Integer.parseInt(resourceBundle.getString(unitName+".ammo"));
        this.maxAmmo = ammo;
        this.armor = Double.parseDouble(resourceBundle.getString(unitName+".armor"));
        this.minRangeDamage = Integer.parseInt(resourceBundle.getString(unitName+".minRangeDamage"));
        this.maxRangeDamage = Integer.parseInt(resourceBundle.getString(unitName+".maxRangeDamage"));
        this.accuracy = Double.parseDouble(resourceBundle.getString(unitName+".accuracy"));
        this.closeDamage = Integer.parseInt(resourceBundle.getString(unitName+".closeDamage"));
        this.walkRange = Integer.parseInt(resourceBundle.getString(unitName+".walkRange"));
        this.shotRange = Integer.parseInt(resourceBundle.getString(unitName+".shotRange"));
        this.picturePath = resourceBundle.getString(unitName+".picturePath");
        this.heightCoeff = 1;
        this.widthCoeff = 1;
        this.isActive = false;
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
        victim.setHealth(victim.getHealth() - closeDamage);

        Board.writeToTextArea("#centerTextArea", Board.getTextFromTextArea("#centerTextArea") + "\n"
                + this.getName() + "\n"
                + "made " + (closeDamage) + "\n"
                + "melee damage to " + "\n"
                + victim.getName() + "\n", true);
        this.setActive(false);
    }

    public void performRangeAttack(Unit victim){
        int rangeDamage = getRangeDamage();
        double chance = Math.random();
        if (this.getAmmo() > 0) {
            if(chance<accuracy) {
                victim.setHealth(victim.getHealth() - rangeDamage);
                Board.writeToTextArea("#centerTextArea", Board.getTextFromTextArea("#centerTextArea") + "\n"
                        + this.getName() + "\n"
                        + "made " + (rangeDamage) + "\n"
                        + "range damage to " + "\n"
                        + victim.getName() + "\n", true);
                this.setAmmo(this.getAmmo() - 1);
                this.setActive(false);
            }else{
                Board.writeToTextArea("#centerTextArea", Board.getTextFromTextArea("#centerTextArea") + "\n"
                        + this.getName() + "\n"
                        + "missed!" + "\n", true);
                this.setAmmo(this.getAmmo() - 1);
                this.setActive(false);
            }
        } else {
            Board.writeToTextArea("#centerTextArea", Board.getTextFromTextArea("#centerTextArea") + "\n"
                    + this.getName() + "\n"
                    + "is out of ammunition!" + "\n", true);
        }
    }

    private int getRangeDamage(){
        return minRangeDamage + (int)(Math.random() * ((maxRangeDamage - minRangeDamage) + 1));
    }
}
