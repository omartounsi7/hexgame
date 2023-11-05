package com.omar.hex;

import com.omar.model.*;
import com.omar.gui.MainPanel;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static com.omar.hex.HexConst.*;

/**********************************
  This is the main class of a Java program to play a game based on hexagonal tiles.
  The mechanism of handling hexes is in the file hexmech.java.

  Written by: M.H.
  Date: December 2012

 ***********************************/

public class HexGame {
	private AI ai = new AI();
	private MainPanel mainPanel;
	private Tile[][] board;
	public static GameStatus status = GameStatus.ACTIVE;
	public static TurnStatus whosturn = TurnStatus.P1TURN;
	public static int numberOfMoves = 3;
	public Set<Army> movedArmies = new HashSet<>();
	private Tile selectedTile;
	private List<int[]> adjacentTiles;
	public HexGame() {
		createMap();
		createAndShowGUI();
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

		board[0][0].setOccupyingArmy(new Army(10, 0, 0, 0));
		Army firstGreenArmy = new Army(10, 1, MAPSIZE - 1, MAPSIZE - 1);
		ai.getArmies().add(firstGreenArmy);
		board[MAPSIZE - 1][MAPSIZE - 1].setOccupyingArmy(firstGreenArmy);
		board[0][0].setTileStatus(TileStatus.P1OCCUPIED);
		board[MAPSIZE - 1][MAPSIZE - 1].setTileStatus(TileStatus.P2OCCUPIED);
	}
	private void createAndShowGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("HexWars");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(475, 775);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new BorderLayout());
		frame.setVisible(true);
		DrawingPanel panel = new DrawingPanel();
		frame.add(panel, BorderLayout.CENTER);
		this.mainPanel = new MainPanel();
		mainPanel.updateLabel();
		frame.add(mainPanel, BorderLayout.NORTH);
	}
	public class DrawingPanel extends JPanel {
		public DrawingPanel() {
			setBackground(BGCOLOR);
			LineBorder lineBorder = new LineBorder(Color.ORANGE, 2);
			this.setBorder(lineBorder);
			MyMouseListener ml = new MyMouseListener();
			addMouseListener(ml);
		}
		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			super.paintComponent(g2);
			updateTurn();
			mainPanel.updateLabel();
			for (int i = 0; i< MAPSIZE; i++) {
				for (int j = 0; j< MAPSIZE; j++) {
					HexMech.drawHex(i,j,g2);
				}
			}
			for (int i = 0; i< MAPSIZE; i++) {
				for (int j = 0; j< MAPSIZE; j++) {
					Army occArmy = board[i][j].getOccupyingArmy();
					String city = board[i][j].getCity();
					String str1 = "";
					String str2 = "";

					if(city != null){
						str1 += city;
					}
					if(occArmy != null){
						str2 += String.valueOf(occArmy.getFirepower());
					}

					HexMech.fillHex(i, j, board[i][j].getTileStatus(), g2, str1, str2, board[i][j] == selectedTile, board[i][j].isAdjacent());
					repaint();
				}
			}
		}
		public class MyMouseListener extends MouseAdapter {	//inner class inside DrawingPanel
			public void mouseClicked(MouseEvent e) {
				Point p = new Point( HexMech.pxtoHex(e.getX(),e.getY()) );
				if (p.x < 0 || p.y < 0 || p.x >= MAPSIZE || p.y >= MAPSIZE){
					return;
				}

				if (status == GameStatus.P1WINS || status == GameStatus.P2WINS){
					return;
				}

				if(whosturn == TurnStatus.P1TURN){
					System.out.println("It is P1's turn");
				} else if(whosturn == TurnStatus.P2TURN){
					System.out.println("It is P2's turn");
				}

				Tile clickedTile = board[p.x][p.y];
				System.out.println("You have clicked " + clickedTile);

				System.out.println("Green armies are: " + ai.getArmies());

				if(selectedTile == null){ // we have yet to select an army
					System.out.println("Selection phase.");
					selectArmy(clickedTile);
				} else { // an army has already been selected and is about to be moved
					System.out.println("Movement phase.");
					moveArmy(clickedTile);
				}
//				repaint();
			}
		}
	}
	public void selectArmy(Tile startTile){
		if(startTile.getOccupyingArmy() != null){ // check if clicked tile contains an army
			if(whosturn == TurnStatus.P1TURN){ // check whose turn it is
				if(startTile.getTileStatus() == TileStatus.P1OCCUPIED){ // check if tile belongs to P1
					if(!movedArmies.contains(startTile.getOccupyingArmy())){
						selectedTile = startTile;
						adjacentTiles = getAdjacent(startTile.getX(), startTile.getY());
						System.out.println(adjacentTiles);
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
		for (int i = 0; i< MAPSIZE; i++) {
			for (int j = 0; j< MAPSIZE; j++) {
				System.out.println(board[i][j].isAdjacent());
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
			adjacentTiles = null;
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
						ai.getArmies().remove(offArmy);
					}
				}
			} else { // combat
				selectedTile.setOccupyingArmy(null);
				if(defArmy.getFirepower() >= offArmy.getFirepower()){
					defArmy.setFirepower(defArmy.getFirepower() - offArmy.getFirepower() + 1);
					if(selectedTile.getTileStatus() == TileStatus.P2OCCUPIED){
						ai.getArmies().remove(offArmy);
					}
				} else {
					if(endTile.getTileStatus() == TileStatus.P2OCCUPIED){
						ai.getArmies().remove(defArmy);
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
			adjacentTiles = null;
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
	public void updateArmies(int faction){
		for (int i = 0; i< MAPSIZE; i++){
			for (int j = 0; j< MAPSIZE; j++) {
				if(board[i][j].getTileStatus().getValue() == faction && board[i][j].getCity() != null){
					Army occArmy = board[i][j].getOccupyingArmy();
					if(occArmy == null){
						Army newArmy = new Army(10, faction - 1, i, j);
						board[i][j].setOccupyingArmy(newArmy);
						if(board[i][j].getTileStatus() == TileStatus.P2OCCUPIED){
							ai.getArmies().add(newArmy);
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
	public void updateTurn(){
		if(numberOfMoves == 0){
			numberOfMoves = 3;
			if(whosturn == TurnStatus.P1TURN){
				updateArmies(1);
				whosturn = TurnStatus.P2TURN;
			} else if(whosturn == TurnStatus.P2TURN){
				updateArmies(2);
				whosturn = TurnStatus.P1TURN;
			}
			movedArmies.clear();
		}
	}
	public List<int[]> getAdjacent(int x, int y){
		List<int[]> adj = new ArrayList<>();

//		adj.add(new int[]{x,y}); // SELF

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
				if(board[x - 1][y].getOccupyingArmy() == null || board[x - 1][y + 1].getOccupyingArmy() == null){
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
				if(board[x + 1][y].getOccupyingArmy() == null || board[x + 1][y + 1].getOccupyingArmy() == null){
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
				if(board[x - 1][y].getOccupyingArmy() == null || board[x - 1][y - 1].getOccupyingArmy() == null){
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
				if(board[x + 1][y].getOccupyingArmy() == null || board[x + 1][y - 1].getOccupyingArmy() == null){
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
}
