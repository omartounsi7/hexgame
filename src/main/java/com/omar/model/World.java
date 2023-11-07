package com.omar.model;

import com.omar.hex.HexMech;

import java.util.*;

import static com.omar.hex.HexConst.*;

public class World {
    public Board board;
    private final List<Army> redArmies;
    private final List<Army> greenArmies;
    private final Set<Army> movedArmies = new HashSet<>();
    public static GameStatus status = GameStatus.ACTIVE;
    public static TurnStatus whosturn = TurnStatus.P1TURN;
    public static int numberOfMoves = 1;
    public Tile selectedTile;
    private List<int[]> adjacentTiles;
    public World(List<Army> redArmies, List<Army> greenArmies){
        this.redArmies = redArmies;
        this.greenArmies = greenArmies;
        this.board = createMap();
    }
    public Tile getTile(int x, int y) {
        return board.getBoard()[x][y];
    }
    private Board createMap(){
        HexMech.setXYasVertex(false);
        HexMech.setHeight(TILESIZE);
        HexMech.setBorders(BORDERS);
        Tile[][] tiles = new Tile[MAPSIZE][MAPSIZE];
        Random random = new Random();

        for (int x = 0; x < MAPSIZE; x++) {
            for (int y = 0; y < MAPSIZE; y++) {
                tiles[x][y] = new Tile(x, y);
                boolean isCapital = (x == 0 && y == 0) || (x == MAPSIZE - 1 && y == MAPSIZE - 1);
                if (random.nextDouble() <= 0.2 || isCapital) {
                    int randomCityIndex = random.nextInt(CityNames.cityNames.length);
                    String cityName = CityNames.cityNames[randomCityIndex];
                    tiles[x][y].setCity(cityName);
                }
            }
        }
        Army firstRedArmy = new Army(10, 0, 0, 0);
        redArmies.add(firstRedArmy);
        tiles[0][0].setOccupyingArmy(firstRedArmy);
        Army firstGreenArmy = new Army(10, 1, MAPSIZE - 1, MAPSIZE - 1);
        greenArmies.add(firstGreenArmy);
        tiles[MAPSIZE - 1][MAPSIZE - 1].setOccupyingArmy(firstGreenArmy);
        tiles[0][0].setTileStatus(TileStatus.P1OCCUPIED);
        tiles[MAPSIZE - 1][MAPSIZE - 1].setTileStatus(TileStatus.P2OCCUPIED);

        return new Board(tiles);
    }
    public void selectArmy(Tile startTile){
        if(startTile.getOccupyingArmy() != null){ // check if clicked tile contains an army
            if(whosturn == TurnStatus.P1TURN){ // check whose turn it is
                if(startTile.getTileStatus() == TileStatus.P1OCCUPIED){ // check if tile belongs to P1
                    if(!movedArmies.contains(startTile.getOccupyingArmy())){
                        selectedTile = startTile;
                        adjacentTiles = getAdjacent(startTile.getX(), startTile.getY(), board.getBoard());
                        for (int[] adjacentTile : adjacentTiles) {
                            getTile(adjacentTile[0], adjacentTile[1]).setAdjacent(true);
                        }
                    }

                }
            } else if(whosturn == TurnStatus.P2TURN){
                if(startTile.getTileStatus() == TileStatus.P2OCCUPIED){ // check if tile belongs to P2
                    if(!movedArmies.contains(startTile.getOccupyingArmy())){
                        selectedTile = startTile;
                        adjacentTiles = getAdjacent(startTile.getX(), startTile.getY(), board.getBoard());
                        for (int[] adjacentTile : adjacentTiles) {
                            getTile(adjacentTile[0], adjacentTile[1]).setAdjacent(true);
                        }
                    }
                }
            }
        }
    }
    public void moveArmy(int x, int y, int endX, int endY) {
        Tile[][] state = board.getBoard();
        Tile startTile = state[x][y];
        Tile endTile = state[endX][endY];

        if(areAdjacent(endX, endY, x, y)){
            Army offArmy = startTile.getOccupyingArmy();
            Army defArmy = endTile.getOccupyingArmy();

            if(defArmy == null){ // movement
                startTile.setOccupyingArmy(null);
                endTile.setOccupyingArmy(offArmy);
                offArmy.setX(endX);
                offArmy.setY(endY);
                if(whosturn == TurnStatus.P1TURN){
                    endTile.setTileStatus(TileStatus.P1OCCUPIED);
                } else if(whosturn == TurnStatus.P2TURN){
                    endTile.setTileStatus(TileStatus.P2OCCUPIED);
                }
                movedArmies.add(offArmy);
            } else if (defArmy.getOwnerFaction() == offArmy.getOwnerFaction()) { // reinforce
                int currFp = defArmy.getFirepower();
                int inFp = offArmy.getFirepower();
                if(currFp + inFp >= 100){
                    defArmy.setFirepower(99);
                    offArmy.setFirepower(currFp + inFp - 99);
                    movedArmies.add(defArmy);
                } else {
                    defArmy.setFirepower(currFp + inFp);
                    startTile.setOccupyingArmy(null);
                    movedArmies.add(defArmy);
                    if(startTile.getTileStatus() == TileStatus.P2OCCUPIED){
                        if(greenArmies.size() == 3){
                            numberOfMoves--;
                        }
                        greenArmies.remove(offArmy);
                    } else if(startTile.getTileStatus() == TileStatus.P1OCCUPIED){
                        if(redArmies.size() == 3){
                            numberOfMoves--;
                        }
                        redArmies.remove(offArmy);
                    }
                }
            } else { // combat
                startTile.setOccupyingArmy(null);
                if(defArmy.getFirepower() >= offArmy.getFirepower()){
                    defArmy.setFirepower(defArmy.getFirepower() - offArmy.getFirepower() + 1);
                    if(startTile.getTileStatus() == TileStatus.P2OCCUPIED){
                        greenArmies.remove(offArmy);
                    }  else if(startTile.getTileStatus() == TileStatus.P1OCCUPIED){
                        redArmies.remove(offArmy);
                    }
                } else {
                    if(endTile.getTileStatus() == TileStatus.P2OCCUPIED){
                        greenArmies.remove(defArmy);
                    }  else if(endTile.getTileStatus() == TileStatus.P1OCCUPIED){
                        redArmies.remove(defArmy);
                    }
                    offArmy.setFirepower(offArmy.getFirepower() - defArmy.getFirepower());
                    endTile.setOccupyingArmy(offArmy);
                    offArmy.setX(endX);
                    offArmy.setY(endY);
                    if(whosturn == TurnStatus.P1TURN){
                        endTile.setTileStatus(TileStatus.P1OCCUPIED);
                    } else if(whosturn == TurnStatus.P2TURN){
                        endTile.setTileStatus(TileStatus.P2OCCUPIED);
                    }
                    movedArmies.add(offArmy);
                }
            }

        } else {
            System.out.println("Incorrect destination!");
        }

        selectedTile = null;
        for (int[] adjacentTile : adjacentTiles) {
            state[adjacentTile[0]][adjacentTile[1]].setAdjacent(false);
        }
        adjacentTiles.clear();
    }
    public void checkVictory(){
        if(getTile(0,0).getTileStatus() == TileStatus.P2OCCUPIED){
            status = GameStatus.P2WINS;
        }
        else if(getTile(MAPSIZE - 1,MAPSIZE - 1).getTileStatus() == TileStatus.P1OCCUPIED){
            status = GameStatus.P1WINS;
        }
    }
    public boolean areAdjacent(int endX, int endY, int x, int y){
        if (endX == x) {
            if(endY == y - 1 || endY == y + 1){
                return true;
            }
            if(endY == y - 2){
                return getTile(x,y - 1).getOccupyingArmy() == null;
            }
            if(endY == y + 2){
                return getTile(x,y + 1).getOccupyingArmy() == null;
            }
        }

        if(x % 2 == 1){ // odd column
            if(endX == x - 1){
                if(endY == y || endY == y + 1){
                    return true;
                }
                if(endY == y - 1){
                    return getTile(x - 1,y).getOccupyingArmy() == null || getTile(x,y - 1).getOccupyingArmy() == null;
                }
                if(endY == y + 2){
                    return getTile(x - 1,y + 1).getOccupyingArmy() == null || getTile(x,y + 1).getOccupyingArmy() == null;
                }
            }
            if(endX == x + 1){
                if(endY == y || endY == y + 1){
                    return true;
                }
                if(endY == y - 1){
                    return getTile(x,y - 1).getOccupyingArmy() == null || getTile(x + 1,y).getOccupyingArmy() == null;
                }
                if(endY == y + 2){
                    return getTile(x,y + 1).getOccupyingArmy() == null || getTile(x + 1,y + 1).getOccupyingArmy() == null;
                }
            }
            if(endX == x - 2) {
                if(endY == y){
                    return getTile(x - 1,y).getOccupyingArmy() == null || (y + 1 < MAPSIZE && getTile(x - 1,y + 1).getOccupyingArmy() == null); // HERE!
                }
                if(endY == y - 1){
                    return getTile(x - 1,y).getOccupyingArmy() == null;
                }
                if(endY == y + 1){
                    return getTile(x - 1,y + 1).getOccupyingArmy() == null;
                }
            }
            if(endX == x + 2) {
                if(endY == y){
                    return getTile(x + 1,y).getOccupyingArmy() == null || (y + 1 < MAPSIZE && getTile(x + 1,y + 1).getOccupyingArmy() == null); // HERE!
                }
                if(endY == y - 1){
                    return getTile(x + 1,y).getOccupyingArmy() == null;
                }
                if(endY == y + 1){
                    return getTile(x + 1,y + 1).getOccupyingArmy() == null;
                }
            }

        } else { // even column
            if(endX == x - 1){
                if(endY == y || endY == y - 1){
                    return true;
                }
                if(endY == y - 2){
                    return getTile(x - 1,y - 1).getOccupyingArmy() == null || getTile(x,y - 1).getOccupyingArmy() == null;
                }
                if(endY == y + 1){
                    return getTile(x - 1,y).getOccupyingArmy() == null || getTile(x,y + 1).getOccupyingArmy() == null;
                }
            }
            if(endX == x + 1){
                if(endY == y || endY == y - 1){
                    return true;
                }
                if(endY == y - 2){
                    return getTile(x + 1,y - 1).getOccupyingArmy() == null || getTile(x,y - 1).getOccupyingArmy() == null;
                }
                if(endY == y + 1){
                    return getTile(x + 1,y).getOccupyingArmy() == null || getTile(x,y + 1).getOccupyingArmy() == null;
                }
            }
            if(endX == x - 2) {
                if(endY == y){
                    return getTile(x - 1,y).getOccupyingArmy() == null || (y - 1 >= 0 && getTile(x - 1,y - 1).getOccupyingArmy() == null); // HERE!
                }
                if(endY == y - 1){
                    return getTile(x - 1,y - 1).getOccupyingArmy() == null;
                }
                if(endY == y + 1){
                    return getTile(x - 1,y).getOccupyingArmy() == null;
                }
            }
            if(endX == x + 2) {
                if(endY == y){
                    return getTile(x + 1,y).getOccupyingArmy() == null || (y - 1 >= 0 && getTile(x + 1,y - 1).getOccupyingArmy() == null); // HERE!
                }
                if(endY == y - 1){
                    return getTile(x + 1,y - 1).getOccupyingArmy() == null;
                }
                if(endY == y + 1){
                    return getTile(x + 1,y).getOccupyingArmy() == null;
                }
            }
        }
        return false;
    }
    public List<int[]> getAdjacent(int x, int y, Tile[][] state){
        List<int[]> adj = new ArrayList<>();

//        adj.add(new int[]{x,y}); // SELF

        if(y - 1 >= 0){
            adj.add(new int[]{x,y - 1});
        }
        if(y + 1 < MAPSIZE){
            adj.add(new int[]{x,y + 1});
        }
        if(y - 2 >= 0 && state[x][y - 1].getOccupyingArmy() == null){
            adj.add(new int[]{x,y - 2});
        }
        if(y + 2 < MAPSIZE && state[x][y + 1].getOccupyingArmy() == null){
            adj.add(new int[]{x,y + 2});
        }

        if(x % 2 == 1){ // odd column
            if(x - 1 >= 0){
                adj.add(new int[]{x - 1,y});
                if(y + 1 < MAPSIZE){
                    adj.add(new int[]{x - 1,y + 1});
                }
                if(y - 1 >= 0 && (state[x - 1][y].getOccupyingArmy() == null || state[x][y - 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x - 1,y - 1});
                }
                if(y + 2 < MAPSIZE && (state[x - 1][y + 1].getOccupyingArmy() == null || state[x][y + 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x - 1,y + 2});
                }
            }
            if(x + 1 < MAPSIZE){
                adj.add(new int[]{x + 1,y});
                if(y + 1 < MAPSIZE){
                    adj.add(new int[]{x + 1,y + 1});
                }
                if(y - 1 >= 0 && (state[x][y - 1].getOccupyingArmy() == null || state[x + 1][y].getOccupyingArmy() == null)){
                    adj.add(new int[]{x + 1,y - 1});
                }
                if(y + 2 < MAPSIZE && (state[x][y + 1].getOccupyingArmy() == null || state[x + 1][y + 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x + 1,y + 2});
                }
            }
            if(x - 2 >= 0) {
                if(state[x - 1][y].getOccupyingArmy() == null || (y + 1 < MAPSIZE && state[x - 1][y + 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x - 2,y});
                }
                if(y - 1 >= 0 && state[x - 1][y].getOccupyingArmy() == null){
                    adj.add(new int[]{x - 2,y - 1});
                }
                if(y + 1 < MAPSIZE && state[x - 1][y + 1].getOccupyingArmy() == null){
                    adj.add(new int[]{x - 2,y + 1});
                }
            }
            if(x + 2 < MAPSIZE) {
                if(state[x + 1][y].getOccupyingArmy() == null || (y + 1 < MAPSIZE && state[x + 1][y + 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x + 2,y});
                }
                if(y - 1 >= 0 && state[x + 1][y].getOccupyingArmy() == null){
                    adj.add(new int[]{x + 2,y - 1});
                }
                if(y + 1 < MAPSIZE && state[x + 1][y + 1].getOccupyingArmy() == null){
                    adj.add(new int[]{x + 2,y + 1});
                }
            }
        } else { // even column
            if(x - 1 >= 0){
                adj.add(new int[]{x - 1,y});
                if(y - 1 >= 0){
                    adj.add(new int[]{x - 1,y - 1});
                }
                if(y - 2 >= 0 && (state[x - 1][y - 1].getOccupyingArmy() == null || state[x][y - 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x - 1,y - 2});
                }
                if(y + 1 < MAPSIZE && (state[x - 1][y].getOccupyingArmy() == null || state[x][y + 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x - 1,y + 1});
                }
            }
            if(x + 1 < MAPSIZE){
                adj.add(new int[]{x + 1,y});
                if(y - 1 >= 0){
                    adj.add(new int[]{x + 1,y - 1});
                }
                if(y - 2 >= 0 && (state[x + 1][y - 1].getOccupyingArmy() == null || state[x][y - 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x + 1,y - 2});
                }
                if(y + 1 < MAPSIZE && (state[x + 1][y].getOccupyingArmy() == null || state[x][y + 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x + 1,y + 1});
                }
            }
            if(x - 2 >= 0) {
                if(state[x - 1][y].getOccupyingArmy() == null || (y - 1 >= 0 && state[x - 1][y - 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x - 2,y});
                }
                if(y - 1 >= 0 && state[x - 1][y - 1].getOccupyingArmy() == null){
                    adj.add(new int[]{x - 2,y - 1});
                }
                if(y + 1 < MAPSIZE && state[x - 1][y].getOccupyingArmy() == null){
                    adj.add(new int[]{x - 2,y + 1});
                }
            }
            if(x + 2 < MAPSIZE) {
                if(state[x + 1][y].getOccupyingArmy() == null || (y - 1 >= 0 && state[x + 1][y - 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x + 2,y});
                }
                if(y - 1 >= 0 && state[x + 1][y - 1].getOccupyingArmy() == null){
                    adj.add(new int[]{x + 2,y - 1});
                }
                if(y + 1 < MAPSIZE && state[x + 1][y].getOccupyingArmy() == null){
                    adj.add(new int[]{x + 2,y + 1});
                }
            }
        }
        return adj;
    }
    public void updateTurn(){
        if(numberOfMoves == 0){
            numberOfMoves = 3;
            if(whosturn == TurnStatus.P1TURN){
                if(greenArmies.size() < 3){
                    numberOfMoves = greenArmies.size();
                }
                updateArmies(1);
                whosturn = TurnStatus.P2TURN;
            } else if(whosturn == TurnStatus.P2TURN){
                if(redArmies.size() < 3){
                    numberOfMoves = redArmies.size();
                }
                updateArmies(2);
                whosturn = TurnStatus.P1TURN;
            }
            movedArmies.clear();
        }
    }
    public void updateArmies(int faction){
        for (int i = 0; i< MAPSIZE; i++){
            for (int j = 0; j< MAPSIZE; j++) {
                if(getTile(i,j).getTileStatus().getValue() == faction && getTile(i,j).getCity() != null){
                    Army occArmy = getTile(i,j).getOccupyingArmy();
                    if(occArmy == null){
                        Army newArmy = new Army(10, faction - 1, i, j);
                        getTile(i,j).setOccupyingArmy(newArmy);
                        if(getTile(i,j).getTileStatus() == TileStatus.P2OCCUPIED){
                            greenArmies.add(newArmy);
                        } else if(getTile(i,j).getTileStatus() == TileStatus.P1OCCUPIED){
                            redArmies.add(newArmy);
                        }
                    } else {
                        int currFp = occArmy.getFirepower();
                        if(currFp + 10 >= 100){
                            occArmy.setFirepower(currFp + 99 - currFp);
                        } else {
                            occArmy.setFirepower(currFp + 10);
                        }
                    }
                }
            }
        }
    }
    public int evaluate(Tile[][] state){
        int redCities = 0;
        int greenCities = 0;
        int redArmyStrength = 0;
        int greenArmyStrength = 0;
        int redArmiesDisttoGreenCap = 0;
        int greenArmiesDisttoRedCap = 0;

        for (int x = 0; x < MAPSIZE; x++) {
            for (int y = 0; y < MAPSIZE; y++) {
                if (state[x][y].getTileStatus() == TileStatus.P1OCCUPIED){
                    redCities++;
                } else if (state[x][y].getTileStatus() == TileStatus.P2OCCUPIED) {
                    greenCities++;
                }
                if(state[x][y].getOccupyingArmy() != null){
                    if (state[x][y].getOccupyingArmy().getOwnerFaction() == 0){
                        redArmyStrength += state[x][y].getOccupyingArmy().getFirepower();
                        redArmiesDisttoGreenCap += manhattanDist(MAPSIZE - 1, MAPSIZE - 1, x, y);
                    } else if (state[x][y].getOccupyingArmy().getOwnerFaction() == 1) {
                        greenArmyStrength += state[x][y].getOccupyingArmy().getFirepower();
                        greenArmiesDisttoRedCap += manhattanDist(0, 0, x, y);
                    }
                }
            }
        }

        int redScore = redCities + redArmyStrength + greenArmiesDisttoRedCap / 4;
        int greenScore = greenCities + greenArmyStrength + redArmiesDisttoGreenCap / 4;
        return redScore - greenScore;
    }
    static int manhattanDist(int X1, int Y1, int X2, int Y2) {
        return Math.abs(X2 - X1) + Math.abs(Y2 - Y1);
    }
    public double[] minimax(Board currBoard, int depth, int faction) {
        Tile[][] state = currBoard.getBoard();

        double[] best = new double[5];
        best[0] = -1; // best starting x
        best[1] = -1; // best starting y
        best[2] = -1; // best ending x
        best[3] = -1; // best ending y

        if (depth == 0 || status != GameStatus.ACTIVE) {
            best[4] = evaluate(state);
            return best;
        }

        if (faction == 0) {
            best[4] = Double.NEGATIVE_INFINITY;
            for(Army army : redArmies){
                if(movedArmies.contains(army)){
                    continue;
                }
                int startX = army.getX();
                int startY = army.getY();
                List<int[]> adjacentCoords = getAdjacent(army.getX(), army.getY(), state);
                for(int[] coords : adjacentCoords){
                    int endX = coords[0];
                    int endY = coords[1];

                    Board newBoard = board.clone();
                    Tile[][] newState = newBoard.getBoard();
                    simulateMove(startX, startY, endX, endY, newState, 0);

                    double[] score = minimax(newBoard, depth - 1, 1);
                    score[0] = startX; // curr starting x
                    score[1] = startY; // curr starting y
                    score[2] = endX; // curr ending x
                    score[3] = endY; // curr ending y
                    if (score[4] > best[4]) {
                        best = score;
                    }
                }
            }
        } else {
            best[4] = Double.POSITIVE_INFINITY;
            for(Army army : greenArmies){
                if(movedArmies.contains(army)){
                    continue;
                }
                int startX = army.getX();
                int startY = army.getY();
                List<int[]> adjacentCoords = getAdjacent(army.getX(), army.getY(), state);
                for(int[] coords : adjacentCoords){
                    int endX = coords[0];
                    int endY = coords[1];

                    Board newBoard = board.clone();
                    Tile[][] newState = newBoard.getBoard();
                    simulateMove(startX, startY, endX, endY, newState, 1);

                    double[] score = minimax(newBoard, depth - 1, 0);
                    score[0] = startX; // curr starting x
                    score[1] = startY; // curr starting y
                    score[2] = endX; // curr ending x
                    score[3] = endY; // curr ending y
                    if (score[4] < best[4]) {
                        best = score;
                    }
                }
            }
        }
        return best;
    }
    public void aiTurn() {
        if(status != GameStatus.ACTIVE){
            return;
        }
        double[] move = minimax(board, 2, 1);
        int startX = (int) move[0];
        int startY = (int) move[1];
        int endX = (int) move[2];
        int endY = (int) move[3];
        moveArmy(startX, startY, endX, endY);
        System.out.println("AI has moved from " + board.getBoard()[startX][startY] + " to " + board.getBoard()[endX][endY]);
    }
    public void simulateMove(int x, int y, int endX, int endY, Tile[][] state, int faction){
        Tile startTile = state[x][y];
        Tile endTile = state[endX][endY];

        if(areAdjacent(endX, endY, x, y)){
            Army offArmy = startTile.getOccupyingArmy();
            Army defArmy = endTile.getOccupyingArmy();

            if(defArmy == null){ // movement
                startTile.setOccupyingArmy(null);
                endTile.setOccupyingArmy(offArmy);
                offArmy.setX(endX);
                offArmy.setY(endY);
                if(faction == 0){
                    endTile.setTileStatus(TileStatus.P1OCCUPIED);
                } else{
                    endTile.setTileStatus(TileStatus.P2OCCUPIED);
                }
            } else if (defArmy.getOwnerFaction() == offArmy.getOwnerFaction()) { // reinforce
                int currFp = defArmy.getFirepower();
                int inFp = offArmy.getFirepower();
                if(currFp + inFp >= 100){
                    defArmy.setFirepower(99);
                    offArmy.setFirepower(currFp + inFp - 99);
                } else {
                    defArmy.setFirepower(currFp + inFp);
                    startTile.setOccupyingArmy(null);
                }
            } else { // combat
                startTile.setOccupyingArmy(null);
                if(defArmy.getFirepower() >= offArmy.getFirepower()){
                    defArmy.setFirepower(defArmy.getFirepower() - offArmy.getFirepower() + 1);
                } else {
                    offArmy.setFirepower(offArmy.getFirepower() - defArmy.getFirepower());
                    endTile.setOccupyingArmy(offArmy);
                    offArmy.setX(endX);
                    offArmy.setY(endY);
                    if(faction == 0){
                        endTile.setTileStatus(TileStatus.P1OCCUPIED);
                    } else{
                        endTile.setTileStatus(TileStatus.P2OCCUPIED);
                    }
                }
            }
        }
    }
}
