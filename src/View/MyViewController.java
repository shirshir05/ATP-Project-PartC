package View;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;



public class MyViewController implements IView {


    private MediaPlayer mediaplayer;
    private boolean flagToMusic = false;


    @FXML
    public MazeDisplayer mazeDisplayer;

    int[][] mazeData = { // a stub...
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0, 1},
            {0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 1, 1},
            {1, 1, 1, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1},
            {1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 1},
            {1, 1, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1},
            {1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1},
            {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1}
    };


    public void generateMaze() {
        //int rows = Integer.valueOf(txtfld_rowsNum.getText());
        //int columns = Integer.valueOf(txtfld_columnsNum.getText());
        //this.mazeDisplayer.setMaze(getRandomMaze(rows,columns));
        this.mazeDisplayer.setMaze(mazeData);
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

    //Prevent the focus taking problem of the TextFields
    public void mazeMouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }


    public void music()
    {
        Media musicFile = new Media(getClass().getResource("/Audio/song.mp3").toString());
        mediaplayer = new MediaPlayer(musicFile);
        mediaplayer.setVolume(0.1);
        mediaplayer.play();
        flagToMusic = true;
    }

    public void StopMusic() {
        if(flagToMusic){
            mediaplayer.stop();
            flagToMusic =  false;

        }

    }

}
