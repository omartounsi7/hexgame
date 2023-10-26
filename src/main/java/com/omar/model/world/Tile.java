package com.omar.model.world;

import com.omar.model.faction.Army;

public class Tile {
    private final int number;
    private TileStatus controller;
    private boolean selected;
    private Army occupyingArmy;
    public Tile(int number) {
        this.number = number;
        this.controller = TileStatus.EMPTY;
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
    public void setController(TileStatus controller) {
        this.controller = controller;
    }
    public int getNumber() {
        return number;
    }
    public TileStatus getController() {
        return controller;
    }
    @Override
    public String toString() {
        return "{" + number + ", " + controller + "}";
    }
}
