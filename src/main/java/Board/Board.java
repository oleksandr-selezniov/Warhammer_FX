package Board;

import MusicPlayer.MusicPlayerLogic;
import Size.Size;
import javafx.application.Platform;
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
import java.io.File;
import java.net.MalformedURLException;



/**
 * Created by Dmitriy on 20.10.2016.
 */
public class Board {
    private static GridPane mainBattlefieldGP = generateCellBattleField(55, 25);
    private String defaultBackgroundPath = "src\\main\\resources\\Background_1.jpg";
    private static Scene scene;
    private static Double scaleCoefficient = 1.0;

    public void createUI(Stage primaryStage){
        primaryStage.setTitle("WarhammerFX Welcome");

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setMinHeight(Size.getSceneHeight());
        anchorPane.setMinWidth(Size.getSceneWidth());

        ImageView mainBackground = new ImageView();
        mainBackground.setImage(getBackgroundImage());
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
        primaryStage.show();
        BoardInitializer boardInit = new BoardInitializer();
        boardInit.initializeBoard();
        BoardUtils.refreshZOrder();
    }

    public HBox generateBottomMenu(){
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
        leftEarIV.setId("leftImageView");

        ScrollPane scrollPane = new ScrollPane(leftEarIV);
        scrollPane.setMinHeight(Size.getSceneHeight()*0.2);
        scrollPane.setMinWidth(Size.getSceneWidth()*0.15);
        leftEarGP.add(scrollPane, 1,0);

        VBox leftVbox = new VBox();
        leftVbox.setPadding(new Insets(5));
        leftVbox.setAlignment(Pos.CENTER);
        leftVbox.setFillWidth(true);
        leftVbox.setMinHeight(Size.getSceneHeight()*0.25);
        leftVbox.setMinWidth(Size.getSceneWidth()*0.15);

//        Button leftUpButton = new Button();
//        leftUpButton.setMinWidth(150);
//        leftUpButton.setMinHeight(40);

        TextArea leftTextArea = new TextArea();
        leftTextArea.setMaxWidth(Size.getSceneWidth()*0.13);
        leftTextArea.setMaxHeight(Size.getSceneHeight()*0.26);
        leftTextArea.setId("leftTextArea");

//        Button leftDownButton = new Button();
//        leftDownButton.setMinWidth(150);
//        leftDownButton.setMinHeight(40);
        leftVbox.getChildren().addAll(leftTextArea);
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

        Button rightButton1 = new Button();
        rightButton1.setMinWidth(70);
        rightButton1.setMinHeight(20);

        Button rightButton2 = new Button();
        rightButton2.setMinWidth(70);
        rightButton2.setMinHeight(20);

        Button rightButton3 = new Button();
        rightButton3.setMinWidth(70);
        rightButton3.setMinHeight(20);

        rightButtonVbox.getChildren().addAll(rightButton1, rightButton2, rightButton3);
        rightEarGP.add(rightButtonVbox,2,0);
        ImageView rightEarIV = new ImageView();
        rightEarIV.setFitWidth(Size.getSceneWidth()*0.183);
        rightEarIV.setFitHeight(Size.getSceneHeight()*0.39);
        rightEarIV.setId("rightImageView");

        ScrollPane scrollPane = new ScrollPane(rightEarIV);
        scrollPane.setMinHeight(Size.getSceneHeight()*0.2);
        scrollPane.setMinWidth(Size.getSceneWidth()*0.15);
        rightEarGP.add(scrollPane, 1,0);

        VBox rightVbox = new VBox();
        rightVbox.setPadding(new Insets(5));
        rightVbox.setAlignment(Pos.CENTER);
        rightVbox.setFillWidth(true);
        rightVbox.setMinHeight(Size.getSceneHeight()*0.25);
        rightVbox.setMinWidth(Size.getSceneWidth()*0.15);

        TextArea rightTextArea = new TextArea();
        rightTextArea.setMaxWidth(Size.getSceneWidth()*0.13);
        rightTextArea.setMaxHeight(Size.getSceneHeight()*0.26);
        rightTextArea.setId("rightTextArea");

        rightVbox.getChildren().addAll(rightTextArea);

        rightEarGP.add(rightVbox, 0,0);

        return rightEarGP;
    }

    public GridPane getCentralGP(){
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

    public static Scene getScene(){
        return scene;
    }

    public static GridPane getMainBattlefieldGP(){
        return mainBattlefieldGP;
    }

    public static void writeToTextArea(String id, String text, boolean scrollDowm){
        TextArea textArea = (TextArea)scene.lookup(id);
        Platform.runLater(()->textArea.setText(text));
        Platform.runLater(()->textArea.setEditable(false));
        if(scrollDowm){
        Platform.runLater(()->textArea.positionCaret(textArea.getText().length()));
        Platform.runLater(()->textArea.setScrollTop(Double.MAX_VALUE));
        }
    }

    public static String getTextFromTextArea(String id){
        TextArea textArea = (TextArea)scene.lookup(id);
        return textArea.getText();
    }

    public static void setImageToImageView(String id, Image image){
        ImageView imageView = (ImageView) scene.lookup(id);
        imageView.setImage(image);
    }

    private HBox getMainTitle(){
        HBox hbox = new HBox();
        Label title = new Label("WARHAMMER GAME");
        title.setPadding(new Insets(3, 3, 0, 500));
        title.setAlignment(Pos.CENTER);
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        title.setTextFill(Color.LIGHTCORAL);
        title.setEffect(new Bloom());
        Label damagePanel = new Label("damagePanel");
        damagePanel.setId("damagePanel");
        damagePanel.setPadding(new Insets(3, 3, 0, 3));
        damagePanel.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        damagePanel.setTextFill(Color.ORANGE);
        damagePanel.setEffect(new Bloom());

        hbox.getChildren().addAll(title, damagePanel);
        return hbox;
    }

    private Image getBackgroundImage(){
        String localUrl = null;
        try {
            File file = new File(defaultBackgroundPath);
            localUrl = file.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return new Image(localUrl, false);
    }

    private static GridPane generateCellBattleField( int length, int height){
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
        slider.setMin(0.175);
        slider.setMax(3.0);
        slider.setValue(1);
        slider.setMajorTickUnit(0.1);
        slider.setBlockIncrement(0.5);
        slider.setShowTickMarks(true);
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                for(int i=0; i<getMainBattlefieldGP().getChildren().size();i++){
                    Node node = getMainBattlefieldGP().getChildren().get(i);
                    if(node instanceof GameCell){

                        GameCell gc = (GameCell)node;
                        gc.setSize(Size.getCellWidth()*newValue.doubleValue(), Size.getCellHeight()*newValue.doubleValue());
                        setScaleCoefficient(newValue.doubleValue());
                        if(gc.getUnit()!=null){
                            gc.setGraphic(gc.getUnit().getImageView(1.0));
                            gc.setPadding(gc.getUnit().getInsets());
                        }else{
                            gc.setDefaultImage();
                        }
                    }
                }
            }
        });
        return slider;
    }

    public static Double getScaleCoefficient() {
        return scaleCoefficient;
    }

    public static void setScaleCoefficient(Double scaleCoefficient) {
        Board.scaleCoefficient = scaleCoefficient;
    }
}




