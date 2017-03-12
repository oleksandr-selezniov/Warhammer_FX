package Board.Utils;

import AI.Elsa_AI;
import Board.Board;
import Units.MeleeInfantry;
import Units.Gui_Unit;
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
import static Board.Utils.BoardUtils.*;
import static Board.Utils.LoggerUtils.getRAMConsumptionStatus;

import Board.BoardInitializer;
import Units.Interfaces.RangeUnit;
import  Board.GameCell;

/**
 * Created by Glazyrin.D on 1/16/2017.
 */
public class GameCellUtils {

    public static Tooltip getToolTip(GameCell gameCell){
        Tooltip tooltip = new Tooltip(gameCell.getGUnit().getInfo());
        tooltip.setAutoHide(false);
        return tooltip;
    }

    public static void placeObstacle(GameCell gameCell, String path){
        gameCell.setBlocked(true);
        gameCell.setCellObstacle(path, 1);
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
        Board.getMainBattlefieldGP().getChildren().stream().filter(p->p instanceof GameCell).forEach(p->{
            double chance = Math.random();
            if(((GameCell)p).getGUnit()==null && !((GameCell)p).isBlocked()
                    && ((GameCell)p).getxCoord()>1 && ((GameCell)p).getyCoord()>=0
                    && ((GameCell)p).getxCoord()<(Board.getBoardWidth()-3)
                    && ((GameCell)p).getyCoord()<(Board.getBoardHeight())){
                if(density > chance){
                    String obstacleImagePath = "obstacles/"+generateRandomNumber(1,18)+".png";
                    placeObstacle(((GameCell)p), obstacleImagePath);
                }
            }
        });
    }

    public static void clickOnUnitCell(GameCell gc){
        if(gc.getGUnit() != null &&!canSkipTurn(gc)) {
            if (isTeamTurn(gc.getGUnit().getTeam()) && gc.getGUnit().isActive()) {
                setIsSelected(true);
                setTemporaryGuiUnit(gc.getGUnit());
                BoardUtils.calculateRanges(gc);
                gc.setGraphic(gc.getGUnit().getImageView(0.7));
                setPreviousGameCell(gc);
                gc.setGuiUnit(null);
            }
        }
    }

    public static void clickOnFreeCell(GameCell gc){
        if (getTemporaryGuiUnit() != null) {
            gc.setGuiUnit(getTemporaryGuiUnit());
            gc.setGraphic(getTemporaryGuiUnit().getImageView(1.0));
            gc.setPadding(getTemporaryGuiUnit().getInsetsY());
            getPreviousGameCell().setEffect(null);
            setTemporaryGuiUnit(null);
        }

        if (!(getPreviousGameCell().equals(gc))) {
            getPreviousGameCell().setGraphic(null);
            getPreviousGameCell().setPadding(new Insets(1));

            if(gc.getGUnit() instanceof MeleeInfantry && haveEnemyUnitsInMeleeRange(gc)){
                if(getBestTargetInAttackRange(gc)!=null){
                    performRapidMeleeAttack(gc, getBestTargetInAttackRange(gc));
                }
            }
            gc.getGUnit().setActive(false);
            checkTeamTurn();
        }
        setIsSelected(false);
        abortRangesAndPassability();
        refreshZOrder();
        refreshZOrderUnderGC(gc);
    }

    public static void clickOnEnemyUnitCell(GameCell gc){
        getPreviousGameCell().setGuiUnit(getTemporaryGuiUnit());
        getPreviousGameCell().setGraphic(getTemporaryGuiUnit().getImageView(1.0));
        setIsSelected(false);
        if (gc.isInShootingRange() || (isOnNeighbouringCellPlusDiagonal(getPreviousGameCell(), gc))) {
            performGeneralAttack(getPreviousGameCell(), gc);
        }
        abortRangesAndPassability();
        setTemporaryGuiUnit(null);
        checkTeamTurn();
    }

    public static boolean canSkipTurn(GameCell gc){
        if(gc.getGUnit() instanceof RangeUnit){
            if(!BoardUtils.canWalkSomewhere(gc) && !BoardUtils.canShootSomebody(gc) && !BoardUtils.canHitSomebody(gc)){
                setIsSelected(false);
                abortRangesAndPassability();
                checkTeamTurn();
                System.out.println(gc.getGUnit().getName() + " Skipped Turn");
                return true;
            }
        }
        else{
            if(!BoardUtils.canWalkSomewhere(gc) && !BoardUtils.canHitSomebody(gc)){
                setIsSelected(false);
                abortRangesAndPassability();
                checkTeamTurn();
                System.out.println(gc.getGUnit().getName() + " Skipped Turn");
                return true;
            }
        }
        return false;
    }

