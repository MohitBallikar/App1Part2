/*
 *  UCF COP3330 Fall 2021 Application Assignment 1 Solution
 *  Copyright 2021 Mohit Ballikar
 */
package baseline;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.*;


public class ToDoListApplication extends Application
{
    private Stage stage;
    ////START OF Running CODE
    //runs program essentially
    @Override
    public void start(Stage primaryStage) throws IOException
    {
        //allows the fxml gui to load up and to be ran
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(ToDoListApplication.class.getResource("Application.fxml"));
        Parent root1 = (Parent)fxmlLoader.load();
        ApplicationController controller = (ApplicationController) fxmlLoader.getController();
        controller.setStage(stage);
        Scene scene = new Scene(root1, 900, 600);
        stage.setTitle("To Do List (Mohit Ballikar)");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
/*
Define FXMLloader and launch the program
 */