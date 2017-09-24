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
import util.LogHelper;
import util.StoneColour;

import java.util.LinkedList;
import java.util.List;

public class Gamedata {

	private int gameId;
	private StoneColour initialPlayer;
	private List<Movedata> moves;
	private int handicap;

	public Gamedata(JSONObject jsonGame) {
		moves = new LinkedList<>();

		try {
			fromJSON(jsonGame);
		}
		catch (JSONException e) {
			LogHelper.jsonError(e);
		}
	}

	private void fromJSON(JSONObject jsonGame) throws JSONException {
		LogHelper.finest("Processing JSONObject to Gamedata");

		if ( jsonGame == null ) {
			LogHelper.severe("JSONObject is null");
			return;
		}

		gameId = jsonGame.getInt("game_id");
		initialPlayer = jsonGame.getString("initial_player").equalsIgnoreCase("white") ? StoneColour.WHITE : StoneColour.BLACK;
		handicap = jsonGame.getInt("handicap");

		JSONArray moveList = jsonGame.getJSONArray("moves");

		for (int i = 0; i < moveList.length(); i++)
			moves.add(new Movedata(gameId, i, moveList.getJSONArray(i)));
	}

	public int getGameId() {
		return gameId;
	}

	public List<Movedata> getMoves() {
		return moves;
	}

	public StoneColour getInitialPlayer() {
		return initialPlayer;
	}

	public int getHandicap() {
		return handicap;
	}
}
