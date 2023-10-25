package com.omar.model.upperpanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MainPanel extends JPanel {
    public MainPanel(){
        this.setBackground(Color.BLACK);
        this.setBounds(0,0,475,200);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(new TitleLabel());
        this.add(new StatusLabel());
        this.add(new EndTurnButton());
        this.add(new CapitulateButton());

        LineBorder lineBorder = new LineBorder(Color.RED, 2);
        this.setBorder(lineBorder);
    }
}
