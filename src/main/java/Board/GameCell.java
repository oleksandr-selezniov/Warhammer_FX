package Board;

import Size.Size;
import Units.Unit;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.io.File;
import java.net.MalformedURLException;

/**
 * Created by Dmitriy on 18.10.2016.
 */
public class GameCell extends Button {
    private static GameCell previousGameCell;
    private static Unit temporaryUnit;
    private static boolean isSelected;
    private static int teamTurnValue = 1;

    private String defaultCellImagePath = "src\\main\\resources\\cellBackground.jpg";
    private String deadCellImagePath = "src\\main\\resources\\dead.jpg";
    private String name = this.getText();
    private int xCoord;
    private int yCoord;
    private Boolean isPassable = false;
    private Boolean isInShootingRange = false;
    private Boolean isSafe = true;
    private Boolean isDangerous = false;
    private Unit unit;

    GameCell() {
        this.setPadding(new Insets(50));
        this.setSize(Size.getCellWidth(), Size.getCellHeight());
        this.setDefaultImage(this, defaultCellImagePath);
        this.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        actionMode();
        mouseMovementMode();
    }

    public void actionMode() {
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {

                if (!isSelected) {
                    if (unit != null && isTeamTurn(unit.getTeam())) {
                        isSelected = true;
                        temporaryUnit = unit;
                        BoardUtils.calculateRanges(GameCell.this);
                        GameCell.this.setGraphic(unit.getImageView(0.7));
                        previousGameCell = GameCell.this;
                        unit = null;
                    }
                } else if (GameCell.this.isPassable() && unit == null) {
                    if (temporaryUnit != null) {
                        unit = temporaryUnit;
                        GameCell.this.setGraphic(temporaryUnit.getImageView(1.0));
                        GameCell.this.setPadding(temporaryUnit.getInsets());
                        previousGameCell.setEffect(null);
                        temporaryUnit = null;
                    }
                    isSelected = false;
                    if (!(previousGameCell.equals(GameCell.this))) {
                        previousGameCell.setDefaultImage(previousGameCell, defaultCellImagePath);
                        previousGameCell.setPadding(new Insets(1));
                        changeTeamTurn();
                    }
                    BoardUtils.abortFieldPassability();
                    BoardUtils.abortShootingRange();

                } else if (unit != null && temporaryUnit.isEnemyUnit(unit) && isTeamTurn(temporaryUnit.getTeam())) {
                    previousGameCell.setUnit(temporaryUnit);
                    previousGameCell.setGraphic(temporaryUnit.getImageView(1.0)); //setUnitImage(temporaryUnit.getPicturePath());
                    isSelected = false;
                    BoardUtils.abortFieldPassability();
                    //***************************ATTACK**********************************************
                    if (GameCell.this.isInShootingRange()) {
                        performAttack(previousGameCell, GameCell.this);
                    }
                    //***************************ATTACK**********************************************
                    BoardUtils.abortShootingRange();
                    temporaryUnit = null;
                }
            }
        });
    }

    public void performAttack(GameCell hunter_GC, GameCell victim_CG){
        int meleeDamage=(int)(temporaryUnit.getCloseDamage()*unit.getArmor());
        int rangeDamage=(int)(temporaryUnit.getRangeDamage()*unit.getArmor());

        boolean isMeleeVulnerable = unit.getHealth()-meleeDamage<unit.getMaxHealth() && unit.getHealth()-meleeDamage!=unit.getHealth();
        boolean isRangeVulnerable = unit.getHealth()-rangeDamage<unit.getMaxHealth() && unit.getHealth()-rangeDamage!=unit.getHealth();

        if (unit.getHealth() > 0) {
            victim_CG.getGraphic().setEffect(new SepiaTone());
            if (BoardUtils.isOnNeighbouringCell(hunter_GC, victim_CG)) {
                //close attack
                if (isMeleeVulnerable) {
                    unit.setHealth(unit.getHealth() - meleeDamage);
                    Board.writeToTextArea("#centerTextArea", Board.getTextFromTextArea("#centerTextArea") + "\n"
                            + temporaryUnit.getName() + "\n"
                            + "made " + (meleeDamage) + "\n"
                            + "melee damage to " + "\n"
                            + unit.getName() + "\n", true);
                    changeTeamTurn();
                } else {
                    Board.writeToTextArea("#centerTextArea", Board.getTextFromTextArea("#centerTextArea") + "\n"
                            + temporaryUnit.getName() + "\n"
                            + "can't even scratch" + "\n"
                            + unit.getName() + "\n", true);
                }
            } else {
                //Range damage
                if (temporaryUnit.getAmmo() > 0) {
                    if (isRangeVulnerable) {
                        unit.setHealth(unit.getHealth() - rangeDamage);
                        Board.writeToTextArea("#centerTextArea", Board.getTextFromTextArea("#centerTextArea") + "\n"
                                + temporaryUnit.getName() + "\n"
                                + "made " + (rangeDamage) + "\n"
                                + "range damage to " + "\n"
                                + unit.getName() + "\n", true);
                        temporaryUnit.setAmmo(temporaryUnit.getAmmo() - 1);
                        changeTeamTurn();
                    } else {
                        Board.writeToTextArea("#centerTextArea", Board.getTextFromTextArea("#centerTextArea") + "\n"
                                + temporaryUnit.getName() + "\n"
                                + "can't even scratch" + "\n"
                                + unit.getName() + "\n", true);
                    }
                } else {
                    Board.writeToTextArea("#centerTextArea", Board.getTextFromTextArea("#centerTextArea") + "\n"
                            + temporaryUnit.getName() + "\n"
                            + "is out of ammunition!" + "\n", true);
                }
            }
        }
        if (unit.getHealth() <= 0) {
            Board.writeToTextArea("#centerTextArea", Board.getTextFromTextArea("#centerTextArea") + "\n"
                    + unit.getName() + "\n"
                    + "is killed by" + "\n"
                    +temporaryUnit.getName()+"\n", true);
            GameCell.this.setDefaultImage(previousGameCell, deadCellImagePath);
            unit = null;
        }
    }

    public void mouseMovementMode(){
        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                if (GameCell.this.getUnit() != null) {
                    Tooltip tooltip = new Tooltip(GameCell.this.getUnit().getInfo());
                    tooltip.setAutoHide(false);
                    GameCell.this.setTooltip(tooltip);
                    if(GameCell.this.getUnit().getTeam()==1){
                        Board.writeToTextArea("#leftTextArea", GameCell.this.getUnit().getInfo(), false);
                        Board.setImageToImageView("#leftImageView", GameCell.this.getUnit().getImage());
                    }else{
                        Board.writeToTextArea("#rightTextArea", GameCell.this.getUnit().getInfo(), false);
                        Board.setImageToImageView("#rightImageView", GameCell.this.getUnit().getImage());
                    }
                }

                if (!(GameCell.this.getEffect() instanceof InnerShadow) && !(GameCell.this.getEffect() instanceof Lighting)){
                    GameCell.this.getGraphic().setEffect(new Glow());
                }
                if (temporaryUnit != null && GameCell.this.getUnit() != null){
                    if (GameCell.this.getUnit().isEnemyUnit(temporaryUnit)) {
                        Cursor c = new ImageCursor(BoardUtils.getImage("src\\main\\resources\\CursorChainsword.png"), 300,300);
                        GameCell.this.setCursor(c);
                        DropShadow dropShadow = new DropShadow();
                        dropShadow.setOffsetX(3);
                        dropShadow.setOffsetY(3);
                        dropShadow.setColor(Color.rgb(255, 0, 0, 1));
                        GameCell.this.getGraphic().setEffect(dropShadow);
                    }
                }
            }
        });

        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                GameCell.this.setCursor(Cursor.DEFAULT);
                GameCell.this.getGraphic().setEffect(null);
                GameCell.this.setTooltip(null);
            }
        });
    }

    public void setDefaultImage(GameCell gc, String path){
        ImageView imageView = new ImageView();
        String imageUrl = null;
        try {
            File file = new File(path);
            imageUrl = file.toURI().toURL().toString();
        }catch (MalformedURLException e){e.printStackTrace();}
        Image buttonImage = new Image(imageUrl, false);
        imageView.setImage(buttonImage);
        imageView.fitHeightProperty().bindBidirectional(gc.minHeightProperty());
        imageView.fitWidthProperty().bindBidirectional(gc.minWidthProperty());
        imageView.setOpacity(0.5);
        this.setPadding(new Insets(1));
        this.setGraphic(imageView);
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

    public void setSize(Double width, Double height){
        this.setMaxHeight(height);
        this.setMinHeight(height);
        this.setMaxWidth(width);
        this.setMinWidth(width);
    }

    private static void changeTeamTurn(){
        if(teamTurnValue == 1){
            teamTurnValue = 2;
        }else{
            teamTurnValue = 1;
        }
        BoardUtils.refreshZOrder();
    }

    private boolean isTeamTurn(int team){
        return (team == teamTurnValue);
    }
}
