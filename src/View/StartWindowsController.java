package View;

import Model.MyModel;
import ViewModel.Mediation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Observable;


public class StartWindowsController extends AController {

    @FXML
    public static Canvas writing;
    public Canvas MazesAndDragons;

    public static void showPics() {
        try {
            GraphicsContext graphicsContext2D = writing.getGraphicsContext2D();
            graphicsContext2D.clearRect(0, 0, writing.getWidth(), writing.getHeight()); //Clears the canvas
            Image title = null;
            title = new Image(new FileInputStream(System.getProperty("user.dir") + "/resources/Images/MAZESDRAGONS.png"));
            graphicsContext2D.drawImage(title, writing.getWidth(), writing.getHeight());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void startGame(javafx.event.ActionEvent actionEvent) throws Exception{
        //definition
        MyModel model = new MyModel();
        model.startServers();
        MyViewModel = new Mediation(model);
        //start the music
        MyViewController MyViewController = new MyViewController();


        //open a new windows -  the main windows
        FXMLLoader FXMLLoader  = new FXMLLoader(getClass().getResource("../View/MyView.fxml"));
        currentStage = new Stage();
        Parent root2 = (Parent)FXMLLoader.load();
        currentStage.setScene(new Scene(root2, 700, 600));
        ((Node)actionEvent.getSource()).getScene().getWindow().hide();
//        MyViewController m =  new MyViewController();


     /*   Pane rootNew = new Pane();
        MazeDisplayer mazeDisplayer =new MazeDisplayer();
        rootNew.getChildren().add(mazeDisplayer);
        rootNew.heightProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                double height = (double)newValue;
                mazeDisplayer.setHeight(height/2);
            }
        });
        rootNew.widthProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                double width = (double)newValue;
                mazeDisplayer.setWidth(width/2);
            }
        });*/

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
        currentStage.setTitle("MAZES & DRAGONS");
        SetStageCloseEvent(currentStage);
        currentStage.show();

    }

    @Override
    public void update(Observable o, Object arg) {
        //Nothing window disappears when the game starts

    }
}
