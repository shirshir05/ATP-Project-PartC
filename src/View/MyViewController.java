package View;
import algorithms.mazeGenerators.MyMazeGenerator;
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

    private MediaPlayer mediaplayerBackground;
    private boolean flagToMusicBackground = false;
    @FXML
    public MazeDisplayer mazeDisplayer;


    public MyViewController(){
        musicBackground();
    }

    public void generateMaze() {
        //int rows = Integer.valueOf(txtfld_rowsNum.getText());
        //int columns = Integer.valueOf(txtfld_columnsNum.getText());
        //this.mazeDisplayer.setMaze(getRandomMaze(rows,columns));
        //this.mazeDisplayer.setMaze(mazeData);
    }

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

    public void solve()
    {
        mazeDisplayer.displayNewSolution(MyViewModel.solveMaze());
    }

    //Prevent the focus taking problem of the TextFields
    public void mazeMouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    public void NewMazeMouseClicked(MouseEvent mouseEvent) throws IOException {
        FXMLLoader FXMLLoader  = new FXMLLoader(getClass().getResource("../View/generateMaze.fxml"));
        Parent root3 = (Parent)FXMLLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root3, 500, 600));
        stage.show();
    }

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


    @Override
    public void update(Observable o, Object arg) {

    }
}
