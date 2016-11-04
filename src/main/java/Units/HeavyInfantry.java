package Units;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Locale;
import java.util.ResourceBundle;
import Size.Size;
import javafx.geometry.Insets;

/**
 * Created by Dmitriy on 03.11.2016.
 */
public class HeavyInfantry extends Unit{
    private static Locale locale = new Locale("en", "US");
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("HeavyInfantry", locale);
    private Insets insets = new Insets(2,2,2,2);

    public HeavyInfantry(String name, int team){
        this.team = team;
        this.name = resourceBundle.getString(name+".name");
        this.health = Integer.parseInt(resourceBundle.getString(name+".health"));
        this.maxHealth = health;
        this.morale = Integer.parseInt(resourceBundle.getString(name+".morale"));
        this.maxMorale = morale;
        this.ammo = Integer.parseInt(resourceBundle.getString(name+".ammo"));
        this.maxAmmo = ammo;
        this.armor = Double.parseDouble(resourceBundle.getString(name+".armor"));
        this.rangeDamage = Integer.parseInt(resourceBundle.getString(name+".rangeDamage"));
        this.closeDamage = Integer.parseInt(resourceBundle.getString(name+".closeDamage"));
        this.walkRange = Integer.parseInt(resourceBundle.getString(name+".walkRange"));
        this.shotRange = Integer.parseInt(resourceBundle.getString(name+".shotRange"));
        this.picturePath = resourceBundle.getString(name+".picturePath");
    }

    @Override
    public ImageView getImageView(){
        String imageUrl = null;
        ImageView imageView = new ImageView();
        try {
            File file = new File(this.picturePath);
            imageUrl = file.toURI().toURL().toString();
        }catch (MalformedURLException e){e.printStackTrace();}
        Image image = new Image(imageUrl, false);
        imageView.setImage(image);
        imageView.setFitHeight(Size.getUnitHeight());
        imageView.setFitWidth(Size.getUnitWidth());
        return imageView;
    }

    @Override
    public javafx.geometry.Insets getInsets() {
        return insets;
    }
}
