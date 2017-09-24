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

import logic.gametree.SimpleGameTree;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.*;

public class SpectatorGameTree extends SimpleGameTree {

	public SpectatorGameTree() {

	}

	public SpectatorGameTree(JSONArray jsonTree, StoneColour turnPlayer, int handicap) {
		super();

		LogHelper.finest("SpectatorGameTree called super.");
		LogHelper.finest("jsonTree null? " + (jsonTree == null ? "Yes" : "No"));
		LogHelper.finest("turnPlayer null? " + (turnPlayer == null ? "Yes" : "No"));

		for (Coords c : HandicapHelper.getHandicapStones(handicap))
			stepForward(Move.play(c, StoneColour.BLACK));

		LogHelper.finest("SpectatorGameTree set handicap stones.");

		try {
			fromJSON(jsonTree, turnPlayer.other().other());
		}
		catch (JSONException e) {
			LogHelper.jsonError(e);
		}
	}

	private void fromJSON(JSONArray jsonTree, StoneColour turnPlayer) throws JSONException {
		for (int i = 0; i < jsonTree.length(); i++) {
			JSONArray moveArray = jsonTree.getJSONArray(i);
			Coords move = Coords.getCoords(moveArray.getInt(0) + 1, moveArray.getInt(1) + 1);

			stepForward(Move.play(move, turnPlayer));

			turnPlayer = turnPlayer.other();
		}
	}

	public void stepForward(JSONObject jsonMove, StoneColour turnPlayer) {
		try {
			stepForwardExceptions(jsonMove, turnPlayer);
		}
		catch (JSONException e) {
			LogHelper.jsonError(e);
		}
	}

	private void stepForwardExceptions(JSONObject jsonMove, StoneColour turnPlayer) throws JSONException {
		JSONArray moveArray = jsonMove.getJSONArray("move");
		Coords move = Coords.getCoords(moveArray.getInt(0) + 1, moveArray.getInt(1) + 1);

		stepForward(Move.play(move, turnPlayer));
	}
}
