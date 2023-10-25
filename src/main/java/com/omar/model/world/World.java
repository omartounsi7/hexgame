package com.omar.model.world;

import java.util.*;

public class World {
    private final Map<Integer, Set<Integer>> adjacencyMatrix;
    private final Tile[] tiles;
    public World(Tile[] tiles, Map<Integer, Set<Integer>> adjacencyMatrix, int size) {
        this.adjacencyMatrix = adjacencyMatrix;
        this.tiles = tiles;
        createTiles(size);
        linkNeighboringTiles(size);
    }
    public Map<Integer, Set<Integer>> getAdjacencyMatrix() {
        return adjacencyMatrix;
    }
    public Tile getTile(int number) {
        return tiles[number];
    }
    public Set<Integer> getTileNeighbors(int number) {
        return adjacencyMatrix.get(number);
    }
    private void addVertex(int v) {
        adjacencyMatrix.put(v, new HashSet<>());
    }
    private void addEdge(int a, int b) {
        adjacencyMatrix.get(a).add(b);
        adjacencyMatrix.get(b).add(a);
    }
    private void createTiles(int size){
        for(int i = 0 ; i < size * size ; i++){
            tiles[i] = new Tile(i);
            addVertex(i);
        }
        tiles[0].setController(TileStatus.P1OCCUPIED);
        tiles[size * size - 1].setController(TileStatus.P2OCCUPIED);
    }
    private void linkNeighboringTiles(int size){
        for(int i = 0 ; i < size * size ; i++){
            int north = i + size;
            int south = i - size;
            int east = i - 1;
            int west = i + 1;

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
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int v : adjacencyMatrix.keySet()) {
            builder.append("{").append(v).append("}").append(": { ");
            for (int w : adjacencyMatrix.get(v)) {
                builder.append(w).append(" ");
            }
            builder.append("}\n");
        }
        return (builder.toString());
    }
}
