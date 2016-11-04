import Units.Vehicle;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.stream.Collector;
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
        if(x1 - x2 < Range && y1 - y2 < Range){
            if(x2 - x1 < Range && y2 - y1 < Range) {
                return true;
            }
        }
        return false;
    }

    public static void showFieldPassability(){
        GridPane gridPane = Board.getMainBattlefieldGP();
        for (Node node : gridPane.getChildren()) {
            if (node instanceof GameCell
                    && ((GameCell) node).isPassable()) {
                node.setEffect(new InnerShadow());
            }
        }
    }

    public static void setWalkingArea(GameCell currentcell){
        int walkrange = currentcell.getUnit().getWalkRange();
        int x = currentcell.getxCoord();
        int y = currentcell.getyCoord();

        GridPane gridPane = Board.getMainBattlefieldGP();
        for (Node node : gridPane.getChildren()) {
            if (node instanceof GameCell &&
                    isReachable( x, y, ((GameCell) node).getxCoord(), ((GameCell) node).getyCoord(), walkrange)){
                ((GameCell) node).setPassable(true);
            }
        }
    }

    public static void abortFieldPassability(){
        GridPane gridPane = Board.getMainBattlefieldGP();
        for (Node node : gridPane.getChildren()) {
            if (node instanceof GameCell && ((GameCell) node).getUnit() == null
                    && ((GameCell) node).isPassable()) {
                node.setEffect(null);
                ((GameCell) node).setPassable(false);
                showFieldPassability();
            }
        }
    }

    public static void showShootingRange(){
        GridPane gridPane = Board.getMainBattlefieldGP();
        for (Node node : gridPane.getChildren()) {
            if (node instanceof GameCell
                    && ((GameCell) node).isInShootingRange() && ((GameCell) node).getUnit()==null) {
                DropShadow dropShadow = new DropShadow();
                dropShadow.setOffsetX(2.0f);
                dropShadow.setOffsetY(4.0f);
                dropShadow.setColor(Color.rgb(0, 200, 0, 1));
                if((((GameCell) node).getUnit()==null)){
                    ((GameCell) node).getGraphic().setEffect(dropShadow);
                }
            }
        }
    }

    public static void setShootingArea(GameCell currentcell){
        int shotRange = currentcell.getUnit().getShotRange();
        int x = currentcell.getxCoord();
        int y = currentcell.getyCoord();

        GridPane gridPane = Board.getMainBattlefieldGP();
        for (Node node : gridPane.getChildren()) {
            if (node instanceof GameCell &&
                    isReachable( x, y, ((GameCell) node).getxCoord(), ((GameCell) node).getyCoord(), shotRange)){
                ((GameCell) node).setInShootingRange(true);
            }
        }
    }

    public static void abortShootingRange(){
        GridPane gridPane = Board.getMainBattlefieldGP();
        for (Node node : gridPane.getChildren()) {
            if (node instanceof GameCell && ((GameCell) node).isInShootingRange()) {
                ((GameCell) node).getGraphic().setEffect(null);
                ((GameCell) node).setInShootingRange(false);
                showShootingRange();
            }
        }
    }

    public static ImageView getImageView(String imagePath, int width, int height, double opacity){
        String imageUrl = null;
        ImageView imageView = new ImageView();
        try {
            File file = new File(imagePath);
            imageUrl = file.toURI().toURL().toString();
        }catch (MalformedURLException e){e.printStackTrace();}
        Image image = new Image(imageUrl, false);
        imageView.setImage(image);
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);
        imageView.setOpacity(opacity);
        return imageView;
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
        ArrayList<Node> nodeList = gridPane.getChildren().stream().filter(p -> {
            if (p instanceof GameCell) {
                if (((GameCell) p).getUnit() != null && (((GameCell) p).getUnit() instanceof Vehicle)) {
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toCollection(ArrayList::new));

        nodeList.stream().forEach(Node::toFront);
    }
}
