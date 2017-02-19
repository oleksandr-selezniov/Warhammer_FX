package Units.Factories;

import Units.MeleeInfantry;
import Units.Unit;

/**
 * Created by Dmitriy on 19.02.2017.
 */
public class MeleeInfantryFactory implements UnitFactory {
    @Override
    public Unit createUnit(String name, int team) {
        return new MeleeInfantry(name, team);
    }

    @Override
    public Unit copy(Unit unit) {
        MeleeInfantry meleeInfantry = new MeleeInfantry();
        return meleeInfantry.copy(((MeleeInfantry) unit));
    }
}