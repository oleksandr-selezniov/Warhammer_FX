package Board;

import Size.Size;
import Units.MeleeInfantry;
import Units.Unit;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.io.File;
import java.net.MalformedURLException;

import static Board.Board.getScene;
import static Board.Board.initializeBottomMenu;

/**
 * Created by Dmitriy on 18.10.2016.
 */
public class GameCell extends Button {
    private static GameCell previousGameCell;
    private static Unit temporaryUnit;
    private static boolean isSelected;
    private static int teamTurnValue = 1;
    private String defaultCellImagePath = "src\\main\\resources\\cellBackground\\"+generateRandomNumber(1,25)+".jpg";
    private String deadCellImagePath = "src\\main\\resources\\dead.jpg";
    private static String obstacleImagePath; //= "src\\main\\resources\\Obstacles\\"+generateRandomNumber(1,25)+".jpg";
    private String name = this.getText();
    private int xCoord;
    private int yCoord;
    private Boolean isBlocked = false;
    private Boolean isPassable = false;
    private Boolean isInShootingRange = false;
    private Boolean isSafe = true;
    private Boolean isDangerous = false;
    private Boolean isStrategical = false;
    private  boolean isActivated = false;
    private int owner = 0;
    private Unit unit;

    GameCell() {
        this.setPadding(new Insets(50));
        this.setSize(Size.getCellWidth(), Size.getCellHeight());
        this.setCellImage(this, defaultCellImagePath, 0.6);
        this.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        actionMode();
        mouseMovementMode();
    }

    public void actionMode() {
        this.setOnMouseClicked(e -> {

            if (!isSelected) { // first step
                if (unit != null && isTeamTurn(unit.getTeam()) && unit.isActive()) {
                    isSelected = true;
                    temporaryUnit = unit;
                    BoardUtils.calculateRanges(GameCell.this);
                    GameCell.this.setGraphic(unit.getImageView(0.7));
                    previousGameCell = GameCell.this;
                    unit = null;
                }
            } else if (GameCell.this.isPassable() && unit == null) { // second step
                if (temporaryUnit != null) {
                    unit = temporaryUnit;
                    GameCell.this.setGraphic(temporaryUnit.getImageView(1.0));
                    GameCell.this.setPadding(temporaryUnit.getInsets());
                    previousGameCell.setEffect(null);
                    temporaryUnit = null;
                }
                isSelected = false;
                if (!(previousGameCell.equals(GameCell.this))) {
                    previousGameCell.setCellImage(previousGameCell, defaultCellImagePath, 0.6);
                    previousGameCell.setPadding(new Insets(1));
                    unit.setActive(false);
                    checkTeamTurn();
                }
                BoardUtils.refreshZOrder();
                BoardUtils.abortFieldPassability();
                BoardUtils.abortShootingRange();

            } else if (GameCell.this.isStrategical() && unit == null) { // activate strategical cell
                temporaryUnit.setActive(false);
                previousGameCell.setUnit(temporaryUnit);
                previousGameCell.setGraphic(temporaryUnit.getImageView(1.0));
                isSelected = false;
                if (GameCell.this.getOwner() != temporaryUnit.getTeam()){
                    GameCell.this.activate(temporaryUnit);
                    checkTeamTurn();
                }
                BoardUtils.refreshZOrder();
                BoardUtils.abortFieldPassability();
                BoardUtils.abortShootingRange();

            } else if (unit != null && temporaryUnit.isEnemyUnit(unit) && isTeamTurn(temporaryUnit.getTeam())) { // attack
                previousGameCell.setUnit(temporaryUnit);
                previousGameCell.setGraphic(temporaryUnit.getImageView(1.0));
                isSelected = false;
                if (GameCell.this.isInShootingRange() || (BoardUtils.isOnNeighbouringCellPlusDiagonal(previousGameCell, GameCell.this))) {
                    performAttack(previousGameCell, GameCell.this);
                }
                BoardUtils.abortFieldPassability();
                BoardUtils.abortShootingRange();
                temporaryUnit = null;
            }
        });
    }

    public void performAttack(GameCell hunter_GC, GameCell victim_CG){

        if (unit.getHealth() > 0) {
            victim_CG.getGraphic().setEffect(new SepiaTone());

            if (BoardUtils.isOnNeighbouringCellPlusDiagonal(hunter_GC, victim_CG)) {
                temporaryUnit.performCloseAttack(unit);
                checkTeamTurn();
            } else if(!(temporaryUnit instanceof MeleeInfantry)){
                    temporaryUnit.performRangeAttack(unit);
                    checkTeamTurn();
            }
        }
        if (unit.getHealth() <= 0) {
            LoggerUtils.writeDeadLog(temporaryUnit, unit);
            GameCell.this.setCellImage(previousGameCell, deadCellImagePath, 0.6);
            unit = null;
            checkTeamTurn();
        }
    }

    public void mouseMovementMode(){
        this.setOnMouseEntered(e -> {
            if (GameCell.this.getUnit() != null) {
                GameCell.this.setTooltip(getToolTip(GameCell.this));
                if(GameCell.this.getUnit().getTeam()==1){
                    initializeBottomMenu(GameCell.this, "left");
                }else{
                    initializeBottomMenu(GameCell.this, "right");
                }
            }
            if (!(GameCell.this.getEffect() instanceof InnerShadow)){
                GameCell.this.getGraphic().setEffect(new Glow());

                if (isAlreadyUsedCurrentTeamUnit())
                    paintUnitWithBlack();
            }

            if (temporaryUnit != null && GameCell.this.getUnit() != null){
                if (GameCell.this.getUnit().isEnemyUnit(temporaryUnit)) {
                    highlightEnemyUnit();
                }
            }
        });

        this.setOnMouseExited(e -> {
            GameCell.this.setCursor(Cursor.DEFAULT);
            GameCell.this.getGraphic().setEffect(null);
            GameCell.this.setTooltip(null);
        });
    }

