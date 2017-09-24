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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.LogHelper;

import java.util.Collection;
import java.util.Vector;
import java.util.function.Consumer;

public class GameList {

	private Vector<Game> games;

	private int totalSize;
	private int size;
	private int start;

	private GameList(JSONObject jsonGameList) {
		try {
			fromJSON(jsonGameList);
		}
		catch (JSONException e) {
			LogHelper.jsonError(e);
		}
	}

	private void fromJSON(JSONObject jsonGameList) throws JSONException {
		totalSize = jsonGameList.getInt("size");
		start = jsonGameList.getInt("from");
		size = jsonGameList.getInt("limit");

		games = new Vector<>();

		JSONArray gameArray = jsonGameList.getJSONArray("results");

		for (int i = 0; i < gameArray.length(); i++) {
			games.add(new Game(gameArray.getJSONObject(i)));
		}
	}

	public static void requestGameList(RequestOptions options, Consumer<GameList> callback) {
		JSONObject args = options.getRequest();

		Connection.getGameList(args, jsonObject -> callback.accept(new GameList(jsonObject)));
	}

	public Collection<Game> getGames() {
		return games;
	}

	public int getTotalGames() {
		return totalSize;
	}

	public int getStartIndex() {
		return start;
	}

	public int getEndIndex() {
		return start + size - 1;
	}

	public enum GameListType {
		LIVE, CORRESPONDENCE;

		@Override
		public String toString() {
			if ( this == LIVE )
				return "live";

			if ( this == CORRESPONDENCE )
				return "corr";

			return "";
		}
	}

	public enum SortingOptions {
		RANK;

		@Override
		public String toString() {
			if ( this == RANK )
				return "rank";

			return "";
		}
	}

	public static class RequestOptions {

		private int page;
		private int size;
		private GameListType type;
		private SortingOptions sort;


		public RequestOptions() {
			page = 0;
			size = 1;
			type = GameListType.LIVE;
			sort = SortingOptions.RANK;
		}

		public void setPage(int page) {
			this.page = page;
		}

		public void setNumGames(int size) {
			this.size = size;
		}

		public void setType(GameListType type) {
			this.type = type;
		}

		public void sortBy(SortingOptions sort) {
			this.sort = sort;
		}

		JSONObject getRequest() {
			JSONObject request = new JSONObject();

			try {
				request.put("list", type.toString());
				request.put("sort_by", sort.toString());
				request.put("from", page);
				request.put("limit", size);
			}
			catch (JSONException e) {
				LogHelper.jsonError(e);
			}

			return request;
		}
	}

	public class Player {

		private int id;
		private String name;
		private int rank;
		private boolean isProfessional;

		Player(JSONObject player) {
			try {
				build(player);
			}
			catch (JSONException e) {
				LogHelper.jsonError(e);
			}
		}

		private void build(JSONObject player) throws JSONException {
			id = player.getInt("id");
			name = player.getString("username");
			rank = player.getInt("rank");
			isProfessional = player.getBoolean("professional");
		}

		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public int getRank() {
			return rank;
		}

		public boolean isProfessional() {
			return isProfessional;
		}
	}

	public class Game {

		private int id;
		private String name;

		private Player black;
		private Player white;

		private int size;
		private boolean isPrivate;

		private int turnPlayer;
		private String phase;
		private int moveNumber;

		Game(JSONObject game) {
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

			black = new Player(game.getJSONObject("black"));
			white = new Player(game.getJSONObject("white"));

			size = game.getInt("height");
			isPrivate = game.getBoolean("private");

			turnPlayer = game.getInt("player_to_move");
			phase = game.getString("phase");
			moveNumber = game.getInt("move_number");
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

		public Player getBlackPlayer() {
			return black;
		}

		public String getBlackName() {
			return black.getName();
		}

		public String getWhiteName() {
			return white.getName();
		}

		public Player getWhitePlayer() {
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

		public int getMoveNumber() {
			return moveNumber;
		}
	}
}
