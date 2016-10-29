import javafx.scene.Scene;

/**
 * Created by Dmitriy on 27.10.2016.
 */
public class BoardInitializer {
    Scene currentScene = Board.getScene();

    public void initializeBoard(){
        GameCell gc1 = getGameCell("#0_0");
        gc1.setUnit(new Unit("13th Penal Legion's Trooper",50,20,45,10,25,15,3,7,1,"src\\main\\resources\\Gifs\\LastChancer.gif"));
        gc1.setUnitImage(gc1.getUnit().getPicturePath());

        GameCell gc2 = getGameCell("#0_1");
        gc2.setUnit(new Unit("Assasin",100,80,24,20,10,50,2,10,1,"src\\main\\resources\\Gifs\\Assasin.gif"));
        gc2.setUnitImage(gc2.getUnit().getPicturePath());

        GameCell gc3 = getGameCell("#14_0");
        gc3.setUnit(new Unit("Choppa Boy",200,100,30,20,30,10,3,5,2,"src\\main\\resources\\Gifs\\Orc.gif"));
        gc3.setUnitImage(gc3.getUnit().getPicturePath());

        GameCell gc4 = getGameCell("#0_2");
        gc4.setUnit(new Unit("Kasrkin",200,80,54,20,10,40,5,5,1,"src\\main\\resources\\Gifs\\Kasrkin.gif"));
        gc4.setUnitImage(gc4.getUnit().getPicturePath());

        GameCell gc5 = getGameCell("#14_1");
        gc5.setUnit(new Unit("Choppa Boy",200,100,30,20,30,10,3,5,2,"src\\main\\resources\\Gifs\\fire.gif"));
        gc5.setUnitImage(gc5.getUnit().getPicturePath());

        GameCell gc6 = getGameCell("#14_2");
        gc6.setUnit(new Unit("Choppa Boy",200,100,30,20,30,10,3,5,2,"src\\main\\resources\\Gifs\\orc2.gif"));
        gc6.setUnitImage(gc6.getUnit().getPicturePath());

        GameCell gc7 = getGameCell("#0_3");
        gc7.setUnit(new Unit("13th Penal Legion's Trooper",50,20,45,10,25,15,3,7,1,"src\\main\\resources\\Gifs\\Sword.gif"));
        gc7.setUnitImage(gc7.getUnit().getPicturePath());

        GameCell gc8 = getGameCell("#0_4");
        gc8.setUnit(new Unit("13th Penal Legion's Trooper",50,20,45,10,25,15,3,7,1,"src\\main\\resources\\Gifs\\Krieg.gif"));
        gc8.setUnitImage(gc8.getUnit().getPicturePath());

        GameCell gc9 = getGameCell("#0_5");
        gc9.setUnit(new Unit("13th Penal Legion's Trooper",50,20,45,10,25,15,3,7,1,"src\\main\\resources\\Gifs\\Tallarn.gif"));
        gc9.setUnitImage(gc9.getUnit().getPicturePath());

        GameCell gc10 = getGameCell("#14_3");
        gc10.setUnit(new Unit("13th Penal Legion's Trooper",50,20,45,10,25,15,3,7,2,"src\\main\\resources\\Gifs\\Gunner.gif"));
        gc10.setUnitImage(gc10.getUnit().getPicturePath());
    }

    private GameCell getGameCell(String id){
        return (GameCell)currentScene.lookup(id);
    }
}
