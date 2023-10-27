package com.omar.model.game;

import com.omar.model.faction.Army;
import com.omar.model.faction.Faction;
import com.omar.model.world.Tile;
import com.omar.model.world.TileStatus;
import com.omar.model.faction.FactionNames;
import com.omar.model.upperpanel.MainPanel;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.event.*;
import java.util.Random;

import static com.omar.model.game.Constants.*;

/**********************************
  This is the main class of a Java program to play a game based on hexagonal tiles.
  The mechanism of handling hexes is in the file hexmech.java.

  Written by: M.H.
  Date: December 2012

 ***********************************/



/*
				g2.setColor(P1ADJCOLOR);
//				g2.fillPolygon(hex(x-1,y));
//				g2.fillPolygon(hex(x-1,y-1));
//				g2.fillPolygon(hex(x, y-1));
//				g2.fillPolygon(hex(x, y+1));
//				g2.fillPolygon(hex(x+1, y));
//				g2.fillPolygon(hex(x+1, y-1));
 */

public class HexGame {
	Tile[][] board;
	Faction[] factions;
	private GameStatus status;
	private Turn whosturn;
	public HexGame() {
		createMap();
		createFactions();
		createAndShowGUI();
		System.out.println("Hello!");
		initGame();
//		play();
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
			if(whosturn == Turn.P1TURN){
				whosturn = Turn.P2TURN;
			} else if(whosturn == Turn.P2TURN){
				whosturn = Turn.P1TURN;
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
		this.whosturn = Turn.P1TURN;
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
				board[i][y] = new Tile(i * MAPSIZE + y);
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
					HexMech.fillHex(i, j, board[i][j].getControllerFaction(), g2, fp);
				}
			}
		}
		class MyMouseListener extends MouseAdapter {	//inner class inside DrawingPanel
			public void mouseClicked(MouseEvent e) {
				Point p = new Point( HexMech.pxtoHex(e.getX(),e.getY()) );
				if (p.x < 0 || p.y < 0 || p.x >= MAPSIZE || p.y >= MAPSIZE){
					return;
				}
				//What do you want to do when a hexagon is clicked?
				System.out.println(p.x + ", " + p.y + " was clicked.");
				System.out.println("It corresponds to Tile " + board[p.x][p.y]);
				board[p.x][p.y].setSelected(true);
				repaint();
			}
		}
	}
}