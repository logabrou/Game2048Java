package com.codegym.games.game2048;
import com.codegym.engine.cell.*;

public class Game2048 extends Game {

    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped = false;

    private void createGame() {
        createNewNumber();
        createNewNumber();
    }

    private void drawScene() {
        for (int x = 0; x < SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                int boardValue = gameField[y][x];
                setCellColoredNumber(x, y, boardValue);
            }
        }
    }

    private void createNewNumber() {
        int maxValue = getMaxTileValue();
        if (maxValue == 2048) {
            win();
        }
        int x = getRandomNumber(SIDE);
        int y = getRandomNumber(SIDE);
        if (gameField[x][y] == 0) {
            int num = getRandomNumber(10);
            if (num == 9) {
                gameField[x][y] = 4;
            }else {
                gameField[x][y] = 2;
            }
        }else {
            createNewNumber();
        }

    }

    @Override
    public void onKeyPress(Key key) {
        if (key == Key.LEFT) {
            moveLeft();
            drawScene();
        } else if (key == Key.RIGHT) {
            moveRight();
            drawScene();
        } else if (key == Key.UP) {
            moveUp();
            drawScene();
        } else if (key == Key.DOWN) {
            moveDown();
            drawScene();
        }
    }



    private void moveLeft() {
        boolean isNewNumberNeeded = false;
        for (int x = 0; x < gameField.length; x++) {
            boolean compress = compressRow(gameField[x]);
            boolean merge = mergeRow(gameField[x]);
            if (merge == true) {
                compressRow(gameField[x]);
            }
            if (compress == true || merge == true) {
                isNewNumberNeeded = true;
            }
        }
        if (isNewNumberNeeded == true) {
            createNewNumber();
        }
    }

    private void moveRight() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }

    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();

    }

    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();

    }

    private int getMaxTileValue() {
        int maximum = gameField[0][0];
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (gameField[i][j] > maximum) {
                    maximum = gameField[i][j];
                }
            }
        }
        return maximum;
    }

    private void win() {
        this.isGameStopped = true;
        showMessageDialog(Color.ORANGE, "You Won!", Color.BLUE, 100);
    }

    private boolean canUserMove() {
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (gameField[i][j] == 0){
                    return true;
                }
            }
        }
        for (int i = 0; i < SIDE-1; i++) {
            for (int j = 0; j < SIDE-1; j++) {
                if (gameField[i][j] == gameField[i+1][j] || gameField[i][j] == gameField[i][j+1]){
                    return true;
                }
            }
        }
        return false;
    }

    private void rotateClockwise() {
        int[][] rotatedMatrix = new int[SIDE][SIDE];
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                rotatedMatrix[j][(SIDE-1)-i] = gameField[i][j];
            }
        }
        gameField = rotatedMatrix;
    }

    private boolean mergeRow(int[] row) {
        int[] tempRow = row.clone();
        for (int x = 0; x < row.length - 1; x++) {
            if (row[x] == row[x+1] && row[x] != 0) {
                int tempItem = row[x] + row[x+1];
                row[x+1] = 0;
                row[x] = tempItem;
            }
        }
        for (int i = 0; i < row.length; i++) {
            if (row[i] != tempRow[i]) {
                return true;
            }
        }
        return false;
    }

    private boolean compressRow(int[] row) {
        int[] tempRow = row.clone();
        for (int y = 0; y < row.length; y++) {
            for (int z = 0; z < row.length - y - 1; z++) {
                if (row[z] < 2) {
                    int tempItem = row[z];
                    row[z] = row[z+1];
                    row[z+1] = tempItem;
                }
            }
        }
        for (int x = 0; x < row.length; x++) {
            if (row[x] != tempRow[x]) {
                return true;
            }
        }
        return false;
    }


    private Color getColorByValue(int cellValue) {
        Color color = Color.WHITE;
        switch(cellValue) {
            case 0: color = Color.WHITE; break;
            case 2: color = Color.CYAN; break;
            case 4: color = Color.BLUE; break;
            case 8: color = Color.GREEN; break;
            case 16: color = Color.RED; break;
            case 32: color = Color.MAGENTA; break;
            case 64: color = Color.BLACK; break;
            case 128: color = Color.PINK; break;
            case 256: color = Color.PURPLE; break;
            case 512: color = Color.ORANGE; break;
            case 1024: color = Color.YELLOW; break;
            case 2048: color = Color.GRAY; break;
        }
        return color;
    }


    private void setCellColoredNumber(int x, int y, int boardValue) {
        String value = String.valueOf(boardValue);
        Color cellColor = getColorByValue(boardValue);
        if (boardValue == 0){
            setCellValueEx(x, y, cellColor, "");
        }else {
            setCellValueEx(x, y, cellColor, value);
        }

    }

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
    }




}
