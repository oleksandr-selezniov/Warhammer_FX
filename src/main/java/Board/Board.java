package Board;

import MusicPlayer.MusicPlayerLogic;
import Size.Size;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import static Board.BoardInitializer.getScoreLimit;


/**
 * Created by Dmitriy on 20.10.2016.
 */
public class Board {
    private static int boardHeight = 20;
    private static int boardWidth = 40;
    private static GridPane mainBattlefieldGP;
    private String defaultBackgroundPath = "backgrounds/background_8.jpg";
    private static Scene scene;
    private static Double scaleCoefficient = 1.0;

    void createUI(Stage primaryStage){
        primaryStage.setTitle("WarhammerFX Welcome");

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setMinHeight(Size.getSceneHeight());
        anchorPane.setMinWidth(Size.getSceneWidth());

        ImageView mainBackground = new ImageView();
        mainBackground.setImage(new BoardUtils().getImage(defaultBackgroundPath));
        mainBackground.setOpacity(0.9);
        mainBackground.setFitWidth(Size.getSceneWidth());
        mainBackground.setFitHeight(Size.getSceneHeight());
        anchorPane.getChildren().add(mainBackground);
        AnchorPane.setTopAnchor(mainBackground, 0.0);

        BorderPane borderPane = new BorderPane();
        borderPane.setMaxWidth(Size.getSceneWidth());
        borderPane.setMaxHeight(Size.getSceneHeight()*0.915);

        anchorPane.getChildren().add(borderPane);

        ScrollPane scrollPane = new ScrollPane(mainBattlefieldGP);
        scrollPane.setMinWidth(Size.getSceneWidth()*0.95);
        scrollPane.setMinHeight(Size.getSceneHeight()*0.675);
        scrollPane.setMaxWidth(Size.getSceneWidth()*0.95);
        scrollPane.setMaxHeight(Size.getSceneHeight()*0.675);
        borderPane.setPadding(new Insets(0,15,0,15));

        borderPane.setCenter(scrollPane);
        borderPane.setLeft(getScaleSlider());
        borderPane.setTop(getMainTitle());
        borderPane.setBottom(generateBottomMenu());

        scene = new Scene(anchorPane, Size.getSceneWidth(), Size.getSceneHeight());
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(Size.getSceneHeight());
        primaryStage.setMinWidth(Size.getSceneWidth());
        primaryStage.setMaxHeight(Size.getSceneHeight());
        primaryStage.setMaxWidth(Size.getSceneWidth());
        primaryStage.show();
        BoardInitializer boardInit = new BoardInitializer();
        boardInit.initializeBoard();
        BoardUtils.refreshZOrder();
    }

    private HBox generateBottomMenu(){
        HBox hBox = new HBox();
        hBox.setMinWidth(Size.getSceneWidth()*0.96);
        hBox.setMinHeight(Size.getSceneHeight()*0.25);
        hBox.setMaxWidth(Size.getSceneWidth()*0.96);
        hBox.setMaxHeight(Size.getSceneHeight()*0.25);

        BorderPane borderPane = new BorderPane();
        borderPane.setMinWidth(Size.getSceneWidth()*0.96);
        borderPane.setMinHeight(Size.getSceneHeight()*0.25);
        borderPane.setMaxWidth(Size.getSceneWidth()*0.96);
        borderPane.setMaxHeight(Size.getSceneHeight()*0.25);
        borderPane.setPadding(new Insets(3,0,3,0));
        borderPane.setLeft(getLeftEarGP());
        borderPane.setRight(getRightEarGP());
        borderPane.setCenter(getCentralGP());

        hBox.getChildren().add(borderPane);
        return hBox;
    }

