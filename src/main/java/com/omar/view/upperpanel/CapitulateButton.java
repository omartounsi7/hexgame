package com.omar.view.upperpanel;

import javax.swing.*;
import java.awt.*;

public class CapitulateButton extends JButton {
    CapitulateButton(){
        this.setText("Capitulate");
        this.setFocusable(false);
        this.addActionListener(e -> System.out.println("Cap!"));
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}
