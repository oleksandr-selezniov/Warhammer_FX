package Units;

import Board.BoardUtils;
import Size.Size;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

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
    private ImageView unitImageView;
    private SimpleIntegerProperty health = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty ammo = new SimpleIntegerProperty(0);
    private SimpleDoubleProperty healthPercentage = new SimpleDoubleProperty(0);
    private SimpleDoubleProperty ammoPercentage = new SimpleDoubleProperty(0);

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
        return "Name: "+name+"\n"+"Efficiency "+rangeEfficiency+"\n"+"Health "+getHealth()+"/"+maxHealth+"\n"+"Accuracy "+accuracy+
                "\n"+"Ammo "+getAmmo()+"/"+maxAmmo+"\n"+"Close Damage "+ minCloseDamage +"-"+maxCloseDamage +"\n"
                + "Range Damage "+ minRangeDamage +"-"+ maxRangeDamage +"\n"+"Walk range "+walkRange+"\n"+"ShotRange "+shotRange+"\n"
                + "Cost: " +cost;
    }

    public GridPane getRightInfoGridPane(){
        GridPane infoGP = new GridPane();
        infoGP.setVgap(1);
        infoGP.setHgap(1);

        ImageView acLIImV = getIcon("icons/icon_16.png");
        ProgressBar pbAcLI = getNewProgressbar(getCurrentAccuracy(new LightInfantry()), "red");
        infoGP.add(acLIImV, 0,0);
        infoGP.add(pbAcLI, 1,0);

        ImageView acHIImV = getIcon("icons/icon_17.png");
        ProgressBar pbAcHI = getNewProgressbar(getCurrentAccuracy(new HeavyInfantry()), "red");
        infoGP.add(acHIImV, 0,1);
        infoGP.add(pbAcHI, 1,1);

        ImageView acVEImV = getIcon("icons/icon_18.png");
        ProgressBar pbAcVE = getNewProgressbar(getCurrentAccuracy(new Vehicle()), "red");
        infoGP.add(acVEImV, 0,2);
        infoGP.add(pbAcVE, 1,2);

        ImageView efLIImV = getIcon("icons/icon_19.png");
        ProgressBar pbEfLI = getNewProgressbar(getCurrentRangeEfficiency(new LightInfantry()), "red");
        infoGP.add(efLIImV, 0,3);
        infoGP.add(pbEfLI, 1,3);

        ImageView efHIImV = getIcon("icons/icon_20.png");
        ProgressBar pbEfHI = getNewProgressbar(getCurrentRangeEfficiency(new HeavyInfantry()), "red");
        infoGP.add(efHIImV, 0,4);
        infoGP.add(pbEfHI, 1,4);

        ImageView efVEImV = getIcon("icons/icon_21.png");
        ProgressBar pbEfVE = getNewProgressbar(getCurrentRangeEfficiency(new Vehicle()), "red");
        infoGP.add(efVEImV, 0,5);
        infoGP.add(pbEfVE, 1,5);

        ImageView closeDamageImV = getIcon("icons/icon_25.png");
        Label closeDamageLabel = getLabel("CONSTANT");
        infoGP.add(closeDamageImV, 0,6);
        infoGP.add(closeDamageLabel, 1,6);

        return infoGP;
    }

    public GridPane getLeftInfoGridPane(){
        GridPane infoGP = new GridPane();
        infoGP.setVgap(1);
        infoGP.setHgap(1);

        ImageView nameImV = getIcon("icons/icon_14.png");
        Label nameLabel = getLabel(name);
        infoGP.add(nameImV, 0,0);
        infoGP.add(nameLabel, 1,0);

        ImageView healthImV = getIcon("icons/icon_7.png");
        HBox healthHBox = new HBox();
        healthHBox.setAlignment(Pos.CENTER_LEFT);
        Label maxHealthLabel = getLabel(Integer.toString(maxHealth));
        healthHBox.getChildren().addAll(maxHealthLabel);
        infoGP.add(healthImV, 0,1);
        infoGP.add(healthHBox, 1,1);

        ImageView ammoIV = getIcon("icons/icon_12.png");
        HBox ammoHBox = new HBox();
        ammoHBox.setAlignment(Pos.CENTER_LEFT);
        Label maxAmmoLabel = getLabel(Integer.toString(maxAmmo));
        ammoHBox.getChildren().addAll(maxAmmoLabel);
        infoGP.add(ammoIV, 0,2);
        infoGP.add(ammoHBox, 1,2);

        ImageView rangeDamageImV = getIcon("icons/icon_6.png");
        Label rangeDamageLabel = getLabel(minRangeDamage + " - " +maxRangeDamage);
        infoGP.add(rangeDamageImV, 0,3);
        infoGP.add(rangeDamageLabel, 1,3);

        ImageView closeDamageImV = getIcon("icons/icon_2.png");
        Label closeDamageLabel = getLabel(minCloseDamage + " - " +maxCloseDamage);
        infoGP.add(closeDamageImV, 0,4);
        infoGP.add(closeDamageLabel, 1,4);

        ImageView walkrangeImV = getIcon("icons/icon_13.png");
        Label walkrangeLabel = getLabel(Integer.toString(walkRange));
        infoGP.add(walkrangeImV, 0,5);
        infoGP.add(walkrangeLabel, 1,5);

        ImageView shotRangeImV = getIcon("icons/icon_5.png");
        Label shotRangeLabel = getLabel(Integer.toString(shotRange));
        infoGP.add(shotRangeImV, 0,6);
        infoGP.add(shotRangeLabel, 1,6);

        ImageView costImV = getIcon("icons/icon_3.png");
        Label costLabel = getLabel(Integer.toString(cost));
        infoGP.add(costImV, 0,7);
        infoGP.add(costLabel, 1,7);

        return infoGP;
    }

    protected ProgressBar getNewProgressbar(double progress, String colour){
        ProgressBar pb1 = new ProgressBar();
        pb1.setMinSize(140, 20);
        pb1.setMaxSize(140, 20);
        pb1.setProgress(progress);
        pb1.setStyle( "-fx-text-box-border: forestgreen; -fx-accent: "+colour);
        pb1.setEffect(new Glow());
        return pb1;
    }

    protected ImageView getIcon(String path){
        BoardUtils boardUtils = new BoardUtils();
        ImageView healthImV = new ImageView(boardUtils.getImage(path));
        healthImV.setFitHeight(40);
        healthImV.setFitWidth(40);
        return healthImV;
    }

    protected Label getLabel(String text){
        Label nameLabel = new Label(text);
        nameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        nameLabel.setTextFill(Color.WHITE);
        return nameLabel;
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


//_____________________________________________________________________________________________________________________

//        ProgressBar healthPB = getNewProgressbar();
//        healthPB.setStyle( "-fx-text-box-border: forestgreen; -fx-accent: red");
//        healthPB.progressProperty().bind(healthPercentageProperty());
//        Label currentHealthLabel = new Label();
//        currentHealthLabel.textProperty().bind(healthProperty().asString());
//        currentHealthLabel.setTextFill(Color.WHITE);
//        currentHealthLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 10));

//_____________________________________________________________________________________________________________________

//        ProgressBar ammoPB = getNewProgressbar();
//        ammoPB.setStyle( "-fx-text-box-border: forestgreen; -fx-accent: yellow");
//        ammoPB.progressProperty().bind(ammoPercentageProperty());
//        Label currentAmmoLabel = new Label();
//        currentAmmoLabel.textProperty().bind(ammoProperty().asString());
//        currentAmmoLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
//        currentAmmoLabel.setTextFill(Color.WHITE);

//_____________________________________________________________________________________________________________________
