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

package logic;

import util.Coords;
import util.StoneColour;

import java.util.Collection;

public class Board {

	private ChainSet stones[];

	/*
	 * Contains the number of stones each player has captured. i.e. captures[BLACK] is the number of *white* stones
	 * that have been captured by *black*
	 */
	private int captures[];

	public Board() {
		captures = new int[StoneColour.values().length];
		stones = new ChainSet[StoneColour.values().length];

		for (StoneColour colour : StoneColour.values()) {
			captures[colour.ordinal()] = 0;
			stones[colour.ordinal()] = new ChainSet();
		}
	}

	public void playStone(Coords coords, StoneColour colour) {
		if ( isOccupied(coords) )
			throwIllegalMove(coords);

		ChainSet stones = getChainSet(colour);
		ChainSet otherStones = getChainSet(colour.other());

		if ( otherStones.chainIsCaptured(coords, stones) )
			addCaptures(otherStones.captureStones(coords, stones), colour);

		stones.add(coords);
	}

	private void addCaptures(int number, StoneColour colour) {
		captures[colour.ordinal()] += number;
	}

	public boolean isOccupied(Coords coords) {
		return getChainSet(StoneColour.BLACK).contains(coords) || getChainSet(StoneColour.WHITE).contains(coords);
	}

	private ChainSet getChainSet(StoneColour colour) {
		return stones[colour.ordinal()];
	}

	@Deprecated
	public boolean isLegalMove(Coords move, StoneColour colour) {
		return false;
	}

	public boolean isSuicide(Coords coords, StoneColour colour) {
		return getChainSet(colour).isSuicide(coords, getChainSet(colour.other()));
	}

	private void throwIllegalMove(Coords coords) {
		throw new IllegalArgumentException(coords.toString() + " is an illegal move.");
	}

	Chain getChainAtCoords(Coords coords) {
		for (StoneColour colour : StoneColour.values())
			if ( getChainSet(colour).contains(coords) )
				return getChainSet(colour).getChainFromStone(coords);

		return null;
	}

	@Override
	public boolean equals(Object other) {
		if ( this == other )
			return true;
		else if ( !(other instanceof Board) )
			return false;
		else {
			Board compare = (Board) other;
			boolean equals = this.getStones(StoneColour.BLACK).containsAll(compare.getStones(StoneColour.BLACK));
			equals &= compare.getStones(StoneColour.BLACK).containsAll(this.getStones(StoneColour.BLACK));

			equals &= this.getStones(StoneColour.WHITE).containsAll(compare.getStones(StoneColour.WHITE));
			equals &= compare.getStones(StoneColour.WHITE).containsAll(this.getStones(StoneColour.WHITE));

			equals &= this.getCaptures(StoneColour.BLACK) == compare.getCaptures(StoneColour.BLACK);
			equals &= this.getCaptures(StoneColour.WHITE) == compare.getCaptures(StoneColour.WHITE);

			return equals;
		}
	}

	public Collection<Coords> getStones(StoneColour colour) {
		return getChainSet(colour).getStones();
	}

	public int getCaptures(StoneColour colour) {
		return captures[colour.ordinal()];
	}
}
