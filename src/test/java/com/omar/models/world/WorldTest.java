package com.omar.models.world;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class WorldTest {
    private final Map<Integer, Set<Integer>> expectedSmallMap;

    public WorldTest() {
        this.expectedSmallMap = createSmallMap();
    }

    public Map<Integer, Set<Integer>> createSmallMap(){
        Map<Integer, Set<Integer>> smallMap = new HashMap<>();
        smallMap.put(0, new HashSet<>(Arrays.asList(1, 4, 5)));
        smallMap.put(1, new HashSet<>(Arrays.asList(0, 2, 5)));
        smallMap.put(2, new HashSet<>(Arrays.asList(1, 3, 5, 6, 7)));
        smallMap.put(3, new HashSet<>(Arrays.asList(2, 7)));
        smallMap.put(4, new HashSet<>(Arrays.asList(0, 5, 8, 9)));
        smallMap.put(5, new HashSet<>(Arrays.asList(0, 1, 2, 4, 6, 9)));
        smallMap.put(6, new HashSet<>(Arrays.asList(2, 5, 7, 9, 10, 11)));
        smallMap.put(7, new HashSet<>(Arrays.asList(2, 3, 6, 11)));
        smallMap.put(8, new HashSet<>(Arrays.asList(4, 9, 12, 13)));
        smallMap.put(9, new HashSet<>(Arrays.asList(4, 5, 6, 8, 10, 13)));
        smallMap.put(10, new HashSet<>(Arrays.asList(6, 9, 11, 13, 14, 15)));
        smallMap.put(11, new HashSet<>(Arrays.asList(6, 7, 10, 15)));
        smallMap.put(12, new HashSet<>(Arrays.asList(8, 13)));
        smallMap.put(13, new HashSet<>(Arrays.asList(8, 9, 10, 12, 14)));
        smallMap.put(14, new HashSet<>(Arrays.asList(10, 13, 15)));
        smallMap.put(15, new HashSet<>(Arrays.asList(10, 11, 14)));
        return smallMap;
    }

    @Test
    void testSmallMap() {
        World smallWorld = new World(MapSize.SMALL);
        assertEquals(expectedSmallMap, smallWorld.getAdjacencyMatrix());
    }
}