package Board;

import Units.Unit;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

import static Board.BoardInitializer.getScoreLimit;
import static Board.BoardInitializer.getTeam1Score;
import static Board.BoardInitializer.getTeam2Score;
import static Board.BoardUtils.*;
import static Board.GameCellUtils.*;

/**
 * Created by Dmitriy on 25.12.2016.
 */
public class AI {
    //размер доски
    //количество и присутствие стратегических ячеек
    //количество своих юнитов
    //количество вражеских юнитов
    //очки до победы
    //текущий номер хода
    //мапа со всеми доступными юнитами на этот ход
    //мапа со всеми вражескими юнитами на этот ход

    int boardSize;
    int strategicalCellNumber;
    boolean StrategicalCellPresence = false;

    private int enemyTotalUnitNumber;
    private int myTotalUnitNumber;

    private int enemyActiveUnitNumber;
    private int myActiveUnitNumber;

    private int enemyCapturedSPNumber;
    private int myCapturedSPNumber;

    private int totalSPNumber;
    private int totalUnitNumber;

    private enum Strategy{
        MELEE_MASSACRE, KILL_FROM_DISTANCE, CAPTURE_IT, RUN_AND_WAIT
    }

    private enum Tactic{
        CLOSE_ATTACK, RANGE_ATTACK, MOVE_TO_NEAREST_ENEMY, MOVE_TO_NEAREST_SP, ACTIVATE_SP, RUNAWAY
    }

    int enemyScore;
    int myScore;
    int scoreLimit;
    int turnNumber;
    Board gamingBoard;
    GridPane mainGP;
    Strategy strategy;
    Tactic tactic;
    ArrayList<GameCell> myUnitGCList;
    ArrayList<GameCell> enemyUnitGCList;
    private static AI ai_Elsa = null;

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

        //getUnitPopularity()

        if(enemyTotalUnitNumber - myTotalUnitNumber > 5 && myScore > enemyScore){
            strategy = Strategy.RUN_AND_WAIT;
        }else if (enemyScore - myScore > 15){
            strategy = Strategy.KILL_FROM_DISTANCE;
        }else{
            strategy = Strategy.CAPTURE_IT;
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
        totalUnitNumber = getTotalUnitNumber();

        enemyScore = getTeam1Score();
        myScore = getTeam2Score();
        scoreLimit = getScoreLimit();
    }

    public void doAction(){
        scanBoard();
        for(GameCell gc : myUnitGCList){
            getTactic(gc);
            GameCell enemyCell = getNearestEnemyUnitCell(gc, mainGP.getChildren().size());
            GameCell targetCell = getNearestPassableCell(gc, enemyCell);

            clickOnUnitCell(gc);
            clickOnFreeCell(targetCell);
        }
    }
}
