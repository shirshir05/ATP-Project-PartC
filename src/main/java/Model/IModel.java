package Model;

import algorithms.mazeGenerators.Maze;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public interface IModel {

    void generateMaze(int width, int height);
    Maze getMaze();
    int getCharacterPositionRow();
    int getCharacterPositionColumn();
    ArrayList<int[]> solveMaze();


     void stopServers();
    void keyPressed(KeyEvent keyEvent);
    ArrayList<String> getListOfSavedFiles();
    void createFileFromDB(String fileName);
    void saveMaze();

   // void keyPressedMouse(DragEvent e, double Height, double Width);
}
