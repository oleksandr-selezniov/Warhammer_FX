import Board.ChooseBoard;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by Dmitriy on 17.10.2016.
 */
public class Main extends Application{

    @Override
    public void start(Stage primaryStage) {
        //Board mainUI = new Board();
        ChooseBoard mainUI = new ChooseBoard();
        mainUI.createUI(primaryStage);
}
    public static void main(String[] args) {
        launch(args);
    }
}


