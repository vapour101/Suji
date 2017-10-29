package ogs;

import ogs.GameList.RequestOptions;
import org.junit.Test;

import java.util.function.Consumer;

import static ogs.GameList.GameListType.CORRESPONDENCE;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GameListTest {

	private static final Object monitor = new Object();

	@Test
	public void requestLiveGameList() throws Exception {
		RequestOptions options = new RequestOptions();
		options.setNumGames(10);

		GameListAcceptor callback = new GameListAcceptor();

		GameList.requestGameList(options, callback);

		synchronized (monitor) {
			monitor.wait(10000);
		}

		assertThat(callback.called, is(true));
	}

	@Test
	public void requestCorrespondenceGameList() throws Exception {
		RequestOptions options = new RequestOptions();
		options.setNumGames(10);
		options.setType(CORRESPONDENCE);

		GameListAcceptor callback = new GameListAcceptor();

		GameList.requestGameList(options, callback);

		synchronized (monitor) {
			if ( !callback.called )
				monitor.wait(10000);
		}

		assertThat(callback.called, is(true));
	}

	static class GameListAcceptor implements Consumer<GameList> {

		public GameList list;
		public boolean called = false;

		@Override
		public void accept(GameList gameList) {
			synchronized (monitor) {
				list = gameList;
				called = true;

				monitor.notifyAll();
			}
		}
	}
}