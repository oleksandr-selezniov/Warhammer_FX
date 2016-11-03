package Units;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Dmitriy on 03.11.2016.
 */
public class LightInfantry extends Unit {
    private static Locale locale = new Locale("en", "US");
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("LightInfantry", locale);

    public LightInfantry(String name, int team){
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
}
