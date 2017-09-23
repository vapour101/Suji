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
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import logic.gamehandler.GameHandler;
import logic.gamehandler.LocalGameHandler;
import logic.score.Scorer;
import ui.drawer.*;
import util.Coords;
import util.HandicapHelper;
import util.StoneColour;

import java.net.URL;
import java.util.ResourceBundle;

import static util.Move.play;

public class LocalGameController extends BoardController {

	private Scorer boardScorer;

	private ScorePaneController scorePaneController;
	private GameMenuController gameMenuController;
	private ReviewPanelController reviewPanelController;

	private BoardStrategy strategy;

	public LocalGameController() {
		game = buildGameHandler();
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
		strategy = new GamePlay(boardCanvas, game, gameDrawer);
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
	GameDrawer buildGameDrawer() {
		GameDrawer drawer = new GameDrawer(boardCanvas, game);

		Image blackStone = new Image("/black.png", false);
		Image whiteStone = new Image("/white.png", false);

		StoneDrawer stoneDrawer = new TexturedStoneDrawer(boardCanvas, blackStone, whiteStone);
		drawer.setStoneDrawer(stoneDrawer);

		Image wood = new Image("/wood.jpg", false);
		Image lines = new Image("/grid.png", false);

		BoardDrawer boardDrawer = new TexturedBoardDrawer(boardCanvas, wood, lines);
		drawer.setBoardDrawer(boardDrawer);

		return drawer;
	}

	@Override
	void canvasClicked(MouseEvent mouseEvent) {
		if ( strategy != null )
			strategy.canvasClicked(mouseEvent);
	}

	@Override
	void canvasHover(MouseEvent mouseEvent) {
		if ( strategy != null )
			strategy.canvasHover(mouseEvent);
	}

	@Override
	void canvasExit(MouseEvent mouseEvent) {
		if ( strategy != null )
			strategy.canvasExit(mouseEvent);
	}

	@Override
	void enterScoring(GameEvent event) {
		if ( event.getHandler() != game )
			return;

		boardScorer = game.getScorer();
		gameMenuController.enterScoring();
		scorePaneController.setScorer(boardScorer);
		scorePaneController.setVisible(true);

		gameDrawer = buildBoardScoreDrawer();
		strategy = new Scoring(boardCanvas, boardScorer, scorePaneController);
		gameDrawer.draw();
		ScoreEvent.fireScoreEvent(boardScorer);
	}

	private GameScoreDrawer buildBoardScoreDrawer() {
		return new GameScoreDrawer(buildGameDrawer(), boardScorer);
	}

	@Override
	void reviewStart(GameEvent event) {
		gameDrawer = buildGameDrawer();
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

	@Override
	protected String getResourcePath() {
		return "/localGame.fxml";
	}

	private void doneScoring(ScoreEvent event) {
		if ( event.getSource() != boardScorer )
			return;

		strategy = null;

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
}