    public static void clickOnStrategicalCell(GameCell gc){
        getPreviousGameCell().setGuiUnit(getTemporaryGuiUnit());
        getPreviousGameCell().setGraphic(getTemporaryGuiUnit().getImageView(1.0));
        setIsSelected(false);
        if (isOnNeighbouringCellPlusDiagonal(getPreviousGameCell(), gc)){
            if (gc.getOwner() != getTemporaryGuiUnit().getTeam()){
                gc.activate(getTemporaryGuiUnit());
                getTemporaryGuiUnit().setActive(false);
            }
        }
        abortRangesAndPassability();
        checkTeamTurn();
    }

    public static void abortRangesAndPassability(){
        //BoardUtils.refreshZOrder();
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
        if (victim_CG.getGUnit().getHealth() > 0) {
            victim_CG.getGraphic().setEffect(new SepiaTone());

            if (isOnNeighbouringCellPlusDiagonal(hunter_GC, victim_CG)) {
                getTemporaryGuiUnit().performCloseAttack(victim_CG.getGUnit());
            } else if(!(getTemporaryGuiUnit() instanceof MeleeInfantry)){
                getTemporaryGuiUnit().performRangeAttack(victim_CG.getGUnit());
            }
        }
        checkForDeath(getTemporaryGuiUnit(), victim_CG);
    }

    private static void performRapidMeleeAttack(GameCell hunter_GC, GameCell victim_CG){
        if (hunter_GC.getGUnit() instanceof MeleeInfantry && victim_CG.getGUnit().getHealth() > 0
                && isOnNeighbouringCellPlusDiagonal(hunter_GC, victim_CG)) {
            victim_CG.getGraphic().setEffect(new SepiaTone());
            ((MeleeInfantry)hunter_GC.getGUnit()).performRapidMeleeAttack(victim_CG.getGUnit());
        }
        checkForDeath(hunter_GC.getGUnit(), victim_CG);
    }

    private static void checkForDeath(Gui_Unit killer, GameCell victim_CG){
        if (victim_CG.getGUnit().getHealth() <= 0) {
            victim_CG.getGUnit().setHealth(0);
            LoggerUtils.writeDeadLog(killer, victim_CG.getGUnit());
            victim_CG.setCellImage(getDeadCellImagePath(), 0.6);
            victim_CG.setGuiUnit(null);
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
        System.out.println(getRAMConsumptionStatus());
    }

    public static void increaseScore(){
        setTeam1Score(BoardUtils.getStrategicalPoints(1));
        setTeam2Score(BoardUtils.getStrategicalPoints(2));
        Board.setScore(getTeam1Score() + " : " + getTeam2Score());
    }

    public static void mouseExited(GameCell gc){
        gc.setCursor(Cursor.DEFAULT);
        if(gc.getGraphic() != null){
            gc.getGraphic().setEffect(null);
        }
        gc.setTooltip(null);
    }

    public static void mouseEntered(GameCell gc){
        if (gc.getGUnit() != null) {
            gc.setTooltip(getToolTip(gc));
            if(gc.getGUnit().getTeam()==1){
                initializeBottomMenu(gc, "left");
            }else{
                initializeBottomMenu(gc, "right");
            }
        }
        if (!(gc.getEffect() instanceof InnerShadow)){
            if(gc.getGraphic() != null){
                gc.getGraphic().setEffect(new Glow());
            }
            if (isAlreadyUsedCurrentTeamUnit(gc))
                paintUnitWithBlack(gc);
        }
        if (getTemporaryGuiUnit() != null && gc.getGUnit() != null){
            if (gc.getGUnit().isEnemyUnit(getTemporaryGuiUnit())) {
                highlightEnemyUnit(gc);
            }
        }
    }

    private static boolean isAlreadyUsedCurrentTeamUnit(GameCell gc){
        return (gc.getGUnit()!=null && !gc.getGUnit().isActive() && gc.getGUnit().getTeam()==getTeamTurnValue());
    }

    public static void paintUnitWithBlack(GameCell gc){
        gc.getGraphic().setEffect(new Lighting(new Light.Spot()));
    }

    public static int generateRandomNumber(int min, int max){
        return min + (int)(Math.random() * ((max - min) + 1));
    }
}
