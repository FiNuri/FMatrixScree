package com.farid.fmatrixscreen;

public class MatrixWord {

    private MatrixChar[] matrixChars;
    private int zPosition;
    private int xPosition;

    public MatrixChar[] getMatrixChars() {
        return this.matrixChars;
    }

    public void setMatrixChars(MatrixChar[] matrixChars) {
        this.matrixChars = matrixChars;
    }

    public int getzPosition() {
        return zPosition;
    }

    public void setzPosition(int zPosition) {
        this.zPosition = zPosition;
    }

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }
}
