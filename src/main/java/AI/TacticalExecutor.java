package AI;

import Board.GameCell;

import static AI.Logger.*;

/**
 * Created by Dmitriy on 08.02.2017.
 */
public class TacticalExecutor {
    private TacticalAnalyzer t;
    private GameCell currentCell;

    TacticalExecutor(TacticalAnalyzer tacticalAnalyzer, GameCell currCell){
        t = tacticalAnalyzer;
        currentCell = currCell;
    }

     void preferCaptureMelee(){
        String unitName = PREFER_CAPTURE_MELEE + currentCell.getUnit().getName();

         t.clickOnCurrentCell();
        if(t.tryToActivateSP()){
            System.out.println(unitName + ACTIVATED_SP);
        }else
        if(t.tryToAttackMelee()){
            System.out.println(unitName + ATTACKED_IN_MELEE);
        }else
        if (t.tryGoToNearestSP()) {
            System.out.println(unitName + WENT_TO_SP);
        }else
        if (t.tryGoToNearestEnemyForMeleeAttack()){
            System.out.println(unitName + WENT_TO_ENEMY);
        }else{
            t.goOnAnyFreeCell();
            System.out.println(unitName + WENT_ON_ANY_CELL);
        }
    }

     void preferCaptureRange(){
        String unitName = PREFER_CAPTURE_RANGE + currentCell.getUnit().getName();

         t.clickOnCurrentCell();
        if(t.tryToActivateSP()){
            System.out.println(unitName + ACTIVATED_SP);
        }else
        if(t.tryToAttackRange()){
            System.out.println(unitName + ATTACKED_FROM_RANGE);
        }else
        if (t.tryGoToNearestSP()) {
            System.out.println(unitName + WENT_TO_SP);
        }else
        if (t.tryGoToNearestEnemyForRangeAttack()){
            System.out.println(unitName + WENT_TO_ENEMY);
        }else{
            t.goOnAnyFreeCell();
            System.out.println(unitName + WENT_ON_ANY_CELL);
        }
    }

    void preferAttackOrCaptureMelee(){
        String unitName = PREFER_ATTACK_OR_CAPTURE_MELEE + currentCell.getUnit().getName();

        t.clickOnCurrentCell();
        if(t.tryToAttackMelee()){
            System.out.println(unitName + ACTIVATED_SP);
        }else
        if(t.tryToAttackMelee()){
            System.out.println(unitName + ATTACKED_IN_MELEE);
        }else
        if (t.tryGoToNearestSPIfCloserThanEnemy()) {
            System.out.println(unitName + WENT_TO_SP_LOWER_DISTANCE);
        }else
        if (t.tryGoToNearestEnemyIfCloserThanSPForMeleeAttack()){
            System.out.println(unitName + WENT_TO_ENEMY_LOWER_DISTANCE);
        }else
        if (t.tryGoToNearestEnemyForMeleeAttack()){
            System.out.println(unitName + WENT_TO_ENEMY);
        }else{
            t.goOnAnyFreeCell();
            System.out.println(unitName + WENT_ON_ANY_CELL);
        }
    }

    void preferAttackOrCaptureRange(){
        String unitName = PREFER_ATTACK_OR_CAPTURE_RANGE + currentCell.getUnit().getName();

        t.clickOnCurrentCell();
        if(t.tryToAttackMelee()){
            System.out.println(unitName + ACTIVATED_SP);
        }else
        if(t.tryToAttackRange()){
            System.out.println(unitName + ATTACKED_FROM_RANGE);
        }else
        if (t.tryGoToNearestSPIfCloserThanEnemy()) {
            System.out.println(unitName + WENT_TO_SP_LOWER_DISTANCE);
        }else
        if (t.tryGoToNearestEnemyIfCloserThanSPForRangeAttack()){
            System.out.println(unitName + WENT_TO_ENEMY_LOWER_DISTANCE);
        }else
        if (t.tryGoToNearestEnemyForRangeAttack()){
            System.out.println(unitName + WENT_TO_ENEMY);
        }else{
            t.goOnAnyFreeCell();
            System.out.println(unitName + WENT_ON_ANY_CELL);
        }
    }

    void preferRetreatMelee(){
        String unitName = PREFER_RETREAT_MELEE + currentCell.getUnit().getName();

        t.clickOnCurrentCell();
        if (t.tryToRunaway()) {
            System.out.println(unitName + RAN_AWAY);
        }else
        if(t.tryToAttackMelee()){
            System.out.println(unitName + ATTACKED_IN_MELEE);
        }else{
            t.goOnAnyFreeCell();
            System.out.println(unitName + WENT_ON_ANY_CELL);
        }
    }

