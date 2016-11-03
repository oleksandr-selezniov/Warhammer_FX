package Weapons;

/**
 * Created by sech_92 on 17.12.2014.
 */
 public class Weapons {
    protected String name;
    protected int damage;
    protected double accuracy;
    //private int damage_delta;

    public Weapons(){
        this.name="";
        this.damage=0;
        this.accuracy = 0;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public String getName() {
        return name;
    }


}

