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

public interface GameTree {

	boolean isRoot();

	int getNumChildren();

	void stepForward(Move move);

	void stepBack();

	Move getLastMove();

	int getNumMoves();

	List<Move> getSequence();

	Board getPosition();

	Board getLastPosition();
}
