package View;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


import javafx.scene.input.MouseEvent;



public class MyViewController implements IView{


    private MediaPlayer mediaplayer;
    private boolean flagToMusic = false;

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




    public void mazeMouseClicked(MouseEvent mouseEvent) {
    }
}
