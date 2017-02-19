package Board;

import Board.Utils.BoardUtils;
import Units.*;
import Units.Factories.UnitFactory;
import javafx.scene.Scene;

import static Board.Board.getBoardHeight;
import static Board.Board.getBoardWidth;
import static Board.Utils.GameCellUtils.generateObstacles;
import static Board.Utils.GameCellUtils.makeStrategical;

/**
 * Created by Dmitriy on 27.10.2016.
 */
public class BoardInitializer {
    private Scene currentScene = Board.getScene();
    private static int scoreLimit = 100;
    private static int armyLimit = 400;
    private static int team1Score;
    private static int team2Score;
    private static boolean useStrategicalCells = true;
    private static BoardType boardSise;

    public enum BoardType{
        SMALL,MEDIUM,LARGE
    }

    void initializeBoard(){
        for(int i=0; i < ChooseBoard.getCurrentHumanList().size(); i++){
            Unit currentUnit = ChooseBoard.getCurrentHumanList().get(i);
            Unit unit = getNewUnitSameAsGiven(currentUnit); // to avoid cloning th SAME unit many times

            if(i < getBoardHeight()-2) {
                    placeOnBoard(1, i+1, unit);
            }else{
                if(i < getBoardHeight()*2-4){
                    placeOnBoard(2, i+3-getBoardHeight(), unit);
                }else{
                    System.out.println("[ WARNING! ] Too many units for 2 rows!");
                    break;
                }
            } //18 14
        }
        for(int i=0; i < ChooseBoard.getCurrentOrkList().size(); i++){
            Unit currentUnit = ChooseBoard.getCurrentOrkList().get(i);
            Unit unit = getNewUnitSameAsGiven(currentUnit); // to avoid cloning th SAME unit many times

            if(i < getBoardHeight()-2) {
                    placeOnBoard(getBoardWidth()-2, i+1, unit);
            }else{
                if(i < getBoardHeight()*2-4){
                        placeOnBoard(getBoardWidth()-3, i+3-getBoardHeight(), unit);
                    }else{
                    System.out.println("[ WARNING! ] Too many units for 2 rows!");
                    break;
                }
            }
        }

        BoardUtils.setActiveTeamUnits(1, true);
        BoardUtils.setActiveTeamUnits(2, false);
        generateObstacles(0.15);

        if(isUseStrategicalCells()){
            switch (boardSise){
                case SMALL: //19\9
                    makeStrategical(9,1);
                    makeStrategical(9,7);
                    break;
                case MEDIUM: //34\14
                    makeStrategical(11,3);
                    makeStrategical(11,10);
                    makeStrategical(22,3);
                    makeStrategical(22,10);
                    break;
                case LARGE: //49\19
                    makeStrategical(9,3);
                    makeStrategical(9,9);
                    makeStrategical(9,15);
                    makeStrategical(19,6);
                    makeStrategical(19,12);
                    makeStrategical(24,9);
                    makeStrategical(29,6);
                    makeStrategical(29,12);
                    makeStrategical(39,3);
                    makeStrategical(39,9);
                    makeStrategical(39,15);
                    break;
            }
        }
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

    private Unit getNewUnitSameAsGiven(Unit currentUnit){
        UnitFactory factory = UnitFactory.getUnitFactory(currentUnit.getUnitClassName());
        return factory.copy(currentUnit);


//
//        Unit unit = null;
//        if(currentUnit instanceof MeleeInfantry){
//            unit = new MeleeInfantry(currentUnit.getKey(), currentUnit.getTeam());
//        }else
//        if(currentUnit instanceof Artillery){
//            unit = new Artillery(currentUnit.getKey(), currentUnit.getTeam());
//        }else
//        if(currentUnit instanceof LightInfantry){
//            unit = new LightInfantry(currentUnit.getKey(), currentUnit.getTeam());
//        }else
//        if(currentUnit instanceof HeavyInfantry){
//            unit = new HeavyInfantry(currentUnit.getKey(), currentUnit.getTeam());
//        }else
//        if(currentUnit instanceof Vehicle){
//            unit = new Vehicle(currentUnit.getKey(), currentUnit.getTeam());
//        }else{
//            System.out.println("Wrong Unit Type Detected");
//        }
//        return unit;
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

    public static void setScoreLimit(int score) { scoreLimit = score; }

    public static int getArmyLimit() {
        return armyLimit;
    }

    public static void setArmyLimit(int armyLimit) {
        BoardInitializer.armyLimit = armyLimit;
    }

    public static boolean isUseStrategicalCells() {
        return useStrategicalCells;
    }

    public static void setUseStrategicalCells(boolean useSP) {
        useStrategicalCells = useSP;
    }

    public static void setBoardSise(BoardType boardSise) {
        BoardInitializer.boardSise = boardSise;
    }

}
