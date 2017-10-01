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
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EventPublisher {

	private final Lock mutex = new ReentrantLock(true);
	private Multimap<EventType, EventHandler> handlers;
	private EventTarget target;

	public EventPublisher(EventTarget owner) {
		this.target = owner;

		handlers = HashMultimap.create();
	}

	public <T extends SujiEvent> void subscribe(EventType<T> eventType, EventHandler<? super T> eventHandler) {
		mutex.lock();
		handlers.put(eventType, eventHandler);
		mutex.unlock();
	}

	public <T extends SujiEvent> void unsubscribe(EventType<T> eventType, EventHandler<? super T> eventHandler) {
		mutex.lock();
		handlers.remove(eventType, eventHandler);
		mutex.unlock();
	}

	public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
		return tail.prepend(this::dispatchEvent);
	}

	private Event dispatchEvent(Event event, EventDispatchChain tail) {
		if ( !(event instanceof SujiEvent) )
			return null;

		EventType type = event.getEventType();

		while (type != SujiEvent.ANY) {
			handleEvent(event, handlers.get(type));
			type = type.getSuperType();
		}

		handleEvent(event, handlers.get(SujiEvent.ANY));
		return null;
	}

	private void handleEvent(Event event, Collection<EventHandler> eventHandlers) {
		Collection<EventHandler> handlers = new LinkedList<>();

		mutex.lock();
		handlers.addAll(eventHandlers);
		mutex.unlock();

		handlers.forEach(handler -> handler.handle(event));
	}

	public synchronized <T extends SujiEvent> void fireEvent(T event) {
		Event.fireEvent(target, event);
	}
}
