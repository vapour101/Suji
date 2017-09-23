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

import logic.gamehandler.GameHandler;
import ogs.GameMeta;
import ogs.SpectatorGameHandler;
import ui.drawer.GameDrawer;

import java.net.URL;
import java.util.ResourceBundle;

public class SpectatorController extends BoardController {

	private GameMeta gameMeta = null;

	public SpectatorController(GameMeta metaData) {
		gameMeta = metaData;
		game = buildGameHandler();
	}

	@Override
	GameHandler buildGameHandler() {
		if ( gameMeta == null )
			return null;
		SpectatorGameHandler handler = new SpectatorGameHandler(gameMeta);
		handler.start();

		return handler;
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		super.initialize(url, resourceBundle);
	}

	@Override
	GameDrawer buildGameDrawer() {
		if ( game == null )
			return null;

		return new GameDrawer(boardCanvas, game);
	}

	@Override
	protected String getResourcePath() {
		return "/localGame.fxml";
	}
}
