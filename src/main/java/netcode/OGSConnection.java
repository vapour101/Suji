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

package netcode;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;

public class OGSConnection {

	private static final Object monitor = new Object();
	private static final String GAMELIST = "gamelist/query";
	private static OGSConnection instance = null;
	private Socket connection;

	private OGSConnection() {

		URI prefix = getPrefix();
		if ( prefix == null )
			return;

		IO.Options options = new IO.Options();
		options.reconnection = true;
		options.reconnectionDelay = 500;
		options.reconnectionDelayMax = 60000;
		options.transports = new String[]{"websocket"};

		connection = IO.socket(prefix, options);

		connection.on(Socket.EVENT_CONNECT, this::onConnect);

		connection.connect();
	}

	private URI getPrefix() {
		URI prefix = null;

		try {
			prefix = new URI("https://online-go.com");
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}

		return prefix;
	}

	public static synchronized void disconnect() {
		if ( getInstance().connection.connected() ) {
			getInstance().connection.disconnect();
		}
	}

	private static OGSConnection getInstance() {
		if ( instance == null ) {
			synchronized (monitor) {
				if ( instance == null )
					instance = new OGSConnection();
			}
		}

		return instance;
	}

	public static void requestGameList(Consumer<JSONObject> callback) {
		Thread thread = new Thread(() -> getConnectedInstance().gameListRequest(callback));
		thread.run();
	}

	private void gameListRequest(Consumer<JSONObject> callback) {
		JSONObject args = new JSONObject();

		try {
			args.put("list", "live");
			args.put("sort_by", "rank");
			args.put("from", 0);
			args.put("limit", 10);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}

		connection.emit(GAMELIST, args, (Ack) res -> {
			JSONObject gameList = (JSONObject) res[0];

			callback.accept(gameList);
		});
	}

	private static OGSConnection getConnectedInstance() {
		try {
			return getConnectedInstanceWithExceptions();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static OGSConnection getConnectedInstanceWithExceptions() throws InterruptedException {
		while (!getInstance().connection.connected()) {
			synchronized (monitor) {
				if ( !getInstance().connection.connected() ) {
					getInstance().connection.connect();

					monitor.wait();
				}
			}
		}

		return getInstance();
	}

	private void onConnect(Object... objects) {
		synchronized (monitor) {
			monitor.notifyAll();
		}
	}
}
