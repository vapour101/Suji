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

	private Vector<GameMeta> games;

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
			games.add(new GameMeta(gameArray.getJSONObject(i)));
		}
	}

	public static void requestGameList(RequestOptions options, Consumer<GameList> callback) {
		JSONObject args = options.getRequest();

		Connection.getGameList(args, jsonObject -> callback.accept(new GameList(jsonObject)));
	}

	public Collection<GameMeta> getGames() {
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
				LogHelper.jsonError(e);
			}

			return request;
		}
	}
}
