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
            Gui_Unit currentGuiUnit = ChooseBoard.getCurrentHumanList().get(i);
            Gui_Unit guiUnit = getNewUnitSameAsGiven(currentGuiUnit); // to avoid cloning th SAME guiUnit many times

            if(i < getBoardHeight()-2) {
                    placeOnBoard(1, i+1, guiUnit);
            }else{
                if(i < getBoardHeight()*2-4){
                    placeOnBoard(2, i+3-getBoardHeight(), guiUnit);
                }else{
                    System.out.println("[ WARNING! ] Too many units for 2 rows!");
                    break;
                }
            } //18 14
        }
        for(int i=0; i < ChooseBoard.getCurrentOrkList().size(); i++){
            Gui_Unit currentGuiUnit = ChooseBoard.getCurrentOrkList().get(i);
            Gui_Unit guiUnit = getNewUnitSameAsGiven(currentGuiUnit); // to avoid cloning th SAME guiUnit many times

            if(i < getBoardHeight()-2) {
                    placeOnBoard(getBoardWidth()-2, i+1, guiUnit);
            }else{
                if(i < getBoardHeight()*2-4){
                        placeOnBoard(getBoardWidth()-3, i+3-getBoardHeight(), guiUnit);
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

    private void placeOnBoard( int X, int Y, Gui_Unit guiUnit){
        GameCell gameCell = getGameCell("#"+X+"_"+Y);
        gameCell.setGuiUnit(guiUnit);
        gameCell.setGraphic(gameCell.getGUnit().getImageView(1.0));
        gameCell.setPadding(gameCell.getGUnit().getInsetsY());
    }

    private Gui_Unit getNewUnitSameAsGiven(Gui_Unit currentGuiUnit){
        UnitFactory factory = UnitFactory.getUnitFactory(currentGuiUnit.getUnitClassName());
        return factory.copy(currentGuiUnit);


//
//        Gui_Unit unit = null;
//        if(currentGuiUnit instanceof MeleeInfantry){
//            unit = new MeleeInfantry(currentGuiUnit.getKey(), currentGuiUnit.getTeam());
//        }else
//        if(currentGuiUnit instanceof Artillery){
//            unit = new Artillery(currentGuiUnit.getKey(), currentGuiUnit.getTeam());
//        }else
//        if(currentGuiUnit instanceof LightInfantry){
//            unit = new LightInfantry(currentGuiUnit.getKey(), currentGuiUnit.getTeam());
//        }else
//        if(currentGuiUnit instanceof HeavyInfantry){
//            unit = new HeavyInfantry(currentGuiUnit.getKey(), currentGuiUnit.getTeam());
//        }else
//        if(currentGuiUnit instanceof Vehicle){
//            unit = new Vehicle(currentGuiUnit.getKey(), currentGuiUnit.getTeam());
//        }else{
//            System.out.println("Wrong Gui_Unit Type Detected");
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
