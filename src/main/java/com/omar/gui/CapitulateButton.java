package com.omar.gui;

import com.omar.model.GameStatus;
import com.omar.model.World;

import javax.swing.*;
import java.awt.*;

public class CapitulateButton extends JButton {
    CapitulateButton(){
        this.setText("Capitulate");
        this.setFocusable(false);
        this.addActionListener(e -> {
            System.out.println("Cap!");
            World.status = GameStatus.P2WINS;
        });
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}
