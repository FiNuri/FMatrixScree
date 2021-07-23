package com.farid.fmatrixscreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class MatrixLogic extends JPanel implements ActionListener {

    private final Dimension dimension;
    private final Random randomizer;
    private final ColorHolder colorHolder;
    private final int wordsRainSpeedMs;
    private final int charChangeBound;
    private final int cellLength;
    private final char[] alphabet;
    private MatrixWord[] verticalWordsOnScree;
    private Font matrixFont;
    private int firsCharChangeTimer;
    private int firsCharChangeTimer2;
    private int[] yCounter;
    private final boolean started;

    public MatrixLogic(Dimension dimension) {
        setBackground(Color.BLACK);
        setSize(dimension);
        setFocusable(true);
        this.dimension = dimension;
        this.randomizer = new Random();
        this.wordsRainSpeedMs = 10;
        this.firsCharChangeTimer = 0;
        this.firsCharChangeTimer2 = 0;
        this.charChangeBound = 6;
        this.cellLength = 12;
        this.alphabet = new MatrixAlphabet().getAlphabet();
        this.colorHolder = new ColorHolder();
        this.started = true;
        addKeyListener(new KeyListener());
        initFont();
        initWordsCount();
        initMatrixWords();
        initCounter();
        startRain();
    }

    private void startRain() {
        Timer timer = new Timer(this.wordsRainSpeedMs, this);
        timer.start();
    }

    private void initFont() {
        try {
            this.matrixFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(this.getClass()
                    .getResourceAsStream("font/matrixCodeNfi.ttf"))).deriveFont(Font.BOLD + Font.ITALIC);
        } catch (FontFormatException | IOException | NullPointerException ignored) {
            System.exit(1);
        }
    }

    private void initWordsCount() {
        int count = this.dimension.width / this.cellLength;
        this.verticalWordsOnScree = new MatrixWord[count];
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        moveWordsDown();
        changeFirsCharOfWord();
        repaint();
    }

    private void changeFirsCharOfWord() {
        if (this.firsCharChangeTimer2 >= 3) {
            changeChar(0);
            this.firsCharChangeTimer2 = 0;
        }
        if (this.firsCharChangeTimer >= this.charChangeBound) {
            changeChar(1);
            this.firsCharChangeTimer = 0;
        }
    }

    private void changeChar(int position) {
        String randomChar;
        for (MatrixWord matrixWord : verticalWordsOnScree) {
            for (int j = 0; j < matrixWord.getMatrixChars().length; j++) {
                randomChar = String.valueOf(alphabet[randomizer.nextInt(alphabet.length - 1)]);
                matrixWord.getMatrixChars()[position].setMatrixChar(randomChar);
            }
        }
    }

    private void moveWordsDown() {
        int lastCharYPosition, lastCharPositionInArr, zPosition;
        boolean[] worked = {false, false, false, false, false};
        String nextChar;
        this.firsCharChangeTimer++;
        this.firsCharChangeTimer2++;
        for (int i = 0; i < this.verticalWordsOnScree.length; i++) {
            zPosition = this.verticalWordsOnScree[i].getzPosition();
            lastCharPositionInArr = this.verticalWordsOnScree[i].getMatrixChars().length - 1;
            lastCharYPosition = this.verticalWordsOnScree[i].getMatrixChars()[lastCharPositionInArr].getyPosition();
            for (int j = lastCharPositionInArr; j >= 0; j--) {
                if (zPosition == 1) {
                    moveYPositionToNext(i, j);
                } else if (zPosition == 2 && this.yCounter[0] == 2) {
                    moveYPositionToNext(i, j);
                    worked[0] = true;
                } else if (zPosition == 3 && this.yCounter[1] == 3) {
                    moveYPositionToNext(i, j);
                    worked[1] = true;
                } else if (zPosition == 4 && this.yCounter[2] == 4) {
                    moveYPositionToNext(i, j);
                    worked[2] = true;
                } else if (zPosition == 5 && this.yCounter[3] == 5) {
                    moveYPositionToNext(i, j);
                    worked[3] = true;
                } else if (zPosition == 6 && this.yCounter[4] == 6) {
                    moveYPositionToNext(i, j);
                    worked[4] = true;
                }
                if (this.firsCharChangeTimer >= this.charChangeBound) {
                    if (j - 1 > 0) {
                        nextChar = this.verticalWordsOnScree[i].getMatrixChars()[j - 1].getMatrixChar();
                        this.verticalWordsOnScree[i].getMatrixChars()[j].setMatrixChar(nextChar);
                    }
                }
            }
            if (lastCharYPosition >= this.dimension.height) {
                changeMatrixWordToNew(i);
            }
        }
        nullifyCounter(worked);
        increaseYCounter();
    }

    private void increaseYCounter() {
        for (int i = 0; i < this.yCounter.length; i++) {
            this.yCounter[i] = this.yCounter[i] + 1;
        }
    }

    private void nullifyCounter(boolean[] worked) {
        for (int i = 0; i < worked.length; i++) {
            if (worked[i]) {
                this.yCounter[i] = 1;
            }
        }
    }

    private void moveYPositionToNext(int wordNumber, int charNumber) {
        int yPosition = this.verticalWordsOnScree[wordNumber].getMatrixChars()[charNumber].getyPosition();
        int nextYPosition = yPosition + 1;
        this.verticalWordsOnScree[wordNumber].getMatrixChars()[charNumber].setyPosition(nextYPosition);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (started) {
            for (MatrixWord matrixWord : this.verticalWordsOnScree) {
                for (int j = 0; j < matrixWord.getMatrixChars().length; j++) {
                    MatrixChar matrixChar = matrixWord.getMatrixChars()[j];
                    g.setFont(retrieveFont(matrixWord.getzPosition()));
                    g.setColor(matrixChar.getCharColor());
                    g.drawString(matrixChar.getMatrixChar(), matrixWord.getxPosition(),
                            matrixChar.getyPosition());
                }
            }
        }
    }

    private Font retrieveFont(int ZPosition) {
        Font font = null;
        switch (ZPosition) {
            case 1:
                font = this.matrixFont.deriveFont(16f);
                break;
            case 2:
                font = this.matrixFont.deriveFont(14f);
                break;
            case 3:
                font = this.matrixFont.deriveFont(12f);
                break;
            case 4:
                font = this.matrixFont.deriveFont(10f);
                break;
            case 5:
                font = this.matrixFont.deriveFont(8f);
                break;
            case 6:
                font = this.matrixFont.deriveFont(6f);
                break;
        }
        return font;
    }

    private void changeMatrixWordToNew(int wordPosition) {
        this.verticalWordsOnScree[wordPosition] = generateMatrixWords(wordPosition);
    }

    private void initMatrixWords() {
        for (int i = 0; i < this.verticalWordsOnScree.length; i++) {
            this.verticalWordsOnScree[i] = generateMatrixWords(i);
        }
    }

    private MatrixWord generateMatrixWords(int wordPosition) {
        MatrixWord matrixWord = new MatrixWord();
        if (wordPosition > 0) {
            matrixWord.setxPosition(this.verticalWordsOnScree[wordPosition - 1].getxPosition() + this.cellLength);
        } else {
            matrixWord.setxPosition(0);
        }
        int zPosition = this.randomizer.nextInt(5) + 1;
        matrixWord.setzPosition(zPosition);
        matrixWord.setMatrixChars(generateChars(zPosition));
        return matrixWord;
    }


    private MatrixChar[] generateChars(int zPosition) {
        Color color = null;
        int wordLength = this.randomizer.nextInt(93) + 7;
        int startYPosition = 1;
        int beginColor2 = 2, beginColor3, middleColor, endColor1, endColor2, endColor3;
        int third, halfOfThird, thirdOfThird;
        MatrixChar[] wordChars = new MatrixChar[wordLength];
        third = (wordLength - 1) / 3;
        halfOfThird = (third - 1) / 2;
        thirdOfThird = third / 3;
        beginColor3 = beginColor2 + halfOfThird;
        middleColor = beginColor3 + halfOfThird;
        endColor1 = middleColor + third;
        endColor2 = endColor1 + thirdOfThird;
        endColor3 = endColor2 + thirdOfThird;
        for (int i = 0; i < wordChars.length; i++) {
            MatrixChar matrixChar = new MatrixChar();
            matrixChar.setMatrixChar("1");
            if (i > 0) {
                startYPosition -= 16;
            }
            if (i == 0) {
                color = retrieveColor(zPosition, 0);
            } else if (i < beginColor2) {
                color = retrieveColor(zPosition, 1);
            } else if (i < beginColor3) {
                color = retrieveColor(zPosition, 2);
            } else if (i < middleColor) {
                color = retrieveColor(zPosition, 3);
            } else if (i < endColor1) {
                color = retrieveColor(zPosition, 4);
            } else if (i < endColor2) {
                color = retrieveColor(zPosition, 5);
            } else if (i < endColor3) {
                color = retrieveColor(zPosition, 6);
            } else if (i >= endColor3) {
                color = retrieveColor(zPosition, 7);
            }
            matrixChar.setyPosition(startYPosition);
            matrixChar.setCharColor(color);
            wordChars[i] = matrixChar;
        }
        return wordChars;
    }

    private Color retrieveColor(int zPosition, int colorNumber) {
        Color color = null;
        switch (zPosition) {
            case 1:
                color = this.colorHolder.getColorsZ1()[colorNumber];
                break;
            case 2:
                color = this.colorHolder.getColorsZ2()[colorNumber];
                break;
            case 3:
                color = this.colorHolder.getColorsZ3()[colorNumber];
                break;
            case 4:
                color = this.colorHolder.getColorsZ4()[colorNumber];
                break;
            case 5:
                color = this.colorHolder.getColorsZ5()[colorNumber];
                break;
            case 6:
                color = this.colorHolder.getColorsZ6()[colorNumber];
                break;
        }
        return color;
    }

    private void initCounter() {
        this.yCounter = new int[]{1, 1, 1, 1, 1};
    }

    private void closeScreen() {
        System.exit(0);
    }

    class KeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_ESCAPE) {
                closeScreen();
            }
        }
    }
}
