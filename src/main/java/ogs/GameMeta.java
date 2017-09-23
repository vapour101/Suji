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

package ogs;

import org.json.JSONException;
import org.json.JSONObject;
import util.LogHelper;

public class GameMeta {

	private int id;
	private String name;

	private PlayerMeta black;
	private PlayerMeta white;

	private int size;
	private boolean isPrivate;

	private int turnPlayer;
	private String phase;

	GameMeta(JSONObject game) {
		try {
			build(game);
		}
		catch (JSONException e) {
			LogHelper.jsonError(e);
		}
	}

	private void build(JSONObject game) throws JSONException {
		id = game.getInt("id");
		name = game.getString("name");

		black = new PlayerMeta(game.getJSONObject("black"));
		white = new PlayerMeta(game.getJSONObject("white"));

		size = game.getInt("height");
		isPrivate = game.getBoolean("private");

		turnPlayer = game.getInt("player_to_move");
		phase = game.getString("phase");
	}

	public int getId() {
		return id;
	}

	public int getBoardSize() {
		return size;
	}

	public int getTurnPlayer() {
		return turnPlayer;
	}

	public PlayerMeta getBlackPlayer() {
		return black;
	}

	public String getBlackName() {
		return black.getName();
	}

	public String getWhiteName() {
		return white.getName();
	}

	public PlayerMeta getWhitePlayer() {
		return white;
	}

	public String getGameName() {
		return name;
	}

	public String getPhase() {
		return phase;
	}

	public boolean isPrivate() {
		return isPrivate;
	}
}
