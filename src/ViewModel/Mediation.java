package ViewModel;

import Model.IModel;
import Model.MyModel;
import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Mediation extends Observable implements Observer  {

    private IModel model;
    private int characterPositionRowIndex;
    private int characterPositionColumnIndex;

    public StringProperty characterPositionRow = new SimpleStringProperty("1"); //For Binding
    public StringProperty characterPositionColumn = new SimpleStringProperty("1"); //For Binding

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
        return characterPositionRowIndex;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumnIndex;
    }
    @Override
    public void update(Observable o, Object arg) {
        if (o==model){
            characterPositionRowIndex = model.getCharacterPositionRow();
            //characterPositionRow.set(characterPositionRowIndex + "");
            characterPositionColumnIndex = model.getCharacterPositionColumn();
           // characterPositionColumn.set(characterPositionColumnIndex + "");
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

}
