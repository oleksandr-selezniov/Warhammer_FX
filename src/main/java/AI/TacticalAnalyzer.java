package AI;

import Board.GameCell;
import Units.Gui_Unit;
import Units.Interfaces.MeleeUnit;
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
    private GameCell furtherShootableCellToNearestEnemy;
    private GameCell furtherFromEnemyCell;
    private GameCell nearestPassableCellToBestTarget;
    private GameCell furtherShootableCellToBestTarget;
    private GameCell nearestPassableCellToSP;
    private GameCell bestTargetInAttackRange;
    private GameCell bestTargetOnBoard;
    private Gui_Unit currentGuiUnit;
    private boolean isFast;
    private boolean isWeak;
    private boolean canAttackMelee;
    private boolean canActivateSP;
    private boolean canAttackRangeEffectively;

    TacticalAnalyzer(GameCell gc){
        currentGameCell = gc;
        nearestEnemyCell = getNearestEnemyUnitCell(gc, 100);
        bestTargetOnBoard = getBestTargetOnBoard(gc);
        nearestStrategicalCell = getNearestStrategicalCell(gc, 100);
        nearestPassableCellToEnemy = getNearestPassableCell(gc, nearestEnemyCell);
        nearestPassableCellToBestTarget = getNearestPassableCell(gc, bestTargetOnBoard);
        furtherShootableCellToBestTarget =  getFurtherShootableCell(gc, bestTargetOnBoard);
        anyPassableCell = getAnyPassableCell(gc);
        furtherShootableCellToNearestEnemy = getFurtherShootableCell(gc, nearestEnemyCell);
        furtherFromEnemyCell = getFurtherPassableCell(gc, nearestEnemyCell);
        if(nearestStrategicalCell!=null) {
            nearestPassableCellToSP = getNearestPassableCell(gc, nearestStrategicalCell);
        }
        bestTargetInAttackRange = getBestTargetInAttackRange(gc);
        currentGuiUnit = gc.getGUnit();
        isFast = (currentGuiUnit.getWalkRange() >= 5);
        isWeak = (currentGuiUnit.getCost() <= 30);
        canAttackRangeEffectively = canPerformRangeAttackEfficiently(gc);
        canAttackMelee = haveEnemyUnitsInMeleeRange(gc);
        canActivateSP = haveSPInActivationRange(gc);
    }

    public void applyTactic(StrategyType strategy){
        TacticalExecutor executor = new TacticalExecutor(this, currentGameCell);

        switch (strategy){
            case PREFER_CAPTURE:{
                if(isFast && isWeak){
                    if(currentGuiUnit instanceof MeleeUnit){
                        executor.preferCaptureMelee();
                    }else executor.preferCaptureRange();
                    break;
                }else if(isFast || isWeak){
                    if(currentGuiUnit instanceof MeleeUnit){
                        executor.preferAttackOrCaptureMelee();
                    }else executor.preferAttackOrCaptureRange();
                    break;
                }else{
                    if(currentGuiUnit instanceof MeleeUnit){
                        executor.preferNormalAttackMelee();
                    }else executor.preferNormalAttackRange();
                    break;
                }
            }
            case PREFER_ATTACK_OR_CAPURE:{
                if(currentGuiUnit instanceof MeleeUnit){
                    executor.preferAttackOrCaptureMelee();
                }else executor.preferAttackOrCaptureRange();
                break;
            }
            case PREFER_RETREAT:{
                if(currentGuiUnit instanceof MeleeUnit){
                    executor.preferRetreatMelee();
                }else executor.preferRetreatRange();
                break;
            }
            case PREFER_DARE_ATTACK:{
                if(currentGuiUnit instanceof MeleeUnit){
                    executor.preferDareAttackMelee();
                }else executor.preferDareAttackRange();
                break;
            }
            case PREFER_NORMAL_ATTACK:{
                if(currentGuiUnit instanceof MeleeUnit){
                    executor.preferNormalAttackMelee();
                }else executor.preferNormalAttackRange();
                break;
            }
            case PREFER_CAUTIOUS_ATTACK:{
                if(currentGuiUnit instanceof MeleeUnit){
                    executor.preferCautiousAttackMelee();
                }else executor.preferCautiousAttackRange();
                break;
            }
            case PREFER_ADVANCED_TARGETING:{
                if(currentGuiUnit instanceof MeleeUnit){
                    executor.preferAdvancedAttackMelee();
                }else executor.preferAdvancedAttackRange();
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
            enemyCellClick(bestTargetInAttackRange);
            return true;
        }
        return false;
    }

    boolean tryToAttackRange(){
        if(canAttackRangeEffectively){
            enemyCellClick(bestTargetInAttackRange);
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
            freeCellClick(furtherShootableCellToNearestEnemy);
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
        if (furtherShootableCellToNearestEnemy != null) {
            freeCellClick(furtherShootableCellToNearestEnemy);
            return true;
        }
        return false;
    }

    boolean tryGoToBestTargetOnBoardForMeleeAttack(){
        if (nearestPassableCellToBestTarget != null) {
            freeCellClick(nearestPassableCellToBestTarget);
            return true;
        }
        return false;
    }

    boolean tryGoToBestTargetOnBoardForRangeAttack(){
        if (furtherShootableCellToBestTarget != null) {
            freeCellClick(furtherShootableCellToBestTarget);
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
