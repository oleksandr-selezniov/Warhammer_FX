//import Board.GameCell;
//import Board.SimpleGameCell;
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
//public class GameCellTest extends Application{
//        @Override
//        public void start(Stage primaryStage) {
//            UnitFactory factory = UnitFactory.getUnitFactory(UnitClassNames.LIGHT_INFANTRY);
//            Gui_Unit unit = factory.createUnit("Krieg", 1);
//            unit.setHealth(10);
//
//            GameCell gc1 = new GameCell();
//            gc1.setGuiUnit(unit);
//
//            SimpleGameCell sgc1 = new SimpleGameCell(gc1);
//            sgc1.getUnit().getHealth();
//
//            GameCell gc2 = new GameCell(sgc1);
//            gc2.getGUnit().getHealth();
//            gc2.getGUnit().getUnitImageView();
//        }
//    public static void main(String[] args) {
//        launch(args);
//    }
//}