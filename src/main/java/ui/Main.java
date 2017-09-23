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
import netcode.OGSConnection;
import org.dockfx.DockNode;
import org.dockfx.DockPane;
import org.dockfx.DockPos;
import ui.controller.GameListController;
import ui.dialog.LocalGameDialog;
import util.LogHelper;

public class Main extends Application {

	private Stage window;
	private DockPane dockPane;
	private MenuBar menuBar;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		LogHelper.finest("Starting");
		window = primaryStage;
		primaryStage.setOnHidden(event -> {
			OGSConnection.disconnect();
			System.exit(0);
		});
		buildWindow();
	}

	private void buildWindow() {
		window.setTitle("Suji");

		window.setScene(getScene());
		window.sizeToScene();

		window.show();
	}

	private Scene getScene() {
		buildDockPane();
		buildMenuBar();

		BorderPane layout = new BorderPane();

		layout.setTop(menuBar);
		layout.setCenter(dockPane);

		return new Scene(layout, 800, 450);
	}

	private void buildMenuBar() {
		//file menu, creating the main tabs
		Menu fileMenu = getFileMenu();
		Menu exitMenu = getExitMenu();

		//main menu bar
		menuBar = new MenuBar();
		menuBar.getMenus().addAll(fileMenu, exitMenu);
	}

	private Menu getFileMenu() {
		Menu fileMenu = new Menu("New...");

		MenuItem newLocalGame = new MenuItem("Local Game");
		newLocalGame.setOnAction(event -> {
			DockNode node = LocalGameDialog.build();
			node.dock(dockPane, DockPos.CENTER);
		});

		MenuItem ogsGameList = new MenuItem("OGS Game List");
		ogsGameList.setOnAction(event -> {
			GameListController controller = new GameListController();
			DockNode node = new DockNode(controller.build(), "OGS GameList");
			node.dock(dockPane, DockPos.RIGHT);
		});

		//Home items
		fileMenu.getItems().add(newLocalGame);
		fileMenu.getItems().add(ogsGameList);

		return fileMenu;
	}

	private Menu getExitMenu() {
		Menu exitMenu = new Menu();

		Label exitLabel = new Label("Exit");
		exitLabel.setOnMouseClicked(event -> window.close());
		exitMenu.setGraphic(exitLabel);

		return exitMenu;
	}

	private void buildDockPane() {
		dockPane = new DockPane();
		Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
		DockPane.initializeDefaultUserAgentStylesheet();
	}
}
