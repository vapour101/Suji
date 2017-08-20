/*
 * Copyright (c) 2017 Tshiwela Mulaudzi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ui;

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
