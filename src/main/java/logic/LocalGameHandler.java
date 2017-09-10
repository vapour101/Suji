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
import util.Move;
import util.StoneColour;

import java.util.Collection;

public class LocalGameHandler implements GameHandler {

	private GameTree gameTree;
	private int handicap;

	public LocalGameHandler() {
		this(0);
	}

	public LocalGameHandler(int handicap) {
		gameTree = new SimpleGameTree();
		this.handicap = handicap;
	}

	@Override
	public boolean isLegalMove(Move move) {
		boolean isLegal;

		isLegal = !gameTree.getPosition().isOccupied(move.getPosition());
		isLegal &= !gameTree.getPosition().isSuicide(move.getPosition(), move.getPlayer());

		if ( isLegal && gameTree.getMoveNumber() > 2 ) {
			Board previous = gameTree.getLastPosition();
			Board future = gameTree.getPosition();
			future.playStone(move.getPosition(), move.getPlayer());

			isLegal = !previous.equals(future);
		}

		return isLegal;
	}

	@Override
	public void playStone(Move move) {
		gameTree.playMove(move.getPosition(), move.getPlayer());
	}

	@Override
	public void undo() {
		gameTree.stepBack();
	}

	@Override
	public Collection<Coords> getStones(StoneColour colour) {
		return getBoard().getStones(colour);
	}

	@Override
	public Board getBoard() {
		return gameTree.getPosition();
	}

	@Override
	public StoneColour getTurnPlayer() {
		if ( gameTree.getMoveNumber() == 0 )
			return handicap == 0 ? StoneColour.BLACK : StoneColour.WHITE;

		return gameTree.getLastMove().getPlayer().other();
	}
}
