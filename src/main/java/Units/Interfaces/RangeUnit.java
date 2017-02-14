package Units.Interfaces;

import Units.Unit;

/**
 * Created by Dmitriy on 22.01.2017.
 */
public interface RangeUnit {
    void performRangeAttack(Unit victim);
    int getRangeDamage(Unit victim);
    void performCloseAttack(Unit victim);
    double getCurrentRangeEfficiency(Unit victim);
    double getCurrentAccuracy(Unit victim);
    int getCloseDamage();
}
