package com.omar.model;

public class Army implements Cloneable {
    private int firepower;
    private final int ownerFaction;
    private int x;
    private int y;
    public Army(int firepower, int ownerFaction, int x, int y) {
        this.firepower = firepower;
        this.ownerFaction = ownerFaction;
        this.x = x;
        this.y = y;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
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
        return "FP = " + firepower + " (" + x + ", " + y + ")";
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
