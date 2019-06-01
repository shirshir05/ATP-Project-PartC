package Model;

import Client.*;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.Maze;
import Server.*;

import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.*;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyModel extends Observable implements IModel {

    private ExecutorService threadPool = Executors.newCachedThreadPool();
    Server mazeGeneratingServer;
    Server solveSearchProblemServer;
    Maze maze;
    private int characterPositionRow;
    private int characterPositionColumn;


    public MyModel() {
        //Raise the servers
         mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
         solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
    }

    public void startServers() {
        mazeGeneratingServer.start();
        solveSearchProblemServer.start();
    }

    public void stopServers() {
        mazeGeneratingServer.stop();
        solveSearchProblemServer.stop();
    }

    @Override
    public void generateMaze(int width, int height) {
            //Generate maze
        threadPool.execute(() -> {
            generateMazeClient(width,height);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setChanged();
            notifyObservers();
        });
    }

    private int[][] generateMazeClient(int width, int height) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        toServer.flush();
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);

                        int[] mazeDimensions = new int[]{width,height};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new
                                ByteArrayInputStream(compressedMaze));
                        //CHANGE SIZE ACCORDING TO YOU MAZE SIZE
                        byte[] decompressedMaze = new byte[width*height+30];
                        //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        maze = new Maze(decompressedMaze);
                        characterPositionColumn = maze.getStartPosition().getColumnIndex();
                        characterPositionRow = maze.getStartPosition().getRowIndex();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return maze.getM_maze();
    }



    @Override
    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    @Override
    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    @Override
    public ArrayList<int[]> solveMaze() {
        ArrayList<int[]>solution = new ArrayList<>();
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                        @Override
                        public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                            try {
                                ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                                ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                                toServer.flush();
                                toServer.writeObject(maze); //send maze to server
                                toServer.flush();
                                Solution mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                                ArrayList<AState> mazeSolutionSteps = mazeSolution.getSolutionPath();
                                for (int i = 0; i < mazeSolutionSteps.size(); i++)
                                {//adds the solution steps to the array, in the same order they are received
                                    int[]position = new int [2] ;
                                    position[0] = ((MazeState)mazeSolutionSteps.get(i)).getRow();
                                    position[1] = ((MazeState)mazeSolutionSteps.get(i)).getCol();
                                    solution.add(i,position);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return solution;
    }

    public Maze getMaze(){
        return maze;
    }

    public void KeyPressed(KeyEvent keyEvent) {
        int[][] mazeArray = maze.getM_maze();
        if (keyEvent.getCode() == KeyCode.UP) {
            if(characterPositionRow-1 <= maze.getNumberOfRows()-1 && characterPositionColumn >= maze.getNumberOfColumns()-1)  {
                musicFail();
            }

            else if(mazeArray[characterPositionRow-1][characterPositionColumn] == 1){
                musicFail();
            }
            else{
                characterPositionRow = characterPositionRow - 1;
                characterPositionColumn = characterPositionColumn;
            }

        } else if (keyEvent.getCode() == KeyCode.DOWN) {
            if(characterPositionRow+1 <= maze.getNumberOfRows()-1 && characterPositionColumn >= maze.getNumberOfColumns()-1)  {
                musicFail();
            }

            else if(mazeArray[characterPositionRow+1][characterPositionColumn] == 1){
                musicFail();

            }
            else{
                characterPositionRow = characterPositionRow + 1;
                characterPositionColumn = characterPositionColumn;
            }

        } else if (keyEvent.getCode() == KeyCode.RIGHT) {
            if(characterPositionRow <= maze.getNumberOfRows()-1 && characterPositionColumn+1 >= maze.getNumberOfColumns()-1)  {
                musicFail();
            }
            else if(mazeArray[characterPositionRow][characterPositionColumn+1] == 1){
                musicFail();
            }
            else{
                characterPositionRow = characterPositionRow;
                characterPositionColumn = characterPositionColumn + 1;
            }

        } else if (keyEvent.getCode() == KeyCode.LEFT) {
            if(characterPositionRow <= maze.getNumberOfRows()-1 && characterPositionColumn-1 >= maze.getNumberOfColumns()-1)  {
                musicFail();
            }
            else if(mazeArray[characterPositionRow][characterPositionColumn-1] == 1){
                musicFail();
            }
            else{
                characterPositionRow = characterPositionRow;
                characterPositionColumn = characterPositionColumn - 1;
            }
        }  else if (keyEvent.getCode() == KeyCode.DIGIT7) {//Upper left
            if(characterPositionRow-1 <= maze.getNumberOfRows()-1 && characterPositionColumn-1 >= maze.getNumberOfColumns()-1)  {
                musicFail();
            }
            else if(mazeArray[characterPositionRow-1][characterPositionColumn-1] == 1){
                musicFail();
            }
            else{
                characterPositionRow = characterPositionRow-1;
                characterPositionColumn = characterPositionColumn - 1;
            }

        }else if (keyEvent.getCode() == KeyCode.DIGIT9) {//Upper right
            if(characterPositionRow-1 <= maze.getNumberOfRows()-1 && characterPositionColumn+1 >= maze.getNumberOfColumns()-1)  {
                musicFail();
            }
            else if(mazeArray[characterPositionRow-1][characterPositionColumn+1] == 1){
                musicFail();
            }
            else{
                characterPositionRow = characterPositionRow-1;
                characterPositionColumn = characterPositionColumn + 1;
            }
        }else if (keyEvent.getCode() == KeyCode.DIGIT1) {//Lower left
            if(characterPositionRow+1 <= maze.getNumberOfRows()-1 && characterPositionColumn-1 >= maze.getNumberOfColumns()-1)  {
                musicFail();
            }
            else if(mazeArray[characterPositionRow+1][characterPositionColumn-1] == 1){
                musicFail();
            }
            else{
                characterPositionRow = characterPositionRow +1;
                characterPositionColumn = characterPositionColumn -1;
            }

        }
        else if (keyEvent.getCode() == KeyCode.DIGIT3) {//Lower right
            if(characterPositionRow+1 <= maze.getNumberOfRows()-1 && characterPositionColumn+1 >= maze.getNumberOfColumns()-1)  {
                musicFail();
            }
            else if(mazeArray[characterPositionRow+1][characterPositionColumn+1] == 1){
                musicFail();
            }
            else{
                characterPositionRow = characterPositionRow +1;
                characterPositionColumn = characterPositionColumn +1;
            }

        }
        else if (keyEvent.getCode() == KeyCode.HOME) {
            characterPositionRow =maze.getStartPosition().getRowIndex();
            characterPositionColumn = maze.getStartPosition().getRowIndex();
        }
        //Updates the MazeDisplayer
        keyEvent.consume();
        setChanged();
        notifyObservers();

    }

    public void musicFail()
    {
        Media musicFile = new Media(getClass().getResource("/Audio/Correct sound effect and wrong sound effect (mp3cut.net).mp3").toString());
        MediaPlayer mediaplayerBackground = new MediaPlayer(musicFile);
        mediaplayerBackground.setVolume(0.3);
        mediaplayerBackground.play();
    }

}
