package Board;

import Units.MeleeInfantry;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.SepiaTone;
import javafx.scene.paint.Color;

import static Board.BoardInitializer.*;

import Units.Interfaces.RangeUnit;

import static Board.BoardUtils.*;
import static Board.GameCell.*;

/**
 * Created by Glazyrin.D on 1/16/2017.
 */
public class GameCellUtils {

    public static Tooltip getToolTip(GameCell gameCell){
        Tooltip tooltip = new Tooltip(gameCell.getUnit().getInfo());
        tooltip.setAutoHide(false);
        return tooltip;
    }

    public static void placeObstacle(GameCell gameCell, String path){
        gameCell.setBlocked(true);
        gameCell.setCellImage(path, 1);
    }

    public static void makeStrategical(int x, int y){
        GameCell gameCell = (GameCell) Board.getScene().lookup("#" + x + "_" + y);
        gameCell.setStrategical(true);
        gameCell.setBlocked(true);
        gameCell.setCellImage("other/strartegical_point.jpg", 1);
    }

    public static void endGame(){
        BoardUtils.setActiveTeamUnits(1, false);
        BoardUtils.setActiveTeamUnits(2, false);
        Board.getScene().lookup("#end_turn").setDisable(true);
    }

    public static void generateObstacles(double density){
        Board.getMainBattlefieldGP().getChildren().forEach(p->{
            double chance = Math.random();
            if(((GameCell)p).getUnit()==null && !((GameCell)p).isBlocked()
                    && ((GameCell)p).getxCoord()>1 && ((GameCell)p).getyCoord()>1
                    && ((GameCell)p).getxCoord()<(Board.getBoardWidth()-2)
                    && ((GameCell)p).getyCoord()<(Board.getBoardHeight()-2)){
                if(density > chance){
                    String obstacleImagePath = "obstacles/"+generateRandomNumber(1,8)+".jpg";
                    placeObstacle(((GameCell)p), obstacleImagePath);
                }
            }
        });
    }

    public static void clickOnUnitCell(GameCell gc){
        if(!canSkipTurn(gc)) {
            if (gc.getUnit() != null && isTeamTurn(gc.getUnit().getTeam()) && gc.getUnit().isActive()) {
                setIsSelected(true);
                setTemporaryUnit(gc.getUnit());
                BoardUtils.calculateRanges(gc);
                gc.setGraphic(gc.getUnit().getImageView(0.7));
                setPreviousGameCell(gc);
                gc.setUnit(null);
            }
        }
    }

    public static void clickOnFreeCell(GameCell gc){
        if (getTemporaryUnit() != null) {
            gc.setUnit(getTemporaryUnit());
            gc.setGraphic(getTemporaryUnit().getImageView(1.0));
            gc.setPadding(getTemporaryUnit().getInsetsY());
            getPreviousGameCell().setEffect(null);
            setTemporaryUnit(null);
        }
        setIsSelected(false);
        if (!(getPreviousGameCell().equals(gc))) {
            getPreviousGameCell().setCellImage(gc.getDefaultCellImagePath(), 0.6);
            getPreviousGameCell().setPadding(new Insets(1));
            gc.getUnit().setActive(false);
            checkTeamTurn();
        }
        abortRangesAndPassability();
    }

    public static void clickOnEnemyUnitCell(GameCell gc){
        getPreviousGameCell().setUnit(getTemporaryUnit());
        getPreviousGameCell().setGraphic(getTemporaryUnit().getImageView(1.0));
        setIsSelected(false);
        if (gc.isInShootingRange() || (BoardUtils.isOnNeighbouringCellPlusDiagonal(getPreviousGameCell(), gc))) {
            performAttack(getPreviousGameCell(), gc);
        }
        abortRangesAndPassability();
        setTemporaryUnit(null);
    }

