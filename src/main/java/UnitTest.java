//import Board.ChooseBoard;
//import Units.Enums.UnitClassNames;
//import Units.Factories.UnitFactory;
//import Units.Unit;
//import Units.Gui_Unit;
//import javafx.application.Application;
//import javafx.stage.Stage;
//
///**
// * Created by Glazyrin.D on 2/21/2017.
// */
//public class UnitTest extends Application {
//    @Override
//    public void start(Stage primaryStage) {
//        UnitFactory factory = UnitFactory.getUnitFactory(UnitClassNames.LIGHT_INFANTRY);
//        Gui_Unit unit = factory.createUnit("Krieg", 1);
//        unit.setHealth(10);
//
//        Unit simpleUnit = new Unit(unit);
//        simpleUnit.setHealth(simpleUnit.getHealth()+5);
//
//        Gui_Unit unit2 = new Gui_Unit(simpleUnit);
//        unit2.getUnitImageView();
//        unit2.getHealth();
//        unit2.getTeam();
//    }
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
