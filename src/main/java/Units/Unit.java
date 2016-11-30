package Units;

import Size.Size;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.net.URL;
import java.util.*;

/**
 * Created by Dmitriy on 17.04.2016.
 */
public abstract class Unit implements Serializable{

    String name;
    String key;
    String accuracy;
    String rangeEfficiency;
    String picturePath;
    int maxHealth;
    int maxAmmo;
    int health;
    int ammo;
    int minRangeDamage;
    int maxRangeDamage;
    int minCloseDamage;
    int maxCloseDamage;
    int walkRange;
    int shotRange;
    int team;
    double armor;
    double heightCoeff;
    double widthCoeff;
    boolean isActive;
    private ImageView unitImageView;

    public Unit(){}

    public boolean isEnemyUnit(Unit unit){
        return this.getTeam() != unit.getTeam();
    }

    public Image getImage() {
        ClassLoader classLoader = getClass().getClassLoader();
        URL urlToImage = classLoader.getResource(this.picturePath);
        return new Image(urlToImage.toString(), false);
    }

    public String getPicturePath() {
        return picturePath;
    }

    public abstract ImageView getImageView(double opacity);

    public  ImageView getImageView(double opacity, double scaleCoeff){
        ImageView imageView = new ImageView();
        ClassLoader classLoader = getClass().getClassLoader();
        URL urlToImage = classLoader.getResource(this.picturePath);
        Image image = new Image(urlToImage.toString(), false);
        imageView.setImage(image);
        imageView.setOpacity(opacity);
        imageView.setFitHeight(Size.getUnitHeight() * this.heightCoeff * scaleCoeff);
        imageView.setFitWidth(Size.getUnitWidth() * this.widthCoeff * scaleCoeff);
        return imageView;
    }

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

    public abstract Insets getInsetsY();

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


    public String getKey() {
        return key;
    }

    protected double getCurrentRangeEfficiency(Unit victim){
        String effString = getRangeEfficiency();
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

    protected double getCurrentAccuracy(Unit victim){
        String effString = getAccuracy();
        String tokens = "/";
        String[] splited = effString.split(tokens);

        double LA = Double.parseDouble(splited[0]);
        double HA = Double.parseDouble(splited[1]);
        double VA = Double.parseDouble(splited[2]);

        if(victim instanceof LightInfantry){return LA;}
        if(victim instanceof HeavyInfantry){return HA;}
        if(victim instanceof Vehicle){return VA;}
        return 0.5;
    }

    public String getInfo(){
        return "Name: "+name+"\n"+"Efficiency "+rangeEfficiency+"\n"+"Health "+health+"/"+maxHealth+"\n"+"Accuracy "+accuracy+
                "\n"+"Ammo "+ammo+"/"+maxAmmo+"\n"+"Close Damage "+ minCloseDamage +"-"+maxCloseDamage +"\n"
                + "Range Damage "+ minRangeDamage +"-"+ maxRangeDamage +"\n"+"Walk range "+walkRange+"\n"+"ShotRange "+shotRange+"\n";
    }

    public static List getUnitNamesList(String race){
        ArrayList<String> arrayList= new ArrayList<>();

        arrayList.addAll(getNameListBySpeciality(race,"HeavyInfantry"));
        arrayList.addAll(getNameListBySpeciality(race,"LightInfantry"));
        arrayList.addAll(getNameListBySpeciality(race,"MeleeInfantry"));
        arrayList.addAll(getNameListBySpeciality(race,"Vehicle"));
        arrayList.addAll(getNameListBySpeciality(race,"Artillery"));

        return arrayList;
    }

    private static ArrayList<String> getNameListBySpeciality(String race, String speciality){
        ArrayList<String> arrayList= new ArrayList<>();
        Locale locale = new Locale("en", "US");
        ResourceBundle resourceBundle = ResourceBundle.getBundle("RaceList", locale);
        int i = 1;
        while (resourceBundle.containsKey(race + "."+speciality+"."+i)){
            arrayList.add(resourceBundle.getString(race + "."+speciality+"."+i));
            i++;
        }
        return arrayList;
    }

    public static Map<String, Unit> getRaceUnitMap(String race){
        int team = (race.equals("Humans")? 1:2);
        Map<String, Unit> unitMap= new HashMap<>();

        ArrayList<String> arrayListHI = getNameListBySpeciality(race,"HeavyInfantry");
        arrayListHI.forEach(p->{
            unitMap.put(p, new HeavyInfantry(p, team));
        });
        ArrayList<String> arrayListLI = getNameListBySpeciality(race,"LightInfantry");
        arrayListLI.forEach(p->{
            unitMap.put(p, new LightInfantry(p, team));
        });
        ArrayList<String> arrayListMI = getNameListBySpeciality(race,"MeleeInfantry");
        arrayListMI.forEach(p->{
            unitMap.put(p, new MeleeInfantry(p, team));
        });
        ArrayList<String> arrayListVE = getNameListBySpeciality(race,"Vehicle");
        arrayListVE.forEach(p->{
            unitMap.put(p, new Vehicle(p, team));
        });
        ArrayList<String> arrayListAR = getNameListBySpeciality(race,"Artillery");
        arrayListAR.forEach(p->{
            unitMap.put(p, new Artillery(p, team));
        });
        return unitMap;
    }
}
