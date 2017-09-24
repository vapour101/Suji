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

package ogs.web;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.Coords;
import util.LogHelper;
import util.Move;
import util.StoneColour;

public class Movedata {

	private int moveNumber;
	private int gameId;
	private Coords coords;
	private int time;

	public Movedata(JSONObject moveObject) {
		try {
			fromJSONObject(moveObject);
		}
		catch (JSONException e) {
			LogHelper.jsonError(e);
		}
	}

	private void fromJSONObject(JSONObject moveObject) throws JSONException {
		gameId = moveObject.getInt("game_id");
		moveNumber = moveObject.getInt("move_number");

		fromJSONArray(moveObject.getJSONArray("move"));
	}

	private void fromJSONArray(JSONArray moveArray) throws JSONException {
		int x = moveArray.getInt(0) + 1;
		int y = moveArray.getInt(1) + 1;

		coords = Coords.getCoords(x, y);
		time = moveArray.getInt(2);
	}

	public Movedata(int game, int number, JSONArray moveArray) {
		gameId = game;
		moveNumber = number;

		try {
			fromJSONArray(moveArray);
		}
		catch (JSONException e) {
			LogHelper.jsonError(e);
		}
	}

	public int getMoveNumber() {
		return moveNumber;
	}

	public int getGameId() {
		return gameId;
	}

	public int getTime() {
		return time;
	}

	public Move getMove(StoneColour player) {
		return Move.play(coords, player);
	}
}
