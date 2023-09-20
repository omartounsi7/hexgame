package com.omar.models.world;

import java.util.*;

public class World {
    private final Map<Tile, Set<Tile>> map;
    private final Tile[] tiles;
    public World(MapSize mapsize) {
        int size = determineSize(mapsize);
        this.map = new HashMap<>();
        this.tiles = new Tile[size * size];
        createTiles(size);
        linkNeighboringTiles(size);
    }
    public Tile getTile(int number) {
        return tiles[number];
    }
    public Set<Tile> getTileNeighbors(int number) {
        return map.get(tiles[number]);
    }
    public void addVertex(Tile t) {
        map.put(t, new HashSet<>());
    }
    public void addEdge(Tile a, Tile b) {
        map.get(a).add(b);
        map.get(b).add(a);
    }
    public void createTiles(int size){
        for(int i = 0 ; i < size * size ; i++){
            tiles[i] = new Tile(i);
            addVertex(tiles[i]);
        }
    }
    public void linkNeighboringTiles(int size){
        for(int i = 0 ; i < size * size ; i++){
            int south = i - size;
            int north = i + size;
            int west = i + 1;
            int east = i - 1;

            if(north < size * size){
                addEdge(getTile(i), getTile(north));
            }
            if(south >= 0){
                addEdge(getTile(i), getTile(south));
            }
            if(i % size != 0){
                addEdge(getTile(i), getTile(east));
            }
            if(i % size != size - 1){
                addEdge(getTile(i), getTile(west));
            }

            if (i % 2 == 0) {
                // Even case
                int northEast = i + size - 1;
                int northWest = i + size + 1;

                if (north < size * size) {
                    if (i % size != 0) {
                        addEdge(getTile(i), getTile(northEast));
                    }
                    if (i % size != size - 1) {
                        addEdge(getTile(i), getTile(northWest));
                    }
                }
            } else {
                // Odd case
                int southEast = i - size - 1;
                int southWest = i - size + 1;

                if (south >= 0) {
                    if (i % size != 0) {
                        addEdge(getTile(i), getTile(southEast));
                    }
                    if (i % size != size - 1) {
                        addEdge(getTile(i), getTile(southWest));
                    }
                }
            }
        }
    }
    public int determineSize(MapSize mapsize){
        return switch (mapsize) {
            case SMALL -> 4;
            case MEDIUM -> 6;
            case LARGE -> 8;
        };
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Tile v : map.keySet()) {
            builder.append(v.toString()).append(": ");
            for (Tile w : map.get(v)) {
                builder.append(w.toString()).append(" ");
            }
            builder.append("\n");
        }
        return (builder.toString());
    }
}
