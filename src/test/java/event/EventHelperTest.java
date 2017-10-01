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

import javafx.event.Event;
import javafx.event.EventHandler;
import logic.gamehandler.GameHandler;
import logic.gamehandler.LocalGame;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static util.Coords.getCoords;
import static util.Move.play;
import static util.StoneColour.BLACK;

@RunWith (MockitoJUnitRunner.StrictStubs.class)
public class EventHelperTest {

	@Mock
	private EventHandler<SujiEvent> handler;

	private GameHandler game;

	@Before
	public void init() {
		game = new LocalGame(0);
	}

	@Test
	public void generateEvents() {
		game.subscribe(GameEvent.GAME, handler);

		verifyZeroInteractions(handler);

		game.pass();

		verify(handler).handle(any());
		verifyNoMoreInteractions(handler);
	}

	@Test
	public void specificEventsOnly() {
		game.subscribe(GameEvent.PASS, handler);

		game.pass();

		verify(handler).handle(any());

		verifyNoMoreInteractions(handler);

		game.playMove(play(getCoords("K10"), BLACK));
	}

	@Test
	public void invalidEvent() {
		Event event = new Event(game, game, Event.ANY);
		Event.fireEvent(game, event);
	}

	@Test
	public void unsubscribe() {
		game.subscribe(GameEvent.UNDO, handler);

		game.undo();

		verify(handler).handle(any());
		verifyNoMoreInteractions(handler);

		game.unsubscribe(GameEvent.UNDO, handler);
		game.undo();
	}
}