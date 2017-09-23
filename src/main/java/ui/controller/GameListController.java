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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import netcode.OGSConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.LogHelper;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class GameListController extends SelfBuildingController implements Initializable {

	public ListView<String> list;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		OGSConnection.requestGameList(this::populateTable);
	}

	private void populateTable(JSONObject jsonObject) {
		ObservableList<String> items = FXCollections.observableArrayList();

		try {
			JSONArray gameList = jsonObject.getJSONArray("results");

			for (int i = 0; i < gameList.length(); ++i) {
				JSONObject game = gameList.getJSONObject(i);
				String black = game.getJSONObject("black").getString("username");
				String white = game.getJSONObject("white").getString("username");

				items.add(black + " vs " + white);
			}
		}
		catch (JSONException e) {
			LogHelper.log(Level.SEVERE, "Malformed JSONObject in GameListController::populateTable", e);
			return;
		}

		list.setItems(items);

		LogHelper.info("Finished populating table");
	}

	@Override
	protected String getResourcePath() {
		return "/gameList.fxml";
	}
}
