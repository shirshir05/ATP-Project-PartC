package View;

import ViewModel.Mediation;

import java.util.Observable;
import java.util.Observer;

public abstract class AController implements IView, Observer {

    protected static Mediation MyViewModel ;

    public static void setMyViewModel(Mediation viewModel)
    {
        MyViewModel = viewModel;
    }

    @Override
    public abstract void update(Observable o, Object arg);
}
