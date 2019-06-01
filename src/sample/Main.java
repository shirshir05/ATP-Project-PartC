package sample;

import Model.MyModel;
import View.AController;
import ViewModel.Mediation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{

        /////
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("../View/StartWindows.fxml").openStream());
        primaryStage.setTitle("MAZES & DRAGONS");
        Scene scene = new Scene(root, 700, 600);
        scene.getStylesheets().add(getClass().getResource("../View/ViewStyle.css").toExternalForm());
        primaryStage.setScene(scene);


        //


        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
