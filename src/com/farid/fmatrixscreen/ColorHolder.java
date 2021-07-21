package com.farid.fmatrixscreen;

import java.awt.*;

public class ColorHolder {
    private Color[] colors;

    public ColorHolder() {
        this.colors = new Color[]{
                new Color(233, 255, 236),
                new Color(182, 250, 193),
                new Color(59, 255, 115),
                new Color(0, 255, 65),
                new Color(0, 143, 17),
                new Color(0, 71, 12),
                new Color(1, 35, 1),
                new Color(1, 12, 1)
        };
    }

    public Color[] getColors() {
        return colors;
    }
}
