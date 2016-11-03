import Units.LightInfantry;
import javafx.scene.Scene;

/**
 * Created by Dmitriy on 27.10.2016.
 */
public class BoardInitializer {
    Scene currentScene = Board.getScene();

    public void initializeBoard(){
        GameCell gc1 = getGameCell("#0_0");
        gc1.setUnit(new LightInfantry("Penal_Trooper", 1));
        gc1.setUnitImage(gc1.getUnit().getPicturePath());

        GameCell gc2 = getGameCell("#0_1");
        gc2.setUnit(new LightInfantry("Assasin", 1));
        gc2.setUnitImage(gc2.getUnit().getPicturePath());

        GameCell gc3 = getGameCell("#0_2");
        gc3.setUnit(new LightInfantry("Kasrkin", 1));
        gc3.setUnitImage(gc3.getUnit().getPicturePath());

        GameCell gc4 = getGameCell("#0_3");
        gc4.setUnit(new LightInfantry("Brontian", 1));
        gc4.setUnitImage(gc4.getUnit().getPicturePath());

        GameCell gc5 = getGameCell("#0_4");
        gc5.setUnit(new LightInfantry("Krieg", 1));
        gc5.setUnitImage(gc5.getUnit().getPicturePath());

        GameCell gc6 = getGameCell("#0_5");
        gc6.setUnit(new LightInfantry("Tallarn", 1));
        gc6.setUnitImage(gc6.getUnit().getPicturePath());

//        GameCell gc99 = getGameCell("#2_2");
//        gc99.setUnit(new Vehicle("Leman_Russ", 1));
//        gc99.setUnitImage(gc99.getUnit().getPicturePath(), 200, 200, 1);



        GameCell gc7 = getGameCell("#9_0");
        gc7.setUnit(new LightInfantry("Choppa_Boy", 2));
        gc7.setUnitImage(gc7.getUnit().getPicturePath());

        GameCell gc8 = getGameCell("#9_1");
        gc8.setUnit(new LightInfantry("Fire_Boy", 2));
        gc8.setUnitImage(gc8.getUnit().getPicturePath());

        GameCell gc9 = getGameCell("#9_2");
        gc9.setUnit(new LightInfantry("Nob", 2));
        gc9.setUnitImage(gc9.getUnit().getPicturePath());

        GameCell gc10 = getGameCell("#9_3");
        gc10.setUnit(new LightInfantry("Shoota_Boy", 2));
        gc10.setUnitImage(gc10.getUnit().getPicturePath());
    }

    private GameCell getGameCell(String id){
        return (GameCell)currentScene.lookup(id);
    }
}
