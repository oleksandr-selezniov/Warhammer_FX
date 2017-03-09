package Units.Interfaces;

import Units.Gui_Unit;

/**
 * Created by Dmitriy on 22.01.2017.
 */
public interface RangeUnit {
    void performRangeAttack(Gui_Unit victim);
    int getRangeDamage(Gui_Unit victim);
    void performCloseAttack(Gui_Unit victim);
    double getCurrentRangeEfficiency(Gui_Unit victim);
    double getCurrentAccuracy(Gui_Unit victim);
    int getCloseDamage();
}
