package sample;

import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    Stage window;
    BorderPane layout;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        window.setTitle("Suji");

        //file menu, creating the main tabs
        Menu fileMenu = new Menu ("New local game");
        Menu exitMenu = new Menu ("Exit");
        Menu helpMenu = new Menu ("Help");

        //Home items
        fileMenu.getItems().add(new MenuItem("Black's Name"));
        fileMenu.getItems().add(new MenuItem("White's Name"));
        fileMenu.getItems().add(new MenuItem("Handicap"));
        fileMenu.getItems().add(new MenuItem("Komi"));

        //main menu bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, exitMenu);

        layout = new BorderPane();

        layout.setTop(menuBar);
        Scene scene = new Scene(layout,400,300);
        window.setScene(scene);
        window.show();
    }

}
