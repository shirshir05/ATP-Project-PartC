
package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Mediation extends Observable implements Observer {

    private IModel model;

    public Mediation(IModel model) {
        this.model = model;
    }

    public void generateMaze(int width, int height){
        model.generateMaze(width, height);
    }

    public ArrayList<int[]> solveMaze() {
        return model.solveMaze();
    }

    public int getCharacterPositionRow() {
        return model.getCharacterPositionRow();
    }

    public int getCharacterPositionColumn() {
        return model.getCharacterPositionColumn();
    }
    @Override
    public void update(Observable o, Object arg) {
        if (o==model){
            setChanged();
            notifyObservers();
        }
    }

    public Maze getMaze() {
        return model.getMaze();
    }

    public void KeyPressed(KeyEvent keyEvent) {
        model.KeyPressed(keyEvent);
    }

    public ArrayList<String> getSavedMazes() {
        return model.getListOfSavedFiles();
    }

    public void createMazeFromFile(String fileName) {
        model.createFileFromDB(fileName);
    }

    public void saveMaze() {
        model.saveMaze();
    }
}