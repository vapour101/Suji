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

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;
import util.LogHelper;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;
import java.util.logging.Level;

public class Connection {

	private static final Object monitor = new Object();
	private static final String GAMELIST = "gamelist/query";
	private static final String GAMECONNECT = "game/connect";
	private static final String GAMEDISCONNECT = "game/disconnect";
	private static final String PREFIX = "https://online-go.com";
	private static final String WEBSOCKET = "websocket";

	private static Connection instance = null;
	private Socket connection;

	private Connection() {
		URI prefix = getPrefix();

		if ( prefix == null )
			return;

		IO.Options options = new IO.Options();
		options.reconnection = true;
		options.reconnectionDelay = 500;
		options.reconnectionDelayMax = 60000;
		options.transports = new String[]{WEBSOCKET};

		connection = IO.socket(prefix, options);

		connection.on(Socket.EVENT_CONNECT, this::onConnect);

		connection.connect();
	}

	private URI getPrefix() {
		URI prefix = null;

		try {
			prefix = new URI(PREFIX);
		}
		catch (URISyntaxException e) {
			LogHelper.log(Level.SEVERE, "Can't get valid URI for OGS", e);
		}

		return prefix;
	}

	public static synchronized void disconnect() {
		if ( instance != null && getInstance().connection.connected() ) {
			getInstance().connection.disconnect();
		}
	}

	private static Connection getInstance() {
		if ( instance == null ) {
			synchronized (monitor) {
				if ( instance == null )
					instance = new Connection();
			}
		}

		return instance;
	}

	static void disconnectGame(int gameId) {
		Thread thread = new Thread(() -> {
			Connection connection = getConnectedInstance();

			if ( connection == null ) {
				LogHelper.severe("Can't establish a connection.");
				return;
			}

			connection.gameDisconnect(gameId);
		});
		thread.run();
	}

	private void gameDisconnect(int gameId) {
		LogHelper.finest("Disconnecting from game: " + gameId);

		connection.off(getGamedataEvent(gameId));
		connection.off(getGameMoveEvent(gameId));

		JSONObject options = getGameDisconnectOptions(gameId);

		connection.emit(GAMEDISCONNECT, options);
	}

	private JSONObject getGameDisconnectOptions(int gameId) {
		JSONObject options = new JSONObject();

		try {
			options.put("game_id", gameId);
		}
		catch (JSONException e) {
			LogHelper.jsonError(e);
		}

		return options;
	}

	private String getGamedataEvent(int gameId) {
		return "game/" + gameId + "/gamedata";
	}

	private String getGameMoveEvent(int gameId) {
		return "game/" + gameId + "/move";
	}

	private static Connection getConnectedInstance() {
		try {
			return getConnectedInstanceWithExceptions();
		}
		catch (InterruptedException e) {
			LogHelper.log(Level.INFO, "Thread interrupted before a connection could be established.", e);
		}

		return null;
	}

	private static Connection getConnectedInstanceWithExceptions() throws InterruptedException {
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

	static void connectToGame(int gameId, Consumer<Gamedata> gamedataConsumer, Consumer<Movedata> moveConsumer) {
		Thread thread = new Thread(() -> {
			Connection connection = getConnectedInstance();

			if ( connection == null ) {
				LogHelper.severe("Can't establish a connection.");
				return;
			}

			connection.gameConnection(gameId, gamedataConsumer, moveConsumer);
		});
		thread.run();
	}

	private void gameConnection(int gameId, Consumer<Gamedata> gamedataConsumer, Consumer<Movedata> moveConsumer) {
		LogHelper.finest("Connecting to game: " + gameId);

		connection.on(getGamedataEvent(gameId), new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				argsToGamedata(gamedataConsumer, args);
			}
		});

		connection.on(getGameMoveEvent(gameId), new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				argsToMovedata(moveConsumer, args);
			}
		});

		JSONObject options = getGameConnectOptions(gameId);

		connection.emit(GAMECONNECT, options);
	}

	private JSONObject getGameConnectOptions(int gameId) {
		JSONObject options = getGameDisconnectOptions(gameId);

		try {
			options.put("chat", false);
		}
		catch (JSONException e) {
			LogHelper.jsonError(e);
		}

		return options;
	}

	private void argsToMovedata(Consumer<Movedata> consumer, Object... args) {
		argsToJSON(jsonObject -> consumer.accept(new Movedata(jsonObject)), args);
	}

	private void argsToGamedata(Consumer<Gamedata> consumer, Object... args) {
		argsToJSON(jsonObject -> consumer.accept(new Gamedata(jsonObject)), args);
	}

	private void argsToJSON(Consumer<JSONObject> consumer, Object... args) {
		JSONObject jsonObject = (JSONObject) args[0];
		consumer.accept(jsonObject);
	}

	static void getGameList(JSONObject args, Consumer<JSONObject> callback) {
		Thread thread = new Thread(() -> {
			Connection connection = getConnectedInstance();

			if ( connection == null ) {
				LogHelper.severe("Can't establish a connection.");
				return;
			}

			connection.gameListQuery(args, callback);
		});
		thread.run();
	}

	private void gameListQuery(JSONObject args, Consumer<JSONObject> callback) {
		LogHelper.finest("Requesting gamelist");
		connection.emit(GAMELIST, args, (Ack) res -> argsToJSON(callback, res));
		LogHelper.finest("Requested gamelist");
	}

	private void onConnect(Object... objects) {
		LogHelper.finest("Connection established with OGS");
		synchronized (monitor) {
			monitor.notifyAll();
		}
	}
}
