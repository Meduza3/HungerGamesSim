import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("GUI.fxml")));
        primaryStage.setTitle("Hunger Games Simulator");
        primaryStage.setScene(new Scene(root, 1300, 700));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}