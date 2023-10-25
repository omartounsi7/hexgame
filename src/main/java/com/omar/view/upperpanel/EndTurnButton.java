package com.omar.view.upperpanel;

import javax.swing.*;
import java.awt.*;

public class EndTurnButton extends JButton {
    EndTurnButton(){
        this.setText("End turn");
        this.setFocusable(false);
        this.addActionListener(e -> System.out.println("End Turn!"));
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}
