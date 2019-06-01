package View;

import Model.MyModel;
import ViewModel.Mediation;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.util.Observable;
import java.util.Observer;


public class StartWindowsController extends AController {

    public void startGame(javafx.event.ActionEvent actionEvent) throws Exception{
        //definition
        MyModel model = new MyModel();
        model.startServers();
        MyViewModel = new Mediation(model);
        //start the music
        MyViewController MyViewController = new MyViewController();


        //open a new windows -  the main windows
        FXMLLoader FXMLLoader  = new FXMLLoader(getClass().getResource("../View/MyView.fxml"));
        Parent root2 = (Parent)FXMLLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root2, 700, 600));
        ((Node)actionEvent.getSource()).getScene().getWindow().hide();
        MyViewController m =  new MyViewController();

        //definition model and MyViewModel Observer
        model.addObserver(MyViewModel);
        AController view  = FXMLLoader.getController();
        view.setMyViewModel(MyViewModel);
        MyViewModel.addObserver(view);

        //definition MyViewController and MyViewModel MyViewController
        MyViewController.addObserver(MyViewModel);
        AController view2  = FXMLLoader.getController();
        view2.setMyViewModel(MyViewModel);
        MyViewModel.addObserver(view2);

        //show
        stage.show();

    }

    @Override
    public void update(Observable o, Object arg) {
        //Nothing window disappears when the game starts

    }
}
