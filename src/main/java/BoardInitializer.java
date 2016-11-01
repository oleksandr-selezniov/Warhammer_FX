import javafx.scene.Scene;

/**
 * Created by Dmitriy on 27.10.2016.
 */
public class BoardInitializer {
    Scene currentScene = Board.getScene();

    public void initializeBoard(){
        GameCell gc1 = getGameCell("#0_0");
        gc1.setUnit(new Unit("13th Penal Legion's Trooper",50,20,45,0.9,25,15,3,7,1,"src\\main\\resources\\Gifs\\LastChancer.gif"));
        gc1.setUnitImage(gc1.getUnit().getPicturePath());

        GameCell gc2 = getGameCell("#0_1");
        gc2.setUnit(new Unit("Assasin",100,80,24,0.7,10,500,2,10,1,"src\\main\\resources\\Gifs\\Assasin.gif"));
        gc2.setUnitImage(gc2.getUnit().getPicturePath());

        GameCell gc3 = getGameCell("#0_2");
        gc3.setUnit(new Unit("Kasrkin",200,80,54,0.7,10,40,5,5,1,"src\\main\\resources\\Gifs\\Kasrkin.gif"));
        gc3.setUnitImage(gc3.getUnit().getPicturePath());

        GameCell gc4 = getGameCell("#0_3");
        gc4.setUnit(new Unit("Brontian Longknives's Trooper",50,20,55,0.9,25,15,3,7,1,"src\\main\\resources\\Gifs\\Sword.gif"));
        gc4.setUnitImage(gc4.getUnit().getPicturePath());

        GameCell gc5 = getGameCell("#0_4");
        gc5.setUnit(new Unit("Krieg Deathkorps Trooper",50,20,45,0.8,25,15,3,7,1,"src\\main\\resources\\Gifs\\Krieg.gif"));
        gc5.setUnitImage(gc5.getUnit().getPicturePath());

        GameCell gc6 = getGameCell("#0_5");
        gc6.setUnit(new Unit("Tallarn's Desert Raider",50,20,45,0.8,25,15,3,7,1,"src\\main\\resources\\Gifs\\Tallarn.gif"));
        gc6.setUnitImage(gc6.getUnit().getPicturePath());

//        GameCell gc99 = getGameCell("#2_2");
//        gc99.setUnit(new Unit("Leman Russ Tank",550,200,45,0.5,40,500,3,7,1,"src\\main\\resources\\Gifs\\LemanRuss.gif"));
//        gc99.setUnitImage(gc99.getUnit().getPicturePath(), 200, 200, 1);



        GameCell gc7 = getGameCell("#9_0");
        gc7.setUnit(new Unit("Choppa Boy",250,100,30,0.5,30,10,3,5,2,"src\\main\\resources\\Gifs\\Orc.gif"));
        gc7.setUnitImage(gc7.getUnit().getPicturePath());

        GameCell gc8 = getGameCell("#9_1");
        gc8.setUnit(new Unit("Fire Boy",100,100,30,0.8,30,10,3,5,2,"src\\main\\resources\\Gifs\\fire.gif"));
        gc8.setUnitImage(gc8.getUnit().getPicturePath());

        GameCell gc9 = getGameCell("#9_2");
        gc9.setUnit(new Unit("Choppa Boy 2",300,100,30,0.7,30,10,3,5,2,"src\\main\\resources\\Gifs\\orc2.gif"));
        gc9.setUnitImage(gc9.getUnit().getPicturePath());

        GameCell gc10 = getGameCell("#9_3");
        gc10.setUnit(new Unit("Shootah Boy",150,20,45,0.7,25,15,3,7,2,"src\\main\\resources\\Gifs\\Gunner.gif"));
        gc10.setUnitImage(gc10.getUnit().getPicturePath());
    }

    private GameCell getGameCell(String id){
        return (GameCell)currentScene.lookup(id);
    }
}
