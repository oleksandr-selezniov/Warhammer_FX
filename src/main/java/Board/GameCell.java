package Board;

import Size.Size;
import Units.Unit;
import javafx.geometry.Insets;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.net.URL;

import static Board.Utils.BoardUtils.getStrategicalPoints;
import static Board.Utils.GameCellUtils.*;

/**
 * Created by Dmitriy on 18.10.2016.
 */
public class GameCell extends SimpleGameCell {
    private static GameCell previousGameCell;
    private static Unit temporaryUnit;
    private int imageNumber = generateRandomNumber(1,25);
 //   private String defaultCellImagePath = "cellBackground/LowRes/" + imageNumber + ".jpg";
    private static String deadCellImagePath = "other/dead.jpg";
    private Unit unit;

    public GameCell() {
        //this.setPadding(new Insets(5));
        this.setSize(Size.getCellSize());
        //this.setCellImage(defaultCellImagePath, 0.6);
        //this.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        actionMode();
        mouseMovementMode();
    }

    public GameCell(SimpleGameCell gc){
        if(gc.getSimpleUnit() != null){
            this.unit = new Unit(gc.getSimpleUnit());
        }
        if(getTemporarySimpleUnit() != null){
            temporaryUnit = new Unit(getTemporarySimpleUnit());
        }
        if(getPreviousSimpleGameCell() != null){
            previousGameCell = new GameCell(getPreviousSimpleGameCell());
        }

        isSelected = SimpleGameCell.isSelected();
        teamTurnValue = SimpleGameCell.getTeamTurnValue();
        this.name = gc.getText();
        this.imageNumber = gc.getImageNumber();
        this.xCoord = gc.getxCoord();
        this.yCoord = gc.getyCoord();
        this.isBlocked = gc.isBlocked();
        this.isPassable = gc.isPassable();
        this.isInShootingRange = gc.isInShootingRange();
        this.isSafe = gc.isSafe();
        this.isDangerous = gc.isDangerous();
        this.isStrategical = gc.isStrategical();
        this.isActivated = gc.isActivated();
        this.owner = gc.getOwner();
    }

    public void setSize(Double size){
        this.setMaxHeight(size);
        this.setMinHeight(size);
        this.setMaxWidth(size);
        this.setMinWidth(size);
    }

    public void actionMode() {
        this.setOnMouseClicked(e -> {

            if (!isSelected) { // first step
                clickOnUnitCell(this);
            } else if (this.isPassable() && this.unit == null) { // second step
                clickOnFreeCell(this);
            } else if (this.isStrategical() && this.unit == null) { // activate strategical cell
                clickOnStrategicalCell(this);
            } else if (this.unit != null && temporaryUnit.isEnemyUnit(this.unit) && isTeamTurn(temporaryUnit.getTeam())) { // attack
                clickOnEnemyUnitCell(this);
            }
        });
    }

    public void mouseMovementMode(){
        this.setOnMouseEntered(e -> mouseEntered(GameCell.this));
        this.setOnMouseExited(e -> mouseExited(GameCell.this));
    }

    public void setCellImage(String path, double opacity){
        ImageView imageView = new ImageView();
        ClassLoader classLoader = getClass().getClassLoader();
        URL urlToImage = classLoader.getResource(path);
        Image buttonImage = new Image(urlToImage.toString(), false);
        imageView.setImage(buttonImage);
        imageView.fitHeightProperty().bindBidirectional(this.minHeightProperty());
        imageView.fitWidthProperty().bindBidirectional(this.minWidthProperty());
        imageView.setOpacity(opacity);
        this.setPadding(new Insets(1));
        this.setGraphic(imageView);
    }

    public void activate(Unit activator){
        this.setActivated(true);
        this.setOwner(activator.getTeam());
        if(activator.getTeam() == 2){
            this.setEffect(new Lighting()); //setStyle("-fx-background-color: #00FF00"); TROUBLE with styling. Research needed
        }else{
            this.setEffect(new Bloom()); //setStyle("-fx-background-color: #FF0000");
        }
    }

    public static int getEnemyTeam(){
        return teamTurnValue==1? 2:1;
    }

    public static boolean isTeamTurn(int team){
        return (team == teamTurnValue);
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public static String getDeadCellImagePath() {
        return deadCellImagePath;
    }

    public static Unit getTemporaryUnit() {
        return temporaryUnit;
    }

    public static void setTemporaryUnit(Unit temporaryUnit) {
        GameCell.temporaryUnit = temporaryUnit;
    }

    public static GameCell getPreviousGameCell() {
        return previousGameCell;
    }

    public static void setPreviousGameCell(GameCell previousGameCell) {
        GameCell.previousGameCell = previousGameCell;
    }

//    public String getDefaultCellImagePath() {
//        return defaultCellImagePath;
//    }
//
//    public void setDefaultCellImagePath(String defaultCellImagePath) {
//        this.defaultCellImagePath = defaultCellImagePath;
//    }

    public static void setDeadCellImagePath(String deadCellImagePath) {
        GameCell.deadCellImagePath = deadCellImagePath;
    }

    public int getImageNumber() {
        return imageNumber;
    }

    public void setImageNumber(int imageNumber) {
        this.imageNumber = imageNumber;
    }

}
