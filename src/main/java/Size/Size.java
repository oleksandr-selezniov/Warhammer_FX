package Size;

import javafx.beans.property.SimpleDoubleProperty;

/**
 * Created by Dmitriy on 28.10.2016.
 */
public class Size {

    private static final double unitHeight = 120.0;
    private static final double unitWidth = 80.0;
    private static final double cellSize = 125.0;
    private static double sceneWidth = 0;
    private static double sceneHeight = 0;

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
        return unitHeight;
    }

    public static double getUnitWidth() {
        return unitWidth;
    }

    public static double getCellSize() {
        return cellSize;
    }
}
