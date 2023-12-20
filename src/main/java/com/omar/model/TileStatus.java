package com.omar.model;

public enum TileStatus {
    EMPTY(0), P1OCCUPIED(1), P2OCCUPIED(2);
    private final int val;
    TileStatus(int val) {
        this.val = val;
    }
    public int getValue() {
        return val;
    }
}
