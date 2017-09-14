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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import logic.gamehandler.GameHandler;
import sgf.SGFWriter;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class GameMenuController extends SelfBuildingController implements Initializable {

	@FXML
	private Button saveButton;
	@FXML
	private Button undoButton;
	@FXML
	private Button passButton;

	private GameHandler game;

	GameMenuController() {
		game = null;
	}

	@Override
	protected String getResourcePath() {
		return "/gameMenu.fxml";
	}


	void setGameHandler(GameHandler gameHandler) {
		game = gameHandler;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupButtons();
	}

	private void setupButtons() {
		passButton.setOnAction(this::pass);
		undoButton.setOnAction(this::undo);
		saveButton.setOnAction(this::save);
		saveButton.setVisible(false);
	}

	void enableSaveButton() {
		saveButton.setVisible(true);
	}

	void enterScoring() {
		passButton.setVisible(false);
		undoButton.setVisible(false);
	}

	private void pass(ActionEvent event) {
		game.pass();
	}


	private void save(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Game As...");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SGF", "*.sgf"));
		File file = fileChooser.showSaveDialog(null);

		if ( file != null ) {
			try {
				Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));

				SGFWriter sgf = game.getSGFWriter();

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
}
