package com.omar.model;

import com.omar.hex.HexMech;

import java.util.*;

import static com.omar.hex.HexConst.*;

public class World {
    public Board board;
    public static GameStatus status = GameStatus.ACTIVE;
    public Tile selectedTile;
    private List<int[]> adjacentTiles;
    public World(){
        this.board = createMap();
    }
    public Tile getTile(int x, int y) {
        return board.getTiles()[x][y];
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
        Army firstRedArmy = new Army(10, 1);
        tiles[0][0].setOccupyingArmy(firstRedArmy);
        Army firstGreenArmy = new Army(10, 2);
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
    public void selectArmy(Tile startTile){
        if(startTile.getOccupyingArmy() != null){ // check if clicked tile contains an army
            if(startTile.getTileStatus() == TileStatus.P1OCCUPIED){ // check if tile belongs to P1
                selectedTile = startTile;
                adjacentTiles = board.getAdjacent(startTile.getX(), startTile.getY());
                for (int[] adjacentTile : adjacentTiles) {
                    getTile(adjacentTile[0], adjacentTile[1]).setAdjacent(true);
                }
            }
        }
    }
    public void executeMove(int x, int y) {
        if(areAdjacent(selectedTile.getX(), selectedTile.getY(), x, y)){
            board.moveArmy(selectedTile.getX(), selectedTile.getY(), x, y, TurnStatus.P1TURN);
            clearAdjTiles(board.getTiles());

            int isVictory = board.checkVictory();
            if(isVictory == 2){
                status = GameStatus.P2WINS;
            } else if(isVictory == 1){
                status = GameStatus.P1WINS;
            }
            updateArmies(1, board.getTiles());

            aiTurn();
            isVictory = board.checkVictory();
            if(isVictory == 2){
                status = GameStatus.P2WINS;
            } else if(isVictory == 1){
                status = GameStatus.P1WINS;
            }
            updateArmies(2, board.getTiles());
        }
    }
    public void clearAdjTiles(Tile[][] state){
        selectedTile = null;
        for (int[] adjacentTile : adjacentTiles) {
            state[adjacentTile[0]][adjacentTile[1]].setAdjacent(false);
        }
        adjacentTiles.clear();
    }
    public void updateArmies(int faction, Tile[][] state){
        for (int i = 0; i< MAPSIZE; i++){
            for (int j = 0; j< MAPSIZE; j++) {
                if(state[i][j].getTileStatus().getValue() == faction && state[i][j].getCity() != null){
                    Army occArmy = state[i][j].getOccupyingArmy();
                    if(occArmy == null){
                        Army newArmy = new Army(10, faction);
                        state[i][j].setOccupyingArmy(newArmy);
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
        board = AI.minimax(board, 4, 2);
    }
}
