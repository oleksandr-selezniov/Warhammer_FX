package Board;

import Units.LightInfantry;
import Units.Unit;
import Units.Vehicle;
import javafx.scene.Scene;

/**
 * Created by Dmitriy on 27.10.2016.
 */
public class BoardInitializer {
    private Scene currentScene = Board.getScene();

    void initializeBoard(){

        placeOnBoard(0,0,new LightInfantry("Penal_Trooper", 1));

        placeOnBoard(0,1,new LightInfantry("Assasin", 1));

        placeOnBoard(0,2,new LightInfantry("Kasrkin", 1));

        placeOnBoard(0,3,new LightInfantry("Brontian", 1));

        placeOnBoard(0,4,new LightInfantry("Krieg", 1));

        placeOnBoard(0,5,new LightInfantry("Tallarn", 1));

        placeOnBoard(2,2,new Vehicle("Leman_Russ", 1));



        placeOnBoard(9,0,new LightInfantry("Choppa_Boy", 2));

        placeOnBoard(9,1,new LightInfantry("Fire_Boy", 2));

        placeOnBoard(9,2,new LightInfantry("Nob", 2));

        placeOnBoard(9,3,new LightInfantry("Shoota_Boy", 2));

        placeOnBoard(5,3,new Vehicle("PartyVan", 2));

        BoardUtils.setActiveTeamUnits(1, true);
        BoardUtils.setActiveTeamUnits(2, false);

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
}
