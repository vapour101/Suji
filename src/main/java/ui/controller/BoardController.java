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

package ui.controller;

import event.GameEvent;
import event.GamePublisher;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import logic.gamehandler.GameHandler;
import ui.drawer.*;

import java.net.URL;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.ResourceBundle;

public class BoardController extends DockNodeController implements Initializable {

	@FXML
	Pane boardPane;
	@FXML
	VBox sideBar;
	Canvas boardCanvas;
	GameDrawer gameDrawer;

	private GamePublisher game;
	private String fxmlLocation;

	private Queue<Node> sideBarItems;


	BoardController(GameHandler gameHandler, String resourcePath) {
		game = new GamePublisher(gameHandler);
		fxmlLocation = resourcePath;
		sideBarItems = new ArrayDeque<>();
	}

	public final void addToSideBar(Node node) {
		sideBarItems.add(node);
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		game.subscribe(GameEvent.GAMEOVER, this::enterScoring);
		game.subscribe(GameEvent.REVIEWSTART, this::reviewStart);

		setupPanes();
		constructCanvas();
		setupSideBar();
	}

	void setupPanes() {
		boardPane.widthProperty().addListener(this::resizeCanvas);
		boardPane.heightProperty().addListener(this::resizeCanvas);
	}

	private void constructCanvas() {
		boardCanvas = new Canvas();
		boardCanvas.setOnMouseMoved(this::canvasHover);
		boardCanvas.setOnMouseClicked(this::canvasClicked);
		boardCanvas.setOnMouseExited(this::canvasExit);

		boardPane.getChildren().add(boardCanvas);

		gameDrawer = buildGameDrawer();
	}

	GameDrawer buildGameDrawer() {
		GameDrawer drawer = new GameDrawer(boardCanvas, getPublisher());

		Image blackStone = new Image("/images/black.png", false);
		Image whiteStone = new Image("/images/white.png", false);

		StoneDrawer stoneDrawer = new TexturedStoneDrawer(boardCanvas, blackStone, whiteStone);
		drawer.setStoneDrawer(stoneDrawer);

		Image wood = new Image("/images/wood.jpg", false);
		Image lines = new Image("/images/grid.png", false);

		BoardDrawer boardDrawer = new TexturedBoardDrawer(boardCanvas, wood, lines);
		drawer.setBoardDrawer(boardDrawer);

		return drawer;
	}

	GamePublisher getPublisher() {
		return game;
	}

	private void setupSideBar() {
		while (!sideBarItems.isEmpty()) {
			sideBar.getChildren().add(sideBarItems.remove());
		}
	}

	GameHandler getGame() {
		return game;
	}

	private void resizeCanvas(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
		boardCanvas.setHeight(boardPane.getHeight());
		boardCanvas.setWidth(boardPane.getWidth());
	}

	void canvasClicked(MouseEvent mouseEvent) {
	}

	void canvasHover(MouseEvent mouseEvent) {
	}

	void canvasExit(MouseEvent mouseEvent) {
	}

	void enterScoring(GameEvent event) {
	}

	void reviewStart(GameEvent event) {
	}

	@Override
	protected String getResourcePath() {
		return fxmlLocation;
	}
}
