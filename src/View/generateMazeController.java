package View;

import javafx.scene.Node;

import java.util.Observable;

public class generateMazeController extends AController {

    public javafx.scene.control.TextField rowsNum;
    public javafx.scene.control.TextField ColumnsNum;
    public javafx.scene.control.Button btn_generateMaze;



    //After the user sets the line number and column of the maze and presses the button create Maze - click me!
    public void createMaze(javafx.event.ActionEvent actionEvent) {
        int heigth = Integer.valueOf(rowsNum.getText());
        int width = Integer.valueOf(ColumnsNum.getText());
        MyViewModel.generateMaze(heigth,width);
        ((Node)actionEvent.getSource()).getScene().getWindow().hide();
    }



    //Updating his listeners that he kept up to date
    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers();    }
}
