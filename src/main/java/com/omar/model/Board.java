package com.omar.model;


public class Board implements Cloneable {
    private Tile[][] board;

    public Board(Tile[][] board) {
        this.board = board;
    }

    public Tile[][] getBoard() {
        return board;
    }

    @Override
    public Board clone() {
        int numRows = board.length;
        int numColumns = board[0].length;

        Tile[][] clonedBoard = new Tile[numRows][numColumns];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                clonedBoard[i][j] = board[i][j].clone();
            }
        }

        return new Board(clonedBoard);
    }
}
