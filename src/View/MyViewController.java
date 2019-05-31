package View;
import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

import java.io.FileInputStream;
import java.io.IOException;

public class MyViewController implements IView{




    public void music()
    {
        AudioPlayer MGP = AudioPlayer.player;
        AudioStream BGM;
        ContinuousAudioDataStream loop = null;
        try {
            BGM= new AudioStream(new FileInputStream("resources/song"));

            AudioData MD = BGM.getData();
            loop =  new ContinuousAudioDataStream(MD);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MGP.start(loop);


    }




}
