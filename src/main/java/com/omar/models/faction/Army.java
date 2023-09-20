package com.omar.models.faction;

public class Army {
    private int firepower;
    private int position;
    public Army(int firepower, int position) {
        this.firepower = firepower;
        this.position = position;
    }
    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }
    public int getFirepower() {
        return firepower;
    }
    public void setFirepower(int firepower) {
        this.firepower = firepower;
    }
    @Override
    public String toString() {
        return "[FP=" + firepower + ", POS=" + position + "]";
    }
}
