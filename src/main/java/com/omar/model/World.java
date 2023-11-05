package com.omar.model;

import com.omar.hex.HexMech;

import java.util.*;

import static com.omar.hex.HexConst.*;

public class World {
    Map<int[], List<int[]>> aiMoves = new HashMap<>();
    private Tile[][] board;
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
        createMap();
    }
    public Tile getTile(int x, int y) {
        return board[x][y];
    }
    private void createMap(){
        HexMech.setXYasVertex(false);
        HexMech.setHeight(TILESIZE);
        HexMech.setBorders(BORDERS);
        board = new Tile[MAPSIZE][MAPSIZE];
        Random random = new Random();

        for (int x = 0; x < MAPSIZE; x++) {
            for (int y = 0; y < MAPSIZE; y++) {
                board[x][y] = new Tile(x, y);
                boolean isCapital = (x == 0 && y == 0) || (x == MAPSIZE - 1 && y == MAPSIZE - 1);
                if (random.nextDouble() <= 0.2 || isCapital) {
                    int randomCityIndex = random.nextInt(CityNames.cityNames.length);
                    String cityName = CityNames.cityNames[randomCityIndex];
                    board[x][y].setCity(cityName);
                }
            }
        }
        Army firstRedArmy = new Army(10, 0, 0, 0);
        redArmies.add(firstRedArmy);
        board[0][0].setOccupyingArmy(firstRedArmy);
        Army firstGreenArmy = new Army(10, 1, MAPSIZE - 1, MAPSIZE - 1);
        greenArmies.add(firstGreenArmy);
        board[MAPSIZE - 1][MAPSIZE - 1].setOccupyingArmy(firstGreenArmy);
        board[0][0].setTileStatus(TileStatus.P1OCCUPIED);
        board[MAPSIZE - 1][MAPSIZE - 1].setTileStatus(TileStatus.P2OCCUPIED);
    }
    public void selectArmy(Tile startTile){
        if(startTile.getOccupyingArmy() != null){ // check if clicked tile contains an army
            if(whosturn == TurnStatus.P1TURN){ // check whose turn it is
                if(startTile.getTileStatus() == TileStatus.P1OCCUPIED){ // check if tile belongs to P1
                    if(!movedArmies.contains(startTile.getOccupyingArmy())){
                        selectedTile = startTile;
                        adjacentTiles = getAdjacent(startTile.getX(), startTile.getY());
                        for (int[] adjacentTile : adjacentTiles) {
                            board[adjacentTile[0]][adjacentTile[1]].setAdjacent(true);
                        }
                    }

                }
            } else if(whosturn == TurnStatus.P2TURN){
                if(startTile.getTileStatus() == TileStatus.P2OCCUPIED){ // check if tile belongs to P2
                    if(!movedArmies.contains(startTile.getOccupyingArmy())){
                        selectedTile = startTile;
                        adjacentTiles = getAdjacent(startTile.getX(), startTile.getY());
                        for (int[] adjacentTile : adjacentTiles) {
                            board[adjacentTile[0]][adjacentTile[1]].setAdjacent(true);
                        }
                    }
                }
            }
        }
    }
    public void moveArmy(Tile endTile){
        int endX = endTile.getX();
        int endY = endTile.getY();
        int x = selectedTile.getX(); // starting x
        int y = selectedTile.getY(); // starting y


        if(x == endX && y == endY){
            selectedTile = null;
            for (int[] adjacentTile : adjacentTiles) {
                board[adjacentTile[0]][adjacentTile[1]].setAdjacent(false);
            }
            adjacentTiles.clear();
//            adjacentTiles = null;
            return;
        }

        if(areAdjacent(endX, endY, x, y)){
            Army offArmy = selectedTile.getOccupyingArmy();
            Army defArmy = endTile.getOccupyingArmy();

            if(defArmy == null){ // movement
                selectedTile.setOccupyingArmy(null);
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
                    selectedTile.setOccupyingArmy(null);
                    if(selectedTile.getTileStatus() == TileStatus.P2OCCUPIED){
                        greenArmies.remove(offArmy);
                    } else if(selectedTile.getTileStatus() == TileStatus.P1OCCUPIED){
                        redArmies.remove(offArmy);
                    }
                }
            } else { // combat
                selectedTile.setOccupyingArmy(null);
                if(defArmy.getFirepower() >= offArmy.getFirepower()){
                    defArmy.setFirepower(defArmy.getFirepower() - offArmy.getFirepower() + 1);
                    if(selectedTile.getTileStatus() == TileStatus.P2OCCUPIED){
                        greenArmies.remove(offArmy);
                    }  else if(selectedTile.getTileStatus() == TileStatus.P1OCCUPIED){
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
            selectedTile = null;
            System.out.println("You have moved to " + endTile);
            movedArmies.add(offArmy);
            numberOfMoves--;
            checkVictory();
//			updateTurn();
//			mainPanel.updateLabel();
            for (int[] adjacentTile : adjacentTiles) {
                board[adjacentTile[0]][adjacentTile[1]].setAdjacent(false);
            }
            adjacentTiles.clear();
//            adjacentTiles = null;


            // THIS IS WHERE THE AI WILL MAKE ITS MOVES
            if(numberOfMoves == 0){
                updateTurn();
                while(numberOfMoves != 0){
                    aiTurn();
                    numberOfMoves--;
                }
                updateTurn();
            }

        } else {
            System.out.println("Incorrect destination!");
        }
    }
    public void checkVictory(){
        if(board[0][0].getTileStatus() == TileStatus.P2OCCUPIED){
            status = GameStatus.P2WINS;
        }
        else if(board[MAPSIZE - 1][MAPSIZE - 1].getTileStatus() == TileStatus.P1OCCUPIED){
            status = GameStatus.P1WINS;
        }
    }
    public boolean areAdjacent(int endX, int endY, int x, int y){
        if (endX == x) {
            if(endY == y - 1 || endY == y + 1){
                return true;
            }
            if(endY == y - 2){
                return board[x][y - 1].getOccupyingArmy() == null;
            }
            if(endY == y + 2){
                return board[x][y + 1].getOccupyingArmy() == null;
            }
        }

        if(x % 2 == 1){ // odd column
            if(endX == x - 1){
                if(endY == y || endY == y + 1){
                    return true;
                }
                if(endY == y - 1){
                    return board[x - 1][y].getOccupyingArmy() == null || board[x][y - 1].getOccupyingArmy() == null;
                }
                if(endY == y + 2){
                    return board[x - 1][y + 1].getOccupyingArmy() == null || board[x][y + 1].getOccupyingArmy() == null;
                }
            }
            if(endX == x + 1){
                if(endY == y || endY == y + 1){
                    return true;
                }
                if(endY == y - 1){
                    return board[x][y - 1].getOccupyingArmy() == null || board[x + 1][y].getOccupyingArmy() == null;
                }
                if(endY == y + 2){
                    return board[x][y + 1].getOccupyingArmy() == null || board[x + 1][y + 1].getOccupyingArmy() == null;
                }
            }
            if(endX == x - 2) {
                if(endY == y){
                    return board[x - 1][y].getOccupyingArmy() == null || board[x - 1][y + 1].getOccupyingArmy() == null; // HERE!
                }
                if(endY == y - 1){
                    return board[x - 1][y].getOccupyingArmy() == null;
                }
                if(endY == y + 1){
                    return board[x - 1][y + 1].getOccupyingArmy() == null;
                }
            }
            if(endX == x + 2) {
                if(endY == y){
                    return board[x + 1][y].getOccupyingArmy() == null || board[x + 1][y + 1].getOccupyingArmy() == null; // HERE!
                }
                if(endY == y - 1){
                    return board[x + 1][y].getOccupyingArmy() == null;
                }
                if(endY == y + 1){
                    return board[x + 1][y + 1].getOccupyingArmy() == null;
                }
            }

        } else { // even column
            if(endX == x - 1){
                if(endY == y || endY == y - 1){
                    return true;
                }
                if(endY == y - 2){
                    return board[x - 1][y - 1].getOccupyingArmy() == null || board[x][y - 1].getOccupyingArmy() == null;
                }
                if(endY == y + 1){
                    return board[x - 1][y].getOccupyingArmy() == null || board[x][y + 1].getOccupyingArmy() == null;
                }
            }
            if(endX == x + 1){
                if(endY == y || endY == y - 1){
                    return true;
                }
                if(endY == y - 2){
                    return board[x + 1][y - 1].getOccupyingArmy() == null || board[x][y - 1].getOccupyingArmy() == null;
                }
                if(endY == y + 1){
                    return board[x + 1][y].getOccupyingArmy() == null || board[x][y + 1].getOccupyingArmy() == null;
                }
            }
            if(endX == x - 2) {
                if(endY == y){
                    return board[x - 1][y].getOccupyingArmy() == null || board[x - 1][y - 1].getOccupyingArmy() == null; // HERE!
                }
                if(endY == y - 1){
                    return board[x - 1][y - 1].getOccupyingArmy() == null;
                }
                if(endY == y + 1){
                    return board[x - 1][y].getOccupyingArmy() == null;
                }
            }
            if(endX == x + 2) {
                if(endY == y){
                    return board[x + 1][y].getOccupyingArmy() == null || board[x + 1][y - 1].getOccupyingArmy() == null; // HERE!
                }
                if(endY == y - 1){
                    return board[x + 1][y - 1].getOccupyingArmy() == null;
                }
                if(endY == y + 1){
                    return board[x + 1][y].getOccupyingArmy() == null;
                }
            }
        }
        return false;
    }
    public List<int[]> getAdjacent(int x, int y){
        List<int[]> adj = new ArrayList<>();

        adj.add(new int[]{x,y}); // SELF

        if(y - 1 >= 0){
            adj.add(new int[]{x,y - 1});
        }
        if(y + 1 < MAPSIZE){
            adj.add(new int[]{x,y + 1});
        }
        if(y - 2 >= 0 && board[x][y - 1].getOccupyingArmy() == null){
            adj.add(new int[]{x,y - 2});
        }
        if(y + 2 < MAPSIZE && board[x][y + 1].getOccupyingArmy() == null){
            adj.add(new int[]{x,y + 2});
        }

        if(x % 2 == 1){ // odd column
            if(x - 1 >= 0){
                adj.add(new int[]{x - 1,y});
                if(y + 1 < MAPSIZE){
                    adj.add(new int[]{x - 1,y + 1});
                }
                if(y - 1 >= 0 && (board[x - 1][y].getOccupyingArmy() == null || board[x][y - 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x - 1,y - 1});
                }
                if(y + 2 < MAPSIZE && (board[x - 1][y + 1].getOccupyingArmy() == null || board[x][y + 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x - 1,y + 2});
                }
            }
            if(x + 1 < MAPSIZE){
                adj.add(new int[]{x + 1,y});
                if(y + 1 < MAPSIZE){
                    adj.add(new int[]{x + 1,y + 1});
                }
                if(y - 1 >= 0 && (board[x][y - 1].getOccupyingArmy() == null || board[x + 1][y].getOccupyingArmy() == null)){
                    adj.add(new int[]{x + 1,y - 1});
                }
                if(y + 2 < MAPSIZE && (board[x][y + 1].getOccupyingArmy() == null || board[x + 1][y + 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x + 1,y + 2});
                }
            }
            if(x - 2 >= 0) {
                if(board[x - 1][y].getOccupyingArmy() == null || (y + 1 < MAPSIZE && board[x - 1][y + 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x - 2,y});
                }
                if(y - 1 >= 0 && board[x - 1][y].getOccupyingArmy() == null){
                    adj.add(new int[]{x - 2,y - 1});
                }
                if(y + 1 < MAPSIZE && board[x - 1][y + 1].getOccupyingArmy() == null){
                    adj.add(new int[]{x - 2,y + 1});
                }
            }
            if(x + 2 < MAPSIZE) {
                if(board[x + 1][y].getOccupyingArmy() == null || (y + 1 < MAPSIZE && board[x + 1][y + 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x + 2,y});
                }
                if(y - 1 >= 0 && board[x + 1][y].getOccupyingArmy() == null){
                    adj.add(new int[]{x + 2,y - 1});
                }
                if(y + 1 < MAPSIZE && board[x + 1][y + 1].getOccupyingArmy() == null){
                    adj.add(new int[]{x + 2,y + 1});
                }
            }
        } else { // even column
            if(x - 1 >= 0){
                adj.add(new int[]{x - 1,y});
                if(y - 1 >= 0){
                    adj.add(new int[]{x - 1,y - 1});
                }
                if(y - 2 >= 0 && (board[x - 1][y - 1].getOccupyingArmy() == null || board[x][y - 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x - 1,y - 2});
                }
                if(y + 1 < MAPSIZE && (board[x - 1][y].getOccupyingArmy() == null || board[x][y + 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x - 1,y + 1});
                }
            }
            if(x + 1 < MAPSIZE){
                adj.add(new int[]{x + 1,y});
                if(y - 1 >= 0){
                    adj.add(new int[]{x + 1,y - 1});
                }
                if(y - 2 >= 0 && (board[x + 1][y - 1].getOccupyingArmy() == null || board[x][y - 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x + 1,y - 2});
                }
                if(y + 1 < MAPSIZE && (board[x + 1][y].getOccupyingArmy() == null || board[x][y + 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x + 1,y + 1});
                }
            }
            if(x - 2 >= 0) {
                if(board[x - 1][y].getOccupyingArmy() == null || (y - 1 >= 0 && board[x - 1][y - 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x - 2,y});
                }
                if(y - 1 >= 0 && board[x - 1][y - 1].getOccupyingArmy() == null){
                    adj.add(new int[]{x - 2,y - 1});
                }
                if(y + 1 < MAPSIZE && board[x - 1][y].getOccupyingArmy() == null){
                    adj.add(new int[]{x - 2,y + 1});
                }
            }
            if(x + 2 < MAPSIZE) {
                if(board[x + 1][y].getOccupyingArmy() == null || (y - 1 >= 0 && board[x + 1][y - 1].getOccupyingArmy() == null)){
                    adj.add(new int[]{x + 2,y});
                }
                if(y - 1 >= 0 && board[x + 1][y - 1].getOccupyingArmy() == null){
                    adj.add(new int[]{x + 2,y - 1});
                }
                if(y + 1 < MAPSIZE && board[x + 1][y].getOccupyingArmy() == null){
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
                if(board[i][j].getTileStatus().getValue() == faction && board[i][j].getCity() != null){
                    Army occArmy = board[i][j].getOccupyingArmy();
                    if(occArmy == null){
                        Army newArmy = new Army(10, faction - 1, i, j);
                        board[i][j].setOccupyingArmy(newArmy);
                        if(board[i][j].getTileStatus() == TileStatus.P2OCCUPIED){
                            greenArmies.add(newArmy);
                        } else if(board[i][j].getTileStatus() == TileStatus.P1OCCUPIED){
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
    public void aiTurn(){
        System.out.println("AI has played!");
        totalPossibleMoves();
        printMap();
        aiMoves.clear();
    }
    public void totalPossibleMoves(){
        for(Army army : greenArmies){
            aiMoves.put(new int[]{army.getX(), army.getY()}, getAdjacent(army.getX(), army.getX()));
        }
    }
    public  void printMap() {
        for (Map.Entry<int[], List<int[]>> entry : aiMoves.entrySet()) {
            int[] key = entry.getKey();
            List<int[]> values = entry.getValue();
            System.out.print("Key: ");
            System.out.print("[");
            for (int i = 0; i < key.length; i++) {
                System.out.print(key[i]);
                if (i < key.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");
            System.out.print("Values: ");
            for (int[] value : values) {
                System.out.print("[");
                for (int i = 0; i < value.length; i++) {
                    System.out.print(value[i]);
                    if (i < value.length - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println("]");

            }
        }




    }
}
