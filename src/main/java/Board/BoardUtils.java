package Board;

import Units.Vehicle;
import javafx.scene.Node;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

/**
 * Created by Dmitriy on 27.10.2016.
 */
public class BoardUtils {

    public static boolean isOnNeighbouringCell(GameCell gc1, GameCell gc2){
        int x1 = gc1.getxCoord();
        int y1 = gc1.getyCoord();
        int x2 = gc2.getxCoord();
        int y2 = gc2.getyCoord();
        return ((x1==x2 && abs(y1-y2)==1)||(y1==y2 && abs(x1-x2)==1));
    }

    private static boolean isReachable(int x1, int y1, int x2, int y2, int Range){
        return ((x1 - x2 < Range && y1 - y2 < Range)&&(x2 - x1 < Range && y2 - y1 < Range));
    }

    public static void showFieldPassability(){
        GridPane gridPane = Board.getMainBattlefieldGP();
        gridPane.getChildren().stream().filter(p->(p instanceof GameCell
                && ((GameCell) p).isPassable() && ((GameCell) p).getUnit()==null))
                .forEach(p->p.setEffect(new InnerShadow()));
    }

    public static void setWalkingArea(GameCell currentcell){
        int walkrange = currentcell.getUnit().getWalkRange();
        int x = currentcell.getxCoord();
        int y = currentcell.getyCoord();
        GridPane gridPane = Board.getMainBattlefieldGP();
        gridPane.getChildren().stream().filter(p->(p instanceof GameCell &&
                isReachable( x,y,((GameCell) p).getxCoord(), ((GameCell) p).getyCoord(), walkrange)))
                .forEach(p->((GameCell) p).setPassable(true));
    }

    public static void abortFieldPassability(){
        GridPane gridPane = Board.getMainBattlefieldGP();
        gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell) p).isPassable()))
                .forEach(p->{
                    p.setEffect(null);
                    ((GameCell) p).setPassable(false);
                    showFieldPassability();
                });
    }

    public static void showShootingRange(){
        GridPane gridPane = Board.getMainBattlefieldGP();
        gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell) p).isInShootingRange() && ((GameCell) p).getUnit()==null))
                .forEach(p -> p.setStyle("-fx-background-color: #00CC33"));
    }

    public static void setShootingArea(GameCell currentCell){
        int shotRange = currentCell.getUnit().getShotRange();
        int x = currentCell.getxCoord();
        int y = currentCell.getyCoord();
        GridPane gridPane = Board.getMainBattlefieldGP();
        gridPane.getChildren().stream().filter(p->
            (p instanceof GameCell && isReachable( x, y, ((GameCell) p).getxCoord(), ((GameCell) p).getyCoord(), shotRange)))
                .forEach(p->((GameCell) p).setInShootingRange(true));
    }

    public static void abortShootingRange(){
        GridPane gridPane = Board.getMainBattlefieldGP();
        gridPane.getChildren().stream().filter(p->(p instanceof GameCell && ((GameCell) p).isInShootingRange()))
                .forEach(p->{
                    p.setStyle(null);
                    ((GameCell) p).setInShootingRange(false);
                    showShootingRange();
                });
    }

    public static void calculateRanges(GameCell gameCell){
        BoardUtils.setWalkingArea(gameCell);
        BoardUtils.setShootingArea(gameCell);
        BoardUtils.showFieldPassability();
        BoardUtils.showShootingRange();
    }

    public static Image getImage(String imagePath){
        String imageUrl = null;
        try {
            File file = new File(imagePath);
            imageUrl = file.toURI().toURL().toString();
        }catch (MalformedURLException e){e.printStackTrace();}
        return new Image(imageUrl, false);
    }

    public static void refreshZOrder() {
        GridPane gridPane = Board.getMainBattlefieldGP();
        ArrayList<Node> nodeList = gridPane.getChildren().stream().filter(p ->
            (p instanceof GameCell && ((GameCell) p).getUnit() != null && (((GameCell) p).getUnit() instanceof Vehicle))
        ).collect(Collectors.toCollection(ArrayList::new));
        nodeList.forEach(Node::toFront);
    }
}
