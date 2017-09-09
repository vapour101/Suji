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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import logic.BoardScorer;
import util.StoneColour;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ScorePaneController implements Initializable {

	@FXML
	private Pane scorePane;
	@FXML
	private Button blackDone;
	@FXML
	private Button whiteDone;
	@FXML
	private Label blackScore;
	@FXML
	private Label whiteScore;

	public static FXMLLoader getScorePaneLoader() {
		FXMLLoader loader = new FXMLLoader(ScorePaneController.class.getResource("/scorePane.fxml"));

		try {
			loader.load();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return loader;
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		scorePane.widthProperty().addListener(this::resizeScore);
		setVisible(false);
	}

	public void setVisible(boolean visible) {
		scorePane.setVisible(visible);
	}

	public void enableButtons() {
		blackDone.setDisable(false);
		whiteDone.setDisable(false);
	}

	public void setDoneScoring(Runnable callback) {
		blackDone.setOnAction(event -> {
			blackDone.setDisable(true);
			if ( whiteDone.isDisabled() )
				callback.run();
		});

		whiteDone.setOnAction(event -> {
			whiteDone.setDisable(true);
			if ( blackDone.isDisabled() )
				callback.run();
		});
	}

	public void updateScore(BoardScorer boardScorer) {
		blackScore.setText(Double.toString(boardScorer.getScore(StoneColour.BLACK)));
		whiteScore.setText(Double.toString(boardScorer.getScore(StoneColour.WHITE)));
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
}
