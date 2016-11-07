package Board;

import Units.Unit;

/**
 * Created by Glazyrin.D on 11/7/2016.
 */
public class LoggerUtils {
    public static void writeCloseAttackLog(Unit attacker, Unit victim, int closeDamage){
        Board.writeToTextArea("#centerTextArea", Board.getTextFromTextArea("#centerTextArea") + "\n"
                + attacker.getName() + "\n"
                + "made " + (closeDamage) + "\n"
                + "melee damage to " + "\n"
                + victim.getName() + "\n", true);
    }

    public static void writeRangeAttackLog(Unit attacker, Unit victim, int rangeDamage){
        Board.writeToTextArea("#centerTextArea", Board.getTextFromTextArea("#centerTextArea") + "\n"
                + attacker.getName() + "\n"
                + "made " + (rangeDamage) + "\n"
                + "range damage to " + "\n"
                + victim.getName() + "\n", true);
    }

    public static void writeMissedLog(Unit unit){
        Board.writeToTextArea("#centerTextArea", Board.getTextFromTextArea("#centerTextArea") + "\n"
                + unit.getName() + "\n"
                + "missed!" + "\n", true);
    }

    public static void writeOutOfAmmoLog(Unit unit){
        Board.writeToTextArea("#centerTextArea", Board.getTextFromTextArea("#centerTextArea") + "\n"
                + unit.getName() + "\n"
                + "is out of ammunition!" + "\n", true);
    }

    public static void writeDeadLog(Unit hunter, Unit victim){
        Board.writeToTextArea("#centerTextArea", Board.getTextFromTextArea("#centerTextArea") + "\n"
                + victim.getName() + "\n"
                + "is killed by" + "\n"
                + hunter.getName()+"\n", true);
    }

}
