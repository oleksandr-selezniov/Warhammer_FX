package Board;

import Size.Size;
import Units.Unit;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Map;

import static Board.Board.*;
import static Board.BoardInitializer.getArmyLimit;


/**
 * Created by Glazyrin.D on 11/24/2016.
 */
public class ChooseBoard {
    private String defaultBackgroundPath = "backgrounds/background_7.jpg";
    private static Scene scene;
    private int winHeight = 700;
    private int winWidth = 800;
    private int teamOneTotalCost = 0;
    private int teamTwoTotalCost = 0;
    private static Map<String, Unit> humanUnitMap = Unit.getRaceUnitMap("Humans");
    private static Map<String, Unit> orkUnitMap = Unit.getRaceUnitMap("Orks");
    private static Unit currentSelectedUnit;
    private static ArrayList<Unit> currentHumanList = new ArrayList<>();
    private static ArrayList<Unit> currentOrkList = new ArrayList<>();

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

        anchorPane.getChildren().add(borderPane);

        scene = new Scene(anchorPane, winWidth, winHeight);
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(winHeight);
        primaryStage.setMinWidth(winWidth);
        primaryStage.setMaxHeight(winHeight);
        primaryStage.setMaxWidth(winWidth);
        primaryStage.show();
    }

    private GridPane getCenterPart(){
        GridPane gridPane = new GridPane();
        gridPane.setMaxSize(winWidth, winHeight* 0.5);
        gridPane.setMinSize(winWidth, winHeight* 0.5);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(5));
        gridPane.setGridLinesVisible(true);

        ImageView centerImageView = new ImageView();
        centerImageView.setFitWidth(winWidth* 0.7);
        centerImageView.setFitHeight(winHeight* 0.5);
        centerImageView.setImage(new BoardUtils().getImage("other/Logo.png"));

        VBox vbox = new VBox(centerImageView);
        vbox.setId("centerImageVbox");
        vbox.setMinWidth(350);
        vbox.setMinHeight(winHeight*0.5);
        vbox.setMaxWidth(350);
        vbox.setMaxHeight(winHeight*0.5);
        vbox.setAlignment(Pos.CENTER);

        VBox leftInfoVbox = new VBox();
        leftInfoVbox.setId("leftInfoVbox");
        leftInfoVbox.setMinSize(200,winHeight*0.5);
        leftInfoVbox.setMaxSize(200,winHeight*0.5);
        leftInfoVbox.setAlignment(Pos.CENTER);

        VBox rightInfoVbox = new VBox();
        rightInfoVbox.setId("rightInfoVbox");
        rightInfoVbox.setMinSize(200,winHeight*0.5);
        rightInfoVbox.setMaxSize(200,winHeight*0.5);
        rightInfoVbox.setAlignment(Pos.CENTER);

        gridPane.add(leftInfoVbox, 0,0);
        gridPane.add(vbox, 1, 0);
        gridPane.add(rightInfoVbox, 3,0);
        return  gridPane;
    }

    private HBox getBottomPart(){

        VBox leftVbox = new VBox();
        leftVbox.setMinWidth(200);
        leftVbox.setMaxWidth(200);
        leftVbox.setId("leftUnitVbox");
        leftVbox.setAlignment(Pos.TOP_CENTER);
        ScrollPane leftScrollPane = new ScrollPane(leftVbox);
        leftScrollPane.setMaxSize(200, 200);
        leftScrollPane.setMinSize(200, 200);
        leftScrollPane.setId("leftScrollPane");

        TextArea centerTextArea = new TextArea();
        centerTextArea.setMaxSize(200, 200);
        centerTextArea.setMinSize(200, 200);
        centerTextArea.setId("centerTextArea");
        HBox centerHBox = new HBox(centerTextArea);
        centerHBox.setMaxSize(200, 200);
        centerHBox.setMinSize(200, 200);
        centerHBox.setId("centerHbox");

        VBox rightVbox = new VBox();
        rightVbox.setMinWidth(200);
        rightVbox.setMaxWidth(200);
        rightVbox.setId("rightUnitVbox");
        rightVbox.setAlignment(Pos.TOP_CENTER);
        ScrollPane rightScrollPane = new ScrollPane(rightVbox);
        rightScrollPane.setMaxSize(200, 200);
        rightScrollPane.setMinSize(200, 200);
        rightScrollPane.setId("rightScrollPane");

        Button selectButton = new Button("Select");
        selectButton.setMinWidth(200);
        selectButton.setOnAction(p->{

            if(currentSelectedUnit.getTeam() == 1){
                if(getTeamTotalCost(1) < getArmyLimit() && (getTeamTotalCost(1)+currentSelectedUnit.getCost())<=getArmyLimit()) {
                    Button leftB = getUnitButton(currentHumanList, currentSelectedUnit, leftVbox);
                    leftVbox.getChildren().add(leftB);
                }else{
                    showUnitLimitWarning();
                }
            }else{
                if(getTeamTotalCost(2) < getArmyLimit() && (getTeamTotalCost(2)+currentSelectedUnit.getCost())<=getArmyLimit()) {
                    Button rightB = getUnitButton(currentOrkList, currentSelectedUnit, rightVbox);
                    rightVbox.getChildren().add(rightB);
                }else{
                    showUnitLimitWarning();
                }
            }
        });

        ComboBox leftComboBox = getUnitComboBox(humanUnitMap, "Human Units");
        ComboBox rightComboBox = getUnitComboBox(orkUnitMap, "Ork Units");

        HBox hBox1 = new HBox(new VBox(leftComboBox, leftScrollPane, getBoardSizeBlock()));
        hBox1.setPadding(new Insets(5));
        hBox1.setAlignment(Pos.CENTER);

        HBox hBox2 = new HBox(new VBox(selectButton, centerHBox, getUnitCostBlock()));
        hBox2.setPadding(new Insets(5));
        hBox2.setAlignment(Pos.CENTER);

        HBox hBox3 = new HBox(new VBox(rightComboBox, rightScrollPane, getNextBlock()));
        hBox3.setPadding(new Insets(5));
        hBox3.setAlignment(Pos.CENTER);

        HBox hbox = new HBox(hBox1, hBox2, hBox3);
        hbox.setAlignment(Pos.CENTER);

        return  hbox;
    }

    private void showUnitLimitWarning(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Unit Limit Reached!");
        alert.setContentText("You can't increase your army");
        alert.showAndWait();
    }

    private Button getUnitButton(ArrayList<Unit> currentList, Unit currentSelectedUnit, VBox vBox){
        Button rightB = new Button(currentSelectedUnit.getName());
        currentList.add(currentSelectedUnit);
        setTeamTotalCost( currentSelectedUnit.getTeam(), (getTeamTotalCost(currentSelectedUnit.getTeam())+currentSelectedUnit.getCost()));
        rightB.setMaxWidth(150);
        rightB.setMinWidth(150);
        rightB.setMaxHeight(40);
        rightB.setMinHeight(40);
        rightB.setAlignment(Pos.BASELINE_LEFT);
        rightB.setGraphic(currentSelectedUnit.getImageView(1, 0.25));
        rightB.setOnAction(s->{
            vBox.getChildren().remove(rightB);
            Unit unit = currentList.stream().filter(k->k.getName().equals(rightB.getText())).findFirst().orElse(null);
            setTeamTotalCost(currentSelectedUnit.getTeam(),(getTeamTotalCost(currentSelectedUnit.getTeam())-unit.getCost()));
            currentList.remove(unit);
        });
        return rightB;
    }

    private ComboBox getUnitComboBox(Map<String, Unit> UnitMap, String name){
        ArrayList<String> arrayListO= new ArrayList<>();
        arrayListO.addAll(UnitMap.keySet());
        ObservableList<String> orkNameList = FXCollections.observableArrayList(arrayListO);
        ComboBox unitComboBox = new ComboBox(orkNameList);
        unitComboBox.setMaxWidth(200);
        unitComboBox.setValue(name);
        unitComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                System.out.println("["+t1 + " is Selected]");

                VBox vbox = (VBox) scene.lookup("#centerImageVbox");
                vbox.getChildren().clear();
                vbox.getChildren().add(UnitMap.get(t1).getImageView(1, 1.8));

                VBox leftInfoBox = (VBox)scene.lookup("#leftInfoVbox");
                leftInfoBox.getChildren().clear();
                leftInfoBox.getChildren().add(UnitMap.get(t1).getLeftInfoGridPane());

                VBox rightInfoBox = (VBox)scene.lookup("#rightInfoVbox");
                rightInfoBox.getChildren().clear();
                rightInfoBox.getChildren().add(UnitMap.get(t1).getRightInfoGridPane());
                currentSelectedUnit = UnitMap.get(t1);
            }
        });
        return unitComboBox;
    }

    private GridPane getBoardSizeBlock(){
        GridPane gridPane = new GridPane();
        gridPane.setMaxSize(200, 50);
        gridPane.setMaxSize(200, 50);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(5));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        ComboBox resolutionComboBox = new ComboBox(FXCollections.observableArrayList("1024/768", "1366/768", "1600/900", "1920/1080"));
        resolutionComboBox.setMaxWidth(100);
        resolutionComboBox.setMinWidth(100);
        resolutionComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                System.out.println("["+t1 + " is Selected]");

                if(t1.equals("1024/768")){
                    Size.setSceneWidth(1024);
                    Size.setSceneHeight(768);
                }
                if(t1.equals("1366/768")){
                    Size.setSceneWidth(1366);
                    Size.setSceneHeight(768);
                }
                if(t1.equals("1600/900")){
                    Size.setSceneWidth(1600);
                    Size.setSceneHeight(900);
                }
                if(t1.equals("1920/1080")){
                    Size.setSceneWidth(1920);
                    Size.setSceneHeight(1080);
                }
            }
        });
        resolutionComboBox.setValue("1366/768");

        ComboBox sizeComboBox = new ComboBox(FXCollections.observableArrayList("Small", "Medium", "Large"));
        sizeComboBox.setMaxWidth(100);
        sizeComboBox.setMinWidth(100);
        sizeComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                System.out.println("["+t1 + " is Selected]");

                if(t1.equals("Small")){
                    setBoardWidth(20);
                    setBoardHeight(10);
                }
                if(t1.equals("Medium")){
                    setBoardWidth(40);
                    setBoardHeight(20);
                }
                if(t1.equals("Large")){
                    setBoardWidth(50);
                    setBoardHeight(30);
                }
            }
        });
        sizeComboBox.setValue("Medium");

        Label boardHeightLabel = new Label("Board Size:");
        boardHeightLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        boardHeightLabel.setTextFill(Color.LIGHTCORAL);
        boardHeightLabel.setMaxWidth(100);

        Label screenResolutionLabel = new Label("Resolution:");
        screenResolutionLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        screenResolutionLabel.setTextFill(Color.LIGHTCORAL);
        screenResolutionLabel.setMaxWidth(100);

        gridPane.add(boardHeightLabel, 0,0);
        gridPane.add(sizeComboBox, 1,0);
        gridPane.add(screenResolutionLabel, 0,1);
        gridPane.add(resolutionComboBox, 1,1);

        return gridPane;
    }

    private GridPane getUnitCostBlock(){
        GridPane gridPane = new GridPane();
        gridPane.setMaxSize(200, 100);
        gridPane.setMinSize(200, 100);
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.setPadding(new Insets(5));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        Label teamOneLabel = new Label("Team ONE:");
        teamOneLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        teamOneLabel.setTextFill(Color.LIGHTCORAL);
        teamOneLabel.setMaxWidth(100);

        Label teamTwoLabel = new Label("Team TWO");
        teamTwoLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        teamTwoLabel.setTextFill(Color.LIGHTCORAL);
        teamTwoLabel.setMaxWidth(100);

        Label costOne = new Label();
        costOne.setText(Integer.toString(getTeamTotalCost(1)));
        costOne.setId("costOne");
        costOne.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        costOne.setTextFill(Color.LIGHTCORAL);
        costOne.setMaxWidth(100);

        Label costTwo = new Label();
        costTwo.setText(Integer.toString(getTeamTotalCost(2)));
        costTwo.setId("costTwo");
        costTwo.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        costTwo.setTextFill(Color.LIGHTCORAL);
        costTwo.setMaxWidth(100);

        gridPane.add(teamOneLabel, 0,0);
        gridPane.add(costOne, 1,0);
        gridPane.add(teamTwoLabel, 0,1);
        gridPane.add(costTwo, 1,1);

        return gridPane;
    }

    private GridPane getNextBlock(){
        GridPane gridPane = new GridPane();
        gridPane.setMaxSize(200, 50);
        gridPane.setMaxSize(200, 50);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(5));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        Label armyLimitLabel = new Label("Army Limit:");
        armyLimitLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        armyLimitLabel.setTextFill(Color.LIGHTCORAL);
        armyLimitLabel.setMaxWidth(100);
        armyLimitLabel.setMinWidth(100);

        TextField armyLimit = new TextField();
        armyLimit.setMaxWidth(70);
        armyLimit.setMinWidth(70);
        armyLimit.setText(Integer.toString(getArmyLimit()));
        armyLimit.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("textfield changed from " + oldValue + " to " + newValue);

            VBox leftSC = (VBox)scene.lookup("#leftUnitVbox");
            leftSC.getChildren().remove(0, leftSC.getChildren().size());
            setTeamTotalCost(1, 0);

            VBox rightSC = (VBox)scene.lookup("#rightUnitVbox");
            rightSC.getChildren().remove(0, rightSC.getChildren().size());
            setTeamTotalCost(2, 0);

            try {
                BoardInitializer.setArmyLimit(Integer.parseInt(newValue));
            }catch (NumberFormatException e){
                //    BoardInitializer.setArmyLimit(0);
                //    armyLimit.setText("0");
            }

        });

        Button nextButton = new Button("Next");
        nextButton.setMinWidth(70);
        nextButton.setOnAction(e -> {

            if(Size.getSceneHeight() == 0 || Size.getSceneWidth() == 0){
                Size.setSceneWidth(1366);
                Size.setSceneHeight(768);
            }
            setMainBattlefieldGP(generateCellBattleField(getBoardWidth(), getBoardHeight()));
            Stage stage = new Stage();
            Board main = new Board();
            main.createUI(stage);

            Stage OldStage = (Stage)scene.getWindow();
            OldStage.close();
        });

        gridPane.add(armyLimitLabel, 0,0);
        gridPane.add(armyLimit, 1,0);
        gridPane.add(nextButton, 1,1);

        return gridPane;
    }


    public static  ArrayList<Unit> getCurrentHumanList() {
        return currentHumanList;
    }

    public static ArrayList<Unit> getCurrentOrkList() {
        return currentOrkList;
    }

    public void setTeamTotalCost(int team, int teamTotalCost) {
        if(team == 1){
            this.teamOneTotalCost = teamTotalCost;
            Label label = (Label) scene.lookup("#costOne");
            label.setText(Integer.toString(teamOneTotalCost));
        }else{
            this.teamTwoTotalCost = teamTotalCost;
            Label label = (Label) scene.lookup("#costTwo");
            label.setText(Integer.toString(teamTwoTotalCost));
        }
    }

    public int getTeamTotalCost(int team) {
        return team == 1? teamOneTotalCost:teamTwoTotalCost;
    }
}
