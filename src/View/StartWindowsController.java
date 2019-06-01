package View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;


public class StartWindowsController extends AController{



    public void startGame(javafx.event.ActionEvent actionEvent) throws Exception{
        //open a new windows
        FXMLLoader FXMLLoader  = new FXMLLoader(getClass().getResource("../View/MyView.fxml"));
        Parent root2 = (Parent)FXMLLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root2, 700, 600));
        stage.show();
        ((Node)actionEvent.getSource()).getScene().getWindow().hide();
        MyViewController m =  new MyViewController();



    }
}
