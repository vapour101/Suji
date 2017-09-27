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
public class GamePublisherTest {

	@Mock
	private EventHandler<SujiEvent> handler;

	private GamePublisher publisher;

	@Before
	public void init() {
		publisher = new GamePublisher(new LocalGame(0));
	}

	@Test
	public void generateEvents() {
		publisher.subscribe(SujiEvent.ANY, handler);

		verifyZeroInteractions(handler);

		publisher.pass();

		verify(handler).handle(any());
		verifyNoMoreInteractions(handler);
	}

	@Test
	public void specificEventsOnly() {
		publisher.subscribe(GameEvent.PASS, handler);

		publisher.pass();

		verify(handler).handle(any());

		verifyNoMoreInteractions(handler);

		publisher.playMove(play(getCoords("K10"), BLACK));
	}

	@Test
	public void invalidEvent() {
		Event event = new Event(publisher, publisher, Event.ANY);
		Event.fireEvent(publisher, event);
	}

	@Test
	public void unsubscribe() {
		publisher.subscribe(GameEvent.UNDO, handler);

		publisher.undo();

		verify(handler).handle(any());
		verifyNoMoreInteractions(handler);

		publisher.unsubscribe(GameEvent.UNDO, handler);
		publisher.undo();
	}
}