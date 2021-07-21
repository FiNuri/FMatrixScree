package com.farid.fmatrixscreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
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
        startRain();
    }

    private void startRain() {
        Timer timer = new Timer(this.wordsRainSpeedMs, this);
        timer.start();
    }

    private void initFont() {
        try {
            this.matrixFont = Font.createFont(Font.TRUETYPE_FONT, this.getClass()
                    .getResourceAsStream("font/matrixCodeNfi.ttf")).deriveFont(Font.BOLD + Font.ITALIC);
        } catch (FontFormatException | IOException ignored) {
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
        String randomChar;
        if (this.firsCharChangeTimer2 >= 3) {
            for (MatrixWord matrixWord : verticalWordsOnScree) {
                for (int j = 0; j < matrixWord.getMatrixChars().length; j++) {
                    randomChar = String.valueOf(alphabet[randomizer.nextInt(alphabet.length - 1)]);
                    matrixWord.getMatrixChars()[0].setMatrixChar(randomChar);
                }
            }
            this.firsCharChangeTimer2 = 0;
        }
        if (this.firsCharChangeTimer >= this.charChangeBound) {
            for (MatrixWord matrixWord : verticalWordsOnScree) {
                for (int j = 0; j < matrixWord.getMatrixChars().length; j++) {
                    randomChar = String.valueOf(alphabet[randomizer.nextInt(alphabet.length - 1)]);
                    matrixWord.getMatrixChars()[1].setMatrixChar(randomChar);
                }
            }
            this.firsCharChangeTimer = 0;
        }
    }

    private void moveWordsDown() {
        int yPosition, nextYPosition, lastCharYPosition, lastCharPositionInArr;
        String nextChar;
        this.firsCharChangeTimer++;
        this.firsCharChangeTimer2++;
        for (int i = 0; i < this.verticalWordsOnScree.length; i++) {
            lastCharPositionInArr = this.verticalWordsOnScree[i].getMatrixChars().length - 1;
            lastCharYPosition = this.verticalWordsOnScree[i].getMatrixChars()[lastCharPositionInArr].getyPosition();
            for (int j = lastCharPositionInArr; j >= 0; j--) {
                yPosition = this.verticalWordsOnScree[i].getMatrixChars()[j].getyPosition();
                nextYPosition = yPosition + 1;
                this.verticalWordsOnScree[i].getMatrixChars()[j].setyPosition(nextYPosition);
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
        matrixWord.setzPosition(this.randomizer.nextInt(5) + 1);
        matrixWord.setMatrixChars(generateChars());
        return matrixWord;
    }


    private MatrixChar[] generateChars() {
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
                color = this.colorHolder.getColors()[0];
            } else if (i < beginColor2) {
                color = this.colorHolder.getColors()[1];
            } else if (i < beginColor3) {
                color = this.colorHolder.getColors()[2];
            } else if (i < middleColor) {
                color = this.colorHolder.getColors()[3];
            } else if (i >= middleColor && i < endColor1) {
                color = this.colorHolder.getColors()[4];
            } else if (i >= endColor1 && i < endColor2) {
                color = this.colorHolder.getColors()[5];
            } else if (i >= endColor2 && i < endColor3) {
                color = this.colorHolder.getColors()[6];
            } else if (i >= endColor3) {
                color = this.colorHolder.getColors()[7];
            }
            matrixChar.setyPosition(startYPosition);
            matrixChar.setCharColor(color);
            wordChars[i] = matrixChar;
        }
        return wordChars;
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
