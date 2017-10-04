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

package logic.score;

import event.ScoreEvent;
import logic.gamehandler.GameHandler;
import util.Coords;

import java.util.Collection;

import static util.StoneColour.BLACK;
import static util.StoneColour.WHITE;

public class EventScorer extends ScorerDecorator {

	private GameHandler game;

	public EventScorer(Scorer scorer, GameHandler gameHandler) {
		super(scorer);
		game = gameHandler;
	}

	@Override
	public void markGroupDead(Coords coords) {
		Collection<Coords> oldDeadStones = getAllDeadStones();

		super.markGroupDead(coords);

		Collection<Coords> newDeadStones = getAllDeadStones();

		if ( !collectionsAreEqual(oldDeadStones, newDeadStones) )
			fireScoreEvent();
	}

	private boolean collectionsAreEqual(Collection<Coords> a, Collection<Coords> b) {
		return a.containsAll(b) && b.containsAll(a);
	}

	private void fireScoreEvent() {
		ScoreEvent event = new ScoreEvent(game, ScoreEvent.SCORE);
		game.fireEvent(event);
	}

	private Collection<Coords> getAllDeadStones() {
		Collection<Coords> deadStones = getDeadStones(BLACK);
		deadStones.addAll(getDeadStones(WHITE));

		return deadStones;
	}

	@Override
	public void unmarkGroupDead(Coords coords) {
		Collection<Coords> oldDeadStones = getAllDeadStones();

		super.unmarkGroupDead(coords);

		Collection<Coords> newDeadStones = getAllDeadStones();

		if ( !collectionsAreEqual(oldDeadStones, newDeadStones) )
			fireScoreEvent();
	}
}
