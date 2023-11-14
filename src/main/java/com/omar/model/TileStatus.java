package com.omar.model;

public enum TileStatus {
    P1OCCUPIED(0), P2OCCUPIED(1), EMPTY(2);
    private final int val;
    TileStatus(int val) {
        this.val = val;
    }
    public int getValue() {
        return val;
    }
}
