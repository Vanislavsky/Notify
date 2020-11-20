package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Button cansel = new Button("cansel");

        Notify notify = new Notify.Builder()
                .withTitle("test title")
                .withAppName("App")
                .withMessage("test message")
                .withView(View.WITHBUTTONANDLIST)
                .withPosition(Position.RIGHT_TOP)
                .withTextField()
                .withCanselButton("cansel")
                .withIconPath("src/images/stich.jpg")
                .withBorder(Border.CIRCLE)
                .withWaitTime(10000)
                .build();
        notify.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
