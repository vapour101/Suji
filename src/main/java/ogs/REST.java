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

import javafx.scene.image.Image;
import org.json.JSONException;
import org.json.JSONObject;
import util.LogHelper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.function.Consumer;

public class REST {

	private static Client client = null;

	public static void requestPlayerIcon(int playerID, int size, Consumer<Image> callback) {
		requestPlayerIconURL(playerID, string -> {
			string = string.replaceAll("-\\d+\\.png$", "-" + size + ".png");
			string = string.replaceAll("s=\\d+", "s=" + size);
			getImageFromURL(string, callback);
		});
	}

	public static void getImageFromURL(String url, Consumer<Image> callback) {
		Thread thread = new Thread(() -> {
			WebTarget target = getClient().target(url);
			Response response = target.request(MediaType.WILDCARD_TYPE).get();

			if ( response.getStatus() != 200 ) {
				LogHelper.severe("Failed : HTTP error code : " + response.getStatus());
			}

			InputStream output = response.readEntity(InputStream.class);
			Image image = new Image(output);

			response.close();

			callback.accept(image);
		});
		thread.start();
	}

	public static void requestPlayerIconURL(int playerID, Consumer<String> callback) {
		Thread thread = new Thread(() -> {
			WebTarget target = getClient().target(OGSReference.getPlayerInfoURL(playerID));
			Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();

			if ( response.getStatus() != 200 ) {
				LogHelper.severe("Failed : HTTP error code : " + response.getStatus());
			}

			String output = response.readEntity(String.class);
			response.close();

			try {
				JSONObject jsonObject = new JSONObject(output);
				callback.accept(jsonObject.getString("icon"));
			}
			catch (JSONException e) {
				LogHelper.jsonError(e);
			}
		});
		thread.start();
	}

	static Client getClient() {
		if ( client == null )
			synchronized (REST.class) {
				if ( client == null )
					client = ClientBuilder.newClient();
			}

		return client;
	}
}
