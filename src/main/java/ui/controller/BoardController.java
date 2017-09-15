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

import event.EventBus;
import event.GameEvent;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import logic.gamehandler.GameHandler;
import ui.drawer.GameDrawer;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class BoardController extends SelfBuildingController implements Initializable {

	@FXML
	Pane boardPane;
	@FXML
	VBox sideBar;

	GameHandler game;
	Canvas boardCanvas;
	GameDrawer gameDrawer;

	BoardController() {
		game = buildGameHandler();
		EventBus.addEventHandler(GameEvent.ANY, this::gameEventHandler);
		EventBus.addEventHandler(GameEvent.GAMEOVER, this::enterScoring);
		EventBus.addEventHandler(GameEvent.REVIEWSTART, this::reviewStart);
	}

	abstract GameHandler buildGameHandler();

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		setupPanes();
		constructCanvas();

		GameEvent.fireGameEvent(game, GameEvent.START);
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

	abstract GameDrawer buildGameDrawer();

	private void resizeCanvas(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
		boardCanvas.setHeight(boardPane.getHeight());
		boardCanvas.setWidth(boardPane.getWidth());
	}

	abstract void gameEventHandler(GameEvent event);

	abstract void canvasClicked(MouseEvent mouseEvent);

	abstract void canvasHover(MouseEvent mouseEvent);

	abstract void canvasExit(MouseEvent mouseEvent);

	abstract void enterScoring(GameEvent event);

	abstract void reviewStart(GameEvent event);
}
