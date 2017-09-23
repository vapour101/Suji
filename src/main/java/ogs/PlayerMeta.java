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

public class PlayerMeta {

	private int id;
	private String name;
	private int rank;
	private boolean isProfessional;

	PlayerMeta(JSONObject player) {
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
