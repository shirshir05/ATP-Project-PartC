package View;

import ViewModel.MyViewModel;

import java.util.Observable;
import java.util.Observer;

public abstract class AController implements IView, Observer {

    protected static MyViewModel MyViewModel ;

    public static void setMyViewModel(MyViewModel viewModel)
    {
        MyViewModel = viewModel;
    }

    @Override
    public abstract void update(Observable o, Object arg);
}
