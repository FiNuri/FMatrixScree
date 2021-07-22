package com.farid.fmatrixscreen;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice[] graphicsDevices = graphicsEnvironment.getScreenDevices();
            for (GraphicsDevice graphicsDevice: graphicsDevices) {
                graphicsDevice.setFullScreenWindow(new MatrixWindow());
            }
        });
    }
}
