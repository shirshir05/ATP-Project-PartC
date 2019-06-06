package Model;

import Client.Client;
import Client.IClientStrategy;
import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MyModel extends Observable implements IModel {

    private ExecutorService threadPool = Executors.newCachedThreadPool();
    Server mazeGeneratingServer;
    Server solveSearchProblemServer;
    Maze maze;
    private int characterPositionRow;
    private int characterPositionColumn;
    //---------------------elements for saving the maze-----------------------//
    private final String NAME_FILE = "NAMEFILE.txt";
    private HashMap<String ,Integer> sizes = new HashMap<>();//key = nameOfFile, value = sizeOfByteArray


    public MyModel() {
        //Raise the servers
         mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
         solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());

         //load existing mazes from file:
        try {
            List<String> list = Files.readAllLines(Paths.get(System.getProperty("java.io.tmpdir") + NAME_FILE));
            list.forEach(line -> {
                if (line.length() > 0) {
                    String[] l = line.split(" ");
                    sizes.put(l[0], Integer.parseInt(l[1]));
                }
            });
        } catch (NoSuchFileException e) {
            try {
                File f = new File(System.getProperty("java.io.tmpdir")+NAME_FILE);
                f.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServers() {
        mazeGeneratingServer.start();
        solveSearchProblemServer.start();
    }

    public void stopServers() {
        threadPool.shutdown();
        try{
            threadPool.awaitTermination(1, TimeUnit.HOURS);
        }
        catch (InterruptedException e){
            //System.out.println("Error await termination for ThreadPool" + e);
        }
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
        if (maze == null) return null;
        //maze.setStartPosition(characterPositionRow,characterPositionColumn);
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

    public void saveMaze() {
        if (maze == null) return;
        try {
            String tempFolder = System.getProperty("java.io.tmpdir");
            String name =  LocalDateTime.now().toString();
            name = name.replace(':','-');
            name = name.replace('.','-');
            name = name.replace('.','0');
            String file = tempFolder +name +".txt";
            byte[] toWrite = maze.toByteArray();
            sizes.put(name,toWrite.length);
            appendToFiles(name,toWrite.length);
            File f = new File(file);
            f.createNewFile();
            OutputStream out = new MyCompressorOutputStream(new FileOutputStream(file));
            out.write(toWrite);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getListOfSavedFiles()
    {
        ArrayList<String> mazes = new ArrayList<>();
        mazes.addAll(sizes.keySet());
        return mazes;
    }

    //adds the saved maze to the file
    public void appendToFiles(String fileName, int length)
            throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(System.getProperty("java.io.tmpdir")+NAME_FILE,true));
        writer.newLine();
        writer.append(fileName + " "+length);
        writer.close();
    }

    @Override
    public void createFileFromDB(String fileName) {
        try {
            String tempFolder = System.getProperty("java.io.tmpdir");
            String fileLocation = tempFolder + fileName + ".txt";
            InputStream in = new MyDecompressorInputStream(new FileInputStream(fileLocation));
            byte currByteArray[] = new byte[sizes.get(fileName)];
            in.read(currByteArray);
            in.close();
            maze = new Maze(currByteArray);
            characterPositionColumn = maze.getStartPosition().getColumnIndex();
            characterPositionRow = maze.getStartPosition().getRowIndex();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            setChanged();
            notifyObservers();
        }
    }


    public Maze getMaze(){
        return maze;
    }

    public void keyPressed(KeyEvent keyEvent) {
        if(maze==null) return;
        int[][] mazeArray = maze.getM_maze();
        if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() ==KeyCode.DIGIT8 || keyEvent.getCode() == KeyCode.NUMPAD8) {//-1 , 0
            if(characterPositionRow-1 > maze.getNumberOfRows()-1 || characterPositionColumn > maze.getNumberOfColumns()-1 || characterPositionRow-1 <0 ||characterPositionColumn<0)  {
                musicFail();
            }

            else if(mazeArray[characterPositionRow-1][characterPositionColumn] == 1){
                musicFail();
            }
            else{
                characterPositionRow = characterPositionRow - 1;
                characterPositionColumn = characterPositionColumn;
            }

        } else if (keyEvent.getCode() == KeyCode.DOWN ||  keyEvent.getCode() ==KeyCode.DIGIT2 || keyEvent.getCode() == KeyCode.NUMPAD2) { // 1 ,0
            if(characterPositionRow+1 > maze.getNumberOfRows()-1||  characterPositionColumn > maze.getNumberOfColumns()-1 || characterPositionRow+1 < 0 ||characterPositionColumn < 0 )  {
                musicFail();
            }

            else if(mazeArray[characterPositionRow+1][characterPositionColumn] == 1){
                musicFail();

            }
            else{
                characterPositionRow = characterPositionRow + 1;
                characterPositionColumn = characterPositionColumn;
            }

        } else if (keyEvent.getCode() == KeyCode.RIGHT ||  keyEvent.getCode() ==KeyCode.DIGIT6 || keyEvent.getCode() == KeyCode.NUMPAD6) { // 0 ,1
            if(characterPositionRow > maze.getNumberOfRows()-1 || characterPositionColumn+1 > maze.getNumberOfColumns()-1 || characterPositionRow < 0 ||characterPositionColumn+1 < 0 )  {
                musicFail();
            }
            else if(mazeArray[characterPositionRow][characterPositionColumn+1] == 1){
                musicFail();
            }
            else{
                characterPositionRow = characterPositionRow;
                characterPositionColumn = characterPositionColumn + 1;
            }

        } else if (keyEvent.getCode() == KeyCode.LEFT  || keyEvent.getCode() ==KeyCode.DIGIT4 || keyEvent.getCode() == KeyCode.NUMPAD4) {//0 ,-1
            if(characterPositionRow > maze.getNumberOfRows()-1 || characterPositionColumn-1 >maze.getNumberOfColumns()-1 || characterPositionRow <0 || characterPositionColumn-1 <0)  {
                musicFail();
            }
            else if(mazeArray[characterPositionRow][characterPositionColumn-1] == 1){
                musicFail();
            }
            else{
                characterPositionRow = characterPositionRow;
                characterPositionColumn = characterPositionColumn - 1;
            }
        }  else if (keyEvent.getCode() == KeyCode.DIGIT7 || keyEvent.getCode() == KeyCode.NUMPAD7) {//Upper left -1, -1
            if(characterPositionRow-1 > maze.getNumberOfRows()-1 || characterPositionColumn-1 > maze.getNumberOfColumns()-1 || characterPositionColumn-1 < 0 || characterPositionRow-1 < 0)  {
                musicFail();
            }
            else if(mazeArray[characterPositionRow-1][characterPositionColumn-1] == 1){
                musicFail();
            }
            else{
                characterPositionRow = characterPositionRow-1;
                characterPositionColumn = characterPositionColumn - 1;
            }

        }else if (keyEvent.getCode() == KeyCode.DIGIT9 || keyEvent.getCode() == KeyCode.NUMPAD9) {//Upper right -1 1
            if(characterPositionRow-1 > maze.getNumberOfRows()-1 || characterPositionColumn+1 > maze.getNumberOfColumns()-1 || characterPositionRow-1<0 || characterPositionColumn+1<0)  {
                musicFail();
            }
            else if(mazeArray[characterPositionRow-1][characterPositionColumn+1] == 1){
                musicFail();
            }
            else{
                characterPositionRow = characterPositionRow-1;
                characterPositionColumn = characterPositionColumn + 1;
            }
        }else if (keyEvent.getCode() == KeyCode.DIGIT1 || keyEvent.getCode() == KeyCode.NUMPAD1) {//Lower left 1 -1
            if(characterPositionRow+1 > maze.getNumberOfRows()-1 || characterPositionColumn-1 > maze.getNumberOfColumns()-1 || characterPositionRow+1<0 ||characterPositionColumn-1 <0 )  {
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
        else if (keyEvent.getCode() == KeyCode.DIGIT3 || keyEvent.getCode() == KeyCode.NUMPAD3) {//Lower right 1 1
            if(characterPositionRow+1 > maze.getNumberOfRows()-1 || characterPositionColumn+1 > maze.getNumberOfColumns()-1 || characterPositionRow+1 < 0 || characterPositionColumn+1 <0)  {
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
            characterPositionColumn = maze.getStartPosition().getColumnIndex();
        }
        else{
            keyEvent.consume();
            return;
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
