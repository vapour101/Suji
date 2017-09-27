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

import javafx.event.EventTarget;
import javafx.event.EventType;
import logic.gamehandler.GameHandler;
import util.DrawCoords;

public class HoverEvent extends GameEvent {

	public static final EventType<HoverEvent> HOVER = new EventType<>(GAME, "HOVER");

	private DrawCoords point;

	public HoverEvent(GameHandler source, DrawCoords location, EventTarget target) {
		super(source, target, HOVER);
		point = location;
	}

	public DrawCoords getPoint() {
		return point;
	}
}
