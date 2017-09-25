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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import ogs.GameList;
import ogs.SpectatorGameHandler;
import org.dockfx.DockNode;
import org.dockfx.DockPos;
import ui.Main;


import java.net.URL;
import java.util.ResourceBundle;

public class GameListController extends SelfBuildingController implements Initializable {

	private boolean live = true;
	@FXML
	private TableView<GameList.Game> table;
	@FXML
	private Button corrButton;
	@FXML
	private Button liveButton;

	private void onSwitch(ActionEvent event) {
		live = !live;

		liveButton.setDisable(live);
		corrButton.setDisable(!live);

		requestGameList();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		buildTable();
		requestGameList();
		liveButton.setOnAction(this::onSwitch);
		corrButton.setOnAction(this::onSwitch);
	}

	private void requestGameList() {
		GameList.RequestOptions options = new GameList.RequestOptions();

		options.setNumGames(1000);
		options.setPage(0);
		options.sortBy(GameList.SortingOptions.RANK);

		if ( live )
			options.setType(GameList.GameListType.LIVE);
		else
			options.setType(GameList.GameListType.CORRESPONDENCE);

		GameList.requestGameList(options, this::populate);
	}

	private void buildTable() {
		TableColumn<GameList.Game, String> titleColumn = new TableColumn<>("Game");
		titleColumn.setPrefWidth(125);
		titleColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getGameName()));

		TableColumn<GameList.Game, String> blackColumn = new TableColumn<>("Black");
		blackColumn.setPrefWidth(125);
		blackColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getBlackName()));

		TableColumn<GameList.Game, String> whiteColumn = new TableColumn<>("White");
		whiteColumn.setPrefWidth(125);
		whiteColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getWhiteName()));

		TableColumn<GameList.Game, Integer> moveColumn = new TableColumn<>("Move");
		moveColumn.setPrefWidth(55);
		moveColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getMoveNumber()));

		table.getColumns().add(blackColumn);
		table.getColumns().add(whiteColumn);
		table.getColumns().add(titleColumn);
		table.getColumns().add(moveColumn);

		table.setRowFactory(new Callback<TableView<GameList.Game>, TableRow<GameList.Game>>() {
			@Override
			public TableRow<GameList.Game> call(TableView<GameList.Game> param) {
				final TableRow<GameList.Game> row = new TableRow<>();
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

	private void spectateGame(GameList.Game game) {
		SpectatorGameHandler handler = new SpectatorGameHandler(game.getId());
		BoardController controller = new BoardController(handler, "/localGame.fxml");

		PlayerInfo playerInfo = new PlayerInfo(game);
		controller.addToSideBar(playerInfo.getRoot());

		DockNode node = controller.build();
		node.closedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if ( newValue ) {
					handler.disconnect();
					observable.removeListener(this);
				}
			}
		});
		node.setTitle(game.getGameName());
		node.dock(Main.instance.dockPane, DockPos.CENTER);
	}

	private void populate(GameList gameList) {
		ObservableList<GameList.Game> items = FXCollections.observableArrayList(gameList.getGames());

		table.setItems(items);
	}

	@Override
	protected String getResourcePath() {
		return "/gameList.fxml";
	}
}
