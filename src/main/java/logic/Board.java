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

import java.util.Set;

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

	public final Set<Coords> getBlackStones() {
		return blackStones.getStones();
	}

	public final Set<Coords> getWhiteStones() {
		return whiteStones.getStones();
	}

	public void playBlackStone(Coords coords) {
		if ( !isLegalBlackMove(coords) )
			throwIllegalMove(coords);

		if ( whiteStones.chainIsCaptured(coords, blackStones) )
			blackCaptures += whiteStones.captureStones(coords, blackStones);

		blackStones.add(coords);
	}

	public boolean isLegalBlackMove(Coords coords) {
		boolean isLegal;

		isLegal = !isOccupied(coords);
		isLegal &= !isBlackSuicide(coords);

		return isLegal;
	}

	private boolean isOccupied(Coords coords) {
		return blackStones.contains(coords) || whiteStones.contains(coords);
	}

	private boolean isBlackSuicide(Coords coords) {
		return blackStones.isSuicide(coords, whiteStones);
	}

	private void throwIllegalMove(Coords coords) {
		throw new IllegalArgumentException(coords.toString() + " is an illegal move.");
	}

	public void playWhiteStone(Coords coords) {
		if ( !isLegalWhiteMove(coords) )
			throwIllegalMove(coords);

		if ( blackStones.chainIsCaptured(coords, whiteStones) )
			whiteCaptures += blackStones.captureStones(coords, whiteStones);

		whiteStones.add(coords);
	}

	public boolean isLegalWhiteMove(Coords coords) {
		boolean isLegal;

		isLegal = !isOccupied(coords);
		isLegal &= !isWhiteSuicide(coords);

		return isLegal;
	}

	private boolean isWhiteSuicide(Coords coords) {
		return whiteStones.isSuicide(coords, blackStones);
	}

	public int getBlackCaptures() {
		return blackCaptures;
	}

	public int getWhiteCaptures() {
		return whiteCaptures;
	}

	protected Chain getChainAtCoords(Coords coords) {
		if ( blackStones.contains(coords) )
			return blackStones.getChainFromStone(coords);

		if ( whiteStones.contains(coords) )
			return whiteStones.getChainFromStone(coords);

		return null;
	}
}
