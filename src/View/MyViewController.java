package View;

import algorithms.mazeGenerators.Maze;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.ResourceBundle;


public class MyViewController extends AController implements Initializable {

    // ----------music Background-----//
    private MediaPlayer mediaplayerBackground;
    private boolean flagToMusicBackground = false;

    //show the maze
    @FXML
    public MazeDisplayer mazeDisplayer;
    public Menu loadMazeMenu;
    public BorderPane BorderPane;
    public VBox VBox;


    //A constructor that plays the music calls it when the window opens
    public MyViewController(){
        musicBackground();
    }

    //<editor-fold desc="Data Binding for Character Row & Column Positions">

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Set Binding for Properties
        BorderPane.prefWidthProperty().bind(getStage().widthProperty().multiply(0.9));
        BorderPane.prefHeightProperty().bind(getStage().heightProperty().multiply(0.7));
        VBox.prefWidthProperty().bind(BorderPane.prefWidthProperty());
        VBox.prefHeightProperty().bind(BorderPane.prefHeightProperty()/*.multiply(0.5)*/);
        mazeDisplayer.widthProperty().bind(VBox.prefWidthProperty());
        mazeDisplayer.heightProperty().bind(VBox.prefHeightProperty());
    }
    //</editor-fold>

    public void NewMazeMouseClicked() throws IOException {
        //open a new windows -  the generate Maze
        FXMLLoader FXMLLoader  = new FXMLLoader(getClass().getResource("../View/generateMaze.fxml"));
        Parent root2 = (Parent)FXMLLoader.load();
        Stage stage = new Stage();
        Scene s = new Scene(root2,381,301);
        stage.setScene(s);




        //definition generateMazeController and MyViewModel Observer
        generateMazeController generateMazeController = new generateMazeController();
        generateMazeController.addObserver(MyViewModel);
        AController view3  = FXMLLoader.getController();
        view3.setMyViewModel(MyViewModel);
        MyViewModel.addObserver(view3);

        //definition generateMazeController and MyViewController Observer
        generateMazeController.addObserver(this);
        AController view4  = FXMLLoader.getController();
        this.addObserver(view4);
        //show
        stage.setTitle("MAZES & DRAGONS");
        stage.show();
    }


    //When something changes he displays the maze again
    public void update(Observable o, Object arg) {
        if (o == MyViewModel) {
            displayMaze(MyViewModel.getMaze());
            //btn_generateMaze.setDisable(false);
        }
    }



    public void displayMaze(Maze maze) {
        //Update location of characters
         int characterPositionRow = MyViewModel.getCharacterPositionRow();
        int characterPositionColumn = MyViewModel.getCharacterPositionColumn();
        mazeDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);
        //A function that draws the maze
        mazeDisplayer.setMaze(maze);

        //this.characterPositionRow.set(characterPositionRow + "");
        //this.characterPositionColumn.set(characterPositionColumn + "");
    }


    //-----------------------------------solve--------------------------//

    //When the button clicks the solve maze button
    public void solve()
    {
        mazeDisplayer.displayNewSolution(MyViewModel.solveMaze());
    }


    //Prevent the focus taking problem of the TextFields
    public void mazeMouseClicked(MouseEvent mouseEvent) {

        mazeDisplayer.requestFocus();
    }



    public void KeyPressed(KeyEvent keyEvent) {
        MyViewModel.KeyPressed(keyEvent);
    }

    //------------------------------menu bar---------------------------//

    public void saveMaze()
    {
        MyViewModel.saveMaze();
    }

    /**
     * onShowing method for the Load Mazes option in the File menu, on the menu bar.
     * displays the menu according to the existing mazes
     */
    public void showSavedMazes()
    {
        ArrayList<String> mazes = MyViewModel.getSavedMazes(); //gets the list of saved mazes
        if(mazes.size() > 0)
        {
            loadMazeMenu.getItems().remove(1, loadMazeMenu.getItems().size()-1);//removes all but the first one
            for (String mazeName : mazes) {
                //creates a new menu item for each maze
                MenuItem menuItem = new MenuItem();
                menuItem.setMnemonicParsing(false);
                menuItem.setText(mazeName);
                menuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        MyViewModel.createMazeFromFile(mazeName);
                    }
                });
                loadMazeMenu.getItems().add(menuItem);
            }
            loadMazeMenu.getItems().remove(0); //removes the first item which was not deleted
        }
        else //mazes.size == 0
        {
            //if there are no mazes- displays an empty menu
            MenuItem menuItem = new MenuItem();
            menuItem.setMnemonicParsing(false);
            menuItem.setText("");
            loadMazeMenu.getItems().add(menuItem);
            loadMazeMenu.getItems().remove(0,loadMazeMenu.getItems().size()-1);
        }


    }

    // -----------------------------music------------------------------//
    public void musicBackground()
    {
        Media musicFile = new Media(getClass().getResource("/Audio/Relaxing Video Game Music for 3 Hours (Vol. 1) (mp3cut.net).m4a").toString());
        mediaplayerBackground = new MediaPlayer(musicFile);
        mediaplayerBackground.setVolume(0.1);
        mediaplayerBackground.play();
        flagToMusicBackground = true;
    }

    public void StopMusicBackground() {
        if(flagToMusicBackground){
            mediaplayerBackground.stop();
            flagToMusicBackground =  false;

        }
        if(flagToMusicBackground == false){
            musicBackground();

        }
    }

}
