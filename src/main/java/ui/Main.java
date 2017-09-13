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
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.dockfx.DockNode;
import org.dockfx.DockPane;
import org.dockfx.DockPos;
import ui.dialog.LocalGameDialog;

public class Main extends Application {

	private Stage window;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		window.setTitle("Suji");
		DockPane dockPane = new DockPane();

		//file menu, creating the main tabs
		Menu fileMenu = new Menu("New...");
		Menu exitMenu = new Menu();

		Label exitLabel = new Label("Exit");
		exitLabel.setOnMouseClicked(event -> window.close());
		exitMenu.setGraphic(exitLabel);

		MenuItem newLocalGame = new MenuItem("Local Game");
		newLocalGame.setOnAction(event -> {
			DockNode node = LocalGameDialog.build();
			node.dock(dockPane, DockPos.CENTER);
		});

		//Home items
		fileMenu.getItems().add(newLocalGame);

		//main menu bar
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(fileMenu, exitMenu);

		BorderPane layout = new BorderPane();

		layout.setTop(menuBar);
		layout.setCenter(dockPane);

		Scene scene = new Scene(layout, 800, 450);

		Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
		DockPane.initializeDefaultUserAgentStylesheet();

		window.setScene(scene);
		window.sizeToScene();
		window.show();
	}
}
