package View;

import javafx.scene.input.MouseEvent;

import java.util.Observable;

public class generateMazeController extends AController {

    private javafx.scene.control.TextField rowsNum;
    private javafx.scene.control.TextField ColumnsNum;


    public void createMaze(MouseEvent mouseEvent) {
        int heigth = Integer.valueOf(rowsNum.getText());
        int width = Integer.valueOf(ColumnsNum.getText());
        MyViewModel.generateMaze(heigth,width);

    }


    @Override
    public void update(Observable o, Object arg) {

    }
}
