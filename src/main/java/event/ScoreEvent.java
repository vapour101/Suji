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
import logic.score.Scorer;
import util.Coords;
import util.StoneColour;

import java.util.Collection;

public class ScoreEvent extends Event {

	public static final EventType<ScoreEvent> ANY = new EventType<ScoreEvent>("SCORE");
	public static final EventType<ScoreEvent> DONE = new EventType<ScoreEvent>(ANY, "DONE");

	private Scorer scorer;

	public ScoreEvent(Scorer source, EventTarget eventTarget) {
		this(source, eventTarget, ANY);
	}

	public ScoreEvent(Scorer source, EventTarget eventTarget, EventType<? extends ScoreEvent> eventType) {
		super(source, eventTarget, eventType);
		scorer = source;
	}

	public double getScore(StoneColour colour) {
		return scorer.getScore(colour);
	}

	public Collection<Coords> getTerritory(StoneColour colour) {
		return scorer.getTerritory(colour);
	}

	public Collection<Coords> getDeadStones(StoneColour colour) {
		return scorer.getDeadStones(colour);
	}
}
