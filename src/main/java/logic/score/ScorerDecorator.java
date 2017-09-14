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

import util.Coords;
import util.StoneColour;

import java.util.Set;

public class ScorerDecorator implements Scorer {

	private Scorer instance;

	protected ScorerDecorator(Scorer scorer) {
		instance = scorer;
	}

	@Override
	public double getScore() {
		return instance.getScore();
	}

	@Override
	public double getScore(StoneColour colour) {
		return instance.getScore(colour);
	}

	@Override
	public void markGroupDead(Coords coords) {
		instance.markGroupDead(coords);
	}

	@Override
	public void unmarkGroupDead(Coords coords) {
		instance.unmarkGroupDead(coords);
	}

	@Override
	public Set<Coords> getDeadStones(StoneColour colour) {
		return instance.getDeadStones(colour);
	}

	@Override
	public Set<Coords> getTerritory(StoneColour colour) {
		return instance.getTerritory(colour);
	}
}
