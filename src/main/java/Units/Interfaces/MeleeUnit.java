package Units.Interfaces;

import Units.Gui_Unit;

/**
 * Created by Dmitriy on 22.01.2017.
 */
public interface MeleeUnit {
    void performCloseAttack(Gui_Unit victim);
    void performRapidMeleeAttack(Gui_Unit victim);
    int  getCloseDamage(Gui_Unit victim);
}
