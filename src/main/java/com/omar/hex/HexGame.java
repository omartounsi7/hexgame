package com.omar.hex;

import com.omar.model.Army;
import com.omar.model.Faction;
import com.omar.resources.FactionNames;
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
	Tile[][] board;
	Faction[] factions;
	private GameStatus status;
	private TurnStatus whoseturn;
	private Tile selectedTile;
	public HexGame() {
		initGame();
		createMap();
		createFactions();
		createAndShowGUI();
	}

	private void makeMove(){
//		Faction currPlayer = factions[1];
//		Faction otherPlayer = factions[0];
//		TileStatus currTileOccupier = TileStatus.P2OCCUPIED;
//		if(whosturn == Turn.P1TURN){
//			currPlayer = factions[0];
//			otherPlayer = factions[1];
//			currTileOccupier = TileStatus.P1OCCUPIED;
//			System.out.println("Player 1's turn.");
//		} else if(whosturn == Turn.P2TURN){
//			System.out.println("Player 2's turn.");
//		}
//		System.out.println(currPlayer);
//
//		// Temporary game end condition.
//		if(currPlayer.getArmies().isEmpty()){
//			System.out.println("You have lost all your armies!");
//			if(currTileOccupier == TileStatus.P2OCCUPIED){
//				status = GameStatus.P1WINS;
//			} else {
//				status = GameStatus.P2WINS;
//			}
//			return;
//		}
//
//		int armyChoice;
//		while (true) {
//			System.out.println("Enter an army's number to select it.");
//			System.out.println("Enter -1 to skip your move.");
//			if (scanner.hasNextInt()) {
//				armyChoice = scanner.nextInt();
//				if (armyChoice >= 1 && armyChoice <= currPlayer.getArmies().size()) {
//					break;
//				} else if(armyChoice == -1) {
//					return;
//				} else {
//					System.out.println("Invalid input!");
//				}
//			} else {
//				scanner.next();
//				System.out.println("Invalid input!");
//			}
//		}
//
//		Army selectedArmy = currPlayer.getArmy(armyChoice - 1);
//		System.out.println("You have selected army #" + armyChoice + ": " + selectedArmy);
//		int armyPosition = selectedArmy.getPosition();
//		Set<Integer> possibleMoves = world.getTileNeighbors(armyPosition);
//		System.out.println("Possible new positions are: " + possibleMoves);
//
//		int posChoice;
//		while (true) {
//			System.out.println("Enter the new position of army #" + armyChoice);
//			if (scanner.hasNextInt()) {
//				posChoice = scanner.nextInt();
//				if (possibleMoves.contains(posChoice)) {
//					break;
//				} else {
//					System.out.println("Invalid input!");
//				}
//			} else {
//				scanner.next();
//				System.out.println("Invalid input!");
//			}
//		}
//
//		world.getTile(armyPosition).setStatus(TileStatus.EMPTY);
//
//		if(world.getTile(posChoice).getStatus() == TileStatus.EMPTY){ // Tile is empty, move there.
//			selectedArmy.setPosition(posChoice);
//			world.getTile(posChoice).setStatus(currTileOccupier);
//		} else if(world.getTile(posChoice).getStatus() == currTileOccupier){ // Tile is occupied by allies.
//			java.util.List<Army> armies = currPlayer.getArmies();
//			int size = armies.size();
//			for (int i = 0 ; i < size ; i++) {
//				if(armies.get(i).getPosition() == posChoice){
//					int firepower = armies.get(i).getFirepower();
//					selectedArmy.setFirepower(selectedArmy.getFirepower() + firepower);
//					armies.remove(i);
//					selectedArmy.setPosition(posChoice);
//					break;
//				}
//			}
//		} else { // // Tile is occupied by enemies. Combat.
//			List<Army> enemyArmies = otherPlayer.getArmies();
//			int size = enemyArmies.size();
//			for(int i = 0 ; i < size ; i++) {
//				if(enemyArmies.get(i).getPosition() == posChoice){
//					int firepower = selectedArmy.getFirepower();
//					int enemyFirepower = enemyArmies.get(i).getFirepower();
//
//					if(enemyFirepower > firepower){
//						enemyArmies.get(i).setFirepower(enemyFirepower - firepower);
//						currPlayer.getArmies().remove(selectedArmy);
//					} else if (enemyFirepower < firepower) {
//						selectedArmy.setFirepower(firepower - enemyFirepower);
//						enemyArmies.remove(i);
//						selectedArmy.setPosition(posChoice);
//					} else {
//						currPlayer.getArmies().remove(selectedArmy);
//						enemyArmies.remove(i);
//					}
//					break;
//				}
//			}
//		}
	}
	public void play(){
		while(status == GameStatus.ACTIVE){
			makeMove();
			System.out.println("hey!");
			if(whoseturn == TurnStatus.P1TURN){
				whoseturn = TurnStatus.P2TURN;
			} else if(whoseturn == TurnStatus.P2TURN){
				whoseturn = TurnStatus.P1TURN;
			}
		}
		if(status == GameStatus.P1WINS){
			System.out.println(factions[0].getName() + " has won!");
		} else if(status == GameStatus.P2WINS) {
			System.out.println(factions[1].getName() + " has won!");
		}
		System.out.println("Game over.");
	}
	private void initGame(){
		this.status = GameStatus.ACTIVE;
		this.whoseturn = TurnStatus.P1TURN;
		this.selectedTile = null;
	}
	private static String getRandomFactionName(){
		Random random = new Random();
		int randomFactionIndex = random.nextInt(FactionNames.factionNames.length);
		return FactionNames.factionNames[randomFactionIndex];
	}
	private void createFactions(){
		factions = new Faction[2];
		factions[0] = new Faction(getRandomFactionName());
		board[0][0].setOccupyingArmy(new Army(10, 0));
		factions[1] = new Faction(getRandomFactionName());
		board[MAPSIZE - 1][MAPSIZE - 1].setOccupyingArmy(new Army(10, 1));
	}
	private void createMap(){
		HexMech.setXYasVertex(false);
		HexMech.setHeight(TILESIZE);
		HexMech.setBorders(BORDERS);
		board = new Tile[MAPSIZE][MAPSIZE];
		for(int i = 0; i < MAPSIZE; i++){
			for(int y = 0; y < MAPSIZE; y++){
				board[i][y] = new Tile(i , y);
			}
		}
		board[0][0].setControllerFaction(TileStatus.P1OCCUPIED);
		board[MAPSIZE - 1][MAPSIZE - 1].setControllerFaction(TileStatus.P2OCCUPIED);
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
		frame.add(new MainPanel(), BorderLayout.NORTH);
	}
	public class DrawingPanel extends JPanel {
		public DrawingPanel() {
			setBackground(BGCOLOR);
			LineBorder lineBorder = new LineBorder(Color.RED, 2);
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
					String fp = "";
					if(occArmy != null){
						fp = String.valueOf(occArmy.getFirepower());
					}

//					HexMech.fillHex(i, j, board[i][j].getControllerFaction(), g2, fp);

					HexMech.fillHex(i, j, board[i][j].getControllerFaction(), g2, i + " " + j);
				}
			}
		}
		class MyMouseListener extends MouseAdapter {	//inner class inside DrawingPanel
			public void mouseClicked(MouseEvent e) {
				Point p = new Point( HexMech.pxtoHex(e.getX(),e.getY()) );
				if (p.x < 0 || p.y < 0 || p.x >= MAPSIZE || p.y >= MAPSIZE){
					return;
				}



				if(whoseturn == TurnStatus.P1TURN){
					System.out.println("It is P1's turn");
				} else if(whoseturn == TurnStatus.P2TURN){
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
					if(whoseturn == TurnStatus.P1TURN){ // check whose turn it is
						if(startTile.getControllerFaction() == TileStatus.P1OCCUPIED){ // check if tile belongs to P1
//							System.out.println("You have clicked a tile that belongs to p1");
							selectedTile = startTile;
						}
					} else if(whoseturn == TurnStatus.P2TURN){
						if(startTile.getControllerFaction() == TileStatus.P2OCCUPIED){ // check if tile belongs to P2
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
					Army toMove = selectedTile.getOccupyingArmy();
					selectedTile.setOccupyingArmy(null);
					selectedTile.setControllerFaction(TileStatus.EMPTY);
					endTile.setOccupyingArmy(toMove);
					if(whoseturn == TurnStatus.P1TURN){
						endTile.setControllerFaction(TileStatus.P1OCCUPIED);
						whoseturn = TurnStatus.P2TURN;
					} else if(whoseturn == TurnStatus.P2TURN){
						endTile.setControllerFaction(TileStatus.P2OCCUPIED);
						whoseturn = TurnStatus.P1TURN;
					}
					selectedTile = null;
					System.out.println("You have moved to " + endTile);
				} else {
					System.out.println("Incorrect destination!");
				}
			}

			public boolean areAdjacent(int endX, int endY, int x, int y){
				if(x % 2 == 1){ // odd column
					if(endX == x - 1){
						return endY == y || endY == y + 1;
					} else if (endX == x){
						return endY == y - 1 || endY == y + 1;
					} else if (endX == x + 1){
						return endY == y || endY == y + 1;
					}



					return false;
				} else {
					if(endX == x - 1){
						return endY == y || endY == y - 1;
					} else if (endX == x){
						return endY == y - 1 || endY == y + 1;
					} else if (endX == x + 1){
						return endY == y || endY == y - 1;
					}
					return false;
				}


			}
		}
	}
}
