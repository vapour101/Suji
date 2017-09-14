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
import event.decorators.GameHandlerEventDecorator;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import logic.gamehandler.GameHandler;
import logic.gamehandler.LocalGameHandler;
import logic.score.Scorer;
import ui.drawer.BoardDrawer;
import ui.drawer.BoardScoreDrawer;
import util.*;

import java.net.URL;
import java.util.ResourceBundle;

import static util.DimensionHelper.getBoardLength;
import static util.Move.play;

public class LocalGameController extends BoardController {

	private Scorer boardScorer;

	private ScorePaneController scorePaneController;
	private GameMenuController gameMenuController;
	private ReviewPanelController reviewPanelController;

	private GameState gameState;

	public LocalGameController() {
		gameState = GameState.PLAYING;
	}

	@Override
	protected String getResourcePath() {
		return "/localGame.fxml";
	}

	@Override
	protected GameHandler buildGameHandler() {
		return buildGameHandler(0);
	}

	private GameHandler buildGameHandler(int handicap) {
		GameHandler result = new LocalGameHandler(handicap);
		return new GameHandlerEventDecorator(result);
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		super.initialize(url, resourceBundle);
		setupEventHandlers();
	}

	private void setupEventHandlers() {
		EventBus.addEventHandler(ScoreEvent.DONE, this::doneScoring);
	}

	@Override
	void setupPanes() {
		super.setupPanes();
		loadGameMenu();
		loadScorePane();
		loadReviewPanel();
	}

	@Override
	BoardDrawer buildBoardDrawer() {
		return new BoardDrawer(boardCanvas, game);
	}

	@Override
	void gameEventHandler(GameEvent event) {
		if ( event.getHandler() == game )
			drawBoard();
	}

	@Override
	void canvasClicked(MouseEvent mouseEvent) {

		DrawCoords mousePosition = new DrawCoords(mouseEvent.getX(), mouseEvent.getY());
		CoordProjector projector = new CoordProjector(getBoardLength(boardCanvas),
													  DimensionHelper.getTopLeftCorner(boardCanvas));
		Coords boardPos = projector.nearestCoords(mousePosition);

		if ( gameState == LocalGameController.GameState.SCORING ) {
			scorePaneController.enableButtons();
			if ( mouseEvent.getButton() == MouseButton.PRIMARY )
				boardScorer.markGroupDead(boardPos);
			else if ( mouseEvent.getButton() == MouseButton.SECONDARY )
				boardScorer.unmarkGroupDead(boardPos);
		}
		else if ( gameState == LocalGameController.GameState.PLAYING ) {
			if ( game.isLegalMove(play(boardPos, getTurnPlayer())) ) {
				game.playMove(play(boardPos, getTurnPlayer()));
			}
		}
	}

	@Override
	void canvasHover(MouseEvent mouseEvent) {
		if ( gameState != GameState.PLAYING )
			return;

		DrawCoords mousePosition = new DrawCoords(mouseEvent.getX(), mouseEvent.getY());
		drawBoard();
		boardDrawer.drawGhostStone(mousePosition, getTurnPlayer());
	}


	@Override
	void enterScoring(GameEvent event) {
		if ( event.getHandler() != game )
			return;

		gameState = GameState.SCORING;
		boardScorer = game.getScorer();
		gameMenuController.enterScoring();
		scorePaneController.setScorer(boardScorer);
		scorePaneController.setVisible(true);

		boardDrawer = new BoardScoreDrawer(boardCanvas, game, boardScorer);
		boardDrawer.draw();
		ScoreEvent.fireScoreEvent(boardScorer);
	}

	@Override
	void reviewStart(GameEvent event) {
		gameState = GameState.REVIEW;
		boardDrawer = buildBoardDrawer();
	}

	private StoneColour getTurnPlayer() {
		return game.getTurnPlayer();
	}

	private void drawBoard() {
		if ( gameState == GameState.SCORING )
			return;
		boardDrawer.draw();
	}

	private void loadScorePane() {
		scorePaneController = new ScorePaneController();

		sideBar.getChildren().add(scorePaneController.build());
	}

	private void loadGameMenu() {
		gameMenuController = new GameMenuController();
		gameMenuController.setGameHandler(game);

		sideBar.getChildren().add(gameMenuController.build());
	}

	private void loadReviewPanel() {
		reviewPanelController = new ReviewPanelController(game);

		sideBar.getChildren().add(reviewPanelController.build());
	}

	private void doneScoring(ScoreEvent event) {
		if ( event.getSource() != boardScorer )
			return;

		gameState = GameState.GAMEOVER;

		gameMenuController.enableEndGameButtons();
	}

	void setHandicap(int handicap) {
		if ( game.getStones(StoneColour.BLACK).size() > 0 || game.getStones(StoneColour.WHITE).size() > 0 )
			game = buildGameHandler(handicap);

		if ( handicap > 0 )
			for (Coords stone : HandicapHelper.getHandicapStones(handicap))
				game.playMove(play(stone, StoneColour.BLACK));
	}

	void setKomi(double komi) {
		game.setKomi(komi);
	}

	private enum GameState {
		PLAYING, SCORING, GAMEOVER, REVIEW
	}
}