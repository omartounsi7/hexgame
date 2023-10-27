package com.omar.gui;

import com.omar.hex.GameStatus;
import com.omar.hex.HexGame;
import com.omar.hex.TurnStatus;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MainPanel extends JPanel {
    private final StatusLabel statusLabel;
    public MainPanel(){
        this.setBackground(Color.BLACK);
        this.setBounds(0,0,475,200);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(new TitleLabel());
        this.statusLabel = new StatusLabel();
        this.add(statusLabel);
        this.add(new EndTurnButton());
        this.add(new CapitulateButton());

        LineBorder lineBorder = new LineBorder(Color.ORANGE, 2);
        this.setBorder(lineBorder);
    }

    public void updateLabel() {
        if (HexGame.status == GameStatus.ACTIVE) {
            if (HexGame.whosturn == TurnStatus.P1TURN) {
                statusLabel.setText("Redosia's move.");
            } else {
                statusLabel.setText("Greenland's move.");
            }
        } else if (HexGame.status == GameStatus.P1WINS) {
            statusLabel.setText("Redosia wins!");
        } else {
            statusLabel.setText("Greenland wins!");
        }
    }
}
