package com.omar.gui;

import com.omar.model.World;

import javax.swing.*;
import java.awt.*;

public class EndTurnButton extends JButton {
    EndTurnButton(){
        this.setText("End turn");
        this.setFocusable(false);
        this.addActionListener(e -> {System.out.println("End Turn!");
//            World.numberOfMoves = 0;
        });
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}
