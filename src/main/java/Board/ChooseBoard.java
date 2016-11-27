package Board;

import Size.Size;
import Units.Unit;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Glazyrin.D on 11/24/2016.
 */
public class ChooseBoard {
    private String defaultBackgroundPath = "backgrounds/background_7.jpg";
    private static Scene scene;
    int winHeight = 600;
    int winWidth = 600;
    private static Map<String, Unit> humanUnitMap = Unit.getRaceUnitMap("Humans");
    private static Map<String, Unit> orkUnitMap = Unit.getRaceUnitMap("Orks");

    public void createUI(Stage primaryStage){
        primaryStage.setTitle("WarhammerFX ChooseBoard");

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setMinHeight(winHeight);
        anchorPane.setMinWidth(winWidth);

        ImageView mainBackground = new ImageView();
        mainBackground.setImage(new BoardUtils().getImage(defaultBackgroundPath));
        mainBackground.setOpacity(0.9);
        mainBackground.setFitWidth(winWidth);
        mainBackground.setFitHeight(winHeight);
        anchorPane.getChildren().add(mainBackground);
        AnchorPane.setTopAnchor(mainBackground, 0.0);

        BorderPane borderPane = new BorderPane();
        borderPane.setMaxWidth(winWidth);
        borderPane.setMaxHeight(winHeight);
        borderPane.setCenter(getCenterPart());
        borderPane.setBottom(getBottomPart());
        borderPane.setTop(getTopPart());

        anchorPane.getChildren().add(borderPane);

        scene = new Scene(anchorPane, winWidth, winHeight);
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(winHeight);
        primaryStage.setMinWidth(winWidth);
        primaryStage.setMaxHeight(winHeight);
        primaryStage.setMaxWidth(winWidth);
        primaryStage.show();
    }

    private VBox getCenterPart(){
        ImageView centerImageView = new ImageView();
        centerImageView.setFitWidth(winWidth* 0.7);
        centerImageView.setFitHeight(winHeight* 0.5);
        centerImageView.setImage(new BoardUtils().getImage("other/chaos.jpg"));
        centerImageView.setId("centerImageView");

        VBox vbox = new VBox(centerImageView);
        vbox.setMinWidth(winWidth);
        vbox.setMinHeight(winHeight*0.5);
        vbox.setAlignment(Pos.CENTER);
        return  vbox;
    }

    private HBox getBottomPart(){
        ListView leftListView = new ListView();
        leftListView.setMaxSize(200, 150);
        leftListView.setId("leftListView");
        ArrayList<String> arrayListH= new ArrayList<>();
        arrayListH.addAll(humanUnitMap.keySet());

        ObservableList<String> humanNameList = FXCollections.observableArrayList(arrayListH);
        ComboBox leftComboBox = new ComboBox(humanNameList);
        leftComboBox.setValue("Human Units");
        leftComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                System.out.println(ov);
                System.out.println(t);
                System.out.println(t1);

                ImageView imageView = (ImageView) scene.lookup("#centerImageView");
                imageView.setImage(humanUnitMap.get(t1).getImage());
            }
        });
        leftComboBox.setMaxWidth(200);

        ArrayList<String> arrayListO= new ArrayList<>();
        arrayListO.addAll(orkUnitMap.keySet());
        ObservableList<String> orkNameList = FXCollections.observableArrayList(arrayListO);
        ComboBox rightComboBox = new ComboBox(orkNameList);
        rightComboBox.setValue("Ork Units");
        rightComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                System.out.println(ov);
                System.out.println(t);
                System.out.println(t1);

                ImageView imageView = (ImageView) scene.lookup("#centerImageView");
                imageView.setImage(orkUnitMap.get(t1).getImage());
            }
        });
        rightComboBox.setMaxWidth(200);

        ListView rightListView = new ListView();
        rightListView.setMaxSize(200, 150);
        rightListView.setId("rightListView");

        HBox hBox1 = new HBox(new VBox(leftComboBox, leftListView));
        hBox1.setPadding(new Insets(5));

        HBox hBox2 = new HBox(new VBox(rightComboBox, rightListView));
        hBox2.setPadding(new Insets(5));

        HBox hbox = new HBox(hBox1, hBox2);
        hbox.setAlignment(Pos.CENTER);

        return  hbox;
    }

    private HBox getTopPart(){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);

        Button res1Button = new Button("1024/768");
        res1Button.setOnAction(e -> {
            Size.setSceneWidth(1024.0);
            Size.setSceneHeight(768.0);
        });

        Button res2Button = new Button("1366/768");
        res2Button.setOnAction(e -> {
            Size.setSceneWidth(1366);
            Size.setSceneHeight(768);
        });

        Button res3Button = new Button("1600/900");
        res3Button.setOnAction(e -> {
            Size.setSceneWidth(1600);
            Size.setSceneHeight(900);
        });

        Button res4Button = new Button("1920/1080");
        res4Button.setOnAction(e -> {
            Size.setSceneWidth(1920);
            Size.setSceneHeight(1080);
        });

        Button nextButton = new Button("Next");
        nextButton.setOnAction(e -> {
            if(Size.getSceneHeight()!=0 && Size.getSceneWidth()!=0){
                Stage stage = new Stage();
                Board main = new Board();
                main.createUI(stage);

                Stage OldStage = (Stage)scene.getWindow();
                OldStage.close();
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Warning!");
                alert.setContentText("Please, choose the screen resolution!");
                alert.showAndWait();
            }
        });


        HBox res1 = new HBox(res1Button);
        HBox res2 = new HBox(res2Button);
        HBox res3 = new HBox(res3Button);
        HBox res4 = new HBox(res4Button);
        HBox next = new HBox(nextButton);

        res1.setPadding(new Insets(5));
        res2.setPadding(new Insets(5));
        res3.setPadding(new Insets(5));
        res4.setPadding(new Insets(5));
        next.setPadding(new Insets(5));

        hBox.getChildren().addAll(res1, res2 , res3, res4, next);

        return hBox;
    }
}