    void preferRetreatRange(){
        String unitName = PREFER_RETREAT_RANGE + currentCell.getUnit().getName();

        t.clickOnCurrentCell();
        if (t.tryToRunaway()) {
            System.out.println(unitName + RAN_AWAY);
        }else
        if(t.tryToAttackRange()){
            System.out.println(unitName + ATTACKED_FROM_RANGE);
        }else{
            t.goOnAnyFreeCell();
            System.out.println(unitName + WENT_ON_ANY_CELL);
        }
    }

    void preferDareAttackMelee(){
        String unitName = PREFER_DARE_ATTACK_MELEE + currentCell.getUnit().getName();

        t.clickOnCurrentCell();
        if(t.tryToAttackMelee()){
            System.out.println(unitName + ATTACKED_IN_MELEE);
        }else
        if (t.tryGoToNearestEnemyForMeleeAttack()) {
            System.out.println(unitName + WENT_TO_ENEMY);
        }else{
            t.goOnAnyFreeCell();
            System.out.println(unitName + WENT_ON_ANY_CELL);
        }
    }

    void preferDareAttackRange(){
        String unitName = PREFER_DARE_ATTACK_RANGE + currentCell.getUnit().getName();

        t.clickOnCurrentCell();
        if(t.tryToAttackRange()){
            System.out.println(unitName + ATTACKED_FROM_RANGE);
        }else
        if (t.tryGoToNearestEnemyForRangeAttack()) {
            System.out.println(unitName + WENT_TO_ENEMY);
        }else{
            t.goOnAnyFreeCell();
            System.out.println(unitName + WENT_ON_ANY_CELL);
        }
    }

    void preferNormalAttackMelee(){
        String unitName = PREFER_NORMAL_ATTACK_MELEE + currentCell.getUnit().getName();

        t.clickOnCurrentCell();
        if(t.tryToActivateSP()){
            System.out.println(unitName + ACTIVATED_SP);
        }else
        if(t.tryToAttackMelee()){
            System.out.println(unitName + ATTACKED_IN_MELEE);
        }else
        if (t.tryGoToNearestEnemyForMeleeAttack()) {
            System.out.println(unitName + WENT_TO_ENEMY);
        }else{
            t.goOnAnyFreeCell();
            System.out.println(unitName + WENT_ON_ANY_CELL);
        }
    }

    void preferNormalAttackRange(){
        String unitName = PREFER_NORMAL_ATTACK_RANGE + currentCell.getUnit().getName();

        t.clickOnCurrentCell();
        if(t.tryToActivateSP()){
            System.out.println(unitName + ACTIVATED_SP);
        }else
        if(t.tryToAttackRange()){
            System.out.println(unitName + ATTACKED_FROM_RANGE);
        }else
        if (t.tryGoToNearestEnemyForRangeAttack()) {
            System.out.println(unitName + WENT_TO_ENEMY);
        }else{
            t.goOnAnyFreeCell();
            System.out.println(unitName + WENT_ON_ANY_CELL);
        }
    }

    void preferCautiousAttackMelee(){
        String unitName = PREFER_CAUTIOUS_ATTACK_MELEE + currentCell.getUnit().getName();

        t.clickOnCurrentCell();
        if(t.tryToAttackMelee()){
            System.out.println(unitName + ATTACKED_IN_MELEE);
        }else
        if(t.tryToActivateSP()){
            System.out.println(unitName + ACTIVATED_SP);
        }else
        if (t.tryGoToNearestEnemyForMeleeAttack()) {
            System.out.println(unitName + WENT_TO_ENEMY);
        }else{
            t.goOnAnyFreeCell();
            System.out.println(unitName + WENT_ON_ANY_CELL);
        }
    }

    void preferCautiousAttackRange(){
        String unitName = PREFER_CAUTIOUS_ATTACK_RANGE + currentCell.getUnit().getName();

        t.clickOnCurrentCell();
        if(t.tryToAttackRange()){
            System.out.println(unitName + ATTACKED_FROM_RANGE);
        }else
        if(t.tryToActivateSP()){
            System.out.println(unitName + ACTIVATED_SP);
        }else
        if (t.tryGoToNearestEnemyForRangeAttack()) {
            System.out.println(unitName + WENT_TO_ENEMY);
        }else{
            t.goOnAnyFreeCell();
            System.out.println(unitName + WENT_ON_ANY_CELL);
        }
    }
}
