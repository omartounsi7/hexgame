package com.omar;

import com.omar.model.game.HexGame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(HexGame::new);
    }
}