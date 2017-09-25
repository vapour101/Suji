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

package ui.controller.sidebar;

import event.EventBus;
import event.GameEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import logic.gamehandler.GameHandler;
import logic.gametree.GameTree;
import ui.controller.SelfBuildingController;

import java.net.URL;
import java.util.ResourceBundle;

public class ReviewPaneController extends SelfBuildingController implements Initializable {

	public Label moveNumber;
	@FXML
	private Button backButton;
	@FXML
	private Button forwardButton;

	private GameHandler game;

	public ReviewPaneController(GameHandler gameHandler) {
		game = gameHandler;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		backButton.setOnAction(this::onClickBack);
		forwardButton.setOnAction(this::onClickForward);

		backButton.setVisible(false);
		forwardButton.setVisible(false);

		EventBus.addEventHandler(GameEvent.REVIEWSTART, this::startReview);
	}

	private void startReview(GameEvent event) {
		if ( event.getHandler() != game )
			return;

		backButton.setVisible(true);
		forwardButton.setVisible(true);
		update();
	}

	private void update() {
		checkTreeBounds();
		updateMoveNumber();
		GameEvent.fireGameEvent(game, GameEvent.REVIEW);
	}

	private void updateMoveNumber() {
		String label = "Move: " + game.getGameTree().getNumMoves();

		moveNumber.setText(label);
	}

	private void checkTreeBounds() {
		GameTree tree = game.getGameTree();

		boolean canGoBack = true;
		boolean canGoForward = true;

		if ( tree.isRoot() )
			canGoBack = false;

		if ( tree.getNumChildren() < 1 )
			canGoForward = false;

		backButton.setDisable(!canGoBack);
		forwardButton.setDisable(!canGoForward);
	}

	private void onClickBack(ActionEvent event) {
		game.getGameTree().stepBack();
		update();
	}

	private void onClickForward(ActionEvent event) {
		game.getGameTree().stepForward(0);
		update();
	}

	@Override
	protected String getResourcePath() {
		return "/reviewPanel.fxml";
	}
}
