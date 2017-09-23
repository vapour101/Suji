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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sgf.SGFWriter;
import util.*;

import java.util.Collection;

public class SpectatorGameHandler implements GameHandler {

	private Board board;
	private StoneColour turnPlayer;
	private GameMeta gameMeta;

	public SpectatorGameHandler(GameMeta meta) {
		board = new Board();
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

		board = new Board();
		try {
			turnPlayer = jsonGame.getString("initial_player").equalsIgnoreCase("white") ? StoneColour.WHITE : StoneColour.BLACK;
			int handicap = jsonGame.getInt("handicap");

			if ( handicap != 0 )
				for (Coords c : HandicapHelper.getHandicapStones(handicap))
					board.playStone(Move.play(c, StoneColour.BLACK));

			LogHelper.finest("Getting moves");

			JSONArray moveList = jsonGame.getJSONArray("moves");

			for (int i = 0; i < moveList.length(); i++) {
				JSONArray moveArray = moveList.getJSONArray(i);
				Coords move = Coords.getCoords(moveArray.getInt(0) + 1, moveArray.getInt(1) + 1);

				board.playStone(Move.play(move, turnPlayer));

				turnPlayer = turnPlayer.other();
			}
		}
		catch (JSONException e) {
			LogHelper.jsonError(e);
		}

		LogHelper.finest("Firing game event");
		GameEvent.fireGameEvent(this);
	}

	private void onMove(JSONObject jsonMove) {
		try {
			JSONArray moveArray = jsonMove.getJSONArray("move");
			Coords move = Coords.getCoords(moveArray.getInt(0) + 1, moveArray.getInt(1) + 1);

			board.playStone(Move.play(move, turnPlayer));

			turnPlayer = turnPlayer.other();
		}

		catch (JSONException e) {
			LogHelper.jsonError(e);
		}

		GameEvent.fireGameEvent(this);
	}

	@Override
	public Board getBoard() {
		return board;
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
		return board.getStones(colour);
	}

	@Override
	public void pass() {

	}

	@Override
	public void undo() {

	}

	@Override
	public StoneColour getTurnPlayer() {
		return turnPlayer;
	}

	@Override
	public void setKomi(double komi) {

	}

	@Override
	public GameTree getGameTree() {
		return null;
	}

	@Override
	public Scorer getScorer() {
		return null;
	}

	@Override
	public SGFWriter getSGFWriter() {
		return null;
	}
}
