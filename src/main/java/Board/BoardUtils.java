package Board;

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

import static Board.GameCellUtils.generateRandomNumber;
import static Units.Enums.UnitClassNames.*;
import static Units.Enums.UnitTypeNames.MELEE;
import static Units.Enums.UnitTypeNames.RANGE;
import static java.lang.Math.abs;

/**
 * Created by Dmitriy on 27.10.2016.
 */
public class BoardUtils {

    static boolean isOnNeighbouringCell(GameCell gc1, GameCell gc2){
        int x1 = gc1.getxCoord();
        int y1 = gc1.getyCoord();
        int x2 = gc2.getxCoord();
        int y2 = gc2.getyCoord();
        return ((x1==x2 && abs(y1-y2)<5)||(y1==y2 && abs(x1-x2)<5));
    }

    private synchronized static boolean isReachable(int x1, int y1, int x2, int y2, int Range){
        return ((x1 - x2 < Range && y1 - y2 < Range)&&(x2 - x1 < Range && y2 - y1 < Range));
    }

    static synchronized boolean isReachable(GameCell gc1, GameCell gc2, int Range){
        int x1 = gc1.getxCoord();
        int y1 = gc1.getyCoord();
        int x2 = gc2.getxCoord();
        int y2 = gc2.getyCoord();
        return ((x1 - x2 < Range && y1 - y2 < Range)&&(x2 - x1 < Range && y2 - y1 < Range));
    }

    static synchronized boolean isOnNeighbouringCellPlusDiagonal(GameCell gc1, GameCell gc2){
        int x1 = gc1.getxCoord();
        int y1 = gc1.getyCoord();
        int x2 = gc2.getxCoord();
        int y2 = gc2.getyCoord();
        return (((x1 - x2) < 2 && (y1 - y2) < 2)&&((x2 - x1) < 2 && (y2 - y1) < 2));
    }

    private static synchronized boolean isTargetCloserToEtalonThanSource(GameCell etalon, GameCell source, GameCell target){
        int x1 = etalon.getxCoord();
        int y1 = etalon.getyCoord();
        int x2 = source.getxCoord();  //yourcell, nearestGC[0], Gamecell p
        int y2 = source.getyCoord();
        int x3 = target.getxCoord();
        int y3 = target.getyCoord();

        return (abs(x3 - x1) + abs(y3 - y1) < abs(x2 - x1) + abs(y2 - y1));
    }

    private static synchronized boolean isTargetFurtherToEtalonThanEtalon(GameCell etalon, GameCell source, GameCell target){
        int x1 = etalon.getxCoord();
        int y1 = etalon.getyCoord();
        int x2 = source.getxCoord();  //yourcell, nearestGC[0], Gamecell p
        int y2 = source.getyCoord();
        int x3 = target.getxCoord();
        int y3 = target.getyCoord();

        return (abs(x3 - x1) + abs(y3 - y1) > abs(x2 - x1) + abs(y2 - y1));
    }

    static synchronized void showFieldPassability(){
        GridPane gridPane = Board.getMainBattlefieldGP();
        gridPane.getChildren().stream().filter(p->(p instanceof GameCell
                && ((GameCell) p).isPassable() && ((GameCell) p).getUnit()==null))
                .forEach(p->p.setEffect(new InnerShadow()));
    }

