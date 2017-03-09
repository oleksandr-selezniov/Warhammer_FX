package Units.Factories;

import Units.Gui_Unit;
import Units.HeavyInfantry;

/**
 * Created by Dmitriy on 19.02.2017.
 */
public class HeavyInfantryFactory implements UnitFactory {
    @Override
    public Gui_Unit createUnit(String name, int team) {
        return new HeavyInfantry(name, team);
    }

    @Override
    public Gui_Unit copy(Gui_Unit guiUnit) {
        HeavyInfantry heavyInfantry = new HeavyInfantry();
        return heavyInfantry.copy(((HeavyInfantry) guiUnit));
    }
}
