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
import util.LogHelper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.function.Consumer;

public class REST {

	private static Client client = null;

	public static void requestPlayerIcon(int playerID, Consumer<Image> callback) {
		Runnable task = new IconRequest(playerID, callback);
		Thread thread = new Thread(task);
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

	private static class IconRequest implements Runnable {

		private int id;
		private Consumer<Image> result;

		IconRequest(int playerID, Consumer<Image> callback) {
			id = playerID;
			result = callback;
		}

		@Override
		public void run() {
			Client client = REST.getClient();
			Response response = client.target(OGSReference.getAvatarURL(id)).request(MediaType.WILDCARD).get();

			if ( response.getStatus() != 200 ) {
				LogHelper.severe("Failed : HTTP error code : " + response.getStatus());
			}

			InputStream output = response.readEntity(InputStream.class);
			Image image = new Image(output);

			response.close();

			result.accept(image);
		}
	}
}
