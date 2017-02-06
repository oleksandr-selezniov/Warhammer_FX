package AI;

import Board.GameCell;
import Units.Enums.UnitTypeNames;
import Units.Interfaces.MeleeUnit;
import Units.Unit;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.function.Consumer;

import static AI.Elsa_AI.Strategy.*;
import static Board.Board.getMainBattlefieldGP;
import static Board.BoardInitializer.getScoreLimit;
import static Board.BoardInitializer.getTeam1Score;
import static Board.BoardInitializer.getTeam2Score;
import static Board.Utils.BoardUtils.*;
import static Board.Utils.GameCellUtils.*;

/**
 * Created by Dmitriy on 25.12.2016.
 */
public class Elsa_AI {
    private GridPane mainGP = getMainBattlefieldGP();
    private int totalSPNumber = getStrategicalPoints();
    private boolean strategicalCellPresence = totalSPNumber > 0;
    private int scoreLimit = getScoreLimit();

    private int enemyTotalUnitNumber;
    private int myTotalUnitNumber;
    private int enemyActiveUnitNumber;
    private int myActiveUnitNumber;
    private int enemyCapturedSPNumber;
    private int myCapturedSPNumber;

    private int totalUnitNumber;
    private int myRangeUnitAmount;
    private int enemyRangeUnitAmount;
    private int myMeleeUnitAmount;
    private int enemyMeleeUnitAmount;
    private int enemyScore = 0;
    private int myScore = 0;
    private int turnNumber = 0;
    private int myArmyStrength;
    private int enemyArmyStrength;

    private Strategy strategy;
    private ArrayList<GameCell> myUnitGCList;
    private ArrayList<GameCell> enemyUnitGCList;
    private static Elsa_AI elsa = null;

    enum Strategy{
        PREFER_CAPTURE, PREFER_RETREAT, PREFER_ATTACK_OR_CAPURE, PREFER_DARE_ATTACK, PREFER_CAUTIOUS_ATTACK, PREFER_NORMAL_ATTACK;
    }

    private Elsa_AI() {}

    public static Elsa_AI getInstance() {
        if (elsa == null)
            elsa = new Elsa_AI();
        return elsa;
    }

    public Strategy getStrategy(){
        turnNumber++;
        myUnitGCList = getUnitCellList(2);
        enemyUnitGCList = getUnitCellList(1);
        myArmyStrength = getTotalUnitCost(2);
        enemyArmyStrength = getTotalUnitCost(1);

        scanBoard();

        if(turnNumber < 5){
            strategy = PREFER_CAPTURE;
        }else
        if(enemyTotalUnitNumber > myTotalUnitNumber && myScore >= enemyScore && myTotalUnitNumber < 4
                && turnNumber > 15 && enemyArmyStrength > myArmyStrength){
            strategy = PREFER_RETREAT;
        }else
        if (myArmyStrength - enemyArmyStrength > 100 && myTotalUnitNumber - enemyTotalUnitNumber > 3){
            strategy = PREFER_DARE_ATTACK;
        }else
        if (myArmyStrength - enemyArmyStrength > 0 && myTotalUnitNumber - enemyTotalUnitNumber > 0){
            strategy = PREFER_ATTACK_OR_CAPURE;
        }else
        if (myArmyStrength <= enemyArmyStrength && myTotalUnitNumber - enemyTotalUnitNumber < 0 && myTotalUnitNumber > 4){
            strategy = PREFER_CAUTIOUS_ATTACK;
        }else{
            strategy = PREFER_NORMAL_ATTACK;
        }
        return strategy;
    }

    public void scanBoard(){
        enemyCapturedSPNumber = getStrategicalPoints(1);
        myCapturedSPNumber = getStrategicalPoints(2);

        enemyTotalUnitNumber = getTotalUnitNumber(1);
        myTotalUnitNumber = getTotalUnitNumber(2);

        enemyActiveUnitNumber = getActiveUnitNumber(1);
        myActiveUnitNumber = getActiveUnitNumber(2);

        totalUnitNumber = getTotalUnitNumber();

        enemyScore = getTeam1Score();
        myScore = getTeam2Score();

        myRangeUnitAmount = getUnitPopularityType(2, UnitTypeNames.RANGE);
        myMeleeUnitAmount = getUnitPopularityType(2, UnitTypeNames.MELEE);

        enemyRangeUnitAmount = getUnitPopularityType(1, UnitTypeNames.RANGE);
        enemyMeleeUnitAmount = getUnitPopularityType(1, UnitTypeNames.MELEE);
    }

    public void doAction(){
        Thread thread = new Thread(() -> {
            Strategy turnStrategy = getStrategy();
            System.out.println(turnStrategy.toString() + "***************************************************** STRATEGY IS SELECTED!");
            for(GameCell gc : myUnitGCList){
                applyTactic(turnStrategy, gc);
            }
        });
        thread.setName("Elsa_AI EXECUTION THREAD");
        thread.start();
    }

