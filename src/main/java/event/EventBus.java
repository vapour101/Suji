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

import java.util.Collection;

public final class EventBus implements EventTarget {

	private static EventBus instance = null;
	private Multimap<EventType, EventHandler> handlers;

	private EventBus() {
		handlers = HashMultimap.create();
	}

	public static <T extends Event> void addEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
		getInstance().handlers.put(eventType, eventHandler);
	}

	public static EventBus getInstance() {
		if ( instance == null )
			instance = new EventBus();

		return instance;
	}

	public static <T extends Event> void removeEventHandler(EventType<T> eventType,
															EventHandler<? super T> eventHandler) {
		getInstance().handlers.remove(eventType, eventHandler);
	}

	public void fireEvent(Event event) {
		Event.fireEvent(this, event);
	}

	@Override
	public final EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
		return tail.prepend(this::dispatchEvent);
	}

	private Event dispatchEvent(Event event, EventDispatchChain tail) {
		EventType type = event.getEventType();
		while (type != Event.ANY) {
			handleEvent(event, handlers.get(type));
			type = type.getSuperType();
		}
		handleEvent(event, handlers.get(Event.ANY));
		return event;
	}

	private void handleEvent(Event event, Collection<EventHandler> handlers) {
		if ( handlers != null ) {
			handlers.forEach(handler -> handler.handle(event));
		}
	}
}
