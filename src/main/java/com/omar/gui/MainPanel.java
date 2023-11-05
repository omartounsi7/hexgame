package com.omar.gui;

import com.omar.model.GameStatus;
import com.omar.model.TurnStatus;
import com.omar.model.World;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MainPanel extends JPanel {
    private final StatusLabel statusLabel;
    private final MovesLabel movesLabel;
    public MainPanel(){
        this.setBackground(Color.BLACK);
        this.setBounds(0,0,475,200);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(new TitleLabel());
        this.statusLabel = new StatusLabel();
        this.movesLabel = new MovesLabel();
        this.add(statusLabel);
        this.add(movesLabel);
        this.add(new EndTurnButton());
        this.add(new CapitulateButton());
        LineBorder lineBorder = new LineBorder(Color.ORANGE, 2);
        this.setBorder(lineBorder);
    }
    public void updateLabel() {
        if (World.status == GameStatus.ACTIVE) {
            if (World.whosturn == TurnStatus.P1TURN) {
                statusLabel.setText("Redosia's move.");
            } else {
                statusLabel.setText("Greenland's move.");
            }
            movesLabel.setText("Turns remaining: " + World.numberOfMoves);
        } else if (World.status == GameStatus.P1WINS) {
            statusLabel.setText("Redosia wins!");
        } else {
            statusLabel.setText("Greenland wins!");
        }
    }
}
