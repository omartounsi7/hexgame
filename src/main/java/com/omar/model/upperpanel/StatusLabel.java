package com.omar.model.upperpanel;

import javax.swing.*;
import java.awt.*;

public class StatusLabel extends JLabel {
    StatusLabel(){
        this.setText("Player 1's turn");
        this.setForeground(Color.RED);
        this.setFont(new Font("Arial", Font.PLAIN, 36));
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}
