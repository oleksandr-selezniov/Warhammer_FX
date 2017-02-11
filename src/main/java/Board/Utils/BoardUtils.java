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
                && ((GameCell) p).isPassable() && ((GameCell) p).getUnit()==null))
                .forEach(p->p.setEffect(new InnerShadow()));
    }

    public static synchronized void setWalkingArea(GameCell currentcell){
        int walkrange = currentcell.getUnit().getWalkRange();
        GridPane gridPane = Board.getMainBattlefieldGP();
        gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable( currentcell, ((GameCell) p), walkrange)) && !((GameCell) p).isBlocked())
                .forEach(p->{
                    if (isOnNeighbouringCellPlusDiagonal(currentcell,((GameCell) p))){if(((GameCell) p).getUnit()==null) p.setStyle("-fx-background-color: #F08080");}
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
        gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell) p).isInShootingRange() && ((GameCell) p).getUnit()==null) && !((GameCell) p).isBlocked())
                .forEach(p -> p.setStyle("-fx-background-color: #00CC33"));
    }

    public static synchronized void setShootingArea(GameCell currentCell){
        int shotRange = currentCell.getUnit().getShotRange();
        GridPane gridPane = Board.getMainBattlefieldGP();

        if(currentCell.getUnit() instanceof Artillery){
            int deadZone =((Artillery)currentCell.getUnit()).getDeadZone();
            gridPane.getChildren().stream().filter(p->
                    (p instanceof GameCell)).forEach(p->{
                if (isOnNeighbouringCellPlusDiagonal(currentCell, ((GameCell) p))){if(((GameCell) p).getUnit()==null) return;}
                if (isReachable(currentCell, ((GameCell) p), deadZone)) return;
                if (isReachable(currentCell, ((GameCell) p), shotRange)){
                    ((GameCell) p).setInShootingRange(true);
                }
            });
        }else {
            gridPane.getChildren().stream().filter(p ->
                    (p instanceof GameCell && isReachable(currentCell, ((GameCell) p), shotRange)))
                    .forEach(p -> {
                        if (isOnNeighbouringCellPlusDiagonal(currentCell, ((GameCell) p))){if(((GameCell) p).getUnit()==null) return;}
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
                (p instanceof GameCell && ((GameCell) p).getUnit() != null)
        ).collect(Collectors.toCollection(ArrayList::new));
        nodeList.forEach(Node::toFront);
    }

    public static synchronized ArrayList<GameCell> getUnitCellList(int team) {
        GridPane gridPane = Board.getMainBattlefieldGP();
        return gridPane.getChildren().stream().filter(p ->
                (p instanceof GameCell && ((GameCell) p).getUnit() != null && ((GameCell) p).getUnit().getTeam()==team)
        ).map(p->(GameCell)p).collect(Collectors.toCollection(ArrayList::new));
    }

    public static synchronized ArrayList<Unit> getUnitList(int team) {
        ArrayList<Unit> unitList = new ArrayList<>();
        getUnitCellList(team).forEach(p->unitList.add( p.getUnit()));
        return unitList;
    }

    public static synchronized int getTotalUnitCost(int team) {
        int unitCost = 0;
        for(Object u : getUnitCellList(team)){
            unitCost+=((GameCell)u).getUnit().getCost();
        }
        return unitCost;
    }

    public static synchronized int getEnemyUnitsInRangeNumber(GameCell gc, int range) {
        GridPane gridPane = Board.getMainBattlefieldGP();
        return (int)gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable(gc, ((GameCell) p), range)) && !((GameCell) p).isBlocked())
                .filter(p->
                        ((GameCell) p).getUnit()!=null && ((GameCell) p).getUnit().getTeam()!=gc.getUnit().getTeam()).count();
    }

    public static synchronized boolean haveEnemyUnitsInShootingRange(GameCell gc) {
        if(gc.getUnit() instanceof RangeUnit){
            return (getEnemyUnitsInRangeNumber(gc, gc.getUnit().getShotRange()) > 0);
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

    public static synchronized ArrayList<GameCell> getEnemyUnitsInSRange(GameCell gc, int range) {
        GridPane gridPane = Board.getMainBattlefieldGP();
        return gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable(gc, ((GameCell) p), range)) && !((GameCell) p).isBlocked())
                .filter(p->((GameCell) p).getUnit()!=null && ((GameCell) p).getUnit().getTeam()!=gc.getUnit().getTeam()).
                        map(p->(GameCell)p).collect(Collectors.toCollection(ArrayList::new));
    }

    public static synchronized ArrayList getUnitsInSRange(GameCell gc, int range, int team) {
        GridPane gridPane = Board.getMainBattlefieldGP();
        return gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable(gc, ((GameCell) p), range)) && !((GameCell) p).isBlocked())
                .filter(p->((GameCell) p).getUnit()!=null && ((GameCell) p).getUnit().getTeam()==team).collect(Collectors.toCollection(ArrayList::new));
                        //collect(Collectors.toList());
    }

    public static synchronized int getUnitsInSRangeNumber(GameCell gc, int range, int team) {
        GridPane gridPane = Board.getMainBattlefieldGP();
        return (int)gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable(gc, ((GameCell) p), range)) && !((GameCell) p).isBlocked())
                .filter(p->
                        ((GameCell) p).getUnit()!=null && ((GameCell) p).getUnit().getTeam()==team).count();
    }

    public static GameCell getBestTarget(GameCell source){
        final GameCell[] bestTarget = {null};

        if (source.getUnit() instanceof RangeUnit && haveEnemyUnitsInShootingRange(source)){
            ArrayList<GameCell> targetList = getEnemyUnitsInSRange(source, source.getUnit().getShotRange());
            bestTarget[0] = targetList.get(0);
            targetList.forEach(p->{

                if(isOnNeighbouringCellPlusDiagonal(p, source)){
                    if(p.getUnit().getHealth() < ((RangeUnit) source.getUnit()).getCloseDamage()){
                        bestTarget[0]=p;
                    }
                }else{
                    if(p.getUnit().getHealth() < ((RangeUnit) source.getUnit()).getRangeDamage(p.getUnit())
                            && p.getUnit().getMaxHealth() > ((RangeUnit) source.getUnit()).getRangeDamage(p.getUnit())){
                        bestTarget[0]=p;
                        return;
                    }
                    if(p.getUnit().getHealth() < ((RangeUnit) source.getUnit()).getRangeDamage(p.getUnit())
                            && p.getUnit().getCost() > (source.getUnit().getCost())){
                        bestTarget[0]=p;
                        return;
                    }
                    if(((RangeUnit) source.getUnit()).getRangeDamage(bestTarget[0].getUnit()) < ((RangeUnit) source.getUnit()).getRangeDamage(p.getUnit())
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
                ArrayList<GameCell> targetList = getEnemyUnitsInSRange(source, 2);
                bestTarget[0] = targetList.get(0);
                targetList.forEach(p -> {
                    if (p.getUnit().getHealth() < ((MeleeUnit) source.getUnit()).getCloseDamage(p.getUnit())
                            && p.getUnit().getMaxHealth() > ((MeleeUnit) source.getUnit()).getCloseDamage(p.getUnit())) {
                        bestTarget[0] = p;
                        return;
                    }
                    if (p.getUnit().getHealth() < ((MeleeUnit) source.getUnit()).getCloseDamage(p.getUnit())
                            && p.getUnit().getCost() > source.getUnit().getCost()) {
                        bestTarget[0] = p;
                        return;
                    }
                    if (((MeleeUnit) source.getUnit()).getCloseDamage(bestTarget[0].getUnit()) < ((MeleeUnit) source.getUnit()).getCloseDamage(p.getUnit())) {
                        bestTarget[0] = p;
                    }
                });
            }
        }
        return bestTarget[0];
    }

    public static GameCell getBestTargetOnBoard(GameCell source){
        final GameCell[] bestTarget = {null};

        if (source.getUnit() instanceof RangeUnit && haveEnemyUnitsInShootingRange(source)){
            ArrayList<GameCell> targetList = getEnemyUnitsInSRange(source, source.getUnit().getShotRange());
            bestTarget[0] = targetList.get(0);
            targetList.forEach(p->{

                if(isOnNeighbouringCellPlusDiagonal(p, source)){
                    if(p.getUnit().getHealth() < ((RangeUnit) source.getUnit()).getCloseDamage()){
                        bestTarget[0]=p;
                    }
                }else{
                    if(p.getUnit().getHealth() < ((RangeUnit) source.getUnit()).getRangeDamage(p.getUnit())
                            && p.getUnit().getMaxHealth() > ((RangeUnit) source.getUnit()).getRangeDamage(p.getUnit())){
                        bestTarget[0]=p;
                        return;
                    }
                    if(p.getUnit().getHealth() < ((RangeUnit) source.getUnit()).getRangeDamage(p.getUnit())
                            && p.getUnit().getCost() > (source.getUnit().getCost())){
                        bestTarget[0]=p;
                        return;
                    }
                    if(((RangeUnit) source.getUnit()).getRangeDamage(bestTarget[0].getUnit()) < ((RangeUnit) source.getUnit()).getRangeDamage(p.getUnit())
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
                ArrayList<GameCell> targetList = getEnemyUnitsInSRange(source, 2);
                bestTarget[0] = targetList.get(0);
                targetList.forEach(p -> {
                    if (p.getUnit().getHealth() < ((MeleeUnit) source.getUnit()).getCloseDamage(p.getUnit())
                            && p.getUnit().getMaxHealth() > ((MeleeUnit) source.getUnit()).getCloseDamage(p.getUnit())) {
                        bestTarget[0] = p;
                        return;
                    }
                    if (p.getUnit().getHealth() < ((MeleeUnit) source.getUnit()).getCloseDamage(p.getUnit())
                            && p.getUnit().getCost() > source.getUnit().getCost()) {
                        bestTarget[0] = p;
                        return;
                    }
                    if (((MeleeUnit) source.getUnit()).getCloseDamage(bestTarget[0].getUnit()) < ((MeleeUnit) source.getUnit()).getCloseDamage(p.getUnit())) {
                        bestTarget[0] = p;
                    }
                });
            }
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
                                ((GameCell) p).getUnit()!=null && ((GameCell) p).getUnit().getTeam()!=yourCell.getUnit().getTeam()).forEach(s->
                        enemyGCList.add((GameCell) s));
                break;
            }
        }

        if(enemyGCList.size() > 0) {
            if (enemyGCList.size() <= 1) {
                //System.out.println("Nearest Enemy Unit for " + yourCell.getUnit().getName() + " is " +enemyGCList.get(0).getUnit().getName());
                return enemyGCList.get(0);
            } else if (enemyGCList.size() > 1) {
                final GameCell[] nearestGC = {enemyGCList.get(0)};

                enemyGCList.forEach(p->{
                    if(isTargetCloserToEtalonThanSource(yourCell, nearestGC[0], p)){
                        nearestGC[0] = p;  // костыль чтобы запихнуть а лямбду НЕ final переменную
                    }
                });
                //System.out.println("Nearest Enemy Unit for " + yourCell.getUnit().getName() + " is " +nearestGC[0].getUnit().getName());
                return nearestGC[0];
            }
        }
        return null;
    }

    public static synchronized boolean canWalkSomewhere(GameCell gc){
        GridPane gridPane = Board.getMainBattlefieldGP();
        final GameCell[] nearestGC = {gc};

        gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable(gc, ((GameCell) p), gc.getUnit().getWalkRange()))
                && !((GameCell) p).isBlocked() && ((GameCell) p).getUnit()==null)
                .forEach(p->{
                    nearestGC[0] = (GameCell)p;  // костыль чтобы запихнуть а лямбду НЕ final переменную
                });

        if(nearestGC[0].equals(gc)){
            System.out.println("Unit on X="+ nearestGC[0].getxCoord() + " Y=" +nearestGC[0].getyCoord() + " Can't walk anywhere!");
        }
        return(!nearestGC[0].equals(gc));
    }

    public static synchronized boolean canShootSomebody(GameCell gc){
        boolean haveTargets = getEnemyUnitsInRangeNumber(gc, gc.getUnit().getShotRange())>0;
        boolean haveBullets = gc.getUnit().getAmmo()>0;
        return (haveBullets && haveTargets);
    }

    public static synchronized boolean canHitSomebody(GameCell gc){
        return getEnemyUnitsInRangeNumber(gc, 2)>0;
    }

    public static synchronized GameCell getNearestPassableCell(GameCell sourceCell, GameCell targetCell){
        GridPane gridPane = Board.getMainBattlefieldGP();
        final GameCell[] nearestGC = {sourceCell};

        gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable(sourceCell,((GameCell) p), sourceCell.getUnit().getWalkRange()))
                && !((GameCell) p).isBlocked() && ((GameCell) p).getUnit()==null).forEach(p->{
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
                isReachable(sourceCell,((GameCell) p), sourceCell.getUnit().getWalkRange()))
                && !((GameCell) p).isBlocked() && ((GameCell) p).getUnit()==null).forEach(p->{
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
        if(sourceCell.getUnit() instanceof RangeUnit) {
            GridPane gridPane = Board.getMainBattlefieldGP();
            final GameCell[] nearestGC = {sourceCell};

            gridPane.getChildren().stream().filter(p -> (p instanceof GameCell &&
                    isReachable(sourceCell, ((GameCell) p), sourceCell.getUnit().getWalkRange()))
                    && !((GameCell) p).isBlocked() && ((GameCell) p).getUnit() == null).forEach(p -> {
                if (isTargetCloserToEtalonThanSource(targetCell, nearestGC[0], (GameCell) p)) {
                    nearestGC[0] = (GameCell) p;  // костыль чтобы запихнуть а лямбду НЕ final переменную
                }
            });

            GameCell nearGC = nearestGC[0]; // ближайшая к врагу ячейка
            if (!nearGC.equals(sourceCell)) {
                if (getUnitsInSRangeNumber(nearGC, sourceCell.getUnit().getShotRange(), 1) > 0) {
                    final GameCell[] furtherGC = {nearGC};

                    gridPane.getChildren().stream().filter(p -> (p instanceof GameCell &&
                            isReachable(nearGC, ((GameCell) p), sourceCell.getUnit().getWalkRange()))
                            && !((GameCell) p).isBlocked() && ((GameCell) p).getUnit() == null).forEach(p -> {

                        if (isTargetFurtherToEtalonThanSource(targetCell, furtherGC[0], (GameCell) p) &&
                                (isReachable(targetCell, ((GameCell) p), sourceCell.getUnit().getShotRange()))) {
                            furtherGC[0] = (GameCell) p;  // костыль чтобы запихнуть а лямбду НЕ final переменную
                        }
                    });
                    System.out.println(sourceCell.getUnit().getName() + " is going to attack "+ targetCell.getUnit().getName());
                    //System.out.println("Further Shootable cell equals X=" + furtherGC[0].getxCoord() + " Y=" + furtherGC[0].getyCoord());
                    return furtherGC[0];
                }
            }

            if (!nearGC.equals(sourceCell)) {
                //System.out.println("Nearest (RANGE)Passable cell equals X=" + nearGC.getxCoord() + " Y=" + nearGC.getyCoord());
                return nearGC;
            }
            //System.out.println(sourceCell.getUnit().getName() + " Reporting: no suitable cells detected!");
            return null;
        }
        //System.out.println("Not Suitable for Melee units!");
        return null;
    }

    public static synchronized GameCell getAnyPassableCell(GameCell sourceCell){
        GridPane gridPane = Board.getMainBattlefieldGP();
        ArrayList<GameCell> anyPassableGC = new ArrayList<>();

        gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable(sourceCell, ((GameCell) p), sourceCell.getUnit().getWalkRange()))
                && !((GameCell) p).isBlocked() && ((GameCell) p).getUnit()==null).forEach(p->{
            anyPassableGC.add(((GameCell) p));
        });

        if(anyPassableGC.size() > 0) {
            if (anyPassableGC.size() <= 1) {
//                System.out.println(sourceCell.getUnit().getName() + ": random cell is chosen");
                return anyPassableGC.get(0);
            } else if (anyPassableGC.size() > 1) {
//                System.out.println(sourceCell.getUnit().getName() + ": random cell is chosen");
                return anyPassableGC.get(GameCellUtils.generateRandomNumber(0, anyPassableGC.size()-1));
            }
        }
        return null;
    }

    public static synchronized int getStrategicalCellsInSRange(GameCell gc, int range) {
        int x = gc.getxCoord();
        int y = gc.getyCoord();
        GridPane gridPane = Board.getMainBattlefieldGP();

        return (int)gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable( x,y,((GameCell) p).getxCoord(), ((GameCell) p).getyCoord(), range)))
                .filter(p->
                        ((GameCell) p).isStrategical() && ((GameCell) p).getOwner()!=gc.getUnit().getTeam()).count();
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
                                ((GameCell) p).isStrategical() && ((GameCell) p).getOwner()!=yourCell.getUnit().getTeam()).forEach(s->
                        strategicalGCList.add((GameCell) s));
            }
        }
        if(strategicalGCList.size() > 0) {
            final GameCell[] nearestSP = {strategicalGCList.get(0)};
            strategicalGCList.forEach(p->{
                if(isTargetCloserToEtalonThanSource(yourCell, nearestSP[0], p)
                        && (getUnitsInSRange(p, 2, yourCell.getUnit().getTeam()).size() == 0
                        || ((getUnitsInSRange(p, 2, yourCell.getUnit().getTeam()).contains(yourCell))))){
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

        getUnitList(team).forEach(p->unitTypeList.add(((Unit) p).getClass().toString()));

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
        return (int)(gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell)p).getUnit() != null))
                .filter(p->((GameCell)p).getUnit().getTeam()==team).count());
    }

    public static synchronized int getTotalUnitNumber(){
        GridPane gridPane = Board.getMainBattlefieldGP();
        return (int)(gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell)p).getUnit() != null))
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
        return (int)(gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell)p).getUnit() != null))
                .filter(p->((GameCell)p).getUnit().getTeam()==team && ((GameCell)p).getUnit().isActive()).count());
    }

    public static synchronized void setActiveTeamUnits(int team, boolean state){
        GridPane gridPane = Board.getMainBattlefieldGP();
        gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell)p).getUnit() != null))
                .filter(p->((GameCell)p).getUnit().getTeam()==team)
                .forEach(p->((GameCell)p).getUnit().setActive(state));
    }

    public synchronized static void setDefaultOpacity(){
        GridPane gridPane = Board.getMainBattlefieldGP();
        gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell)p).getUnit() != null))
                .forEach(p->((GameCell)p).setGraphic(((GameCell) p).getUnit().getImageView(1.0)));
    }

    public synchronized static void showAllThreads(){
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
        for(int i=0; i<threadArray.length; i++) {System.out.println(threadArray[i]);}
    }
}
