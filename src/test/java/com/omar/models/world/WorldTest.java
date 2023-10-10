package com.omar.models.world;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class WorldTest {
    private final Map<Integer, Set<Integer>> expectedSmallMap;
    private final Map<Integer, Set<Integer>> expectedMediumMap;
    private final Map<Integer, Set<Integer>> expectedLargeMap;
    private final int smallSize;
    private final int mediumSize;
    private final int largeSize;
    private final Tile[] smallTiles;
    private final Tile[] mediumTiles;
    private final Tile[] largeTiles;
    private final Map<Integer, Set<Integer>> smallAdjacencyMatrix;
    private final Map<Integer, Set<Integer>> mediumAdjacencyMatrix;
    private final Map<Integer, Set<Integer>> largeAdjacencyMatrix;

    public WorldTest() {
        this.smallSize = MapSize.determineSize(MapSize.SMALL);
        this.mediumSize = MapSize.determineSize(MapSize.MEDIUM);
        this.largeSize = MapSize.determineSize(MapSize.LARGE);

        this.smallTiles = new Tile[smallSize * smallSize];
        this.mediumTiles = new Tile[mediumSize * mediumSize];
        this.largeTiles = new Tile[largeSize * largeSize];

        this.smallAdjacencyMatrix = new HashMap<>();
        this.mediumAdjacencyMatrix = new HashMap<>();
        this.largeAdjacencyMatrix = new HashMap<>();

        this.expectedSmallMap = createSmallMap();
        this.expectedMediumMap = createMediumMap();
        this.expectedLargeMap = createLargeMap();
    }
    private Map<Integer, Set<Integer>> createSmallMap(){
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
    private Map<Integer, Set<Integer>> createMediumMap(){
        Map<Integer, Set<Integer>> mediumMap = new HashMap<>();
        mediumMap.put(0, new HashSet<>(Arrays.asList(1, 6, 7)));
        mediumMap.put(1, new HashSet<>(Arrays.asList(0, 2, 7)));
        mediumMap.put(2, new HashSet<>(Arrays.asList(1, 3, 7, 8, 9)));
        mediumMap.put(3, new HashSet<>(Arrays.asList(2, 4, 9)));
        mediumMap.put(4, new HashSet<>(Arrays.asList(3, 5, 9, 10, 11)));
        mediumMap.put(5, new HashSet<>(Arrays.asList(4, 11)));
        mediumMap.put(6, new HashSet<>(Arrays.asList(0, 7, 12, 13)));
        mediumMap.put(7, new HashSet<>(Arrays.asList(0, 1, 2, 6, 8, 13)));
        mediumMap.put(8, new HashSet<>(Arrays.asList(2, 7, 9, 13, 14, 15)));
        mediumMap.put(9, new HashSet<>(Arrays.asList(2, 3, 4, 8, 10, 15)));
        mediumMap.put(10, new HashSet<>(Arrays.asList(16, 17, 4, 9, 11, 15)));
        mediumMap.put(11, new HashSet<>(Arrays.asList(17, 4, 5, 10)));
        mediumMap.put(12, new HashSet<>(Arrays.asList(18, 19, 6, 13)));
        mediumMap.put(13, new HashSet<>(Arrays.asList(19, 6, 7, 8, 12, 14)));
        mediumMap.put(14, new HashSet<>(Arrays.asList(19, 20, 21, 8, 13, 15)));
        mediumMap.put(15, new HashSet<>(Arrays.asList(16, 21, 8, 9, 10, 14)));
        mediumMap.put(16, new HashSet<>(Arrays.asList(17, 21, 22, 23, 10, 15)));
        mediumMap.put(17, new HashSet<>(Arrays.asList(16, 23, 10, 11)));
        mediumMap.put(18, new HashSet<>(Arrays.asList(19, 24, 25, 12)));
        mediumMap.put(19, new HashSet<>(Arrays.asList(18, 20, 25, 12, 13, 14)));
        mediumMap.put(20, new HashSet<>(Arrays.asList(19, 21, 25, 26, 27, 14)));
        mediumMap.put(21, new HashSet<>(Arrays.asList(16, 20, 22, 27, 14, 15)));
        mediumMap.put(22, new HashSet<>(Arrays.asList(16, 21, 23, 27, 28, 29)));
        mediumMap.put(23, new HashSet<>(Arrays.asList(16, 17, 22, 29)));
        mediumMap.put(24, new HashSet<>(Arrays.asList(18, 25, 30, 31)));
        mediumMap.put(25, new HashSet<>(Arrays.asList(18, 19, 20, 24, 26, 31)));
        mediumMap.put(26, new HashSet<>(Arrays.asList(32, 33, 20, 25, 27, 31)));
        mediumMap.put(27, new HashSet<>(Arrays.asList(33, 20, 21, 22, 26, 28)));
        mediumMap.put(28, new HashSet<>(Arrays.asList(33, 34, 35, 22, 27, 29)));
        mediumMap.put(29, new HashSet<>(Arrays.asList(35, 22, 23, 28)));
        mediumMap.put(30, new HashSet<>(Arrays.asList(24, 31)));
        mediumMap.put(31, new HashSet<>(Arrays.asList(32, 24, 25, 26, 30)));
        mediumMap.put(32, new HashSet<>(Arrays.asList(33, 26, 31)));
        mediumMap.put(33, new HashSet<>(Arrays.asList(32, 34, 26, 27, 28)));
        mediumMap.put(34, new HashSet<>(Arrays.asList(33, 35, 28)));
        mediumMap.put(35, new HashSet<>(Arrays.asList(34, 28, 29)));
        return mediumMap;
    }
    private Map<Integer, Set<Integer>> createLargeMap(){
        Map<Integer, Set<Integer>> largeMap = new HashMap<>();
        largeMap.put(0, new HashSet<>(Arrays.asList(1, 8, 9)));
        largeMap.put(1, new HashSet<>(Arrays.asList(0, 2, 9)));
        largeMap.put(2, new HashSet<>(Arrays.asList(1, 3, 9, 10, 11)));
        largeMap.put(3, new HashSet<>(Arrays.asList(2, 4, 11)));
        largeMap.put(4, new HashSet<>(Arrays.asList(3, 5, 11, 12, 13)));
        largeMap.put(5, new HashSet<>(Arrays.asList(4, 6, 13)));
        largeMap.put(6, new HashSet<>(Arrays.asList(5, 7, 13, 14, 15)));
        largeMap.put(7, new HashSet<>(Arrays.asList(6, 15)));
        largeMap.put(8, new HashSet<>(Arrays.asList(0, 16, 17, 9)));
        largeMap.put(9, new HashSet<>(Arrays.asList(0, 1, 17, 2, 8, 10)));
        largeMap.put(10, new HashSet<>(Arrays.asList(17, 2, 18, 19, 9, 11)));
        largeMap.put(11, new HashSet<>(Arrays.asList(2, 3, 19, 4, 10, 12)));
        largeMap.put(12, new HashSet<>(Arrays.asList(19, 4, 20, 21, 11, 13)));
        largeMap.put(13, new HashSet<>(Arrays.asList(4, 5, 21, 6, 12, 14)));
        largeMap.put(14, new HashSet<>(Arrays.asList(21, 6, 22, 23, 13, 15)));
        largeMap.put(15, new HashSet<>(Arrays.asList(6, 7, 23, 14)));
        largeMap.put(16, new HashSet<>(Arrays.asList(17, 8, 24, 25)));
        largeMap.put(17, new HashSet<>(Arrays.asList(16, 18, 8, 9, 25, 10)));
        largeMap.put(18, new HashSet<>(Arrays.asList(17, 19, 25, 10, 26, 27)));
        largeMap.put(19, new HashSet<>(Arrays.asList(18, 20, 10, 11, 27, 12)));
        largeMap.put(20, new HashSet<>(Arrays.asList(19, 21, 27, 12, 28, 29)));
        largeMap.put(21, new HashSet<>(Arrays.asList(20, 22, 12, 13, 29, 14)));
        largeMap.put(22, new HashSet<>(Arrays.asList(21, 23, 29, 14, 30, 31)));
        largeMap.put(23, new HashSet<>(Arrays.asList(22, 14, 15, 31)));
        largeMap.put(24, new HashSet<>(Arrays.asList(16, 32, 33, 25)));
        largeMap.put(25, new HashSet<>(Arrays.asList(16, 17, 33, 18, 24, 26)));
        largeMap.put(26, new HashSet<>(Arrays.asList(33, 18, 34, 35, 25, 27)));
        largeMap.put(27, new HashSet<>(Arrays.asList(18, 19, 35, 20, 26, 28)));
        largeMap.put(28, new HashSet<>(Arrays.asList(35, 20, 36, 37, 27, 29)));
        largeMap.put(29, new HashSet<>(Arrays.asList(20, 21, 37, 22, 28, 30)));
        largeMap.put(30, new HashSet<>(Arrays.asList(37, 22, 38, 39, 29, 31)));
        largeMap.put(31, new HashSet<>(Arrays.asList(22, 23, 39, 30)));
        largeMap.put(32, new HashSet<>(Arrays.asList(33, 24, 40, 41)));
        largeMap.put(33, new HashSet<>(Arrays.asList(32, 34, 24, 25, 41, 26)));
        largeMap.put(34, new HashSet<>(Arrays.asList(33, 35, 41, 26, 42, 43)));
        largeMap.put(35, new HashSet<>(Arrays.asList(34, 36, 26, 27, 43, 28)));
        largeMap.put(36, new HashSet<>(Arrays.asList(35, 37, 43, 28, 44, 45)));
        largeMap.put(37, new HashSet<>(Arrays.asList(36, 38, 28, 29, 45, 30)));
        largeMap.put(38, new HashSet<>(Arrays.asList(37, 39, 45, 30, 46, 47)));
        largeMap.put(39, new HashSet<>(Arrays.asList(38, 30, 31, 47)));
        largeMap.put(40, new HashSet<>(Arrays.asList(32, 48, 49, 41)));
        largeMap.put(41, new HashSet<>(Arrays.asList(32, 33, 49, 34, 40, 42)));
        largeMap.put(42, new HashSet<>(Arrays.asList(49, 34, 50, 51, 41, 43)));
        largeMap.put(43, new HashSet<>(Arrays.asList(34, 35, 51, 36, 42, 44)));
        largeMap.put(44, new HashSet<>(Arrays.asList(51, 36, 52, 53, 43, 45)));
        largeMap.put(45, new HashSet<>(Arrays.asList(36, 37, 53, 38, 44, 46)));
        largeMap.put(46, new HashSet<>(Arrays.asList(53, 38, 54, 55, 45, 47)));
        largeMap.put(47, new HashSet<>(Arrays.asList(38, 39, 55, 46)));
        largeMap.put(48, new HashSet<>(Arrays.asList(49, 40, 56, 57)));
        largeMap.put(49, new HashSet<>(Arrays.asList(48, 50, 40, 41, 57, 42)));
        largeMap.put(50, new HashSet<>(Arrays.asList(49, 51, 57, 42, 58, 59)));
        largeMap.put(51, new HashSet<>(Arrays.asList(50, 52, 42, 43, 59, 44)));
        largeMap.put(52, new HashSet<>(Arrays.asList(51, 53, 59, 44, 60, 61)));
        largeMap.put(53, new HashSet<>(Arrays.asList(52, 54, 44, 45, 61, 46)));
        largeMap.put(54, new HashSet<>(Arrays.asList(53, 55, 61, 46, 62, 63)));
        largeMap.put(55, new HashSet<>(Arrays.asList(54, 46, 47, 63)));
        largeMap.put(56, new HashSet<>(Arrays.asList(48, 57)));
        largeMap.put(57, new HashSet<>(Arrays.asList(48, 49, 50, 56, 58)));
        largeMap.put(58, new HashSet<>(Arrays.asList(50, 57, 59)));
        largeMap.put(59, new HashSet<>(Arrays.asList(50, 51, 52, 58, 60)));
        largeMap.put(60, new HashSet<>(Arrays.asList(52, 59, 61)));
        largeMap.put(61, new HashSet<>(Arrays.asList(52, 53, 54, 60, 62)));
        largeMap.put(62, new HashSet<>(Arrays.asList(54, 61, 63)));
        largeMap.put(63, new HashSet<>(Arrays.asList(54, 55, 62)));
        return largeMap;
    }
    @Test
    void testSmallMap() {
        World smallWorld = new World(smallTiles, smallAdjacencyMatrix, smallSize);
        assertEquals(expectedSmallMap, smallWorld.getAdjacencyMatrix());
    }
    @Test
    void testMediumMap() {
        World mediumWorld = new World(mediumTiles, mediumAdjacencyMatrix, mediumSize);
        assertEquals(expectedMediumMap, mediumWorld.getAdjacencyMatrix());
    }
    @Test
    void testLargeMap() {
        World largeWorld = new World(largeTiles, largeAdjacencyMatrix, largeSize);
        assertEquals(expectedLargeMap, largeWorld.getAdjacencyMatrix());
    }
}
