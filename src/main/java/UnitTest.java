import Board.ChooseBoard;
import Units.Enums.UnitClassNames;
import Units.Factories.UnitFactory;
import Units.SimpleUnit;
import Units.Unit;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by Glazyrin.D on 2/21/2017.
 */
public class UnitTest extends Application {
    @Override
    public void start(Stage primaryStage) {
        UnitFactory factory = UnitFactory.getUnitFactory(UnitClassNames.LIGHT_INFANTRY);
        Unit unit = factory.createUnit("Krieg", 1);
        unit.setHealth(10);

        SimpleUnit simpleUnit = new SimpleUnit(unit);
        simpleUnit.setHealth(simpleUnit.getHealth()+5);

        Unit unit2 = new Unit(simpleUnit);
        unit2.getUnitImageView();
        unit2.getHealth();
        unit2.getTeam();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