    static synchronized void setWalkingArea(GameCell currentcell){
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

    static synchronized void abortShootingRange(){
        GridPane gridPane = Board.getMainBattlefieldGP();
        gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell) p).isInShootingRange()) && !((GameCell) p).isBlocked())
                .forEach(p->{
                    p.setStyle(null);
                    ((GameCell) p).setInShootingRange(false);
                    showShootingRange();
                });
    }

    static synchronized void showShootingRange(){
        GridPane gridPane = Board.getMainBattlefieldGP();
        gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell) p).isInShootingRange() && ((GameCell) p).getUnit()==null) && !((GameCell) p).isBlocked())
                .forEach(p -> p.setStyle("-fx-background-color: #00CC33"));
    }

    static synchronized void setShootingArea(GameCell currentCell){
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

    static synchronized void calculateRanges(GameCell gameCell){
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

    static synchronized void refreshZOrder() {
        GridPane gridPane = Board.getMainBattlefieldGP();
        ArrayList<Node> nodeList = gridPane.getChildren().stream().filter(p ->
                (p instanceof GameCell && ((GameCell) p).getUnit() != null)
        ).collect(Collectors.toCollection(ArrayList::new));
        nodeList.forEach(Node::toFront);
    }

    static synchronized ArrayList getUnitCellList(int team) {
        GridPane gridPane = Board.getMainBattlefieldGP();
        return gridPane.getChildren().stream().filter(p ->
                (p instanceof GameCell && ((GameCell) p).getUnit() != null && ((GameCell) p).getUnit().getTeam()==team)
        ).collect(Collectors.toCollection(ArrayList::new));
    }

    static synchronized ArrayList getUnitList(int team) {
        ArrayList<Unit> unitList = new ArrayList<>();
        getUnitCellList(team).forEach(p->unitList.add(((GameCell) p).getUnit()));
        return unitList;
    }

    static synchronized int getTotalUnitCost(int team) {
        int unitCost = 0;
        for(Object u : getUnitCellList(team)){
            unitCost+=((GameCell)u).getUnit().getCost();
        }
        return unitCost;
    }

    static synchronized int getEnemyUnitsInRangeNumber(GameCell gc, int range) {
        GridPane gridPane = Board.getMainBattlefieldGP();
        return (int)gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable(gc, ((GameCell) p), range)) && !((GameCell) p).isBlocked())
                .filter(p->
                        ((GameCell) p).getUnit()!=null && ((GameCell) p).getUnit().getTeam()!=gc.getUnit().getTeam()).count();
    }

    static synchronized boolean haveEnemyUnitsInShootingRange(GameCell gc) {
        if(!(gc.getUnit() instanceof MeleeUnit)){
            return (getEnemyUnitsInRangeNumber(gc, gc.getUnit().getShotRange()) > 0);
        }
        return false;
    }

    static synchronized boolean haveEnemyUnitsInMeleeRange(GameCell gc) {
        return (getEnemyUnitsInRangeNumber(gc, 2) > 0);
    }

    static synchronized boolean haveEnemyUnitsInRange(GameCell gc, int range) {
        return (getEnemyUnitsInRangeNumber(gc, range) > 0);
    }

    static synchronized ArrayList getEnemyUnitsInSRange(GameCell gc, int range) {
        GridPane gridPane = Board.getMainBattlefieldGP();
        return (ArrayList) gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable(gc, ((GameCell) p), range)) && !((GameCell) p).isBlocked())
                .filter(p->((GameCell) p).getUnit()!=null && ((GameCell) p).getUnit().getTeam()!=gc.getUnit().getTeam()).collect(Collectors.toList());
    }

    static synchronized ArrayList getUnitsInSRange(GameCell gc, int range, int team) {
        GridPane gridPane = Board.getMainBattlefieldGP();
        return (ArrayList) gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable(gc, ((GameCell) p), range)) && !((GameCell) p).isBlocked())
                .filter(p->((GameCell) p).getUnit()!=null && ((GameCell) p).getUnit().getTeam()==team).collect(Collectors.toList());
    }

    static synchronized int getUnitsInSRangeNumber(GameCell gc, int range, int team) {
        GridPane gridPane = Board.getMainBattlefieldGP();
        return (int)gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable(gc, ((GameCell) p), range)) && !((GameCell) p).isBlocked())
                .filter(p->
                        ((GameCell) p).getUnit()!=null && ((GameCell) p).getUnit().getTeam()==team).count();
    }

    static GameCell getBestTarget(GameCell source){
        final GameCell[] bestTarget = {null};

        if (!(source.getUnit() instanceof MeleeUnit) && haveEnemyUnitsInShootingRange(source)){
            ArrayList<GameCell> targetList = getEnemyUnitsInSRange(source, source.getUnit().getShotRange());
            bestTarget[0] = targetList.get(0);
            targetList.forEach(p->{
                if(p.getUnit().getHealth() < ((RangeUnit) source.getUnit()).getCloseDamage() && isOnNeighbouringCellPlusDiagonal(p, source)){
                    bestTarget[0]=p;
                    return;
                }
                if(p.getUnit().getHealth() < ((RangeUnit) source.getUnit()).getRangeDamage(p.getUnit()) && !isOnNeighbouringCellPlusDiagonal(p, source)){
                    bestTarget[0]=p;
                    return;
                }
                if(((RangeUnit) source.getUnit()).getRangeDamage(bestTarget[0].getUnit()) < ((RangeUnit) source.getUnit()).getRangeDamage(p.getUnit())){
                    bestTarget[0]=p;
                }
            });
        }else {
            if (haveEnemyUnitsInMeleeRange(source)) {
                ArrayList<GameCell> targetList = getEnemyUnitsInSRange(source, 2);
                bestTarget[0] = targetList.get(0);
                targetList.forEach(p -> {
                    if (p.getUnit().getHealth() < ((MeleeUnit) source.getUnit()).getCloseDamage(p.getUnit())) {
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

    static synchronized GameCell getNearestEnemyUnitCell(GameCell yourCell, int maxRange){
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
                System.out.println("Nearest Enemy Unit Has coords: X="+enemyGCList.get(0).getxCoord() + " Y=" + enemyGCList.get(0).getyCoord());
                return enemyGCList.get(0);
            } else if (enemyGCList.size() > 1) {
                final GameCell[] nearestGC = {enemyGCList.get(0)};

                enemyGCList.forEach(p->{
                    if(isTargetCloserToEtalonThanSource(yourCell, nearestGC[0], p)){
                        nearestGC[0] = p;  // костыль чтобы запихнуть а лямбду НЕ final переменную
                    }
                });
                System.out.println("Nearest Enemy Unit Has coords: X="+nearestGC[0].getxCoord() + " Y=" + nearestGC[0].getyCoord());
                return nearestGC[0];
            }
        }
        return null;
    }

    static synchronized boolean canWalkSomewhere(GameCell gc){
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

    static synchronized boolean canShootSomebody(GameCell gc){
        boolean haveTargets = getEnemyUnitsInRangeNumber(gc, gc.getUnit().getShotRange())>0;
        boolean haveBullets = gc.getUnit().getAmmo()>0;
        return (haveBullets && haveTargets);
    }

    static synchronized boolean canHitSomebody(GameCell gc){
        return getEnemyUnitsInRangeNumber(gc, 2)>0;
    }

    static synchronized GameCell getNearestPassableCell(GameCell sourceCell, GameCell targetCell){
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
            System.out.println("Nearest Passable cell equals X="+ nearestGC[0].getxCoord() + " Y=" +nearestGC[0].getyCoord());
            return nearestGC[0];
        }
        return null;
    }

    static synchronized GameCell getFurtherShootableCell(GameCell sourceCell, GameCell targetCell){
        if(!(sourceCell.getUnit() instanceof MeleeUnit)) {
            GridPane gridPane = Board.getMainBattlefieldGP();
            final GameCell[] nearestGC = {sourceCell};

            gridPane.getChildren().stream().filter(p -> (p instanceof GameCell &&
                    isReachable(sourceCell, ((GameCell) p), sourceCell.getUnit().getWalkRange()))
                    && !((GameCell) p).isBlocked() && ((GameCell) p).getUnit() == null).forEach(p -> {
                if (isTargetCloserToEtalonThanSource(targetCell, nearestGC[0], (GameCell) p)) {
                    nearestGC[0] = (GameCell) p;  // костыль чтобы запихнуть а лямбду НЕ final переменную
                }
            });

            GameCell nearGC = nearestGC[0];
            if (!nearGC.equals(sourceCell)) {
                if (getUnitsInSRangeNumber(nearGC, sourceCell.getUnit().getShotRange(), 1) > 0) {
                    final GameCell[] furtherGC = {nearGC};

                    gridPane.getChildren().stream().filter(p -> (p instanceof GameCell &&
                            isReachable(nearGC, ((GameCell) p), sourceCell.getUnit().getWalkRange()))
                            && !((GameCell) p).isBlocked() && ((GameCell) p).getUnit() == null).forEach(p -> {

                        if (isTargetFurtherToEtalonThanEtalon(targetCell, furtherGC[0], (GameCell) p) &&
                                (isReachable(nearGC, ((GameCell) p), sourceCell.getUnit().getShotRange()-1))) {
                            furtherGC[0] = (GameCell) p;  // костыль чтобы запихнуть а лямбду НЕ final переменную
                        }
                    });
                    System.out.println("Further Shootable cell equals X=" + furtherGC[0].getxCoord() + " Y=" + furtherGC[0].getyCoord());
                    return furtherGC[0];
                }
            }

            if (!nearGC.equals(sourceCell)) {
                System.out.println("Nearest Passable cell equals X=" + nearGC.getxCoord() + " Y=" + nearGC.getyCoord());
                return nearGC;
            }
            return null;
        }
        System.out.println("Not Suitable for Melee units!");
        return null;
    }

    static synchronized GameCell getAnyPassableCell(GameCell sourceCell){
        GridPane gridPane = Board.getMainBattlefieldGP();
        ArrayList<GameCell> anyPassableGC = new ArrayList<>();

        gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable(sourceCell, ((GameCell) p), sourceCell.getUnit().getWalkRange()))
                && !((GameCell) p).isBlocked() && ((GameCell) p).getUnit()==null).forEach(p->{
            anyPassableGC.add(((GameCell) p));
        });

        if(anyPassableGC.size() > 0) {
            if (anyPassableGC.size() <= 1) {
                return anyPassableGC.get(0);
            } else if (anyPassableGC.size() > 1) {
                return anyPassableGC.get(generateRandomNumber(0, anyPassableGC.size()-1));
            }
        }
        return null;
    }

    static synchronized int getStrategicalCellsInSRange(GameCell gc, int range) {
        int x = gc.getxCoord();
        int y = gc.getyCoord();
        GridPane gridPane = Board.getMainBattlefieldGP();

        return (int)gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable( x,y,((GameCell) p).getxCoord(), ((GameCell) p).getyCoord(), range)) && !((GameCell) p).isBlocked())
                .filter(p->
                        ((GameCell) p).isStrategical() && ((GameCell) p).getOwner()!=gc.getUnit().getTeam()).count();
    }

    static synchronized GameCell getNearestStrategicalCell(GameCell yourCell, int maxRange){
        ArrayList<GameCell> strategicalGCList = new ArrayList<>();
        GridPane gridPane = Board.getMainBattlefieldGP();
        int x = yourCell.getxCoord();
        int y = yourCell.getyCoord();

        for(int i=0; i<maxRange; i++){
            if(getStrategicalCellsInSRange(yourCell, i)>0){

                gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                        isReachable( x,y,((GameCell) p).getxCoord(), ((GameCell) p).getyCoord(), maxRange)) && !((GameCell) p).isBlocked())
                        .filter(p->
                                ((GameCell) p).isStrategical() && ((GameCell) p).getOwner()!=yourCell.getUnit().getTeam()).forEach(s->
                        strategicalGCList.add((GameCell) s));

            }else System.out.println("no enemy units in range "+i);
        }

        if(strategicalGCList.size() > 0) {
            if (strategicalGCList.size() <= 1) {
                return strategicalGCList.get(0);
            } else if (strategicalGCList.size() > 1) {
                return strategicalGCList.get(generateRandomNumber(0, strategicalGCList.size()));
            }
        }
        return null;
    }

    static synchronized int getUnitPopularityClass(int team, UnitClassNames type){
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

    static synchronized int getUnitPopularityType(int team, UnitTypeNames type){
        Map<UnitTypeNames, Integer> unitPopularityMap = new HashMap<>();
        ArrayList<UnitTypeNames> unitTypeList = new ArrayList<>();

        getUnitList(team).forEach(p->{
            if(!(p instanceof MeleeUnit)){
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

    static synchronized int getTotalUnitNumber(int team){
        GridPane gridPane = Board.getMainBattlefieldGP();
        return (int)(gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell)p).getUnit() != null))
                .filter(p->((GameCell)p).getUnit().getTeam()==team).count());
    }

    static synchronized int getTotalUnitNumber(){
        GridPane gridPane = Board.getMainBattlefieldGP();
        return (int)(gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell)p).getUnit() != null))
                .count());
    }

    static synchronized int getStrategicalPoints(int team){
        GridPane gridPane = Board.getMainBattlefieldGP();
        return (int)(gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell)p).isStrategical()
                && ((GameCell)p).getOwner() == team)).count());
    }

    static synchronized int getStrategicalPoints(){
        GridPane gridPane = Board.getMainBattlefieldGP();
        return (int)(gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell)p).isStrategical())).count());
    }

    static synchronized int getActiveUnitNumber(int team){
        GridPane gridPane = Board.getMainBattlefieldGP();
        return (int)(gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell)p).getUnit() != null))
                .filter(p->((GameCell)p).getUnit().getTeam()==team && ((GameCell)p).getUnit().isActive()).count());
    }

    static synchronized void setActiveTeamUnits(int team, boolean state){
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