    public static boolean canSkipTurn(GameCell gc){
        if(gc.getUnit() instanceof RangeUnit){
            if(!canWalkSomewhere(gc) && !canShootSomebody(gc) && !canHitSomebody(gc)){
                setIsSelected(false);
                abortRangesAndPassability();
                checkTeamTurn();
                return true;
            }
        }
       else{
            if(!canWalkSomewhere(gc) && !canHitSomebody(gc)){
                setIsSelected(false);
                abortRangesAndPassability();
                checkTeamTurn();
                return true;
            }
        }
        return false;
    }

    public static void clickOnStrategicalCell(GameCell gc){
        getPreviousGameCell().setUnit(getTemporaryUnit());
        getPreviousGameCell().setGraphic(getTemporaryUnit().getImageView(1.0));
        setIsSelected(false);
        if (BoardUtils.isOnNeighbouringCellPlusDiagonal(getPreviousGameCell(), gc)){
            if (gc.getOwner() != getTemporaryUnit().getTeam()){
                gc.activate(getTemporaryUnit());
                getTemporaryUnit().setActive(false);
                checkTeamTurn();
            }
        }
        abortRangesAndPassability();
    }

    public static void abortRangesAndPassability(){
        BoardUtils.refreshZOrder();
        BoardUtils.abortFieldPassability();
        BoardUtils.abortShootingRange();
    }

    public static void checkTeamTurn(){
        if(getTeam1Score() >= BoardInitializer.getScoreLimit() | getTeam2Score() >=BoardInitializer.getScoreLimit()) {
            int winner = (getTeam1Score() >= BoardInitializer.getScoreLimit())? 1:2;
            LoggerUtils.writeWinLog(winner);
            endGame();
        }else{
            if(BoardUtils.getTotalUnitNumber(getEnemyTeam())<=0){
                endGame();
            }else {
                if (BoardUtils.getActiveUnitNumber(getTeamTurnValue()) == 0) {
                    changeTeamTurn();
                    LoggerUtils.writeTurnLog(getTeamTurnValue());
                    BoardUtils.setActiveTeamUnits(getTeamTurnValue(), true);
                    if(getTeamTurnValue()==2){
                        AI.getInstance().doAction();
                    }
                }
            }
        }
    }

    private static void performAttack(GameCell hunter_GC, GameCell victim_CG){
        if (victim_CG.getUnit().getHealth() > 0) {
            victim_CG.getGraphic().setEffect(new SepiaTone());

            if (BoardUtils.isOnNeighbouringCellPlusDiagonal(hunter_GC, victim_CG)) {
                getTemporaryUnit().performCloseAttack(victim_CG.getUnit());
            } else if(!(getTemporaryUnit() instanceof MeleeInfantry)){
                getTemporaryUnit().performRangeAttack(victim_CG.getUnit());
            }
        }
        if (victim_CG.getUnit().getHealth() <= 0) {
            LoggerUtils.writeDeadLog(getTemporaryUnit(), victim_CG.getUnit());
            victim_CG.setCellImage(getDeadCellImagePath(), 0.6);
            victim_CG.setUnit(null);
            checkTeamTurn();
        }
        checkTeamTurn();
    }

    public static void highlightEnemyUnit(GameCell gc){
        Cursor c = new ImageCursor(new BoardUtils().getImage("other/CursorChainsword.png"), 300,300);
        gc.setCursor(c);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(3);
        dropShadow.setOffsetY(3);
        dropShadow.setColor(Color.rgb(255, 0, 0, 1));
        gc.getGraphic().setEffect(dropShadow);
    }

    public static void changeTeamTurn(){
        setTeam1Score(getStrategicalPoints(1));
        setTeam2Score(getStrategicalPoints(2));
        Board.setScore(getTeam1Score() + " : " + getTeam2Score());
        setTeamTurnValue( getTeamTurnValue()==1? 2:1);
    }

    public static void paintUnitWithBlack(GameCell gc){
        gc.getGraphic().setEffect(new Lighting(new Light.Spot()));
    }

    public static int generateRandomNumber(int min, int max){
        return min + (int)(Math.random() * ((max - min) + 1));
    }
}
