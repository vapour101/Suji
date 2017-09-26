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

package util;

import javafx.scene.image.Image;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.glassfish.jersey.apache.connector.ApacheClientProperties;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.function.Consumer;

public class WebHelper {

	public static final String PROXY_PROTOCOL = "http://";
	public static final String PROXY_SERVER = "";
	public static final String PROXY_DOMAIN = "";
	public static final String PROXY_USER = "";
	public static final String PROXY_PASS = "";
	public static final boolean USE_PROXY = false;

	private static Client client = null;

	public static void requestImage(String url, Consumer<Image> callback) {
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

	private static Client getClient() {
		if ( client == null )
			synchronized (WebHelper.class) {
				if ( client == null )
					buildClient();
			}

		return client;
	}

	private static void buildClient() {
		if ( USE_PROXY ) {
			ClientConfig config = new ClientConfig();
			config.connectorProvider(new ApacheConnectorProvider());
			CredentialsProvider credentials = new BasicCredentialsProvider();
			credentials.setCredentials(AuthScope.ANY, new NTCredentials(PROXY_USER, PROXY_PASS, null, PROXY_DOMAIN));
			config.property(ApacheClientProperties.CREDENTIALS_PROVIDER, credentials);
			config.property(ClientProperties.PROXY_URI, PROXY_PROTOCOL + PROXY_SERVER);

			client = ClientBuilder.newClient(config);
		}
		else
			client = ClientBuilder.newClient();
	}

	public static void requestJSON(String url, Consumer<JSONObject> callback) {
		Thread thread = new Thread(() -> {
			WebTarget target = getClient().target(url);
			Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();

			if ( response.getStatus() != 200 ) {
				LogHelper.severe("Failed : HTTP error code : " + response.getStatus());
			}

			String output = response.readEntity(String.class);
			response.close();

			try {
				JSONObject jsonObject = new JSONObject(output);
				callback.accept(jsonObject);
			}
			catch (JSONException e) {
				LogHelper.jsonError(e);
			}
		});
		thread.start();
	}
}
