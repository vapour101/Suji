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

package logic.gametree;

import logic.board.Board;
import util.Move;

import java.util.LinkedList;
import java.util.List;

public class SimpleGameTree implements GameTree {

	private LinkedList<Move> moveList;

	public SimpleGameTree() {
		moveList = new LinkedList<>();
	}

	@Override
	public boolean isRoot() {
		return moveList.size() < 2;
	}

	@Override
	public int getNumChildren() {
		return 0;
	}

	@Override
	public void stepForward(Move move) {
		moveList.add(move);
	}

	@Override
	public void stepBack() {
		moveList.removeLast();
	}

	@Override
	public Move getLastMove() {
		return moveList.getLast();
	}

	@Override
	public int getNumMoves() {
		return moveList.size();
	}

	@Override
	public List<Move> getSequence() {
		return moveList;
	}

	@Override
	public Board getPosition() {
		return getPositionAt(moveList.size());
	}

	private Board getPositionAt(int moveNumber) {
		Board board = new Board();

		for (int i = 0; i < moveNumber; ++i) {
			if ( moveList.get(i).getType() == Move.Type.PLAY )
				board.playStone(moveList.get(i).getPosition(), moveList.get(i).getPlayer());
		}

		return board;
	}

	@Override
	public Board getLastPosition() {
		return getPositionAt(moveList.size() - 1);
	}
}
