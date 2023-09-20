package com.omar.models.world;

import java.util.*;

public class World {
    private final Map<Integer, Set<Integer>> adjacencyMatrix;
    private final Tile[] tiles;
    public World(MapSize mapsize) {
        int size = determineSize(mapsize);
        this.adjacencyMatrix = new HashMap<>();
        this.tiles = new Tile[size * size];
        createTiles(size);
        linkNeighboringTiles(size);
    }
    public Tile getTile(int number) {
        return tiles[number];
    }
    public Set<Integer> getTileNeighbors(int number) {
        return adjacencyMatrix.get(number);
    }
    public void addVertex(int v) {
        adjacencyMatrix.put(v, new HashSet<>());
    }
    public void addEdge(int a, int b) {
        adjacencyMatrix.get(a).add(b);
        adjacencyMatrix.get(b).add(a);
    }
    public void createTiles(int size){
        for(int i = 0 ; i < size * size ; i++){
            tiles[i] = new Tile(i);
            addVertex(i);
        }
    }
    public void linkNeighboringTiles(int size){
        for(int i = 0 ; i < size * size ; i++){
            int south = i - size;
            int north = i + size;
            int west = i + 1;
            int east = i - 1;

            if(north < size * size){
                addEdge(i, north);
            }
            if(south >= 0){
                addEdge(i, south);
            }
            if(i % size != 0){
                addEdge(i, east);
            }
            if(i % size != size - 1){
                addEdge(i, west);
            }

            if (i % 2 == 0) {
                // Even case
                int northEast = i + size - 1;
                int northWest = i + size + 1;

                if (north < size * size) {
                    if (i % size != 0) {
                        addEdge(i, northEast);
                    }
                    if (i % size != size - 1) {
                        addEdge(i, northWest);
                    }
                }
            } else {
                // Odd case
                int southEast = i - size - 1;
                int southWest = i - size + 1;

                if (south >= 0) {
                    if (i % size != 0) {
                        addEdge(i, southEast);
                    }
                    if (i % size != size - 1) {
                        addEdge(i, southWest);
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
        for (int v : adjacencyMatrix.keySet()) {
            builder.append(v).append(": ");
            for (int w : adjacencyMatrix.get(v)) {
                builder.append(w).append(" ");
            }
            builder.append("\n");
        }
        return (builder.toString());
    }
}
