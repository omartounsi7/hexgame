package com.omar.gui;

import javax.swing.*;
import java.awt.*;

public class TitleLabel extends JLabel {
    TitleLabel(){
        this.setText("HexWars 0.1");
        this.setForeground(Color.ORANGE);
        this.setFont(new Font("Arial", Font.BOLD, 48));
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}
