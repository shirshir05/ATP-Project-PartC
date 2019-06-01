package View;

import ViewModel.MyViewModel;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class generateMazeController extends AController {

    public javafx.scene.control.TextField rowsNum;
    public javafx.scene.control.TextField ColumnsNum;


    public void createMaze(javafx.event.ActionEvent actionEvent) {
        int heigth = Integer.valueOf(rowsNum.getText());
        int width = Integer.valueOf(ColumnsNum.getText());
        MyViewModel.generateMaze(heigth,width);
        ((Node)actionEvent.getSource()).getScene().getWindow().hide();
    }


    @Override
    public void update(Observable o, Object arg) {

    }
}
