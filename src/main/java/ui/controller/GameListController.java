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

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.util.Callback;
import ogs.web.GameList;
import org.dockfx.DockNode;
import org.dockfx.DockPos;
import ui.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class GameListController extends SelfBuildingController implements Initializable {

	public TableView<GameList.GameMeta> table;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		buildTable();

		GameList.RequestOptions options = new GameList.RequestOptions();

		options.setNumGames(1000);
		options.setPage(0);
		options.setType(GameList.GameListType.LIVE);
		options.sortBy(GameList.SortingOptions.RANK);

		GameList.requestGameList(options, this::populateTable);
	}

	private void buildTable() {
		TableColumn<GameList.GameMeta, String> titleColumn = new TableColumn<>("Game");
		titleColumn.setPrefWidth(125);
		titleColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getGameName()));

		TableColumn<GameList.GameMeta, String> blackColumn = new TableColumn<>("Black");
		blackColumn.setPrefWidth(125);
		blackColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getBlackName()));

		TableColumn<GameList.GameMeta, String> whiteColumn = new TableColumn<>("White");
		whiteColumn.setPrefWidth(125);
		whiteColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getWhiteName()));

		TableColumn<GameList.GameMeta, Integer> moveColumn = new TableColumn<>("Move");
		moveColumn.setPrefWidth(55);
		moveColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getMoveNumber()));

		table.getColumns().add(blackColumn);
		table.getColumns().add(whiteColumn);
		table.getColumns().add(titleColumn);
		table.getColumns().add(moveColumn);

		table.setRowFactory(new Callback<TableView<GameList.GameMeta>, TableRow<GameList.GameMeta>>() {
			@Override
			public TableRow<GameList.GameMeta> call(TableView<GameList.GameMeta> param) {
				final TableRow<GameList.GameMeta> row = new TableRow<>();
				final ContextMenu rowMenu = new ContextMenu();

				MenuItem spectateItem = new MenuItem("Watch Game");
				spectateItem.setOnAction(event -> spectateGame(row.getItem()));

				rowMenu.getItems().add(spectateItem);

				row.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(row.itemProperty())).then(rowMenu).otherwise(
						(ContextMenu) null));
				return row;
			}
		});
	}

	private void spectateGame(GameList.GameMeta game) {
		SpectatorController controller = new SpectatorController(game);
		Parent root = controller.build();
		DockNode node = new DockNode(root, game.getGameName());
		node.dock(Main.instance.dockPane, DockPos.CENTER);
	}

	private void populateTable(GameList gameList) {
		ObservableList<GameList.GameMeta> items = FXCollections.observableArrayList(gameList.getGames());

		table.setItems(items);
	}

	@Override
	protected String getResourcePath() {
		return "/gameList.fxml";
	}
}
