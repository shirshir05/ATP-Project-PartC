package ViewModel;

import Model.IModel;
import Model.MyModel;

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

    @Override
    public void update(Observable o, Object arg) {

    }
}
