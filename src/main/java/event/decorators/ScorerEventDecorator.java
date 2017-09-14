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

package event.decorators;

import event.ScoreEvent;
import logic.score.Scorer;
import logic.score.ScorerDecorator;
import util.Coords;

import java.util.Collection;

import static util.StoneColour.BLACK;
import static util.StoneColour.WHITE;

public class ScorerEventDecorator extends ScorerDecorator {

	public ScorerEventDecorator(Scorer scorer) {
		super(scorer);

		fireScoreEvent();
	}

	private void fireScoreEvent() {
		ScoreEvent.fireScoreEvent(this);
	}

	@Override
	public void markGroupDead(Coords coords) {
		Collection<Coords> oldBlack = getDeadStones(BLACK);
		Collection<Coords> oldWhite = getDeadStones(WHITE);

		super.markGroupDead(coords);

		Collection<Coords> newBlack = getDeadStones(BLACK);
		Collection<Coords> newWhite = getDeadStones(WHITE);

		if ( !collectionsEqual(oldBlack, newBlack) || !collectionsEqual(oldWhite, newWhite) )
			fireScoreEvent();
	}

	private boolean collectionsEqual(Collection<Coords> lhs, Collection<Coords> rhs) {
		boolean result = lhs.containsAll(rhs);
		result &= rhs.containsAll(lhs);

		return result;
	}

	@Override
	public void unmarkGroupDead(Coords coords) {
		Collection<Coords> oldBlack = getDeadStones(BLACK);
		Collection<Coords> oldWhite = getDeadStones(WHITE);

		super.unmarkGroupDead(coords);

		Collection<Coords> newBlack = getDeadStones(BLACK);
		Collection<Coords> newWhite = getDeadStones(WHITE);

		if ( !collectionsEqual(oldBlack, newBlack) || !collectionsEqual(oldWhite, newWhite) )
			fireScoreEvent();
	}
}
