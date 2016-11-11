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

        placeOnBoard(0,0,new LightInfantry("Penal_Trooper", 1));

        placeOnBoard(0,1,new LightInfantry("Assasin", 1));

        placeOnBoard(0,2,new LightInfantry("Kasrkin", 1));

        placeOnBoard(0,3,new LightInfantry("Brontian", 1));

        placeOnBoard(0,4,new LightInfantry("Krieg", 1));

        placeOnBoard(0,5,new LightInfantry("Tallarn", 1));

        placeOnBoard(2,2,new Vehicle("Leman_Russ", 1));

        placeOnBoard(2,4,new Artillery("Taros_Basilisk", 1));



        placeOnBoard(9,0,new LightInfantry("Choppa_Boy", 2));

        placeOnBoard(9,1,new LightInfantry("Fire_Boy", 2));

        placeOnBoard(9,2,new LightInfantry("Nob", 2));

        placeOnBoard(9,3,new LightInfantry("Shoota_Boy", 2));

        placeOnBoard(5,3,new Vehicle("PartyVan", 2));

        placeOnBoard(9,4,new MeleeInfantry("Squig", 2));


        BoardUtils.setActiveTeamUnits(1, true);
        BoardUtils.setActiveTeamUnits(2, false);

        GameCell.generateObstacles(0.15);

        GameCell.makeStrategical(5,1);
        GameCell.makeStrategical(5,17);

        GameCell.makeStrategical(14,9);

        GameCell.makeStrategical(24,1);
        GameCell.makeStrategical(24,9);
        GameCell.makeStrategical(24,17);

        GameCell.makeStrategical(34,9);

        GameCell.makeStrategical(44,1);
        GameCell.makeStrategical(44,17);

    }

    private GameCell getGameCell(String id){
        return (GameCell)currentScene.lookup(id);
    }

    private void placeOnBoard( int X, int Y, Unit unit){
        GameCell gameCell = getGameCell("#"+X+"_"+Y);
        gameCell.setUnit(unit);
        gameCell.setGraphic(gameCell.getUnit().getImageView(1.0));
        gameCell.setPadding(gameCell.getUnit().getInsets());
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
