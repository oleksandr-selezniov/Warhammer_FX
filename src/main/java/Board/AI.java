package Board;

import Units.Enums.UnitTypeNames;
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

    private Strategy strategy;
    private Tactic tactic;
    private ArrayList<GameCell> myUnitGCList;
    private ArrayList<GameCell> enemyUnitGCList;
    private static AI ai_Elsa = null;

    enum Strategy{
        MELEE_MASSACRE, KILL_FROM_DISTANCE, CAPTURE_IT, RUN_AND_WAIT
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
        scanBoard();

        if(turnNumber < 5){
            strategy = CAPTURE_IT;
        }else
        if(enemyTotalUnitNumber - myTotalUnitNumber > 5 && myScore > enemyScore && myTotalUnitNumber < 3){
            strategy = RUN_AND_WAIT;
        }else
        if (myRangeUnitAmount > myMeleeUnitAmount && enemyTotalUnitNumber - myTotalUnitNumber < 10){
            strategy = KILL_FROM_DISTANCE;
        }else
        if (myMeleeUnitAmount > myRangeUnitAmount && enemyTotalUnitNumber - myTotalUnitNumber < 10){
            strategy = MELEE_MASSACRE;
        }else{
            strategy = RUN_AND_WAIT;
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

//        int k = getEnemyUnitsInSRange(gameCell, 50);
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
                GameCell nearestStrategicalCell = getNearestStrategicalCell(gc, 100);
                GameCell nearestSootablePassableCell = getNearestShootablePassableCell(gc, 100);
                int enemyUnitsInMeleeRange = getEnemyUnitsInSRange(gc, 2);
                int enemyUnitsInShotRange = getEnemyUnitsInSRange(gc, gc.getUnit().getShotRange());
                int enemyUnitsInRange = getEnemyUnitsInSRange(gc, 10);
                int unActivatedSPInRange = getStrategicalCellsInSRange(gc, 10);

// tactic for meleeMassacre ---------------------------------------------------------------------------------
                runWithDelay(gc,
                        p->clickOnUnitCell(gc), 1);
                if (nearestPassableCell != null) {
                    runWithDelay(nearestPassableCell,
                            p->clickOnFreeCell(nearestPassableCell), 1);
                }else if (nearestEnemyCell != null && isOnNeighbouringCellPlusDiagonal(nearestEnemyCell, gc)){
                    runWithDelay(nearestEnemyCell,
                            p->clickOnEnemyUnitCell(nearestEnemyCell), 1);
                }else{
                    runWithDelay(anyPassableCell,
                            p->clickOnFreeCell(anyPassableCell), 1);
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
