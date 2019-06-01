package View;

import Model.MyModel;
import ViewModel.Mediation;
import javafx.fxml.FXMLLoader;

import java.util.Observable;
import java.util.Observer;

public abstract class AController extends Observable implements IView, Observer {

    //Defines the program's moderator
    protected static Mediation MyViewModel ;


    // A function called the main
    public static void setMyViewModel(Mediation viewModel)
    {
        MyViewModel = viewModel;
    }

    //What to do when the person I'm listening to has changed
    @Override
    public abstract void update(Observable o, Object arg);
}
