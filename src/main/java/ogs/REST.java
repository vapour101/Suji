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
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.function.Consumer;
import java.util.logging.Level;

public class REST {

	public static void requestPlayerIcon(int playerID, Consumer<Image> callback) {
		Runnable task = new IconRequest(playerID, callback);
		Thread thread = new Thread(task);
		thread.start();
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
			try {
				runWithExceptions();
			}
			catch (MalformedURLException e) {
				LogHelper.log(Level.WARNING, "Player icon URL is malformed", e);
			}
			catch (IOException e) {
				LogHelper.log(Level.WARNING, "IO exception", e);
			}
			catch (RuntimeException e) {
				LogHelper.log(Level.WARNING, "Caught runtime exception", e);
			}
		}

		private void runWithExceptions() throws IOException, RuntimeException {
			Client client = ClientBuilder.newClient();
			Response response = client.target(OGSReference.getAvatarURL(id)).request(MediaType.WILDCARD).get();

			if ( response.getStatus() != 200 ) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			InputStream output = response.readEntity(InputStream.class);

			Image image = new Image(output);

			response.close();
			client.close();

			result.accept(image);
		}
	}
}
