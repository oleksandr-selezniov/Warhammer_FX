package Board.Utils;

import AI.Elsa_AI;
import Board.Board;
import Units.MeleeInfantry;
import Units.Unit;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.*;
import javafx.scene.paint.Color;

import static Board.Board.initializeBottomMenu;
import static Board.BoardInitializer.*;
import static Board.ChooseBoard.*;
import static Board.GameCell.*;
import static Board.GameCell.getTeamTurnValue;
import static Board.Utils.BoardUtils.getBestTarget;
import static Board.Utils.BoardUtils.haveEnemyUnitsInMeleeRange;
import static Board.Utils.BoardUtils.isOnNeighbouringCellPlusDiagonal;

import Board.BoardInitializer;
import Units.Interfaces.RangeUnit;
import  Board.GameCell;

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
        if(gc.getUnit() != null &&!canSkipTurn(gc)) {
            if (isTeamTurn(gc.getUnit().getTeam()) && gc.getUnit().isActive()) {
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

        if (!(getPreviousGameCell().equals(gc))) {
            getPreviousGameCell().setCellImage(gc.getDefaultCellImagePath(), 0.6);
            getPreviousGameCell().setPadding(new Insets(1));

            if(gc.getUnit() instanceof MeleeInfantry && haveEnemyUnitsInMeleeRange(gc)){
                if(getBestTarget(gc)!=null){
                    performRapidMeleeAttack(gc, getBestTarget(gc));
                }
            }
            gc.getUnit().setActive(false);
            checkTeamTurn();
        }
        setIsSelected(false);
        abortRangesAndPassability();
    }

    public static void clickOnEnemyUnitCell(GameCell gc){
        getPreviousGameCell().setUnit(getTemporaryUnit());
        getPreviousGameCell().setGraphic(getTemporaryUnit().getImageView(1.0));
        setIsSelected(false);
        if (gc.isInShootingRange() || (isOnNeighbouringCellPlusDiagonal(getPreviousGameCell(), gc))) {
            performGeneralAttack(getPreviousGameCell(), gc);
        }
        abortRangesAndPassability();
        setTemporaryUnit(null);
        checkTeamTurn();
    }

    public static boolean canSkipTurn(GameCell gc){
        if(gc.getUnit() instanceof RangeUnit){
            if(!BoardUtils.canWalkSomewhere(gc) && !BoardUtils.canShootSomebody(gc) && !BoardUtils.canHitSomebody(gc)){
                setIsSelected(false);
                abortRangesAndPassability();
                checkTeamTurn();
                System.out.println(gc.getUnit().getName() + " Skipped Turn");
                return true;
            }
        }
        else{
            if(!BoardUtils.canWalkSomewhere(gc) && !BoardUtils.canHitSomebody(gc)){
                setIsSelected(false);
                abortRangesAndPassability();
                checkTeamTurn();
                System.out.println(gc.getUnit().getName() + " Skipped Turn");
                return true;
            }
        }
        return false;
    }

    public static void clickOnStrategicalCell(GameCell gc){
        getPreviousGameCell().setUnit(getTemporaryUnit());
        getPreviousGameCell().setGraphic(getTemporaryUnit().getImageView(1.0));
        setIsSelected(false);
        if (isOnNeighbouringCellPlusDiagonal(getPreviousGameCell(), gc)){
            if (gc.getOwner() != getTemporaryUnit().getTeam()){
                gc.activate(getTemporaryUnit());
                getTemporaryUnit().setActive(false);
            }
        }
        abortRangesAndPassability();
        checkTeamTurn();
    }

    public static void abortRangesAndPassability(){
        BoardUtils.refreshZOrder();
        BoardUtils.abortFieldPassability();
        BoardUtils.abortShootingRange();
    }

    public static void checkTeamTurn(){
        if(getTeam1Score() >= BoardInitializer.getScoreLimit() | getTeam2Score() >= BoardInitializer.getScoreLimit()) {
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
                    if(getTeamTurnValue()==2 && isEnableAI()){
                        Elsa_AI.getInstance().doAction();
                    }
                }
            }
        }
    }

    private static void performGeneralAttack(GameCell hunter_GC, GameCell victim_CG){
        if (victim_CG.getUnit().getHealth() > 0) {
            victim_CG.getGraphic().setEffect(new SepiaTone());

            if (isOnNeighbouringCellPlusDiagonal(hunter_GC, victim_CG)) {
                getTemporaryUnit().performCloseAttack(victim_CG.getUnit());
            } else if(!(getTemporaryUnit() instanceof MeleeInfantry)){
                getTemporaryUnit().performRangeAttack(victim_CG.getUnit());
            }
        }
        checkForDeath(getTemporaryUnit(), victim_CG);
    }

    private static void performRapidMeleeAttack(GameCell hunter_GC, GameCell victim_CG){
        if (hunter_GC.getUnit() instanceof MeleeInfantry && victim_CG.getUnit().getHealth() > 0
                && isOnNeighbouringCellPlusDiagonal(hunter_GC, victim_CG)) {
            victim_CG.getGraphic().setEffect(new SepiaTone());
            ((MeleeInfantry)hunter_GC.getUnit()).performRapidMeleeAttack(victim_CG.getUnit());
        }
        checkForDeath(hunter_GC.getUnit(), victim_CG);
    }

    private static void checkForDeath(Unit killer, GameCell victim_CG){
        if (victim_CG.getUnit().getHealth() <= 0) {
            victim_CG.getUnit().setHealth(0);
            LoggerUtils.writeDeadLog(killer, victim_CG.getUnit());
            victim_CG.setCellImage(getDeadCellImagePath(), 0.6);
            victim_CG.setUnit(null);
            //  checkTeamTurn();
        }
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
        if (getTeamTurnValue()==2){
            increaseScore();
        }
        setTeamTurnValue( getTeamTurnValue()==1? 2:1);
    }

    public static void increaseScore(){
        setTeam1Score(BoardUtils.getStrategicalPoints(1));
        setTeam2Score(BoardUtils.getStrategicalPoints(2));
        Board.setScore(getTeam1Score() + " : " + getTeam2Score());
    }

    public static void mouseExited(GameCell gc){
        gc.setCursor(Cursor.DEFAULT);
        gc.getGraphic().setEffect(null);
        gc.setTooltip(null);
    }

    public static void mouseEntered(GameCell gc){
        if (gc.getUnit() != null) {
            gc.setTooltip(getToolTip(gc));
            if(gc.getUnit().getTeam()==1){
                initializeBottomMenu(gc, "left");
            }else{
                initializeBottomMenu(gc, "right");
            }
        }
        if (!(gc.getEffect() instanceof InnerShadow)){
            gc.getGraphic().setEffect(new Glow());

            if (isAlreadyUsedCurrentTeamUnit(gc))
                paintUnitWithBlack(gc);
        }
        if (getTemporaryUnit() != null && gc.getUnit() != null){
            if (gc.getUnit().isEnemyUnit(getTemporaryUnit())) {
                highlightEnemyUnit(gc);
            }
        }
    }

    private static boolean isAlreadyUsedCurrentTeamUnit(GameCell gc){
        return (gc.getUnit()!=null && !gc.getUnit().isActive() && gc.getUnit().getTeam()==getTeamTurnValue());
    }

    public static void paintUnitWithBlack(GameCell gc){
        gc.getGraphic().setEffect(new Lighting(new Light.Spot()));
    }

    public static int generateRandomNumber(int min, int max){
        return min + (int)(Math.random() * ((max - min) + 1));
    }
}
