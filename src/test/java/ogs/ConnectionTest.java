package ogs;

import org.junit.Test;

import java.util.function.Consumer;

import static ogs.GameList.RequestOptions;
import static ogs.GameList.requestGameList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class ConnectionTest {

	private static final Object monitor = new Object();

	@Test
	public void gameConnection() throws Exception {
		GameListAcceptor acceptor = new GameListAcceptor();
		requestGameList(new RequestOptions(), acceptor);

		synchronized (monitor) {
			if ( !acceptor.called )
				monitor.wait(10000);
		}

		assertThat(acceptor.called, is(true));

		int id = -1;

		for (GameList.Game game : acceptor.list.getGames()) {
			id = game.getId();
			break;
		}

		assertThat(id, is(not(-1)));

		GameDataAcceptor dataAcceptor = new GameDataAcceptor();

		Connection.connectToGame(id, dataAcceptor, movedata -> {
		});

		synchronized (monitor) {
			if ( !dataAcceptor.called )
				monitor.wait(10000);
		}

		assertThat(dataAcceptor.called, is(true));

		Connection.disconnectGame(id);
	}

	class GameListAcceptor implements Consumer<GameList> {

		GameList list;
		boolean called = false;

		@Override
		public void accept(GameList gameList) {
			synchronized (monitor) {
				list = gameList;
				called = true;

				monitor.notifyAll();
			}
		}
	}

	class GameDataAcceptor implements Consumer<Gamedata> {

		boolean called = false;

		@Override
		public void accept(Gamedata gamedata) {
			synchronized (monitor) {
				called = true;

				monitor.notifyAll();
			}
		}
	}
}
