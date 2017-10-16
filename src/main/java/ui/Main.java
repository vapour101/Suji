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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.gamehandler.GameHandler;
import logic.gamehandler.LocalGame;
import ogs.Connection;
import org.dockfx.DockNode;
import org.dockfx.DockPane;
import org.dockfx.DockPos;
import sgf.SGFReader;
import ui.controller.BoardController;
import ui.controller.DockNodeController;
import ui.controller.GameListController;
import ui.controller.NewLocalGameController;
import ui.controller.dialog.ProxySettings;
import util.LogHelper;

import java.io.*;

public class Main extends Application {

	public static Main instance;
	public DockPane dockPane;
	private Stage window;
	private MenuBar menuBar;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		instance = this;
		LogHelper.finest("Starting");
		window = primaryStage;

		primaryStage.setOnCloseRequest(event -> {
			Connection.disconnect();
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
		Menu ogsMenu = getOGSMenu();
		Menu settingsMenu = getSettingsMenu();

		//main menu bar
		menuBar = new MenuBar();
		menuBar.getMenus().addAll(fileMenu, ogsMenu, settingsMenu);
	}

	private Menu getFileMenu() {
		Menu fileMenu = new Menu("File");

		MenuItem newLocalGame = new MenuItem("New Local Game");
		newLocalGame.setOnAction(event -> {
			DockNodeController controller = new NewLocalGameController();
			DockNode node = controller.getDockNode();
			node.setTitle("New Local Game");
			node.dock(dockPane, DockPos.CENTER);
			node.setFloating(true);
		});

		MenuItem openSGFFile = new MenuItem("Open SGF File");
		openSGFFile.setOnAction(event -> {
			FileChooser.ExtensionFilter sgfFilter = new FileChooser.ExtensionFilter("SGF File", "*.sgf");

			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open SGF");
			fileChooser.getExtensionFilters().add(sgfFilter);
			fileChooser.setSelectedExtensionFilter(sgfFilter);

			File file = fileChooser.showOpenDialog(window);

			InputStreamReader is = null;
			BufferedReader reader = null;
			StringBuilder builder = new StringBuilder();

			try {
				is = new FileReader(file.getPath());
				reader = new BufferedReader(is);

				String currentLine = null;
				while ((currentLine = reader.readLine()) != null) {
					builder.append(currentLine);
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				try {
					if ( is != null )
						is.close();

					if ( reader != null )
						reader.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}

			String sgfString = builder.toString();

			SGFReader sgfReader = new SGFReader(sgfString);
			GameHandler handler = new LocalGame(sgfReader.getGameTree());
			BoardController controller = new BoardController(handler, "/fxml/localGame.fxml", false);
			DockNode node = controller.getDockNode();
			node.setTitle("SGF Game");
			node.dock(dockPane, DockPos.CENTER);
		});

		fileMenu.getItems().add(newLocalGame);
		fileMenu.getItems().add(openSGFFile);

		return fileMenu;
	}

	private Menu getOGSMenu() {
		Menu ogsMenu = new Menu("OGS");

		MenuItem ogsGameList = new MenuItem("Game List");
		ogsGameList.setOnAction(event -> {
			GameListController controller = new GameListController();

			DockNode node = controller.getDockNode();
			node.setTitle("OGS GameList");
			node.dock(dockPane, DockPos.RIGHT);
		});

		ogsMenu.getItems().add(ogsGameList);

		return ogsMenu;
	}

	private Menu getSettingsMenu() {
		Menu settings = new Menu("Settings");

		MenuItem proxy = new MenuItem("Configure Proxy");
		proxy.setOnAction(event -> {
			DockNode node = (new ProxySettings()).getDockNode();
			node.setTitle("Proxy Settings");
			node.dock(dockPane, DockPos.CENTER);
			node.setFloating(true);
		});

		settings.getItems().add(proxy);

		return settings;
	}

	private void buildDockPane() {
		dockPane = new DockPane();
		Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
		DockPane.initializeDefaultUserAgentStylesheet();
	}
}
