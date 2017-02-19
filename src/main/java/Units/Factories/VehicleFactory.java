package Units.Factories;

import Units.Unit;
import Units.Vehicle;

/**
 * Created by Dmitriy on 19.02.2017.
 */
public class VehicleFactory implements UnitFactory {
    @Override
    public Unit createUnit(String name, int team) {
        return new Vehicle(name, team);
    }

    @Override
    public Unit copy(Unit unit) {
        Vehicle vehicle = new Vehicle();
        return vehicle.copy(((Vehicle)unit));
    }
}
