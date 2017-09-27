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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import javafx.event.*;
import logic.gamehandler.GameHandler;
import logic.gamehandler.GameHandlerDecorator;
import util.Move;

import java.util.Collection;

public class GamePublisher extends GameHandlerDecorator implements EventTarget {

	private Multimap<EventType, EventHandler> handlers;

	public GamePublisher(GameHandler gameHandler) {
		super(gameHandler);

		handlers = HashMultimap.create();
	}

	public <T extends SujiEvent> void subscribe(EventType<T> eventType, EventHandler<? super T> eventHandler) {
		handlers.put(eventType, eventHandler);
	}

	public <T extends SujiEvent> void unsubscribe(EventType<T> eventType, EventHandler<? super T> eventHandler) {
		handlers.remove(eventType, eventHandler);
	}

	@Override
	public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
		return tail.prepend(this::dispatchEvent);
	}

	private Event dispatchEvent(Event event, EventDispatchChain tail) {
		if ( !(event instanceof SujiEvent) )
			return null;

		EventType type = event.getEventType();

		while (type != Event.ANY) {
			handleEvent(event, handlers.get(type));
			type = type.getSuperType();
		}

		handleEvent(event, handlers.get(Event.ANY));
		return event;
	}

	private void handleEvent(Event event, Collection<EventHandler> handlers) {
		handlers.forEach(handler -> handler.handle(event));
	}

	@Override
	public void pass() {
		super.pass();

		fireEvent(GameEvent.PASS);
	}

	@Override
	public void undo() {
		super.undo();

		fireEvent(GameEvent.UNDO);
	}

	@Override
	public void playMove(Move move) {
		super.playMove(move);

		fireEvent(GameEvent.MOVE);
	}

	private synchronized void fireEvent(EventType<? extends GameEvent> eventType) {
		GameEvent event = new GameEvent(this, this, eventType);
		Event.fireEvent(this, event);
	}
}
