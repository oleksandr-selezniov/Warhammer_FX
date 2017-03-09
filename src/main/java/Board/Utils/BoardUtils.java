package Board.Utils;

import Board.Board;
import Units.*;
import Units.Enums.UnitClassNames;
import Units.Enums.UnitTypeNames;
import Units.Interfaces.MeleeUnit;
import Units.Interfaces.RangeUnit;
import javafx.scene.Node;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static Board.Board.getBoardWidth;
import static Units.Enums.UnitClassNames.*;
import static Units.Enums.UnitTypeNames.MELEE;
import static Units.Enums.UnitTypeNames.RANGE;
import static java.lang.Math.abs;
import Board.GameCell;

/**
 * Created by Dmitriy on 27.10.2016.
 */
public class BoardUtils {

    public static boolean isOnNeighbouringCell(GameCell gc1, GameCell gc2){
        int x1 = gc1.getxCoord();
        int y1 = gc1.getyCoord();
        int x2 = gc2.getxCoord();
        int y2 = gc2.getyCoord();
        return ((x1==x2 && abs(y1-y2)<5)||(y1==y2 && abs(x1-x2)<5));
    }

    private synchronized static boolean isReachable(int x1, int y1, int x2, int y2, int Range){
        return ((x1 - x2 < Range && y1 - y2 < Range)&&(x2 - x1 < Range && y2 - y1 < Range));
    }

    public static synchronized boolean isReachable(GameCell gc1, GameCell gc2, int Range){
        int x1 = gc1.getxCoord();
        int y1 = gc1.getyCoord();
        int x2 = gc2.getxCoord();
        int y2 = gc2.getyCoord();
        return ((x1 - x2 < Range && y1 - y2 < Range)&&(x2 - x1 < Range && y2 - y1 < Range));
    }

    public static synchronized boolean isOnNeighbouringCellPlusDiagonal(GameCell gc1, GameCell gc2){
        int x1 = gc1.getxCoord();
        int y1 = gc1.getyCoord();
        int x2 = gc2.getxCoord();
        int y2 = gc2.getyCoord();
        return (((x1 - x2) < 2 && (y1 - y2) < 2)&&((x2 - x1) < 2 && (y2 - y1) < 2));
    }

    public static synchronized boolean isTargetCloserToEtalonThanSource(GameCell etalon, GameCell source, GameCell target){
        int x1 = etalon.getxCoord();
        int y1 = etalon.getyCoord();
        int x2 = source.getxCoord();  //yourcell, nearestGC[0], Gamecell p
        int y2 = source.getyCoord();
        int x3 = target.getxCoord();
        int y3 = target.getyCoord();

        return (abs(x3 - x1) + abs(y3 - y1) < abs(x2 - x1) + abs(y2 - y1));
    }

    public static synchronized boolean isTargetFurtherToEtalonThanSource(GameCell etalon, GameCell source, GameCell target){
        int x1 = etalon.getxCoord();
        int y1 = etalon.getyCoord();
        int x2 = source.getxCoord();  //yourcell, nearestGC[0], Gamecell p
        int y2 = source.getyCoord();
        int x3 = target.getxCoord();
        int y3 = target.getyCoord();

        return (abs(x3 - x1) + abs(y3 - y1) > abs(x2 - x1) + abs(y2 - y1));
    }

    public static synchronized void showFieldPassability(){
        GridPane gridPane = Board.getMainBattlefieldGP();
        gridPane.getChildren().stream().filter(p->(p instanceof GameCell
                && ((GameCell) p).isPassable() && ((GameCell) p).getGUnit()==null))
                .forEach(p->p.setEffect(new InnerShadow()));
    }

