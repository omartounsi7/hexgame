package com.omar.model;

public class Tile {
    private final int number;
    private TileStatus controllerFaction;
    private Army occupyingArmy;
    private boolean selected;
    public Tile(int number) {
        this.number = number;
        this.controllerFaction = TileStatus.EMPTY;
        this.selected = false;
    }
    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
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
    public int getNumber() {
        return number;
    }
    public TileStatus getControllerFaction() {
        return controllerFaction;
    }
    @Override
    public String toString() {
        return "{" + number + ", " + controllerFaction + "}";
    }
}
