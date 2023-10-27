package com.omar.model;

public class Tile {
    private final int x;
    private final int y;
    private TileStatus controllerFaction;
    private Army occupyingArmy;
    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.controllerFaction = TileStatus.EMPTY;
    }
    public Army getOccupyingArmy() {
        return occupyingArmy;
    }
    public void setOccupyingArmy(Army occupyingArmy) {
        this.occupyingArmy = occupyingArmy;
    }
    public void setControllerFaction(TileStatus controllerFaction) {
        this.controllerFaction = controllerFaction;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public TileStatus getControllerFaction() {
        return controllerFaction;
    }
    @Override
    public String toString() {
        return "{(" + x + ", " + y + ") " + controllerFaction + "}";
    }
}
