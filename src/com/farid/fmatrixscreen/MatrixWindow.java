package com.farid.fmatrixscreen;

import javax.swing.*;
import java.awt.*;

public class MatrixWindow extends JFrame {

    public MatrixWindow() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(dimension);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);
        setAlwaysOnTop(true);
        setVisible(true);
        add(new MatrixLogic(dimension));
    }

}
