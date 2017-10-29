package ogs;

import org.junit.Test;

import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PlayerdataTest {

	private static final Object monitor = new Object();

	@Test
	public void requestPlayerdata() throws Exception {
		PlayerDataAcceptor acceptor = new PlayerDataAcceptor();
		Playerdata.requestPlayerdata(401046, acceptor);

		synchronized (monitor) {
			if ( !acceptor.called ) {
				monitor.wait(10000);
			}
		}

		assertThat(acceptor.called, is(true));
		assertThat(acceptor.playerdata.getUsername(), is("vapour101"));
	}


	static class PlayerDataAcceptor implements Consumer<Playerdata> {

		Playerdata playerdata;
		boolean called = false;

		@Override
		public void accept(Playerdata playerdata) {
			synchronized (monitor) {
				called = true;
				this.playerdata = playerdata;

				monitor.notifyAll();
			}
		}
	}
}