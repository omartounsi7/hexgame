package com.omar.gui;

import javax.swing.*;
import java.awt.*;

public class TitleLabel extends JLabel {
    TitleLabel(){
        this.setText("HexWars 1.0");
        this.setForeground(Color.RED);
        this.setFont(new Font("Arial", Font.BOLD, 48));
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}
