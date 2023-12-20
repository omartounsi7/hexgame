package com.omar.model;

public class Army implements Cloneable {
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
        return "FP = " + firepower;
    }
    @Override
    public Army clone() {
        try {
            return (Army) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
}