    public static synchronized void setWalkingArea(GameCell currentcell){
        int walkrange = currentcell.getGUnit().getWalkRange();
        GridPane gridPane = Board.getMainBattlefieldGP();
        gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable( currentcell, ((GameCell) p), walkrange)) && !((GameCell) p).isBlocked())
                .forEach(p->{
                    if (isOnNeighbouringCellPlusDiagonal(currentcell,((GameCell) p))){if(((GameCell) p).getGUnit()==null) p.setStyle("-fx-background-color: #F08080");}
                    ((GameCell) p).setPassable(true);});
    }

    static synchronized void abortFieldPassability(){
        GridPane gridPane = Board.getMainBattlefieldGP();
        gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell) p).isPassable()) && !((GameCell) p).isBlocked())
                .forEach(p->{
                    p.setStyle(null);
                    p.setEffect(null);
                    ((GameCell) p).setPassable(false);
                    showFieldPassability();
                });
    }

    public static synchronized void abortShootingRange(){
        GridPane gridPane = Board.getMainBattlefieldGP();
        gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell) p).isInShootingRange()) && !((GameCell) p).isBlocked())
                .forEach(p->{
                    p.setStyle(null);
                    ((GameCell) p).setInShootingRange(false);
                    showShootingRange();
                });
    }

    public static synchronized void showShootingRange(){
        GridPane gridPane = Board.getMainBattlefieldGP();
        gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell) p).isInShootingRange() && ((GameCell) p).getGUnit()==null) && !((GameCell) p).isBlocked())
                .forEach(p -> p.setStyle("-fx-background-color: #00CC33"));
    }

    public static synchronized void setShootingArea(GameCell currentCell){
        int shotRange = currentCell.getGUnit().getShotRange();
        GridPane gridPane = Board.getMainBattlefieldGP();

        if(currentCell.getGUnit() instanceof Artillery){
            int deadZone =((Artillery)currentCell.getGUnit()).getDeadZone();
            gridPane.getChildren().stream().filter(p->
                    (p instanceof GameCell)).forEach(p->{
                if (isOnNeighbouringCellPlusDiagonal(currentCell, ((GameCell) p))){if(((GameCell) p).getGUnit()==null) return;}
                if (isReachable(currentCell, ((GameCell) p), deadZone)) return;
                if (isReachable(currentCell, ((GameCell) p), shotRange)){
                    ((GameCell) p).setInShootingRange(true);
                }
            });
        }else {
            gridPane.getChildren().stream().filter(p ->
                    (p instanceof GameCell && isReachable(currentCell, ((GameCell) p), shotRange)))
                    .forEach(p -> {
                        if (isOnNeighbouringCellPlusDiagonal(currentCell, ((GameCell) p))){if(((GameCell) p).getGUnit()==null) return;}
                        ((GameCell) p).setInShootingRange(true);
                    });
        }
    }

    public static synchronized void calculateRanges(GameCell gameCell){
        BoardUtils.setWalkingArea(gameCell);
        BoardUtils.setShootingArea(gameCell);
        BoardUtils.showFieldPassability();
        BoardUtils.showShootingRange();
    }

    public Image getImage(String imagePath){
        ClassLoader classLoader = getClass().getClassLoader();
        URL urlToImage = classLoader.getResource(imagePath);
        return new Image(urlToImage.toString(), false);
    }

    public static synchronized void refreshZOrder() {
        GridPane gridPane = Board.getMainBattlefieldGP();
        ArrayList<Node> nodeList = gridPane.getChildren().stream().filter(p ->
                (p instanceof GameCell && ((GameCell) p).getGUnit() != null)
        ).collect(Collectors.toCollection(ArrayList::new));
        nodeList.forEach(Node::toFront);
    }

    public static synchronized void refreshZOrderUnderGC(GameCell gc) {
        gc.toFront();
        GridPane gridPane = Board.getMainBattlefieldGP();
        ArrayList<Node> nodeList = gridPane.getChildren().stream().filter(p ->
                (p instanceof GameCell && ((GameCell) p).getGUnit() != null && ((GameCell) p).getyCoord() > gc.getyCoord())
        ).collect(Collectors.toCollection(ArrayList::new));
        nodeList.forEach(Node::toFront);
    }

    public static synchronized ArrayList<GameCell> getUnitCellList(int team) {
        GridPane gridPane = Board.getMainBattlefieldGP();
        return gridPane.getChildren().stream().filter(p ->
                (p instanceof GameCell && ((GameCell) p).getGUnit() != null && ((GameCell) p).getGUnit().getTeam()==team)
        ).map(p->(GameCell)p).collect(Collectors.toCollection(ArrayList::new));
    }

    public static synchronized ArrayList<GameCell> getActiveUnitCellList(int team) {
        GridPane gridPane = Board.getMainBattlefieldGP();
        return gridPane.getChildren().stream().filter(p ->
                (p instanceof GameCell && ((GameCell) p).getGUnit() != null && ((GameCell) p).getGUnit().getTeam()==team
                        && ((GameCell) p).getGUnit().isActive())
        ).map(p->(GameCell)p).collect(Collectors.toCollection(ArrayList::new));
    }

    public static synchronized ArrayList<Gui_Unit> getUnitList(int team) {
        ArrayList<Gui_Unit> guiUnitList = new ArrayList<>();
        getUnitCellList(team).forEach(p-> guiUnitList.add( p.getGUnit()));
        return guiUnitList;
    }

    public static synchronized int getTotalUnitCost(int team) {
        int unitCost = 0;
        for(Object u : getUnitCellList(team)){
            unitCost+=((GameCell)u).getGUnit().getCost();
        }
        return unitCost;
    }

    public static synchronized int getEnemyUnitsInRangeNumber(GameCell gc, int range) {
        GridPane gridPane = Board.getMainBattlefieldGP();
        return (int)gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable(gc, ((GameCell) p), range)) && !((GameCell) p).isBlocked())
                .filter(p->
                        ((GameCell) p).getGUnit()!=null && ((GameCell) p).getGUnit().getTeam()!=gc.getGUnit().getTeam()).count();
    }

    public static synchronized boolean haveEnemyUnitsInShootingRange(GameCell gc) {
        if(gc.getGUnit() instanceof RangeUnit){
            return (getEnemyUnitsInRangeNumber(gc, gc.getGUnit().getShotRange()) > 0);
        }
        return false;
    }

    public static synchronized boolean canPerformRangeAttackEfficiently(GameCell gc) {
        if(haveEnemyUnitsInShootingRange(gc)){
            if(getTotalUnitNumber(gc.getGUnit().getTeam()) > 4) {
                ArrayList<GameCell> targetList = getEnemyUnitCellsInSRange(gc, gc.getGUnit().getShotRange());
                long targetNumber = targetList.stream().filter(p -> gc.getGUnit().getCurrentRangeEfficiency(p.getGUnit()) > 0.5 && gc.getGUnit().getCurrentAccuracy(p.getGUnit()) > 0.5).count();
                return targetNumber > 0;
            }
            return true;
        }
        return false;
    }

    public static synchronized boolean haveEnemyUnitsInMeleeRange(GameCell gc) {
        return (getEnemyUnitsInRangeNumber(gc, 2) > 0);
    }

    public static synchronized boolean haveSPInActivationRange(GameCell gc) {
        return (getStrategicalCellsInSRange(gc, 2) > 0);
    }

    public static synchronized boolean haveEnemyUnitsInRange(GameCell gc, int range) {
        return (getEnemyUnitsInRangeNumber(gc, range) > 0);
    }

    public static synchronized ArrayList<GameCell> getEnemyUnitCellsInSRange(GameCell gc, int range) {
        GridPane gridPane = Board.getMainBattlefieldGP();
        return gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable(gc, ((GameCell) p), range)) && !((GameCell) p).isBlocked())
                .filter(p->((GameCell) p).getGUnit()!=null && ((GameCell) p).getGUnit().getTeam()!=gc.getGUnit().getTeam()).
                        map(p->(GameCell)p).collect(Collectors.toCollection(ArrayList::new));
    }

    public static synchronized ArrayList getUnitsInSRange(GameCell gc, int range, int team) {
        GridPane gridPane = Board.getMainBattlefieldGP();
        return gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable(gc, ((GameCell) p), range)) && !((GameCell) p).isBlocked())
                .filter(p->((GameCell) p).getGUnit()!=null && ((GameCell) p).getGUnit().getTeam()==team).collect(Collectors.toCollection(ArrayList::new));
        //collect(Collectors.toList());
    }

    public static synchronized int getUnitsInSRangeNumber(GameCell gc, int range, int team) {
        GridPane gridPane = Board.getMainBattlefieldGP();
        return (int)gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable(gc, ((GameCell) p), range)) && !((GameCell) p).isBlocked())
                .filter(p->
                        ((GameCell) p).getGUnit()!=null && ((GameCell) p).getGUnit().getTeam()==team).count();
    }

    public static GameCell getBestTargetInAttackRange(GameCell source){
        final GameCell[] bestTarget = {null};

        if (source.getGUnit() instanceof RangeUnit && haveEnemyUnitsInShootingRange(source)){
            ArrayList<GameCell> targetList = getEnemyUnitCellsInSRange(source, source.getGUnit().getShotRange());
            bestTarget[0] = targetList.get(0);
            targetList.forEach(p->{

                if(isOnNeighbouringCellPlusDiagonal(p, source)){
                    if(p.getGUnit().getHealth() < ((RangeUnit) source.getGUnit()).getCloseDamage()){
                        bestTarget[0]=p;
                    }
                }else{
                    if(p.getGUnit().getHealth() < ((RangeUnit) source.getGUnit()).getRangeDamage(p.getGUnit())
                            && p.getGUnit().getMaxHealth() > ((RangeUnit) source.getGUnit()).getRangeDamage(p.getGUnit())){
                        bestTarget[0]=p;
                        return;
                    }
                    if(p.getGUnit().getHealth() < ((RangeUnit) source.getGUnit()).getRangeDamage(p.getGUnit())
                            && p.getGUnit().getCost() > (source.getGUnit().getCost())){
                        bestTarget[0]=p;
                        return;
                    }
                    if(((RangeUnit) source.getGUnit()).getRangeDamage(bestTarget[0].getGUnit()) < ((RangeUnit) source.getGUnit()).getRangeDamage(p.getGUnit())
                            && !isOnNeighbouringCellPlusDiagonal(bestTarget[0], source)){
                        bestTarget[0]=p;
                        return;
                    }
                    if(isOnNeighbouringCellPlusDiagonal(bestTarget[0], source) && !isOnNeighbouringCellPlusDiagonal(p, source)){
                        bestTarget[0]=p;
                    }
                }
            });
        }else {
            if (haveEnemyUnitsInMeleeRange(source)) {
                ArrayList<GameCell> targetList = getEnemyUnitCellsInSRange(source, 2);
                bestTarget[0] = targetList.get(0);
                targetList.forEach(p -> {
                    if (p.getGUnit().getHealth() < ((MeleeUnit) source.getGUnit()).getCloseDamage(p.getGUnit())
                            && p.getGUnit().getMaxHealth() > ((MeleeUnit) source.getGUnit()).getCloseDamage(p.getGUnit())) {
                        bestTarget[0] = p;
                        return;
                    }
                    if (p.getGUnit().getHealth() < ((MeleeUnit) source.getGUnit()).getCloseDamage(p.getGUnit())
                            && p.getGUnit().getCost() > source.getGUnit().getCost()) {
                        bestTarget[0] = p;
                        return;
                    }
                    if (((MeleeUnit) source.getGUnit()).getCloseDamage(bestTarget[0].getGUnit()) < ((MeleeUnit) source.getGUnit()).getCloseDamage(p.getGUnit())) {
                        bestTarget[0] = p;
                    }
                });
            }
        }
        return bestTarget[0];
    }

    public static GameCell getBestTargetOnBoard(GameCell source){
        final GameCell[] bestTarget = {null};

        if (source.getGUnit() instanceof RangeUnit){
            ArrayList<GameCell> targetList = getEnemyUnitCellsInSRange(source, getBoardWidth());
            bestTarget[0] = targetList.get(0);
            targetList.forEach(p->{

                RangeUnit hunter = ((RangeUnit)source.getGUnit());
                Gui_Unit oldVictim = bestTarget[0].getGUnit();
                Gui_Unit newVictim = p.getGUnit();

                if(hunter.getRangeDamage(oldVictim)*hunter.getCurrentAccuracy(oldVictim) <
                        hunter.getRangeDamage(newVictim)*hunter.getCurrentAccuracy(newVictim)){
                    bestTarget[0]=p;
                }
            });
        }else {
            ArrayList<GameCell> targetList = getEnemyUnitCellsInSRange(source, getBoardWidth());
            bestTarget[0] = targetList.get(0);
            targetList.forEach(p -> {

                MeleeUnit hunter = ((MeleeUnit)source.getGUnit());
                Gui_Unit oldVictim = bestTarget[0].getGUnit();
                Gui_Unit newVictim = p.getGUnit();

                if(hunter.getCloseDamage(oldVictim) < hunter.getCloseDamage(newVictim)){
                    bestTarget[0]=p;
                }
            });
        }
        return bestTarget[0];
    }

    public static synchronized GameCell getNearestEnemyUnitCell(GameCell yourCell, int maxRange){
        ArrayList<GameCell> enemyGCList = new ArrayList<>();
        GridPane gridPane = Board.getMainBattlefieldGP();

        for(int i=0; i<maxRange; i++){
            if(getEnemyUnitsInRangeNumber(yourCell, i)>0){
                gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                        isReachable(yourCell, ((GameCell) p), maxRange)) && !((GameCell) p).isBlocked())
                        .filter(p->
                                ((GameCell) p).getGUnit()!=null && ((GameCell) p).getGUnit().getTeam()!=yourCell.getGUnit().getTeam()).forEach(s->
                        enemyGCList.add((GameCell) s));
                break;
            }
        }

        if(enemyGCList.size() > 0) {
            if (enemyGCList.size() <= 1) {
                //System.out.println("Nearest Enemy Gui_Unit for " + yourCell.getGUnit().getName() + " is " +enemyGCList.get(0).getGUnit().getName());
                return enemyGCList.get(0);
            } else if (enemyGCList.size() > 1) {
                final GameCell[] nearestGC = {enemyGCList.get(0)};

                enemyGCList.forEach(p->{
                    if(isTargetCloserToEtalonThanSource(yourCell, nearestGC[0], p)){
                        nearestGC[0] = p;  // костыль чтобы запихнуть а лямбду НЕ final переменную
                    }
                });
                //System.out.println("Nearest Enemy Gui_Unit for " + yourCell.getGUnit().getName() + " is " +nearestGC[0].getGUnit().getName());
                return nearestGC[0];
            }
        }
        return null;
    }

    public static synchronized ArrayList<GameCell> getPotentialHunters(GameCell gc, Gui_Unit victim){
        GridPane gridPane = Board.getMainBattlefieldGP();
        return gridPane.getChildren().stream().filter(p->p instanceof GameCell).map(p->(GameCell)p)
                .filter(p->p.getGUnit()!=null).filter(p->p.getGUnit().getTeam()!=victim.getTeam()
                        && (getEnemyUnitCellsInSRange(p, p.getGUnit().getShotRange()).contains(gc) || isOnNeighbouringCellPlusDiagonal(p,gc)))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static synchronized int getPotentialDamage(GameCell sourceCell, GameCell potentialTarget){
        final int[] potentialDamage = {0};
        ArrayList<GameCell> potentialHunters = getPotentialHunters(potentialTarget, sourceCell.getGUnit());
        potentialHunters.forEach(p->{
            if(isOnNeighbouringCellPlusDiagonal(p,potentialTarget)){
                if(p.getGUnit() instanceof MeleeInfantry){
                    potentialDamage[0] += ((MeleeUnit)p.getGUnit()).getCloseDamage(sourceCell.getGUnit());
                }else{
                    potentialDamage[0] += (p.getGUnit().getMinCloseDamage() + p.getGUnit().getMaxCloseDamage())/2;
                }
            }else{
                potentialDamage[0] += ((RangeUnit)p.getGUnit()).getRangeDamage(sourceCell.getGUnit());
            }
        });
        return potentialDamage[0];
    }

    public static synchronized boolean canWalkSomewhere(GameCell gc){
        GridPane gridPane = Board.getMainBattlefieldGP();
        final GameCell[] nearestGC = {gc};

        gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable(gc, ((GameCell) p), gc.getGUnit().getWalkRange()))
                && !((GameCell) p).isBlocked() && ((GameCell) p).getGUnit()==null)
                .forEach(p->{
                    nearestGC[0] = (GameCell)p;  // костыль чтобы запихнуть а лямбду НЕ final переменную
                });

        if(nearestGC[0].equals(gc)){
            System.out.println("Gui_Unit on X="+ nearestGC[0].getxCoord() + " Y=" +nearestGC[0].getyCoord() + " Can't walk anywhere!");
        }
        return(!nearestGC[0].equals(gc));
    }

    public static synchronized boolean canShootSomebody(GameCell gc){
        boolean haveTargets = getEnemyUnitsInRangeNumber(gc, gc.getGUnit().getShotRange())>0;
        boolean haveBullets = gc.getGUnit().getAmmo()>0;
        return (haveBullets && haveTargets);
    }

    public static synchronized boolean canHitSomebody(GameCell gc){
        return getEnemyUnitsInRangeNumber(gc, 2)>0;
    }

    public static synchronized GameCell getNearestPassableCell(GameCell sourceCell, GameCell targetCell){
        GridPane gridPane = Board.getMainBattlefieldGP();
        final GameCell[] nearestGC = {sourceCell};

        gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable(sourceCell,((GameCell) p), sourceCell.getGUnit().getWalkRange()))
                && !((GameCell) p).isBlocked() && ((GameCell) p).getGUnit()==null).forEach(p->{
            if(isTargetCloserToEtalonThanSource(targetCell, nearestGC[0], (GameCell)p)){
                nearestGC[0] = (GameCell)p;  // костыль чтобы запихнуть а лямбду НЕ final переменную
            }
        });

        if(!nearestGC[0].equals(sourceCell)){
            //System.out.println("Nearest Passable cell equals X="+ nearestGC[0].getxCoord() + " Y=" +nearestGC[0].getyCoord());
            return nearestGC[0];
        }
        return null;
    }

    public static synchronized GameCell getFurtherPassableCell(GameCell sourceCell, GameCell targetCell){
        GridPane gridPane = Board.getMainBattlefieldGP();
        final GameCell[] nearestGC = {sourceCell};

        gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable(sourceCell,((GameCell) p), sourceCell.getGUnit().getWalkRange()))
                && !((GameCell) p).isBlocked() && ((GameCell) p).getGUnit()==null).forEach(p->{
            if(isTargetFurtherToEtalonThanSource(targetCell, nearestGC[0], (GameCell)p)){
                nearestGC[0] = (GameCell)p;  // костыль чтобы запихнуть а лямбду НЕ final переменную
            }
        });

        if(!nearestGC[0].equals(sourceCell)){
            //System.out.println("Nearest Passable cell equals X="+ nearestGC[0].getxCoord() + " Y=" +nearestGC[0].getyCoord());
            return nearestGC[0];
        }
        return null;
    }

    public static synchronized GameCell getFurtherShootableCell(GameCell sourceCell, GameCell targetCell){
        if(sourceCell.getGUnit() instanceof RangeUnit) {
            GridPane gridPane = Board.getMainBattlefieldGP();
            final GameCell[] nearestGC = {sourceCell};

            gridPane.getChildren().stream().filter(p -> (p instanceof GameCell &&
                    isReachable(sourceCell, ((GameCell) p), sourceCell.getGUnit().getWalkRange()))
                    && !((GameCell) p).isBlocked() && ((GameCell) p).getGUnit() == null).forEach(p -> {
                if (isTargetCloserToEtalonThanSource(targetCell, nearestGC[0], (GameCell) p)) {
                    nearestGC[0] = (GameCell) p;  // костыль чтобы запихнуть а лямбду НЕ final переменную
                }
            });

            GameCell nearGC = nearestGC[0]; // ближайшая к врагу ячейка
            if (!nearGC.equals(sourceCell)) {
                if (getUnitsInSRangeNumber(nearGC, sourceCell.getGUnit().getShotRange(), 1) > 0) {
                    final GameCell[] furtherGC = {nearGC};

                    gridPane.getChildren().stream().filter(p -> (p instanceof GameCell &&
                            isReachable(nearGC, ((GameCell) p), sourceCell.getGUnit().getWalkRange()))
                            && !((GameCell) p).isBlocked() && ((GameCell) p).getGUnit() == null).forEach(p -> {

                        if (isTargetFurtherToEtalonThanSource(targetCell, furtherGC[0], (GameCell) p) &&
                                (isReachable(targetCell, ((GameCell) p), sourceCell.getGUnit().getShotRange()))) {
                            furtherGC[0] = (GameCell) p;  // костыль чтобы запихнуть а лямбду НЕ final переменную
                        }
                    });
                    System.out.println(sourceCell.getGUnit().getName() + " is going to attack "+ targetCell.getGUnit().getName());
                    //System.out.println("Further Shootable cell equals X=" + furtherGC[0].getxCoord() + " Y=" + furtherGC[0].getyCoord());
                    return furtherGC[0];
                }
            }

            if (!nearGC.equals(sourceCell)) {
                //System.out.println("Nearest (RANGE)Passable cell equals X=" + nearGC.getxCoord() + " Y=" + nearGC.getyCoord());
                return nearGC;
            }
            //System.out.println(sourceCell.getGUnit().getName() + " Reporting: no suitable cells detected!");
            return null;
        }
        //System.out.println("Not Suitable for Melee units!");
        return null;
    }

    public static synchronized GameCell getAnyPassableCell(GameCell sourceCell){
        GridPane gridPane = Board.getMainBattlefieldGP();
        ArrayList<GameCell> anyPassableGC = new ArrayList<>();

        gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable(sourceCell, ((GameCell) p), sourceCell.getGUnit().getWalkRange()))
                && !((GameCell) p).isBlocked() && ((GameCell) p).getGUnit()==null).forEach(p->{
            anyPassableGC.add(((GameCell) p));
        });

        final GameCell[] anyGameCell = {anyPassableGC.get(0)};
        anyPassableGC.forEach(p->{
            if (getPotentialDamage(sourceCell, p) < getPotentialDamage(sourceCell, anyGameCell[0])){
                anyGameCell[0] = p;
            }
        });
        return anyGameCell[0];
    }

    public static synchronized int getStrategicalCellsInSRange(GameCell gc, int range) {
        int x = gc.getxCoord();
        int y = gc.getyCoord();
        GridPane gridPane = Board.getMainBattlefieldGP();

        return (int)gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable( x,y,((GameCell) p).getxCoord(), ((GameCell) p).getyCoord(), range)))
                .filter(p->
                        ((GameCell) p).isStrategical() && ((GameCell) p).getOwner()!=gc.getGUnit().getTeam()).count();
    }

    public static synchronized GameCell getNearestStrategicalCell(GameCell yourCell, int maxRange){
        ArrayList<GameCell> strategicalGCList = new ArrayList<>();
        GridPane gridPane = Board.getMainBattlefieldGP();
        int x = yourCell.getxCoord();
        int y = yourCell.getyCoord();

        for(int i=0; i<maxRange; i++){
            if(getStrategicalCellsInSRange(yourCell, i)>0){
                gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                        isReachable( x,y,((GameCell) p).getxCoord(), ((GameCell) p).getyCoord(), maxRange)))
                        .filter(p->
                                ((GameCell) p).isStrategical() && ((GameCell) p).getOwner()!=yourCell.getGUnit().getTeam()).forEach(s->
                        strategicalGCList.add((GameCell) s));
            }
        }
        if(strategicalGCList.size() > 0) {
            final GameCell[] nearestSP = {strategicalGCList.get(0)};
            strategicalGCList.forEach(p->{
                if(isTargetCloserToEtalonThanSource(yourCell, nearestSP[0], p)
                        && (getUnitsInSRange(p, 2, yourCell.getGUnit().getTeam()).size() == 0
                        || ((getUnitsInSRange(p, 2, yourCell.getGUnit().getTeam()).contains(yourCell))))){
                    nearestSP[0] = p;
                }
            });
            //System.out.println("Nearest SP equals X=" + nearestSP[0].getxCoord() + " Y=" + nearestSP[0].getyCoord());
            return nearestSP[0];
        }
        return null;
    }

    public static synchronized int getUnitPopularityClass(int team, UnitClassNames type){
        Map<UnitClassNames, Integer> unitPopularityMap = new HashMap<>();
        ArrayList<String> unitTypeList = new ArrayList<>();

        getUnitList(team).forEach(p->unitTypeList.add(((Gui_Unit) p).getClass().toString()));

        long ARcount = unitTypeList.stream().filter("Artillery"::equals).count();
        long MIcount = unitTypeList.stream().filter("MeleeInfantry"::equals).count();
        long LIcount = unitTypeList.stream().filter("LightInfantry"::equals).count();
        long HIcount = unitTypeList.stream().filter("HeavyInfantry"::equals).count();
        long VEcount = unitTypeList.stream().filter("Vehicle"::equals).count();

        unitPopularityMap.put(ARTILLERY, (int)ARcount);
        unitPopularityMap.put(MELEE_INFANTRY, (int)MIcount);
        unitPopularityMap.put(LIGHT_INFANTRY, (int)LIcount);
        unitPopularityMap.put(HEAVY_INFANTRY, (int)HIcount);
        unitPopularityMap.put(VEHICLE, (int)VEcount);

        return unitPopularityMap.get(type);
    }

    public static synchronized int getUnitPopularityType(int team, UnitTypeNames type){
        Map<UnitTypeNames, Integer> unitPopularityMap = new HashMap<>();
        ArrayList<UnitTypeNames> unitTypeList = new ArrayList<>();

        getUnitList(team).forEach(p->{
            if(p instanceof RangeUnit){
                unitTypeList.add(RANGE);
            }else{
                unitTypeList.add(MELEE);
            }
        });

        long RAcount = unitTypeList.stream().filter(RANGE::equals).count();
        long MEcount = unitTypeList.stream().filter("Melee"::equals).count();

        unitPopularityMap.put(RANGE, (int)RAcount);
        unitPopularityMap.put(MELEE, (int)MEcount);

        return unitPopularityMap.get(type);
    }

    public static synchronized int getTotalUnitNumber(int team){
        GridPane gridPane = Board.getMainBattlefieldGP();
        return (int)(gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell)p).getGUnit() != null))
                .filter(p->((GameCell)p).getGUnit().getTeam()==team).count());
    }

    public static synchronized int getTotalUnitNumber(){
        GridPane gridPane = Board.getMainBattlefieldGP();
        return (int)(gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell)p).getGUnit() != null))
                .count());
    }

    public static synchronized int getStrategicalPoints(int team){
        GridPane gridPane = Board.getMainBattlefieldGP();
        return (int)(gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell)p).isStrategical()
                && ((GameCell)p).getOwner() == team)).count());
    }

    public static synchronized int getStrategicalPoints(){
        GridPane gridPane = Board.getMainBattlefieldGP();
        return (int)(gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell)p).isStrategical())).count());
    }

    public static synchronized int getActiveUnitNumber(int team){
        GridPane gridPane = Board.getMainBattlefieldGP();
        return (int)(gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell)p).getGUnit() != null))
                .filter(p->((GameCell)p).getGUnit().getTeam()==team && ((GameCell)p).getGUnit().isActive()).count());
    }

    public static synchronized void setActiveTeamUnits(int team, boolean state){
        GridPane gridPane = Board.getMainBattlefieldGP();
        gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell)p).getGUnit() != null))
                .filter(p->((GameCell)p).getGUnit().getTeam()==team)
                .forEach(p->((GameCell)p).getGUnit().setActive(state));
    }

    public synchronized static void showAllThreads(){
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
        for(int i=0; i<threadArray.length; i++) {System.out.println(threadArray[i]);}
    }
}
