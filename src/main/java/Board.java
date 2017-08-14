package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;

import javafx.scene.paint.Color;

import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Board extends Application {

    @Override
    public void start(Stage primaryStage){
        Group root = new Group();
        Scene scene = new Scene(root, 453, 453, Color.YELLOW);

        double width = 50;
        int n =  9;
        Rectangle [][] rec = new Rectangle[n][n];

        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){

                rec[i][j] = new Rectangle();
                rec[i][j].setX(i * width);
                rec[i][j].setY(j * width);
                rec[i][j].setWidth(width);
                rec[i][j].setHeight(width);
                rec[i][j].setFill(null);

                rec[i][j].setStrokeWidth(3);
                rec[i][j].setStroke(Color.BLACK);
                root.getChildren().add(rec[i][j]);
            }
        }

        primaryStage.setScene(scene);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
