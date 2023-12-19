package com.omar.model;

import com.omar.hex.HexMech;

import java.util.*;

import static com.omar.hex.HexConst.*;

public class World {
    public Board board;
    private final List<Army> redArmies;
    private final List<Army> greenArmies;
    public static GameStatus status = GameStatus.ACTIVE;
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
    public void selectArmy(Tile startTile){
        if(startTile.getOccupyingArmy() != null){ // check if clicked tile contains an army
            if(startTile.getTileStatus() == TileStatus.P1OCCUPIED){ // check if tile belongs to P1
                selectedTile = startTile;
                adjacentTiles = getAdjacent(startTile.getX(), startTile.getY(), board.getBoard());
                for (int[] adjacentTile : adjacentTiles) {
                    getTile(adjacentTile[0], adjacentTile[1]).setAdjacent(true);
                }
            }
        }
    }
    public void executeMove(int x, int y) {
        if(areAdjacent(selectedTile.getX(), selectedTile.getY(), x, y)){
            moveArmy(selectedTile.getX(), selectedTile.getY(), x, y, board.getBoard(), TurnStatus.P1TURN, redArmies, greenArmies);
            clearAdjTiles(board.getBoard());

            int isVictory = checkVictory(board.getBoard());
            if(isVictory == 2){
                status = GameStatus.P2WINS;
            } else if(isVictory == 1){
                status = GameStatus.P1WINS;
            }
            updateArmies(0, board.getBoard(), redArmies, greenArmies);

            aiTurn();
            isVictory = checkVictory(board.getBoard());
            if(isVictory == 2){
                status = GameStatus.P2WINS;
            } else if(isVictory == 1){
                status = GameStatus.P1WINS;
            }
            updateArmies(1, board.getBoard(), redArmies, greenArmies);
        }
    }
    public void clearAdjTiles(Tile[][] state){
        selectedTile = null;
        for (int[] adjacentTile : adjacentTiles) {
            state[adjacentTile[0]][adjacentTile[1]].setAdjacent(false);
        }
        adjacentTiles.clear();
    }
    public void moveArmy(int x, int y, int endX, int endY, Tile[][] state, TurnStatus whosturn, List<Army> redArmies, List<Army> greenArmies) {
        Tile startTile = state[x][y];
        Tile endTile = state[endX][endY];

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
        } else if (defArmy.getOwnerFaction() == offArmy.getOwnerFaction()) { // reinforce
            int currFp = defArmy.getFirepower();
            int inFp = offArmy.getFirepower();
            if(currFp + inFp >= 100){
                defArmy.setFirepower(99);
                offArmy.setFirepower(currFp + inFp - 99);
            } else {
                defArmy.setFirepower(currFp + inFp);
                startTile.setOccupyingArmy(null);
                if(startTile.getTileStatus() == TileStatus.P2OCCUPIED){
                    greenArmies.remove(offArmy);
                } else if(startTile.getTileStatus() == TileStatus.P1OCCUPIED){
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
            }
        }
    }
    public int checkVictory(Tile[][] state){
        if(state[0][0].getTileStatus() == TileStatus.P2OCCUPIED){
            return 2;
        }
        else if(state[MAPSIZE - 1][MAPSIZE - 1].getTileStatus() == TileStatus.P1OCCUPIED){
            return 1;
        }
        return 0;
    }
    public void updateArmies(int faction, Tile[][] state, List<Army> redArmies, List<Army> greenArmies){
        for (int i = 0; i< MAPSIZE; i++){
            for (int j = 0; j< MAPSIZE; j++) {
                if(state[i][j].getTileStatus().getValue() == faction && state[i][j].getCity() != null){
                    Army occArmy = state[i][j].getOccupyingArmy();
                    if(occArmy == null){
                        Army newArmy = new Army(10, faction, i, j);
                        state[i][j].setOccupyingArmy(newArmy);
                        if(state[i][j].getTileStatus() == TileStatus.P2OCCUPIED){
                            greenArmies.add(newArmy);
                        } else if(state[i][j].getTileStatus() == TileStatus.P1OCCUPIED){
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
    public void aiTurn() {
        if(status != GameStatus.ACTIVE){
            return;
        }

        // minimax here
        // hardcoded for now!
        int startX =  MAPSIZE - 1;
        int startY = MAPSIZE - 1;
        int endX = MAPSIZE - 2;
        int endY = MAPSIZE - 1;

        moveArmy(startX, startY, endX, endY, board.getBoard(), TurnStatus.P2TURN, redArmies, greenArmies);
        System.out.println("AI has moved from " + board.getBoard()[startX][startY] + " to " + board.getBoard()[endX][endY]);
    }

    static int manhattanDist(int X1, int Y1, int X2, int Y2) {
        return Math.abs(X2 - X1) + Math.abs(Y2 - Y1);
    }
    public static List<Army> cloneArmies(List<Army> armies) {
        List<Army> clonedArmies = new LinkedList<>();
        for (Army originalArmy : armies) {
            Army clonedArmy = originalArmy.clone();
            clonedArmies.add(clonedArmy);
        }
        return clonedArmies;
    }
}
