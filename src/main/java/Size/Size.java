package Size;

/**
 * Created by Dmitriy on 28.10.2016.
 */
public class Size {
    private static double cellHeight = 125.0;
    private static double cellWidth = 125.0;
    private static double unitHeight = 120.0;
    private static double unitWidth = 80.0;
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

    public static void setUnitHeight(double unitHeight) {
        Size.unitHeight = unitHeight;
    }

    public static double getUnitWidth() {
        return unitWidth;
    }

    public static void setUnitWidth(double unitWidth) {
        Size.unitWidth = unitWidth;
    }

    public static double getCellHeight() {
        return cellHeight;
    }

    public static void setCellHeight(double cellHeight) {
        Size.cellHeight = cellHeight;
    }

    public static double getCellWidth() {
        return cellWidth;
    }

    public static void setCellWidth(double cellWidth) {
        Size.cellWidth = cellWidth;
    }


}
