package Board;

import Units.Enums.UnitTypeNames;
import Units.Interfaces.MeleeUnit;
import Units.Interfaces.RangeUnit;
import Units.Unit;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.function.Consumer;

import static Board.AI.Strategy.*;
import static Board.Board.getMainBattlefieldGP;
import static Board.BoardInitializer.getScoreLimit;
import static Board.BoardInitializer.getTeam1Score;
import static Board.BoardInitializer.getTeam2Score;
import static Board.BoardUtils.*;
import static Board.GameCell.getPreviousGameCell;
import static Board.GameCellUtils.*;

/**
 * Created by Dmitriy on 25.12.2016.
 */
public class AI {
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
    private Tactic tactic;
    private ArrayList<GameCell> myUnitGCList;
    private ArrayList<GameCell> enemyUnitGCList;
    private static AI ai_Elsa = null;

    enum Strategy{
        PREFER_CAPTURE, PREFER_RETREAT, PREFER_RETREAT_CAPURE, PREFER_DARE_ATTACK, PREFER_CAUTIOUS_ATTACK, PREFER_NORMAL_ATTACK;
    }

    enum Tactic{
        CLOSE_ATTACK, RANGE_ATTACK, MOVE_TO_NEAREST_ENEMY, MOVE_TO_NEAREST_SP, ACTIVATE_SP, RUNAWAY
    }

    private AI() {}

    public static AI getInstance() {
        if (ai_Elsa == null)
            ai_Elsa = new AI();
        return ai_Elsa;
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
            strategy = PREFER_NORMAL_ATTACK;
        }else
        if (myArmyStrength <= enemyArmyStrength && myTotalUnitNumber - enemyTotalUnitNumber < 0){
            strategy = PREFER_CAUTIOUS_ATTACK;
        }else{
            strategy = PREFER_RETREAT_CAPURE;
        }
        return strategy;
    }

    public Tactic getTactic(GameCell gameCell){
        scanBoard();
// рядом есть враг в зоне поражения дальним оружием - атаковать его дальним оружием (если оно есть и стратегия не ранэвэй)
// рядом есть враг в зоне поражения ближним оружием - атаковать его ближним оружием (если стратегия не ранэвэй)
// рядом нет врагов на расстоянии 3*walkingRange и стратегия CaptureIt - двигаться к ближайшей стратегической точке
// рядом есть СТ в зоне активации и стратегия CaptureIt - активировать СП.
// рядом есть враги на расстоянии меньше 3*walkingRange, и стратегия не ранэвэй - идти к ближайшему врагу
// дефолтная идти к ближайшему врагу и тд;

//        int k = getEnemyUnitsInRangeNumber(gameCell, 50);
//        int n = getStrategicalCellsInSRange(gameCell, 50);

        return tactic=Tactic.RANGE_ATTACK;
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
            getStrategy();

            for(GameCell gc : myUnitGCList){
                getTactic(gc);
                GameCell nearestEnemyCell = getNearestEnemyUnitCell(gc, 100);
                GameCell nearestPassableCell = getNearestPassableCell(gc, nearestEnemyCell);
                GameCell anyPassableCell = getAnyPassableCell(gc);
                GameCell furtherShootableCell;
                GameCell nearestStrategicalCell = getNearestStrategicalCell(gc, 100);
                furtherShootableCell = getFurtherShootableCell(gc, nearestEnemyCell);
//                int enemyUnitsInMeleeRange = getEnemyUnitsInRangeNumber(gc, 2);
//                int enemyUnitsInShotRange = getEnemyUnitsInRangeNumber(gc, gc.getUnit().getShotRange());
//                int enemyUnitsInRange = getEnemyUnitsInRangeNumber(gc, 10);
//                int unActivatedSPInRange = getStrategicalCellsInSRange(gc, 10);
//                Unit currentUnit = gc.getUnit();
                boolean canAttackRange = haveEnemyUnitsInShootingRange(gc);
                boolean canAttackMelee = haveEnemyUnitsInMeleeRange(gc);
                GameCell bestTarget = getBestTarget(gc);
                if(canAttackMelee || canAttackRange){
                System.out.println(gc.getUnit().getName() + " can attack " + bestTarget.getUnit().getName());
                }
// tactic for meleeMassacre ---------------------------------------------------------------------------------
                if(gc.getUnit() instanceof MeleeUnit){
                    runWithDelay(gc,
                            p->clickOnUnitCell(gc), 1);
                    if(canAttackMelee){
                        runWithDelay(bestTarget,
                                p->clickOnEnemyUnitCell(bestTarget), 1);
                        System.out.println("Attack performed");
                    }else
                    if (nearestPassableCell != null) {
                        runWithDelay(nearestPassableCell,
                                p->clickOnFreeCell(nearestPassableCell), 1);
                    }else{
                        runWithDelay(anyPassableCell,
                                p->clickOnFreeCell(anyPassableCell), 1);
                    }
                }else{
                    runWithDelay(gc,
                            p->clickOnUnitCell(gc), 1);
                    if(canAttackRange){
                        runWithDelay(bestTarget,
                                p->clickOnEnemyUnitCell(bestTarget), 1);
                        System.out.println("Attack performed");
                    }else
                    if (furtherShootableCell != null) {
                        runWithDelay(furtherShootableCell,
                                p->clickOnFreeCell(furtherShootableCell), 1);
                    }else{
                        runWithDelay(anyPassableCell,
                                p->clickOnFreeCell(anyPassableCell), 1);
                    }
                }
            }
        });
        thread.setName("AI EXECUTION THREAD");
        thread.start();
//_____________________________________________________________________________________________________________
    }

    private void runWithDelay(GameCell gc, Consumer<GameCell> action, int delayInSec){
        try{
            Platform.runLater(()-> action.accept(gc));
            Thread.sleep(delayInSec * 1000);
        } catch (InterruptedException exc) {
            throw new Error("Unexpected interruption");
        }

    }
}
