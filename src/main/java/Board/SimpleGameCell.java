package Board;

import Units.SimpleUnit;
import javafx.scene.control.Button;

/**
 * Created by Glazyrin.D on 2/20/2017.
 */
public class SimpleGameCell extends Button{
    static SimpleGameCell previousSimpleGameCell;
    static boolean isSelected;
    static int teamTurnValue = 1;
    String name = this.getText();
    int imageNumber;
    private SimpleUnit simpleUnit;
    private static SimpleUnit temporarySimpleUnit;
    int xCoord;
    int yCoord;
    boolean isDead;
    boolean isBlocked = false;
    boolean isPassable = false;
    boolean isInShootingRange = false;
    boolean isSafe = true;
    boolean isDangerous = false;
    boolean isStrategical = false;
    boolean isActivated = false;
    int owner = 0;

    SimpleGameCell(){}

    SimpleGameCell(GameCell gc){
        simpleUnit = new SimpleUnit(gc.getUnit());
        temporarySimpleUnit = new SimpleUnit(GameCell.getTemporaryUnit());
        previousSimpleGameCell = new SimpleGameCell(GameCell.getPreviousGameCell());
        isSelected = GameCell.isSelected();
        teamTurnValue = GameCell.getTeamTurnValue();
        temporarySimpleUnit = GameCell.getTemporaryUnit();
        this.name = gc.getText();
        this.imageNumber = gc.getImageNumber();
        this.simpleUnit = gc.getUnit();
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

    public SimpleUnit getUnit() {
        return simpleUnit;
    }

    public void setSimpleUnit(SimpleUnit simpleUnit) {
        this.simpleUnit = simpleUnit;
    }

    public static SimpleUnit getTemporarySimpleUnit() {
        return temporarySimpleUnit;
    }

    public static void setTemporarySimpleUnit(SimpleUnit temporarySimpleUnit) {
        SimpleGameCell.temporarySimpleUnit = temporarySimpleUnit;
    }

    public static SimpleGameCell getPreviousSimpleGameCell() {
        return previousSimpleGameCell;
    }

    public static void setPreviousSimpleGameCell(SimpleGameCell previousCell) {
   previousSimpleGameCell = previousCell;
    }


    public static boolean isSelected() {
        return isSelected;
    }

    public static void setIsSelected(boolean isSelected) {
        SimpleGameCell.isSelected = isSelected;
    }

    public static int getTeamTurnValue() {
        return teamTurnValue;
    }

    public static void setTeamTurnValue(int teamTurnValue) {
        SimpleGameCell.teamTurnValue = teamTurnValue;
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

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
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
