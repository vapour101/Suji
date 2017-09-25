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

import util.LogHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Consumer;
import java.util.logging.Level;

public class REST {

	public static void requestPlayerIcon(int playerID, Consumer<String> callback) {
		Runnable task = new IconRequest(playerID, callback);
		Thread thread = new Thread(task);
		thread.start();
	}

	private static class IconRequest implements Runnable {

		private int id;
		private Consumer<String> result;

		IconRequest(int playerID, Consumer<String> callback) {
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
		}

		private void runWithExceptions() throws IOException, RuntimeException {
			LogHelper.finest("Attempting to retrieve URL");
			URL url = new URL(OGSReference.getAvatarURL(id));

			LogHelper.finest("Attempting connection");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");


			if ( conn.getResponseCode() != 200 ) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			StringBuilder sb = new StringBuilder();
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}

			LogHelper.finest(sb.toString());

			conn.disconnect();
		}
	}
}
