package com.omar.model.world;

public enum MapSize {
    SMALL, MEDIUM, LARGE;

    public static int determineSize(MapSize mapsize){
        return switch (mapsize) {
            case SMALL -> 4;
            case MEDIUM -> 6;
            case LARGE -> 8;
        };
    }
}
