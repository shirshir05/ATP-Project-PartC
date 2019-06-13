package View;

import algorithms.mazeGenerators.Maze;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.transform.Rotate;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Optional;
import java.util.ResourceBundle;


public class MyViewController extends AController implements Initializable {

    // ----------music Background-----//
    private MediaPlayer mediaplayerBackground;
    private boolean flagToMusicBackground = false;
    public javafx.scene.control.Button music;


    //show the maze
    @FXML
    public MazeDisplayer mazeDisplayer;
    public Menu loadMazeMenu;
   // public Pane pane;


    //A constructor that plays the music calls it when the window opens


    //<editor-fold desc="Data Binding for Maze size">

    public BorderPane BorderPane;
    public VBox VBox;
    //public Pane pane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Set Binding for Properties
        musicBackground();
        BorderPane.prefWidthProperty().bind(getStage().widthProperty().multiply(0.9));//0.9
        BorderPane.prefHeightProperty().bind(getStage().heightProperty().multiply(0.7));//0.7
        VBox.prefWidthProperty().bind(BorderPane.prefWidthProperty());
        VBox.prefHeightProperty().bind(BorderPane.prefHeightProperty()/*.multiply(0.5)*/);
        //pane.prefWidthProperty().bind(VBox.prefWidthProperty());
        //pane.prefHeightProperty().bind(VBox.prefHeightProperty());
        mazeDisplayer.widthProperty().bind(/*pane*/BorderPane.prefWidthProperty());
        mazeDisplayer.heightProperty().bind(/*pane*/BorderPane.prefHeightProperty());

    }


    public  void setOnScroll(){
        currentStage.getScene().setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                zoomScroll(event);
            }
        });
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
        stage.setTitle("New Maze");
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
        currentStage.getScene().setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                zoomScroll(event);
            }
        });
        // thr princes get to ths end
        if(characterPositionRow ==  maze.getGoalPosition().getRowIndex() && characterPositionColumn == maze.getGoalPosition().getColumnIndex()){
            Media musicSound = new Media(getClass().getResource("/Audio/Success Sound Effects All Sounds.mp3").toString());
            MediaPlayer musicSound1 = new MediaPlayer(musicSound);
            musicSound1.setVolume(0.5);
            musicSound1.play();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Love Wins!");
            alert.setHeaderText("You've Reached the End - Very Good");
            alert.show();

        }

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


    public void keyPressed(KeyEvent keyEvent) {
        MyViewModel.keyPressed(keyEvent);
    }

    //------------------------------menu bar---------------------------//

    public void saveMaze()
    {
        MyViewModel.saveMaze();
        if (!(MyViewModel.getMaze() == null)) {//alert will show only if there is a maze
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("");
            alert.setHeaderText("Maze Saved");
            alert.show();
        }
    }

    public  void setClose() {
        SetStageCloseEvent(currentStage);

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
            loadMazeMenu.getItems().remove(0, loadMazeMenu.getItems().size()-1);//removes all but the first one
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
        mediaplayerBackground.play();
        mediaplayerBackground.setVolume(0.1);
        flagToMusicBackground = true;
        //music.setDisable(false);
    }

    public void StopMusicBackground() {

        if(flagToMusicBackground == false){
            mediaplayerBackground.play();
            flagToMusicBackground = true;

        }
       else{
            flagToMusicBackground =  false;
            mediaplayerBackground.stop();


        }


    }

    //---------------------------help------------------------//

    public void openHelp() throws IOException {

        FXMLLoader FXMLLoader  = new FXMLLoader(getClass().getResource("../View/HelpWindows.fxml"));
        Parent root3 = (Parent)FXMLLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Help");
        Scene s = new Scene(root3,570,609);
        s.getStylesheets().add(getClass().getResource("../View/ViewStyle.css").toExternalForm());

        stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes

        stage.setScene(s);
        stage.show();
    }

    //---------------------------Properties------------------------//

    public void openProperties() throws IOException {

        FXMLLoader FXMLLoader  = new FXMLLoader(getClass().getResource("../View/Properties.fxml"));
        Parent root3 = (Parent)FXMLLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Properties");
        Scene s = new Scene(root3,464,319);
        s.getStylesheets().add(getClass().getResource("../View/ViewStyle.css").toExternalForm());

        stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes

        stage.setScene(s);
        stage.show();
    }


    //-------------------------About--------------------//

    public void About(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("About");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("AboutWindows.fxml").openStream());
            Scene scene = new Scene(root, 495, 333);
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("../View/ViewStyle.css").toExternalForm());
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {

        }
    }

/*
    public void moveWitheMouse(DragEvent eventHandler){
        MyViewModel.keyPressedMouse(eventHandler,mazeDisplayer.getHeight(),mazeDisplayer.getWidth());

    }
*/
    //-------------------------ZOOM--------------------//

    public void zoomScroll(ScrollEvent event){
        if(event.isControlDown()){
            double change = event.getDeltaY();
            double zoomConst = 1.03;
            if(change < 0){
                zoomConst = 0.97;
            }

            mazeDisplayer.setScaleY(mazeDisplayer.getScaleY()*zoomConst);
            mazeDisplayer.setScaleX(mazeDisplayer.getScaleX()*zoomConst);
            event.consume();


        }
    }

    //-------------------------Exit--------------------//

    public void exitMaze()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK
            // Close program
            AController.freeProgram();

        }
    }



}
