package AI;

import Board.GameCell;
import Units.Interfaces.MeleeUnit;
import Units.Unit;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.function.Consumer;

import static AI.Elsa_AI.Strategy.*;
import static Board.BoardInitializer.getTeam1Score;
import static Board.BoardInitializer.getTeam2Score;
import static Board.Utils.BoardUtils.*;
import static Board.Utils.GameCellUtils.*;

/**
 * Created by Dmitriy on 25.12.2016.
 */
public class Elsa_AI {
//    private int scoreLimit = getScoreLimit();                                                 [ get the total score limit ]
//    private int myActiveUnitNumber = getActiveUnitNumber(2);                                  [ get info about units that still can walk during THIS turn ]
//    private int enemyCapturedSPNumber = getStrategicalPoints(1);                              [ get number of SP captured by player 1 ]
//    private int myCapturedSPNumber = getStrategicalPoints(1);                                 [ get number of SP captured by Elsa ]
//    private int totalUnitNumber = getTotalUnitNumber();                                       [ get total unit number on the board ]

//    private int myRangeUnitAmount = getUnitPopularityType(2, UnitTypeNames.RANGE);            [get the amount of units of the certain type]
//    private int enemyRangeUnitAmount = getUnitPopularityType(1, UnitTypeNames.RANGE);         [get the amount of units of the certain type]
//    private int myMeleeUnitAmount =  getUnitPopularityType(2, UnitTypeNames.MELEE);           [get the amount of units of the certain type]
//    private int enemyMeleeUnitAmount = getUnitPopularityType(1, UnitTypeNames.MELEE);         [get the amount of units of the certain type]

//    int enemyUnitsInMeleeRange = getEnemyUnitsInRangeNumber(gc, 2);                           [ get enemy unit NUMBER around the given cell in the given range ]
//    int enemyUnitsInShotRange = getEnemyUnitsInRangeNumber(gc, gc.getUnit().getShotRange());  [ get enemy unit NUMBER around the given cell in the given range ]
//    int enemyUnitsInRange = getEnemyUnitsInRangeNumber(gc, 10);                               [ get enemy unit NUMBER around the given cell in the given range ]
//    int unActivatedSPInRange = getStrategicalCellsInSRange(gc, 10);                           [ get neutral or enemy strategical cell NUMBER around the given cell in the given range ]
    //***********************EVERY TURN************************************************
    private int totalSPNumber = getStrategicalPoints();                                       //[ get total strategical cell NUMBER on the board ]
    private boolean strategicalCellPresence = totalSPNumber > 0;                              //[ if board have any SP than true ]

    private int enemyTotalUnitNumber;
    private int myTotalUnitNumber;

    private int enemyScore = 0;
    private int myScore = 0;
    private int turnNumber = 0;
    private int myArmyStrength;
    private int enemyArmyStrength;

    private Strategy strategy;
    private ArrayList<GameCell> myUnitGCList;
    private ArrayList<GameCell> enemyUnitGCList;
    private static Elsa_AI elsa = null;

    //***********************EVERY UNIT************************************************
    private GameCell nearestEnemyCell;
    private GameCell nearestStrategicalCell;
    private GameCell nearestPassableCellToEnemy;
    private GameCell anyPassableCell;
    private GameCell furtherShootableCell;
    private GameCell furtherFromEnemyCell;
    private GameCell nearestPassableCellToSP;
    private GameCell bestTarget;
    private Unit currentUnit;
    private boolean isFast;
    private boolean isWeak;
    private boolean canAttackRange;
    private boolean canAttackMelee;
    private boolean canActivateSP;
    //***********************************************************************

    enum Strategy{
        PREFER_CAPTURE, PREFER_RETREAT, PREFER_ATTACK_OR_CAPURE, PREFER_DARE_ATTACK, PREFER_CAUTIOUS_ATTACK, PREFER_NORMAL_ATTACK;
    }

    private Elsa_AI() {}
    public static Elsa_AI getInstance() {
        if (elsa == null)
            elsa = new Elsa_AI();
        return elsa;
    }

    private void scanBoard(){ //Runs every Turn
        turnNumber++;
        myUnitGCList = getUnitCellList(2);                      // [ get Arraylist with all GameCells with Units from team 2 on them ]
        enemyUnitGCList = getUnitCellList(1);                   // [ get Arraylist with all GameCells with Units from team 1 on them ]
        myArmyStrength = getTotalUnitCost(2);                   // [ get team's 2 total unit cost ]
        enemyArmyStrength = getTotalUnitCost(1);                // [ get team's 1 total unit cost ]
        enemyScore = getTeam1Score();                           // [ get team's 1 score ]
        myScore = getTeam2Score();                              // [ get team's 2 score ]
        enemyTotalUnitNumber = getTotalUnitNumber(1);           // [ get total team's 1 unit NUMBER on the board ]
        myTotalUnitNumber = getTotalUnitNumber(2);              // [ get total team's 2 unit NUMBER on the board ]
    }

