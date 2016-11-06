package Units;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.MalformedURLException;

/**
 * Created by Dmitriy on 17.04.2016.
 */
public abstract class Unit {

    protected String name;
    protected int maxHealth;
    protected int maxAmmo;
    protected int health;
    protected int ammo;
    protected double armor;
    protected int minRangeDamage;
    protected int maxRangeDamage;
    protected Double accuracy;
    protected int minCloseDamage;
    protected int maxCloseDamage;
    protected int walkRange;
    protected int shotRange;
    protected String picturePath;
    protected int team;
    protected double heightCoeff;
    protected double widthCoeff;
    protected ImageView unitImageView;
    protected boolean isActive;

    public Unit(){}

    public boolean isEnemyUnit(Unit unit){
        if(this.getTeam()!=unit.getTeam()){
            return true;
        }
        return false;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public Image getImage() {
        String imageUrl = null;
        try {
            File file = new File(picturePath);
            imageUrl = file.toURI().toURL().toString();
        }catch (MalformedURLException e){e.printStackTrace();}
        return new Image(imageUrl, false);
    }

    public abstract ImageView getImageView(Double opacity);

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public double getArmor() {
        return armor;
    }

    public void setArmor(double armor) {
        this.armor = armor;
    }

    public int getMinCloseDamage() {
        return minCloseDamage;
    }

    public void setMinCloseDamage(int minCloseDamage) {
        this.minCloseDamage = minCloseDamage;
    }

    public int getWalkRange() {
        return walkRange;
    }

    public void setWalkRange(int walkRange) {
        this.walkRange = walkRange;
    }

    public int getShotRange() {
        return shotRange;
    }

    public void setShotRange(int shotRange) {
        this.shotRange = shotRange;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public int getMaxHealth() {return maxHealth;}

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public int getMinRangeDamage() {
        return minRangeDamage;
    }

    public ImageView getUnitImageView() {
        return unitImageView;
    }

    public void setUnitImageView(ImageView unitImageView) {
        this.unitImageView = unitImageView;
    }

    public abstract Insets getInsets();

    public double getWidthCoeff() {
        return widthCoeff;
    }

    public void setWidthCoeff(double widthCoeff) {
        this.widthCoeff = widthCoeff;
    }

    public double getHeightCoeff() {
        return heightCoeff;
    }

    public void setHeightCoeff(double heightCoeff) {
        this.heightCoeff = heightCoeff;
    }

    public boolean isActive() {
        return isActive;
    }

    public abstract void performRangeAttack(Unit victim);

    public abstract void performCloseAttack(Unit victim);

    public int getMaxRangeDamage() {
        return maxRangeDamage;
    }

    public void setMaxRangeDamage(int maxRangeDamage) {
        this.maxRangeDamage = maxRangeDamage;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getMaxCloseDamage() {
        return maxCloseDamage;
    }

    public void setMaxCloseDamage(int maxCloseDamage) {
        this.maxCloseDamage = maxCloseDamage;
    }

    public String getInfo(){
        String unitInfo = "Name: "+name+"\n"+"Armor "+armor+"\n"+"Health "+health+"/"+maxHealth+"\n"+"Accuracy "+accuracy+
                "\n"+"Ammo "+ammo+"/"+maxAmmo+"\n"+"Close Damage "+ minCloseDamage +"-"+maxCloseDamage +"\n"
                + "Range Damage "+ minRangeDamage +"-"+ maxRangeDamage +"\n"+"Walk range "+walkRange+"\n"+"ShotRange "+shotRange+"\n";
        return unitInfo;
    }
}
