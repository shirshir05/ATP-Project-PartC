
package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;

    public MyViewModel(IModel model) {
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
/*
    public void keyPressedMouse(DragEvent E, double Height, double Width){
        model.keyPressedMouse(E, Height, Width);

    }
    */

    public Maze getMaze() {
        return model.getMaze();
    }

    public void keyPressed(KeyEvent keyEvent) {
        model.keyPressed(keyEvent);
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

    public void StopServer(){
            model.stopServers();

    }
}