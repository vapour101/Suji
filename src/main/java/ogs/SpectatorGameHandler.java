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

import event.GameEvent;
import logic.board.Board;
import logic.gamehandler.GameHandler;
import logic.gametree.GameTree;
import logic.score.Scorer;
import ogs.web.Connection;
import ogs.web.GameList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sgf.SGFWriter;
import util.Coords;
import util.LogHelper;
import util.Move;
import util.StoneColour;

import java.util.Collection;

public class SpectatorGameHandler implements GameHandler {

	private StoneColour initialPlayer;
	private GameList.GameMeta gameMeta;
	private SpectatorGameTree gameTree;

	public SpectatorGameHandler(GameList.GameMeta meta) {
		initialPlayer = StoneColour.BLACK;
		gameTree = new SpectatorGameTree();
		gameMeta = meta;
	}

	public void start() {
		LogHelper.info("Attempting connection");
		Connection.connectToGame(gameMeta, this::onGameData, this::onMove);
	}

	private void onGameData(JSONObject jsonGame) {
		LogHelper.info("Gamedata received");

		if ( jsonGame == null )
			LogHelper.severe("Gamedata is null");

		LogHelper.info(jsonGame.toString());

		try {
			initialPlayer = jsonGame.getString("initial_player").equalsIgnoreCase("white") ? StoneColour.WHITE : StoneColour.BLACK;
			int handicap = jsonGame.getInt("handicap");

			LogHelper.finest("Getting moves");

			JSONArray moveList = jsonGame.getJSONArray("moves");

			LogHelper.finest("Initialising gameTree");

			gameTree = new SpectatorGameTree(moveList, initialPlayer, handicap);
		}
		catch (JSONException e) {
			LogHelper.jsonError(e);
		}

		LogHelper.finest("Firing game event");
		GameEvent.fireGameEvent(this);
	}

	private void onMove(JSONObject jsonMove) {
		if ( gameTree == null ) {
			LogHelper.severe("gameTree is null");
			return;
		}

		gameTree.stepForward(jsonMove, getTurnPlayer());

		GameEvent.fireGameEvent(this);
	}

	@Override
	public void pass() {

	}

	@Override
	public void undo() {

	}

	@Override
	public StoneColour getTurnPlayer() {
		if ( gameTree.getNumMoves() == 0 )
			return initialPlayer;

		return gameTree.getLastMove().getPlayer().other();
	}

	@Override
	public void setKomi(double komi) {

	}

	@Override
	public GameTree getGameTree() {
		return gameTree;
	}

	@Override
	public SGFWriter getSGFWriter() {
		return null;
	}

	@Override
	public Scorer getScorer() {
		return null;
	}

	@Override
	public Board getBoard() {
		return gameTree.getPosition();
	}

	@Override
	public boolean isLegalMove(Move move) {
		return false;
	}

	@Override
	public void playMove(Move move) {

	}

	@Override
	public Collection<Coords> getStones(StoneColour colour) {
		return getBoard().getStones(colour);
	}
}
