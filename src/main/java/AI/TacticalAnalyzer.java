package AI;

import Board.GameCell;
import Units.Interfaces.MeleeUnit;
import Units.Unit;
import javafx.application.Platform;
import java.util.function.Consumer;

import static Board.Utils.BoardUtils.*;
import static Board.Utils.BoardUtils.haveSPInActivationRange;
import static Board.Utils.GameCellUtils.*;
import static Board.Utils.GameCellUtils.mouseEntered;
import static Board.Utils.GameCellUtils.mouseExited;

/**
 * Created by Glazyrin.D on 2/7/2017.
 */
public class TacticalAnalyzer {
    private GameCell currentGameCell;
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

    TacticalAnalyzer(GameCell gc){
        currentGameCell = gc;
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

    public void applyTactic(StrategyType strategy){
        TacticalExecutor executor = new TacticalExecutor(this, currentGameCell);

        switch (strategy){
            case PREFER_CAPTURE:{
                if(currentUnit instanceof MeleeUnit){
                    executor.preferCaptureMelee();
                }else executor.preferCaptureRange();
                break;
            }
            case PREFER_ATTACK_OR_CAPURE:{
                if(currentUnit instanceof MeleeUnit){
                    executor.preferAttackOrCaptureMelee();
                }else executor.preferAttackOrCaptureRange();
                break;
            }
            case PREFER_RETREAT:{
                if(currentUnit instanceof MeleeUnit){
                    executor.preferRetreatMelee();
                }else executor.preferRetreatRange();
                break;
            }
            case PREFER_DARE_ATTACK:{
                if(currentUnit instanceof MeleeUnit){
                    executor.preferDareAttackMelee();
                }else executor.preferDareAttackRange();
                break;
            }
            case PREFER_NORMAL_ATTACK:{
                if(currentUnit instanceof MeleeUnit){
                    executor.preferNormalAttackMelee();
                }else executor.preferNormalAttackRange();
                break;
            }
            case PREFER_CAUTIOUS_ATTACK:{
                if(currentUnit instanceof MeleeUnit){
                    executor.preferCautiousAttackMelee();
                }else executor.preferCautiousAttackRange();
                break;
            }
        }
    }

    boolean tryToActivateSP(){
        if(canActivateSP){
            strategicalCellClick(nearestStrategicalCell);
            return true;
        }
        return false;
    }

    boolean tryToAttackMelee(){
        if(canAttackMelee){
            enemyCellClick(bestTarget);
            return true;
        }
        return false;
    }

    boolean tryToAttackRange(){
        if(canAttackRange){
            enemyCellClick(bestTarget);
            return true;
        }
        return false;
    }

    boolean tryGoToNearestSP(){
        if (nearestPassableCellToSP != null) {
            freeCellClick(nearestPassableCellToSP);
            return true;
        }
        return false;
    }

    boolean tryGoToNearestSPIfCloserThanEnemy(){
        if (nearestEnemyCell != null && nearestStrategicalCell !=null
                && isTargetCloserToEtalonThanSource(currentGameCell, nearestEnemyCell, nearestStrategicalCell)) {
            freeCellClick(nearestPassableCellToSP);
            return true;
        }
        return false;
    }

    boolean tryGoToNearestEnemyIfCloserThanSPForMeleeAttack(){
        if (nearestEnemyCell != null && nearestStrategicalCell !=null
                && isTargetFurtherToEtalonThanSource(currentGameCell, nearestEnemyCell, nearestStrategicalCell)) {
            freeCellClick(nearestPassableCellToEnemy);
            return true;
        }
        return false;
    }

    boolean tryGoToNearestEnemyIfCloserThanSPForRangeAttack(){
        if (nearestEnemyCell != null && nearestStrategicalCell !=null
                && isTargetFurtherToEtalonThanSource(currentGameCell, nearestEnemyCell, nearestStrategicalCell)) {
            freeCellClick(furtherShootableCell);
            return true;
        }
        return false;
    }

    boolean tryGoToNearestEnemyForMeleeAttack(){
        if (nearestPassableCellToEnemy != null) {
            freeCellClick(nearestPassableCellToEnemy);
            return true;
        }
        return false;
    }

    boolean tryGoToNearestEnemyForRangeAttack(){
        if (furtherShootableCell != null) {
            freeCellClick(furtherShootableCell);
            return true;
        }
        return false;
    }

    boolean tryToRunaway(){
        if (furtherFromEnemyCell != null) {
            freeCellClick(furtherFromEnemyCell);
            return true;
        }
        return false;
    }

    void goOnAnyFreeCell(){
        freeCellClick(anyPassableCell);
    }

    void clickOnCurrentCell(){
        unitCellClick(currentGameCell);
    }

    private void strategicalCellClick(GameCell gc){
        runWithDelay(gc,p->clickOnStrategicalCell(gc), 1);
    }

    private  void freeCellClick(GameCell gc){
        runWithDelay(gc,p->clickOnFreeCell(gc), 1);
    }

    private void enemyCellClick(GameCell gc){
        runWithDelay(gc, p->clickOnEnemyUnitCell(gc), 1);
    }

    private void unitCellClick(GameCell gc){
        runWithDelay(gc, p->clickOnUnitCell(gc), 1);
    }

    private static void runWithDelay(GameCell gc, Consumer<GameCell> action, int delayInSec){
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
