package pkg2048;

import extendsFX.BaseGraphics;
import java.util.Random;
import static javafx.application.Application.launch;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class Main extends BaseGraphics {
    int Start = 0;
    final int step = 80;
    int gridSize = 4;
    Tile[][] tiles = new Tile[gridSize][gridSize];
    int score = 0;
    
    private void drawGrid() { 
        double step1 = step;
        gc.setLineWidth(1);
        gc.setStroke(Color.RED);
        gc.setFill(Color.BLACK.brighter());
        for (int i = 0; i < gridSize; i++) { 
            gc.strokeLine(39, step1 - 50, canvasW / 1.6 - 14, step1 - 50); // horizontali
            gc.strokeLine(step1 - 40, 30, step1 - 40, canvasH / 1.3 + step / 2 + 3); // vertikali
            step1 += step;
    }
        gc.strokeLine(39, step1 - 50, canvasW / 1.6 - 14, step1 - 50); // horizontali
        gc.strokeLine(step1 - 40, 30, step1 - 40, canvasH / 1.3 + step / 2 + 3); // vertikali
    }
    @Override
    public void createControls(){
        addButton("Start Game",  e -> startGame());
        addButton("Restart", e -> resetGame()); 
        addButton("Push Left", e -> makeAMove(0));
        addButton("Push Right", e -> makeAMove(1));
        addButton("Push Up", e -> makeAMove(2));
        addButton("Push Down", e -> makeAMove(3));
        addNewHBox();
    }
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("2048");        
        setCanvas(Color.BLACK.brighter(), 600, 400);
        super.start(stage);
        drawGrid();
    }       
    public static void main(String[] args) {
        launch(args);
    }
    
    public int newTileValue() {
        Random rng = new Random();
        
        int value = rng.nextInt(5);
        
        if (value == 4 || value == 2)
            return 4;
        else
            return 2;
    }
    
    public void InitializeTiles() {
        for (int i = 0; i < gridSize; i++)
            for (int j = 0; j < gridSize; j++) {
                tiles[i][j] = new Tile();
            }
    }
    
    public void startGame() {
        if (Start == 0) {
        InitializeTiles();
        int blocks = 0;
        Random rng = new Random();
        while (blocks != 2) {
        int tilesRow = rng.nextInt(gridSize);
        int tilesCol = rng.nextInt(gridSize);
        if (tiles[tilesRow][tilesCol].getValue() == 0) {
        tiles[tilesCol][tilesRow].setValue(newTileValue());
        blocks++;
        }
        }
        dataToGrid();
        } else if (Start == 1) {
            JOptionPane.showMessageDialog(null, "The game has already started", "Game Started Error", 1);
        }
        Start = 1;
    }
    
    public void dataToGrid() {
        int addStep = 0;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (tiles[i][j].getValue() != 0){
                    gc.setFont(Font.font("Times New Roman", 36));
                    if (tiles[i][j].getValue() < 10) {
                    gc.strokeText(tiles[i][j].toString(), 70 + 80 * j, 80 + 53 * i + addStep);
                    } else if (tiles[i][j].getValue() > 9 && tiles[i][j].getValue() < 100) {
                        gc.strokeText(tiles[i][j].toString(), 60 + 80 * j, 80 + 53 * i + addStep);
                    } else if (tiles[i][j].getValue() > 99 && tiles[i][j].getValue() < 1000) {
                        gc.strokeText(tiles[i][j].toString(), 50 + 80 * j, 80 + 53 * i + addStep);
                    } else if (tiles[i][j].getValue() > 999 && tiles[i][j].getValue() < 10000) {
                        gc.strokeText(tiles[i][j].toString(), 43 + 80 * j, 80 + 53 * i + addStep);
                    }
                }
            }
            addStep += 26;
        }
        printOutScore();
    }
    
    public void resetGame() {
        clearCanvas();
        drawGrid();
        Start = 0;
        score = 0;
        startGame();
    }
    
    public void makeAMove(int move) {
        if (ifLost() == true) {
            int n = JOptionPane.showConfirmDialog(null, "No more moves left. Would you like to try again?", "Game lost message", 
                    JOptionPane.YES_NO_OPTION);
            if (n == 0)
                resetGame();
            if (n == 1)
                System.exit(0);
        }
        int addRandom = 0;
        switch (move) {
            case 0: // left
                for (int i = 0; i < gridSize; i++)
                    addRandom += pushTilesLeft(tilesInARow(i), i);
                break;
            case 1: // right
                for (int i = 0; i < gridSize; i++)
                    addRandom += pushTilesRight(tilesInARow(i), i);
                break;
            case 2: // up
                for (int i = 0; i < gridSize; i++)
                    addRandom += pushTilesUp(tilesInAColumn(i), i);
                break;
            case 3: // down
                for (int i = 0; i < gridSize; i++)
                    addRandom += pushTilesDown(tilesInAColumn(i), i);
                break;
        }
        if (addRandom > 0){
        printOutScore();
        randomTileSpawn();
        dataToGrid();
        }
        if (ifWon() == true) {
            int n = JOptionPane.showConfirmDialog(null, "You won. Would you like to continue playing?", "Game won message", 
                    JOptionPane.YES_NO_OPTION);
            if (n == 1)
                System.exit(0);
        }
    }
    
    private int pushTilesRight(int rowTiles, int row) {
        if (rowTiles != 0 && verifyPushRight(rowTiles, row) == false) {
            for (int i = gridSize - 2; i >= 0; i--) {
                    if ((tiles[row][i + 1].getValue() == 0 && tiles[row][i].getValue() != 0) || (tiles[row][i + 1].getValue() == tiles[row][i].getValue() && tiles[row][i].getValue() != 0)) {
                    if (tiles[row][i + 1].getValue() != 0)
                    score += score(row, i + 1) * 2;
                    tiles[row][i + 1].mergeTiles(tiles[row][i]);
                    tiles[row][i].clear();
                    i = gridSize - 1;
                }
                }
            refreshRow(row);
            return 1;
        }
        return 0;
    }
    
    private boolean verifyPushRight(int rowTiles, int row) {
        int count = 0;
        switch (rowTiles) {
            case 0:
                return false;
            case 1:
                if (tiles[row][gridSize - 1].getValue() != 0)
                    return true;
                else
                    return false;
            case 2:
                if ((tiles[row][gridSize - 1].getValue() != 0 && tiles[row][gridSize - 2].getValue() != 0) && (tiles[row][gridSize - 1].getValue() != tiles[row][gridSize - 2].getValue()))
                    return true;
                else
                    return false;
            case 3:
                if (tiles[row][0].getValue() == 0)
                    for (int i = 1; i < gridSize - 1; i++) {
                        if ((tiles[row][i].getValue() != 0 && tiles[row][i + 1].getValue() != 0) && (tiles[row][i].getValue() != tiles[row][i + 1].getValue()))
                            count++;
                    }
                        if (count == rowTiles - 1)
                            return true;
                        else
                            return false;
            case 4:
                for (int i = 0; i < gridSize - 1; i++) {
                    if ((tiles[row][i].getValue() != 0 && tiles[row][i + 1].getValue() != 0) && (tiles[row][i].getValue() != tiles[row][i + 1].getValue()))
                        count++;
                }
                if (count == rowTiles - 1)
                    return true;
                else 
                    return false;
        }
        return false;
    }
    
    private int pushTilesLeft(int rowTiles, int row) {
        if (rowTiles != 0 && verifyPushLeft(rowTiles, row) == false) {
            for (int i = 1; i < gridSize; i++) {
                    if ((tiles[row][i - 1].getValue() == 0 && tiles[row][i].getValue() != 0) || (tiles[row][i - 1].getValue() == tiles[row][i].getValue() && tiles[row][i].getValue() != 0)) {
                    if (tiles[row][i - 1].getValue() != 0)
                    score += score(row, i - 1) * 2;
                    tiles[row][i - 1].mergeTiles(tiles[row][i]);
                    tiles[row][i].clear();
                    i = 0;
                }
                }
            refreshRow(row);
            return 1;
        }
        return 0;
    }
    
    private boolean verifyPushLeft(int rowTiles, int row) {
        int count = 0;
        switch (rowTiles) {
            case 0:
                return false;
            case 1:
                if (tiles[row][0].getValue() != 0)
                    return true;
                else
                    return false;
            case 2:
                if ((tiles[row][0].getValue() != 0 && tiles[row][1].getValue() != 0) && (tiles[row][0].getValue() != tiles[row][1].getValue()))
                    return true;
                else
                    return false;
            case 3:
                if (tiles[row][gridSize - 1].getValue() == 0)
                    for (int i = 0; i < gridSize - 2; i++) {
                        if ((tiles[row][i].getValue() != 0 && tiles[row][i + 1].getValue() != 0) && (tiles[row][i].getValue() != tiles[row][i + 1].getValue()))
                            count++;
                    }
                        if (count == rowTiles - 1)
                            return true;
                        else
                            return false;
            case 4:
                for (int i = 0; i < gridSize - 1; i++) {
                    if ((tiles[row][i].getValue() != 0 && tiles[row][i + 1].getValue() != 0) && (tiles[row][i].getValue() != tiles[row][i + 1].getValue()))
                        count++;
                }
                if (count == rowTiles - 1)
                    return true;
                else 
                    return false;
        }
        return false;
    }
    
    private int pushTilesUp(int columnTiles, int column) {
        if (columnTiles != 0 && verifyPushUp(columnTiles, column) == false) {
            for (int i = 1; i < gridSize; i++) {
                    if ((tiles[i - 1][column].getValue() == 0 && tiles[i][column].getValue() != 0) || (tiles[i - 1][column].getValue() == tiles[i][column].getValue() && tiles[i][column].getValue() != 0)) {
                    if (tiles[i - 1][column].getValue() != 0)
                    score += score(i - 1, column) * 2;
                    tiles[i - 1][column].mergeTiles(tiles[i][column]);
                    tiles[i][column].clear();
                    i = 0;
                }
                }
            refreshColumn(column);
            return 1;
        }
        return 0;
    }
    
    private boolean verifyPushUp(int columnTiles, int column) {
        int count = 0;
        switch (columnTiles) {
            case 0:
                return false;
            case 1:
                if (tiles[0][column].getValue() != 0)
                    return true;
                else
                    return false;
            case 2:
                if ((tiles[0][column].getValue() != 0 && tiles[1][column].getValue() != 0) && (tiles[0][column].getValue() != tiles[1][column].getValue()))
                    return true;
                else
                    return false;
            case 3:
                if (tiles[gridSize - 1][column].getValue() == 0)
                    for (int i = 0; i < gridSize - 2; i++) {
                        if ((tiles[i][column].getValue() != 0 && tiles[i + 1][column].getValue() != 0) && (tiles[i][column].getValue() != tiles[i + 1][column].getValue()))
                            count++;
                    }
                        if (count == columnTiles - 1)
                            return true;
                        else
                            return false;
            case 4:
                for (int i = 0; i < gridSize - 1; i++) {
                    if ((tiles[i][column].getValue() != 0 && tiles[i + 1][column].getValue() != 0) && (tiles[i][column].getValue() != tiles[i + 1][column].getValue()))
                        count++;
                }
                if (count == columnTiles - 1)
                    return true;
                else 
                    return false;
        }
        return false;
    }
    
    private int pushTilesDown(int columnTiles, int column) {
        if (columnTiles != 0 && verifyPushDown(columnTiles, column) == false) {
            for (int i = gridSize - 2; i >= 0; i--) {
                    if ((tiles[i + 1][column].getValue() == 0 && tiles[i][column].getValue() != 0) || (tiles[i + 1][column].getValue() == tiles[i][column].getValue() && tiles[i][column].getValue() != 0)) {
                    if (tiles[i + 1][column].getValue() != 0)
                    score += score(i + 1, column) * 2;
                    tiles[i + 1][column].mergeTiles(tiles[i][column]);
                    tiles[i][column].clear();
                    i = gridSize - 1;
                }
                }
            refreshColumn(column);
            return 1;
        }
        return 0;
    }
    
    private boolean verifyPushDown(int columnTiles, int column) {
        int count = 0;
        switch (columnTiles) {
            case 0:
                return false;
            case 1:
                if (tiles[gridSize - 1][column].getValue() != 0)
                    return true;
                else
                    return false;
            case 2:
                if ((tiles[gridSize - 1][column].getValue() != 0 && tiles[gridSize - 2][column].getValue() != 0) && (tiles[gridSize - 1][column].getValue() != tiles[gridSize - 2][column].getValue()))
                    return true;
                else
                    return false;
            case 3:
                if (tiles[0][column].getValue() == 0)
                    for (int i = 1; i < gridSize - 1; i++) {
                        if ((tiles[i][column].getValue() != 0 && tiles[i + 1][column].getValue() != 0) && (tiles[i][column].getValue() != tiles[i + 1][column].getValue()))
                            count++;
                    }
                        if (count == columnTiles - 1)
                            return true;
                        else
                            return false;
            case 4:
                for (int i = 0; i < gridSize - 1; i++) {
                    if ((tiles[i][column].getValue() != 0 && tiles[i + 1][column].getValue() != 0) && (tiles[i][column].getValue() != tiles[i + 1][column].getValue()))
                        count++;
                }
                if (count == columnTiles - 1)
                    return true;
                else 
                    return false;
        }
        return false;
    }
    
    private void refreshRow(int row) {
        for (int i = 0; i < gridSize; i++) {
            gc.fillRect(41 + (step - 3) * i + 3 * i, 31 + step * row, step - 4, step - 2);
        }
        dataToGrid();
    }
    
    private void refreshColumn(int column) {
        for (int i = 0; i < gridSize; i++) {
            gc.fillRect(41 + (step - 3) * column + 3 * column, 31 + step * i, step - 4, step - 2);
        }
        dataToGrid();
    }
    
    private int tilesInARow(int row) {
        int counter = 0;
        for (int i = 0; i < gridSize; i++) {
            if (tiles[row][i].getValue() != 0)
                counter++;
        }
        return counter;
    }
    
    private int tilesInAColumn(int column) {
        int counter = 0;
        for (int i = 0; i < gridSize; i++) {
            if (tiles[i][column].getValue() != 0)
                counter++;
        }
        return counter;
    }
    
    public void randomTileSpawn() {
        
        Random rng = new Random();
        boolean tile = false;
        
        while (tile == false) {
            int tilesRow = rng.nextInt(gridSize);
            int tilesCol = rng.nextInt(gridSize);
            if (tiles[tilesRow][tilesCol].getValue() == 0) {
                tiles[tilesRow][tilesCol].setValue(newTileValue());
                tile = true;
            }
        }
    }
    
    public boolean ifLost() {
        if (emptyTiles() == false){
            if (hasEqualNeighbour() != true)
                return true;
        }
        return false;
    }
    
    public boolean emptyTiles() {
        for (int i = 0; i < gridSize; i++)
            for (int j = 0; j < gridSize; j++) {
                if (tiles[i][j].getValue() == 0)
                    return true;
            }
        return false;
    }
    
    public boolean hasEqualNeighbour() {
        for (int i = 0; i < gridSize - 1; i++) {
            for (int j = 0; j < gridSize - 1; j++)
            {
                if (tiles[i][j].getValue() == tiles[i][j + 1].getValue() || tiles[i][j].getValue() == tiles[i + 1][j].getValue())
                    return true;
            }
            if ((tiles[i][gridSize - 1].getValue() == tiles[i + 1][gridSize - 1].getValue()) || (tiles[gridSize - 1][i].getValue() == tiles[gridSize - 1][i + 1].getValue()))
                return true;
        }
        return false;
    }
    
    public boolean ifWon() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (tiles[i][j].getValue() == 2048)
                    return true;
            }
        }
        return false;
    }
    
    public int score(int row, int column) {
        return tiles[row][column].getValue();
    }
    
    public void printOutScore() {
        gc.fillRect(390, 15, 80, 80);
        String scoreString = Integer.toString(score);
        gc.strokeText(scoreString, 401, 51);
    }
}
