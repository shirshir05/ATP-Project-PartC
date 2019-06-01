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
        Parent root = loader.load(getClass().getResource("../View/StartWindows.fxml").openStream());
        primaryStage.setTitle("MAZES & DRAGONS");
        Scene scene = new Scene(root,700,600);
        scene.getStylesheets().add(getClass().getResource("../View/ViewStyle.css").toExternalForm());
        primaryStage.setScene(scene);

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
