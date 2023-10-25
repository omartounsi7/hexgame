package com.omar.view.hexpanel;

import com.omar.view.upperpanel.MainPanel;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.event.*; 

/**********************************
  This is the main class of a Java program to play a game based on hexagonal tiles.
  The mechanism of handling hexes is in the file hexmech.java.

  Written by: M.H.
  Date: December 2012

 ***********************************/

public class hexgame
{
  public hexgame() {
		initGame();
		createAndShowGUI();
	}
	//constants and global variables
	final static Color COLOURBACK =  Color.BLACK;
	final static Color COLOURCELL =  Color.RED;
	final static Color COLOURGRID =  Color.BLACK;
	final static Color COLOURONE = new Color(255,255,255,200);
	final static Color COLOURONETXT = Color.BLUE;
	final static Color COLOURTWO = new Color(0,0,0,200);
	final static Color COLOURTWOTXT = new Color(255,100,255);
	final static int EMPTY = 0;
	final static int BSIZE = 8;
	final static int HEXSIZE = 60;
	final static int BORDERS = 15;  
	final static int SCRSIZE = HEXSIZE * (BSIZE + 1) + BORDERS*3; //screen size (vertical dimension).
	int[][] board = new int[BSIZE][BSIZE];

	void initGame(){
		hexmech.setXYasVertex(false); //RECOMMENDED: leave this as FALSE.
		hexmech.setHeight(HEXSIZE); //Either setHeight or setSize must be run to initialize the hex
		hexmech.setBorders(BORDERS);

		for (int i=0;i<BSIZE;i++) {
			for (int j=0;j<BSIZE;j++) {
				board[i][j]=EMPTY;
			}
		}

		// Init map
		board[3][4] = '<'; // SOUTH EAST
		board[3][3] = 'n'; // NORTH EAST
		board[4][3] = 'N'; // NORTH
		board[4][4] = '0'; // me!
		board[4][5] = 'S'; // SOUTH
		board[5][4] = 's'; // SOUTH WEST
		board[5][3] = '>'; // NORTH WEST

	}

	private void createAndShowGUI()
	{
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
	class DrawingPanel extends JPanel
	{
		public DrawingPanel()
		{
			setBackground(COLOURBACK);
			LineBorder lineBorder = new LineBorder(Color.RED, 2);
			this.setBorder(lineBorder);
			MyMouseListener ml = new MyMouseListener();            
			addMouseListener(ml);
		}

		public void paintComponent(Graphics g)
		{
			Graphics2D g2 = (Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			super.paintComponent(g2);
			//draw grid
			for (int i=0;i<BSIZE;i++) {
				for (int j=0;j<BSIZE;j++) {
					hexmech.drawHex(i,j,g2);
				}
			}
			//fill in hexes
			for (int i=0;i<BSIZE;i++) {
				for (int j=0;j<BSIZE;j++) {
					hexmech.fillHex(i,j,board[i][j],g2);
				}
			}
		}

		class MyMouseListener extends MouseAdapter	{	//inner class inside DrawingPanel 
			public void mouseClicked(MouseEvent e) { 
				int x = e.getX(); 
				int y = e.getY();
				Point p = new Point( hexmech.pxtoHex(e.getX(),e.getY()) );
				if (p.x < 0 || p.y < 0 || p.x >= BSIZE || p.y >= BSIZE) return;

				//What do you want to do when a hexagon is clicked?
				board[p.x][p.y] = 'X';
				repaint();
			}		 
		}
	}
}