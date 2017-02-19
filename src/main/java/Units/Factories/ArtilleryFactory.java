package Units.Factories;

import Units.Artillery;
import Units.Unit;

/**
 * Created by Dmitriy on 19.02.2017.
 */
public class ArtilleryFactory implements UnitFactory {
    @Override
    public Unit createUnit(String name, int team) {
        return new Artillery(name, team);
    }

    @Override
    public Unit copy(Unit unit) {
        Artillery artillery = new Artillery();
        return artillery.copy(((Artillery)unit));
    }
}
