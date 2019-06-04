package View;

import algorithms.mazeGenerators.Maze;
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
    private StringProperty ImageFileNamePrince = new SimpleStringProperty();

    private StringProperty ImageFileNameSolution = new SimpleStringProperty();
    private Maze maze;
    private int characterPositionRow;
    private int characterPositionColumn;



    private int PrincePositionRow;
    private int PrincePositionColumn;
    private boolean solutionDisplayed = false;
    private ArrayList<int[]> solution;



    public void setImageFileNameSolution(String imageFileNameSolution) { this.ImageFileNameSolution.set(imageFileNameSolution); }

    public void setImageFileNamePrince(String imageFileNameSolution) { this.ImageFileNamePrince.set(imageFileNameSolution); }


    public String getImageFileNameSolution() { return ImageFileNameSolution.get(); }

    public String getImageFileNamePrince() { return ImageFileNameSolution.get(); }

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



    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    public int getPrincePositionRow() {
        return PrincePositionRow;
    }

    public int getPrincePositionColumn() {
        return PrincePositionColumn;
    }


    public MazeDisplayer() {
        // Redraw canvas when size changes.
        widthProperty().addListener(evt -> redraw());
        heightProperty().addListener(evt -> redraw());
    }


    @Override
    public boolean isResizable() {
        return true;
    }

    // new maze
    public void setMaze(Maze maze) {
        this.maze = maze;
        if(PrincePositionColumn == maze.getStartPosition().getColumnIndex() && PrincePositionRow==maze.getStartPosition().getRowIndex()){
            solutionDisplayed = false;
        }
        redraw();
    }

    public void setCharacterPosition(int row, int column) {
        characterPositionRow = row;
        characterPositionColumn = column;
    }

    public void redraw() {
        if (maze != null) {
            PrincePositionRow = maze.getGoalPosition().getRowIndex();
            PrincePositionColumn = maze.getGoalPosition().getColumnIndex();
            int[][] mazeArray = maze.getM_maze();
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / mazeArray.length;
            double cellWidth = canvasWidth / mazeArray[0].length;
            GraphicsContext graphicsContext2D = getGraphicsContext2D();
            graphicsContext2D.clearRect(0, 0, getWidth(), getHeight()); //Clears the canvas
            drawMaze(cellHeight,cellWidth,graphicsContext2D);
            if (solutionDisplayed)
                drawSolution(cellHeight,cellWidth,graphicsContext2D);
            drawCharacter(cellHeight,cellWidth,graphicsContext2D);
            drawCharacterPrince(cellHeight,cellWidth,graphicsContext2D);
            //maze.print();
        }
    }

    public void displayNewSolution(ArrayList<int[]> solution) {
        if (solutionDisplayed || solution == null)//enables hiding the solution
            solutionDisplayed = false;
        else {
            this.solution = solution; //saves the solution so it can be redrawn
            solutionDisplayed = true; //the solution will be displayed as long as the user continues playing
        }
        redraw();
    }


    public void drawMaze(double cellHeight, double cellWidth, GraphicsContext graphicsContext2D)
    {
        try {
            int[][] mazeArray = maze.getM_maze();
            Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
            for (int i = 0; i < mazeArray.length; i++) {
                for (int j = 0; j < mazeArray[i].length; j++) {
                    if (mazeArray[i][j] == 1) {
                        graphicsContext2D.drawImage(wallImage, j *cellWidth , i * cellHeight,cellWidth ,cellHeight );
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
            graphicsContext2D.drawImage(characterImage, characterPositionColumn * cellWidth, characterPositionRow * cellHeight, cellWidth, cellHeight);
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(String.format("Image doesn't exist: %s", e.getMessage()));
            alert.show();



        }
    }

    private void drawCharacterPrince(double cellHeight, double cellWidth,GraphicsContext graphicsContext2D) {
        try {
            Image characterImage = new Image(new FileInputStream(ImageFileNamePrince.get()));
            graphicsContext2D.drawImage(characterImage,getPrincePositionColumn()* cellWidth, getPrincePositionRow() * cellHeight, cellHeight, cellHeight);
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
                graphicsContext2D.drawImage(pathImage, solution.get(i)[1]*cellWidth,solution.get(i)[0]* cellHeight,cellWidth  , cellHeight);
              //  System.out.println(solution.get(i)[0] + ", " + solution.get(i)[1]);
            }
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(String.format("Image doesn't exist: %s", e.getMessage()));
            alert.show();
        }
    }


}

