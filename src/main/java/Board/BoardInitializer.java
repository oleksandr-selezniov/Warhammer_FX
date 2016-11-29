package Board;

import Units.*;
import javafx.scene.Scene;

/**
 * Created by Dmitriy on 27.10.2016.
 */
public class BoardInitializer {
    private Scene currentScene = Board.getScene();
    private static int ScoreLimit = 100;
    private static int team1Score;
    private static int team2Score;

    void initializeBoard(){

        for(int i=0; i < ChooseBoard.getCurrentHumanList().size(); i++){
            placeOnBoard(1,i,ChooseBoard.getCurrentHumanList().get(i));
        }

        for(int i=0; i < ChooseBoard.getCurrentOrkList().size(); i++){
            placeOnBoard(38,i,ChooseBoard.getCurrentOrkList().get(i));
        }

//        placeOnBoard(0,0,new LightInfantry("Penal_Trooper", 1));
//
//        placeOnBoard(0,1,new LightInfantry("Assasin", 1));
//
//        placeOnBoard(0,2,new LightInfantry("Kasrkin", 1));
//
//        placeOnBoard(0,3,new MeleeInfantry("Brontian", 1));
//
//        placeOnBoard(0,4,new LightInfantry("Krieg", 1));
//
//        placeOnBoard(0,5,new MeleeInfantry("Tallarn", 1));
//
//        placeOnBoard(0,6,new LightInfantry("Catachan_Flamer", 1));
//
//
//        placeOnBoard(2,1,new Vehicle("Leman_Russ", 1));
//
//        placeOnBoard(2,2,new Vehicle("LasChimaera", 1));
//
//        placeOnBoard(2,3,new Vehicle("Hellhound", 1));
//
//        placeOnBoard(2,4,new Artillery("Taros_Basilisk", 1));
//
//        placeOnBoard(2,6,new Vehicle("Sentinel", 1));
//
//
//        placeOnBoard(39,0,new MeleeInfantry("Grot", 2));
//
//        placeOnBoard(39,1,new HeavyInfantry("Fire_Boy", 2));
//
//        placeOnBoard(39,2,new HeavyInfantry("Nob", 2));
//
//        placeOnBoard(39,3,new HeavyInfantry("Shoota_Boy", 2));
//
//        placeOnBoard(39,4,new MeleeInfantry("Squig", 2));
//
//        placeOnBoard(39,5,new HeavyInfantry("Choppa_Boy", 2));
//
//        placeOnBoard(39,6,new MeleeInfantry("Armor_Boy", 2));
//
//        placeOnBoard(39,7,new MeleeInfantry("Slugga_Nob", 2));
//
//        placeOnBoard(39,8,new HeavyInfantry("Tank_Busta", 2));
//
//        placeOnBoard(39,9,new LightInfantry("Brain_Boy", 2));
//
//
//
//        placeOnBoard(37,1,new Vehicle("PartyVan", 2));
//
//        placeOnBoard(37,3,new Vehicle("Looted_Baneblade", 2));
//
//        placeOnBoard(37,5,new Vehicle("Speed_Boy", 2));
//
//        placeOnBoard(37,7,new Vehicle("Grot_Tank", 2));


        BoardUtils.setActiveTeamUnits(1, true);
        BoardUtils.setActiveTeamUnits(2, false);

        GameCell.generateObstacles(0.15);

        GameCell.makeStrategical(5,1);
        GameCell.makeStrategical(5,17);

        //GameCell.makeStrategical(14,9);

        GameCell.makeStrategical(19,1);
        GameCell.makeStrategical(19,9);
        GameCell.makeStrategical(19,17);

        //GameCell.makeStrategical(34,9);

        GameCell.makeStrategical(34,1);
        GameCell.makeStrategical(34,17);

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
        return ScoreLimit;
    }
}
