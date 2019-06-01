package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.time.LocalDateTime;
import java.util.Optional;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println(System.getProperty("java.io.tmpdir"));
        /////
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("../View/StartWindows.fxml").openStream());
        primaryStage.setTitle("MAZES & DRAGONS");
        Scene scene = new Scene(root, 700, 600);
        scene.getStylesheets().add(getClass().getResource("../View/ViewStyle.css").toExternalForm());
        primaryStage.setScene(scene);
        SetStageCloseEvent(primaryStage);
        //
        primaryStage.show();

    }

    private void SetStageCloseEvent(Stage primaryStage) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to exit?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    // ... user chose OK
                    // Close program
                } else {
                    // ... user chose CANCEL or closed the dialog
                    windowEvent.consume();
                }
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
