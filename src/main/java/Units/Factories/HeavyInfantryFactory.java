package Units.Factories;

import Units.HeavyInfantry;
import Units.Unit;

/**
 * Created by Dmitriy on 19.02.2017.
 */
public class HeavyInfantryFactory implements UnitFactory {
    @Override
    public Unit createUnit(String name, int team) {
        return new HeavyInfantry(name, team);
    }

    @Override
    public Unit copy(Unit unit) {
        HeavyInfantry heavyInfantry = new HeavyInfantry();
        return heavyInfantry.copy(((HeavyInfantry)unit));
    }
}
