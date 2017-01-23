package Units.Interfaces;

import Units.Unit;

/**
 * Created by Dmitriy on 22.01.2017.
 */
public interface MeleeUnit {
    void performCloseAttack(Unit victim);
    int  getCloseDamage(Unit victim);
}
