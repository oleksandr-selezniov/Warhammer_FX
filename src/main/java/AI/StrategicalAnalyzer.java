package AI;

import static AI.StrategyType.*;
import static Board.BoardInitializer.getTeam1Score;
import static Board.BoardInitializer.getTeam2Score;
import static Board.Utils.BoardUtils.*;
import static Board.Utils.BoardUtils.getTotalUnitNumber;

/**
 * Created by Glazyrin.D on 2/7/2017.
 */
public class StrategicalAnalyzer {
    private int totalSPNumber = getStrategicalPoints();                                          //[ get total strategical cell NUMBER on the board ]
    private boolean strategicalCellPresence = totalSPNumber > 0;                                 //[ if board have any SP than true ]
    private int enemyTotalUnitNumber;
    private int myTotalUnitNumber;
    private int enemyScore = 0;
    private int myScore = 0;
    private int turnNumber = 0;
    private int myArmyStrength;
    private int enemyArmyStrength;

    private void scanBoard() {
        turnNumber++;
        myArmyStrength = getTotalUnitCost(2);                                                     // [ get team's 2 total unit cost ]
        enemyArmyStrength = getTotalUnitCost(1);                                                  // [ get team's 1 total unit cost ]
        enemyScore = getTeam1Score();                                                             // [ get team's 1 score ]
        myScore = getTeam2Score();                                                                // [ get team's 2 score ]
        enemyTotalUnitNumber = getTotalUnitNumber(1);                                             // [ get total team's 1 unit NUMBER on the board ]
        myTotalUnitNumber = getTotalUnitNumber(2);                                                // [ get total team's 2 unit NUMBER on the board ]
    }

    public StrategyType getStrategy() {
        scanBoard();
        StrategyType strategy;

        if (strategicalCellPresence) {
            if (turnNumber < 5) {
                strategy = PREFER_CAPTURE;
            } else if (enemyTotalUnitNumber > myTotalUnitNumber && myScore >= enemyScore && myTotalUnitNumber < 4
                    && turnNumber > 15 && enemyArmyStrength > myArmyStrength) {
                strategy = PREFER_RETREAT;
            } else if (myArmyStrength - enemyArmyStrength > 100 && myTotalUnitNumber - enemyTotalUnitNumber > 3) {
                strategy = PREFER_DARE_ATTACK;
            } else if (myArmyStrength - enemyArmyStrength > 0 && myTotalUnitNumber - enemyTotalUnitNumber > 0) {
                strategy = PREFER_ATTACK_OR_CAPURE;
            } else if (myArmyStrength <= enemyArmyStrength && myTotalUnitNumber - enemyTotalUnitNumber < 0 && myTotalUnitNumber > 4) {
                strategy = PREFER_CAUTIOUS_ATTACK;
            } else {
                strategy = PREFER_NORMAL_ATTACK;
            }
        } else {
            strategy = PREFER_ADVANCED_TARGETING;
        }
        return strategy;
    }
}
//    enemyUnitGCList = getUnitCellList(1);                                                     [ get Arraylist with all GameCells with Units from team 1 on them ]
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
//    int enemyUnitsInShotRange = getEnemyUnitsInRangeNumber(gc, gc.getGUnit().getShotRange());  [ get enemy unit NUMBER around the given cell in the given range ]
//    int enemyUnitsInRange = getEnemyUnitsInRangeNumber(gc, 10);                               [ get enemy unit NUMBER around the given cell in the given range ]
//    int unActivatedSPInRange = getStrategicalCellsInSRange(gc, 10);                           [ get neutral or enemy strategical cell NUMBER around the given cell in the given range ]
//    myUnitGCList = getUnitCellList(2);                                                        [ get Arraylist with all GameCells with Units from team 2 on them ]


