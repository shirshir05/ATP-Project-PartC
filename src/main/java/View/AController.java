package View;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

public abstract class AController extends Observable implements IView, Observer {

    public static Stage currentStage;

    //Defines the program's moderator
    protected static ViewModel.MyViewModel MyViewModel ;


    // A function called the main
    public static void setMyViewModel(ViewModel.MyViewModel viewModel)
    {
        MyViewModel = viewModel;
    }

    public static Stage getStage(){return currentStage;}

    //What to do when the person I'm listening to has changed
    @Override
    public abstract void update(Observable o, Object arg);

    public static void SetStageCloseEvent(Stage primaryStage) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to exit?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    // ... user chose OK
                    // Close program
                    freeProgram();
                } else {
                    // ... user chose CANCEL or closed the dialog
                    windowEvent.consume();
                }
            }
        });
    }

    public static void freeProgram(){

        if(MyViewModel !=null){
            MyViewModel.StopServer();

        }
        //currentStage.getS
        currentStage.close();
        Platform.exit();
    }
}
