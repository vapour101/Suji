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

import java.util.LinkedList;

public class GameTree {

	LinkedList<Move> moveList;

	GameTree() {
		moveList = new LinkedList<>();
	}

	public void playMove(Coords coords, StoneColour colour) {
		Move move = new Move(coords, colour);
		moveList.add(move);
	}

	public Board getPosition() {
		return getPositionAt(moveList.size());
	}

	public void stepBack() {
		moveList.removeLast();
	}

	private class Move {

		public Coords coords;
		public StoneColour colour;

		Move(Coords coords, StoneColour colour) {
			this.coords = coords;
			this.colour = colour;
		}
	}

	public Board getPositionAt(int moveNumber) {
		Board board = new Board();

		for (int i = 0; i < moveNumber; ++i) {
			board.playStone(moveList.get(i).coords, moveList.get(i).colour);
		}

		return board;
	}

	public int getNumberOfMoves() {
		return moveList.size();
	}

	public Board getLastPosition() {
		return getPositionAt(moveList.size() - 1);
	}
}
