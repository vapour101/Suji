/*
 * Copyright (c) 2017
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ui.controller.NewLocalGameController;

public class Main extends Application {

	private Stage window;
	private Parent newLocalGameMenu;

	public static void main(String[] args) {
		launch(args);
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/newLocalGame.fxml"));

		window = primaryStage;
		window.setTitle("Suji");

		newLocalGameMenu = loader.load();
		loader.<NewLocalGameController>getController().setWindow(window);

		//file menu, creating the main tabs
		Menu fileMenu = new Menu("New...");
		Menu exitMenu = new Menu("Exit");

		MenuItem newLocalGame = new MenuItem("Local Game");
		newLocalGame.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				window.setScene(new Scene(newLocalGameMenu));
				window.show();
			}
		});

		//Home items
		fileMenu.getItems().add(newLocalGame);

		//main menu bar
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(fileMenu, exitMenu);

		BorderPane layout = new BorderPane();

		layout.setTop(menuBar);
		Scene scene = new Scene(layout, 400, 300);
		window.setScene(scene);
		window.show();
	}
}
