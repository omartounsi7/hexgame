package com.omar.gui;

import com.omar.model.GameStatus;
import com.omar.model.TurnStatus;
import com.omar.model.World;

import javax.swing.*;
import java.awt.*;

public class SwitchModeButton extends JButton {
    SwitchModeButton(){
        this.setText("Switch to 2 players");
        this.setFocusable(false);
        this.addActionListener(e -> {System.out.println("Switched game mode");
            if(World.selectedTile == null){
                World.aiMode = false;
                World.turnStatus = TurnStatus.P2TURN;
                this.setEnabled(false);
            }
        });
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}
