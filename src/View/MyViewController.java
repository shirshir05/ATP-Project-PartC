package View;
import algorithms.mazeGenerators.MyMazeGenerator;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;


public class MyViewController extends AController {

    // ----------music Background-----//
    private MediaPlayer mediaplayerBackground;
    private boolean flagToMusicBackground = false;

    //show the maze
    @FXML
    public MazeDisplayer mazeDisplayer;


    //A constructor that plays the music calls it when the window opens
    public MyViewController(){

        musicBackground();
    }

    public void NewMazeMouseClicked() throws IOException {

        //open a new windows -  the generate Maze
        FXMLLoader FXMLLoader  = new FXMLLoader(getClass().getResource("../View/generateMaze.fxml"));
        Parent root2 = (Parent)FXMLLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root2, 500, 600));

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
        stage.show();
    }


    //When something changes he displays the maze again
    public void update(Observable o, Object arg) {
        if (o == MyViewModel) {
            displayMaze(MyViewModel.getMaze());
            //btn_generateMaze.setDisable(false);
        }

    }

    public void displayMaze(int[][] maze) {
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


    //-----------------------------------click KeyPressed--------------------------//
    public void KeyPressed(KeyEvent keyEvent) {
        int characterRowCurrentPosition = mazeDisplayer.getCharacterPositionRow();
        int characterColumnCurrentPosition = mazeDisplayer.getCharacterPositionColumn();
        int characterRowNewPosition = characterRowCurrentPosition;
        int characterColumnNewPosition = characterColumnCurrentPosition;

        if (keyEvent.getCode() == KeyCode.UP) {
            characterRowNewPosition = characterRowCurrentPosition - 1;
            characterColumnNewPosition = characterColumnCurrentPosition;
        } else if (keyEvent.getCode() == KeyCode.DOWN) {
            characterRowNewPosition = characterRowCurrentPosition + 1;
            characterColumnNewPosition = characterColumnCurrentPosition;
        } else if (keyEvent.getCode() == KeyCode.RIGHT) {
            characterRowNewPosition = characterRowCurrentPosition;
            characterColumnNewPosition = characterColumnCurrentPosition + 1;
        } else if (keyEvent.getCode() == KeyCode.LEFT) {
            characterRowNewPosition = characterRowCurrentPosition;
            characterColumnNewPosition = characterColumnCurrentPosition - 1;
        } else if (keyEvent.getCode() == KeyCode.HOME) {
            characterRowNewPosition = 0;
            characterColumnNewPosition = 0;
        }

        //Updates the MazeDisplayer
        mazeDisplayer.setCharacterPosition(characterRowNewPosition, characterColumnNewPosition);
        keyEvent.consume();
    }


    // -----------------------------music------------------------------//
    public void musicBackground()
    {
        Media musicFile = new Media(getClass().getResource("/Audio/GameMusic.m4a").toString());
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
