package View;

import ViewModel.MyViewModel;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class generateMazeController extends AController implements IView  {

    private javafx.scene.control.TextField rowsNum;
    private javafx.scene.control.TextField ColumnsNum;


    public void createMaze(MouseEvent mouseEvent) {
        int heigth = Integer.valueOf(rowsNum.getText());
        int width = Integer.valueOf(ColumnsNum.getText());
        MyViewModel.generateMaze(heigth,width);

    }


}
