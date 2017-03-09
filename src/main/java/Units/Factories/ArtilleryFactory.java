package Units.Factories;

import Units.Artillery;
import Units.Gui_Unit;

/**
 * Created by Dmitriy on 19.02.2017.
 */
public class ArtilleryFactory implements UnitFactory {
    @Override
    public Gui_Unit createUnit(String name, int team) {
        return new Artillery(name, team);
    }

    @Override
    public Gui_Unit copy(Gui_Unit guiUnit) {
        Artillery artillery = new Artillery();
        return artillery.copy(((Artillery) guiUnit));
    }
}
