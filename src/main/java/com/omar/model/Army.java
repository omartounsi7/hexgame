package com.omar.model;

public class Army {
    private int firepower;
    private final int ownerFaction;
    public Army(int firepower, int ownerFaction) {
        this.firepower = firepower;
        this.ownerFaction = ownerFaction;
    }
    public int getOwnerFaction() {
        return ownerFaction;
    }
    public int getFirepower() {
        return firepower;
    }
    public void setFirepower(int firepower) {
        this.firepower = firepower;
    }
    @Override
    public String toString() {
        return "Firepower = " + firepower;
    }
}
