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
import ogs.GameList;
import ogs.GameMeta;

import java.net.URL;
import java.util.ResourceBundle;

public class GameListController extends SelfBuildingController implements Initializable {

	public ListView<String> list;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		GameList.RequestOptions options = new GameList.RequestOptions();

		options.setNumGames(10);
		options.setPage(0);
		options.setType(GameList.GameListType.LIVE);
		options.sortBy(GameList.SortingOptions.RANK);

		GameList.requestGameList(options, this::populateTable);
	}

	private void populateTable(GameList gameList) {
		ObservableList<String> items = FXCollections.observableArrayList();

		for (GameMeta game : gameList.getGames()) {
			String gameInfo = game.getBlackName() + " vs " + game.getWhiteName();
			items.add(gameInfo);
		}

		items.add("");

		StringBuilder sb = new StringBuilder();
		sb.append("Displaying games ");
		sb.append(gameList.getStartIndex() + 1);
		sb.append(" to ");
		sb.append(gameList.getEndIndex() + 1);
		sb.append(" of ");
		sb.append(gameList.getTotalGames());

		items.add(sb.toString());

		list.setItems(items);
	}

	@Override
	protected String getResourcePath() {
		return "/gameList.fxml";
	}
}
