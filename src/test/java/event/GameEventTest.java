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

package event;

import logic.GameHandler;
import logic.LocalGameHandler;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static util.Coords.getCoords;
import static util.Move.play;
import static util.StoneColour.BLACK;
import static util.StoneColour.WHITE;

public class GameEventTest {

	@Test
	public void gameEvent() {
		GameHandler game = new LocalGameHandler(0);

		GameEventConsumer dummy = new GameEventConsumer();
		EventBus.addEventHandler(GameEvent.ANY, dummy::consume);

		EventBus.getInstance().fireEvent(new GameEvent(game, EventBus.getInstance()));
		assertThat(dummy.hits, is(1));

		GameEvent.fireGameEvent(game, GameEvent.START);
		GameEvent.fireGameEvent(game, GameEvent.GAMEOVER);
		assertThat(dummy.hits, is(3));

		dummy = new GameEventConsumer();

		GameEvent.fireGameEvent(game, GameEvent.ANY);
		assertThat(dummy.hits, is(0));

		EventBus.addEventHandler(GameEvent.START, dummy::consume);

		GameEvent.fireGameEvent(game, GameEvent.GAMEOVER);
		GameEvent.fireGameEvent(game, GameEvent.ANY);
		GameEvent.fireGameEvent(game, GameEvent.START);
		assertThat(dummy.hits, is(1));
	}

	@Test
	public void boardData() {
		GameHandler game = new LocalGameHandler(0);
		game.playMove(play(getCoords("D4"), BLACK));
		game.playMove(play(getCoords("E5"), WHITE));
		game.playMove(play(getCoords("C4"), BLACK));
		game.playMove(play(getCoords("F8"), WHITE));
		game.playMove(play(getCoords("G9"), BLACK));
		game.playMove(play(getCoords("M10"), WHITE));

		GameEventConsumer dummy = new GameEventConsumer();
		EventBus.addEventHandler(GameEvent.ANY, dummy::consume);

		GameEvent.fireGameEvent(game, GameEvent.ANY);
		assertThat(dummy.gameEvent.getBoard(), is(game.getBoard()));
		assertThat(dummy.gameEvent.getStones(BLACK), is(game.getStones(BLACK)));
	}

	private class GameEventConsumer {

		public int hits = 0;
		public GameEvent gameEvent;

		public void consume(GameEvent event) {
			hits++;
			gameEvent = event;
		}
	}
}
