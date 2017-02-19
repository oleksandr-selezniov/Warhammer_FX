package Units.Factories;

import Units.LightInfantry;
import Units.Unit;

/**
 * Created by Dmitriy on 19.02.2017.
 */
public class LightInfantryFactory implements UnitFactory {
    @Override
    public Unit createUnit(String name, int team) {
        return new LightInfantry(name, team);
    }

    @Override
    public Unit copy(Unit unit) {
        LightInfantry lightInfantry = new LightInfantry();
        return lightInfantry.copy(((LightInfantry)unit));
    }
}
