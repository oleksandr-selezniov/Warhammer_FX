package Weapons;

/**
 * Created by Dmitriy on 04.04.2015.
 */
public class CloseWeapon extends  Weapons {
    private int hits;
    private int energyPerHit;

    public CloseWeapon(){
        this.name="";
        this.damage=0;
        this.hits=0;
        this.energyPerHit =0;
        this.accuracy = 0;
    }

    public CloseWeapon(String name, int damage, int hits, int energyPerHit, double accuracy) {
        this.name = name;
        this.damage = damage;
        this.hits = hits;
        this.energyPerHit = energyPerHit;
        this.accuracy = accuracy;
    }

    public static CloseWeapon closeWeaponGenerator(TCloseWeapon name){
        CloseWeapon c = new CloseWeapon();
        switch(name){
            case CHAINSWORD:
                c = new CloseWeapon("CHAINSWORD",50,3,20,0.5);
                break;
            case KNIFE:
                c = new CloseWeapon("KNIFE",10,5,5,0.7);
                break;
            case HATCHET:
                c = new CloseWeapon("HATCHET",70,1,70,0.3);
                break;
            case CHAINHATCHET:
                c = new CloseWeapon("HATCHET",80,1,70,0.3);
                break;
            case DAGGER:
                c = new CloseWeapon("DAGGER",20,3,10,0.9);
                break;
            case POWERBLADE:
                c = new CloseWeapon("POWERBLADE",90,3,10,0.9);
                break;
            case POWERCLAWS:
                c = new CloseWeapon("POWERCLAWS",40,3,10,0.9);
                break;
            case POWERFIST:
                c = new CloseWeapon("POWERFIST",30,3,10,0.9);
                break;
            case NECRON_GUNBLADE:
                c = new CloseWeapon("NECRON_GUNBLADE",90,3,10,0.9);
                break;
            case KROOT_SPEAR:
                c = new CloseWeapon("KROOT_SPEAR",70,3,10,0.9);
                break;
            case GLIMMERSTEEL_SICKLE:
                c = new CloseWeapon("GLIMMERSTEEL_SICKLE",20,3,10,0.9);
                break;
            case WHYCH_KNIFE:
                c = new CloseWeapon("WHYCH_KNIFE",10,3,10,0.9);
                break;
            case BONESWORD:
                c = new CloseWeapon("BONESWORD",90,3,10,0.9);
                break;
            case SABRE:
                c = new CloseWeapon("SABRE",50,3,10,0.9);
                break;
            case SHOCKER:
                c = new CloseWeapon("SHOCKER",40,3,10,0.9);
                break;

        }
        return(c);
    }

    public int getHits() {
        return hits;
    }

    public int getEnergyPerHit() {
        return energyPerHit;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public void setEnergyPerHit(int energyPerHit) {
        this.energyPerHit = energyPerHit;
    }

}
