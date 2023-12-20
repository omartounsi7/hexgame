package com.omar.gui;

import com.omar.model.GameStatus;
import com.omar.model.TurnStatus;
import com.omar.model.World;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MainPanel extends JPanel {
    private final StatusLabel statusLabel;
    private final ModeLabel modeLabel;
    public MainPanel(){
        this.setBackground(Color.BLACK);
        this.setBounds(0,0,475,200);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(new TitleLabel());
        this.statusLabel = new StatusLabel();
        this.modeLabel = new ModeLabel();
        this.add(statusLabel);
        this.add(modeLabel);
        this.add(new SwitchModeButton());
        LineBorder lineBorder = new LineBorder(Color.ORANGE, 2);
        this.setBorder(lineBorder);
    }
    public void updateLabel() {
        if (World.gameStatus == GameStatus.ACTIVE) {
            statusLabel.setText("Game active");
            if(World.aiMode){
                modeLabel.setText("AI mode");
            } else {
                if(World.turnStatus == TurnStatus.P1TURN){
                    modeLabel.setText("Redosia's turn");
                } else if (World.turnStatus == TurnStatus.P2TURN){
                    modeLabel.setText("Greenland's turn");
                }
            }
        } else if (World.gameStatus == GameStatus.P1WINS) {
            statusLabel.setText("Game over");
            modeLabel.setText("Redosia wins!");
        } else {
            statusLabel.setText("Game over");
            modeLabel.setText("Greenland wins!");
        }
    }
}
