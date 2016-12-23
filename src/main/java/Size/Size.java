package Size;

import javafx.beans.property.SimpleDoubleProperty;

/**
 * Created by Dmitriy on 28.10.2016.
 */
public class Size {
    private static double normalCellSize = 125.0;

    private static double normalUnitHeight = 120.0;
    private static double normalUnitWidth = 80.0;

    private static double sceneWidth = 0;
    private static double sceneHeight = 0;

    private static SimpleDoubleProperty cellSizePercentage = new SimpleDoubleProperty(0);

    private static SimpleDoubleProperty cellSize = new SimpleDoubleProperty(0);

    private static SimpleDoubleProperty unitHeight = new SimpleDoubleProperty(0);
    private static SimpleDoubleProperty unitWidth = new SimpleDoubleProperty(0);

    public static SimpleDoubleProperty unitHeightProperty() {
        return unitHeight;
    }

    public static SimpleDoubleProperty unitWidthProperty() {
        return unitWidth;
    }

    public static double getCellSizePercentage() {
        return cellSizePercentage.get();
    }

    public static SimpleDoubleProperty cellSizePercentageProperty() {
        return cellSizePercentage;
    }

    public static void setCellSizePercentage(double cellSizePercentage) {
        Size.cellSizePercentage.set(cellSizePercentage);
    }

    public static double getSceneWidth() {
        return sceneWidth;
    }

    public static void setSceneWidth(double sceneWidth) {
        Size.sceneWidth = sceneWidth;
    }

    public static double getSceneHeight() {
        return sceneHeight;
    }

    public static void setSceneHeight(double sceneHeight) {
        Size.sceneHeight = sceneHeight;
    }

    public static double getUnitHeight() {
        return unitHeight.get();
    }

    public static void setUnitHeight(double unitHeight) {
        Size.unitHeight.set(unitHeight);
    }

    public static double getUnitWidth() {
        return unitWidth.get();
    }

    public static void setUnitWidth(double unitWidth) {
        Size.unitWidth.set(unitWidth);
    }

    public static double getCellSize() {
        return cellSize.get();
    }

    public static void setCellSize(double cellSize) {
        Size.cellSize.set(cellSize);
        cellSizePercentage.set(normalCellSize/cellSize);
    }

    public static double getNormalCellSize() {
        return normalCellSize;
    }

    public static double getNormalUnitHeight() {
        return normalUnitHeight;
    }

    public static double getNormalUnitWidth() {
        return normalUnitWidth;
    }

    public static SimpleDoubleProperty cellSizeProperty() {
        return cellSize;
    }
}
