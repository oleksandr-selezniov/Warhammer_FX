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

import java.net.URL;

import static Board.Board.initializeBottomMenu;
import static Board.BoardInitializer.*;
import static Board.BoardInitializer.getTeam1Score;
import static Board.BoardUtils.getStrategicalPoints;

/**
 * Created by Dmitriy on 18.10.2016.
 */
public class GameCell extends Button {
    private static GameCell previousGameCell;
    private static Unit temporaryUnit;
    private static boolean isSelected;
    private static int teamTurnValue = 1;
    private String defaultCellImagePath = "cellBackground/"+generateRandomNumber(1,25)+".jpg";
    private static String deadCellImagePath = "other/dead.jpg";
    private static String obstacleImagePath;
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
        //this.setSize(Size.getCellSize());
        this.maxHeightProperty().bindBidirectional(Size.cellSizeProperty());
        this.maxWidthProperty().bindBidirectional(Size.cellSizeProperty());
        this.minHeightProperty().bindBidirectional(Size.cellSizeProperty());
        this.minWidthProperty().bindBidirectional(Size.cellSizeProperty());

        this.setCellImage(defaultCellImagePath, 0.6);
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
                    GameCell.this.setPadding(temporaryUnit.getInsetsY());
                    previousGameCell.setEffect(null);
                    temporaryUnit = null;
                }
                isSelected = false;
                if (!(previousGameCell.equals(GameCell.this))) {
                    previousGameCell.setCellImage(defaultCellImagePath, 0.6);
                    previousGameCell.setPadding(new Insets(1));
                    unit.setActive(false);
                    checkTeamTurn();
                }
                BoardUtils.refreshZOrder();
                BoardUtils.abortFieldPassability();
                BoardUtils.abortShootingRange();

            } else if (GameCell.this.isStrategical() && unit == null) { // activate strategical cell
                previousGameCell.setUnit(temporaryUnit);
                previousGameCell.setGraphic(temporaryUnit.getImageView(1.0));
                isSelected = false;
                if (BoardUtils.isOnNeighbouringCellPlusDiagonal(previousGameCell, GameCell.this)){
                    if (GameCell.this.getOwner() != temporaryUnit.getTeam()){
                        GameCell.this.activate(temporaryUnit);
                        temporaryUnit.setActive(false);
                        checkTeamTurn();
                    }
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
            GameCell.this.setCellImage(deadCellImagePath, 0.6);
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

    public void setCellImage(String path, double opacity){
        ImageView imageView = new ImageView();
        ClassLoader classLoader = getClass().getClassLoader();
        URL urlToImage = classLoader.getResource(path);
        Image buttonImage = new Image(urlToImage.toString(), false);
        imageView.setImage(buttonImage);
        imageView.fitHeightProperty().bindBidirectional(this.minHeightProperty());
        imageView.fitWidthProperty().bindBidirectional(this.minWidthProperty());
        imageView.setOpacity(opacity);
        this.setPadding(new Insets(1));
        this.setGraphic(imageView);
    }

    public void setSize(Double size){
        this.setMaxHeight(size);
        this.setMinHeight(size);
        this.setMaxWidth(size);
        this.setMinWidth(size);
    }

    public static void checkTeamTurn(){
        if(getTeam1Score() >= BoardInitializer.getScoreLimit() | getTeam2Score() >=BoardInitializer.getScoreLimit()) {
            int winner = (getTeam1Score() >= BoardInitializer.getScoreLimit())? 1:2;
            LoggerUtils.writeWinLog(winner);
            endGame();
        }else{
            if(BoardUtils.getTotalUnitNumber(getEnemyTeam())<=0){
                endGame();
            }else {
                if (BoardUtils.getActiveUnitNumber(teamTurnValue) == 0) {
                    changeTeamTurn();
                    LoggerUtils.writeTurnLog(teamTurnValue);
                    BoardUtils.setActiveTeamUnits(teamTurnValue, true);
                }
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
        gameCell.setCellImage(path, 1);
    }

    static void generateObstacles(double density){
        Board.getMainBattlefieldGP().getChildren().forEach(p->{
            double chance = Math.random();
            if(((GameCell)p).getUnit()==null && !((GameCell)p).isBlocked()
                    && ((GameCell)p).getxCoord()>1 && ((GameCell)p).getyCoord()>1
                    && ((GameCell)p).getxCoord()<(Board.getBoardWidth()-2)
                    && ((GameCell)p).getyCoord()<(Board.getBoardHeight()-2)){
                if(density > chance){
                    obstacleImagePath = "obstacles/"+generateRandomNumber(1,8)+".jpg";
                    placeObstacle(((GameCell)p), obstacleImagePath);
                }
            }
        });
    }

    private boolean isAlreadyUsedCurrentTeamUnit(){
        return (GameCell.this.getUnit()!=null && !GameCell.this.getUnit().isActive() && GameCell.this.getUnit().getTeam()==teamTurnValue);
    }

    private void highlightEnemyUnit(){
        Cursor c = new ImageCursor(new BoardUtils().getImage("other/CursorChainsword.png"), 300,300);
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
        gameCell.setCellImage("other/strartegical_point.jpg", 1);
    }

    void activate(Unit activator){
        this.setActivated(true);
        this.setOwner(activator.getTeam());
        if(activator.getTeam() == 2){
            this.setEffect(new Lighting()); //setStyle("-fx-background-color: #00FF00"); TROUBLE with styling. Research needed
        }else{
            this.setEffect(new Bloom()); //setStyle("-fx-background-color: #FF0000");
        }
    }

    private static int generateRandomNumber(int min, int max){
        return min + (int)(Math.random() * ((max - min) + 1));
    }

    public static void changeTeamTurn(){
        setTeam1Score(getStrategicalPoints(1));
        setTeam2Score(getStrategicalPoints(2));
        Board.setScore(getTeam1Score() + " : " + getTeam2Score());
        teamTurnValue = teamTurnValue==1? 2:1 ;
    }

    private static void endGame(){
        BoardUtils.setActiveTeamUnits(1, false);
        BoardUtils.setActiveTeamUnits(2, false);
        Board.getScene().lookup("#end_turn").setDisable(true);
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

    public static String getDeadCellImagePath() {
        return deadCellImagePath;
    }
}
