package com.omar.models.faction;

public class Army {
    private int firepower;
    public Army(int firepower) {
        this.firepower = firepower;
    }
    public int getFirepower() {
        return firepower;
    }
    public void setFirepower(int firepower) {
        this.firepower = firepower;
    }
    @Override
    public String toString() {
        return "{" + firepower + "}";
    }
}
