package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MazeDisplayer extends Canvas {

    //region Properties
    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();
    private StringProperty ImageFileNameSolution = new SimpleStringProperty();

    public void setImageFileNameSolution(String imageFileNameSolution) { this.ImageFileNameSolution.set(imageFileNameSolution); }

    public String getImageFileNameSolution() { return ImageFileNameSolution.get(); }

    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }

    public void setImageFileNameCharacter(String imageFileNameCharacter) { this.ImageFileNameCharacter.set(imageFileNameCharacter);}
    //endregion


    private int[][] maze;
    private int characterPositionRow;
    private int characterPositionColumn;
    private boolean solutionDisplayed = false;
    private ArrayList<int[]> solution;

    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    // new maze
    public void setMaze(int[][] maze) {
        this.maze = maze;
        solutionDisplayed = false;
        redraw();
    }

    public void setCharacterPosition(int row, int column) {
        characterPositionRow = row;
        characterPositionColumn = column;
        redraw();
    }

    public void redraw() {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.length;
            double cellWidth = canvasWidth / maze[0].length;
            GraphicsContext graphicsContext2D = getGraphicsContext2D();
            graphicsContext2D.clearRect(0, 0, getWidth(), getHeight()); //Clears the canvas
            drawMaze(cellHeight,cellWidth,graphicsContext2D);
            if (solutionDisplayed)
                drawSolution(cellHeight,cellWidth,graphicsContext2D);
            drawCharacter(cellHeight,cellWidth,graphicsContext2D);
        }
    }

    public void displayNewSolution(ArrayList<int[]> solution) {
        maze = new int[][] { // temporary...
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0, 1},
                {0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 1, 1},
                {1, 1, 1, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1},
                {1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 1},
                {1, 1, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1},
                {1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1},
                {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1}
        };

        if (solutionDisplayed)//enables hiding the solution
            solutionDisplayed = false;//????????????????????
        else {
            this.solution = solution; //saves the solution so it can be redrawn
            solutionDisplayed = true; //the solution will be displayed as long as the user continues playing
        }
        redraw();
    }


    public void drawMaze(double cellHeight, double cellWidth, GraphicsContext graphicsContext2D)
    {
        try {
            Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
            for (int i = 0; i < maze.length; i++) {
                for (int j = 0; j < maze[i].length; j++) {
                    if (maze[i][j] == 1) {
                        graphicsContext2D.drawImage(wallImage, i * cellHeight, j * cellWidth, cellHeight, cellWidth);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(String.format("Image doesn't exist: %s", e.getMessage()));
            alert.show();
        }
    }

    private void drawCharacter(double cellHeight, double cellWidth,GraphicsContext graphicsContext2D) {
        try {
            Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
            graphicsContext2D.drawImage(characterImage, characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(String.format("Image doesn't exist: %s", e.getMessage()));
            alert.show();
        }
    }

    private void drawSolution(double cellHeight, double cellWidth, GraphicsContext graphicsContext2D)
    {
        try {
            Image pathImage = new Image(new FileInputStream(ImageFileNameSolution.get()));
            for (int i = 0; i < solution.size(); i++) {
                graphicsContext2D.drawImage(pathImage, solution.get(i)[0]*cellHeight, solution.get(i)[1]*cellWidth, cellHeight, cellWidth);
            }
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(String.format("Image doesn't exist: %s", e.getMessage()));
            alert.show();
        }
    }

}

