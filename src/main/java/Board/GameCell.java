package Board;

import Size.Size;
import Units.Unit;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
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
public class GameCell extends Button {
    private static GameCell previousGameCell;
    private static Unit temporaryUnit;
    private static boolean isSelected;
    private static int teamTurnValue = 1;
    private String defaultCellImagePath = "cellBackground/"+generateRandomNumber(1,25)+".jpg";
    private static String deadCellImagePath = "other/dead.jpg";
    private String name = this.getText();
    private int xCoord;
    private int yCoord;
    private Boolean isBlocked = false;
    private Boolean isPassable = false;
    private Boolean isInShootingRange = false;
    private Boolean isSafe = true;
    private Boolean isDangerous = false;
    private Boolean isStrategical = false;
    private  boolean isActivated = false;
    private int owner = 0;
    private Unit unit;

    GameCell() {
        this.setPadding(new Insets(50));
        this.setSize(Size.getCellSize());
        this.setCellImage(defaultCellImagePath, 0.6);
        this.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        actionMode();
        mouseMovementMode();
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

    public static int getTeamTurnValue() {
        return teamTurnValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getxCoord() {
        return xCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    public Boolean isPassable() {
        return isPassable;
    }

    public Boolean isInShootingRange() {
        return isInShootingRange;
    }

    public void setInShootingRange(Boolean inShootingRange) {
        isInShootingRange = inShootingRange;
    }

    public void setPassable(Boolean passable) {
        isPassable = passable;
    }

    public Boolean getSafe() {
        return isSafe;
    }

    public void setSafe(Boolean safe) {
        isSafe = safe;
    }

    public Boolean getDangerous() {
        return isDangerous;
    }

    public void setDangerous(Boolean dangerous) {
        isDangerous = dangerous;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public Boolean isStrategical() {
        return isStrategical;
    }

    public void setStrategical(Boolean strategical) {
        isStrategical = strategical;
    }

    public Boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
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

    public static boolean isSelected() {
        return isSelected;
    }

    public static void setIsSelected(boolean isSelected) {
        GameCell.isSelected = isSelected;
    }

    public static void setTeamTurnValue(int teamTurnValue) {
        GameCell.teamTurnValue = teamTurnValue;
    }

    public String getDefaultCellImagePath() {
        return defaultCellImagePath;
    }

    public void setDefaultCellImagePath(String defaultCellImagePath) {
        this.defaultCellImagePath = defaultCellImagePath;
    }

    public static void setDeadCellImagePath(String deadCellImagePath) {
        GameCell.deadCellImagePath = deadCellImagePath;
    }

    public Boolean getBlocked() {
        return isBlocked;
    }

    public Boolean getPassable() {
        return isPassable;
    }

    public Boolean getInShootingRange() {
        return isInShootingRange;
    }

    public Boolean getStrategical() {
        return isStrategical;
    }
}
