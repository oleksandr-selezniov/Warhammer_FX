package Units.Factories;

import Units.Gui_Unit;
import Units.Vehicle;

/**
 * Created by Dmitriy on 19.02.2017.
 */
public class VehicleFactory implements UnitFactory {
    @Override
    public Gui_Unit createUnit(String name, int team) {
        return new Vehicle(name, team);
    }

    @Override
    public Gui_Unit copy(Gui_Unit guiUnit) {
        Vehicle vehicle = new Vehicle();
        return vehicle.copy(((Vehicle) guiUnit));
    }
}
