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
import javafx.event.EventTarget;
import javafx.event.EventType;
import logic.Board;
import logic.GameHandler;
import util.Coords;
import util.StoneColour;

import java.util.Collection;

public class GameEvent extends Event {

	public static final EventType<GameEvent> ANY = new EventType<GameEvent>("GAME");

	private GameHandler handler;

	public GameEvent(GameHandler source, EventTarget target) {
		this(source, target, ANY);
	}

	public GameEvent(GameHandler source, EventTarget target, EventType<? extends GameEvent> eventType) {
		super(source, target, eventType);
		handler = source;
	}

	public Board getBoard() {
		return handler.getBoard();
	}

	public Collection<Coords> getStones(StoneColour colour) {
		return handler.getStones(colour);
	}
}
