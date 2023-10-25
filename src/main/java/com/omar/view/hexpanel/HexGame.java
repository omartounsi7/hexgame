package com.omar.view.hexpanel;

import com.omar.model.faction.Army;
import com.omar.model.faction.Faction;
import com.omar.model.world.Tile;
import com.omar.model.world.TileStatus;
import com.omar.resources.FactionNames;
import com.omar.view.upperpanel.MainPanel;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

import static com.omar.view.hexpanel.Constants.*;

/**********************************
  This is the main class of a Java program to play a game based on hexagonal tiles.
  The mechanism of handling hexes is in the file hexmech.java.

  Written by: M.H.
  Date: December 2012

 ***********************************/



/*
		board[x-1][y] = '<'; // SOUTH EAST
		board[x-1][y-1] = 'n'; // NORTH EAST
		board[x][y-1] = 'N'; // NORTH
		board[x][y] = '0'; // me!
		board[x][y+1] = 'S'; // SOUTH
		board[x+1][y] = 's'; // SOUTH WEST
		board[x+1][y-1] = '>'; // NORTH WEST

 */

public class HexGame {
	Tile[][] board;
	Faction[] factions;
	public HexGame() {
		initGame();
		createFactions();
		createAndShowGUI();
	}
	private static String getRandomFactionName(){
		Random random = new Random();
		int randomFactionIndex = random.nextInt(FactionNames.factionNames.length);
		return FactionNames.factionNames[randomFactionIndex];
	}
	private void createFactions(){
		factions = new Faction[2];
		factions[0] = new Faction(getRandomFactionName(), new LinkedList<>());
		board[0][0].setOccupyingArmy(factions[0].getArmy(0));
		factions[1] = new Faction(getRandomFactionName(), new LinkedList<>());
		board[MAPSIZE - 1][MAPSIZE - 1].setOccupyingArmy(factions[1].getArmy(0));
	}
	private void initGame(){
		HexMech.setXYasVertex(false);
		HexMech.setHeight(TILESIZE);
		HexMech.setBorders(BORDERS);
		board = new Tile[MAPSIZE][MAPSIZE];
		for(int i = 0; i < MAPSIZE; i++){
			for(int y = 0; y < MAPSIZE; y++){
				board[i][y] = new Tile(i * MAPSIZE + y);
			}
		}
		board[0][0].setController(TileStatus.P1OCCUPIED);
		board[MAPSIZE - 1][MAPSIZE - 1].setController(TileStatus.P2OCCUPIED);
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
					HexMech.fillHex(i, j, board[i][j].getController(), g2, fp, board[i][j].isSelected());
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