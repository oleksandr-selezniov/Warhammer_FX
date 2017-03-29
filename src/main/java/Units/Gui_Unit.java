package Units;

import Board.Board;
import Board.Utils.BoardUtils;
import Size.Size;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.Serializable;
import java.net.URL;
import java.util.*;

/**
 * Created by Dmitriy on 17.04.2016.
 */
public class Gui_Unit extends Unit implements Serializable{

    private ImageView unitImageView;
  //  public Gui_Unit(){}

    public Gui_Unit(){}

//    public Gui_Unit(Unit unit){
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
//        unitImageView = getImageView(1.0);
//    }


    public void setUnitImageView(ImageView unitImageView) {
        this.unitImageView = unitImageView;
    }
    public ImageView getUnitImageView() {
        return unitImageView;
    }

    public Image getImage() {
        ClassLoader classLoader = getClass().getClassLoader();
        URL urlToImage = classLoader.getResource(this.picturePath);
        return new Image(urlToImage.toString(), false);
    }

    public  ImageView getImageView(double opacity, double scaleCoeff){
        ImageView imageView = getBasicImageView(opacity);
        imageView.setFitHeight(Size.getUnitHeight() * this.heightCoeff * scaleCoeff);
        imageView.setFitWidth(Size.getUnitWidth() * this.widthCoeff * scaleCoeff);
        return imageView;
    }

