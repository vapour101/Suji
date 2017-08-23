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

package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.ListSpinnerValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class newLocalGameController implements Initializable {

	public Spinner<Integer> handicapSpinner;
	public Button startButton;
	public Spinner<Double> komiSpinner;

	private Stage window;

	protected void setWindow(Stage window) {
		this.window = window;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupHandicapSpinner();
		setupKomiSpinner();

		startButton.setOnAction(this::start);
	}

	private void setupHandicapSpinner() {
		ObservableList<Integer> handicaps = FXCollections.observableArrayList(new ArrayList<>());
		handicaps.add(0);
		for (int i = 2; i < 10; i++)
			handicaps.add(i);

		SpinnerValueFactory<Integer> handicapFactory = new ListSpinnerValueFactory<>(handicaps);
		handicapSpinner.setValueFactory(handicapFactory);
	}

	private void setupKomiSpinner() {

	}

	private void start(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/localGame.fxml"));

		Parent scene = null;
		try {
			scene = loader.load();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		if ( scene == null )
			return;


		BoardController controller = loader.getController();

		int handicap = handicapSpinner.getValue();

		controller.setHandicap(handicap);

		window.setScene(new Scene(scene));
		window.show();
	}
}
