package View;

import javafx.scene.Node;
import javafx.scene.control.Alert;

import java.util.Observable;

public class generateMazeController extends AController {

    public javafx.scene.control.TextField rowsNum;
    public javafx.scene.control.TextField ColumnsNum;
    public javafx.scene.control.Button btn_generateMaze;



    //After the user sets the line number and column of the maze and presses the button create Maze - click me!
    public void createMaze(javafx.event.ActionEvent actionEvent) {


        try {
            int heigth = Integer.valueOf(rowsNum.getText());
            int width = Integer.valueOf(ColumnsNum.getText());
            if(heigth <=3 || width<=3){
                heigth = 10;
                width = 10;
            }
            MyViewModel.generateMaze(heigth,width);
            ((Node)actionEvent.getSource()).getScene().getWindow().hide();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Missing Values");
            alert.show();


        }
    }



    //Updating his listeners that he kept up to date
    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers();
    }
}
