package com.omar.gui;

import javax.swing.*;
import java.awt.*;

public class StatusLabel extends JLabel {
    StatusLabel(){
        this.setForeground(Color.ORANGE);
        this.setFont(new Font("Arial", Font.PLAIN, 36));
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}
