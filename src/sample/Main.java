package sample;

import Model.MyModel;
import View.AController;
import View.IView;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        MyModel model = new MyModel();
        model.startServers();
        MyViewModel viewModel = new MyViewModel(model);
        model.addObserver(viewModel);

        /////
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("../View/StartWindows.fxml"));
        primaryStage.setTitle("MAZES & DRAGONS");
        primaryStage.setScene(new Scene(root, 700, 600));

        //
        AController view  = loader.getController();
        view.setMyViewModel(viewModel);
        viewModel.addObserver(view);


        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
