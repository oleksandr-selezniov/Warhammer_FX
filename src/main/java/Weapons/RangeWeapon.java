package Weapons;

/**
 * Created by Dmitriy on 04.04.2015.
 */
public class RangeWeapon extends Weapons {
    private int ammo;
    private int shots;

    public RangeWeapon(){
        this.name="";
        this.damage=0;
        this.ammo=0;
        this.shots=0;
        this.accuracy = 0;
    }

    public RangeWeapon(String name, int damage, int shots, int ammo, double accuracy) {
        this.name = name;
        this.damage = damage;
        this.shots = shots;
        this.ammo = ammo;
        this.accuracy = accuracy;
    }

    public static RangeWeapon rangeWeaponGenerator(TRangeWeapon name) {
        RangeWeapon c = new RangeWeapon();
        switch (name) {
            case LASGUN:
                c = new RangeWeapon("Lasgun",10, 10, 50, 0.5);
                break;
            case LASPISTOL:
                c = new RangeWeapon("Laspistol",15, 3, 15, 0.9);
                break;
            case BOLTPISTOL:
                c = new RangeWeapon("Boltpistol",20, 3, 15, 0.8);
                break;
            case BOLTER:
                c = new RangeWeapon("Bolter",20, 10, 50, 0.4);
                break;
            case HEAVY_BOLTER:
                c = new RangeWeapon("Heavy_bolter",10, 30, 180, 0.2);
                break;
            case HELLGUN:
                c = new RangeWeapon("Hellgun",30, 30, 80, 0.7);
                break;
            case MELTAGUN:
                c = new RangeWeapon("Melta-gun",50, 5, 180, 0.2);
                break;
        }
        return (c);
    }

    public int getAmmo() {
        return ammo;
    }

    public int getShots() {
        return shots;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public void setShots(int shots) {
        this.shots = shots;
    }


}