    private GridPane getLeftEarGP(){
        GridPane leftEarGP = new GridPane();
        leftEarGP.setGridLinesVisible(true);
        leftEarGP.setMinWidth(Size.getSceneWidth()*0.3);
        leftEarGP.setMinHeight(Size.getSceneHeight()*0.25);
        leftEarGP.add(MusicPlayerLogic.getMusicPlayer(),0,0);

        ImageView leftEarIV = new ImageView();
        leftEarIV.setFitWidth(Size.getSceneWidth()*0.183);
        leftEarIV.setFitHeight(Size.getSceneHeight()*0.39);
        leftEarIV.setImage(new BoardUtils().getImage("other/chaos.jpg"));
        leftEarIV.setId("leftImageView");

        ScrollPane scrollPane = new ScrollPane(leftEarIV);
        scrollPane.setId("leftScrollPane");
        scrollPane.setEffect(new InnerShadow());
        scrollPane.setMinHeight(Size.getSceneHeight()*0.25);
        scrollPane.setMinWidth(Size.getSceneWidth()*0.18);
        scrollPane.setMaxHeight(Size.getSceneHeight()*0.25);
        scrollPane.setMaxWidth(Size.getSceneWidth()*0.18);
        leftEarGP.add(scrollPane, 1,0);

        VBox leftVbox = new VBox();
        leftVbox.setPadding(new Insets(5));
        leftVbox.setAlignment(Pos.TOP_CENTER);
        leftVbox.setFillWidth(true);
        leftVbox.setMinHeight(Size.getSceneHeight()*0.25);
        leftVbox.setMinWidth(Size.getSceneWidth()*0.15);
        leftVbox.setMaxHeight(Size.getSceneHeight()*0.25);
        leftVbox.setMaxWidth(Size.getSceneWidth()*0.15);
        leftVbox.setId("leftVBox");
//
//        TextArea leftTextArea = new TextArea();
//        leftTextArea.setMaxWidth(Size.getSceneWidth()*0.14);
//        leftTextArea.setMaxHeight(Size.getSceneHeight()*0.26);
//        leftTextArea.setId("leftTextArea");

//        leftVbox.getChildren().addAll(leftTextArea);
        leftEarGP.add(leftVbox, 2,0);

        return leftEarGP;
    }

    private GridPane getRightEarGP(){
        GridPane rightEarGP = new GridPane();
        rightEarGP.setAlignment(Pos.CENTER_RIGHT);
        rightEarGP.setGridLinesVisible(true);
        rightEarGP.setMinWidth(Size.getSceneWidth()*0.3);
        rightEarGP.setMinHeight(Size.getSceneHeight()*0.25);

        VBox rightButtonVbox = new VBox();
        rightButtonVbox.setMinWidth(100);
        rightButtonVbox.setPadding(new Insets(10));

        Button endTurnButton = new Button("End Turn");
        endTurnButton.setId("end_turn");
        endTurnButton.setMinWidth(70);
        endTurnButton.setMinHeight(20);
        endTurnButton.setOnAction(p-> {
            GameCell.checkTeamTurn();
            BoardUtils.setActiveTeamUnits(GameCell.getTeamTurnValue(), false);
            GameCell.changeTeamTurn();
            BoardUtils.setActiveTeamUnits(GameCell.getTeamTurnValue(), true);
        });


        Button rightButton2 = new Button();
        rightButton2.setMinWidth(70);
        rightButton2.setMinHeight(20);

        Button rightButton3 = new Button();
        rightButton3.setMinWidth(70);
        rightButton3.setMinHeight(20);

        rightButtonVbox.getChildren().addAll(endTurnButton, rightButton2, rightButton3);
        rightEarGP.add(rightButtonVbox,2,0);

        ImageView rightEarIV = new ImageView();
        rightEarIV.setFitWidth(Size.getSceneWidth()*0.183);
        rightEarIV.setFitHeight(Size.getSceneHeight()*0.39);
        rightEarIV.setImage(new BoardUtils().getImage("other/chaos.jpg"));
        rightEarIV.setId("rightImageView");

        ScrollPane scrollPane = new ScrollPane(rightEarIV);
        scrollPane.setId("rightScrollPane");
        scrollPane.setEffect(new InnerShadow());
        scrollPane.setMinHeight(Size.getSceneHeight()*0.25);
        scrollPane.setMinWidth(Size.getSceneWidth()*0.18);
        scrollPane.setMaxHeight(Size.getSceneHeight()*0.25);
        scrollPane.setMaxWidth(Size.getSceneWidth()*0.18);
        rightEarGP.add(scrollPane, 1,0);

        VBox rightVbox = new VBox();
        rightVbox.setPadding(new Insets(5));
        rightVbox.setAlignment(Pos.TOP_CENTER);
        rightVbox.setFillWidth(true);
        rightVbox.setMinHeight(Size.getSceneHeight()*0.25);
        rightVbox.setMinWidth(Size.getSceneWidth()*0.15);
        rightVbox.setMaxHeight(Size.getSceneHeight()*0.25);
        rightVbox.setMaxWidth(Size.getSceneWidth()*0.15);
        rightVbox.setId("rightVBox");

//        TextArea rightTextArea = new TextArea();
//        rightTextArea.setMaxWidth(Size.getSceneWidth()*0.14);
//        rightTextArea.setMaxHeight(Size.getSceneHeight()*0.26);
//        rightTextArea.setId("rightTextArea");

//        rightVbox.getChildren().addAll(rightTextArea);
        rightEarGP.add(rightVbox, 0,0);
        return rightEarGP;
    }

