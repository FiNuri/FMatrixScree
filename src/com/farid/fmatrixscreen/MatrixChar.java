package com.farid.fmatrixscreen;

import java.awt.*;

public class MatrixChar {
    private String matrixChar;
    private Color charColor;
    private int yPosition;

    public MatrixChar() {}

    public MatrixChar(String matrixChar,Color charColor , int yPosition) {
        this.matrixChar = matrixChar;
        this.charColor = charColor;
        this.yPosition = yPosition;
    }

    public Color getCharColor() {
        return charColor;
    }

    public void setCharColor(Color charColor) {
        this.charColor = charColor;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public String getMatrixChar() {
        return matrixChar;
    }

    public void setMatrixChar(String matrixChar) {
        this.matrixChar = matrixChar;
    }
}
