package Units;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.geometry.Insets;

/**
 * Created by Dmitriy on 02.11.2016.
 */
public class Vehicle extends Unit {
    private static Locale locale = new Locale("en", "US");
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("Vehicles", locale);
    private Insets insets = new Insets(2,2,80,2);

    public Vehicle(String name, int team){
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
    public javafx.geometry.Insets getInsets() {
        return insets;
    }
}

