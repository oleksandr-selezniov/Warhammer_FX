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
    private ImageView gameCellImageView = new ImageView();
    private String name = this.getText();
    private int xCoord;
    private int yCoord;
    private Boolean isPassable = false;
    private Boolean isInShootingRange = false;
    private Boolean isSafe = true;
    private Boolean isDangerous = false;
    //private Boolean isSelected = false;?;lkjhgdsdf test
    private Unit unit;

    GameCell() {
        this.setSize(Size.getCellWidth(), Size.getCellHeight());
        this.setDefaultImage();
        this.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        actionMode();
        mouseMovementMode();
    }

    public void actionMode() {
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            //@Override
            public void handle(MouseEvent e) {

                if (!isSelected) {
                    if (unit != null && isTeamTurn(unit.getTeam())) {
                        isSelected = true;
                        temporaryUnit = unit;
                        BoardUtils.calculateRanges(GameCell.this);
                        setTransparentImage(unit.getPicturePath());
                        previousGameCell = GameCell.this;
                        unit = null;
                    }

                } else if (GameCell.this.isPassable() && unit == null) {
                    if (temporaryUnit != null) {
                        unit = temporaryUnit;
                        setUnitImage(temporaryUnit.getPicturePath());
                        previousGameCell.setEffect(null);
                        temporaryUnit = null;
                    }
                    isSelected = false;
                    if (!(previousGameCell.equals(GameCell.this))) {
                        previousGameCell.setDefaultImage();
                        changeTeamTurn();
                    }
                    BoardUtils.abortFieldPassability();
                    BoardUtils.abortShootingRange();

                } else if (unit != null && temporaryUnit.isEnemyUnit(unit) && isTeamTurn(temporaryUnit.getTeam())) {
                    previousGameCell.setUnit(temporaryUnit);
                    previousGameCell.setUnitImage(temporaryUnit.getPicturePath());
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
            GameCell.this.setUnitImage("src\\main\\resources\\dead.jpg", 125, 125, 1);
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

                if (!(GameCell.this.getEffect() instanceof InnerShadow) && !(GameCell.this.getEffect() instanceof Lighting) && !(GameCell.this.getGraphic().getEffect() instanceof DropShadow)){
                    GameCell.this.getGraphic().setEffect(new Glow());
                }
                if (temporaryUnit != null && GameCell.this.getUnit() != null){
                    if (GameCell.this.getUnit().isEnemyUnit(temporaryUnit)) {
                        Cursor c = new ImageCursor(BoardUtils.getImage("src\\main\\resources\\CursorChainsword.png"), 300,300);
                        GameCell.this.setCursor(c);
                        InnerShadow innerShadow = new InnerShadow();
                        innerShadow.setRadius(20);
                        innerShadow.setOffsetX(4);
                        innerShadow.setOffsetY(4);
                        innerShadow.setColor(Color.rgb(255, 0, 0, 1));
                        GameCell.this.getGraphic().setEffect(innerShadow);
                    }
                }
            }
        });

        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            //@Override
            public void handle(MouseEvent e) {
                GameCell.this.setCursor(Cursor.DEFAULT);
                if(!(GameCell.this.getGraphic().getEffect() instanceof DropShadow)){
                    GameCell.this.getGraphic().setEffect(null);
                }
                GameCell.this.setTooltip(null);
            }
        });
    }

    public void setDefaultImage(){
        String imageUrl = null;
        try {
            File file = new File(defaultCellImagePath);
            imageUrl = file.toURI().toURL().toString();
        }catch (MalformedURLException e){e.printStackTrace();}
        Image buttonImage = new Image(imageUrl, false);
        this.gameCellImageView.setImage(buttonImage);
        if(Board.getScaleCoefficient()!=null){
            this.gameCellImageView.setFitHeight(Size.getCellHeight()*Board.getScaleCoefficient());
            this.gameCellImageView.setFitWidth(Size.getCellWidth()*Board.getScaleCoefficient());
        }else {
            this.gameCellImageView.setFitHeight(Size.getCellHeight());
            this.gameCellImageView.setFitWidth(Size.getCellWidth());
        }
        this.gameCellImageView.setOpacity(0.5);
        this.setPadding(new Insets(1));
        this.setGraphic(gameCellImageView);
    }

    public void setUnitImage(String imagePath){
        String imageUrl = null;
        try {
            File file = new File(imagePath);
            imageUrl = file.toURI().toURL().toString();
        }catch (MalformedURLException e){e.printStackTrace();}
        Image buttonImage = new Image(imageUrl, false);
        this.gameCellImageView.setImage(buttonImage);
        if (Board.getScaleCoefficient() != null) {
            this.gameCellImageView.setFitHeight(Size.getUnitHeight()*Board.getScaleCoefficient());
            this.gameCellImageView.setFitWidth(Size.getUnitWidth()*Board.getScaleCoefficient());
        }
        else {
            this.gameCellImageView.setFitHeight(Size.getUnitHeight());
            this.gameCellImageView.setFitWidth(Size.getUnitWidth());
        }
        this.gameCellImageView.setOpacity(1);
        this.setPadding(new Insets(2,2,5,2));
        this.setGraphic(gameCellImageView);
    }

    public void setTransparentImage(String imagePath){
        String imageUrl = null;
        try {
            File file = new File(imagePath);
            imageUrl = file.toURI().toURL().toString();
        }catch (MalformedURLException e){e.printStackTrace();}
        Image buttonImage = new Image(imageUrl, false);
        this.gameCellImageView.setImage(buttonImage);
        if (Board.getScaleCoefficient() != null) {
            this.gameCellImageView.setFitHeight(Size.getUnitHeight()*Board.getScaleCoefficient());
            this.gameCellImageView.setFitWidth(Size.getUnitWidth()*Board.getScaleCoefficient());
        }
        else {
            this.gameCellImageView.setFitHeight(Size.getUnitHeight());
            this.gameCellImageView.setFitWidth(Size.getUnitWidth());
        }
        this.gameCellImageView.setOpacity(0.8);
        this.setPadding(new Insets(2,2,5,2));
        this.setGraphic(gameCellImageView);
    }

    public void setUnitImage(String imagePath, int width, int height, double opacity){
        String imageUrl = null;
        try {
            File file = new File(imagePath);
            imageUrl = file.toURI().toURL().toString();
        }catch (MalformedURLException e){e.printStackTrace();}
        Image buttonImage = new Image(imageUrl, false);
        this.gameCellImageView.setImage(buttonImage);
        if (Board.getScaleCoefficient() != null) {
            this.gameCellImageView.setFitHeight(height*Board.getScaleCoefficient());
            this.gameCellImageView.setFitWidth(width*Board.getScaleCoefficient());
        }
        else {
            this.gameCellImageView.setFitHeight(height);
            this.gameCellImageView.setFitWidth(width);
        }
        this.gameCellImageView.setOpacity(opacity);
        this.setPadding(new Insets(3,3,-80,-80));
        this.setGraphic(gameCellImageView);
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
        if(teamTurnValue==1){
            teamTurnValue = 2;
        }else{
            teamTurnValue = 1;
        }
    }

    private boolean isTeamTurn(int team){
        return (team == teamTurnValue);
    }

    public ImageView getGameCellImageView() {
        return gameCellImageView;
    }
}
