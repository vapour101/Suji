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

package ogs.web;

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
			e.printStackTrace();
		}

		return prefix;
	}

	public static synchronized void disconnect() {
		if ( getInstance().connection.connected() ) {
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

	public static void connectToGame(GameMeta game,
									 Consumer<JSONObject> gamedataConsumer,
									 Consumer<JSONObject> moveConsumer) {
		Thread thread = new Thread(() -> getConnectedInstance().gameConnection(game, gamedataConsumer, moveConsumer));
		thread.run();
	}

	private void gameConnection(GameMeta game,
								Consumer<JSONObject> gamedataConsumer,
								Consumer<JSONObject> moveConsumer) {
		connection.on(getGamedataEvent(game), new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				argsToJSON(gamedataConsumer, args);
			}
		});

		connection.on(getGameMoveEvent(game), new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				argsToJSON(moveConsumer, args);
			}
		});

		JSONObject options = getGameConnectOptions(game);

		connection.emit(GAMECONNECT, options);
	}

	private JSONObject getGameConnectOptions(GameMeta game) {
		JSONObject options = new JSONObject();

		try {
			options.put("game_id", game.getId());
			options.put("chat", false);
		}
		catch (JSONException e) {
			LogHelper.jsonError(e);
		}

		return options;
	}

	private void argsToJSON(Consumer<JSONObject> consumer, Object... args) {
		JSONObject jsonObject = (JSONObject) args[0];
		consumer.accept(jsonObject);
	}

	private String getGamedataEvent(GameMeta game) {
		return "game/" + game.getId() + "/gamedata";
	}

	private String getGameMoveEvent(GameMeta game) {
		return "game/" + game.getId() + "/move";
	}

	private static Connection getConnectedInstance() {
		try {
			return getConnectedInstanceWithExceptions();
		}
		catch (InterruptedException e) {
			LogHelper.log(Level.SEVERE, "Interrupted", e);
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

		LogHelper.finest("Returning connected instance.");
		return getInstance();
	}

	public static void getGameList(JSONObject args, Consumer<JSONObject> callback) {
		Thread thread = new Thread(() -> getConnectedInstance().gameListQuery(args, callback));
		thread.run();
	}

	private void gameListQuery(JSONObject args, Consumer<JSONObject> callback) {
		connection.emit(GAMELIST, args, (Ack) res -> argsToJSON(callback, res));
	}

	private void onConnect(Object... objects) {
		synchronized (monitor) {
			monitor.notifyAll();
		}
	}
}
