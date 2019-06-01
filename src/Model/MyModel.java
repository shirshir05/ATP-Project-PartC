package Model;

import Client.*;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.Maze;
import Server.*;

import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static sun.net.www.protocol.http.AuthCacheValue.Type.Server;

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
            generateRandomMaze(width,height);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //setChanged();
            //notifyObservers();
        });
    }

    private int[][] generateRandomMaze(int width, int height) {
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

        /*Random rand = new Random();
        maze = new int[width][height];
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                maze[i][j] = Math.abs(rand.nextInt() % 2);
            }
        }*/
        return maze.getM_maze();
    }

    @Override
    public int[][] getMaze() {
        if (maze == null) generateMaze(10,10);
        return maze.getM_maze();
    }

    @Override
    public void moveCharacter(KeyCode movement) {
        switch (movement) {
            case UP:
                characterPositionRow--;
                break;
            case DOWN:
                characterPositionRow++;
                break;
            case RIGHT:
                characterPositionColumn++;
                break;
            case LEFT:
                characterPositionColumn--;
                break;
        }
        //setChanged();
        //notifyObservers();
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
}
