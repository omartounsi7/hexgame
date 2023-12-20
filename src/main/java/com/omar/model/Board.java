package com.omar.model;


import java.util.ArrayList;
import java.util.List;

import static com.omar.hex.HexConst.MAPSIZE;

public class Board implements Cloneable {
    private double score;
    private Tile[][] tiles;
    public Board(Tile[][] tiles) {
        this.tiles = tiles;
        evaluate();
    }
    public double getScore() {
        return score;
    }
    public void setScore(double score) {
        this.score = score;
    }
    public Tile[][] getTiles() {
        return tiles;
    }
    public int checkVictory(){
        if(tiles[0][0].getTileStatus() == TileStatus.P2OCCUPIED){
            return 2;
        }
        else if(tiles[MAPSIZE - 1][MAPSIZE - 1].getTileStatus() == TileStatus.P1OCCUPIED){
            return 1;
        }
        return 0;
    }
    public void evaluate(){
        int isVictory = this.checkVictory();
        if(isVictory == 2){
            this.score = Double.NEGATIVE_INFINITY;
            return;
        } else if(isVictory == 1){
            this.score = Double.POSITIVE_INFINITY;
            return;
        }

        double redCities = 0;
        double greenCities = 0;
        for (int i = 0; i < MAPSIZE; i++) {
            for (int j = 0; j < MAPSIZE; j++) {
                if(tiles[i][j].getCity() != null){
                    if(tiles[i][j].getTileStatus() == TileStatus.P1OCCUPIED){
                        redCities++;
                    } else if(tiles[i][j].getTileStatus() == TileStatus.P2OCCUPIED){
                        greenCities++;
                    }
                }
            }
        }
        this.score = redCities - greenCities;
    }
    public void moveArmy(int startX, int startY, int endX, int endY, TurnStatus whosturn) {
        Tile startTile = tiles[startX][startY];
        Tile endTile = tiles[endX][endY];

        Army offArmy = startTile.getOccupyingArmy();
        Army defArmy = endTile.getOccupyingArmy();

        if(defArmy == null){ // movement
            startTile.setOccupyingArmy(null);
            endTile.setOccupyingArmy(offArmy);
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
            }
        } else { // combat
            startTile.setOccupyingArmy(null);
            if(defArmy.getFirepower() >= offArmy.getFirepower()){
                defArmy.setFirepower(defArmy.getFirepower() - offArmy.getFirepower() + 1);
            } else {
                offArmy.setFirepower(offArmy.getFirepower() - defArmy.getFirepower());
                endTile.setOccupyingArmy(offArmy);
                if(whosturn == TurnStatus.P1TURN){
                    endTile.setTileStatus(TileStatus.P1OCCUPIED);
                } else if(whosturn == TurnStatus.P2TURN){
                    endTile.setTileStatus(TileStatus.P2OCCUPIED);
                }
            }
        }
    }
    public List<int[]> getArmiesPos(int faction){
        List<int[]> positions = new ArrayList<>();

        for (int i = 0; i < MAPSIZE; i++) {
            for (int j = 0; j < MAPSIZE; j++) {
                if(tiles[i][j].getOccupyingArmy() != null && tiles[i][j].getOccupyingArmy().getOwnerFaction() == faction){
                    positions.add(new int[]{i, j});
                }
            }
        }

        return positions;
    }
    public List<int[]> getAdjacent(int x, int y){
        List<int[]> adj = new ArrayList<>();

        if(y - 1 >= 0){
            adj.add(new int[]{x,y - 1});
        }
        if(y + 1 < MAPSIZE){
            adj.add(new int[]{x,y + 1});
        }
        if(y - 2 >= 0 && tiles[x][y - 1].getOccupyingArmy() == null){
            adj.add(new int[]{x,y - 2});
        }
        if(y + 2 < MAPSIZE && tiles[x][y + 1].getOccupyingArmy() == null){
            adj.add(new int[]{x,y + 2});
        }

        if(x % 2 == 1){ // odd column
            if(x - 1 >= 0){
                adj.add(new int[]{x - 1,y});
                if(y + 1 < MAPSIZE){
                    adj.add(new int[]{x - 1,y + 1});
                }
                if(y - 1 >= 0 && (tiles[x - 1][y].getOccupyingArmy() == null || tiles[x][y - 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x - 1,y - 1});
                }
                if(y + 2 < MAPSIZE && (tiles[x - 1][y + 1].getOccupyingArmy() == null || tiles[x][y + 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x - 1,y + 2});
                }
            }
            if(x + 1 < MAPSIZE){
                adj.add(new int[]{x + 1,y});
                if(y + 1 < MAPSIZE){
                    adj.add(new int[]{x + 1,y + 1});
                }
                if(y - 1 >= 0 && (tiles[x][y - 1].getOccupyingArmy() == null || tiles[x + 1][y].getOccupyingArmy() == null)){
                    adj.add(new int[]{x + 1,y - 1});
                }
                if(y + 2 < MAPSIZE && (tiles[x][y + 1].getOccupyingArmy() == null || tiles[x + 1][y + 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x + 1,y + 2});
                }
            }
            if(x - 2 >= 0) {
                if(tiles[x - 1][y].getOccupyingArmy() == null || (y + 1 < MAPSIZE && tiles[x - 1][y + 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x - 2,y});
                }
                if(y - 1 >= 0 && tiles[x - 1][y].getOccupyingArmy() == null){
                    adj.add(new int[]{x - 2,y - 1});
                }
                if(y + 1 < MAPSIZE && tiles[x - 1][y + 1].getOccupyingArmy() == null){
                    adj.add(new int[]{x - 2,y + 1});
                }
            }
            if(x + 2 < MAPSIZE) {
                if(tiles[x + 1][y].getOccupyingArmy() == null || (y + 1 < MAPSIZE && tiles[x + 1][y + 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x + 2,y});
                }
                if(y - 1 >= 0 && tiles[x + 1][y].getOccupyingArmy() == null){
                    adj.add(new int[]{x + 2,y - 1});
                }
                if(y + 1 < MAPSIZE && tiles[x + 1][y + 1].getOccupyingArmy() == null){
                    adj.add(new int[]{x + 2,y + 1});
                }
            }
        } else { // even column
            if(x - 1 >= 0){
                adj.add(new int[]{x - 1,y});
                if(y - 1 >= 0){
                    adj.add(new int[]{x - 1,y - 1});
                }
                if(y - 2 >= 0 && (tiles[x - 1][y - 1].getOccupyingArmy() == null || tiles[x][y - 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x - 1,y - 2});
                }
                if(y + 1 < MAPSIZE && (tiles[x - 1][y].getOccupyingArmy() == null || tiles[x][y + 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x - 1,y + 1});
                }
            }
            if(x + 1 < MAPSIZE){
                adj.add(new int[]{x + 1,y});
                if(y - 1 >= 0){
                    adj.add(new int[]{x + 1,y - 1});
                }
                if(y - 2 >= 0 && (tiles[x + 1][y - 1].getOccupyingArmy() == null || tiles[x][y - 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x + 1,y - 2});
                }
                if(y + 1 < MAPSIZE && (tiles[x + 1][y].getOccupyingArmy() == null || tiles[x][y + 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x + 1,y + 1});
                }
            }
            if(x - 2 >= 0) {
                if(tiles[x - 1][y].getOccupyingArmy() == null || (y - 1 >= 0 && tiles[x - 1][y - 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x - 2,y});
                }
                if(y - 1 >= 0 && tiles[x - 1][y - 1].getOccupyingArmy() == null){
                    adj.add(new int[]{x - 2,y - 1});
                }
                if(y + 1 < MAPSIZE && tiles[x - 1][y].getOccupyingArmy() == null){
                    adj.add(new int[]{x - 2,y + 1});
                }
            }
            if(x + 2 < MAPSIZE) {
                if(tiles[x + 1][y].getOccupyingArmy() == null || (y - 1 >= 0 && tiles[x + 1][y - 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x + 2,y});
                }
                if(y - 1 >= 0 && tiles[x + 1][y - 1].getOccupyingArmy() == null){
                    adj.add(new int[]{x + 2,y - 1});
                }
                if(y + 1 < MAPSIZE && tiles[x + 1][y].getOccupyingArmy() == null){
                    adj.add(new int[]{x + 2,y + 1});
                }
            }
        }
        return adj;
    }
    public Tile getTile(int x, int y) {
        return tiles[x][y];
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
    @Override
    public Board clone() {
        Tile[][] clonedBoard = new Tile[MAPSIZE][MAPSIZE];

        for (int i = 0; i < MAPSIZE; i++) {
            for (int j = 0; j < MAPSIZE; j++) {
                clonedBoard[i][j] = tiles[i][j].clone();
            }
        }

        return new Board(clonedBoard);
    }
}
