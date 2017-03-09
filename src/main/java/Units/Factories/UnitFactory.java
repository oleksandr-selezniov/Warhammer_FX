package Units.Factories;

import Units.Enums.UnitClassNames;
import Units.Gui_Unit;

/**
 * Created by Dmitriy on 19.02.2017.
 */
public interface UnitFactory {
    Gui_Unit createUnit(String name, int team);
    Gui_Unit copy(Gui_Unit guiUnit);

    static UnitFactory getUnitFactory(UnitClassNames name){
        UnitFactory factory = null;

        switch (name){
            case ARTILLERY:{
                factory = new ArtilleryFactory();
                break;
            }
            case VEHICLE:{
                factory = new VehicleFactory();
                break;
            }
            case LIGHT_INFANTRY:{
                factory = new LightInfantryFactory();
                break;
            }
            case MELEE_INFANTRY:{
                factory = new MeleeInfantryFactory();
                break;
            }
            case HEAVY_INFANTRY:{
                factory = new HeavyInfantryFactory();
                break;
            }
        }
        return factory ;
    }
}

