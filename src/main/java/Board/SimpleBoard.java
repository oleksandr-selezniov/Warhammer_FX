//package Board;
//
//import javafx.scene.layout.GridPane;
//
//import java.util.ArrayList;
//
//import static Board.Board.getMainBattlefieldGP;
//
///**
// * Created by Glazyrin.D on 3/3/2017.
// */
//public class SimpleBoard {
//    private static SimpleBoard simpleBoard = null;
//    private ArrayList<SimpleGameCell> simpleGCList = null;
//
//    public static SimpleBoard getInstance() {
//        if (simpleBoard == null)
//            simpleBoard = new SimpleBoard(getMainBattlefieldGP());
//        return simpleBoard;
//    }
//
//    public void refresh(){
//        simpleBoard = new SimpleBoard(getMainBattlefieldGP());
//    }
//
//    private SimpleBoard(GridPane mainBattlefieldGP){
//        mainBattlefieldGP.getChildren().stream().forEach(p-> simpleGCList.add(new SimpleGameCell(((GameCell)p))));
//    }
//
//    public GridPane getCurrentMainGp(){
//        GridPane currentGP =getMainBattlefieldGP();
//        currentGP.getChildren().remove(0, getMainBattlefieldGP().getChildren().size());
//
//       simpleGCList.forEach(p-> currentGP.getChildren().add(new GameCell(p)));
//
//    return currentGP;
//    }
//
//}
