package Board.Utils;

import Board.Board;
import Units.Gui_Unit;

import java.text.DecimalFormat;

/**
 * Created by Glazyrin.D on 11/7/2016.
 */
public class LoggerUtils {
    public static void writeCloseAttackLog(Gui_Unit attacker, Gui_Unit victim, int closeDamage){
        Board.writeToTextArea("#centerTextArea", Board.getTextFromTextArea("#centerTextArea") + "\n"
                + attacker.getName() + "\n"
                + "made " + (closeDamage) + "\n"
                + "melee damage to " + "\n"
                + victim.getName() + "\n", true);
    }

    public static void writeRangeAttackLog(Gui_Unit attacker, Gui_Unit victim, int rangeDamage){
        Board.writeToTextArea("#centerTextArea", Board.getTextFromTextArea("#centerTextArea") + "\n"
                + attacker.getName() + "\n"
                + "made " + (rangeDamage) + "\n"
                + "range damage to " + "\n"
                + victim.getName() + "\n", true);
    }

    public static void writeMissedLog(Gui_Unit guiUnit){
        Board.writeToTextArea("#centerTextArea", Board.getTextFromTextArea("#centerTextArea") + "\n"
                + guiUnit.getName() + "\n"
                + "missed!" + "\n", true);
    }

    public static void writeOutOfAmmoLog(Gui_Unit guiUnit){
        Board.writeToTextArea("#centerTextArea", Board.getTextFromTextArea("#centerTextArea") + "\n"
                + guiUnit.getName() + "\n"
                + "is out of ammunition!" + "\n", true);
    }

    public static void writeDeadLog(Gui_Unit hunter, Gui_Unit victim){
        Board.writeToTextArea("#centerTextArea", Board.getTextFromTextArea("#centerTextArea") + "\n"
                + victim.getName() + "\n"
                + "is killed by" + "\n"
                + hunter.getName()+"\n", true);
    }

    public static void writeWinLog(int winner){
        Board.writeToTextArea("#centerTextArea", Board.getTextFromTextArea("#centerTextArea") + "\nTEAM " + winner + " WINS!", true);
    }

    public static void writeTurnLog(int team){
        Board.writeToTextArea("#centerTextArea", Board.getTextFromTextArea("#centerTextArea") + "\nTEAM'S " + team + " TURN", true);
    }

    public static String getRAMConsumptionStatus() {
        double currentMemory = (((double) (Runtime.getRuntime().totalMemory() / 1024) / 1024)) - (((double) (Runtime.getRuntime().freeMemory() / 1024) / 1024));
        DecimalFormat myFormatter = new DecimalFormat("###.#");
        String output = myFormatter.format(currentMemory);
        String memoryInMB = "Used amount of RAM: " + output + " MB ";
        return memoryInMB;
    }

}