    public void setCellImage(GameCell gc, String path, double opacity){
        ImageView imageView = new ImageView();
        String imageUrl = null;
        try {
            File file = new File(path);
            imageUrl = file.toURI().toURL().toString();
        }catch (MalformedURLException e){e.printStackTrace();}
        Image buttonImage = new Image(imageUrl, false);
        imageView.setImage(buttonImage);
        imageView.fitHeightProperty().bindBidirectional(gc.minHeightProperty());
        imageView.fitWidthProperty().bindBidirectional(gc.minWidthProperty());
        imageView.setOpacity(opacity);
        this.setPadding(new Insets(1));
        this.setGraphic(imageView);
    }

    public void setSize(Double width, Double height){
        this.setMaxHeight(height);
        this.setMinHeight(height);
        this.setMaxWidth(width);
        this.setMinWidth(width);
    }

    public static void checkTeamTurn(){
        if(BoardUtils.getTotalUnitNumber(getEnemyTeam())<=0){
            Board.writeToTextArea("#centerTextArea", Board.getTextFromTextArea("#centerTextArea") + "\n TEAM " + teamTurnValue + " WINS!", true);
            BoardUtils.setActiveTeamUnits(teamTurnValue, false);
        }else{
            if(BoardUtils.getActiveUnitNumber(teamTurnValue) == 0) {
                changeTeamTurn();
                Board.writeToTextArea("#centerTextArea", Board.getTextFromTextArea("#centerTextArea") + " Now it's Team " + teamTurnValue + " turn\n", true);
                BoardUtils.setActiveTeamUnits(teamTurnValue, true);
            }
        }
    }

    private static Tooltip getToolTip(GameCell gameCell){
        Tooltip tooltip = new Tooltip(gameCell.getUnit().getInfo());
        tooltip.setAutoHide(false);
        return tooltip;
    }

    private static void placeObstacle(GameCell gameCell, String path){
        gameCell.setBlocked(true);
        gameCell.setCellImage(gameCell, path, 1);
    }

    static void generateObstacles(double density){
        Board.getMainBattlefieldGP().getChildren().forEach(p->{
            double chance = Math.random();
            if(((GameCell)p).getUnit()==null && ((GameCell)p).getxCoord()>3 && ((GameCell)p).getyCoord()>2){
                if(density > chance){
                    obstacleImagePath = "src\\main\\resources\\Obstacles\\"+generateRandomNumber(1,19)+".jpg";
                    placeObstacle(((GameCell)p), obstacleImagePath);
                }
            }
        });
    }

    private boolean isAlreadyUsedCurrentTeamUnit(){
        return (GameCell.this.getUnit()!=null && !GameCell.this.getUnit().isActive() && GameCell.this.getUnit().getTeam()==teamTurnValue);
    }

    private void highlightEnemyUnit(){
        Cursor c = new ImageCursor(BoardUtils.getImage("src\\main\\resources\\CursorChainsword.png"), 300,300);
        this.setCursor(c);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(3);
        dropShadow.setOffsetY(3);
        dropShadow.setColor(Color.rgb(255, 0, 0, 1));
        this.getGraphic().setEffect(dropShadow);
    }

    private void paintUnitWithBlack(){
        this.getGraphic().setEffect(new Lighting(new Light.Spot()));
    }

    static void makeStrategical(int x, int y){
        GameCell gameCell = (GameCell) Board.getScene().lookup("#" + x + "_" + y);
        gameCell.setStrategical(true);
        gameCell.setBlocked(true);
        //gc.setCellImage();
    }

    void activate(Unit activator){
        this.setActivated(true);
        this.setOwner(activator.getTeam());
        if(activator.getTeam() == 1){
            this.setStyle("-fx-background-color: #00FF00");
        }else{
            this.setStyle("-fx-background-color: #FF0000");
        }
    }

    private static int generateRandomNumber(int min, int max){
        return min + (int)(Math.random() * ((max - min) + 1));
    }

    public static void changeTeamTurn(){
        teamTurnValue = teamTurnValue==1? 2:1 ;
    }

    public static int getEnemyTeam(){
        return teamTurnValue==1? 2:1;
    }

    private boolean isTeamTurn(int team){
        return (team == teamTurnValue);
    }

    public static int getTeamTurnValue() {
        return teamTurnValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getxCoord() {
        return xCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    public Boolean isPassable() {
        return isPassable;
    }

    public Boolean isInShootingRange() {
        return isInShootingRange;
    }

    public void setInShootingRange(Boolean inShootingRange) {
        isInShootingRange = inShootingRange;
    }

    public void setPassable(Boolean passable) {
        isPassable = passable;
    }

    public Boolean getSafe() {
        return isSafe;
    }

    public void setSafe(Boolean safe) {
        isSafe = safe;
    }

    public Boolean getDangerous() {
        return isDangerous;
    }

    public void setDangerous(Boolean dangerous) {
        isDangerous = dangerous;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public Boolean isStrategical() {
        return isStrategical;
    }

    public void setStrategical(Boolean strategical) {
        isStrategical = strategical;
    }

    public Boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
    }

}
