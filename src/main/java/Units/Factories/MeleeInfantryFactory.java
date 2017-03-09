package Units.Factories;

import Units.MeleeInfantry;
import Units.Gui_Unit;

/**
 * Created by Dmitriy on 19.02.2017.
 */
public class MeleeInfantryFactory implements UnitFactory {
    @Override
    public Gui_Unit createUnit(String name, int team) {
        return new MeleeInfantry(name, team);
    }

    @Override
    public Gui_Unit copy(Gui_Unit guiUnit) {
        MeleeInfantry meleeInfantry = new MeleeInfantry();
        return meleeInfantry.copy(((MeleeInfantry) guiUnit));
    }
}