    public ImageView getImageView(double opacity){
        ImageView imageView = getBasicImageView(opacity);
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

    protected ImageView getBasicImageView(double opacity){
        ImageView imageView = new ImageView();
        ClassLoader classLoader = getClass().getClassLoader();
        URL urlToImage = classLoader.getResource(this.picturePath);
        Image image = new Image(urlToImage.toString(), false);
        imageView.setImage(image);
        imageView.setOpacity(opacity);
        return  imageView;
    }

    public GridPane getRightInfoGridPane(){
        GridPane infoGP = new GridPane();
        infoGP.setVgap(1);
        infoGP.setHgap(1);

        ImageView acLIImV = getIcon("icons/icon_16.png", 40);
        ProgressBar pbAcLI = getNewProgressbar(getCurrentAccuracy(new LightInfantry()), "red", 140, 20);
        infoGP.add(acLIImV, 0,0);
        infoGP.add(pbAcLI, 1,0);

        ImageView acHIImV = getIcon("icons/icon_17.png", 40);
        ProgressBar pbAcHI = getNewProgressbar(getCurrentAccuracy(new HeavyInfantry()), "red", 140, 20);
        infoGP.add(acHIImV, 0,1);
        infoGP.add(pbAcHI, 1,1);

        ImageView acVEImV = getIcon("icons/icon_18.png", 40);
        ProgressBar pbAcVE = getNewProgressbar(getCurrentAccuracy(new Vehicle()), "red", 140, 20);
        infoGP.add(acVEImV, 0,2);
        infoGP.add(pbAcVE, 1,2);

        ImageView efLIImV = getIcon("icons/icon_19.png", 40);
        ProgressBar pbEfLI = getNewProgressbar(getCurrentRangeEfficiency(new LightInfantry()), "red", 140, 20);
        infoGP.add(efLIImV, 0,3);
        infoGP.add(pbEfLI, 1,3);

        ImageView efHIImV = getIcon("icons/icon_20.png", 40);
        ProgressBar pbEfHI = getNewProgressbar(getCurrentRangeEfficiency(new HeavyInfantry()), "red", 140, 20);
        infoGP.add(efHIImV, 0,4);
        infoGP.add(pbEfHI, 1,4);

        ImageView efVEImV = getIcon("icons/icon_21.png", 40);
        ProgressBar pbEfVE = getNewProgressbar(getCurrentRangeEfficiency(new Vehicle()), "red", 140, 20);
        infoGP.add(efVEImV, 0,5);
        infoGP.add(pbEfVE, 1,5);

        ImageView closeDamageImV = getIcon("icons/icon_25.png", 40);
        Label closeDamageLabel = getLabel("CONSTANT", 12);
        infoGP.add(closeDamageImV, 0,6);
        infoGP.add(closeDamageLabel, 1,6);

        return infoGP;
    }

    public GridPane getLeftInfoGridPane(){
        GridPane infoGP = new GridPane();
        infoGP.setVgap(1);
        infoGP.setHgap(1);

        ImageView nameImV = getIcon("icons/icon_14.png", 40);
        Label nameLabel = getLabel(name, 12);
        infoGP.add(nameImV, 0,0);
        infoGP.add(nameLabel, 1,0);

        ImageView healthImV = getIcon("icons/icon_7.png", 40);
        HBox healthHBox = new HBox();
        healthHBox.setAlignment(Pos.CENTER_LEFT);
        Label maxHealthLabel = getLabel(Integer.toString(maxHealth), 12);
        healthHBox.getChildren().addAll(maxHealthLabel);
        infoGP.add(healthImV, 0,1);
        infoGP.add(healthHBox, 1,1);

        ImageView ammoIV = getIcon("icons/icon_12.png", 40);
        HBox ammoHBox = new HBox();
        ammoHBox.setAlignment(Pos.CENTER_LEFT);
        Label maxAmmoLabel = getLabel(Integer.toString(maxAmmo), 12);
        ammoHBox.getChildren().addAll(maxAmmoLabel);
        infoGP.add(ammoIV, 0,2);
        infoGP.add(ammoHBox, 1,2);

        ImageView rangeDamageImV = getIcon("icons/icon_6.png", 40);
        Label rangeDamageLabel = getLabel(minRangeDamage + " - " +maxRangeDamage, 12);
        infoGP.add(rangeDamageImV, 0,3);
        infoGP.add(rangeDamageLabel, 1,3);

        ImageView closeDamageImV = getIcon("icons/icon_2.png", 40);
        Label closeDamageLabel = getLabel(minCloseDamage + " - " +maxCloseDamage, 12);
        infoGP.add(closeDamageImV, 0,4);
        infoGP.add(closeDamageLabel, 1,4);

        ImageView walkrangeImV = getIcon("icons/icon_13.png", 40);
        Label walkrangeLabel = getLabel(Integer.toString(walkRange), 12);
        infoGP.add(walkrangeImV, 0,5);
        infoGP.add(walkrangeLabel, 1,5);

        ImageView shotRangeImV = getIcon("icons/icon_5.png", 40);
        Label shotRangeLabel = getLabel(Integer.toString(shotRange), 12);
        infoGP.add(shotRangeImV, 0,6);
        infoGP.add(shotRangeLabel, 1,6);

        ImageView costImV = getIcon("icons/icon_3.png", 40);
        Label costLabel = getLabel(Integer.toString(cost), 12);
        infoGP.add(costImV, 0,7);
        infoGP.add(costLabel, 1,7);

        return infoGP;
    }

    public VBox getBattleInfoGridPane(){
        HBox hBox = new HBox();
        VBox vBox = new VBox();
        GridPane infoGP = new GridPane();
        infoGP.setVgap(1);
        infoGP.setHgap(1);

        GridPane infoGPLowLeft = new GridPane();
        infoGPLowLeft.setMinWidth(60);
        infoGPLowLeft.setAlignment(Pos.CENTER);
        infoGPLowLeft.setVgap(1);
        infoGPLowLeft.setHgap(1);

        GridPane infoGPLowRight = new GridPane();
        infoGPLowRight.setMinWidth(60);
        infoGPLowRight.setAlignment(Pos.CENTER);
        infoGPLowRight.setVgap(1);
        infoGPLowRight.setHgap(1);

        ImageView nameImV = getIcon("icons/icon_14.png", 25);
        Label nameLabel = getLabel(name, 10);
        infoGP.add(nameImV, 0,0);
        infoGP.add(nameLabel, 1,0);

        ImageView healthImV = getIcon("icons/icon_7.png", 25);
        ProgressBar healthPB = getNewProgressbar(0, "red", 90, 12);
        healthPB.progressProperty().bind(healthPercentageProperty());
        HBox healthHBox = new HBox();
        Label currentHealthLabel = getLabel("", 10);
        healthHBox.setAlignment(Pos.CENTER_LEFT);
        currentHealthLabel.textProperty().bind(healthProperty().asString());
        Label maxHealthLabel = getLabel("/"+maxHealth, 10);
        healthHBox.getChildren().addAll(healthPB, currentHealthLabel, maxHealthLabel);
        infoGP.add(healthImV, 0,1);
        infoGP.add(healthHBox, 1,1);

        ImageView ammoIV = getIcon("icons/icon_12.png", 25);
        ProgressBar ammoPB = getNewProgressbar(0, "orange", 90, 12);
        ammoPB.progressProperty().bind(ammoPercentageProperty());
        HBox ammoHBox = new HBox();
        Label currentAmmoLabel = getLabel("", 10);
        ammoHBox.setAlignment(Pos.CENTER_LEFT);
        currentAmmoLabel.textProperty().bind(ammoProperty().asString());
        Label maxAmmoLabel = getLabel("/"+maxAmmo, 10);
        ammoHBox.getChildren().addAll(ammoPB, currentAmmoLabel, maxAmmoLabel);
        infoGP.add(ammoIV, 0,2);
        infoGP.add(ammoHBox, 1,2);

        ImageView rangeDamageEffImV = getIcon("icons/icon_26.png", 25);
        Label rangeDamageEffLabel = getLabel(getRangeEfficiency(), 10);
        infoGPLowLeft.add(rangeDamageEffImV, 0,0);
        infoGPLowLeft.add(rangeDamageEffLabel, 1,0);

        ImageView accuracyImV = getIcon("icons/icon_27.png", 25);
        Label accuracyLabel = getLabel(getAccuracy(), 10);
        infoGPLowLeft.add(accuracyImV, 0,1);
        infoGPLowLeft.add(accuracyLabel, 1,1);

        ImageView walkrangeImV = getIcon("icons/icon_13.png", 25);
        Label walkrangeLabel = getLabel(Integer.toString(walkRange), 10);
        infoGPLowLeft.add(walkrangeImV, 0,2);
        infoGPLowLeft.add(walkrangeLabel, 1,2);

        ImageView shotRangeImV = getIcon("icons/icon_5.png", 25);
        Label shotRangeLabel = getLabel(Integer.toString(shotRange), 10);
        infoGPLowLeft.add(shotRangeImV, 0,3);
        infoGPLowLeft.add(shotRangeLabel, 1,3);

        ImageView rangeDamageImV = getIcon("icons/icon_6.png", 25);
        Label rangeDamageLabel = getLabel(minRangeDamage + " - " +maxRangeDamage, 10);
        infoGPLowRight.add(rangeDamageImV, 0,0);
        infoGPLowRight.add(rangeDamageLabel, 1,0);

        ImageView closeDamageImV = getIcon("icons/icon_2.png", 25);
        Label closeDamageLabel = getLabel(minCloseDamage + " - " +maxCloseDamage, 10);
        infoGPLowRight.add(closeDamageImV, 0,1);
        infoGPLowRight.add(closeDamageLabel, 1,1);

        ImageView costImV = getIcon("icons/icon_3.png", 25);
        Label costLabel = getLabel(Integer.toString(cost), 10);
        infoGPLowRight.add(costImV, 0,2);
        infoGPLowRight.add(costLabel, 1,2);

        hBox.getChildren().addAll(infoGPLowLeft, infoGPLowRight);
        vBox.getChildren().addAll(infoGP,hBox);
        return vBox;
    }

    protected ProgressBar getNewProgressbar(double progress, String colour, int width, int height){
        ProgressBar pb1 = new ProgressBar();
        pb1.setMinSize(width, height);
        pb1.setMaxSize(width, height);
        pb1.setProgress(progress);
        pb1.setStyle( "-fx-text-box-border: forestgreen; -fx-accent: "+colour);
        pb1.setEffect(new Glow());
        return pb1;
    }

    protected ImageView getIcon(String path, int size){
        BoardUtils boardUtils = new BoardUtils();
        ImageView healthImV = new ImageView(boardUtils.getImage(path));
        healthImV.setFitHeight(size);
        healthImV.setFitWidth(size);
        return healthImV;
    }

    protected Label getLabel(String text, int fontSize){
        Label nameLabel = new Label(text);
        nameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, fontSize));
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

    public static Map<String, Gui_Unit> getRaceUnitMap(String race){
        int team = (race.equals("Humans")? 1:2);
        Map<String, Gui_Unit> unitMap= new HashMap<>();

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

