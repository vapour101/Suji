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
import event.ScoreEvent;
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
import javafx.stage.FileChooser;
import logic.BoardScorer;
import logic.GameHandler;
import logic.GameHandlerEventDecorator;
import logic.LocalGameHandler;
import sgf.SGFWriter;
import sgf.SimpleSGFWriter;
import ui.drawer.BoardDrawer;
import ui.drawer.BoardScoreDrawer;
import util.*;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

import static util.DimensionHelper.getBoardLength;
import static util.Move.play;

public class BoardController implements Initializable {

	@FXML
	private Button saveButton;
	@FXML
	private Button undoButton;
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

	private GameState gameState;
	private double komi;

	public BoardController() {
		game = buildGameHandler(0);
		gameState = GameState.PLAYING;
		komi = 0;

		EventBus.addEventHandler(GameEvent.ANY, this::gameEventHandler);
		EventBus.addEventHandler(GameEvent.GAMEOVER, this::enterScoring);
	}

	private GameHandler buildGameHandler(int handicap) {
		GameHandler result = new LocalGameHandler(handicap);
		return new GameHandlerEventDecorator(result);
	}

	private void gameEventHandler(GameEvent event) {
		if ( event.getHandler() == game )
			drawBoard();
	}

	private void drawBoard() {
		if ( gameState == GameState.SCORING )
			return;
		boardDrawer.draw();
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		setupPanes();
		constructCanvas();
		setupButtons();
		GameEvent.fireGameEvent(game, GameEvent.START);
	}

	private void setupButtons() {
		passButton.setOnAction(this::pass);
		undoButton.setOnAction(this::undo);
		saveButton.setOnAction(this::save);
		scorePaneController.setDoneScoring(this::doneScoring);
		saveButton.setVisible(false);
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

		boardDrawer = new BoardDrawer(boardCanvas, game);
	}

	private void save(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Game As...");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SGF", "*.sgf"));
		File file = fileChooser.showSaveDialog(null);

		if ( file != null ) {
			try {
				if ( !file.exists() )
					file.createNewFile();

				Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));

				SGFWriter sgf = new SimpleSGFWriter(game.getGameTree().getSequence());

				writer.write(sgf.getSGFString());
				writer.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void undo(ActionEvent event) {
		game.undo();
	}

	private void canvasHover(MouseEvent mouseEvent) {
		if ( gameState != GameState.PLAYING )
			return;

		DrawCoords mousePosition = new DrawCoords(mouseEvent.getX(), mouseEvent.getY());
		drawBoard();
		boardDrawer.drawGhostStone(mousePosition, getTurnPlayer());
	}

	private StoneColour getTurnPlayer() {
		return game.getTurnPlayer();
	}

	private void doneScoring() {
		gameState = GameState.GAMEOVER;

		saveButton.setVisible(true);
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
			game = buildGameHandler(handicap);

		if ( handicap > 0 )
			for (Coords stone : HandicapHelper.getHandicapStones(handicap))
				game.playMove(play(stone, StoneColour.BLACK));
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
			if ( game.isLegalMove(play(boardPos, getTurnPlayer())) ) {
				game.playMove(play(boardPos, getTurnPlayer()));
			}
		}
	}

	private void resizeCanvas(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
		boardCanvas.setHeight(boardPane.getHeight());
		boardCanvas.setWidth(boardPane.getWidth());
	}

	private void pass(ActionEvent event) {
		game.pass();
	}

	private void enterScoring(GameEvent event) {
		if ( event.getHandler() != game )
			return;

		gameState = GameState.SCORING;
		boardScorer = new BoardScorer(event.getBoard(), komi);
		passButton.setVisible(false);
		undoButton.setVisible(false);
		scorePaneController.setScorer(boardScorer);
		scorePaneController.setVisible(true);

		boardDrawer = new BoardScoreDrawer(boardCanvas, game, boardScorer);
		boardDrawer.draw();
		ScoreEvent.fireEvent(EventBus.getInstance(), new ScoreEvent(boardScorer, EventBus.getInstance()));
	}

	private enum GameState {
		PLAYING, SCORING, GAMEOVER
	}
}