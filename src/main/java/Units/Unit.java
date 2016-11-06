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
    protected int maxMorale;
    protected int maxAmmo;
    protected int health;
    protected int morale;
    protected int ammo;
    protected double armor;
    protected int rangeDamage;
    protected int closeDamage;
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

    public int getMorale() {
        return morale;
    }

    public void setMorale(int morale) {
        this.morale = morale;
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

    public int getCloseDamage() {
        return closeDamage;
    }

    public void setCloseDamage(int closeDamage) {
        this.closeDamage = closeDamage;
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

    public int getMaxMorale() {
        return maxMorale;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public int getRangeDamage() {
        return rangeDamage;
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

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getInfo(){
        String unitInfo = "Name: "+name+"\n"+"Armor "+armor+"\n"+"Health "+health+"/"+maxHealth+"\n"+"Morale "+morale+
                "/"+maxMorale+"\n"+"Ammo "+ammo+"/"+maxAmmo+"\n"+"Close Damage "+closeDamage+"\n"+ "Range Damage "+
                rangeDamage+"\n"+"Walk range "+walkRange+"\n"+"ShotRange "+shotRange+"\n";
        return unitInfo;
    }
}
