package Board;

import Units.*;
import javafx.scene.Scene;

import static Board.GameCellUtils.generateObstacles;
import static Board.GameCellUtils.makeStrategical;

/**
 * Created by Dmitriy on 27.10.2016.
 */
public class BoardInitializer {
    private Scene currentScene = Board.getScene();
    private static int scoreLimit = 100;
    private static int armyLimit = 400;
    private static int team1Score;
    private static int team2Score;

    void initializeBoard(){
        for(int i=0; i < ChooseBoard.getCurrentHumanList().size(); i++){
            Unit currentUnit = ChooseBoard.getCurrentHumanList().get(i);
            Unit unit = null;

            if(currentUnit instanceof MeleeInfantry){
                unit = new MeleeInfantry(currentUnit.getKey(), currentUnit.getTeam());
            }else
            if(currentUnit instanceof Artillery){
                unit = new Artillery(currentUnit.getKey(), currentUnit.getTeam());
            }else
            if(currentUnit instanceof LightInfantry){
                unit = new LightInfantry(currentUnit.getKey(), currentUnit.getTeam());
            }else
            if(currentUnit instanceof HeavyInfantry){
                unit = new HeavyInfantry(currentUnit.getKey(), currentUnit.getTeam());
            }else
            if(currentUnit instanceof Vehicle){
                unit = new Vehicle(currentUnit.getKey(), currentUnit.getTeam());
            }else{
                System.out.println("Wrong Unit Type Detected");
            }

            placeOnBoard(1,i+1, unit);
        }

        for(int i=0; i < ChooseBoard.getCurrentOrkList().size(); i++){
            Unit currentUnit = ChooseBoard.getCurrentOrkList().get(i);
            Unit unit = null;

            if(currentUnit instanceof MeleeInfantry){
                unit = new MeleeInfantry(currentUnit.getKey(), currentUnit.getTeam());
            }else
            if(currentUnit instanceof Artillery){
                unit = new Artillery(currentUnit.getKey(), currentUnit.getTeam());
            }else
            if(currentUnit instanceof LightInfantry){
                unit = new LightInfantry(currentUnit.getKey(), currentUnit.getTeam());
            }else
            if(currentUnit instanceof HeavyInfantry){
                unit = new HeavyInfantry(currentUnit.getKey(), currentUnit.getTeam());
            }else
            if(currentUnit instanceof Vehicle){
                unit = new Vehicle(currentUnit.getKey(), currentUnit.getTeam());
            }else{
                System.out.println("Wrong Unit Type Detected");
            }

            placeOnBoard(Board.getBoardWidth()-2,i+1,unit);
        }
//
//        placeOnBoard(0,0,new LightInfantry("Assasin", 1));
//
//        placeOnBoard(0,1,new LightInfantry("Assasin", 1));
//


        BoardUtils.setActiveTeamUnits(1, true);
        BoardUtils.setActiveTeamUnits(2, false);

        generateObstacles(0.15);

        makeStrategical(5,1);
        makeStrategical(5,17);

        //GameCell.makeStrategical(14,9);

        makeStrategical(19,1);
        makeStrategical(19,9);
        makeStrategical(19,17);

        //GameCell.makeStrategical(34,9);

        makeStrategical(34,1);
        makeStrategical(34,17);
    }

    private GameCell getGameCell(String id){
        return (GameCell)currentScene.lookup(id);
    }

    private void placeOnBoard( int X, int Y, Unit unit){
        GameCell gameCell = getGameCell("#"+X+"_"+Y);
        gameCell.setUnit(unit);
        gameCell.setGraphic(gameCell.getUnit().getImageView(1.0));
        gameCell.setPadding(gameCell.getUnit().getInsetsY());
    }


    public static int getTeam1Score() {
        return team1Score;
    }

    public static void setTeam1Score(int team1Score) {
        BoardInitializer.team1Score = getTeam1Score() + team1Score;
    }

    public static int getTeam2Score() {
        return team2Score;
    }

    public static void setTeam2Score(int team2Score) {
        BoardInitializer.team2Score = getTeam2Score() + team2Score;
    }

    public static int getScoreLimit() {
        return scoreLimit;
    }

    public static int getArmyLimit() {
        return armyLimit;
    }

    public static void setArmyLimit(int armyLimit) {
        BoardInitializer.armyLimit = armyLimit;
    }

}
