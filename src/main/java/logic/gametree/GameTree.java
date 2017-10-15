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
import logic.gamehandler.GameHandler;
import util.Move;

import java.util.List;
import java.util.function.Consumer;

/**
 * Tracks the history of a Go game, possibly including variations, and iterate over it.
 */
public interface GameTree {

	/**
	 * @return Whether the iterator is at the root node.
	 */
	boolean isRoot();

	GameTreeIterator getRoot();

	/**
	 * @return The number of children to the current node pointed to by the GameTree's
	 * internal iterator.
	 */
	int getNumChildren();

	/**
	 * Move the iterator to a pre-existing child node.
	 *
	 * @param child The ordinal value of the child to move the iterator to.
	 */
	void stepForward(int child);

	/**
	 * Move the iterator to the child matching the argument, if no such child exists, create it.
	 *
	 * @param move The next move.
	 */
	void stepForward(Move move);

	/**
	 * Move the iterator to the current node's parent. If we're at the root node, do nothing.
	 */
	void stepBack();

	/**
	 * @return The last move played in the tree, if the current node's parent doesn't correspond
	 * to a move, keep searching until we find one, if no moves have been played in the current
	 * sequence, return null.
	 */
	Move getLastMove();

	/**
	 * @return The number of moves played in the current sequence.
	 */
	int getNumMoves();

	/**
	 * @return All moves played between the root node and the iterator.
	 */
	List<Move> getSequence();

	/**
	 * @return The board corresponding to the current sequence pointed to by the iterator.
	 */
	Board getPosition();

	/**
	 * @return The last board position, as if we called stepBack() followed by getPosition(), but
	 * without moving the iterator.
	 */
	Board getLastPosition();
}
