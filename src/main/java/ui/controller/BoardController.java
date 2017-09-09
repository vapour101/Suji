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

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import logic.BoardScorer;
import logic.GameHandler;
import logic.LocalGameHandler;
import ui.drawer.BoardDrawer;
import ui.drawer.BoardScoreDrawer;
import util.*;

import java.net.URL;
import java.util.ResourceBundle;

import static util.DimensionHelper.getBoardLength;

public class BoardController implements Initializable {

	@FXML
	private Button passButton;
	@FXML
	private Pane boardPane;
	@FXML
	private VBox sideBar;

	private Canvas boardCanvas;
	private GameHandler game;
	private BoardScorer boardScorer;
	private BoardDrawer boardDrawer;
	private ScorePaneController scorePaneController;

	private boolean blackMove;
	private boolean pass;
	private GameState gameState;
	private double komi;

	public BoardController() {
		game = new LocalGameHandler();
		blackMove = true;
		pass = false;
		gameState = GameState.PLAYING;
		komi = 0;
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		setupPanes();
		constructCanvas();
		setupButtons();
		drawBoard();
	}

	private void setupButtons() {
		passButton.setOnAction(this::pass);
		scorePaneController.setDoneScoring(this::doneScoring);
	}

	private void setupPanes() {
		boardPane.widthProperty().addListener(this::resizeCanvas);
		boardPane.heightProperty().addListener(this::resizeCanvas);
		loadScorePane();
	}

	private void loadScorePane() {
		FXMLLoader scoreLoader = ScorePaneController.getScorePaneLoader();
		scorePaneController = scoreLoader.getController();
		sideBar.getChildren().add(scoreLoader.getRoot());
	}

	private void constructCanvas() {
		boardCanvas = new Canvas();
		boardCanvas.setOnMouseMoved(this::canvasHover);
		boardCanvas.setOnMouseClicked(this::canvasClicked);

		boardPane.getChildren().add(boardCanvas);

		boardDrawer = new BoardDrawer(boardCanvas);
	}

	private void drawBoard() {
		boardDrawer.draw(game.getBoard());
	}

	private void doneScoring() {
		gameState = GameState.GAMEOVER;

		displayFinalScore(boardScorer.getScore());
	}

	private void displayFinalScore(double finalScore) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Game Over");
		if ( finalScore == 0 ) {
			alert.setContentText("Game ends in a draw.");
			alert.showAndWait();
			return;
		}

		String message;

		if ( finalScore > 0 )
			message = "Black";
		else
			message = "White";

		finalScore = Math.abs(finalScore);

		message += " wins by " + Double.toString(finalScore) + " points.";

		alert.setContentText(message);
		alert.showAndWait();
	}

	void setHandicap(int handicap) {
		if ( game.getStones(StoneColour.BLACK).size() > 0 || game.getStones(StoneColour.WHITE).size() > 0 )
			game = new LocalGameHandler();

		blackMove = (handicap == 0);

		if ( handicap > 0 )
			for (Coords stone : HandicapHelper.getHandicapStones(handicap))
				game.playStone(stone, StoneColour.BLACK);
	}

	void setKomi(double komi) {
		this.komi = komi;
	}

	private void canvasClicked(MouseEvent mouseEvent) {
		DrawCoords mousePosition = new DrawCoords(mouseEvent.getX(), mouseEvent.getY());
		CoordProjector projector = new CoordProjector(getBoardLength(boardCanvas),
													  DimensionHelper.getTopLeftCorner(boardCanvas));
		Coords boardPos = projector.nearestCoords(mousePosition);

		if ( gameState == GameState.SCORING ) {
			scorePaneController.enableButtons();
			if ( mouseEvent.getButton() == MouseButton.PRIMARY )
				boardScorer.markGroupDead(boardPos);
			else if ( mouseEvent.getButton() == MouseButton.SECONDARY )
				boardScorer.unmarkGroupDead(boardPos);
		}
		else if ( gameState == GameState.PLAYING ) {
			if ( game.isLegalMove(boardPos, getTurnPlayer()) ) {
				game.playStone(boardPos, getTurnPlayer());
				blackMove = !blackMove;
				pass = false;
			}
		}

		drawBoard();
	}

	private StoneColour getTurnPlayer() {
		if ( blackMove )
			return StoneColour.BLACK;
		else
			return StoneColour.WHITE;
	}

	private void canvasHover(MouseEvent mouseEvent) {
		if ( gameState != GameState.PLAYING )
			return;

		DrawCoords mousePosition = new DrawCoords(mouseEvent.getX(), mouseEvent.getY());
		drawBoard();

		StoneColour turnPlayer = blackMove ? StoneColour.BLACK : StoneColour.WHITE;

		boardDrawer.drawGhostStone(game, mousePosition, turnPlayer);
	}

	private void resizeCanvas(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
		boardCanvas.setHeight(boardPane.getHeight());
		boardCanvas.setWidth(boardPane.getWidth());
		drawBoard();
	}

	private void pass(ActionEvent event) {
		blackMove = !blackMove;

		if ( pass ) {
			gameState = GameState.SCORING;
			boardScorer = new BoardScorer(game.getBoard(), komi);
			passButton.setVisible(false);
			scorePaneController.setVisible(true);

			boardDrawer = new BoardScoreDrawer(boardCanvas, boardScorer);

			drawBoard();
		}

		pass = true;
	}

	private enum GameState {
		PLAYING, SCORING, GAMEOVER
	}
}