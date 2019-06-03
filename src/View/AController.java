package View;

import ViewModel.Mediation;
import javafx.stage.Stage;

import java.util.Observable;
import java.util.Observer;

public abstract class AController extends Observable implements IView, Observer {

    public static Stage currentStage;

    //Defines the program's moderator
    protected static Mediation MyViewModel ;


    // A function called the main
    public static void setMyViewModel(Mediation viewModel)
    {
        MyViewModel = viewModel;
    }

    public static Stage getStage(){return currentStage;}

    //What to do when the person I'm listening to has changed
    @Override
    public abstract void update(Observable o, Object arg);
}
