/**
 * Created by Dmitriy on 28.10.2016.
 */
public class Size {
    private static Double cellHeight = 125.0;
    private static Double cellWidth = 125.0;
    private static Double unitHeight = 120.0;
    private static Double unitWidth = 80.0;
    private static Double sceneWidth = 1366.0;
    private static Double sceneHeight = 768.0;
    private static Double maxSceneWidth = 1600.0;
    private static Double maxSceneHeight = 900.0;
    private static Double minSceneWidth = 800.0;
    private static Double minSceneHeight = 600.0;

    public static Double getSceneWidth() {
        return sceneWidth;
    }

    public static void setSceneWidth(Double sceneWidth) {
        Size.sceneWidth = sceneWidth;
    }

    public static Double getSceneHeight() {
        return sceneHeight;
    }

    public static void setSceneHeight(Double sceneHeight) {
        Size.sceneHeight = sceneHeight;
    }

    public static Double getUnitHeight() {
        return unitHeight;
    }

    public static void setUnitHeight(Double unitHeight) {
        Size.unitHeight = unitHeight;
    }

    public static Double getUnitWidth() {
        return unitWidth;
    }

    public static void setUnitWidth(Double unitWidth) {
        Size.unitWidth = unitWidth;
    }

    public static Double getCellHeight() {
        return cellHeight;
    }

    public static void setCellHeight(Double cellHeight) {
        Size.cellHeight = cellHeight;
    }

    public static Double getCellWidth() {
        return cellWidth;
    }

    public static void setCellWidth(Double cellWidth) {
        Size.cellWidth = cellWidth;
    }


}
