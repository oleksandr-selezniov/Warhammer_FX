package Board;

import Units.Enums.UnitTypeNames;
import Units.Unit;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

import static Board.AI.Strategy.*;
import static Board.BoardInitializer.getScoreLimit;
import static Board.BoardInitializer.getTeam1Score;
import static Board.BoardInitializer.getTeam2Score;
import static Board.BoardUtils.*;
import static Board.GameCellUtils.*;

/**
 * Created by Dmitriy on 25.12.2016.
 */
public class AI {

    private boolean strategicalCellPresence;
    private int enemyTotalUnitNumber;
    private int myTotalUnitNumber;
    private int enemyActiveUnitNumber;
    private int myActiveUnitNumber;
    private int enemyCapturedSPNumber;
    private int myCapturedSPNumber;
    private int totalSPNumber;
    private int totalUnitNumber;
    private int myRangeUnitAmount;
    private int enemyRangeUnitAmount;
    private int myMeleeUnitAmount;
    private int enemyMeleeUnitAmount;
    private int enemyScore = 0;
    private int myScore = 0;
    private int scoreLimit;
    private int turnNumber = 0;
    private Board gamingBoard;
    private GridPane mainGP;
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

    public static synchronized AI getInstance() {
        if (ai_Elsa == null)
            ai_Elsa = new AI();
        return ai_Elsa;
    }

    public void setBoard(Board gamingBoard) {
        this.gamingBoard = gamingBoard;
    }

    public Strategy getStrategy(){
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
// рядом есть враг в зоне поражения дальним оружием - атаковать его дальним оружием (если оно есть и стратегия не ранэвэй)
// рядом есть враг в зоне поражения ближним оружием - атаковать его ближним оружием (если стратегия не ранэвэй)
// рядом нет врагов на расстоянии 3*walkingRange и стратегия CaptureIt - двигаться к ближайшей стратегической точке
// рядом есть СТ в зоне активации и стратегия CaptureIt - активировать СП.
// рядом есть враги на расстоянии меньше 3*walkingRange, и стратегия не ранэвэй - идти к ближайшему врагу
// дефолтная идти к ближайшему врагу и тд;

        int k = getEnemyUnitsInSRange(gameCell, 50);

        int n = getStrategicalCellsInSRange(gameCell, 50);

        return tactic=Tactic.RANGE_ATTACK;
    }

    public void scanBoard(){
        turnNumber++;

        mainGP = gamingBoard.getMainBattlefieldGP();

        myUnitGCList = getUnitCellList(2);
        enemyUnitGCList = getUnitCellList(1);

        enemyTotalUnitNumber = getTotalUnitNumber(1);
        myTotalUnitNumber = getTotalUnitNumber(2);

        enemyActiveUnitNumber = getActiveUnitNumber(1);
        myActiveUnitNumber = getActiveUnitNumber(2);

        enemyCapturedSPNumber = getStrategicalPoints(1);
        myCapturedSPNumber = getStrategicalPoints(2);

        totalSPNumber = getStrategicalPoints();
        strategicalCellPresence = totalSPNumber > 0;

        myCapturedSPNumber = getStrategicalPoints(2);
        enemyCapturedSPNumber = getStrategicalPoints(1);

        totalUnitNumber = getTotalUnitNumber();

        enemyScore = getTeam1Score();
        myScore = getTeam2Score();
        scoreLimit = getScoreLimit();

        myRangeUnitAmount = getUnitPopularityType(2, UnitTypeNames.RANGE);
        myMeleeUnitAmount = getUnitPopularityType(2, UnitTypeNames.MELEE);

        enemyRangeUnitAmount = getUnitPopularityType(1, UnitTypeNames.RANGE);
        enemyMeleeUnitAmount = getUnitPopularityType(1, UnitTypeNames.MELEE);



    }

    //проблема
    //если юнит не может хоть куда-то ходить, или хоть кого-то атаковать  (или нет снарядов) - пропуск ходв.

    public void doAction(){
        scanBoard();

        for(GameCell gc : myUnitGCList){
            getTactic(gc);
            GameCell nearestEnemyCell = getNearestEnemyUnitCell(gc, mainGP.getChildren().size());
            GameCell nearestPassableCell = getNearestPassableCell(gc, nearestEnemyCell);



// tactic for meleeMassacre ---------------------------------------------------------------------------------
            clickOnUnitCell(gc);
            if(nearestPassableCell!=null){
                clickOnFreeCell(nearestPassableCell);
            }else if(nearestEnemyCell!=null && isOnNeighbouringCellPlusDiagonal(nearestEnemyCell, gc)){
                clickOnEnemyUnitCell(nearestEnemyCell);
            }
//_____________________________________________________________________________________________________________
        }
    }
}
