package Board;

import Units.LightInfantry;
import Units.Vehicle;
import javafx.scene.Scene;

/**
 * Created by Dmitriy on 27.10.2016.
 */
public class BoardInitializer {
    Scene currentScene = Board.getScene();

    public void initializeBoard(){
        GameCell gc1 = getGameCell("#0_0");
        gc1.setUnit(new LightInfantry("Penal_Trooper", 1));
        gc1.setGraphic(gc1.getUnit().getImageView(1.0));
        gc1.setPadding(gc1.getUnit().getInsets());

        GameCell gc2 = getGameCell("#0_1");
        gc2.setUnit(new LightInfantry("Assasin", 1));
        gc2.setGraphic(gc2.getUnit().getImageView(1.0));
        gc2.setPadding(gc2.getUnit().getInsets());

        GameCell gc3 = getGameCell("#0_2");
        gc3.setUnit(new LightInfantry("Kasrkin", 1));
        gc3.setGraphic(gc3.getUnit().getImageView(1.0));
        gc3.setPadding(gc3.getUnit().getInsets());

        GameCell gc4 = getGameCell("#0_3");
        gc4.setUnit(new LightInfantry("Brontian", 1));
        gc4.setGraphic(gc4.getUnit().getImageView(1.0));
        gc4.setPadding(gc4.getUnit().getInsets());

        GameCell gc5 = getGameCell("#0_4");
        gc5.setUnit(new LightInfantry("Krieg", 1));
        gc5.setGraphic(gc5.getUnit().getImageView(1.0));
        gc5.setPadding(gc5.getUnit().getInsets());

        GameCell gc6 = getGameCell("#0_5");
        gc6.setUnit(new LightInfantry("Tallarn", 1));
        gc6.setGraphic(gc6.getUnit().getImageView(1.0));
        gc6.setPadding(gc6.getUnit().getInsets());

        GameCell gc99 = getGameCell("#2_2");
        gc99.setUnit(new Vehicle("Leman_Russ", 1));
        gc99.setGraphic(gc99.getUnit().getImageView(1.0));
        gc99.setPadding(gc99.getUnit().getInsets());



        GameCell gc7 = getGameCell("#9_0");
        gc7.setUnit(new LightInfantry("Choppa_Boy", 2));
        gc7.setGraphic(gc7.getUnit().getImageView(1.0));
        gc7.setPadding(gc7.getUnit().getInsets());

        GameCell gc8 = getGameCell("#9_1");
        gc8.setUnit(new LightInfantry("Fire_Boy", 2));
        gc8.setGraphic(gc8.getUnit().getImageView(1.0));
        gc8.setPadding(gc8.getUnit().getInsets());

        GameCell gc9 = getGameCell("#9_2");
        gc9.setUnit(new LightInfantry("Nob", 2));
        gc9.setGraphic(gc9.getUnit().getImageView(1.0));
        gc9.setPadding(gc9.getUnit().getInsets());

        GameCell gc10 = getGameCell("#9_3");
        gc10.setUnit(new LightInfantry("Shoota_Boy", 2));
        gc10.setGraphic(gc10.getUnit().getImageView(1.0));
        gc10.setPadding(gc10.getUnit().getInsets());

        GameCell gc90 = getGameCell("#7_3");
        gc90.setUnit(new Vehicle("PartyVan", 2));
        gc90.setGraphic(gc90.getUnit().getImageView(1.0));
        gc90.setPadding(gc90.getUnit().getInsets());

        BoardUtils.setActiveTeamUnits(1, true);
        BoardUtils.setActiveTeamUnits(2, false);

    }

    private GameCell getGameCell(String id){
        return (GameCell)currentScene.lookup(id);
    }
}