    private GridPane getCentralGP(){
        GridPane mainGridPane = new GridPane();
        mainGridPane.setAlignment(Pos.CENTER);
        mainGridPane.setGridLinesVisible(true);
        mainGridPane.setMaxHeight(Size.getSceneHeight()*0.25);
        mainGridPane.setMaxWidth(Size.getSceneWidth()*0.1);
        mainGridPane.setMinHeight(Size.getSceneHeight()*0.25);
        mainGridPane.setMinWidth(Size.getSceneWidth()*0.1);

        TextArea centerTextArea = new TextArea();
        centerTextArea.setMinWidth(Size.getSceneWidth()*0.153);
        centerTextArea.setId("centerTextArea");
        mainGridPane.add(centerTextArea, 0,0);

        return mainGridPane;
    }

    static Scene getScene(){
        return scene;
    }

    static GridPane getMainBattlefieldGP(){
        return mainBattlefieldGP;
    }

    static void writeToTextArea(String id, String text, boolean scrollDowm){
        TextArea textArea = (TextArea)scene.lookup(id);
        textArea.setText(text);
        textArea.setEditable(false);
        if(scrollDowm){
        textArea.positionCaret(textArea.getText().length());
        textArea.setScrollTop(Double.MAX_VALUE);
        }
    }

    static String getTextFromTextArea(String id){
        TextArea textArea = (TextArea)scene.lookup(id);
        return textArea.getText();
    }

    static void setImageToImageView(String id, Image image){
        ImageView imageView = (ImageView) scene.lookup(id);
        imageView.setImage(image);
    }

    private HBox getMainTitle(){
        HBox hbox = new HBox();
        Label title = new Label("ICEHAMMER GAME");
        title.setPadding(new Insets(3, 3, 0, 500));
        title.setAlignment(Pos.CENTER);
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        title.setTextFill(Color.LIGHTCORAL);
        title.setEffect(new Bloom());

        Label damagePanel = new Label("Score: ");
        damagePanel.setId("score");
        damagePanel.setPadding(new Insets(3, 3, 0, 3));
        damagePanel.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        damagePanel.setTextFill(Color.ORANGE);
        damagePanel.setEffect(new Bloom());

//        ProgressBar dc = new ProgressBar(1);
//        dc.
//

        hbox.getChildren().addAll(title, damagePanel);
        return hbox;
    }

    public static void setScore(String text){
        Label score = (Label)scene.lookup("#score");
        score.setText("Score: "+text + " Limit "+ getScoreLimit());
    }