    private void applyTactic(Strategy strategy, GameCell gc){
//      int enemyUnitsInMeleeRange = getEnemyUnitsInRangeNumber(gc, 2);
//      int enemyUnitsInShotRange = getEnemyUnitsInRangeNumber(gc, gc.getUnit().getShotRange());
//      int enemyUnitsInRange = getEnemyUnitsInRangeNumber(gc, 10);
//      int unActivatedSPInRange = getStrategicalCellsInSRange(gc, 10);
        String info = "[info] -------------------------- ";
        GameCell nearestEnemyCell = getNearestEnemyUnitCell(gc, 100);
        if(nearestEnemyCell == null) System.out.println(info + "no enemy in range of 100 found");

        GameCell nearestStrategicalCell = getNearestStrategicalCell(gc, 100);
        if(nearestStrategicalCell == null) System.out.println(info + "no SP in range of 100 found");

        GameCell nearestPassableCellToEnemy = getNearestPassableCell(gc, nearestEnemyCell);
        if(nearestPassableCellToEnemy == null) System.out.println(info + "no passable cell near enemy found");

        GameCell anyPassableCell = getAnyPassableCell(gc);
        if(anyPassableCell == null) System.out.println(info + "no passable cell found");

        GameCell furtherShootableCell = getFurtherShootableCell(gc, nearestEnemyCell);
        if(furtherShootableCell == null) System.out.println(info + "no further shootable cell found");

        GameCell furtherFromEnemyCell = getFurtherPassableCell(gc, nearestEnemyCell);
        if(furtherFromEnemyCell == null) System.out.println(info + "no further from enemy cell found");

        GameCell nearestPassableCellToSP = null;
        if(nearestStrategicalCell!=null) {
            nearestPassableCellToSP = getNearestPassableCell(gc, nearestStrategicalCell);
            if (nearestPassableCellToSP == null) System.out.println(info + "no passable cell near SP found");
        }
        GameCell bestTarget = getBestTarget(gc);
        if(bestTarget == null) System.out.println(info + "no best target found");

        Unit currentUnit = gc.getUnit();
        boolean isFast = (currentUnit.getWalkRange() >= 5);
        boolean isWeak = (currentUnit.getCost() <= 30);
        boolean canAttackRange = haveEnemyUnitsInShootingRange(gc);
        boolean canAttackMelee = haveEnemyUnitsInMeleeRange(gc);
        boolean canActivateSP = haveSPInActivationRange(gc);
        if(canAttackMelee || canAttackRange){
            System.out.println(gc.getUnit().getName() + " can attack " + bestTarget.getUnit().getName());
        }

        switch (strategy){
            case PREFER_CAPTURE:{
                if(gc.getUnit() instanceof MeleeUnit){
                    unitCellClick(gc);
                    if(canActivateSP){
                        strategicalCellClick(nearestStrategicalCell);
                    }else
                    if(canAttackMelee){
                        enemyCellClick(bestTarget);
                    }else
                    if (nearestPassableCellToSP != null) {
                        freeCellClick(nearestPassableCellToSP);
                    }else
                    if (nearestPassableCellToEnemy != null) {
                        freeCellClick(nearestPassableCellToEnemy);
                    }else{
                        freeCellClick(anyPassableCell);
                    }
                }else{
                    unitCellClick(gc);
                    if(canActivateSP){
                        strategicalCellClick(nearestStrategicalCell);
                    }else
                    if(canAttackRange){
                        enemyCellClick(bestTarget);
                    }else
                    if (nearestPassableCellToSP != null) {
                        freeCellClick(nearestPassableCellToSP);
                    }else
                    if (furtherShootableCell != null) {
                        freeCellClick(furtherShootableCell);
                    }else{
                        freeCellClick(anyPassableCell);
                    }
                }
                break;
            }
            case PREFER_ATTACK_OR_CAPURE:{
                if(gc.getUnit() instanceof MeleeUnit){
                    unitCellClick(gc);
                    if(canActivateSP){
                        strategicalCellClick(nearestStrategicalCell);
                    }else
                    if(canAttackMelee){
                        enemyCellClick(bestTarget);
                    }else{
                        if (nearestEnemyCell != null && nearestStrategicalCell !=null
                                && isTargetCloserToEtalonThanSource(gc, nearestEnemyCell, nearestStrategicalCell)) {
                            freeCellClick(nearestPassableCellToSP);
                        }else
                        if (nearestEnemyCell != null && nearestStrategicalCell !=null
                                && isTargetFurtherToEtalonThanSource(gc, nearestEnemyCell, nearestStrategicalCell)) {
                            freeCellClick(nearestPassableCellToEnemy);
                        }else
                            freeCellClick(anyPassableCell);
                    }
                }else{
                    unitCellClick(gc);
                    if(canActivateSP){
                        strategicalCellClick(nearestStrategicalCell);
                    }else
                    if(canAttackRange){
                        enemyCellClick(bestTarget);
                    }else
                    if (nearestEnemyCell != null && nearestStrategicalCell !=null
                            && isTargetCloserToEtalonThanSource(gc, nearestEnemyCell, nearestStrategicalCell)) {
                        freeCellClick(nearestPassableCellToSP);
                    }else
                    if (nearestEnemyCell != null && nearestStrategicalCell !=null
                            && isTargetFurtherToEtalonThanSource(gc, nearestEnemyCell, nearestStrategicalCell)) {
                        freeCellClick(furtherShootableCell);
                    }else
                        freeCellClick(anyPassableCell);
                }
                break;
            }
            case PREFER_RETREAT:{
                if(gc.getUnit() instanceof MeleeUnit){
                    unitCellClick(gc);
                    if (furtherFromEnemyCell != null) {
                        freeCellClick(furtherFromEnemyCell);
                    }else
                    if(canAttackMelee){
                        enemyCellClick(bestTarget);
                    }else{
                        freeCellClick(anyPassableCell);
                    }
                }else{
                    unitCellClick(gc);
                    if (furtherFromEnemyCell != null) {
                        freeCellClick(furtherFromEnemyCell);
                    }else
                    if(canAttackRange){
                        enemyCellClick(bestTarget);
                    }else
                    {
                        freeCellClick(anyPassableCell);
                    }
                }
                break;
            }
            case PREFER_DARE_ATTACK:{
                if(gc.getUnit() instanceof MeleeUnit){
                    unitCellClick(gc);
                    if(canAttackMelee){
                        enemyCellClick(bestTarget);
                    }else
                    if (nearestPassableCellToEnemy != null) {
                        freeCellClick(nearestPassableCellToEnemy);
                    }else{
                        freeCellClick(anyPassableCell);
                    }
                }else{
                    unitCellClick(gc);
                    if(canAttackRange){
                        enemyCellClick(bestTarget);
                    }else
                    if (furtherShootableCell != null) {
                        freeCellClick(furtherShootableCell);
                    }else{
                        freeCellClick(anyPassableCell);
                    }
                }
                break;
            }
            case PREFER_NORMAL_ATTACK:{
                if(gc.getUnit() instanceof MeleeUnit){
                    unitCellClick(gc);
                    if(canActivateSP){
                        strategicalCellClick(nearestStrategicalCell);
                    }else
                    if(canAttackMelee){
                        enemyCellClick(bestTarget);
                    }else
                    if (nearestPassableCellToEnemy != null) {
                        freeCellClick(nearestPassableCellToEnemy);
                    }else{
                        freeCellClick(anyPassableCell);
                    }
                }else{
                    unitCellClick(gc);
                    if(canActivateSP){
                        strategicalCellClick(nearestStrategicalCell);
                    }else
                    if(canAttackRange){
                        enemyCellClick(bestTarget);
                    }else
                    if (furtherShootableCell != null) {
                        freeCellClick(furtherShootableCell);
                    }else{
                        freeCellClick(anyPassableCell);
                    }
                }
                break;
            }
            case PREFER_CAUTIOUS_ATTACK:{
                if(gc.getUnit() instanceof MeleeUnit){
                    unitCellClick(gc);
                    if(canAttackMelee){
                        enemyCellClick(bestTarget);
                    }else
                    if(canActivateSP){
                        strategicalCellClick(nearestStrategicalCell);
                    }else
                    if (nearestPassableCellToEnemy != null) {
                        freeCellClick(nearestPassableCellToEnemy);
                    }else{
                        freeCellClick(anyPassableCell);
                    }
                }else{
                    unitCellClick(gc);
                    if(canAttackRange){
                        enemyCellClick(bestTarget);
                    }else
                    if(canActivateSP){
                        strategicalCellClick(nearestStrategicalCell);
                    }else
                    if (furtherShootableCell != null) {
                        freeCellClick(furtherShootableCell);
                    }else{
                        freeCellClick(anyPassableCell);
                    }
                }
                break;
            }
        }
    }

    private void strategicalCellClick(GameCell gc){
        runWithDelay(gc,p->clickOnStrategicalCell(gc), 1);
    }

    private void freeCellClick(GameCell gc){
        runWithDelay(gc,p->clickOnFreeCell(gc), 1);
    }

    private void enemyCellClick(GameCell gc){
        runWithDelay(gc, p->clickOnEnemyUnitCell(gc), 1);
        System.out.println("Attack performed");
    }

    private void unitCellClick(GameCell gc){
        runWithDelay(gc, p->clickOnUnitCell(gc), 1);
    }

    private void runWithDelay(GameCell gc, Consumer<GameCell> action, int delayInSec){
        try{
            Platform.runLater(()-> mouseEntered(gc));
            Platform.runLater(()-> action.accept(gc));
            Thread.sleep(delayInSec * 1000);
            Platform.runLater(()-> mouseExited(gc));
        } catch (InterruptedException exc) {
            throw new Error("Unexpected interruption");
        }

    }
}

// определить, что рядом со свободной сп нет моего юнита, который может ее активировать, и если он есть - искать другую.
