package com.omar.model.faction;

public class Army {
    private int firepower;
    private int x;
    private int y;
    public Army(int firepower, int x, int y) {
        this.firepower = firepower;
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getFirepower() {
        return firepower;
    }
    public void setFirepower(int firepower) {
        this.firepower = firepower;
    }
    @Override
    public String toString() {
        return "Firepower = " + firepower + " | Position = (" + x + ", " + y + ")";
    }
}
