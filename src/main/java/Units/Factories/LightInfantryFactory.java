package Units.Factories;

import Units.LightInfantry;
import Units.Gui_Unit;

/**
 * Created by Dmitriy on 19.02.2017.
 */
public class LightInfantryFactory implements UnitFactory {
    @Override
    public Gui_Unit createUnit(String name, int team) {
        return new LightInfantry(name, team);
    }

    @Override
    public Gui_Unit copy(Gui_Unit guiUnit) {
        LightInfantry lightInfantry = new LightInfantry();
        return lightInfantry.copy(((LightInfantry) guiUnit));
    }
}
