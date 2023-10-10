package com.omar.models.world;

public class Tile {
    private final int number;
    private TileStatus status;
    public Tile(int number) {
        this.number = number;
        this.status = TileStatus.EMPTY;
    }
    public void setStatus(TileStatus status) {
        this.status = status;
    }
    public int getNumber() {
        return number;
    }
    public TileStatus getStatus() {
        return status;
    }
    @Override
    public String toString() {
        return "{" + number + "}";
    }
}
