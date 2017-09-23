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

import java.util.Vector;
import java.util.function.Consumer;
import java.util.logging.Level;

public class GameList {

	private Vector<String> games;

	private GameList(JSONObject jsonGame) {
		games = new Vector<>();

		try {
			JSONArray gameArray = jsonGame.getJSONArray("results");

			for (int i = 0; i < gameArray.length(); i++) {
				JSONObject game = gameArray.getJSONObject(i);

				String black = game.getJSONObject("black").getString("username");
				String white = game.getJSONObject("white").getString("username");

				games.add(black + " vs " + white);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static void requestGameList(RequestOptions options, Consumer<GameList> callback) {
		JSONObject args = options.getRequest();

		Connection.getGameList(args, jsonObject -> callback.accept(new GameList(jsonObject)));
	}

	public Vector<String> getGames() {
		return games;
	}

	public enum GameListType {
		LIVE, CORRESPONDENCE;

		@Override
		public String toString() {
			if ( this == LIVE )
				return "live";

			if ( this == CORRESPONDENCE )
				return "correspondence";

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
				LogHelper.log(Level.WARNING, "Error creating JSONObject", e);
			}

			return request;
		}
	}
}
