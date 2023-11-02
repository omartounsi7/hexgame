package com.omar.hex;

import com.omar.model.Army;
import com.omar.model.CityNames;
import com.omar.model.Tile;
import com.omar.model.TileStatus;
import com.omar.gui.MainPanel;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.event.*;
import java.util.Random;

import static com.omar.hex.HexConst.*;

/**********************************
  This is the main class of a Java program to play a game based on hexagonal tiles.
  The mechanism of handling hexes is in the file hexmech.java.

  Written by: M.H.
  Date: December 2012

 ***********************************/

public class HexGame {
	private MainPanel mainPanel;
	private Tile[][] board;
	public static GameStatus status = GameStatus.ACTIVE;
	public static TurnStatus whosturn = TurnStatus.P1TURN;
	private Tile selectedTile;
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

		board[0][0].setOccupyingArmy(new Army(10, 0));
		board[MAPSIZE - 1][MAPSIZE - 1].setOccupyingArmy(new Army(10, 1));
		board[0][0].setTileStatus(TileStatus.P1OCCUPIED);
		board[MAPSIZE - 1][MAPSIZE - 1].setTileStatus(TileStatus.P2OCCUPIED);
	}
	private void createAndShowGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("HexWars");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(475, 750);
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

					HexMech.fillHex(i, j, board[i][j].getTileStatus(), g2, str1, str2);
				}
			}
		}
		class MyMouseListener extends MouseAdapter {	//inner class inside DrawingPanel
			public void mouseClicked(MouseEvent e) {
				Point p = new Point( HexMech.pxtoHex(e.getX(),e.getY()) );
				if (p.x < 0 || p.y < 0 || p.x >= MAPSIZE || p.y >= MAPSIZE){
					return;
				}

				if(whosturn == TurnStatus.P1TURN){
					System.out.println("It is P1's turn");
				} else if(whosturn == TurnStatus.P2TURN){
					System.out.println("It is P2's turn");
				}

				Tile clickedTile = board[p.x][p.y];
				System.out.println("You have clicked " + clickedTile);

				if(selectedTile == null){ // we have yet to select an army
					System.out.println("Selection phase.");
					selectArmy(clickedTile);
				} else { // an army has already been selected and is about to be moved
					System.out.println("Movement phase.");
					moveArmy(clickedTile);
				}
				repaint();
			}
			public void selectArmy(Tile startTile){
				if(startTile.getOccupyingArmy() != null){ // check if clicked tile contains an army
					if(whosturn == TurnStatus.P1TURN){ // check whose turn it is
						if(startTile.getTileStatus() == TileStatus.P1OCCUPIED){ // check if tile belongs to P1
//							System.out.println("You have clicked a tile that belongs to p1");
							selectedTile = startTile;
						}
					} else if(whosturn == TurnStatus.P2TURN){
						if(startTile.getTileStatus() == TileStatus.P2OCCUPIED){ // check if tile belongs to P2
//							System.out.println("You have clicked a tile that belongs to p2");
							selectedTile = startTile;
						}
					}
				}
			}
			public void moveArmy(Tile endTile){
				int endX = endTile.getX();
				int endY = endTile.getY();
				int x = selectedTile.getX(); // starting x
				int y = selectedTile.getY(); // starting y

				if(areAdjacent(endX, endY, x, y)){
					Army offArmy = selectedTile.getOccupyingArmy();
					Army defArmy = endTile.getOccupyingArmy();

					if(defArmy == null){ // movement
						selectedTile.setOccupyingArmy(null);
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
							selectedTile.setOccupyingArmy(null);
						}


					} else { // combat
						selectedTile.setOccupyingArmy(null);
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

					if(whosturn == TurnStatus.P1TURN){
						updateArmies(1);
						whosturn = TurnStatus.P2TURN;
					} else if(whosturn == TurnStatus.P2TURN){
						updateArmies(2);
						whosturn = TurnStatus.P1TURN;
					}

					selectedTile = null;
					System.out.println("You have moved to " + endTile);
					mainPanel.updateLabel();

				} else {
					System.out.println("Incorrect destination!");
				}
			}
			public void updateArmies(int faction){
				for (int i = 0; i< MAPSIZE; i++){
					for (int j = 0; j< MAPSIZE; j++) {
						if(board[i][j].getTileStatus().getValue() == faction && board[i][j].getCity() != null){
							Army occArmy = board[i][j].getOccupyingArmy();
							if(occArmy == null){
								board[i][j].setOccupyingArmy(new Army(10, faction - 1));
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
			public boolean areAdjacent(int endX, int endY, int x, int y){
				if (endX == x) {
					return endY == y - 1 || endY == y + 1 || endY == y - 2 || endY == y + 2;
				} else if(endX == x - 2 || endX == x + 2) {
					return endY == y || endY == y - 1 || endY == y + 1;
				}

				if(x % 2 == 1){ // odd column
					if(endX == x - 1 || endX == x + 1){
						return endY == y || endY == y + 1 || endY == y - 1 || endY == y + 2;
					}
                } else { // even column
					if(endX == x - 1 || endX == x + 1){
						return endY == y || endY == y - 1 || endY == y - 2 || endY == y + 1;
					}
                }
                return false;
            }
		}
	}
}
