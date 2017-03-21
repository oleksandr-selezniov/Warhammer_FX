package Board;

import Size.Size;
import Units.Gui_Unit;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
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
public class GameCell extends Hyperlink {
    private static GameCell previousGameCell;
    private static Gui_Unit temporaryGuiUnit;
    private int imageNumber = generateRandomNumber(1,25);
    private static String deadCellImagePath = "other/dead.png";
    private Gui_Unit guiUnit;
    private static boolean isSelected;
    private static int teamTurnValue = 1;
    private String name = this.getText();
    private int xCoord;
    private int yCoord;
    private boolean isBlocked = false;
    private boolean isPassable = false;
    private boolean isInShootingRange = false;
    private boolean isSafe = true;
    private boolean isDangerous = false;
    private boolean isStrategical = false;
    private boolean isActivated = false;
    private int owner = 0;
    private SimpleDoubleProperty obstacleSize = new SimpleDoubleProperty(0);

    public GameCell() {
        this.setSize(Size.getCellSize());
        actionMode();
        mouseMovementMode();
    }

    public void setSize(Double size){
        this.setMaxHeight(size);
        this.setMinHeight(size);
        this.setMaxWidth(size);
        this.setMinWidth(size);
        this.minHeightProperty().addListener((ov, oldVal, newVal) -> {
            obstacleSize.set(newVal.doubleValue() * 1.4);
        });
    }

    public void actionMode() {
        this.setOnMouseClicked(e -> {

            if (!isSelected) { // first step
                clickOnUnitCell(this);
            } else if (this.isPassable() && this.guiUnit == null) { // second step
                clickOnFreeCell(this);
            } else if (this.isStrategical() && this.guiUnit == null) { // activate strategical cell
                clickOnStrategicalCell(this);
            } else if (this.guiUnit != null && temporaryGuiUnit.isEnemyUnit(this.guiUnit) && isTeamTurn(temporaryGuiUnit.getTeam())) { // attack
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

    public void setCellObstacle(String path, double opacity){
        ImageView imageView = new ImageView();
        ClassLoader classLoader = getClass().getClassLoader();
        URL urlToImage = classLoader.getResource(path);
        Image buttonImage = new Image(urlToImage.toString(), false);
        imageView.setImage(buttonImage);
        imageView.fitHeightProperty().bindBidirectional(this.obstacleSize);
        imageView.fitWidthProperty().bindBidirectional(this.obstacleSize);
        imageView.setOpacity(opacity);
        this.setPadding(new Insets(1,1,40,1));
        this.setGraphic(imageView);
    }

    public void activate(Gui_Unit activator){
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

    public Gui_Unit getGUnit() {
        return guiUnit;
    }

    public void setGuiUnit(Gui_Unit guiUnit) {
        this.guiUnit = guiUnit;
    }

    public static String getDeadCellImagePath() {
        return deadCellImagePath;
    }

    public static Gui_Unit getTemporaryGuiUnit() {
        return temporaryGuiUnit;
    }

    public static void setTemporaryGuiUnit(Gui_Unit temporaryGuiUnit) {
        GameCell.temporaryGuiUnit = temporaryGuiUnit;
    }

    public static GameCell getPreviousGameCell() {
        return previousGameCell;
    }

    public static void setPreviousGameCell(GameCell previousGameCell) {
        GameCell.previousGameCell = previousGameCell;
    }

    public static void setDeadCellImagePath(String deadCellImagePath) {
        GameCell.deadCellImagePath = deadCellImagePath;
    }

    public static boolean isSelected() {
        return isSelected;
    }

    public static void setIsSelected(boolean Selected) {
        isSelected = Selected;
    }

    public static int getTeamTurnValue() {
        return teamTurnValue;
    }

    public static void setTeamTurnValue(int tTurnValue) {
        teamTurnValue = tTurnValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageNumber() {
        return imageNumber;
    }

    public void setImageNumber(int imageNumber) {
        this.imageNumber = imageNumber;
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

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public boolean isPassable() {
        return isPassable;
    }

    public void setPassable(boolean passable) {
        isPassable = passable;
    }

    public boolean isInShootingRange() {
        return isInShootingRange;
    }

    public void setInShootingRange(boolean inShootingRange) {
        isInShootingRange = inShootingRange;
    }

    public boolean isSafe() {
        return isSafe;
    }

    public void setSafe(boolean safe) {
        isSafe = safe;
    }

    public boolean isDangerous() {
        return isDangerous;
    }

    public void setDangerous(boolean dangerous) {
        isDangerous = dangerous;
    }

    public boolean isStrategical() {
        return isStrategical;
    }

    public void setStrategical(boolean strategical) {
        isStrategical = strategical;
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
}
