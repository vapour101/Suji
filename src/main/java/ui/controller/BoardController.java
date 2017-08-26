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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import logic.Board;
import logic.BoardScorer;
import ui.drawer.BoardDrawer;
import ui.drawer.BoardScoreDrawer;
import util.*;

import java.net.URL;
import java.util.ResourceBundle;

import static util.DimensionHelper.getBoardLength;

public class BoardController implements Initializable {

	public Button passButton;
	public Label blackScore;
	public Label whiteScore;
	public Pane pane;
	public Pane scorePane;
	public Button blackDone;
	public Button whiteDone;

	private Canvas boardCanvas;
	private Board board;
	private BoardScorer boardScorer;
	private BoardDrawer boardDrawer;

	private boolean blackMove;
	private boolean pass;
	private GameState gameState;
	private double komi;

	public BoardController() {
		board = new Board();
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

		blackDone.setOnAction(event -> {
			blackDone.setDisable(true);
			if ( whiteDone.isDisabled() )
				doneScoring();
		});

		whiteDone.setOnAction(event -> {
			whiteDone.setDisable(true);
			if ( blackDone.isDisabled() )
				doneScoring();
		});
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

	private void setupPanes() {
		pane.widthProperty().addListener(this::resizeCanvas);
		pane.heightProperty().addListener(this::resizeCanvas);
		scorePane.widthProperty().addListener(this::resizeScore);
		scorePane.setVisible(false);
	}

	private void constructCanvas() {
		boardCanvas = new Canvas();
		boardCanvas.setOnMouseMoved(this::canvasHover);
		boardCanvas.setOnMouseClicked(this::canvasClicked);

		pane.getChildren().add(boardCanvas);

		boardDrawer = new BoardDrawer(boardCanvas);
	}

	private void drawBoard() {
		boardDrawer.draw(board);

		updateScore();
	}

	private void updateScore() {
		if ( gameState == GameState.SCORING ) {
			blackScore.setText(Double.toString(boardScorer.getScore(StoneColour.BLACK)));
			whiteScore.setText(Double.toString(boardScorer.getScore(StoneColour.WHITE)));
		}
	}

	void setKomi(double komi) {
		this.komi = komi;
	}

	void setHandicap(int handicap) {
		if ( board.getStones(StoneColour.BLACK).size() > 0 || board.getStones(StoneColour.WHITE).size() > 0 )
			board = new Board();

		blackMove = (handicap == 0);

		if ( handicap > 0 )
			for (Coords stone : HandicapHelper.getHandicapStones(handicap))
				board.playStone(stone, StoneColour.BLACK);
	}

	private void canvasClicked(MouseEvent mouseEvent) {
		DrawCoords mousePosition = new DrawCoords(mouseEvent.getX(), mouseEvent.getY());
		CoordProjector projector = new CoordProjector(getBoardLength(boardCanvas),
													  DimensionHelper.getTopLeftCorner(boardCanvas));
		Coords boardPos = projector.nearestCoords(mousePosition);

		if ( gameState == GameState.SCORING ) {
			blackDone.setDisable(false);
			whiteDone.setDisable(false);
			if ( mouseEvent.getButton() == MouseButton.PRIMARY )
				boardScorer.markGroupDead(boardPos);
			else if ( mouseEvent.getButton() == MouseButton.SECONDARY )
				boardScorer.unmarkGroupDead(boardPos);
		}
		else if ( gameState == GameState.PLAYING ) {
			if ( board.isLegalMove(boardPos, getTurnPlayer()) ) {
				board.playStone(boardPos, getTurnPlayer());
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

		boardDrawer.drawGhostStone(board, mousePosition, turnPlayer);
	}

	private void resizeScore(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
		VBox blackScoreBox = null;
		Separator separator = null;

		for (Node node : scorePane.getChildren()) {
			if ( node instanceof Separator )
				separator = (Separator) node;
		}

		if ( separator == null )
			return;

		for (Node node : scorePane.getChildren()) {
			if ( node instanceof VBox && node.getLayoutX() < separator.getLayoutX() )
				blackScoreBox = (VBox) node;
		}

		if ( blackScoreBox == null )
			return;

		double width = (scorePane.getWidth() - separator.getWidth()) / 2;

		blackScoreBox.setMinWidth(width);
	}

	private void resizeCanvas(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
		boardCanvas.setHeight(pane.getHeight());
		boardCanvas.setWidth(pane.getWidth());
		drawBoard();
	}

	private void pass(ActionEvent event) {
		blackMove = !blackMove;

		if ( pass ) {
			gameState = GameState.SCORING;
			boardScorer = new BoardScorer(board, komi);
			passButton.setVisible(false);
			scorePane.setVisible(true);

			boardDrawer = new BoardScoreDrawer(boardCanvas, boardScorer);

			drawBoard();
		}

		pass = true;
	}

	private enum GameState {
		PLAYING, SCORING, GAMEOVER
	}
}