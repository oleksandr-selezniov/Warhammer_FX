package Units;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Locale;
import java.util.ResourceBundle;
import Size.Size;
import Board.Board;
import javafx.geometry.Insets;

/**
 * Created by Dmitriy on 03.11.2016.
 */
public class HeavyInfantry extends Unit{
    private static Locale locale = new Locale("en", "US");
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("HeavyInfantry", locale);
    private Insets insets = new Insets(2,2,2,2);

    public HeavyInfantry(String unitName, int team){
        this.team = team;
        this.name = resourceBundle.getString(unitName+".name");
        this.health = Integer.parseInt(resourceBundle.getString(unitName+".health"));
        this.maxHealth = health;
        this.morale = Integer.parseInt(resourceBundle.getString(unitName+".morale"));
        this.maxMorale = morale;
        this.ammo = Integer.parseInt(resourceBundle.getString(unitName+".ammo"));
        this.maxAmmo = ammo;
        this.armor = Double.parseDouble(resourceBundle.getString(unitName+".armor"));
        this.rangeDamage = Integer.parseInt(resourceBundle.getString(unitName+".rangeDamage"));
        this.closeDamage = Integer.parseInt(resourceBundle.getString(unitName+".closeDamage"));
        this.walkRange = Integer.parseInt(resourceBundle.getString(unitName+".walkRange"));
        this.shotRange = Integer.parseInt(resourceBundle.getString(unitName+".shotRange"));
        this.picturePath = resourceBundle.getString(unitName+".picturePath");
        this.heightCoeff = 1;
        this.widthCoeff = 1;
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
}
