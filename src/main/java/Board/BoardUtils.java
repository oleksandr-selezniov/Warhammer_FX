package Board;

import Units.*;
import Units.Enums.UnitClassNames;
import Units.Enums.UnitTypeNames;
import Units.Interfaces.RangeUnit;
import javafx.scene.Node;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

    private static boolean isReachable(int x1, int y1, int x2, int y2, int Range){
        return ((x1 - x2 < Range && y1 - y2 < Range)&&(x2 - x1 < Range && y2 - y1 < Range));
    }

    static boolean isOnNeighbouringCellPlusDiagonal(GameCell gc1, GameCell gc2){
        int x1 = gc1.getxCoord();
        int y1 = gc1.getyCoord();
        int x2 = gc2.getxCoord();
        int y2 = gc2.getyCoord();
        return (((x1 - x2) < 2 && (y1 - y2) < 2)&&((x2 - x1) < 2 && (y2 - y1) < 2));
    }

    static void showFieldPassability(){
        GridPane gridPane = Board.getMainBattlefieldGP();
        gridPane.getChildren().stream().filter(p->(p instanceof GameCell
                && ((GameCell) p).isPassable() && ((GameCell) p).getUnit()==null))
                .forEach(p->p.setEffect(new InnerShadow()));
    }

    static void setWalkingArea(GameCell currentcell){
        int walkrange = currentcell.getUnit().getWalkRange();
        int x = currentcell.getxCoord();
        int y = currentcell.getyCoord();
        GridPane gridPane = Board.getMainBattlefieldGP();
        gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable( x,y,((GameCell) p).getxCoord(), ((GameCell) p).getyCoord(), walkrange)) && !((GameCell) p).isBlocked())
                .forEach(p->{
                    if (isOnNeighbouringCellPlusDiagonal(currentcell,((GameCell) p))){if(((GameCell) p).getUnit()==null) p.setStyle("-fx-background-color: #F08080");}
                    ((GameCell) p).setPassable(true);});
    }

    static void abortFieldPassability(){
        GridPane gridPane = Board.getMainBattlefieldGP();
        gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell) p).isPassable()) && !((GameCell) p).isBlocked())
                .forEach(p->{
                    p.setStyle(null);
                    p.setEffect(null);
                    ((GameCell) p).setPassable(false);
                    showFieldPassability();
                });
    }

    static void abortShootingRange(){
        GridPane gridPane = Board.getMainBattlefieldGP();
        gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell) p).isInShootingRange()) && !((GameCell) p).isBlocked())
                .forEach(p->{
                    p.setStyle(null);
                    ((GameCell) p).setInShootingRange(false);
                    showShootingRange();
                });
    }

    static void showShootingRange(){
        GridPane gridPane = Board.getMainBattlefieldGP();
        gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell) p).isInShootingRange() && ((GameCell) p).getUnit()==null) && !((GameCell) p).isBlocked())
                .forEach(p -> p.setStyle("-fx-background-color: #00CC33"));
    }

    static void setShootingArea(GameCell currentCell){
        int shotRange = currentCell.getUnit().getShotRange();
        int x = currentCell.getxCoord();
        int y = currentCell.getyCoord();
        GridPane gridPane = Board.getMainBattlefieldGP();

        if(currentCell.getUnit() instanceof Artillery){
            int deadZone =((Artillery)currentCell.getUnit()).getDeadZone();
            gridPane.getChildren().stream().filter(p->
                    (p instanceof GameCell)).forEach(p->{
                if (isOnNeighbouringCellPlusDiagonal(currentCell, ((GameCell) p))){if(((GameCell) p).getUnit()==null) return;}
                if (isReachable( x, y, ((GameCell) p).getxCoord(), ((GameCell) p).getyCoord(), deadZone)) return;
                if(isReachable( x, y, ((GameCell) p).getxCoord(), ((GameCell) p).getyCoord(), shotRange)){
                    ((GameCell) p).setInShootingRange(true);
                }
            });
        }else {
            gridPane.getChildren().stream().filter(p ->
                    (p instanceof GameCell && isReachable(x, y, ((GameCell) p).getxCoord(), ((GameCell) p).getyCoord(), shotRange)))
                    .forEach(p -> {
                        if (isOnNeighbouringCellPlusDiagonal(currentCell, ((GameCell) p))){if(((GameCell) p).getUnit()==null) return;}
                        ((GameCell) p).setInShootingRange(true);
                    });
        }
    }

    static void calculateRanges(GameCell gameCell){
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

    static void refreshZOrder() {
        GridPane gridPane = Board.getMainBattlefieldGP();
        ArrayList<Node> nodeList = gridPane.getChildren().stream().filter(p ->
            (p instanceof GameCell && ((GameCell) p).getUnit() != null)
        ).collect(Collectors.toCollection(ArrayList::new));
        nodeList.forEach(Node::toFront);
    }

    static ArrayList getUnitCellList(int team) {
        GridPane gridPane = Board.getMainBattlefieldGP();
        return gridPane.getChildren().stream().filter(p ->
                (p instanceof GameCell && ((GameCell) p).getUnit() != null && ((GameCell) p).getUnit().getTeam()==team)
        ).collect(Collectors.toCollection(ArrayList::new));
    }

    static ArrayList getUnitList(int team) {
        ArrayList<Unit> unitList = new ArrayList<>();
        getUnitCellList(team).forEach(p->unitList.add(((GameCell) p).getUnit()));
        return unitList;
    }

    static int getEnemyUnitsInSRange(GameCell gc, int range) {
        int x = gc.getxCoord();
        int y = gc.getyCoord();
        GridPane gridPane = Board.getMainBattlefieldGP();

        return (int)gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable( x,y,((GameCell) p).getxCoord(), ((GameCell) p).getyCoord(), range)) && !((GameCell) p).isBlocked())
                .filter(p->
                    ((GameCell) p).getUnit()!=null && ((GameCell) p).getUnit().getTeam()!=gc.getUnit().getTeam()).count();
    }

    static GameCell getNearestEnemyUnitCell(GameCell yourCell, int maxRange){
        ArrayList<GameCell> enemyGCList = new ArrayList<>();
        GridPane gridPane = Board.getMainBattlefieldGP();
        int x = yourCell.getxCoord();
        int y = yourCell.getyCoord();

        for(int i=0; i<maxRange; i++){
            if(getEnemyUnitsInSRange(yourCell, i)>0){
                gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                        isReachable( x,y,((GameCell) p).getxCoord(), ((GameCell) p).getyCoord(), maxRange)) && !((GameCell) p).isBlocked())
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
                    if(abs(p.getxCoord() - x) + abs(p.getyCoord() - y) < abs(nearestGC[0].getxCoord() - x) + abs(nearestGC[0].getyCoord() - y)) {
                        nearestGC[0] = (GameCell) p;  // костыль чтобы запихнуть а лямбду НЕ final переменную
                    }
                });
                //GameCell randomGC = enemyGCList.get(generateRandomNumber(0, enemyGCList.size()-1));
                System.out.println("Nearest Enemy Unit Has coords: X="+nearestGC[0].getxCoord() + " Y=" + nearestGC[0].getyCoord());
                return nearestGC[0];
            }
        }
        return null;
    }

    static GameCell getNearestPassableCell(GameCell targetCell, int maxRange){
        ArrayList<GameCell> nearestGCList = new ArrayList<>();
        GridPane gridPane = Board.getMainBattlefieldGP();
        int x = targetCell.getxCoord();
        int y = targetCell.getyCoord();

        for(int i=0; i<maxRange; i++){
            if(getEnemyUnitsInSRange(targetCell, i)>0){
                gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                        isReachable( x,y,((GameCell) p).getxCoord(), ((GameCell) p).getyCoord(), maxRange)) && ((GameCell) p).isPassable())
                        .forEach(s->
                        nearestGCList.add((GameCell) s));
                break;
            }else System.out.println("no enemy units in range "+i);
        }

        if(nearestGCList.size() > 0) {
            if (nearestGCList.size() <= 1) {
                return nearestGCList.get(0);
            } else if (nearestGCList.size() > 1) {
                return nearestGCList.get(generateRandomNumber(0, nearestGCList.size()));
            }
        }
        return null;
    }

    static boolean canWalkSomewhere(GameCell gc){
        GridPane gridPane = Board.getMainBattlefieldGP();
        final GameCell[] nearestGC = {gc};

        int x1 = gc.getxCoord();
        int y1 = gc.getyCoord();

        gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable( x1,y1,((GameCell) p).getxCoord(), ((GameCell) p).getyCoord(), gc.getUnit().getWalkRange()))
                && !((GameCell) p).isBlocked() && ((GameCell) p).getUnit()==null)
                .forEach(p->{
                    nearestGC[0] = (GameCell)p;  // костыль чтобы запихнуть а лямбду НЕ final переменную
                });
        if(nearestGC[0].equals(gc)){
            System.out.println("Unit on X="+ nearestGC[0].getxCoord() + " Y=" +nearestGC[0].getyCoord() + " Can't walk anywhere!");
        }
        return(!nearestGC[0].equals(gc));
    }

    static boolean canShootSomebody(GameCell gc){
        boolean haveTargets = getEnemyUnitsInSRange(gc, gc.getUnit().getShotRange())>0;
        boolean haveBullets = gc.getUnit().getAmmo()>0;
        return (haveBullets && haveTargets);
    }

    static boolean canHitSomebody(GameCell gc){
        return getEnemyUnitsInSRange(gc, 2)>0;
    }

    static GameCell getNearestPassableCell(GameCell sourceCell, GameCell targetCell){
        GridPane gridPane = Board.getMainBattlefieldGP();
        final GameCell[] nearestGC = {sourceCell};

        int x1 = sourceCell.getxCoord();
        int y1 = sourceCell.getyCoord();

        int x2 = targetCell.getxCoord();
        int y2 = targetCell.getyCoord();

        gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable( x1,y1,((GameCell) p).getxCoord(), ((GameCell) p).getyCoord(), sourceCell.getUnit().getWalkRange()))
                && !((GameCell) p).isBlocked() && ((GameCell) p).getUnit()==null).forEach(p->{
                      if(abs(((GameCell) p).getxCoord() - x2) + abs(((GameCell) p).getyCoord() - y2) < abs(nearestGC[0].getxCoord() - x2) + abs(nearestGC[0].getyCoord() - y2)){
                          nearestGC[0] = (GameCell)p;  // костыль чтобы запихнуть а лямбду НЕ final переменную
                      }
                });

    if(!nearestGC[0].equals(sourceCell)){
        System.out.println("Nearest Passable cell equals X="+ nearestGC[0].getxCoord() + " Y=" +nearestGC[0].getyCoord());
    return nearestGC[0];
    }
    return null;
    }

    static GameCell getNearestShootablePassableCell(GameCell targetCell, int maxRange){
        ArrayList<GameCell> nearestGCList = new ArrayList<>();
        GridPane gridPane = Board.getMainBattlefieldGP();
        int x = targetCell.getxCoord();
        int y = targetCell.getyCoord();

        for(int i=0; i<maxRange; i++){
            if(getEnemyUnitsInSRange(targetCell, i)>0){
                gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                        isReachable( x,y,((GameCell) p).getxCoord(), ((GameCell) p).getyCoord(), maxRange)) && ((GameCell) p).isPassable() && ((GameCell) p).isInShootingRange())
                        .forEach(s->
                                nearestGCList.add((GameCell) s));
                break;
            }else System.out.println("no enemy units in range "+i);
        }

        if(nearestGCList.size() > 0) {
            if (nearestGCList.size() <= 1) {
                return nearestGCList.get(0);
            } else if (nearestGCList.size() > 1) {
                return nearestGCList.get(generateRandomNumber(0, nearestGCList.size()));
            }
        }
        return null;
    }

    static int getStrategicalCellsInSRange(GameCell gc, int range) {
        int x = gc.getxCoord();
        int y = gc.getyCoord();
        GridPane gridPane = Board.getMainBattlefieldGP();

        return (int)gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable( x,y,((GameCell) p).getxCoord(), ((GameCell) p).getyCoord(), range)) && !((GameCell) p).isBlocked())
                .filter(p->
                        ((GameCell) p).isStrategical() && ((GameCell) p).getOwner()!=gc.getUnit().getTeam()).count();
    }

    static GameCell getNearestStrategicalCell(GameCell yourCell, int maxRange){
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

    static int getUnitPopularityClass(int team, UnitClassNames type){
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

    static int getUnitPopularityType(int team, UnitTypeNames type){
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

    static int getTotalUnitNumber(int team){
        GridPane gridPane = Board.getMainBattlefieldGP();
        return (int)(gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell)p).getUnit() != null))
                .filter(p->((GameCell)p).getUnit().getTeam()==team).count());
    }

    static int getTotalUnitNumber(){
        GridPane gridPane = Board.getMainBattlefieldGP();
        return (int)(gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell)p).getUnit() != null))
                .count());
    }

    static int getStrategicalPoints(int team){
        GridPane gridPane = Board.getMainBattlefieldGP();
        return (int)(gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell)p).isStrategical()
                && ((GameCell)p).getOwner() == team)).count());
    }

    static int getStrategicalPoints(){
        GridPane gridPane = Board.getMainBattlefieldGP();
        return (int)(gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell)p).isStrategical())).count());
    }

    static int getActiveUnitNumber(int team){
        GridPane gridPane = Board.getMainBattlefieldGP();
        return (int)(gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell)p).getUnit() != null))
                .filter(p->((GameCell)p).getUnit().getTeam()==team && ((GameCell)p).getUnit().isActive()).count());
    }

    static void setActiveTeamUnits(int team, boolean state){
        GridPane gridPane = Board.getMainBattlefieldGP();
        gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell)p).getUnit() != null))
                .filter(p->((GameCell)p).getUnit().getTeam()==team)
                .forEach(p->((GameCell)p).getUnit().setActive(state));
    }

    public static void setDefaultOpacity(){
        GridPane gridPane = Board.getMainBattlefieldGP();
        gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell)p).getUnit() != null))
                .forEach(p->((GameCell)p).setGraphic(((GameCell) p).getUnit().getImageView(1.0)));
    }
}
