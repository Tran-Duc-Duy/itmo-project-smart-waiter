package com.example.itmoprojectsmartwaiter;

import com.example.itmoprojectsmartwaiter.model.Sound_cdjv;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class SmartWaiterApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //declare FXML
        FXMLLoader fxmlLoader = new FXMLLoader(SmartWaiterApplication.class.getResource("home-view.fxml"));
        //setting stage
        stage.setTitle("Smart Waiter!");
        stage.initStyle(StageStyle.TRANSPARENT);
        //declare and setting scene
        Scene scene = new Scene(fxmlLoader.load());
        scene.setFill(Color.TRANSPARENT);
        //setting stage
        stage.getIcons().add(new Image(Objects.requireNonNull(SmartWaiterApplication.class.getResourceAsStream("img/Chef.png"))));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        //thread for start music
        Thread music =new Thread(() -> {
            String currentDirectory = System.getProperty("user.dir");
            String newPath= currentDirectory.replaceAll("\\\\","\\\\");
            Sound_cdjv sound=new Sound_cdjv(newPath+"\\audio.wav");
            sound.run();
        });
        music.setDaemon(true);
        music.start();
        //run app
        launch();
    }
}
