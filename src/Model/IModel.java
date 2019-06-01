package Model;

import javafx.scene.input.KeyCode;

import java.util.ArrayList;

public interface IModel {

    void generateMaze(int width, int height);
    void moveCharacter(KeyCode movement);
    int[][] getMaze();
    int getCharacterPositionRow();
    int getCharacterPositionColumn();
    ArrayList<int[]> solveMaze();


}
