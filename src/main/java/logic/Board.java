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

	private ChainSet blackStones;
	private ChainSet whiteStones;

	private int blackCaptures;
	private int whiteCaptures;

	public Board() {
		blackCaptures = 0;
		whiteCaptures = 0;
		blackStones = new ChainSet();
		whiteStones = new ChainSet();
	}

	public Collection<Coords> getStones(StoneColour colour) {
		return getChainSet(colour).getStones();
	}

	private ChainSet getChainSet(StoneColour colour) {
		if ( colour == StoneColour.BLACK )
			return blackStones;
		else
			return whiteStones;
	}

	public void playStone(Coords coords, StoneColour colour) {
		if ( !isLegalMove(coords, colour) )
			throwIllegalMove(coords);

		ChainSet stones = getChainSet(colour);
		ChainSet otherStones = getChainSet(colour.other());

		if ( otherStones.chainIsCaptured(coords, stones) )
			addCaptures(otherStones.captureStones(coords, stones), colour);

		stones.add(coords);
	}

	private void addCaptures(int captures, StoneColour colour) {
		if ( colour == StoneColour.BLACK )
			blackCaptures += captures;
		else
			whiteCaptures += captures;
	}

	public boolean isLegalMove(Coords coords, StoneColour colour) {
		boolean isLegal;

		isLegal = !isOccupied(coords);
		isLegal &= !isSuicide(colour, coords);

		return isLegal;
	}


	private boolean isOccupied(Coords coords) {
		return blackStones.contains(coords) || whiteStones.contains(coords);
	}

	private boolean isSuicide(StoneColour colour, Coords coords) {
		return getChainSet(colour).isSuicide(coords, getChainSet(colour.other()));
	}

	private void throwIllegalMove(Coords coords) {
		throw new IllegalArgumentException(coords.toString() + " is an illegal move.");
	}

	public int getCaptures(StoneColour colour) {
		if ( colour == StoneColour.BLACK )
			return blackCaptures;
		else
			return whiteCaptures;
	}

	Chain getChainAtCoords(Coords coords) {
		if ( blackStones.contains(coords) )
			return blackStones.getChainFromStone(coords);

		if ( whiteStones.contains(coords) )
			return whiteStones.getChainFromStone(coords);

		return null;
	}
}
