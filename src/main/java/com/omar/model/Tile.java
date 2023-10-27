package com.omar.model;

public class Tile {
    private final int x;
    private final int y;
    private TileStatus tileStatus;
    private Army occupyingArmy;
    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.tileStatus = TileStatus.EMPTY;
    }
    public Army getOccupyingArmy() {
        return occupyingArmy;
    }
    public void setOccupyingArmy(Army occupyingArmy) {
        this.occupyingArmy = occupyingArmy;
    }
    public void setTileStatus(TileStatus tileStatus) {
        this.tileStatus = tileStatus;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public TileStatus getTileStatus() {
        return tileStatus;
    }
    @Override
    public String toString() {
        return "{(" + x + ", " + y + ") " + tileStatus + "}";
    }
}
