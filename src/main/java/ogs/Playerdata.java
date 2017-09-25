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
import util.WebHelper;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Playerdata {

	private static Map<Integer, Playerdata> playerCache = new HashMap<>();

	private int id;
	private String username;
	private boolean isPro;
	private String country;
	private String language;
	private String about;
	private boolean isSupporter;
	private boolean isBot;
	private String botAI;
	private int botOwner;
	private String website;
	private Instant registrationDate;
	private String name;
	private double rating;
	private int gamesPlayed;
	private boolean isFriend;
	private String avatar;

	public Playerdata(JSONObject jsonPlayer) {
		try {
			fromJSON(jsonPlayer);
		}
		catch (JSONException e) {
			LogHelper.jsonError(e);
		}
		catch (NullPointerException e) {
			LogHelper.severe("Something was null.");
		}
	}

	private void fromJSON(JSONObject jsonPlayer) throws JSONException {
		id = jsonPlayer.getInt("id");
		username = jsonPlayer.getString("username");
		isPro = jsonPlayer.getBoolean("professional");
		country = jsonPlayer.getString("country");
		language = jsonPlayer.getString("language");
		about = jsonPlayer.getString("about");
		isSupporter = jsonPlayer.getBoolean("supporter");
		isBot = jsonPlayer.getBoolean("is_bot");
		website = jsonPlayer.getString("website");
		registrationDate = Instant.parse(jsonPlayer.getString("registration_date"));
		name = jsonPlayer.getString("name");

		JSONObject overallRating = jsonPlayer.getJSONObject("ratings").getJSONObject("overall");
		rating = overallRating.getDouble("rating");
		gamesPlayed = overallRating.getInt("games_played");

		isFriend = jsonPlayer.getBoolean("is_friend");
		avatar = jsonPlayer.getString("icon");

		botAI = null;
		botOwner = 0;

		if ( isBot ) {
			botAI = jsonPlayer.getString("bot_ai");
			botOwner = jsonPlayer.getInt("bot_owner");
		}
	}

	public static void requestPlayerdata(int id, Consumer<Playerdata> callback) {
		if ( playerCache.containsKey(id) ) {
			callback.accept(playerCache.get(id));
			return;
		}

		WebHelper.requestJSON(OGSReference.getPlayerInfoURL(id), new Consumer<JSONObject>() {
			@Override
			public void accept(JSONObject jsonObject) {
				Playerdata player = new Playerdata(jsonObject);

				playerCache.put(id, player);
				callback.accept(player);
			}
		});
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public double getRating() {
		return rating;
	}

	public int getGamesPlayed() {
		return gamesPlayed;
	}

	public String getAbout() {
		return about;
	}

	public String getAvatarURL(int size) {
		String url = getAvatarURL();

		url = url.replaceAll("-\\d+\\.png$", "-" + size + ".png");
		url = url.replaceAll("s=\\d+", "s=" + size);

		return url;
	}

	public String getAvatarURL() {
		return avatar;
	}

	public String getBotAI() {
		return botAI;
	}

	public int getBotOwner() {
		return botOwner;
	}

	public String getCountry() {
		return country;
	}

	public String getLanguage() {
		return language;
	}

	public String getUsername() {
		return username;
	}

	public String getWebsite() {
		return website;
	}

	public boolean isBot() {
		return isBot;
	}

	public boolean isFriend() {
		return isFriend;
	}

	public boolean isPro() {
		return isPro;
	}

	public boolean isSupporter() {
		return isSupporter;
	}
}
