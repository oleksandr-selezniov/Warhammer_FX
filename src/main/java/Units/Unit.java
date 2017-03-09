package Units;

import Units.Enums.UnitClassNames;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;

/**
 * Created by Glazyrin.D on 2/20/2017.
 */
public class Unit {
    UnitClassNames unitClassName;
    String name;
    String key;
    String accuracy;
    String rangeEfficiency;
    int maxHealth;
    int maxAmmo;
    int minRangeDamage;
    int maxRangeDamage;
    int minCloseDamage;
    int maxCloseDamage;
    int walkRange;
    int shotRange;
    int team;
    int cost;
    double armor;
    double heightCoeff;
    double widthCoeff;
    boolean isActive;
    SimpleIntegerProperty health = new SimpleIntegerProperty(0);
    SimpleIntegerProperty ammo = new SimpleIntegerProperty(0);
    SimpleDoubleProperty healthPercentage = new SimpleDoubleProperty(0);
    SimpleDoubleProperty ammoPercentage = new SimpleDoubleProperty(0);
    Insets insetsY;
    String picturePath;

    public Unit(){}

//    public Unit(Gui_Unit unit){
//        this.unitClassName = unit.getUnitClassName();
//        this.name = unit.getName();
//        this.key = unit.getKey();
//        this.accuracy = unit.getAccuracy();
//        this.rangeEfficiency = getRangeEfficiency();
//        this.maxHealth = unit.getMaxHealth();
//        this.maxAmmo = unit.getMaxAmmo();
//        this.minRangeDamage = unit.getMinRangeDamage();
//        this.maxRangeDamage = unit.getMaxRangeDamage();
//        this.minCloseDamage = unit.getMinCloseDamage();
//        this.maxCloseDamage = unit.getMaxCloseDamage();
//        this.walkRange = unit.getWalkRange();
//        this.shotRange = unit.getShotRange();
//        this.team = unit.getTeam();
//        this.cost = unit.getCost();
//        this.armor = unit.getArmor();
//        this.heightCoeff = unit.getHeightCoeff();
//        this.widthCoeff = unit.getWidthCoeff();
//        this.isActive = unit.isActive();
//        this.health = unit.healthProperty();
//        this.ammo = unit.ammoProperty();
//        this.healthPercentage = unit.healthPercentageProperty();
//        this.ammoPercentage = unit.ammoPercentageProperty();
//        this.insetsY = unit.getInsetsY();
//        this.picturePath = unit.getPicturePath();
//    }

    public String getInfo(){
        return "Name: "+name+"\n"+"Efficiency "+rangeEfficiency+"\n"+"Health "+getHealth()+"/"+maxHealth+"\n"+"Accuracy "+accuracy+
                "\n"+"Ammo "+getAmmo()+"/"+maxAmmo+"\n"+"Close Damage "+ minCloseDamage +"-"+maxCloseDamage +"\n"
                + "Range Damage "+ minRangeDamage +"-"+ maxRangeDamage +"\n"+"Walk range "+walkRange+"\n"+"ShotRange "+shotRange+"\n"
                + "Cost: " +cost;
    }

    public boolean isEnemyUnit(Gui_Unit guiUnit){
        return this.getTeam() != guiUnit.getTeam();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Insets getInsetsY(){
        return insetsY;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

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

    public void performRangeAttack(Gui_Unit victim){}

    public void performCloseAttack(Gui_Unit victim){}

    public int getMaxRangeDamage() {
        return maxRangeDamage;
    }

    public void setMaxRangeDamage(int maxRangeDamage) {
        this.maxRangeDamage = maxRangeDamage;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
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

    public String getRangeEfficiency() {
        return rangeEfficiency;
    }

    public void setRangeEfficiency(String rangeEfficiency) {
        this.rangeEfficiency = rangeEfficiency;
    }

    public int getHealth() {
        return health.get();
    }

    public void setHealth(double currentHealth) {
        this.health.set((int)currentHealth);
        if(maxHealth==0) maxHealth = (int)currentHealth;
        this.healthPercentage.set(currentHealth/maxHealth);
    }

    public SimpleIntegerProperty  healthProperty() {
        return health;
    }

    public int getAmmo() {
        return ammo.get();
    }

    public void setAmmo(double ammo) {
        this.ammo.set((int)ammo);
        if(maxAmmo==0) maxAmmo = (int)ammo;
        this.ammoPercentage.set(ammo/maxAmmo);
    }

    public SimpleIntegerProperty  ammoProperty() {
        return ammo;
    }

    public SimpleDoubleProperty healthPercentageProperty(){
        return healthPercentage;
    }

    public SimpleDoubleProperty ammoPercentageProperty(){
        return ammoPercentage;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public UnitClassNames getUnitClassName() {
        return unitClassName;
    }

    public void setUnitClassName(UnitClassNames unitClassName) {
        this.unitClassName = unitClassName;
    }

    public String getKey() {
        return key;
    }

    public double getCurrentRangeEfficiency(Gui_Unit victim){
        String effString = getRangeEfficiency();
        String tokens = "/";
        String[] splited = effString.split(tokens);

        double LE = Double.parseDouble(splited[0]);
        double HE = Double.parseDouble(splited[1]);
        double VE = Double.parseDouble(splited[2]);

        if(victim instanceof MeleeInfantry){return LE;}
        if(victim instanceof LightInfantry){return LE;}
        if(victim instanceof HeavyInfantry){return HE;}
        if(victim instanceof Vehicle){return VE;}
        return 0;
    }

    public double getCurrentAccuracy(Gui_Unit victim){
        String effString = getAccuracy();
        String tokens = "/";
        String[] splited = effString.split(tokens);

        double LA = Double.parseDouble(splited[0]);
        double HA = Double.parseDouble(splited[1]);
        double VA = Double.parseDouble(splited[2]);

        if(victim instanceof MeleeInfantry){return LA;}
        if(victim instanceof LightInfantry){return LA;}
        if(victim instanceof HeavyInfantry){return HA;}
        if(victim instanceof Vehicle){return VA;}
        return 0;
    }
}


