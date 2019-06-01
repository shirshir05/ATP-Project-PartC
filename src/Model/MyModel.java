package Model;

import Client.*;
import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.Maze;
import Server.*;

import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.*;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyModel extends Observable implements IModel {

    private ExecutorService threadPool = Executors.newCachedThreadPool();
    Server mazeGeneratingServer;
    Server solveSearchProblemServer;
    Maze maze;
    private int characterPositionRow;
    private int characterPositionColumn;
    private HashMap<Integer,Integer> sizes = new HashMap<>();//key = count, value = sizeOfByteArray

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
        maze = (new MyMazeGenerator()).generate(10,10);////temp
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

    public void saveMaze() {
        try {
            String tempFolder = System.getProperty("java.io.tmpdir");
            String file = tempFolder + LocalDateTime.now() +".txt";
            OutputStream out = new MyCompressorOutputStream(new FileOutputStream(file));
            byte[] toWrite = maze.toByteArray();
            sizes.put(LocalDateTime.now().hashCode(),toWrite.length);
            out.write(toWrite);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getListOfSavedMazes()
    {
        String tempFolder = System.getProperty("java.io.tmpdir");
        ArrayList<String> mazes = new ArrayList<>();
/*

        String fileLocation = tempFolder + "/m" + i + ".txt";
            InputStream in = new MyDecompressorInputStream(new FileInputStream(fileLocation));
            byte currByteArray[] = new byte[sizes.get(i)];
            in.read(currByteArray);
            in.close();
            Maze maze = new Maze(currByteArray);
            stop = Arrays.equals(maze.toByteArray(), byteMazeFromClient);

*/

        return mazes;
    }

    public Maze getMazeFromFile(String nameOfFile)
    {
        Maze mazeToReturn = null;
        return mazeToReturn;
    }

}
