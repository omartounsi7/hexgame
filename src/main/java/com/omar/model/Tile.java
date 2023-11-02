package com.omar.model;

public class Tile {
    private final int x;
    private final int y;
    private TileStatus tileStatus;
    private Army occupyingArmy;
    private String city;
    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.tileStatus = TileStatus.EMPTY;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
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