    private void performGameCellCalculations(GameCell gc){ //Runs every Unit
        nearestEnemyCell = getNearestEnemyUnitCell(gc, 100);
        nearestStrategicalCell = getNearestStrategicalCell(gc, 100);
        nearestPassableCellToEnemy = getNearestPassableCell(gc, nearestEnemyCell);
        anyPassableCell = getAnyPassableCell(gc);
        furtherShootableCell = getFurtherShootableCell(gc, nearestEnemyCell);
        furtherFromEnemyCell = getFurtherPassableCell(gc, nearestEnemyCell);
        if(nearestStrategicalCell!=null) {
            nearestPassableCellToSP = getNearestPassableCell(gc, nearestStrategicalCell);
        }
        bestTarget = getBestTarget(gc);
        currentUnit = gc.getUnit();
        isFast = (currentUnit.getWalkRange() >= 5);
        isWeak = (currentUnit.getCost() <= 30);
        canAttackRange = haveEnemyUnitsInShootingRange(gc);
        canAttackMelee = haveEnemyUnitsInMeleeRange(gc);
        canActivateSP = haveSPInActivationRange(gc);
    }

    private void resetGameCellCalculations(){
        nearestEnemyCell = null;
        nearestStrategicalCell = null;
        nearestPassableCellToEnemy = null;
        anyPassableCell = null;
        furtherShootableCell = null;
        furtherFromEnemyCell = null;
        nearestPassableCellToSP = null;
        bestTarget = null;
        currentUnit = null;
        isFast = false;
        isWeak = false;
        canAttackRange = false;
        canAttackMelee = false;
        canActivateSP = false;
    }

    private Strategy getStrategy(){
        scanBoard();

        if(strategicalCellPresence){
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
        }else{
            strategy = PREFER_DARE_ATTACK;
        }
        return strategy;
    }

    public void doAction(){
        Thread thread = new Thread(() -> {
            Strategy turnStrategy = getStrategy();
            System.out.println("[Elsa STRATEGY]: " + turnStrategy.toString());
            for(GameCell gc : myUnitGCList){
                performGameCellCalculations(gc);
                applyTactic(turnStrategy, gc);
                resetGameCellCalculations();
            }
        });
        thread.setName("Elsa_AI EXECUTION THREAD");
        thread.start();
    }

    private boolean tryToActivateSP(){
        if(canActivateSP){
            strategicalCellClick(nearestStrategicalCell);
            return true;
        }
        return false;
    }

    private boolean tryToAttackMelee(){
        if(canAttackMelee){
            enemyCellClick(bestTarget);
            return true;
        }
        return false;
    }

    private boolean tryToAttackRange(){
        if(canAttackRange){
            enemyCellClick(bestTarget);
            return true;
        }
        return false;
    }

    private boolean tryToGoToNearestSP(){
        if (nearestPassableCellToSP != null) {
            freeCellClick(nearestPassableCellToSP);
            return true;
        }
        return false;
    }
    private boolean tryGoToNearestEnemyForMeleeAttack(){
        if (nearestPassableCellToEnemy != null) {
            freeCellClick(nearestPassableCellToEnemy);
            return true;
        }
        return false;
    }

    private boolean tryGoToNearestEnemyForRangeAttack(){
        if (furtherShootableCell != null) {
            freeCellClick(furtherShootableCell);
            return true;
        }
        return false;
    }

    private void goOnAnyFreeCell(){
        freeCellClick(anyPassableCell);
    }

    private void applyTactic(Strategy strategy, GameCell gc){
        if(canAttackMelee || canAttackRange){
            System.out.println(gc.getUnit().getName() + " can attack " + bestTarget.getUnit().getName());
        }
        switch (strategy){
            case PREFER_CAPTURE:{
                if(gc.getUnit() instanceof MeleeUnit){
                    unitCellClick(gc);
                    if(tryToActivateSP()){
                        break;
                    }else
                    if(tryToAttackMelee()){
                        break;
                    }else
                    if (tryToGoToNearestSP()) {
                        break;
                    }else
                    if (tryGoToNearestEnemyForMeleeAttack()){
                        break;
                    }else{
                        goOnAnyFreeCell();
                    }
                }else{
                    unitCellClick(gc);
                    if(tryToActivateSP()){
                        break;
                    }else
                    if(tryToAttackRange()){
                        break;
                    }else
                    if (tryToGoToNearestSP()) {
                        break;
                    }else
                    if (tryGoToNearestEnemyForRangeAttack()) {
                        break;
                    }else{
                        goOnAnyFreeCell();
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

