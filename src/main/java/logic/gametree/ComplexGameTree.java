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

import logic.Board;
import util.Move;

import java.util.List;

public class ComplexGameTree implements GameTree {


	@Override
	public boolean isRoot() {
		return false;
	}

	@Override
	public int getNumChildren() {
		return 0;
	}

	@Override
	public void stepForward(Move move) {

	}

	@Override
	public void stepBack() {

	}

	@Override
	public Move getLastMove() {
		return null;
	}

	@Override
	public int getNumMoves() {
		return 0;
	}

	@Override
	public List<Move> getSequence() {
		return null;
	}

	@Override
	public Board getPosition() {
		return null;
	}

	@Override
	public Board getLastPosition() {
		return null;
	}

	private class GameNode {

		private Move move;
	}
}