    static GridPane generateCellBattleField( int length, int height){
        GridPane gridPane = new GridPane();
        gridPane.setId("boardGC");
        gridPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        gridPane.setPadding(new Insets(1));
        gridPane.setVgap(0.5);
        gridPane.setHgap(0.5);

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < height; j++) {
                GameCell gameCell = new GameCell();
                gameCell.setxCoord(i);
                gameCell.setyCoord(j);
                gameCell.setText(i + "-" + j);
                gameCell.setId(i + "_" + j);
                gridPane.add(gameCell, i, j);
            }
        }
        return gridPane;
    }

    private Slider getScaleSlider(){
        Slider slider = new Slider();
        slider.setPrefWidth(10);
        slider.setPrefHeight(120);
        slider.setOrientation(Orientation.VERTICAL);
        slider.setCursor(Cursor.HAND);
//        slider.setMin(0.19);
//        slider.setMax(2.0);
//        slider.setMajorTickUnit(0.1);
//        slider.setBlockIncrement(0.1);

        slider.setMin(10);
        slider.setMax(200);
        slider.setMajorTickUnit(10);
        slider.setBlockIncrement(10);
        slider.setShowTickMarks(true);

        slider.valueProperty().bindBidirectional(Size.cellSizeProperty());


//        slider.valueProperty().addListener(new ChangeListener<Number>() {
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                for(int i=0; i<getMainBattlefieldGP().getChildren().size();i++){
//                    Node node = getMainBattlefieldGP().getChildren().get(i);
//                    if(node instanceof GameCell){
//
//                          GameCell gc = (GameCell)node;
//                        gc.setSize(Size.getCellWidth()*newValue.doubleValue(), Size.getCellSize()*newValue.doubleValue());
//                        setScaleCoefficient(newValue.doubleValue());
//                        if(gc.getUnit()!=null){
//                            gc.setGraphic(gc.getUnit().getImageView(1.0));
//                            gc.setPadding(gc.getUnit().getInsetsY());
//                        }
//                    }
//                }
//            }
//        });
        slider.setValue(30);
        return slider;
    }

    static void initializeBottomMenu(GameCell gameCell, String direction){
        HBox hbox = new HBox(gameCell.getUnit().getImageView(1, 1.1));
        hbox.setMinWidth(Size.getSceneWidth()*0.18);
        hbox.setMinHeight(Size.getSceneHeight()*0.25);
        if(direction.equals("left")){
            hbox.setStyle("-fx-background-color: linear-gradient(from 25% 100% to 100% 25%, #703D66, #99FFFF)");
            VBox vBox = (VBox)scene.lookup("#leftVBox");
            vBox.getChildren().clear();
            vBox.getChildren().add(gameCell.getUnit().getBattleInfoGridPane());
        }else{
            hbox.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #99FFFF, #703D66)");
            VBox vBox = (VBox)scene.lookup("#rightVBox");
            vBox.getChildren().clear();
            vBox.getChildren().add(gameCell.getUnit().getBattleInfoGridPane());
        }
        hbox.setAlignment(Pos.CENTER);
        ScrollPane scrollPane = (ScrollPane)scene.lookup("#"+direction+"ScrollPane");
        scrollPane.setContent(hbox);
    }

    public static Double getScaleCoefficient() {
        return Size.getCellSizePercentage();
    }

//    private static void setScaleCoefficient(Double scaleCoefficient) {
//        Board.scaleCoefficient = scaleCoefficient;
//    }


    public static int getBoardHeight() {
        return boardHeight;
    }

    public static void setBoardHeight(int boardHeight) {
        Board.boardHeight = boardHeight;
    }

    public static int getBoardWidth() {
        return boardWidth;
    }

    public static void setBoardWidth(int boardWidth) {
        Board.boardWidth = boardWidth;
    }

    public static void setMainBattlefieldGP(GridPane mainBattlefieldGP) {
        Board.mainBattlefieldGP = mainBattlefieldGP;
    }
}





