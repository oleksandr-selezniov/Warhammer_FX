package Board;

import javafx.scene.control.Tooltip;

/**
 * Created by Glazyrin.D on 1/16/2017.
 */
public class GameCellUtils {

    public static Tooltip getToolTip(GameCell gameCell){
        Tooltip tooltip = new Tooltip(gameCell.getUnit().getInfo());
        tooltip.setAutoHide(false);
        return tooltip;
    }

    public static void placeObstacle(GameCell gameCell, String path){
        gameCell.setBlocked(true);
        gameCell.setCellImage(path, 1);
    }

    public static void makeStrategical(int x, int y){
        GameCell gameCell = (GameCell) Board.getScene().lookup("#" + x + "_" + y);
        gameCell.setStrategical(true);
        gameCell.setBlocked(true);
        gameCell.setCellImage("other/strartegical_point.jpg", 1);
    }

    public static void endGame(){
        BoardUtils.setActiveTeamUnits(1, false);
        BoardUtils.setActiveTeamUnits(2, false);
        Board.getScene().lookup("#end_turn").setDisable(true);
    }

    public static void generateObstacles(double density){
        Board.getMainBattlefieldGP().getChildren().forEach(p->{
            double chance = Math.random();
            if(((GameCell)p).getUnit()==null && !((GameCell)p).isBlocked()
                    && ((GameCell)p).getxCoord()>1 && ((GameCell)p).getyCoord()>1
                    && ((GameCell)p).getxCoord()<(Board.getBoardWidth()-2)
                    && ((GameCell)p).getyCoord()<(Board.getBoardHeight()-2)){
                if(density > chance){
                    String obstacleImagePath = "obstacles/"+generateRandomNumber(1,8)+".jpg";
                    placeObstacle(((GameCell)p), obstacleImagePath);
                }
            }
        });
    }

    public static int generateRandomNumber(int min, int max){
        return min + (int)(Math.random() * ((max - min) + 1));
    }
}
